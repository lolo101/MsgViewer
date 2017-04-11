/*
 * msgparser - http://auxilii.com/msgparser
 * Copyright (C) 2007  Roman Kurmanowytsch
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package com.auxilii.msgparser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import org.apache.log4j.Logger;

/**
 * Main parser class that does the actual
 * parsing of the Outlook .msg file. It uses the 
 * <a href="http://poi.apache.org/poifs/">POI</a>
 * library for parsing the .msg container file
 * and is based on a description posted by
 * Peter Fiskerstrand at
 * <a href="http://www.fileformat.info/format/outlookmsg/">fileformat.info</a>.
 * <br /><br />
 * It parses the .msg file and stores the information
 * in a {@link Message} object. Attachments are
 * put into an {@link FileAttachment} object. Hence, please
 * keep in mind that the complete mail is held in the memory!
 * If an attachment is another .msg file, this
 * attachment is not processed as a normal attachment
 * but rather included as a {@link MsgAttachment}. This
 * attached mail is, again, a {@link Message} object 
 * and may have further attachments and so on.
 * <br /><br />
 * Note: this code has not been tested on a wide
 * range of .msg files. Use in production level
 * (as in any other level) at your own risk.
 * <br /><br />
 * Usage:
 * <br /><br />
 * <code>
 * MsgParser msgp = new MsgParser();<br />
 * Message msg = msgp.parseMsg("test.msg"); 
 * </code>
 * @author roman.kurmanowytsch
 */
public class MsgParser {
	protected static final Logger logger = Logger.getLogger(MsgParser.class.getName());
	
	/**
	 * Empty constructor.
	 */
	public MsgParser() {
	}
	
	/**
	 * Parses a .msg file provided in the specified file.
	 * 
	 * @param msgFile The .msg file.
	 * @return A {@link Message} object representing the .msg file.
	 * @throws IOException Thrown if the file could not be loaded or parsed.
	 * @throws UnsupportedOperationException Thrown if the .msg file cannot
	 *   be parsed correctly.
	 */
	public Message parseMsg(File msgFile) throws IOException, UnsupportedOperationException {
		return this.parseMsg(new FileInputStream(msgFile), true);
	}
	
	/**
	 * Parses a .msg file provided in the specified file.
         *
	 * @param msgFile The .msg file as a String path.
	 * @return A {@link Message} object representing the .msg file.
	 * @throws IOException Thrown if the file could not be loaded or parsed.
	 * @throws UnsupportedOperationException Thrown if the .msg file cannot
	 *   be parsed correctly.
	 */
	public Message parseMsg(String msgFile) throws IOException, UnsupportedOperationException {
		return this.parseMsg(new FileInputStream(msgFile), true);
	}

	/**
	 * Parses a .msg file provided by an input stream.
	 * 
	 * @param msgFileStream The .msg file as a InputStream.
	 * @return A {@link Message} object representing the .msg file.
	 * @throws IOException Thrown if the file could not be loaded or parsed.
	 * @throws UnsupportedOperationException Thrown if the .msg file cannot
	 *   be parsed correctly.
	 */
	public Message parseMsg(InputStream msgFileStream) throws IOException, UnsupportedOperationException {
	    return this.parseMsg(msgFileStream, true);
        }

	/**
	 * Parses a .msg file provided by an input stream.
	 * 
	 * @param msgFileStream The .msg file as a InputStream.
	 * @param closeStream Indicates whether the provided stream should
	 *   be closed after the message has been read.
	 * @return A {@link Message} object representing the .msg file.
	 * @throws IOException Thrown if the file could not be loaded or parsed.
	 * @throws UnsupportedOperationException Thrown if the .msg file cannot
	 *   be parsed correctly.
	 */
	public Message parseMsg(InputStream msgFileStream, boolean closeStream) throws IOException, UnsupportedOperationException {
		// the .msg file, like a file system, contains directories
		// and documents within this directories
		// we now gain access to the root node
		// and recursively go through the complete 'filesystem'.
		Message msg = null;
	        try {
		    POIFSFileSystem fs = new POIFSFileSystem(msgFileStream);
		    DirectoryEntry dir = fs.getRoot();
		    msg = new Message();
		    this.checkDirectoryEntry(dir, msg);
		} finally {
		    if (closeStream) {
			try {
			    msgFileStream.close();		    
			} catch(Exception e) {
			    // ignore
			}
		    }
		}
		return msg;
	}
	
