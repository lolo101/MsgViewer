package net.sourceforge.MSGViewer.factory.msg.properties;

public enum Properties {
    GUIDStream("0002", "001f"),
    EntryStream("0003", "001f"),
    PidTagAutoForwardComment("0004", "001f"),
    PidTagMessageClass("001a", "001f"),
    PidTagSubject("0037", "001f"),
    PidTagClientSubmitTime("0039", "0040"),
    PidTagSentRepresentingSearchKey("003b", "001f"),
    PidTagSubjectPrefix("003d", "001f"),
    PidTagReceivedByEntryId("003f", "001f"),
    PidTagSentRepresentingEntryId("0041", "001f"),
    PidTagSentRepresentingName("0042", "001f"),
    PidTagReceivedRepresentingName("0044", "001f"),
    PidTagOriginalAuthorName("004d", "001f"),
    PidTagReplyRecipientNames("0050", "001f"),
    PidTagReceivedBySearchKey("0051", "001f"),
    PidTagOriginalSenderName("005a", "001f"),
    PidTagSentRepresentingAddressType("0064", "001f"),
    PidTagSentRepresentingEmailAddress("0065", "001f"),
    PidTagConversationTopic("0070", "001f"),
    PidTagConversationIndex("0071", "001f"),
    PidTagReceivedByAddressType("0075", "001f"),
    PidTagReceivedByEmailAddress("0076", "001f"),
    PidTagTransportMessageHeaders("007d", "001f"),
    PidTagReceivedRepresentingEmailAddress("0078", "001f"),

    PidTagSenderEntryId("0c19", "001f"),
    PidTagSenderName("0c1a", "001f"),
    PidTagSenderAddressType("0c1e", "001f"),
    PidTagRecipientType("0c15", "001f"),
    PidTagSenderSearchKey("0c1d", "001f"),
    PidTagSenderEmailAddress("0c1f", "001f"),

    PidTagDisplayBcc("0e02", "001f"),
    PidTagDisplayCc("0e03", "001f"),
    PidTagDisplayTo("0e04", "001f"),
    PidTagMessageDeliveryTime("0e06", "0040"),
    PidTagMessageFlags("0e07", "001f"),
    PidTagHasAttachments("0e1b", "001f"),
    PidTagNormalizedSubject("0e1d", "001f"),
    PidTagRtfInSync("0e1f", "001f"),
    PidTagPrimarySendAccount("0e28", "001f"),
    PidTagNextSendAcct("0e29", "001f"),

    PidTagAccess("0ff4", "001f"),
    PidTagInstanceKey("0ff6", "001f"),
    PidTagAccessLevel("0ff7", "001f"),
    PidTagEntryId("0fff", "001f"),

    PidTagBody("1000", "001f"),
    rtfSync("1008", "001f"),
    PidTagRtfCompressed("1009", "001f"),
    PidTagInternetMessageId("1035", "001f"),
    PidTagOriginalMessageId("1046", "001f"),

    PidTagRowid("3000", "001f"),
    PidTagDisplayName("3001", "001f"),
    PidTagAddressType("3002", "001f"),
    PidTagEmailAddress("3003", "001f"),
    PidTagCreationTime("3007", "0040"),
    PidTagLastModificationTime("3008", "0040"),
    PidTagSearchKey("300b", "001f"),

    PidTagStoreSupportMask("340d", "001f"),

    PidTagAttachDataBinary("3701", "001f"),
    PidTagAttachExtension("3703", "001f"),
    PidTagAttachFilename("3704", "001f"),
    PidTagAttachLongFilename("3707", "001f"),
    PidTagAttachMimeTag("370e", "001f"),
    PidTagAttachContentId("3712", "001f"),

    PidTagInternetCodepage("3fde", "001f"),

    PidLidContactItemData("8007", "001f");

    private final String propId;
    private final String propType;

    Properties(String propId, String propType) {
        this.propId = propId;
        this.propType = propType;
    }

    public static Properties get(String id) {
        for (Properties value : Properties.values()) {
            if (value.propId.equals(id)) {
                return value;
            }
        }
        throw new IllegalArgumentException(id);
    }
}
