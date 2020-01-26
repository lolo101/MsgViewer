/**
 *
 */
package at.redeye.FrameWork.utilities;

/**
 * @author Mario Mattl
 */
public class Rounding {

	public static Double rndDouble(Double d, int digits) {

		long exp = (long) Math.pow(10, digits);

		return Math.round(d * exp) / (double) exp;

	}

}
