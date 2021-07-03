package at.redeye.FrameWork.widgets.calendarday;

import java.awt.*;

public class CalendarDay extends javax.swing.JPanel implements DisplayDay {

    private DayEventListener listener = null;
    private final Color INACTIVE_COLOR = new Color(214,214,214);
    private Color ACTIVE_COLOR;
    private final Color SELECTED_COLOR = new Color(220,255,220);
    private final Color BG_NORMAL = new Color(255,255,255);
    private final Color BG_SATURDAY = new Color(220,240,255);
    private final Color BG_SUNDAY = new Color(240,200,200);
    private final Color BG_TODAY = new Color(236,219,166);
    private boolean isActive = true;
    private boolean isSelected = false;
    private InfoRenderer renderer = null;
    private int weekday = 0;
    private boolean is_holiday = false;

    /** Creates new form Day */
    public CalendarDay() {

        ACTIVE_COLOR = BG_NORMAL;

        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDay = new javax.swing.JLabel();
        jSum = new javax.swing.JLabel();
        js = new javax.swing.JScrollPane();
        jInfo = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 255, 255)));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jDay.setText("31");
        jDay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDayMouseClicked(evt);
            }
        });

        jSum.setText("Summe");
        jSum.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jSumMouseClicked(evt);
            }
        });

        js.setBackground(new java.awt.Color(255, 255, 255));
        js.setBorder(null);
        js.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        js.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jInfo.setBackground(new java.awt.Color(255, 255, 255));
        jInfo.setText("jInfo");
        jInfo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jInfo.setOpaque(true);
        jInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jInfoMouseClicked(evt);
            }
        });
        js.setViewportView(jInfo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSum, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jDay))
            .addComponent(js, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(js, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jDay)
                    .addComponent(jSum, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

private void jInfoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jInfoMouseClicked

    clicked();
}//GEN-LAST:event_jInfoMouseClicked

private void jSumMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSumMouseClicked

    clicked();
}//GEN-LAST:event_jSumMouseClicked

private void jDayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDayMouseClicked

    clicked();
}//GEN-LAST:event_jDayMouseClicked

private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

    clicked();
}//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jDay;
    private javax.swing.JLabel jInfo;
    private javax.swing.JLabel jSum;
    private javax.swing.JScrollPane js;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setInfo(String info) {
        String text =  jInfo.getText();

        if( text != null )
        {
            if( info.equals(text) )
                return;
        }

        jInfo.setText(info);
    }

    @Override
    public void setSum(String sum) {
        jSum.setText(sum);
    }

    @Override
    public void setDay(String day) {
        jDay.setText(day);
    }

    @Override
    public void setListener(DayEventListener listener) {
        this.listener = listener;
    }

    public void clicked()
    {
        if( listener != null )
            listener.onClicked(this);
    }

    @Override
    public void setBackground( Color col )
    {
        super.setBackground(col);

        if( js != null )
            js.setBackground(col);

        if( jInfo != null )
            jInfo.setBackground(col);
    }

    @Override
    public void clear() {
        setSum("");
        setDay("");
        setInfo("");
        setActive();
        setNormalBackground();

        if( renderer != null )
            renderer.clear();

        weekday = 0;
        is_holiday = false;
    }

    @Override
    public void setActive() {
        isActive = true;
        setBackground(ACTIVE_COLOR);
    }

    @Override
    public void setInactive() {
        isActive = false;
        setBackground(INACTIVE_COLOR);
    }

    @Override
    public void setSelected() {
        isSelected = true;
        setBackground(SELECTED_COLOR);
    }

    @Override
    public void setUnSelected() {
        if(isActive)
            this.setActive();
        else
            this.setInactive();

        isSelected = false;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setNormalBackground() {
        ACTIVE_COLOR = BG_NORMAL;
        setBackground(ACTIVE_COLOR);
    }

    @Override
    public void setSaturdayBackground() {
        ACTIVE_COLOR = BG_SATURDAY;
        setBackground(ACTIVE_COLOR);
    }

    @Override
    public void setSundayBackground() {
        ACTIVE_COLOR = BG_SUNDAY;
        setBackground(ACTIVE_COLOR);
    }

    @Override
    public void setInfoRenderer(InfoRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void update()
    {
        if( renderer != null )
        {
            renderer.update();
            setInfo(renderer.render());
            setSum(renderer.renderSum());
        }
    }

    @Override
    public InfoRenderer getInfoRenderer() {
        return renderer;
    }

    @Override
    public void setWeekDay(int day) {
        weekday = day;
    }

    @Override
    public int getWeekDay() {
        return weekday;
    }

    @Override
    public boolean isSunday() {
        return weekday == 7;
    }

    @Override
    public boolean isSaturday() {
        return weekday == 6;
    }

    @Override
    public void setHoliday(boolean is_holiday) {
        this.is_holiday = is_holiday;
    }

    @Override
    public boolean isHoliday() {
        return is_holiday;
    }

    @Override
    public void setToday() {
        ACTIVE_COLOR = BG_TODAY;
        setBackground(ACTIVE_COLOR);
    }
}
