package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.Root;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;

import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class AttachmentRepository {
    private final Root root;
    private final MessageDigest digest;

    public AttachmentRepository(Root root) {
        this.root = root;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Path getTempFile(FileAttachment fatt) {
        String tempFileName = HexFormat.of().formatHex(digest.digest(fatt.getData()));
        return getTempFile(tempFileName);
    }

    public Path getTempFile(MsgAttachment matt) {
        return getTempFile(matt.message().hashCode() + ".msg");
    }

    private Path getTempFile(String fileName) {
        return root.getStorage().resolve(fileName);
    }
}
