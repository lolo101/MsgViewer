module FrameWorkMinimized {
    requires transitive java.desktop;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    exports at.redeye.FrameWork.base;
    exports at.redeye.FrameWork.base.imagestorage;
    exports at.redeye.FrameWork.base.prm;
    exports at.redeye.FrameWork.base.prm.bindtypes;
    exports at.redeye.FrameWork.base.prm.impl;
    exports at.redeye.FrameWork.base.prm.impl.gui;
    exports at.redeye.FrameWork.base.tablemanipulator;
    exports at.redeye.FrameWork.Plugin;
    exports at.redeye.FrameWork.utilities;
    exports at.redeye.FrameWork.widgets;
    exports at.redeye.FrameWork.widgets.helpwindow;
    exports at.redeye.Plugins.ShellExec;
}