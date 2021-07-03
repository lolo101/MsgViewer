/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.utilities.calendar;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Vector;

public class SwitzerlandHolidays extends BaseHolidays implements Holidays {

    public static String myCountryCode = "CH";

    public SwitzerlandHolidays()
    {
        super(myCountryCode);

        root.loadMlM4Class(this, "de");
    }

    @Override
    public Collection<HolidayInfo> getHolidays(int year) {

        // siehe http://de.wikipedia.org/wiki/Feiertage_in_der_Schweiz
        Vector<HolidayInfo> res = new Vector<>();

        Easterformular easter_formular = new Easterformular(year);

        int day = easter_formular.easterday();

        LocalDate easter = getEaster(year);

        res.add( create( easter, true, true, "Ostersonntag" ) );


        res.add( create( year, 1, 1, false, true, "Neujahrstag" ) );
        res.add( create( year, 1, 2, false, false, "Berchtoldstag" ) );
        res.add( create( year, 1, 6, false, false, "Dreikönigstag" ) );

        LocalDate gruendonnerstag = easter.minusDays(3);
        res.add( create( gruendonnerstag, true, false, "Gründonnerstag"));

        LocalDate karfreitag = easter.minusDays(2);
        res.add( create( karfreitag, true, true, "Karfreitag"));

        LocalDate ostermontag = easter.plusDays(1);
        res.add( create( ostermontag, true, true, "Ostermontag"));

        res.add( create( year, 5, 1, false, false, "Tag der Arbeit" ) );

        LocalDate christihimmelfahrt = easter.plusDays(39);
        res.add( create( christihimmelfahrt, true, true, "Auffahrt"));

        LocalDate aschermittwoch = easter.minusDays(46);
        res.add( create( aschermittwoch, true, false, "Aschermittwoch"));

        LocalDate faschingdienstag = easter.minusDays(47);
        res.add( create( faschingdienstag, true, false, "Faschingdienstag"));

        LocalDate rosenmontag = easter.minusDays(48);
        res.add( create( rosenmontag, true, false, "Rosenmontag"));

        LocalDate pfingsten = easter.plusDays(49);
        res.add( create( pfingsten, true, false, "Pfingsten"));

        LocalDate pfingstmontag = easter.plusDays(50);
        res.add( create( pfingstmontag, true, true, "Pfingstmontag"));

        LocalDate fronleichnam = easter.plusDays(60);
        res.add( create( fronleichnam, true, true, "Fronleichnam"));

        res.add( create( year, 8, 1, false, true, "Bundesfeier" ) );
        res.add( create( year, 8, 15, false, false, "Maria Himmelfahrt" ) );

        res.add( create( year, 11, 1, false, false, "Allerheiligen" ) );
        res.add( create( year, 11, 2, false, false, "Allerseelen" ) );

        res.add( create( year, 12, 25, false, true, "1. Weihnachtsfeiertag" ) );
        res.add( create( year, 12, 26, false, true, "2. Weihnachtsfeiertag" ) );

        res.add( create( getEuropeanSummerTimeBegin(year), true, false, "Sommerzeit Beginn" ));
        res.add( create( getEuropeanSummerTimeEnd(year), true, false, "Ende Sommerzeit" ));

        return res;
    }

    @Override
    public String getPrimaryCountryCode() {
        return myCountryCode;
    }

}
