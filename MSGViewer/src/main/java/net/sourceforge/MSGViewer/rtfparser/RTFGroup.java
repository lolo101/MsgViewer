package net.sourceforge.MSGViewer.rtfparser;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RTFGroup
{
    private final StringBuilder text_content = new StringBuilder();
    private final List<String> commands = new ArrayList<>();
    private String last_command = "";

    void addCommand( String command )
    {

        if (command.equals("\\par"))
            addTextContent("\n");
        else if (command.equals("\\tab"))
            addTextContent("\t");
        else {
            commands.add(command);
            last_command = command;
        }
    }

    public void addEscapedChar(String characterSet, String hexa) {
        addTextContent(ConvertCharset.convertCharacter(characterSet, hexa));
    }

    public void addUnicodeChar(String code) {
        String codepoint = code.substring(2);
        char ch = (char) Integer.parseInt(codepoint);
        addCharacterContent(ch);
    }

    public void addTextContent(String text) {
        if (last_command.startsWith("\\html"))
            text_content.append(text);
    }

    public void addCharacterContent(char character) {
        if (last_command.startsWith("\\html"))
            text_content.append(character);
    }

    public boolean isEmpty() {
        return text_content.length() == 0 && commands.isEmpty();
    }

    public String getTextContent() {
        return text_content.toString();
    }

    public List<String> getCommands() {
        return commands;
    }

    boolean isNotEmptyText() {

       return !StringUtils.isEmpty(text_content);
    }

}
