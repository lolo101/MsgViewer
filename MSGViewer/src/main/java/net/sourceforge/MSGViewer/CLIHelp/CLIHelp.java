package net.sourceforge.MSGViewer.CLIHelp;

import at.redeye.FrameWork.base.BaseModuleLauncher;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author martin
 */
public class CLIHelp 
{
    private static final int LINE_LEN=80;
    private final List<CLIOption> options = new ArrayList<>();    
    private final BaseModuleLauncher module_launcher;
    
    public CLIHelp(BaseModuleLauncher module_launcher)
    {
        this.module_launcher = module_launcher;
    }
    
    public final void add( CLIOption option ) {
        options.add(option);
    }
    
    public void printHelpScreen()
    {
        int max_option_len = getMaxOptionLen();
        
        StringBuilder sb = new StringBuilder();
        
        for( CLIOption o : options )
        {
            o.buildShortHelpText(sb, max_option_len, LINE_LEN);
            sb.append("\n");
            o.buildLongHelpText(sb, max_option_len + 1, LINE_LEN);
        }
        
        System.out.println( module_launcher.root.getAppName() + " - " + module_launcher.root.getAppTitle() );
        System.out.println( "Version: " + module_launcher.getVersion() + "\n");
        
        System.out.println(sb);
    }
    
    public void printVersion()
    {
        System.out.println( module_launcher.root.getAppName() + " - " + module_launcher.root.getAppTitle() );
        System.out.println( "Version: " + module_launcher.getVersion() + "\n");           
    }
    
    private int getMaxOptionLen()
    {
        int val = 0;
        
        for( CLIOption o : options )
        {
            val = Math.max(val, o.getName().length() );
        }
        
        return val;
    }
}
