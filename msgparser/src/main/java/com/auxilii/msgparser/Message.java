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

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;

import org.apache.log4j.Logger;
import org.apache.poi.hmef.CompressedRTF;

/**
 * Class that represents a .msg file. Some
 * fields from the .msg file are stored in special
 * parameters (e.g., {@link #fromEmail}).
 * Attachments are stored in the property
 * {@link #attachments}). An attachment may be
 * of the type {@link MsgAttachment} which
 * represents another attached (encapsulated)
 * .msg object.
 *
 * @author roman.kurmanowytsch
 */
public class Message {
    private static final Logger LOGGER = Logger.getLogger(Message.class.getName());

    /**
     * The message class as defined in the .msg file.
     */
    private String messageClass = "IPM.Note";
    /**
     * The message Id.
     */
    private String messageId = null;
    /**
     * The address part of From: mail address.
     */
    private String fromEmail = null;
    /**
     * The name part of the From: mail address
     */
    private String fromName = null;
    /**
     * The address part of To: mail address.
     */
    private String toEmail = null;
    /**
     * The name part of the To: mail address
     */
    private String toName = null;
    /**
     * The mail's subject.
     */
    private String subject = null;
    /**
     * The normalized body text.
     */
    private String bodyText = null;
    /**
     * The displayed To: field
     */
    private String displayTo = null;
    /**
     * The displayed Cc: field
     */
    private String displayCc = null;
    /**
     * The displayed Bcc: field
     */
    private String displayBcc = null;

    /**
     * The body in RTF format (if available)
     */
    private String bodyRTF = null;

    /**
     * Email headers (if available)
     */
    private String headers = null;

    /**
     * Email Date
     */
    private Date date = null;

