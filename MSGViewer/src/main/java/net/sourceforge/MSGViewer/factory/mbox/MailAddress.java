package net.sourceforge.MSGViewer.factory.mbox;

public record MailAddress(String displayName, String email) {

    @Override
    public String toString() {
        if (displayName == null)
            return email;

        return "\"" + displayName + "\"" + " <" + email + ">";
    }
}
