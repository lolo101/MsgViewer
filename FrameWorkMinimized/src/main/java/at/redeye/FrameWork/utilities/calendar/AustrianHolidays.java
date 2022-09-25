package at.redeye.FrameWork.utilities.calendar;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Vector;

public class AustrianHolidays extends BaseHolidays implements Holidays {

    private static final String myCountryCode = "AT";

    public AustrianHolidays()
    {
        super(myCountryCode);

        if( root != null )
            root.loadMlM4Class(this, "de");
    }

    @Override
    public Collection<HolidayInfo> getHolidays(int year) {

        // siehe http://de.wikipedia.org/wiki/Feiertage_in_Österreich

        Collection<HolidayInfo> res = new Vector<>();

        res.add( create( year, 1, 1, false, true, "Neujahrstag" ) );
        res.add( create( year, 1, 6, false, true, "Dreikönigstag" ) );
        res.add( create( year, 5, 1, false, true, "Tag der Arbeit" ) );
        res.add( create( year, 8, 15, false, true, "Maria Himmelfahrt" ) );
        res.add( create( year, 10, 26, false, true, "Nationalfeiertag" ) );
        res.add( create( year, 11, 1, false, true, "Allerheiligen" ) );
        res.add( create( year, 11, 2, false, false, "Allerseelen" ) );
        res.add( create( year, 12, 8, false, false, "Maria Empfängnis" ) );
        res.add( create( year, 12, 25, false, true, "1. Weihnachtsfeiertag" ) );
        res.add( create( year, 12, 26, false, true, "2. Weihnachtsfeiertag" ) );

        /* TODO, die restlichen fixen Feiertage eintragen */

        LocalDate easter = getEaster( year );

        res.add( create( easter, true, true, "Ostersonntag" ) );

        LocalDate ostermontag = easter.plusDays(1);
        res.add( create( ostermontag, true, true, "Ostermontag"));

        LocalDate gruendonnerstag = easter.minusDays(3);
        res.add( create( gruendonnerstag, true, false, "Gründonnerstag"));

        LocalDate karfreitag = easter.minusDays(2);
        res.add( create( karfreitag, true, false, "Karfreitag"));

        LocalDate christihimmelfahrt = easter.plusDays(39);
        res.add( create( christihimmelfahrt, true, true, "Christi Himmelfahrt"));

        LocalDate pfingsten = easter.plusDays(49);
        res.add( create( pfingsten, true, true, "Pfingsten"));

        LocalDate pfingstmontag = easter.plusDays(50);
        res.add( create( pfingstmontag, true, true, "Pfingstmontag"));

        LocalDate fronleichnam = easter.plusDays(60);
        res.add( create( fronleichnam, true, true, "Fronleichnam"));

        LocalDate aschermittwoch = easter.minusDays(46);
        res.add( create( aschermittwoch, true, false, "Aschermittwoch"));

        LocalDate faschingdienstag = easter.minusDays(47);
        res.add( create( faschingdienstag, true, false, "Faschingdienstag"));

        LocalDate rosenmontag = easter.minusDays(48);
        res.add( create( rosenmontag, true, false, "Rosenmontag"));

        res.add( create( getEuropeanSummerTimeBegin(year), true, false, "Sommerzeit Beginn" ));
        res.add( create( getEuropeanSummerTimeEnd(year), true, false, "Ende Sommerzeit" ));

        return res;
    }

    @Override
    public String getPrimaryCountryCode() {
        return myCountryCode;
    }
}
