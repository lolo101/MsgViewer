package net.sourceforge.MSGViewer.rtfparser;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RTFGroup {

    private final StringBuilder htmlContent;
    private final Charset characterSet;
    private final List<String> commands = new ArrayList<>();

    public RTFGroup(Charset characterSet, StringBuilder htmlContent, RTFGroup parent) {
        this.characterSet = characterSet;
        this.htmlContent = htmlContent;
        if (parent != null) this.commands.addAll(parent.commands);
    }

    public void handleCommand(String command) {
        this.commands.add(command);
    }

    public void destination(String destination) {
    }

    public String getTextContent() {
        return htmlContent.toString();
    }

    public void addEscapedChar(String escapedChar) {
        int hexa = Integer.parseInt(escapedChar.substring(2), 16);
        byte[] bytes = {(byte) hexa};
        addTextContent(new String(bytes, characterSet));
    }

    public void addUnicodeChar(String code) {
        String codepoint = code.substring(2);
        char ch = (char) Integer.parseInt(codepoint);
        addCharacterContent(ch);
    }

    public void addTextContent(String text) {
        if (shouldPrint())
            htmlContent.append(text);
    }

    private void addCharacterContent(char character) {
        if (shouldPrint())
            htmlContent.append(character);
    }

    private boolean shouldPrint() {
        return htmlRtfDisabled() && !plainTextBullet();
    }

    private boolean htmlRtfDisabled() {
        return commands.lastIndexOf("\\htmlrtf0") > commands.lastIndexOf("\\htmlrtf")
                && commands.lastIndexOf("\\htmlrtf0") > commands.lastIndexOf("\\htmlrtf1");
    }

    private boolean plainTextBullet() {
        return commands.contains("\\pntext");
    }
}