    /**
     * A list of all attachments (both {@link FileAttachment}
     * and {@link MsgAttachment}).
     */
    private List<Attachment> attachments = new ArrayList<>();
    /**
     * Contains all properties that are not
     * covered by the special properties.
     */
    private final Map<String,Object> properties = new HashMap<>();
    /**
     * A list containing all recipients for this message
     * (which can be set in the 'to:', 'cc:' and 'bcc:' field, respectively).
     */
    private List<RecipientEntry> recipients = new ArrayList<>();

    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }

    public void addRecipient(RecipientEntry recipient) {
        this.recipients.add(recipient);
    }


    /**
     * Sets the name/value pair in the {@link #properties}
     * map. Some properties are put into
     * special attributes (e.g., {@link #toEmail} when
     * the property name is '0076').
     *
     * @param name The property name (i.e., the class
     *  of the document entry).
     * @param value The value of the field.
     * @throws ClassCastException Thrown if the detected data
     *  type does not match the expected data type.
     */
    public void setProperty(String name, Object value) throws ClassCastException {

        if (name == null || value == null) {
            return;
        }
        name = name.intern();

        // we know that the name is lower case
        // because this is done in MsgParser.analyzeDocumentEntry
        if (name == "001a") {
            this.setMessageClass((String) value);
        } else if (name == "1035") {
            this.setMessageId((String) value);
        } else if (name == "0037") {
            this.setSubject((String) value);
        } else if (name == "0c1f") {
            this.setFromEmail((String) value);
        } else if (name == "0042") {
            this.setFromName((String) value);
        } else if (name == "0076") {
            this.setToEmail((String) value);
        } else if (name == "3001") {
            this.setToName((String) value);
        } else if (name == "0e04") {
            this.setDisplayTo((String) value);
        } else if (name == "0e03") {
            this.setDisplayCc((String) value);
        } else if (name == "0e02") {
            this.setDisplayBcc((String) value);
        } else if (name == "1000") {
                if (value instanceof String) {
                    this.setBodyText((String) value);
                } else if (value instanceof byte[]) {
                        this.setBodyText(new String((byte[]) value));
                } else {
                        LOGGER.info( "Unexpected body class: "+value.getClass().getName());
                    this.setBodyText(value.toString());
                }
        } else if (name == "1009") {
            // we simply try to decompress the RTF data
            // if it's not compressed, the utils class
            // is able to detect this anyway
            if (value instanceof byte[]) {
                byte[] compressedRTF = (byte[]) value;
                try {
                    byte[] decompressedRTF = new CompressedRTF().decompress(new ByteArrayInputStream(compressedRTF));
                    // is RTF always in ANSI encoding?
                    this.setBodyRTF(new String(decompressedRTF));
                } catch(Exception e) {
                    LOGGER.info( "Could not decompress RTF data "  +  e);
                }
            } else {
                LOGGER.info( "Unexpected data type "+value.getClass());
            }
        } else if (name == "007d") {
            // email headers
            String headers = (String) value;
            this.setHeaders(headers);
            // try to parse the date from the headers
            Date date = Message.getDateFromHeaders(headers);
            if (date != null) {
                this.setDate(date);
            }
        } else {
                    String res = null;
                    int len = 0;

                    if (LOGGER.isTraceEnabled())
                    {
                        if (value instanceof byte[]) {
                            byte[] compressedRTF = (byte[]) value;

                            len = compressedRTF.length;

                            try {
                                byte[] decompressedRTF = new CompressedRTF().decompress(new ByteArrayInputStream(compressedRTF));
                                // is RTF always in ANSI encoding?
                                res = new String(decompressedRTF);
                            } catch (Exception e) {
                                LOGGER.info("Could not decompress RTF data " + e);
                            }

                            if (res == null) {
                                res = new String(compressedRTF);
                            }
                        }

                        if (len > 100) {
                            LOGGER.trace("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx len: " + len);
                        }
                    }
                    LOGGER.trace("unknown name: " + name + " res: " + ( res != null ? res : ""));
        }

        // save all properties (incl. those identified above)
        this.properties.put(name, value);

        // other possible values (some are duplicates)
        // 0044: recv name
        // 004d: author
        // 0050: reply
        // 005a: sender
        // 0065: sent email
        // 0076: received email
        // 0078: repr. email
        // 0c1a: sender name
        // 0e04: to
        // 0e1d: subject normalized
        // 1046: sender email
        // 3003: email address
        // 1008 rtf sync
    }

    /**
     * Provides a short representation of this .msg object.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("From: ").append(this.createMailString(this.fromEmail, this.fromName)).append("\n");
        sb.append("To: ").append(this.createMailString(this.toEmail, this.toName)).append("\n");
        if (this.date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            sb.append("Date: ").append(formatter.format(this.date)).append("\n");
        }
        if (this.subject != null) {
            sb.append("Subject: ").append(this.subject).append("\n");
        }
        sb.append("").append(this.attachments.size()).append(" attachments.");
        return sb.toString();
    }

    /**
     * Provides all information of this message object.
     *
     * @return The full message information.
     */
    public String toLongString() {
        StringBuilder sb = new StringBuilder();
        sb.append("From: ").append(this.createMailString(this.fromEmail, this.fromName)).append("\n");
        sb.append("To: ").append(this.createMailString(this.toEmail, this.toName)).append("\n");
        if (this.date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            sb.append("Date: ").append(formatter.format(this.date)).append("\n");
        }
        if (this.subject != null) {
            sb.append("Subject: ").append(this.subject).append("\n");
        }
        sb.append("\n");
        if (this.bodyText != null) {
            sb.append(this.bodyText);
        }
        if (this.attachments.size() > 0) {
            sb.append("\n");
            sb.append("").append(this.attachments.size()).append(" attachments.\n");
            for (Attachment att : this.attachments) {
                sb.append(att.toString()).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Convenience method for creating
     * an email address expression (including
     * the name, the address, or both).
     *
     * @param mail The mail address.
     * @param name The name part of the address.
     * @return A combination of the name and address.
     */
    public String createMailString(String mail, String name) {
        if (mail == null && name == null) {
            return null;
        }
        if (name == null) {
            return mail;
        }
        if (mail == null) {
            return name;
        }
        if (mail.equalsIgnoreCase(name)) {
            return mail;
        }
        return "\""+name+"\" <"+mail+">";
    }


    /**
     * @return the attachments
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }


    /**
     * @param attachments the attachments to set
     */
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * @return the recipients
     */
    public List<RecipientEntry> getRecipients() {
        return recipients;
    }

    /**
     * @param recipients the recipients to set
     */
    public void setRecipients(List<RecipientEntry> recipients) {
        this.recipients = recipients;
    }


    /**
     * @return the bodyText
     */
    public String getBodyText() {
        return bodyText;
    }


    /**
     * @param bodyText the bodyText to set
     */
    public void setBodyText(String bodyText) {
        this.bodyText = nullOrTrim(bodyText);
    }


    /**
     * @return the fromEmail
     */
    public String getFromEmail() {
        return fromEmail;
    }


    /**
     * @param fromEmail the fromEmail to set
     */
    public void setFromEmail(String fromEmail) {
        this.fromEmail = nullOrTrim(fromEmail);
    }


    /**
     * @return the fromName
     */
    public String getFromName() {
        return fromName;
    }


    /**
     * @param fromName the fromName to set
     */
    public void setFromName(String fromName) {
        this.fromName = nullOrTrim(fromName);
    }

    public String getDisplayTo() {
        return displayTo;
    }

    public void setDisplayTo(String displayTo) {
        this.displayTo = displayTo;
    }

    public String getDisplayCc() {
        return displayCc;
    }

    public void setDisplayCc(String displayCc) {
        this.displayCc = displayCc;
    }

    public String getDisplayBcc() {
        return displayBcc;
    }

    public void setDisplayBcc(String displayBcc) {
        this.displayBcc = displayBcc;
    }

    /**
     * @return the messageClass
     */
    public String getMessageClass() {
        return messageClass;
    }


    /**
     * @param messageClass the messageClass to set
     */
    public void setMessageClass(String messageClass) {
        this.messageClass = messageClass;
    }


    /**
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }


    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }


    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }


    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = nullOrTrim(subject);
    }

    /**
     * @return the toEmail
     */
    public String getToEmail() {
        return toEmail;
    }


    /**
     * @param toEmail the toEmail to set
     */
    public void setToEmail(String toEmail) {
        this.toEmail = nullOrTrim(toEmail);
    }


    /**
     * @return the toName
     */
    public String getToName() {
        return toName;
    }


    /**
     * @param toName the toName to set
     */
    public void setToName(String toName) {
        this.toName = nullOrTrim(toName);
    }

    /**
     * @return the bodyRTF
     */
    public String getBodyRTF() {
        return bodyRTF;
    }


    /**
     * @param bodyRTF the bodyRTF to set
     */
    public void setBodyRTF(String bodyRTF) {
        this.bodyRTF = nullOrTrim(bodyRTF);
    }


    /**
     * @return the headers
     */
    public String getHeaders() {
        return headers;
    }


    /**
     * @param headers the headers to set
     */
    public void setHeaders(String headers) {
        this.headers = headers;
    }

    /**
     * Parses the message date from the mail headers.
     *
     * @param headers The headers in a single String object
     * @return The Date object or null, if no valid Date:
     *   has been found
     */
    public static Date getDateFromHeaders(String headers) {
        if (headers == null) {
            return null;
        }
        String[] headerLines = headers.split("\n");
        for (String headerLine : headerLines) {
            if (headerLine.toLowerCase().startsWith("date:")) {
                String dateValue = headerLine.substring("Date:".length()).trim();
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                try {
                    Date date = formatter.parse(dateValue);
                    return date;
                } catch(Exception e) {
                    LOGGER.info( "Could not parse date "+dateValue, e);
                }
                // there is only one Date: header; we can exit the loop here
                break;
            }
        }
        return null;
    }


    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }


    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }


    public Set<String> getProperties() {
        return this.properties.keySet();
    }

    public Object getProperty(String name) {
        return this.properties.get(name);
    }

    private static String nullOrTrim(String subject1) {
        return subject1 == null ? null : subject1.trim();
    }
}
