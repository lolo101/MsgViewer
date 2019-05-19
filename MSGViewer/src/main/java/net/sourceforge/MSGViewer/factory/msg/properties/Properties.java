package net.sourceforge.MSGViewer.factory.msg.properties;

public enum Properties {
    // 0x0001-0x0BFF Message object envelope property; reserved
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
    PidTagReceivedRepresentingEntryId("0043"),
    PidTagReceivedRepresentingName("0044"),
    PidTagOriginalAuthorName("004d"),
    PidTagReplyRecipientNames("0050"),
    PidTagReceivedBySearchKey("0051"),
    PidTagReceivedRepresentingSearchKey("0052"),
    PidTagOriginalSenderName("005a"),
    PidTagSentRepresentingAddressType("0064"),
    PidTagSentRepresentingEmailAddress("0065"),
    PidTagConversationTopic("0070"),
    PidTagConversationIndex("0071"),
    PidTagReceivedByAddressType("0075"),
    PidTagReceivedByEmailAddress("0076"),
    PidTagReceivedRepresentingAddressType("0077"),
    PidTagReceivedRepresentingEmailAddress("0078"),
    PidTagTransportMessageHeaders("007d"),
    PidTagTnefCorrelationKey("007f"),

    // 0x0C00-0x0DFF Recipient property; reserved
    PidTagSenderEntryId("0c19"),
    PidTagSenderName("0c1a"),
    PidTagSenderAddressType("0c1e"),
    PidTagRecipientType("0c15"),
    PidTagSenderSearchKey("0c1d"),
    PidTagSenderEmailAddress("0c1f"),

    // 0x0E00-0x0FFF Non-transmittable Message property; reserved
    PidTagDisplayBcc("0e02"),
    PidTagDisplayCc("0e03"),
    PidTagDisplayTo("0e04"),
    PidTagMessageDeliveryTime("0e06"),
    PidTagMessageFlags("0e07"),
    PidTagResponsibility("0e0f"),
    PidTagHasAttachments("0e1b"),
    PidTagNormalizedSubject("0e1d"),
    PidTagRtfInSync("0e1f"),
    PidTagPrimarySendAccount("0e28"),
    PidTagNextSendAcct("0e29"),
    PidTagAccess("0ff4"),
    PidTagInstanceKey("0ff6"),
    PidTagAccessLevel("0ff7"),
    PidTagRecordKey("0ff9"),
    PidTagObjectType("0ffe"),
    PidTagEntryId("0fff"),

    // 0x1000-0x2FFF Message content property; reserved
    PidTagBody("1000"),
    PidTagRtfCompressed("1009"),
    PidTagInternetMessageId("1035"),
    PidTagOriginalMessageId("1046"),

    // 0x3000-0x33FF Multi-purpose property that can appear on all or most objects; reserved
    PidTagRowid("3000"),
    PidTagDisplayName("3001"),
    PidTagAddressType("3002"),
    PidTagEmailAddress("3003"),
    PidTagCreationTime("3007"),
    PidTagLastModificationTime("3008"),
    PidTagSearchKey("300b"),

    // 0x3400-0x35FF Message store property; reserved
    PidTagStoreSupportMask("340d"),

    // 0x3600-0x36FF Folder and address book container property; reserved

    // 0x3700-0x38FF Attachment property; reserved
    PidTagAttachDataBinary("3701"),
    PidTagAttachEncoding("3702"),
    PidTagAttachExtension("3703"),
    PidTagAttachFilename("3704"),
    PidTagAttachMethod("3705"),
    PidTagAttachLongFilename("3707"),
    PidTagAttachRendering("3709"),
    PidTagRenderingPosition("370b"),
    PidTagAttachMimeTag("370e"),
    PidTagAttachContentId("3712"),
    PidTagAttachContentLocation("3713"),
    PidTagAttachFlags("3714"),

    // 0x3900-0x39FF Address Book object property; reserved
    PidTagSmtpAddress("39fe"),
    PidTagAddressBookDisplayNamePrintable("39ff"),

    // 0x3A00 0x3BFF Mail user object property; reserved
    PidTagLanguage("3a0c"),
    PidTagTransmittableDisplayName("3a20"),
    PidTagSendRichInfo("3a40"),

    // 0x3C00 0x3CFF Distribution list property; reserved

    // 0x3D00 0x3DFF Profile section property; reserved

    // 0x3E00 0x3EFF Status object property; reserved

    PidTagInternetCodepage("3fde"),
    PidTagCreatorName("3ff8"),
    PidTagLastModifierName("3ffa"),

    // 0x4000 0x57FF Transport-defined envelope property

    // 0x5800 0x5FFF Transport-defined recipient property
    PidTagSentRepresentingSmtpAddress("5d02"),
    PidTagReceivedBySmtpAddress("5d07"),
    PidTagReceivedRepresentingSmtpAddress("5d08"),
    PidTagRecipientEntryId("5ff7"),

    // 0x6000 0x65FF User-defined non-transmittable property

    // 0x6600 0x67FF Provider-defined internal non-transmittable property

    // 0x6800 0x7BFF Message class-defined content property

    // 0x7C00 0x7FFF Message class-defined non-transmittable property
    PidTagAttachmentLinkId("7ffa"),
    PidTagExceptionStartTime("7ffb"),
    PidTagExceptionEndTime("7ffc"),
    PidTagAttachmentFlags("7ffd"),
    PidTagAttachmentHidden("7ffe"),
    PidTagAttachmentContactPhoto("7fff"),

    // 0x8000 0xFFFF Reserved for mapping to named properties. The exceptions to this rule are some of the address book tagged properties (those with names beginning with PidTagAddressBook). Many are static property ids but are in this range.

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
