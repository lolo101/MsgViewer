/*
 * CalendarComponent.java
 *
 * Created on 2. Januar 2009, 10:51
 */

package at.redeye.FrameWork.widgets.calendar;


import at.redeye.FrameWork.utilities.calendar.Holidays;
import at.redeye.FrameWork.utilities.calendar.Holidays.HolidayInfo;
import at.redeye.FrameWork.utilities.calendar.MonthNames;
import at.redeye.FrameWork.widgets.calendarday.CalendarDay;
import at.redeye.FrameWork.widgets.calendarday.CommonInfoRenderer;
import at.redeye.FrameWork.widgets.calendarday.DayEventListener;
import at.redeye.FrameWork.widgets.calendarday.DisplayDay;
import at.redeye.FrameWork.widgets.calendarday.InfoRenderer;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Vector;
import org.joda.time.DateMidnight;
import org.joda.time.LocalDate;

/**
 *
 * @author  martin
 */
public class CalendarComponent extends javax.swing.JPanel implements DisplayMonth, DayEventListener {

    protected Vector<DisplayDay> days = new Vector<DisplayDay>();
    protected int daysOfMonth = 0;
    protected int offset = 0;
    protected DayEventListener listener = null;    
    protected Holidays holidays = null;
    protected InfoRenderer info_renderer = new CommonInfoRenderer();
    protected int month = 0;
    protected int year = 0;
    protected boolean showMonth = true;
    protected boolean allowClickOnInactiveDays = false;
    
    /** Creates new form CalendarComponent */
    public CalendarComponent() {
        initComponents();
        
        days.add(calendarDay1);
        days.add(calendarDay2);
        days.add(calendarDay3);
        days.add(calendarDay4);
        days.add(calendarDay5);
        days.add(calendarDay6);
        days.add(calendarDay7);
        days.add(calendarDay8);
        days.add(calendarDay9);
        days.add(calendarDay10);
        days.add(calendarDay11);
        days.add(calendarDay12);
        days.add(calendarDay13);
        days.add(calendarDay14);
        days.add(calendarDay15);
        days.add(calendarDay16);
        days.add(calendarDay17);
        days.add(calendarDay18);
        days.add(calendarDay19);
        days.add(calendarDay20);
        days.add(calendarDay21);
        days.add(calendarDay22);
        days.add(calendarDay23);
        days.add(calendarDay24);
        days.add(calendarDay25);
        days.add(calendarDay26);
        days.add(calendarDay27);
        days.add(calendarDay28);
        days.add(calendarDay29);
        days.add(calendarDay30);
        days.add(calendarDay31);
        days.add(calendarDay32);
        days.add(calendarDay33);
        days.add(calendarDay34);
        days.add(calendarDay35);
        days.add(calendarDay36);
        days.add(calendarDay37);
        days.add(calendarDay38);
        days.add(calendarDay39);
        days.add(calendarDay40);
        days.add(calendarDay41);
        days.add(calendarDay42);
        
        for( DisplayDay day : days )
        {
            day.setListener(this);
            day.setInfoRenderer(info_renderer.getNewInstance());
        }
        
    }

    public DisplayDay getDay(CalendarDay day) {
        return getDay(isWhatDayOfMonth(day));
    }

    public void setShowMonthName( boolean state )
    {
        showMonth = state;
        jPMonth.setVisible(false);
    }

    /**
     * by default an inactive element is not clickable
     * and a listener will not be informed. By using this
     * function the behavior can be changed
     * @param state
     */
    public void setAllowClickOnInactiveElements( boolean state )
    {
        allowClickOnInactiveDays = state;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        calendarDay1 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay2 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay3 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay4 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay5 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay6 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay7 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay8 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay9 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay10 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay11 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay12 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay13 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay14 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay15 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay16 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay17 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay18 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay19 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay20 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay21 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay22 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay23 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay24 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay25 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay26 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay27 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay28 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay29 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay30 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay31 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay32 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay33 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay34 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay35 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay36 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay37 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay38 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay39 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay40 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay41 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        calendarDay42 = new at.redeye.FrameWork.widgets.calendarday.CalendarDay();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPMonth = new javax.swing.JPanel();
        jLTitle = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(800, 602));

