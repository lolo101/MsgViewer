/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer;

/**
 *
 * @author martin
 */
public class MBox2Msg extends CLIFileConverter {
	public static final String CLI_PARAMETER = "-mbox2msg";

	public MBox2Msg(ModuleLauncher module_launcher) {
		super(module_launcher, "mbox", "msg");
	}

	@Override
	public String getCLIParameter() {
		return CLI_PARAMETER;
	}
}
