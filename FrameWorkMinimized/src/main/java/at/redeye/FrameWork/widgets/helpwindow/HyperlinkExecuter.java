package at.redeye.FrameWork.widgets.helpwindow;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import at.redeye.FrameWork.base.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import java.io.IOException;
import java.util.Arrays;

public class HyperlinkExecuter implements HyperlinkListener {

    private static final Logger logger = LogManager.getLogger(HyperlinkExecuter.class);
    private final Root root;

    public HyperlinkExecuter(Root root) {
        this.root = root;
    }

    @Override
    public void hyperlinkUpdate(final HyperlinkEvent e) {
        new AutoMBox<>(HelpWin.class.getName(), () -> hyperlinkUpdate_int(e)).run();
    }

    public void hyperlinkUpdate_int(HyperlinkEvent e) throws IOException {
        if (!e.getEventType().equals(EventType.ACTIVATED))
            return;

        logger.info(e.getURL());

        String[] command_array = new String[2];
        command_array[0] = getOpenCommand();
        command_array[1] = e.getURL().toString();

        logger.info(Arrays.toString(command_array));

        Runtime.getRuntime().exec(command_array);
    }

    public String getOpenCommand() {
        return root.getSetup().getLocalConfig(FrameWorkConfigDefinitions.OpenCommand);
    }
}
