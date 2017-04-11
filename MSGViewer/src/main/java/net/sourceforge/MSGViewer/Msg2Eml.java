/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer;

/**
 *
 * @author martin
 */
public class Msg2Eml extends CLIFileConverter {
	public static final String CLI_PARAMETER = "-msg2eml";

	public Msg2Eml(ModuleLauncher module_launcher) {
		super(module_launcher, "msg", "eml");
	}

	@Override
	public String getCLIParameter() {
		return CLI_PARAMETER;
	}
}
