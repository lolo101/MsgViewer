/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.base.prm.impl;

import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.SqlDBInterface.SqlDBIO.StmtExecInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author mmattl
 */
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

		for (int idx = 0; idx < validStr.length; idx++) {

			if (event.getNewPrmValue().toString().equalsIgnoreCase(
					validStr[idx])) {
				return true;
			}

		}
		logger.warn(event.getParameterName().toString()
				+ ": Not a Yes/No (True/False) !");
		return false;

	}

	private boolean passesHasAValueEqual(PrmActionEvent event) {

		String[] values = event.getPossibleVals();
		for (int idx = 0; idx < values.length; idx++) {
			logger.trace("Checking " + values[idx] + " / "
					+ event.getNewPrmValue().toString());
			if (values[idx].equals(event.getNewPrmValue().toString())) {
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
		} catch (ParseException pe) {
			logger.warn(event.getParameterName().toString()
					+ ": Date and/or Time is not valid!\n" + pe.getMessage());
			return false;
		}

		return true;

	}

	private boolean passesLookAndFeel(PrmActionEvent event) {

		String[] validStr = { "metal", "system", "motif", "nimbus" };

		for (int idx = 0; idx < validStr.length; idx++) {

			if (event.getNewPrmValue().toString().equalsIgnoreCase(
					validStr[idx])) {
				return true;
			}

		}
		logger.warn(event.getParameterName().toString()
				+ ": Not a valid LookAndFeel value !");
		return false;

	}

	public boolean doChecks(PrmActionEvent event) {

		if ((checks2Execute & PRM_IS_DOUBLE) != 0) {
			if (!passesDouble(event)) {
				return false;
			}
		}

		if ((checks2Execute & PRM_IS_LONG) != 0) {
			if (!passesLong(event)) {
				return false;
			}
		}

		if ((checks2Execute & PRM_IS_BIT) != 0) {
			if (!passesBit(event)) {
				return false;
			}
		}

		if ((checks2Execute & PRM_IS_TRUE_FALSE) != 0) {
			if (!passesJaNein(event)) {
				return false;
			}
		}

		if ((checks2Execute & PRM_HAS_VALUE) != 0) {
			if (!passesHasAValueEqual(event)) {
				return false;
			}
		}

		if ((checks2Execute & PRM_IS_DATE) != 0) {
			SimpleDateFormat sdf = new SimpleDateFormat(
					StmtExecInterface.SQLIF_STD_DATE_FORMAT);
			if (!passesDateTime(event, sdf)) {
				return false;
			}
		}

		if ((checks2Execute & PRM_IS_TIME) != 0) {
			SimpleDateFormat sdf = new SimpleDateFormat(
					StmtExecInterface.SQLIF_STD_TIME_FORMAT);
			if (!passesDateTime(event, sdf)) {
				return false;
			}
		}

		if ((checks2Execute & PRM_IS_SHORTTIME) != 0) {
			SimpleDateFormat sdf = new SimpleDateFormat(
					StmtExecInterface.SQLIF_STD_SHORTTIME_FORMAT);
			if (!passesDateTime(event, sdf)) {
				return false;
			}
		}

		if ((checks2Execute & PRM_IS_DATETIME) != 0) {
			SimpleDateFormat sdf = new SimpleDateFormat(
					StmtExecInterface.SQLIF_STD_DATE_FORMAT + " "
							+ StmtExecInterface.SQLIF_STD_TIME_FORMAT);
			if (!passesDateTime(event, sdf)) {
				return false;
			}
		}

		if ((checks2Execute & PRM_IS_LOOKANDFEEL) != 0) {
			if (!passesLookAndFeel(event)) {
				return false;
			}
		}
		return true;
	}
}
