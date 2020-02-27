package net.sourceforge.MSGViewer.factory.msg.entries;

import static com.auxilii.msgparser.Pid.PidTagSubject;

public class SubjectEntry extends StringUTF16SubstgEntry {
    public SubjectEntry(String subject) {
        super(PidTagSubject, subject);
    }
}
