package net.sourceforge.MSGViewer.factory.msg.properties;

public enum Properties {
    GUIDStream("0002"),
    EntryStream("0003"),
    PidTagAutoForwardComment("0004"),
    PidTagMessageClass("001a"),
    PidTagSubject("0037"),
    PidTagClientSubmitTime("0039"),
    PidTagSentRepresentingSearchKey("003b"),
    PidTagSubjectPrefix("003d"),
    PidTagReceivedByEntryId("003f"),
    PidTagReceivedByName("0040"),
    PidTagSentRepresentingEntryId("0041"),
    PidTagSentRepresentingName("0042"),
    PidTagReceivedRepresentingName("0044"),
    PidTagOriginalAuthorName("004d"),
    PidTagReplyRecipientNames("0050"),
    PidTagReceivedBySearchKey("0051"),
    PidTagOriginalSenderName("005a"),
    PidTagSentRepresentingAddressType("0064"),
    PidTagSentRepresentingEmailAddress("0065"),
    PidTagConversationTopic("0070"),
    PidTagConversationIndex("0071"),
    PidTagReceivedByAddressType("0075"),
    PidTagReceivedByEmailAddress("0076"),
    PidTagTransportMessageHeaders("007d"),
    PidTagReceivedRepresentingEmailAddress("0078"),

    PidTagSenderEntryId("0c19"),
    PidTagSenderName("0c1a"),
    PidTagSenderAddressType("0c1e"),
    PidTagRecipientType("0c15"),
    PidTagSenderSearchKey("0c1d"),
    PidTagSenderEmailAddress("0c1f"),

    PidTagDisplayBcc("0e02"),
    PidTagDisplayCc("0e03"),
    PidTagDisplayTo("0e04"),
    PidTagMessageDeliveryTime("0e06"),
    PidTagMessageFlags("0e07"),
    PidTagHasAttachments("0e1b"),
    PidTagNormalizedSubject("0e1d"),
    PidTagRtfInSync("0e1f"),
    PidTagPrimarySendAccount("0e28"),
    PidTagNextSendAcct("0e29"),

    PidTagAccess("0ff4"),
    PidTagInstanceKey("0ff6"),
    PidTagAccessLevel("0ff7"),
    PidTagRecordKey("0ff9"),
    PidTagEntryId("0fff"),

    PidTagBody("1000"),
    PidTagRtfCompressed("1009"),
    PidTagInternetMessageId("1035"),
    PidTagOriginalMessageId("1046"),

    PidTagRowid("3000"),
    PidTagDisplayName("3001"),
    PidTagAddressType("3002"),
    PidTagEmailAddress("3003"),
    PidTagCreationTime("3007"),
    PidTagLastModificationTime("3008"),
    PidTagSearchKey("300b"),

    PidTagStoreSupportMask("340d"),

    PidTagAttachDataBinary("3701"),
    PidTagAttachEncoding("3702"),
    PidTagAttachExtension("3703"),
    PidTagAttachFilename("3704"),
    PidTagAttachLongFilename("3707"),
    PidTagAttachRendering("3709"),
    PidTagAttachMimeTag("370e"),
    PidTagAttachContentId("3712"),
    PidTagAttachContentLocation("3713"),

    PidTagInternetCodepage("3fde"),
    PidTagLastModifierName("3ffa"),

    PidLidContactItemData("8007"),

    Unknown("????");

    private final String propId;

    Properties(String propId) {
        this.propId = propId;
    }

    public static Properties get(String id) {
        for (Properties value : Properties.values()) {
            if (value.propId.equals(id)) {
                return value;
            }
        }
        return Unknown;
    }
}
