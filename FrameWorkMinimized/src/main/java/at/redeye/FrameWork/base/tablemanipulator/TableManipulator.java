  /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.*;
import at.redeye.FrameWork.base.bindtypes.*;
import at.redeye.FrameWork.utilities.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author martin
 */
public class TableManipulator {

    private DBStrukt binddesc = null;
    private Vector<Integer> hidden_values = new Vector<Integer>();

    TableDesign tabledesign;
    JTable table;
    NormalTableModel model;
    boolean allEditable = false;
    Root root;
    RowHeader row_header;
    int auto_show_row_header = 20;
    private static Logger logger = LogManager.getLogger(TableManipulator.class);
    TableEditorStopper editor_stopper;
    BaseDialogBase base_dlg;
    boolean allowReordering = true;
    boolean allowResorting = true;
    boolean saveUserColWidth = true;
    Runnable closeListener = null;

    public TableManipulator( Root root, JTable table, TableDesign tabledesign )
    {
        this.tabledesign = tabledesign;
        this.table = table;
        this.model = new NormalTableModel(tabledesign);
        this.root = root;
        table.setModel(model);
        table.setDefaultRenderer(Object.class, new NormalCellRenderer(root, this.tabledesign));
        row_header = new RowHeader( table,  new Runnable() {

            @Override
            public void run() {
                checkRowHeaderLimit();
            }
        } );

        editor_stopper = new TableEditorStopper(table);

        readShowHeaderLimit();
        addCloseListener();
    }

    public TableManipulator( Root root, JTable table, DBStrukt binddesc )
    {
        this.root = root;
        readShowHeaderLimit();
        configure( table, binddesc, false );
        addCloseListener();
    }

    public TableManipulator( Root root, JTable table, DBStrukt binddesc, boolean allEditable )
    {
        this.root = root;
        readShowHeaderLimit();
        configure( table, binddesc, allEditable );
        addCloseListener();
    }

    public void setSaveUserColWidth( boolean state )
    {
        saveUserColWidth = state;
    }

    protected boolean isHidden( int i )
    {
        for( Integer ii = 0; ii < hidden_values.size(); ii++ )
        {
            if( hidden_values.get(ii).equals(i) )
                return true;
        }

        return false;
    }

    /**
     * reconfigure the table for a new bindtype
     * @param binddesc
     */
    public void reconfigure(DBStrukt binddesc)
    {
        hidden_values.clear();
        configure( table, binddesc, allEditable );
    }

    private void configure( JTable table, DBStrukt binddesc, boolean allEditable )
    {
        this.binddesc = binddesc;
        this.allEditable = allEditable;

        if( editor_stopper == null )
        {
            // ansonten hängen wir mehrere listener drann und das wollen wir nicht.
            editor_stopper = new TableEditorStopper(table);
        }

        Vector<TableDesign.Coll> vec = new Vector<TableDesign.Coll>();

        ArrayList<String> names = binddesc.getAllNames();
        ArrayList<DBValue> values = binddesc.getAllValues();

        for( int i = 0; i < names.size(); i++ )
        {
            if( !isHidden(i) )
                vec.add( new TableDesign.Coll( names.get(i), false, values.get(i) ) );
        }

        this.tabledesign = new TableDesign( vec );
        this.table = table;
        this.model = new NormalTableModel(tabledesign);
        table.setModel(model);
        table.setDefaultRenderer(Object.class, new NormalCellRenderer(root, this.tabledesign));
        row_header = new RowHeader( table, new Runnable() {

            @Override
            public void run() {
                checkRowHeaderLimit();
            }
        } );
    }

    public void autoResize()
    {
        autoResizeColWidth( table );
        setUserColWidth();
    }

    public void autoResizeNoUserColWidth()
    {
        autoResizeColWidth( table );
    }

