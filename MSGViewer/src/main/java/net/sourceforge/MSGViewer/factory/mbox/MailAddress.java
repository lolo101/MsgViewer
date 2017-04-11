/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.MSGViewer.factory.mbox;

/**
 *
 * @author martin
 */
public class MailAddress
{
    String display_name;
    String email;

    public MailAddress( String display_name, String email )
    {
        this.display_name = display_name;
        this.email = email;
    }

    public MailAddress()
    {

    }


    public String getDisplayName()
    {
        return display_name;
    }

    public String getEmail()
    {
        return email;
    }

    @Override
    public String toString()
    {
        if( display_name == null )
            return email;

        return "\"" + display_name + "\""+ " <" + email + ">";
    }

    public void setDisplayName( String name )
    {
        display_name = name;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }
    
    @Override
    public boolean equals( Object o )
    {
        if( o == null )
            return false;
        
        if( o == this )
            return true;
        
        if( o.getClass() == this.getClass() ) {
            MailAddress addr = (MailAddress)o;
            return addr.email.equals(email) && addr.display_name.equals(display_name);
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.display_name != null ? this.display_name.hashCode() : 0);
        hash = 59 * hash + (this.email != null ? this.email.hashCode() : 0);
        return hash;
    }
}
