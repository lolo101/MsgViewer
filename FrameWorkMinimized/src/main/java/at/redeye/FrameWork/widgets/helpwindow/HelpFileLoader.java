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
import java.util.List;
import java.util.stream.Collectors;

public class HelpFileLoader {

	private static final Logger logger = LogManager.getLogger(HelpFileLoader.class);

	private static int findImgTag(String s) {
		int pos;
		int start = 0;

		do {
			pos = s.indexOf("img", start);

			if (pos <= 0) {
				// weil bei start 0 getht sich keine <img aus
				return -1;
			}

			int rpos = StringUtils.skip_spaces_reverse(s, pos - 1);

			if (rpos < 0) {
				start = pos + 3;
				continue;
			}

			if (s.charAt(rpos) != '<') {
				start = pos + 3;
				continue;
			}

			if (s.length() <= pos + 3)
				return -1;

			if (StringUtils.is_space(s.charAt(pos + 3)))
				return pos;
		} while (pos < (s.length() - 1));

		return -1;
	}

	private String replace_src(String s) {

		List<String> res = StringUtils.split_str(s, "=");

		int i = 0;

		StringBuilder ret = new StringBuilder();

		for (String part : res) {
			i++;

			if (i == 1) {
				ret.append(part);
				continue;
			}

			ret.append("=");

			String src = StringUtils
					.strip(part, " \t\r\n\"");

			URL resource = getClass().getResource(src);

			String imgsrc = null;

			if (resource != null) {
				imgsrc = resource.toString();
				logger.info("image: " + src);
			} else {
				logger.error("Cannot load Image: " + src);
			}

			if (imgsrc != null) {
				ret.append("\"");
				ret.append(imgsrc);
				ret.append("\"");
			}
		}

		return ret.toString();
	}

	private String prepareImages(String s) {
		StringBuilder res = new StringBuilder(s);
		for (int start = findImgTag(s); start >= 0; ) {
			int end = s.indexOf('>', start);

			if (end < 0)
				break;

			String substitute = replace_src(res.substring(start, end));

			res.replace(start, end, substitute);

			start += substitute.length();
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