        jPanel1.setMaximumSize(new java.awt.Dimension(32000, 32000));
        jPanel1.setLayout(new java.awt.GridLayout(0, 7));
        jPanel1.add(calendarDay1);
        jPanel1.add(calendarDay2);
        jPanel1.add(calendarDay3);
        jPanel1.add(calendarDay4);
        jPanel1.add(calendarDay5);
        jPanel1.add(calendarDay6);
        jPanel1.add(calendarDay7);
        jPanel1.add(calendarDay8);
        jPanel1.add(calendarDay9);
        jPanel1.add(calendarDay10);
        jPanel1.add(calendarDay11);
        jPanel1.add(calendarDay12);
        jPanel1.add(calendarDay13);
        jPanel1.add(calendarDay14);
        jPanel1.add(calendarDay15);
        jPanel1.add(calendarDay16);
        jPanel1.add(calendarDay17);
        jPanel1.add(calendarDay18);
        jPanel1.add(calendarDay19);
        jPanel1.add(calendarDay20);
        jPanel1.add(calendarDay21);
        jPanel1.add(calendarDay22);
        jPanel1.add(calendarDay23);
        jPanel1.add(calendarDay24);
        jPanel1.add(calendarDay25);
        jPanel1.add(calendarDay26);
        jPanel1.add(calendarDay27);
        jPanel1.add(calendarDay28);
        jPanel1.add(calendarDay29);
        jPanel1.add(calendarDay30);
        jPanel1.add(calendarDay31);
        jPanel1.add(calendarDay32);
        jPanel1.add(calendarDay33);
        jPanel1.add(calendarDay34);
        jPanel1.add(calendarDay35);
        jPanel1.add(calendarDay36);
        jPanel1.add(calendarDay37);
        jPanel1.add(calendarDay38);
        jPanel1.add(calendarDay39);
        jPanel1.add(calendarDay40);
        jPanel1.add(calendarDay41);
        jPanel1.add(calendarDay42);

        jPanel2.setPreferredSize(new java.awt.Dimension(800, 27));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Montag");
        jPanel2.add(jLabel1);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Dienstag");
        jPanel2.add(jLabel2);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Mittwoch");
        jPanel2.add(jLabel3);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Donnerstag");
        jPanel2.add(jLabel4);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Freitag");
        jPanel2.add(jLabel5);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Samstag");
        jPanel2.add(jLabel6);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Sonntag");
        jPanel2.add(jLabel7);

        jLTitle.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTitle.setText("Jänner 2009");