	/**
	 * Recursively parses the complete .msg file with the
	 * help of the POI library. The parsed information is
	 * put into the {@link Message} object.
	 * 
	 * @param dir The current node in the .msg file.
	 * @param msg The resulting {@link Message} object.
	 * @throws IOException Thrown if the .msg file could not
	 *  be parsed.
	 * @throws UnsupportedOperationException Thrown if 
	 *  the .msg file contains unknown data.
	 */
	protected void checkDirectoryEntry(DirectoryEntry dir, Message msg) throws IOException, UnsupportedOperationException {
		
		// we iterate through all entries in the current directory
		for (Iterator<?> iter = dir.getEntries(); iter.hasNext(); ) {
		    Entry entry = (Entry) iter.next();
		    
		    // check whether the entry is either a directory entry
		    // or a document entry
		    
		    if (entry.isDirectoryEntry()) {
		    	
		    	DirectoryEntry de = (DirectoryEntry) entry;

                        logger.trace("subdir: " + de.getName());

		    	// attachments have a special name and
		    	// have to be handled separately at this point
			    if (de.getName().startsWith("__attach_version1.0")) {
			    	this.parseAttachment(de, msg, de.getName());
			    } else if (de.getName().startsWith("__recip_version1.0")) {
			    	// a recipient entry has been found (which is also a directory entry itself)
			    	this.checkRecipientDirectoryEntry(de, msg);
			    } else {
			    	// a directory entry has been found. this
			    	// node will be recursively checked
			    	this.checkDirectoryEntry(de, msg);
			    }
			    
		    } else if (entry.isDocumentEntry()) {
		    	
		    	// a document entry contains information about
		    	// the mail (e.g, from, to, subject, ...)
		    	DocumentEntry de = (DocumentEntry) entry;

                        logger.trace("entry: "  + de.getName() );

		    	// the data is accessed by getting an input stream
		    	// for the given document entry
		    	DocumentInputStream dstream = new DocumentInputStream(de);
		    	// analyze the document entry
		    	// (i.e., get class and data type)
		    	FieldInformation info = this.analyzeDocumentEntry(de);
		    	// create a Java object from the data provided
		    	// by the input stream. depending on the field
		    	// information, either a String or a byte[] will
		    	// be returned. other datatypes are not yet supported
		    	Object data = this.getData(dstream, info);
		    	
		    	logger.trace("  Document data: "+((data == null) ? "null" : data.toString()));
		    	// the data is written into the Message object
		    	msg.setProperty(info.getClazz(), data);
		    	
		    } else {
                        logger.info("what happend here? " + entry.getName());
		        // any other type is not supported
		    }
		}		
	}

	/**
	 * Parses a recipient directory entry which holds informations about one of possibly multiple recipients. 
	 * The parsed information is put into the {@link Message} object.
	 * 
	 * @param dir The current node in the .msg file.
	 * @param msg The resulting {@link Message} object.
	 * @throws IOException Thrown if the .msg file could not
	 *  be parsed.
	 * @throws UnsupportedOperationException Thrown if 
	 *  the .msg file contains unknown data.
	 */
	protected void checkRecipientDirectoryEntry(DirectoryEntry dir, Message msg) throws IOException, UnsupportedOperationException {
		
		RecipientEntry recipient = new RecipientEntry();
		
		// we iterate through all entries in the current directory
		for (Iterator<?> iter = dir.getEntries(); iter.hasNext(); ) {
			Entry entry = (Entry) iter.next();
			
			// check whether the entry is either a directory entry
			// or a document entry, while we are just interested in document entries on this level			
			if (entry.isDirectoryEntry()) {
				// not expected within a recipient entry
				
			} else if (entry.isDocumentEntry()) {
				
				// a document entry contains information about
				// the mail (e.g, from, to, subject, ...)
				DocumentEntry de = (DocumentEntry) entry;
				
				// the data is accessed by getting an input stream
				// for the given document entry
				DocumentInputStream dstream = new DocumentInputStream(de);
				// analyze the document entry
				// (i.e., get class and data type)
				FieldInformation info = this.analyzeDocumentEntry(de);
				// create a Java object from the data provided
				// by the input stream. depending on the field
				// information, either a String or a byte[] will
				// be returned. other datatypes are not yet supported
				Object data = this.getData(dstream, info);
				
				logger.trace("  Document data: "+((data == null) ? "null" : data.toString()));
				// the data is written into the Message object
				recipient.setProperty(info.getClazz(), data);
				
			} else {
				// any other type is not supported
			}
		}		
		
		//after all properties are set -> add recipient to msg object
		msg.addRecipient(recipient);
	}
	
