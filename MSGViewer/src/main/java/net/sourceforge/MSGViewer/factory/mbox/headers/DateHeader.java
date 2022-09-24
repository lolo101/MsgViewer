package net.sourceforge.MSGViewer.factory.mbox.headers;

import com.auxilii.msgparser.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.DateTimeException;
import java.time.ZonedDateTime;

/**
 * <a href="http://tools.ietf.org/html/rfc5322#section-3.6.1">rfc5322 3.6.1</a>
 * <p>
 * <pre>
 *  date-time       =   [ day-of-week "," ] date time [CFWS]
 *  day-of-week     =   ([FWS] day-name) / obs-day-of-week
 *  day-name        =   "Mon" / "Tue" / "Wed" / "Thu" /
 *                      "Fri" / "Sat" / "Sun"
 *  date            =   day month year
 *  day             =   ([FWS] 1*2DIGIT FWS) / obs-day
 *  month           =   "Jan" / "Feb" / "Mar" / "Apr" /
 *                      "May" / "Jun" / "Jul" / "Aug" /
 *                      "Sep" / "Oct" / "Nov" / "Dec"
 *  year            =   (FWS 4*DIGIT FWS) / obs-year
 *  time            =   time-of-day zone
 *  time-of-day     =   hour ":" minute [ ":" second ]
 *  hour            =   2DIGIT / obs-hour
 *  minute          =   2DIGIT / obs-minute
 *  second          =   2DIGIT / obs-second
 *  zone            =   (FWS ( "+" / "-" ) 4DIGIT) / obs-zone
 *     </pre>
 * <p>
 * eg: Fri, 1 Jul 2011 20:18:36 +0200
 */
public class DateHeader extends HeaderParser
{
    private static final Logger LOGGER = LogManager.getLogger(DateHeader.class);

    public DateHeader()
    {
        super("Date");
    }

    @Override
    public void parse(Message message, String line) {
        if (!line.isEmpty()) try {
            ZonedDateTime date = ZonedDateTime.from(Message.DATE_TIME_FORMATTER.parse(line));
            message.setDate(date);
        } catch (DateTimeException ex) {
            LOGGER.warn("Could not parse date", ex);
        }
    }

}
