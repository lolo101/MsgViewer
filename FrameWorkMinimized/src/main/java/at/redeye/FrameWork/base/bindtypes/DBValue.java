/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.bindtypes;

import javax.swing.text.PlainDocument;

import at.redeye.SqlDBInterface.SqlDBIO.impl.DBDataType;



/**
 *
 * @author martin
 */
public abstract class DBValue {
    
    protected String name;
    protected String title;
    protected boolean is_primary_key = false;
    protected boolean create_index = false;
    
    public DBValue( String name )
    {
        this.name = name.toLowerCase();
        this.title = "";
    }
    
    public DBValue( String name, String title )
    {
        this.name = name.toLowerCase();
        this.title = title;
    }

    public DBValue( String name, String title, boolean name_is_already_lowercase )
    {
        if( name_is_already_lowercase )
            this.name = name;
        else 
            this.name = name.toLowerCase();
        
        this.title = title;
    }
    
    public abstract DBDataType getDBType();
    public abstract void loadFromDB( Object obj );
    public abstract void loadFromString( String s );
    public abstract boolean acceptString( String s );
    public abstract void loadFromCopy( Object obj );
    
    /**     
     * @return the name of this value in lower case letters. 
     */
    public String getName() {
        return name;
    }
    
    public void setName( String name )
    {
        this.name = name.toLowerCase();
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public abstract Object getValue();
    
    public abstract DBValue getCopy();
    
    
    public PlainDocument getDocumentValidator()
    {
        return null;
    }    
    
    public boolean isPrimaryKey()
    {
        return is_primary_key;
    }
    
    public void setAsPrimaryKey( boolean val )
    {
        is_primary_key = val;
    }
    
    public void setAsPrimaryKey()
    {
        is_primary_key = true;
    }
    
    public void setShouldHaveIndex()
    {
        create_index = true;
    }
    
    public boolean shouldHaveIndex()
    {
        return create_index;
    }
}