	/**
	 * Reads the information from the InputStream and
	 * creates, based on the information in the
	 * {@link FieldInformation} object, either a String
	 * or a byte[] (e.g., for attachments) Object 
	 * containing this data.
	 * 
	 * @param dstream The InputStream of the Document Entry.
	 * @param info The field information that is needed to
	 *  determine the data type of the input stream.
	 * @return The String/byte[] object representing
	 *  the data.
	 * @throws IOException Thrown if the .msg file could not
	 *  be parsed.
	 * @throws UnsupportedOperationException Thrown if 
	 *  the .msg file contains unknown data.
	 */
	protected Object getData(DocumentInputStream dstream, FieldInformation info) throws IOException {
		// if there is no field information available, we simply
		// return null. in that case, we're not interested in the
		// data anyway
		if ((info == null) || (info.getType() == FieldInformation.UNKNOWN)) {
			return null;
		}
		// if the type is 001e (we know it is lower case
		// because analyzeDocumentEntry stores the type in
		// lower case), we create a String object from the data.
		// the encoding of the binary data is most probably
		// ISO-8859-1 (not pure ASCII).
		if (info.getType().equals("001e")) {
			// we put the complete data into a byte[] object...
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int read = -1;
			while ((read = dstream.read(buffer)) > 0) {
				baos.write(buffer, 0, read);
			}
			// ...and create a String object from it
/*
                        byte bytes[] = baos.toByteArray();

                        for( int i = 0; i < bytes.length; i++ )
                        {
                            System.out.print(String.format("%d %c,", (int)bytes[i], (char)bytes[i]));
                        }
                          System.out.println();
*/
			String text = new String(baos.toByteArray(), "ISO-8859-1");                        
			return text;			
		} else if (info.getType().equals("001f")) {
			// Unicode encoding with lowbyte followed by hibyte
			// Note: this is arcane guesswork, but it works
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int read = -1;
			while ((read = dstream.read(buffer)) > 0) {
				baos.write(buffer, 0, read);
			}
			byte[] bytes = baos.toByteArray();

                        String text = new String(bytes,"UTF-16LE");
			return text;
		} else if (info.getType().equals("0102")) {
			// the data is read into a byte[] object
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int read = -1;
			while ((read = dstream.read(buffer)) > 0) {
				baos.write(buffer, 0, read);
			}
			return baos.toByteArray();
                } else if (info.getType().equals("0000")) {
			// the data is read into a byte[] object
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int read = -1;

                        StringBuilder sb = new StringBuilder();

			while ((read = dstream.read(buffer)) > 0) {
                            for( int i = 0; i < read; i++ )
                            {
                                sb.append((int)buffer[i]);
                                sb.append(" ");
				// baos.write(buffer, 0, read);
                            }
			}
                        return sb.toString();
			// return baos.toByteArray();
		}
		
		// this should not happen		
		logger.trace("Unknown field type "+info.getType());
		return null;
	}
	
