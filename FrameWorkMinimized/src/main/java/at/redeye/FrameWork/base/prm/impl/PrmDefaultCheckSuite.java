package at.redeye.FrameWork.base.prm.impl;

import at.redeye.FrameWork.base.bindtypes.DBString;
import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.SqlDBInterface.SqlDBIO.DateTimeFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.function.Predicate;

public class PrmDefaultCheckSuite implements PrmDefaultChecksInterface {

	private final Logger logger = LogManager.getLogger(PrmDefaultCheckSuite.class);
	private final long checks2Execute;

	public PrmDefaultCheckSuite(long checks2Execute) {

		this.checks2Execute = checks2Execute;

	}

	private boolean passesDouble(PrmActionEvent event) {

		try {
			Double.parseDouble(event.getNewPrmValue().toString());
		} catch (NumberFormatException nfe) {
			logger.warn(event.getParameterName().toString()
					+ ": Not a Double!\n" + nfe.getMessage());
			return false;
		}

		return true;
	}

	private boolean passesBit(PrmActionEvent event) {

		try {
			Boolean.parseBoolean(event.getNewPrmValue().toString());
		} catch (NumberFormatException nfe) {
			logger.warn(event.getParameterName().toString() + ": Not a Bit!\n"
					+ nfe.getMessage());
			return false;
		}

		return true;
	}

	private boolean passesLong(PrmActionEvent event) {

		try {
			Long.parseLong(event.getNewPrmValue().toString());
		} catch (NumberFormatException nfe) {
			logger.warn(": Not a Long!\n" + nfe.getMessage());
			return false;
		}

		return true;
	}

	private boolean passesJaNein(PrmActionEvent event) {

		String[] validStr = { "ja", "nein", "true", "false", "yes", "no" };

		for (String s : validStr) {
			if (event.getNewPrmValue().toString().equalsIgnoreCase(
					s)) {
				return true;
			}
		}
		logger.warn(event.getParameterName().toString()
				+ ": Not a Yes/No (True/False) !");
		return false;

	}

	private boolean passesHasAValueEqual(PrmActionEvent event) {

		String[] values = event.getPossibleVals();
		for (String value : values) {
			logger.trace("Checking " + value + " / "
					+ event.getNewPrmValue().toString());
			if (value.equals(event.getNewPrmValue().toString())) {
				return true;
			}
		}
		logger.warn(event.getParameterName().toString()
				+ ": Value doesn't match the allowed ones!");
		return false;

	}

	private boolean passesDateTime(PrmActionEvent event, SimpleDateFormat sdf) {
		try {
			sdf.parse(event.getNewPrmValue().toString());
			return true;
		} catch (ParseException pe) {
			logger.warn(event.getParameterName().toString()
					+ ": Date and/or Time is not valid!\n" + pe.getMessage());
			return false;
		}
	}

	private boolean passesLookAndFeel(PrmActionEvent event) {

		DBString newPrmValue = event.getNewPrmValue();
		boolean valid = Arrays.stream(UIManager.getInstalledLookAndFeels())
				.map(UIManager.LookAndFeelInfo::getName)
				.anyMatch(Predicate.isEqual(newPrmValue.toString()));

		if (!valid) {
			logger.warn(newPrmValue
					+ ": Not a valid LookAndFeel value !");
		}
		return valid;

	}

	public boolean doChecks(PrmActionEvent event) {

		if (parameterType(PRM_IS_DOUBLE)) {
			return passesDouble(event);
		}

		if (parameterType(PRM_IS_LONG)) {
			return passesLong(event);
		}

		if (parameterType(PRM_IS_BIT)) {
			return passesBit(event);
		}

		if (parameterType(PRM_IS_TRUE_FALSE)) {
			return passesJaNein(event);
		}

		if (parameterType(PRM_HAS_VALUE)) {
			return passesHasAValueEqual(event);
		}

		if (parameterType(PRM_IS_DATE)) {
			SimpleDateFormat sdf = DateTimeFormat.SQLIF_STD_DATE_FORMAT.formatter();
			return passesDateTime(event, sdf);
		}

		if (parameterType(PRM_IS_TIME)) {
			SimpleDateFormat sdf = DateTimeFormat.SQLIF_STD_TIME_FORMAT.formatter();
			return passesDateTime(event, sdf);
		}

		if (parameterType(PRM_IS_SHORTTIME)) {
			SimpleDateFormat sdf = DateTimeFormat.SQLIF_STD_SHORTTIME_FORMAT.formatter();
			return passesDateTime(event, sdf);
		}

		if (parameterType(PRM_IS_DATETIME)) {
			SimpleDateFormat sdf = DateTimeFormat.SQLIF_STD_DATETIME_FORMAT.formatter();
			return passesDateTime(event, sdf);
		}

		if (parameterType(PRM_IS_LOOKANDFEEL)) {
			return passesLookAndFeel(event);
		}
		return true;
	}

	private boolean parameterType(long type) {
		return (checks2Execute & type) != 0;
	}
}
