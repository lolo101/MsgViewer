package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.BaseModuleLauncher;
import at.redeye.FrameWork.base.Setup;
import com.auxilii.msgparser.Message;
import net.sourceforge.MSGViewer.factory.MessageParserFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

public abstract class CLIFileConverter {

	private static final Logger LOGGER = BaseModuleLauncher.logger;

	private final ModuleLauncher module_launcher;
	private final String sourceType;
	private final String targetType;
	private boolean convertToTemp = false;
	private boolean openAfterConvert = false;

	/**
	 * @param module_launcher
	 * @param sourceType
	 *            the source file type (i.e. ending) without a leading dot. E.g.
	 *            "msg"
	 * @param targetType
	 *            the target file type (i.e. ending) without a leading dot. E.g.
	 *            "mbox"
	 */
	CLIFileConverter(ModuleLauncher module_launcher, String sourceType,
					 String targetType) {
		this.module_launcher = module_launcher;
		BaseModuleLauncher.BaseConfigureLogging(Level.INFO);

		this.sourceType = sourceType;
		this.targetType = targetType;
	}

	/**
	 * @return the CLI flag that will trigger this converter. E.g. "-msg2mbox"
	 */
	public abstract String getCLIParameter();

	/**
	 * @param convertToTemp
	 *            if true, creates the target file in the users tmp directory;
	 *            if false, creates the target file in the source files
	 *            directory
	 */
	void setConvertToTemp(boolean convertToTemp) {
		this.convertToTemp = convertToTemp;
	}

	/**
	 * @param openAfterConvert
	 *            if true, opens the converted file using
	 *            {@link Desktop#open(File)}
	 */
	void setOpenAfterConvert(boolean openAfterConvert) {
		this.openAfterConvert = openAfterConvert;
	}

	void work() {
		boolean converted = false;
		MessageParserFactory factory = new MessageParserFactory();

		for (String sourceFilePath : module_launcher.args) {
			if (sourceFilePath.toLowerCase().endsWith(String.format(".%s", sourceType))) {
				converted = true;
				processFile(factory, sourceFilePath);
			}
		}

		if (!converted) {
			usage();
		}
	}

	private void usage() {
		System.out.println(module_launcher.root.MlM(String.format(
				"usage: %s FILE FILE ....", getCLIParameter())));
	}

	private void processFile(MessageParserFactory factory, String sourceFilePath) {
		File sourceFile = new File(sourceFilePath).getAbsoluteFile();
		String baseFileName = sourceFile.getName();
		int idx = baseFileName.lastIndexOf('.');
		baseFileName = baseFileName.substring(0, idx);
		try {
			File targetFile = convertToTemp
					? File.createTempFile(baseFileName, String.format(".%s", targetType))
					: Paths.get(sourceFile.getParent(), String.format("%s.%s", baseFileName, targetType)).toFile();

			LOGGER.info("conversion source file: " + sourceFile);
			Message msg = factory.parseMessage(sourceFile);

			LOGGER.info("conversion target file: " + targetFile);
			factory.saveMessage(msg, targetFile);

			if (openAfterConvert) {
				openFile(targetFile);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void openFile(File targetFile) {
		try {
			Desktop.getDesktop().open(targetFile);
		} catch (Exception e) {
			LOGGER.warn("failed to open default application with Desktop.open(), trying system dependent command.");
			String[] cmdarray = null;

			if (Setup.is_linux_system()) {
				cmdarray = new String[2];
				cmdarray[0] = "xdg-open";
				cmdarray[1] = targetFile.getAbsolutePath();
			} else if (Setup.is_mac_system()) {
				cmdarray = new String[2];
				cmdarray[0] = "open";
				cmdarray[1] = targetFile.getAbsolutePath();
			} else if (Setup.is_win_system()) {
				cmdarray = new String[3];
				cmdarray[0] = "cmd";
				cmdarray[1] = "/c";
				cmdarray[2] = targetFile.getAbsolutePath();
			}
			if (cmdarray != null) {
				try {
					Runtime.getRuntime().exec(cmdarray);
				} catch (Exception e1) {
					String message = String.format("Unable to open converted file.\nCould not execute command %s",
							Arrays.toString(cmdarray));
					LOGGER.error(message.replaceAll("\n", " "));
					JOptionPane.showMessageDialog(null, message,
							"Error opening converted file",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}