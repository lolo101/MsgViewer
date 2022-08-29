package at.redeye.Plugins.ShellExec;

import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.base.dll_cache.DLLExtractor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class HSWChellExecDLL implements DLLExtractor {
    public static final String LIB_NAME_BASE = "HSWShellExec_";
    public static final String PROPERTY_NAME = "HSWShellExec_HOME";

    public String getPropertyNameForDllDir() {
        return PROPERTY_NAME;
    }

    public void extractDlls() throws IOException {
        String envdir = System.getProperty(PROPERTY_NAME);

        for (String lib : this.getNames()) {
            try (InputStream source = this.getClass().getResourceAsStream("/at/redeye/Plugins/ShellExec/" + lib);
                 FileOutputStream fout = new FileOutputStream(envdir + "/" + lib)) {
                byte[] buffer = new byte[1024];

                for (int read = 0; read >= 0; read = source.read(buffer)) {
                    fout.write(buffer, 0, read);
                }
            }
        }
    }

    public List<String> getNames() {
        if (Setup.is_win_system()) return List.of(LIB_NAME_BASE + System.getProperty("os.arch") + ".dll");
        return List.of();
    }
}
