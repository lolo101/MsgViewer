package net.sourceforge.MSGViewer.factory.msg;

import com.auxilii.msgparser.Message;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class MsgContainerTest {

    @Test
    void should_convert_message_to_POI_Filesystem() throws IOException {
        Message msg = new Message();

        try (POIFSFileSystem fs = new POIFSFileSystem()) {
            DirectoryNode root = fs.getRoot();

            MsgContainer cont = new MsgContainer(msg);
            cont.write(root);

            assertThat(root.getEntryNames()).containsExactlyInAnyOrder(
                    "__substg1.0_0C1E001F",
                    "__nameid_version1.0",
                    "__substg1.0_001A001F",
                    "__substg1.0_0064001F",
                    "__properties_version1.0"
            );
        }
    }
}
