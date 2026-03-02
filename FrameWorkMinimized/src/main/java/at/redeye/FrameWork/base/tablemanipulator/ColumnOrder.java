package at.redeye.FrameWork.base.tablemanipulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.util.ArrayList;
import java.util.Comparator;

public class ColumnOrder implements Comparator<Order> {
    private static final Logger logger = LogManager.getLogger(ColumnOrder.class);

    private final JTable table;
    private final ArrayList<Order> order_list = new ArrayList<>();

    public ColumnOrder(JTable table) {
        this.table = table;
    }

    void addColumn(String name, int position_now, int position_wanted) {
        if (position_wanted >= table.getColumnCount())
            position_wanted = -1;

        order_list.add(new Order(name, position_now, position_wanted));
    }

    private void sort() {
        order_list.sort(this);

        if (logger.isDebugEnabled())
        {
            for (Order order : order_list) {
                logger.debug(order);
            }
        }
    }

    @Override
    public int compare(Order order1, Order order2) {
        return Integer.compare(order1.position_now, order2.position_now);
    }

    void moveColumns()
    {
        boolean needs_sorting;

        final TableColumnModel model = table.getColumnModel();
        final int columns = table.getColumnCount();

        do
        {
            needs_sorting = false;

            sort();

            Order min_order = null;

            // suche jenen Eintrag der am weitesten nach vorne will
            for( Order order : order_list ) {
                if (min_order == null && order.isUnwantedPosition()) {
                    min_order = order;
                }

                if (min_order != null && order.isUnwantedPosition()) {
                    if (min_order.position_wanted > order.position_wanted) {
                        logger.debug(min_order.position_wanted + " > " + order.position_wanted);
                        min_order = order;
                    }
                }
            }

            {
                Order order = null;

                if (min_order == null) {
                    // order = order_list.get(0);
                    // finde ersten unzufriedenen
                    for (Order o : order_list) {
                        if (o.isUnwantedPosition()) {
                            order = o;
                            break;
                        }
                    }
                } else {
                    order = min_order;
                }

                if (order != null && order.isUnwantedPosition()) {
                    logger.debug(" => " + order);
                    model.moveColumn(order.position_now, order.position_wanted);

                    // alle Spalten, die dahinter liegen eine position runterzählen
                    for (int i = order.position_now + 1; i < columns && i < order_list.size(); i++) {
                        Order o = order_list.get(i);
                        o.position_now--;
                    }

                    // und jetzt alle Spalten die hinter der Einfügepositin liegen eine Spalte hinaufzählen
                    for (int i = order.position_wanted; i < columns && i < order_list.size(); i++) {

                        // mich selbst nicht hinaufzählen
                        if( i == order.position_now )
                            continue;

                        Order o = order_list.get(i);
                        o.position_now++;
                    }

                    order.position_now = order.position_wanted;
                    //   break;
                }
            }



            for( Order order : order_list )
            {
                if (order.isUnwantedPosition())
                    needs_sorting = true;
            }


        } while( needs_sorting );
    }

}
