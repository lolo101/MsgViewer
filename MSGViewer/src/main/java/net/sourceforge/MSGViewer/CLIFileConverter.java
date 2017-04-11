package net.sourceforge.MSGViewer;

import java.awt.Desktop;
import java.io.File;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;

import javax.swing.JOptionPane;

import net.sourceforge.MSGViewer.factory.MessageParserFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import at.redeye.FrameWork.base.BaseModuleLauncher;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;

import com.auxilii.msgparser.Message;

public abstract class CLIFileConverter {

	private static final Logger LOGGER = BaseModuleLauncher.logger;

	private final ModuleLauncher module_launcher;
	private final Root root;
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
	public CLIFileConverter(ModuleLauncher module_launcher, String sourceType,
			String targetType) {
		this.module_launcher = module_launcher;
		this.root = module_launcher.root;
		BaseModuleLauncher.BaseConfigureLogging(Level.INFO);

		this.sourceType = sourceType;
		this.targetType = targetType;
	}

	/**
	 * @param convertToTemp
	 *            if true, creates the target file in the users tmp directory;
	 *            if false, creates the target file in the source files
	 *            directory
	 */
	public void setConvertToTemp(boolean convertToTemp) {
		this.convertToTemp = convertToTemp;
	}

	/**
	 * @param openAfterConvert
	 *            if true, opens the converted file using
	 *            {@link Desktop#open(File)}
	 */
	public void setOpenAfterConvert(boolean openAfterConvert) {
		this.openAfterConvert = openAfterConvert;
	}

	/**
	 * @return the CLI flag that will trigger this converter. E.g. "-msg2mbox"
	 */
	public abstract String getCLIParameter();

	public void usage() {
		System.out.println(this.root.MlM(MessageFormat.format(
				"usage: {0} FILE FILE ....", this.getCLIParameter())));
	}

	public void work() {
		boolean converted = false;
		MessageParserFactory factory = new MessageParserFactory();

		for (String sourceFilePath : this.module_launcher.args) {
			if (sourceFilePath.toLowerCase().endsWith(
					MessageFormat.format(".{0}", this.sourceType))) {
				converted = true;
				this.processFile(factory, sourceFilePath);
			}
		}

		if (!converted) {
			this.usage();
		}
	}

	private void processFile(MessageParserFactory factory, String sourceFilePath) {
		File sourceFile = new File(sourceFilePath);
		String baseFileName = sourceFile.getName();
		int idx = baseFileName.lastIndexOf('.');
		baseFileName = baseFileName.substring(0, idx);
		LOGGER.info(MessageFormat.format("conversion source file: {0}",
				sourceFile));
		try {

			File targetFile;
			if (convertToTemp) {
				targetFile = File.createTempFile(baseFileName,
						MessageFormat.format(".{0}", targetType));
			} else {
				File targetDirectory = sourceFile.getParentFile();
				targetFile = Paths.get(
						targetDirectory.getAbsolutePath(),
						MessageFormat.format("{0}.{1}", baseFileName,
								targetType)).toFile();
			}

			LOGGER.info(MessageFormat.format("conversion target file: {0}",
					targetFile));
			Message msg = factory.parseMessage(sourceFile);

			factory.saveMessage(msg, targetFile);

			if (openAfterConvert) {
				openFile(targetFile);
			}
		} catch (Exception ex) {
			System.err.print(ex);
			ex.printStackTrace();
		}
	}

	public void openFile(File targetFile) {
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
					String message = MessageFormat
							.format("Unable to open converted file.\nCould not execute command {0} \nwith parameters {1}",
									cmdarray[0], Arrays.asList(cmdarray)
											.subList(1, cmdarray.length));
					LOGGER.error(message.replaceAll("\n", ""));
					JOptionPane.showMessageDialog(null, message,
							"Error opening converted file",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}