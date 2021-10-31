package net.sourceforge.MSGViewer.factory.mbox;

public class MailAddress
{
    private final String displayName;
    private final String email;

    public MailAddress( String displayName, String email )
    {
        this.displayName = displayName;
        this.email = email;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getEmail()
    {
        return email;
    }

    @Override
    public String toString()
    {
        if( displayName == null )
            return email;

        return "\"" + displayName + "\""+ " <" + email + ">";
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
            return addr.email.equals(email) && addr.displayName.equals(displayName);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.displayName != null ? this.displayName.hashCode() : 0);
        hash = 59 * hash + (this.email != null ? this.email.hashCode() : 0);
        return hash;
    }
}
