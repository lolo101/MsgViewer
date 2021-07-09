package com.auxilii.msgparser;

import java.util.Arrays;

public enum Pid {
    PidTagGuidStream(0x0002),
    PidTagEntryStream(0x0003),
    PidTagStringStream(0x0004),

    // 0x0001-0x0BFF Message object envelope property; reserved
    PidTagMessageClass(0x001a),
    PidTagSubject(0x0037),
    PidTagClientSubmitTime(0x0039),
    PidTagSentRepresentingSearchKey(0x003b),
    PidTagSubjectPrefix(0x003d),
    PidTagReceivedByEntryId(0x003f),
    PidTagReceivedByName(0x0040),
    PidTagSentRepresentingEntryId(0x0041),
    PidTagSentRepresentingName(0x0042),
    PidTagReceivedRepresentingEntryId(0x0043),
    PidTagReceivedRepresentingName(0x0044),
    PidTagOriginalAuthorName(0x004d),
    PidTagReplyRecipientNames(0x0050),
    PidTagReceivedBySearchKey(0x0051),
    PidTagReceivedRepresentingSearchKey(0x0052),
    PidTagOriginalSenderName(0x005a),
    PidTagSentRepresentingAddressType(0x0064),
    PidTagSentRepresentingEmailAddress(0x0065),
    PidTagConversationTopic(0x0070),
    PidTagConversationIndex(0x0071),
    PidTagReceivedByAddressType(0x0075),
    PidTagReceivedByEmailAddress(0x0076),
    PidTagReceivedRepresentingAddressType(0x0077),
    PidTagReceivedRepresentingEmailAddress(0x0078),
    PidTagTransportMessageHeaders(0x007d),
    PidTagTnefCorrelationKey(0x007f),

    // 0x0C00-0x0DFF Recipient property; reserved
    PidTagSenderEntryId(0x0c19),
    PidTagSenderName(0x0c1a),
    PidTagSenderAddressType(0x0c1e),
    PidTagRecipientType(0x0c15),
    PidTagSenderSearchKey(0x0c1d),
    PidTagSenderEmailAddress(0x0c1f),

    // 0x0E00-0x0FFF Non-transmittable Message property; reserved
    PidTagDisplayBcc(0x0e02),
    PidTagDisplayCc(0x0e03),
    PidTagDisplayTo(0x0e04),
    PidTagMessageDeliveryTime(0x0e06),
    PidTagMessageFlags(0x0e07),
    PidTagResponsibility(0x0e0f),
    PidTagHasAttachments(0x0e1b),
    PidTagNormalizedSubject(0x0e1d),
    PidTagRtfInSync(0x0e1f),
    PidTagPrimarySendAccount(0x0e28),
    PidTagNextSendAcct(0x0e29),
    PidTagAccess(0x0ff4),
    PidTagInstanceKey(0x0ff6),
    PidTagAccessLevel(0x0ff7),
    PidTagRecordKey(0x0ff9),
    PidTagObjectType(0x0ffe),
    PidTagEntryId(0x0fff),

    // 0x1000-0x2FFF Message content property; reserved
    PidTagBody(0x1000),
    PidTagRtfCompressed(0x1009),
    PidTagInternetMessageId(0x1035),
    PidTagOriginalMessageId(0x1046),

    // 0x3000-0x33FF Multi-purpose property that can appear on all or most objects; reserved
    PidTagRowid(0x3000),
    PidTagDisplayName(0x3001),
    PidTagAddressType(0x3002),
    PidTagEmailAddress(0x3003),
    PidTagCreationTime(0x3007),
    PidTagLastModificationTime(0x3008),
    PidTagSearchKey(0x300b),

    // 0x3400-0x35FF Message store property; reserved
    PidTagStoreSupportMask(0x340d),

    // 0x3600-0x36FF Folder and address book container property; reserved

    // 0x3700-0x38FF Attachment property; reserved
    PidTagAttachDataBinary(0x3701),
    PidTagAttachEncoding(0x3702),
    PidTagAttachExtension(0x3703),
    PidTagAttachFilename(0x3704),
    PidTagAttachMethod(0x3705),
    PidTagAttachLongFilename(0x3707),
    PidTagAttachRendering(0x3709),
    PidTagAttachTag(0x370a),
    PidTagRenderingPosition(0x370b),
    PidTagAttachMimeTag(0x370e),
    PidTagAttachContentId(0x3712),
    PidTagAttachContentLocation(0x3713),
    PidTagAttachFlags(0x3714),

    // 0x3900-0x39FF Address Book object property; reserved
    PidTagSmtpAddress(0x39fe),
    PidTagAddressBookDisplayNamePrintable(0x39ff),

    // 0x3A00 0x3BFF Mail user object property; reserved
    PidTagLanguage(0x3a0c),
    PidTagTransmittableDisplayName(0x3a20),
    PidTagSendRichInfo(0x3a40),

    // 0x3C00 0x3CFF Distribution list property; reserved

    // 0x3D00 0x3DFF Profile section property; reserved

    // 0x3E00 0x3EFF Status object property; reserved

    PidTagInternetCodepage(0x3fde),
    PidTagCreatorName(0x3ff8),
    PidTagLastModifierName(0x3ffa),

    // 0x4000 0x57FF Transport-defined envelope property

    // 0x5800 0x5FFF Transport-defined recipient property
    PidTagSentRepresentingSmtpAddress(0x5d02),
    PidTagReceivedBySmtpAddress(0x5d07),
    PidTagReceivedRepresentingSmtpAddress(0x5d08),
    PidTagRecipientEntryId(0x5ff7),

    // 0x6000 0x65FF User-defined non-transmittable property

    // 0x6600 0x67FF Provider-defined internal non-transmittable property

    // 0x6800 0x7BFF Message class-defined content property

    // 0x7C00 0x7FFF Message class-defined non-transmittable property
    PidTagAttachmentLinkId(0x7ffa),
    PidTagExceptionStartTime(0x7ffb),
    PidTagExceptionEndTime(0x7ffc),
    PidTagAttachmentFlags(0x7ffd),
    PidTagAttachmentHidden(0x7ffe),
    PidTagAttachmentContactPhoto(0x7fff),

    // 0x8000 0xFFFF Reserved for mapping to named properties. The exceptions to this rule are some of the address book tagged properties (those with names beginning with PidTagAddressBook). Many are static property ids but are in this range.

    Unknown(0);

    public final int id;

    Pid(int id) {
        this.id = id;
    }

    public static Pid from(int id) {
        return Arrays.stream(Pid.values())
                .filter(value -> value.id == id)
                .findFirst()
                .orElse(Unknown);
    }
}
