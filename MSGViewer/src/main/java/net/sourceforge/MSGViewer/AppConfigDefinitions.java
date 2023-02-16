package net.sourceforge.MSGViewer;

import at.redeye.FrameWork.base.BaseAppConfigDefinitions;
import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.prm.impl.LocalConfigDefinitions;
import at.redeye.FrameWork.base.prm.impl.PrmDefaultCheckSuite;

public class AppConfigDefinitions extends BaseAppConfigDefinitions {

    public static DBConfig IconSize = new DBConfig("IconSize", "100", "Icon Größe", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_LONG));


    public static void registerDefinitions() {

        BaseRegisterDefinitions();

        addLocal(IconSize);

        LocalConfigDefinitions.add_help_path("/net/souceforge/MSGViewer/resources/Help/Params/");
    }
}
