package at.redeye.FrameWork.base;

import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;

import static com.google.common.jimfs.Configuration.unix;
import static org.assertj.core.api.Assertions.assertThat;

class SetupTest {
    @Test
    void should_not_throw_when_file_missing() throws IOException {
        try (FileSystem fileSystem = Jimfs.newFileSystem(unix())) {
            Path userHome = fileSystem.getPath("/home");
            new Setup(userHome, "test");
            assertThat(userHome.resolve(".test/test.properties")).exists();
        }
    }
}