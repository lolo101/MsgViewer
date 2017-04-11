package net.sourceforge.MSGViewer;

public class Eml2Msg extends CLIFileConverter {
	public static final String CLI_PARAMETER = "-eml2msg";

	public Eml2Msg(ModuleLauncher module_launcher) {
		super(module_launcher, "eml", "msg");
	}

	@Override
	public String getCLIParameter() {
		return CLI_PARAMETER;
	}
}
