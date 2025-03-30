package net.sourceforge.MSGViewer;

import net.sourceforge.MSGViewer.rtfparser.ParseException;
import net.sourceforge.MSGViewer.rtfparser.RTFParser;

import java.io.StringReader;

class HtmlFromRtf {
    private final String rtf;

    HtmlFromRtf(String rtf) {
        this.rtf = rtf;
    }

    /*
     *
     *  The Code looks like this:
     *
{\rtf1\ansi\ansicpg1252\fromhtml1 \deff0{\fonttbl
{\f0\fswiss Arial;}
{\f1\fmodern Courier New;}
{\f2\fnil\fcharset2 Symbol;}
{\f3\fmodern\fcharset0 Courier New;}
{\f4\fswiss\fcharset0 Arial;}}
{\colortbl\red0\green0\blue0;\red0\green0\blue255;}
{\*\htmltag19 <html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:w="urn:schemas-microsoft-com:office:word" xmlns="http://www.w3.org/TR/REC-html40">}
{\*\htmltag2 \par }
{\*\htmltag2 \par }
{\*\htmltag34 <head>}
{\*\htmltag1 \par }
{\*\htmltag1 \par }
{\*\htmltag161 <meta name=Generator content="Microsoft Word 11 (filtered medium)">}
{\*\htmltag1 \par }
{\*\htmltag241 <style>}
     *
     *  So we will try to remove all leading { and } and all \htmltag and hope for the best
     */

    byte[] extractHtml() throws ParseException {
        RTFParser parser = new RTFParser(new StringReader(rtf));

        parser.parse();

        return parser.getHTML();
    }
}
