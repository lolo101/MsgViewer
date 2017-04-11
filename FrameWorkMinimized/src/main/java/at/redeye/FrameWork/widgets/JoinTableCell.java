/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FrameWork.widgets;

import at.redeye.FrameWork.base.bindtypes.DBSqlAsInteger;
import at.redeye.FrameWork.base.bindtypes.DBValue;
import at.redeye.FrameWork.base.tablemanipulator.TableDesign;
import at.redeye.FrameWork.base.tablemanipulator.TableValidator;
import at.redeye.FrameWork.base.transaction.Transaction;
import java.util.Vector;
import javax.swing.JComponent;

/**
 *
 * @author martin
 */
public class JoinTableCell extends TableValidator
{
    protected DBValue joinedColumn;
    protected Transaction trans;

    /**
     * This class refreshes the current Component before it will be edited
     * therefore the target column has to be a AutocompleteColumn and has to be
     * based on DBSqlAsIneteger Column.
     * This class simple adds via DBSqlAsInteger.query.setExtraSql() the joined value
     * of the joinedColumn. Therefore in the current table which is used by the query
     * the field has to be available.
     *
     * @param joinedColumn
     * @param trans
     */
    public JoinTableCell( DBValue joinedColumn, Transaction trans )
    {
        this.joinedColumn = joinedColumn;
        this.trans = trans;
    }


    @Override
    public void updateComponentBeforeEdit(JComponent component, Object value, TableDesign tabledesign, int row, int column) {
         doJoin((AutoCompleteCombo) component, joinedColumn, tabledesign, row, column, trans);
    }

    /**
     * param targetComponent  The target combobox where new Items should be loaded before edited.
     *                        It has to be a DBSqlAsInteger Column
     * @param JoindColumn     The column where it all depends
     * @param tabledesign     current tabledesign
     * @param row             current row
     * @param column          current col
     * @param trans           current transaction
     */
    public static void doJoin( AutoCompleteCombo targetComponent, DBValue JoinedColumn, TableDesign tabledesign, int row, int column, Transaction trans )
    {
        Vector<Object> cols = tabledesign.rows.get(row);

        for (int i = 0; i < cols.size(); i++)
        {
            Object col = cols.get(i);

            if (col instanceof DBValue)
            {
                DBValue val = (DBValue) col;

                if (val.getName().equals(JoinedColumn.getName()))
                {
                    DBSqlAsInteger easi = (DBSqlAsInteger) tabledesign.rows.get(row).get(column);

                    easi.query.setExtraSql(" and " + trans.markColumn(JoinedColumn) + "= " + val.getValue() );

                    easi.refresh();

                    targetComponent.set_items(easi.getPossibleValues());

                    break;
                }
            }
        }
    }

}
