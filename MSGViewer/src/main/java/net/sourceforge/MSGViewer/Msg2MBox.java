/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer;

/**
 *
 * @author martin
 */
public class Msg2MBox extends CLIFileConverter {
	public static final String CLI_PARAMETER = "-msg2mbox";

	public Msg2MBox(ModuleLauncher module_launcher) {
		super(module_launcher, "msg", "mbox");
	}

	@Override
	public String getCLIParameter() {
		return CLI_PARAMETER;
	}
}
