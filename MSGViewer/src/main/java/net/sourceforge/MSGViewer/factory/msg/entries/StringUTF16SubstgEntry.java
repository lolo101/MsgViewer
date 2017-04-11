/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer.factory.msg.entries;

import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropPtypString;
import net.sourceforge.MSGViewer.factory.msg.PropTypes.PropType;
import java.io.IOException;
import org.apache.poi.poifs.filesystem.DirectoryEntry;

/**
 *
 * @author martin
 */
public class StringUTF16SubstgEntry extends SubstGEntry 
{
    private String value;
    
    public StringUTF16SubstgEntry( String name ) {
        super( name, TYPE_UTF16 );
    }
    
    
    public StringUTF16SubstgEntry( String name , String value ) {
        super( name, TYPE_UTF16 );
        this.value = value;
    }    
    
    public void setValue( String value ) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }

    @Override
    public PropType getPropType() {
        PropPtypString prop = new PropPtypString(getTagName());
        prop.setValue(value);
        return prop;        
    }

    @Override
    public void createEntry(DirectoryEntry dir) throws IOException {
        
        if( value == null )
            value = "";
         createEntry(dir,value);
    }
    
}