        javax.swing.GroupLayout jPMonthLayout = new javax.swing.GroupLayout(jPMonth);
        jPMonth.setLayout(jPMonthLayout);
        jPMonthLayout.setHorizontalGroup(
            jPMonthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPMonthLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPMonthLayout.setVerticalGroup(
            jPMonthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay1;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay10;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay11;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay12;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay13;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay14;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay15;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay16;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay17;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay18;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay19;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay2;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay20;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay21;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay22;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay23;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay24;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay25;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay26;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay27;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay28;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay29;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay3;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay30;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay31;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay32;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay33;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay34;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay35;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay36;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay37;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay38;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay39;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay4;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay40;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay41;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay42;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay5;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay6;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay7;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay8;
    private at.redeye.FrameWork.widgets.calendarday.CalendarDay calendarDay9;
    private javax.swing.JLabel jLTitle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPMonth;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setMonth(int Month, int Year) {
        
        clear();
        
        month = Month;
        year = Year;
        
        DateMidnight cal = new DateMidnight(Year, Month, 1 );

        int dayOfWeek = cal.getDayOfWeek();
        int maxDays = cal.toGregorianCalendar().getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        daysOfMonth = maxDays;
        
        System.out.println("Day of Week:" + dayOfWeek + " Month: " + maxDays );                                                    
        
        // an dieser Stelle beginnen die Tage des aktuellen Monats
        offset = dayOfWeek - 1;
                
        
        // weils einfach besser aussieht.
        if( offset == 0 )
            offset = 7;
                
        System.out.println("offset:" + offset);
        
        /* das aktuelle Monat durchnummerieren */
        for( int i = offset, count=1; count <= maxDays && i < 42; i++, count++ )
        {
            days.get(i).setDay(Integer.toString(count));
            days.get(i).setActive();
            InfoRenderer renderer = days.get(i).getInfoRenderer();
            
            if( renderer != null )
                renderer.setDay(cal.plusDays(count-1));
            
            days.get(i).setWeekDay(cal.plusDays(count-1).getDayOfWeek());            
        } 
        
        // Samstage hervorheben
        for( int i = 5; i < 42; i += 7  )
            days.get(i).setSaturdayBackground();
        
        // Sonntage hervorheben
        for( int i = 6; i < 42; i += 7 ) {
            days.get(i).setSundayBackground();            
        }
        
        /* das nächste Monat durchnummerieren */
        for( int i = maxDays+(offset), count=1; i < 42; i++, count++ )
        {
            days.get(i).setDay(Integer.toString(count));
            days.get(i).setInactive();
            
            InfoRenderer renderer = days.get(i).getInfoRenderer();
            
            if( renderer != null )
                renderer.setDay(cal.plusDays(maxDays + count - 1));
        }
        
        DateMidnight cal2 = new DateMidnight(Year,Month,1);
        DateMidnight cal3 = cal2.minusMonths(1);
             
        int maxDaysbefore = cal3.toGregorianCalendar().getActualMaximum(GregorianCalendar.DAY_OF_MONTH);        
        
        System.out.println( "maxDaysBefore:" + maxDaysbefore );
        
        /* das vorhergehende Monat durchnummerieren */
        for( int i = 0, count=maxDaysbefore-offset+1; i < offset; i++, count++ )
        {
            days.get(i).setDay(Integer.toString(count));
            days.get(i).setInactive();
            
            InfoRenderer renderer = days.get(i).getInfoRenderer();
            
            if( renderer != null )
                renderer.setDay(cal3.plusDays(count - 1));
        }                        
        
        jLTitle.setText( MonthNames.getFullMonthName(month)+ " " + Integer.toString(cal.getYear()) );
        
        /* Feiertage anzeigen */
        if( holidays != null )
        {
            Collection<HolidayInfo> hdays = holidays.getHolidays(Year);
            
            for( HolidayInfo hinfo : hdays )
            {        
                    LocalDate d = hinfo.date;
                
                    if( d.getMonthOfYear() == Month )
                    {
                        DisplayDay day = getDay(d.getDayOfMonth());
                        
                        if( day != null )
                        {                            
                            String extra = "";
                            
                            if( holidays.getNumberOfCountryCodes() > 1 )
                                extra = hinfo.CountryCode;
                            day.getInfoRenderer().setInfo(extra + " " + hinfo.name );                            

                            if( hinfo.official_holiday ) {
                                day.setSundayBackground();
                                day.setHoliday(true);
                            }
                            
                        } else {
                            System.out.println( "cant get " + d + "day: " + d.getDayOfMonth() );
                        }
                    } else {
                        // System.out.println( "ignoring " + d );
                    }
            }
        }
        
        setAktualDay();
        
        for( DisplayDay day : days )
            day.update();                
    }
    
    public void clear()
    {
        for( DisplayDay day : days )
        {
           day.clear();
        }
    }

    // from DayEventListener
    @Override
    public void onClicked(CalendarDay day) {
        
        if( !day.isActive() && !allowClickOnInactiveDays )
            return;
        
        for( DisplayDay d : days )
        {
            if( d.isSelected() && d != day )
                d.setUnSelected();
        }
        
        day.setSelected();
        
        if( listener != null )
            listener.onClicked(day);
    }

    @Override
    public int getDaysOfMonth() {
        return daysOfMonth;
    }

    @Override
    public DisplayDay getDay(int day) {
        if( day > daysOfMonth )
            return null;
        
        if( day < 1 )
            return null;
        
        return days.get(day-1+offset);
    }

    @Override
    public void setListener(DayEventListener listener) {
        this.listener = listener;
    }

    @Override
    public int isWhatDayOfMonth(DisplayDay day) {
        int i = days.indexOf(day);
        
        if( i < 0 )
            return i;
        
        return (i - offset) + 1;
    }

    @Override
    public void setHolidays(Holidays holidays) {
        this.holidays = holidays;
    }

    @Override
    public void setInfoRenderer(InfoRenderer renderer) {
        info_renderer = renderer;
        
        for( DisplayDay day : days )
        {
            day.setInfoRenderer(renderer.getNewInstance());
        }
    }

    @Override
    public int getMonth() {
        return month;
    }

    @Override
    public int getYear() {
        return year;
    }

    private void setAktualDay() {
        DateMidnight today = new DateMidnight();
        
        int this_year = today.getYear();        
        int this_mon = today.getMonthOfYear();
        int this_day = today.getDayOfMonth();
        
        if( year != this_year )
            return;
        
        if( month != this_mon )
            return;
        
        DisplayDay dd = getDay(this_day);
        
        if( dd == null )
            return;
        
        dd.setToday();
    }

    @Override
    public Holidays getHolidays() {
       return holidays;
    }


}
