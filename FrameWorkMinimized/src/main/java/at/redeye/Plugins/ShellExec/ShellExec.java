package at.redeye.Plugins.ShellExec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ShellExec {
    private static boolean library_not_loaded = true;

    public ShellExec() {
        init(this.getClass());
    }

    public native int execute(String var1);

    protected static void init(Class<?> loader) {
        if (library_not_loaded) {
            String libname = HSWChellExecDLL.LIB_NAME_BASE + System.getProperty("os.arch") + ".dll";

            try {
                String appDir = System.getProperty(HSWChellExecDLL.PROPERTY_NAME);
                if (appDir != null) {
                    File f = new File(appDir, libname);
                    String path = f.getAbsolutePath();
                    System.load(path);
                } else {
                    System.loadLibrary("HSWShellExec");
                }
                library_not_loaded = false;
            } catch (UnsatisfiedLinkError var11) {
                String cp = System.getProperty("java.class.path");
                boolean notFound = true;

                while (cp.length() > 0) {
                    int x = cp.indexOf(File.pathSeparator);
                    String dir;
                    if (x >= 0) {
                        dir = cp.substring(0, x);
                        cp = cp.substring(x + 1);
                    } else {
                        dir = cp;
                        cp = "";
                    }

                    if (dir.length() > 4 && dir.substring(dir.length() - 4).equalsIgnoreCase(".jar")) {
                        x = dir.lastIndexOf(File.separator);
                        if (x > 0) {
                            dir = dir.substring(0, x);
                        } else {
                            dir = ".";
                        }
                    }

                    File f = new File(dir, libname);
                    if (f.exists()) {
                        String path = f.getAbsolutePath();
                        System.load(path);
                        notFound = false;
                        break;
                    }
                }

                if (notFound) {
                    try {
                        InputStream in = loader.getResourceAsStream("/at/redeye/Plugins/ShellExec/" + libname);
                        if (in == null) {
                            throw new Exception("libname: " + libname + " not found");
                        }

                        File tmplib = File.createTempFile("HSWShellExec-", ".dll");
                        tmplib.deleteOnExit();
                        OutputStream out = new FileOutputStream(tmplib);
                        byte[] buf = new byte[1024];

                        int len;
                        while ((len = in.read(buf)) != -1) {
                            out.write(buf, 0, len);
                        }

                        in.close();
                        out.close();
                        System.load(tmplib.getAbsolutePath());
                        library_not_loaded = false;
                        notFound = false;
                        System.out.println(libname + " loaded via tmp generated pathname: " + tmplib.getAbsolutePath());
                    } catch (Exception var10) {
                        notFound = true;
                    }
                }

                if (notFound) {
                    System.out.println("failed loading jshortcut.dll");
                    throw var11;
                }
            }
        }
    }
}
