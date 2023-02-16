package at.redeye.FrameWork.widgets.helpwindow;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import java.io.IOException;
import java.util.Arrays;

public class HyperlinkExecuter implements HyperlinkListener {

    private static final Logger logger = LogManager.getLogger(HyperlinkExecuter.class);
    private static OpenUrlInterface open_url;

    @Override
    public void hyperlinkUpdate(final HyperlinkEvent e) {
        new AutoMBox<>(HelpWin.class.getName(), () -> hyperlinkUpdate_int(e)).run();
    }

    private static void hyperlinkUpdate_int(HyperlinkEvent e) throws IOException {
        if (!e.getEventType().equals(EventType.ACTIVATED))
            return;

        logger.info(e.getURL());

        if (open_url != null) {
            open_url.openUrl(e.getURL().toString());
        } else {
            String[] command_array = new String[2];
            command_array[0] = getOpenCommand();
            command_array[1] = e.getURL().toString();

            logger.info(Arrays.toString(command_array));

            Runtime.getRuntime().exec(command_array);
        }
    }

    private static String getOpenCommand() {
        return FrameWorkConfigDefinitions.OpenCommand.getConfigValue();
    }

    public static void setOpenUrl(OpenUrlInterface open_url_) {
        open_url = open_url_;
    }
}