	/**
	 * Analyzes the {@link DocumentEntry} and returns
	 * a {@link FieldInformation} object containing the
	 * class (the field name, so to say) and type of
	 * the entry. 
	 * 
	 * @param de The {@link DocumentEntry} that should be examined.
	 * @return A {@link FieldInformation} object containing class
	 *  and type of the document entry or, if the entry is
	 *  not an interesting field, an empty {@link FieldInformation}
	 *  object containing {@link FieldInformation#UNKNOWN} class
	 *  and type.
	 */
	protected FieldInformation analyzeDocumentEntry(DocumentEntry de) {
    	String name = de.getName();
    	// we are only interested in document entries
    	// with names starting with __substg1.
    	logger.trace("Document entry: "+name);
    	String key = "__substg1.";
    	if (name.startsWith(key)) {
    		String clazz = FieldInformation.UNKNOWN;
    		String type = FieldInformation.UNKNOWN; 
    		try {
    			String val = name.substring(key.length()+2).toLowerCase();
    			// the first 4 digits of the remainder
    			// defines the field class (or field name)
    			// and the last 4 digits indicate the
    			// data type.
    			clazz = val.substring(0, 4);
    			type = val.substring(4);
    			logger.trace("  Found document entry: class="+clazz+", type="+type + " size: " + de.getSize());
    		} catch(RuntimeException re) {
    			logger.trace("Could not parse directory entry "+name + " " + re);
    		}
    		return new FieldInformation(clazz, type);
    	} else {
    		logger.trace("Ignoring entry with name "+name + " size: " + de.getSize());

                /*
                if( de.getSize() > 500 )
                {
                    return new FieldInformation("000","0000");
                }
                 */
    	}
    	// we are not interested in the field
    	// and return an empty FieldInformation object
    	return new FieldInformation();
	}
	
	/**
	 * Creates an {@link Attachment} object based on
	 * the given directory entry. The entry may either
	 * point to an attached file or to an
	 * attached .msg file, which will be added 
	 * as a {@link MsgAttachment} object instead.
	 * 
	 * @param dir The directory entry containing the attachment
	 *  document entry and some other document entries
	 *  describing the attachment (name, extension, mime type, ...)
	 * @param msg The {@link Message} object that this
	 *  attachment should be added to.
	 * @throws IOException Thrown if the attachment could
	 *  not be parsed/read.
	 */
	protected void parseAttachment(DirectoryEntry dir, Message msg, String directoryName) throws IOException {
		
		FileAttachment attachment = new FileAttachment();
		
		// iterate through all document entries
		for (Iterator<?> iter = dir.getEntries(); iter.hasNext(); ) {
		    Entry entry = (Entry)iter.next();
		    if (entry.isDocumentEntry()) {
		    	
		    	// the document entry may contain information
		    	// about the attachment
		    	DocumentEntry de = (DocumentEntry) entry;
		    	
		    	FieldInformation info = analyzeDocumentEntry(de);
		    	DocumentInputStream dstream = new DocumentInputStream(de);
		    	
		    	Object data = this.getData(dstream, info);
		    	String clazz = info.getClazz();

		    	// we provide the class and data of the document
		    	// entry to the attachment. the attachment implementation
		    	// has to know the semantics of the field names
		    	attachment.setProperty(clazz, data, de);

                        if( directoryName != null )
                        {
                            int index = directoryName.indexOf("#");
                            if( index > 0 )
                                attachment.setSubDir(directoryName.substring(index+1));
                        }

		    } else {

		    	// a directory within the attachment directory
		    	// entry  means that a .msg file is attached
		    	// at this point. we recursively parse
		    	// this .msg file and add it as a MsgAttachment
		    	// object to the current Message object.
		    	Message attachmentMsg = new Message();
		    	MsgAttachment msgAttachment = new MsgAttachment();
		    	msgAttachment.setMessage(attachmentMsg);
		    	msg.addAttachment(msgAttachment);
		    	
		    	this.checkDirectoryEntry((DirectoryEntry) entry, attachmentMsg);
		    }
		}

		// only if there was really an attachment, we
		// add this object to the Message object
    	if (attachment.getSize() > -1) {
    		msg.addAttachment(attachment);
    	}
    	
		
	}
}
