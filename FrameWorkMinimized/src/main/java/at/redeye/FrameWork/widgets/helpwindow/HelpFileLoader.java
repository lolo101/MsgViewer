package at.redeye.FrameWork.widgets.helpwindow;

import at.redeye.FrameWork.utilities.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HelpFileLoader {

	private static final Logger logger = LogManager.getLogger(HelpFileLoader.class);
	private static final Pattern IMG_TAG = Pattern.compile("<img\\s+");
	private static final Pattern SRC_ATTRIBUTE = Pattern.compile("src\\s*=\\s*\"(?<value>.*?)\"");

	private String replace_src(String s) {
		String src = StringUtils.strip(s, " \t\r\n\"");
		URL resource = getClass().getResource(src);
		if (resource == null) {
			logger.error("Cannot load Image: " + src);
			return s;
		}
		logger.info("image: " + src);
		return resource.toString();
	}

	private String prepareImages(String s) {
		StringBuilder res = new StringBuilder(s);

		int tagFromIndex = 0;
		for (Matcher imgMatcher = IMG_TAG.matcher(res); imgMatcher.find(tagFromIndex); ) {
			int attributeFromIndex = imgMatcher.end();
			Matcher srcMatcher = SRC_ATTRIBUTE.matcher(res);
			if (srcMatcher.find(attributeFromIndex)) {
				int attributeValueStart = srcMatcher.start("value");
				int attributeValueEnd = srcMatcher.end("value");

				String substitute = replace_src(res.substring(attributeValueStart, attributeValueEnd));

				res.replace(attributeValueStart, attributeValueEnd, substitute);
			}
			tagFromIndex = attributeFromIndex;
		}

		return res.toString();
	}

        public static String getResourceName( String Base, String ModuleName )
        {
            return Base + "/" + ModuleName + ".html";
        }

	public String loadHelp(String Base, String ModuleName) throws IOException {
		String resource = getResourceName(Base, ModuleName);

		resource = resource.replaceAll("//", "/");

		try (InputStream stream = getClass().getResourceAsStream(resource)) {
			if (stream == null) {
				System.out.println("Failed loading resource:" + resource);
				return "";
			}
			System.out.println("Loading resource:" + resource);

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream, StandardCharsets.UTF_8))) {
				String res = reader.lines().collect(Collectors.joining());
				return prepareImages(res);
			}
		}
	}
}
