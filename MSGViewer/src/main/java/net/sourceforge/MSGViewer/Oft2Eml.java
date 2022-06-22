/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.MSGViewer;

/**
 *
 * @author martin
 */
public class Oft2Eml extends CLIFileConverter {
	public static final String CLI_PARAMETER = "-oft2eml";

	public Oft2Eml(ModuleLauncher module_launcher) {
		super(module_launcher, "oft", "eml");
	}

	@Override
	public String getCLIParameter() {
		return CLI_PARAMETER;
	}
}
