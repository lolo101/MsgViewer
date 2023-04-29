package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.*;
import at.redeye.FrameWork.base.bindtypes.DBStrukt;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;

public class TableManipulator {

    private DBStrukt binddesc;
    private final Collection<Integer> hidden_values = new ArrayList<>();

    private TableDesign tabledesign;
    private JTable table;
    private boolean allEditable;
    private final Root root;
    private RowHeader row_header;
    private int auto_show_row_header = 20;
    private static final Logger logger = LogManager.getLogger(TableManipulator.class);
    private TableEditorStopper editor_stopper;
    private BaseDialogBase base_dlg;
    private boolean allowReordering = true;
    private boolean allowResorting = true;
    private static final boolean saveUserColWidth = true;

    public TableManipulator(Root root, JTable table, DBStrukt binddesc) {
        this(root, table, binddesc, false);
    }

    private TableManipulator(Root root, JTable table, DBStrukt binddesc, boolean allEditable) {
        this.root = root;
        readShowHeaderLimit();
        configure(table, binddesc, allEditable);
        addCloseListener();
    }

    private boolean isHidden(int i) {
        return hidden_values.contains(i);
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

        Vector<Coll> vec = new Vector<>();

        ArrayList<String> names = binddesc.getAllNames();
        List<DBValue> values = binddesc.getAllValues();

        for (int i = 0; i < names.size(); i++) {
            if (!isHidden(i))
                vec.add(new Coll(names.get(i), false, values.get(i)));
        }

        this.tabledesign = new TableDesign(vec);
        this.table = table;
        table.setModel(tabledesign);
        table.setDefaultRenderer(Object.class, new NormalCellRenderer(this.tabledesign));
        row_header = new RowHeader(table, this::checkRowHeaderLimit);
    }

    public void autoResize()
    {
        autoResizeColWidth( table );
        setUserColWidth();
    }

    private void autoResizeColWidth(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        String smargin_default = FrameWorkConfigDefinitions.SpreadSheetMarginReadOnly.getConfigValue();
        String smargin_editable = FrameWorkConfigDefinitions.SpreadSheetMarginEditable.getConfigValue();

        int margin_default = Integer.parseInt(smargin_default);
        int margin_editable = Integer.parseInt(smargin_editable);

        int max_height = 0;

        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumnModel colModel = table.getColumnModel();
            TableColumn col = colModel.getColumn(i);
            int width = 0;

            // Get width of column header
            TableCellRenderer renderer = col.getHeaderRenderer();

            if (renderer == null) {
                renderer = table.getTableHeader().getDefaultRenderer();
            }

            Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);

            int width_header = comp.getPreferredSize().width;

            // Get maximum width of column data
            for (int r = 0; r < table.getRowCount(); r++) {
                renderer = table.getCellRenderer(r, i);
                comp     = renderer.getTableCellRendererComponent(table, table.getValueAt(r, i), false, false,
                        r, i);

                Dimension dim = comp.getPreferredSize();

                width = Math.max(width, dim.width);

                max_height = Math.max(max_height, dim.height);
            }


