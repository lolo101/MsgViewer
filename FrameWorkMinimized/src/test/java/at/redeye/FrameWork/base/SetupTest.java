package at.redeye.FrameWork.base;

import static org.assertj.core.api.Assertions.assertThat;
import com.google.common.jimfs.Jimfs;
import java.io.IOException;
import java.nio.file.*;
import org.junit.jupiter.api.Test;

class SetupTest {
    @Test
    void should_not_throw_when_file_missing() throws IOException {
        try (FileSystem fileSystem = Jimfs.newFileSystem()) {
            Path userHome = fileSystem.getPath("/home");
            new Setup(userHome, "test");
            assertThat(userHome.resolve(".test/test.properties")).exists();
        }
    }
}