     public void autoResizeColWidth(JTable table ) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        String smargin_default = root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.SpreadSheetMarginReadOnly);
        String smargin_editable = root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.SpreadSheetMarginEditable);

        int margin_default = Integer.valueOf(smargin_default);
        int margin_editable = Integer.valueOf(smargin_editable);

        int max_height = 0;

        for (int i = 0; i < table.getColumnCount(); i++) {
            int                     vColIndex = i;
            DefaultTableColumnModel colModel  = (DefaultTableColumnModel) table.getColumnModel();
            TableColumn             col       = colModel.getColumn(vColIndex);
            int                     width     = 0;

            // Get width of column header
            TableCellRenderer renderer = col.getHeaderRenderer();

            if (renderer == null) {
                renderer = table.getTableHeader().getDefaultRenderer();
            }

            Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);

            int width_header = comp.getPreferredSize().width;

            // Get maximum width of column data
            for (int r = 0; r < table.getRowCount(); r++) {
                renderer = table.getCellRenderer(r, vColIndex);
                comp     = renderer.getTableCellRendererComponent(table, table.getValueAt(r, vColIndex), false, false,
                        r, vColIndex);

                Dimension dim = comp.getPreferredSize();

                width = Math.max(width, dim.width);

                // System.out.println("hieght: " + dim.height + " row: " + (r +1) + " col: " + (i+1) );

                max_height = Math.max(max_height, dim.height);
            }


            if( tabledesign.colls.get(vColIndex).isEditable )
            {
                if( width_header <= width )
                    width += 2 * margin_editable;
                else
                    width = width_header += margin_default;
            } else {
                // Add margin
                if( width_header <= width )
                    width += 2 * margin_default;
                else
                    width = width_header += margin_default;
            }

            // Set the width
            col.setPreferredWidth(width);
        }

        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(
            SwingConstants.LEFT);

        setReorderingAllowed(allowReordering);
        setResortingAllowed(allowResorting);

        if( max_height > 0 )
        {

            int correction = 0;

            LookAndFeel look_and_feel = UIManager.getLookAndFeel();

            if( look_and_feel != null )
            {
                logger.info("look and feel: " + look_and_feel.getID() );

                if( Setup.is_linux_system() )
                {
                    correction = 1;

                    if( look_and_feel.getID().equals("Nimbus") )
                        correction = 3;
                }
                else // Windows
                {
                    correction=2;

                    if( look_and_feel.getID().equals("Nimbus") )
                        correction = 4;
                    else if( look_and_feel.getID().equals("Windows") )
                        correction = 0;
                }
            }

            //System.out.println(String.format("height: %d",max_height) );
            row_header.setCellHeight(max_height-correction);
        }
 /*
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);

            column.setCellRenderer(new DefaultTableColour());
        }

        return table;
  */
    }

    public void setReorderingAllowed(boolean state) {
        allowReordering = state;
        table.getTableHeader().setReorderingAllowed(state);
    }

    public void setResortingAllowed(boolean state) {
        allowResorting = state;
        table.setAutoCreateRowSorter(state);
    }

    public void add(DBStrukt binddesc)
    {
        ArrayList<DBValue> values = binddesc.getAllValues();

        addRow( values );
    }

    public void add(DBStrukt strukt, boolean set_edited )
    {
        add( strukt );

        if( set_edited )
            tabledesign.edited_rows.add(tabledesign.rows.size()-1);
    }

    public void add(DBStrukt strukt, boolean set_edited, boolean scrool_to_last_row )
    {
        add( strukt, set_edited);
        scrollToLastRow();
    }

    public <T extends DBStrukt> void addAll( Collection<T> col)
    {
        for( DBStrukt s : col )
        {
            ArrayList<DBValue> values = s.getAllValues();

            addRow( values, false );
        }

        checkRowHeaderLimit();
        row_header.updateUI();
    }

    public void prepareTable()
    {
        for( TableDesign.Coll coll : tabledesign.colls )
        {
            model.addColumn(MlM(coll.Title));
        }

        /* Das muß so sein,
         * da bei der ersten for Schleife der CellEditor resetted wird
         * anscheinend
         */
        for( int i = 0; i < table.getColumnCount(); i++ )
        {
            TableColumn col = table.getColumnModel().getColumn(i);

            TableDesign.Coll tcoll = tabledesign.colls.get(i);

            if( tcoll.dbval instanceof DBEnum ) {
                col.setCellEditor(new AdvancedEnumTableCellEditor(tabledesign, (DBEnum)tcoll.dbval));
            } else if( tcoll.dbval instanceof DBEnumAsInteger ) {
                col.setCellEditor(new AdvancedEnumTableCellEditor(tabledesign, (DBEnumAsInteger)tcoll.dbval));
            } else if( tcoll.dbval instanceof DBSqlAsInteger ) {
                col.setCellEditor(new AdvancedEnumTableCellEditor(tabledesign, (DBSqlAsInteger)tcoll.dbval));
            } else {
                col.setCellEditor(new AdvancedTableCellEditor(tabledesign));
            }
        }
    }

    public void addRow( Collection<?> data )
    {
        addRow(data,true);
    }


    private void addRow( Collection<?> data, boolean update_ui )
    {
        /* Wir müssen hier einen 2. Vector anlegen,
         * da der eine an die Tabelle angebunden wird
         * und wenn über den TableVelidator ein
         * anderer Anzeige Format String verwendet wird
         * wird unser ursprüngliches Objekt in
         * table_copy durch einen String ersetzt,
         * und weil das alles Referenzen sind,
         * würde dies auch mit unserem db_copy
         * Vector passieren.
         * Deswegen der 2. Vector.
         */


        Vector<Object> table_copy = new Vector<Object>();
        Vector<Object> db_copy = new Vector<Object>();

        int i = 0;
        for( Object d : data )
        {
            if( !hidden_values.contains(i) ) {
                table_copy.add( d );
                db_copy.add( d );
            }
            i++;

        }

        model.addRow(table_copy);
        tabledesign.rows.add(db_copy);

        if( update_ui ) {
            checkRowHeaderLimit();
            row_header.updateUI();
        }
    }

    public void scrollToLastRow()
    {
        scrollToVisible(table,model.getRowCount()-1,0);
    }

    public void scrollToRow( int row )
    {
        scrollToVisible(table,row,0);
    }

    public static void scrollToVisible(JTable table, int rowIndex, int vColIndex) {
        if (!(table.getParent() instanceof JViewport)) {
            return;
        }
        JViewport viewport = (JViewport)table.getParent();

        // This rectangle is relative to the table where the
        // northwest corner of cell (0,0) is always (0,0).
        Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);

        // The location of the viewport relative to the table
        Point pt = viewport.getViewPosition();

        // Translate the cell location so that it is relative
        // to the view, assuming the northwest corner of the
        // view is (0,0)
        rect.setLocation(rect.x-pt.x, rect.y-pt.y + table.getRowHeight());

        table.scrollRectToVisible(rect);

        // Scroll the area into view
        viewport.scrollRectToVisible(rect);
    }

    public void clear()
    {
        int i;
        while( ( i = model.getRowCount() ) > 0 )
            model.removeRow( i-1 );

        tabledesign.edited_cols.clear();
        tabledesign.edited_rows.clear();
        tabledesign.rows.clear();
        tabledesign.coloredCells.clear();

        checkRowHeaderLimit();
        row_header.updateUI();
    }

    public void remove( int row )
    {
        editor_stopper.doPause();
        logger.info(("PAUSE PAUSE PAUSE"));

        model.removeRow(row);
        tabledesign.rows.remove(row);

        Object rows[] = getEditedRows().toArray();

        HashSet<Integer> er = new HashSet<Integer>();

        for( int i = 0; i < rows.length; i++ )
        {
            if( (Integer)rows[i] == row )
            {
                continue;
            }

            if( (Integer)rows[i] < row )
            {
                er.add( new Integer(i));
            }
            else
            {
                er.add(new Integer(i-1));
            }
        }

        tabledesign.edited_rows = er;

        checkRowHeaderLimit();
        row_header.updateUI();

        logger.info(("CONTINUE CONTINUE CONTINUE"));

        editor_stopper.doContinue();
    }

    public Set<Integer> getEditedRows()
    {
        return tabledesign.edited_rows;
    }

    public void setEditedAll()
    {
        for( int i = 0; i < tabledesign.rows.size(); i++ )
            tabledesign.edited_rows.add(i);
    }

    public void setEdited( int row)
    {
        tabledesign.edited_rows.add(row);
    }


    public boolean isEdited()
    {
        if( tabledesign.edited_rows == null )
            return false;

        if( tabledesign.edited_rows.isEmpty() )
            return false;

        return true;
    }

    public void setEditable( DBValue column )
    {
        setEditable( column, true );
    }

    public void setEditable( DBValue column, boolean isEditable )
    {
        ArrayList<DBValue> values = binddesc.getAllValues();

        for( int i = 0, col=0; i < values.size(); i++ )
        {
            if( isHidden( i ) )
                continue;

            if( values.get(i).hashCode() == column.hashCode() )
            {

                tabledesign.colls.get(col).setEditable( isEditable );
                return;
            }

            col++;
        }
    }

    /**
     * disable, or enables the Autocompletet feature for this column
     * @param column
     * @param doAutocomplete
     */
    public void setAutoCompleteForAllOfThisColl(DBValue column, boolean doAutocomplete) {
        ArrayList<DBValue> values = binddesc.getAllValues();

        for (int i = 0, col = 0; i < values.size(); i++) {
            if (isHidden(i)) {
                continue;
            }

            if (values.get(i).hashCode() == column.hashCode()) {
                tabledesign.colls.get(col).setDoAutocompleteForAllOfThisColl(doAutocomplete);
                return;
            }

            col++;
        }
    }

    /**
     * disable, or enables the Autocompletet feature
     * @param column
     * @param doAutocomplete
     */
    public void setAutoCompleteForCollAtAll(DBValue column, boolean doAutocomplete) {
        ArrayList<DBValue> values = binddesc.getAllValues();

        for (int i = 0, col = 0; i < values.size(); i++) {
            if (isHidden(i)) {
                continue;
            }

            if (values.get(i).hashCode() == column.hashCode()) {
                tabledesign.colls.get(col).setAutoCompleteForCollAtAll(doAutocomplete);
                return;
            }

            col++;
        }
    }


    public void setValidator(DBValue column,TableValidator validator)
    {
        ArrayList<DBValue> values = binddesc.getAllValues();

        for( int i = 0, col=0; i < values.size(); i++ )
        {
            if( isHidden( i ) )
                continue;

            if( values.get(i).hashCode() == column.hashCode() )
            {
                tabledesign.colls.get(col).validator = validator;
                return;
            }

            col++;
        }
    }

    public void setAdditionalAutocompleteData( DBValue column, Vector<Object> data )
    {
        ArrayList<DBValue> values = binddesc.getAllValues();

        for( int i = 0, col=0; i < values.size(); i++ )
        {
            if( isHidden( i ) )
                continue;

            if( values.get(i).hashCode() == column.hashCode() )
            {
                tabledesign.colls.get(col).additional_autocomplete_values = data;
                return;
            }

            col++;
        }
    }

    public void hide( DBValue ... columns )
    {
        ArrayList<DBValue> col_list = new ArrayList();

        for (DBValue column : columns)
        {
            col_list.add(column);
        }

        hide( col_list );
    }

    public void hide( DBStrukt hist )
    {
        hide( hist.getAllValues() );
    }

    public void hide( List<DBValue> columns )
    {
        for (DBValue column : columns )
        {
            if( column == null )
                continue;

            ArrayList<DBValue> values = binddesc.getAllValues();

            boolean found = false;

            for (int i = 0; i < values.size(); i++) {
                if (values.get(i).hashCode() == column.hashCode()) {
                    hidden_values.add(i);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Didn't found: " + column.getName());
                return;
            }
        }

        configure( table, binddesc, allEditable );
    }


    public void stopEditing()
    {
       TableCellEditor ce =  table.getCellEditor();

       if( ce != null )
           ce.stopCellEditing();
    }


    public void setCellColor (DBValue column, int row, Color color) {

        ArrayList<DBValue> values = binddesc.getAllValues();


        for( int i = 0, col=0; i < values.size(); i++ )
        {
            if( isHidden( i ) )
                continue;


            if( values.get(i).getName().equals(column.getName()) )
            {
                tabledesign.addColoredCell(row, col, color);
                return;
            }

            col++;
        }
    }

    public void setToolTip (DBValue column, int row, String tooltip) {

        ArrayList<DBValue> values = binddesc.getAllValues();


        for( int i = 0, col=0; i < values.size(); i++ )
        {
            if( isHidden( i ) )
                continue;


            if( values.get(i).getName().equals(column.getName()) )
            {
                tabledesign.addToolTipCell(row, col, tooltip);
                return;
            }

            col++;
        }
    }

    public void updateValue( DBValue value, int row )
    {
        ArrayList<DBValue> values = binddesc.getAllValues();

        for( int i = 0, col=0; i < values.size(); i++ )
        {
            if( isHidden( i ) )
                continue;

            if( values.get(i).getName().equals(value.getName()) )
            {
                model.setValueAt(value, row, col);
                //model.fireTableCellUpdated(row, col);
                return;
            }

            col++;
        }
    }

    public void updateUI()
    {
        model.fireTableDataChanged();
        row_header.updateUI();
        //table.updateUI();
    }

    /**
     * @return -1 if nothing was selected
     */
    public int getSelectedRow()
    {
        int row = table.getSelectedRow();

        if( row < 0 || row > table.getRowCount() )
            return row;

        return TableDesign.getModelRow(table, row);
    }

    public TableDesign getTabledesign() {
        return tabledesign;
    }

    public void showRowHeader(boolean selected)
    {
        if( selected )
            showRowHeader();
        else
            hideRowHeader();
    }

    public void showRowHeader()
    {
        row_header.setVisible(true);
    }

    public void hideRowHeader()
    {
        row_header.setVisible(false);
    }

    private void checkRowHeaderLimit()
    {
        if( auto_show_row_header < 0 )
        {
            row_header.setVisible(false);
        }
        else if( auto_show_row_header == 0 )
        {
             row_header.setVisible(true);
        }
        else
        {
            if (table.getRowCount() < auto_show_row_header &&
                !row_header.isScrollBarVisible() ) {
                row_header.setVisible(false);
            } else {
                row_header.setVisible(true);
            }
        }
    }

    private void readShowHeaderLimit()
    {
        try {
            auto_show_row_header = Integer.parseInt(root.getSetup().getLocalConfig(
                    FrameWorkConfigDefinitions.SpreadSheetRowHeaderLimit));

        } catch ( NumberFormatException ex ) {
            logger.error(StringUtils.exceptionToString(ex));
        }
    }

    public void disableAutoRowHeader()
    {
        auto_show_row_header = -1;
    }

    public void enableAutoRowHeader()
    {
        readShowHeaderLimit();
    }

    private void addCloseListener()
    {
        BaseDialogBase base = getBaseDialog();

        if( base == null )
            return;

        if( closeListener == null )
        {
            closeListener = new Runnable() {

                @Override
                public void run() {
                    if( saveUserColWidth )
                        saveTableHeaderSize();
                }
            };

            base.registerOnCloseListener(closeListener);
        }
    }

    private BaseDialogBase getBaseDialog()
    {
        if( base_dlg == null )
            base_dlg = getBaseDialogInt();

        return base_dlg;
    }

    private BaseDialogBase getBaseDialogInt()
    {
        Container parent = table;

        do
        {
            parent = parent.getParent();

            if( parent instanceof BaseDialog ||
                parent instanceof BaseDialogDialog )
            {
                return (BaseDialogBase) parent;
            }

        } while( parent != null );

        return null;
    }

    private String getUniqueSaveIdForTable()
    {
        BaseDialogBase base = getBaseDialog();

        if( base == null )
            return null;

        return base.getUniqueDialogIdentifier(this);
    }

    public void saveTableHeaderSize()
    {
        if( binddesc == null )
        {
            logger.error("save Table size without a binddesc not testet yet");
            return;
        }

        String uid = getUniqueSaveIdForTable();

        if( uid == null )
        {
            logger.error("die Tabelle befindet sich nicht in einem BaseDialog sichern nicht möglich!");
        }

        uid += binddesc.getName();

        JTableHeader header = table.getTableHeader();

        logger.info("saving cols width for binddesc: " + binddesc.getName() );

        Setup setup = root.getSetup();

        for( int j = 0; j < table.getColumnCount(); j++ )
        {
            Rectangle col_rect = header.getHeaderRect(j);

            TableDesign.Coll col =  tabledesign.colls.get(TableDesign.getModelCol(table, j));

            logger.info(j + ": " + col_rect.width + " " +col.Title + " => " +  col.dbval.getName() );

            String col_uid = uid + "_" + col.dbval.getName();

            // das j hinten drann ist die Position an der sich die Spalte befindet
            setup.setLocalConfig(col_uid, String.valueOf(col_rect.width) + "," + j);
        }
    }

    private void setUserColWidth()
    {
        if( binddesc == null )
        {
            logger.error("save Table size without a binddesc not testet yet");
            return;
        }

        String uid = getUniqueSaveIdForTable();

        if( uid == null )
        {
            logger.error("die Tabelle befindet sich nicht in einem BaseDialog sichern nicht möglich!");
        }

        uid += binddesc.getName();
        Setup setup = root.getSetup();

        // remember the position of each column
        // in this array
        List<Entry<String,Integer>> positions = new ArrayList();

        for (int i = 0; i < table.getColumnCount(); i++) {
            DefaultTableColumnModel colModel  = (DefaultTableColumnModel) table.getColumnModel();
            TableColumn             tcol       = colModel.getColumn(i);

            int width = 0;

            TableDesign.Coll col =  tabledesign.colls.get(TableDesign.getModelCol(table, i));

            String col_uid = uid + "_" + col.dbval.getName();

            String val = setup.getLocalConfig(col_uid,"");

            logger.debug(col_uid + "=" + val);

            if( val.isEmpty() )
                continue;

            String values[] = val.split(",");

            try
            {
                width = Integer.parseInt(values[0]);

                if( values.length > 1 )
                    positions.add( new AbstractMap.SimpleEntry<String, Integer>(col_uid,Integer.parseInt(values[1])));

            } catch( NumberFormatException ex ) {
                logger.error(StringUtils.exceptionToString(ex));
                positions.add(new AbstractMap.SimpleEntry<String, Integer>(col_uid,-1));
            }

            if( width > 5 )
                tcol.setPreferredWidth(width);
        }

        if (allowReordering) {
            // Wiederherstellen der Spalten, so wie es das letzte mal
            // abgespeichert war

            ColumnOrder orderer = new ColumnOrder(table);

            for (int i = 0; i < positions.size(); i++)
            {
                Entry<String,Integer> entry = positions.get(i);
                orderer.addColumn(entry.getKey(), i, entry.getValue());
            }

            orderer.moveColumns();
/*
            List<Integer> dont_move_anymore = new ArrayList<Integer>();

            for (int i = 0; i < positions.size(); i++) {
                int index = positions.get(i);

                if (dont_move_anymore.contains(i)) {
                    continue;
                }

                if (index >= 0 && index != i && index < table.getColumnCount()) {
                    //System.out.println("i " + i + " = " + index );

                    table.getColumnModel().moveColumn(i, index);
                    dont_move_anymore.add(index);
                }
            }
            *
            */
        }
    }

    public String MlM( String message )
    {
        base_dlg = getBaseDialog();

        if( base_dlg == null )
            return message;

        return base_dlg.MlM(message);
    }
}
