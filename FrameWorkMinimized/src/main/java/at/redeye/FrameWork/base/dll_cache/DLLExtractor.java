/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.dll_cache;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author martin
 */
public interface DLLExtractor
{
    /**
     * @return returns something like proxy_vole_dir
     */
    String getPropertyNameForDllDir();

    /**
     * should extract required dlls to directory specfied by
     * System.getProperty("proxy_vole_dir");
     */
    void extractDlls() throws IOException;

    /**
     * @return list of all dlls would be extracted with call of extractDlls
     */
    List<String> getNames();
}