            if( tabledesign.colls.get(i).isEditable )
            {
                if( width_header <= width )
                    width += 2 * margin_editable;
                else
                    width = width_header + margin_default;
            } else {
                // Add margin
                if( width_header <= width )
                    width += 2 * margin_default;
                else
                    width = width_header + margin_default;
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

            row_header.setCellHeight(max_height-correction);
        }
    }

    private void setReorderingAllowed(boolean state) {
        allowReordering = state;
        table.getTableHeader().setReorderingAllowed(state);
    }

    private void setResortingAllowed(boolean state) {
        allowResorting = state;
        table.setAutoCreateRowSorter(state);
    }

    public void add(DBStrukt binddesc)
    {
        List<DBValue> values = binddesc.getAllValues();

        addRow( values );
    }

    public <T extends DBStrukt> void addAll(Iterable<T> col) {
        for (DBStrukt s : col) {
            List<DBValue> values = s.getAllValues();

            addRow(values, false);
        }

        checkRowHeaderLimit();
        row_header.updateUI();
    }

    public void prepareTable()
    {
        /* Das muß so sein,
         * da bei der ersten for Schleife der CellEditor resetted wird
         * anscheinend
         */
        for( int i = 0; i < table.getColumnCount(); i++ )
        {
            TableColumn col = table.getColumnModel().getColumn(i);

            col.setCellEditor(new AdvancedTableCellEditor(tabledesign));
        }
    }

    private void addRow(Iterable<?> data) {
        addRow(data, true);
    }


    private void addRow(Iterable<?> data, boolean update_ui) {
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


        List<Object> db_copy = new ArrayList<>();

        int i = 0;
        for( Object d : data )
        {
            if( !hidden_values.contains(i) ) {
                db_copy.add( d );
            }
            i++;

        }

        tabledesign.rows.add(db_copy);

        if( update_ui ) {
            checkRowHeaderLimit();
            row_header.updateUI();
        }
    }

    public void clear()
    {
        tabledesign.clear();
        checkRowHeaderLimit();
        row_header.updateUI();
    }

    public void remove( int row )
    {
        editor_stopper.doPause();
        logger.info(("PAUSE PAUSE PAUSE"));

        tabledesign.rows.remove(row);

        Object[] rows = getEditedRows().toArray();

        Set<Integer> er = new HashSet<>();

        for( int i = 0; i < rows.length; i++ )
        {
            if( (Integer)rows[i] == row )
            {
                continue;
            }

            if( (Integer)rows[i] < row )
            {
                er.add(i);
            }
            else
            {
                er.add(i - 1);
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


    public void setEditable( DBValue column )
    {
        List<DBValue> values = binddesc.getAllValues();

        for( int i = 0, col=0; i < values.size(); i++ )
        {
            if( isHidden( i ) )
                continue;

            if( values.get(i).hashCode() == column.hashCode() )
            {
                tabledesign.colls.get(col).setEditable();
                return;
            }

            col++;
        }
    }

    /**
     * disable or enables the Autocompletet feature for this column
     */
    public void setAutoCompleteForAllOfThisColl(DBValue column, boolean doAutocomplete) {
        List<DBValue> values = binddesc.getAllValues();

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


    public void hide(DBValue... columns) {
        hide(Arrays.asList(columns));
    }

    private void hide(Iterable<DBValue> columns) {
        for (DBValue column : columns) {
            if (column == null)
                continue;

            List<DBValue> values = binddesc.getAllValues();

            boolean missing = true;

            for (int i = 0; i < values.size(); i++) {
                if (values.get(i).hashCode() == column.hashCode()) {
                    hidden_values.add(i);
                    missing = false;
                    break;
                }
            }

            if (missing) {
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

    public void showRowHeader(boolean selected)
    {
        if( selected )
            showRowHeader();
        else
            hideRowHeader();
    }

    private void showRowHeader() {
        row_header.setVisible(true);
    }

    private void hideRowHeader() {
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
            row_header.setVisible(auto_show_row_header <= table.getRowCount() || row_header.isScrollBarVisible());
        }
    }

    private void readShowHeaderLimit()
    {
        try {
            auto_show_row_header = Integer.parseInt(FrameWorkConfigDefinitions.SpreadSheetRowHeaderLimit.getConfigValue());

        } catch ( NumberFormatException ex ) {
            logger.error(ex.getLocalizedMessage(), ex);
        }
    }

    private void addCloseListener()
    {
        BaseDialogBase base = getBaseDialog();

        if( base == null )
            return;

        base.registerOnCloseListener(() -> {
            if( saveUserColWidth )
                saveTableHeaderSize();
        });
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

    private String getUniqueSaveIdForTable() {
        BaseDialogBase base = getBaseDialog();

        if (base == null)
            return null;

        return base.getUniqueDialogIdentifier();
    }

    private void saveTableHeaderSize() {
        if (binddesc == null) {
            logger.error("save Table size without a binddesc not testet yet");
            return;
        }

        String uid = getUniqueSaveIdForTable();

        if (uid == null) {
            logger.error("die Tabelle befindet sich nicht in einem BaseDialog sichern nicht möglich!");
        }

        uid += binddesc.getName();

        JTableHeader header = table.getTableHeader();

        logger.info("saving cols width for binddesc: " + binddesc.getName() );

        Setup setup = root.getSetup();

        for( int j = 0; j < table.getColumnCount(); j++ )
        {
            Rectangle col_rect = header.getHeaderRect(j);

            Coll col = tabledesign.colls.get(TableDesign.getModelCol(table, j));

            logger.info(j + ": " + col_rect.width + " " + col.title + " => " + col.dbval.getName());

            String col_uid = uid + "_" + col.dbval.getName();

            // das j hinten drann ist die Position an der sich die Spalte befindet
            setup.setLocalConfig(col_uid, col_rect.width + "," + j);
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
        List<Entry<String,Integer>> positions = new ArrayList<>();

        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumnModel colModel = table.getColumnModel();
            TableColumn tcol = colModel.getColumn(i);

            int width = 0;

            Coll col = tabledesign.colls.get(TableDesign.getModelCol(table, i));

            String col_uid = uid + "_" + col.dbval.getName();

            String val = setup.getConfig(col_uid, "");

            logger.debug(col_uid + "=" + val);

            if( val.isEmpty() )
                continue;

            String[] values = val.split(",");

            try
            {
                width = Integer.parseInt(values[0]);

                if( values.length > 1 )
                    positions.add(new AbstractMap.SimpleEntry<>(col_uid, Integer.parseInt(values[1])));

            } catch( NumberFormatException ex ) {
                logger.error(ex.getLocalizedMessage(), ex);
                positions.add(new AbstractMap.SimpleEntry<>(col_uid, -1));
            }

            if( width > 5 )
                tcol.setPreferredWidth(width);
        }

        if (allowReordering) {
            ColumnOrder orderer = new ColumnOrder(table);

            for (int i = 0; i < positions.size(); i++)
            {
                Entry<String,Integer> entry = positions.get(i);
                orderer.addColumn(entry.getKey(), i, entry.getValue());
            }

            orderer.moveColumns();
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
