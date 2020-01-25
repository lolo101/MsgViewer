/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FrameWork.base.prm.impl;

import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Mario
 */
public class PrmDBInit {

    private Root root;
    private Logger logger = LogManager.getLogger(PrmDBInit.class);

    public PrmDBInit(Root root) {
        this.root = root;
    }

    public void initDb() {
/*
        if( root.getDBConnection() != null )
        {
            try {

                Transaction trans = root.getDBConnection().getNewTransaction();
                Set<String> keys = GlobalConfigDefinitions.entries.keySet();
                for (String key : keys) {
                    DBConfig prm = root.getSetup().getConfig(key);
                    if (prm == null) {
                        prm = GlobalConfigDefinitions.get(key);
                        prm.hist.setAnHist("ModuleLauncher");
                        if (trans.insertValues(prm) != 1) {
                            logger.error("Failed to insert PRM <" + prm.getConfigName().toString() + ">\n");
                        }
                        trans.commit();
                        logger.info("PRM <" + prm.getConfigName().toString() + "> successfully inserted!");
                    } else {
                        logger.debug("PRM <" + prm.getConfigName().toString() + "> already exists in database!");
                    }
                }
                trans.rollback();
                root.getDBConnection().closeTransaction(trans);
            } catch (SQLException se) {
                logger.error("SQL-Error: " + se.getMessage());
            } catch (WrongBindFileFormatException we) {
                logger.error("Bind-Error: " + we.getMessage());
            } catch (UnsupportedDBDataTypeException ute) {
                logger.error("Wrong binding: " + ute.getMessage());
            } catch (IOException ioe) {
                logger.error("I/O error: " + ioe.getMessage());
            }
        }
*/

        Set<String> keys = LocalConfigDefinitions.entries.keySet();

        TreeMap<String, DBConfig> vals = new TreeMap<String, DBConfig>();

        for (String key : keys) {
            vals.put(key, LocalConfigDefinitions.get(key));
        }

        for (String key : keys) {
            DBConfig c = (DBConfig) vals.get(key).getCopy();
            root.getSetup().setLocalConfig(c.getConfigName(), c.getConfigValue(), true);
        }

        root.saveSetup();
    }

}
