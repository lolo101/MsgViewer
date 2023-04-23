package at.redeye.Plugins.ShellExec;

import at.redeye.FrameWork.base.dll_cache.DLLExtractor;

public class HSWChellExecDLL implements DLLExtractor {
    public static final String LIB_NAME_BASE = "HSWShellExec_";
    public static final String PROPERTY_NAME = "HSWShellExec_HOME";

    public String getPropertyNameForDllDir() {
        return PROPERTY_NAME;
    }

}
