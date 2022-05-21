package com.auxilii.msgparser;

import java.util.Arrays;

import static com.auxilii.msgparser.Ptyp.*;

public enum Pid {
    PidTagGuidStream(0x0002, PtypBinary),
    PidTagEntryStream(0x0003, PtypBinary),
    PidTagStringStream(0x0004, PtypBinary),

    // 0x0001-0x0BFF Message object envelope property; reserved
    PidTagMessageClass(0x001a, PtypString),
    PidTagSensitivity(0x0036, PtypInteger32),
    PidTagSubject(0x0037, PtypString),
    PidTagClientSubmitTime(0x0039, PtypTime),
    PidTagSentRepresentingSearchKey(0x003b, PtypBinary),
    PidTagSubjectPrefix(0x003d, PtypString),
    PidTagReceivedByEntryId(0x003f, PtypBinary),
    PidTagReceivedByName(0x0040, PtypString),
    PidTagSentRepresentingEntryId(0x0041, PtypBinary),
    PidTagSentRepresentingName(0x0042, PtypString),
    PidTagReceivedRepresentingEntryId(0x0043, PtypBinary),
    PidTagReceivedRepresentingName(0x0044, PtypString),
    PidTagOriginalAuthorName(0x004d, PtypString),
    PidTagReplyRecipientEntries(0x004f, PtypBinary),
    PidTagReplyRecipientNames(0x0050, PtypString),
    PidTagReceivedBySearchKey(0x0051, PtypBinary),
    PidTagReceivedRepresentingSearchKey(0x0052, PtypBinary),
    PidTagOriginalSenderName(0x005a, PtypString),
    PidTagSentRepresentingAddressType(0x0064, PtypString),
    PidTagSentRepresentingEmailAddress(0x0065, PtypString),
    PidTagConversationTopic(0x0070, PtypString),
    PidTagConversationIndex(0x0071, PtypBinary),
    PidTagReceivedByAddressType(0x0075, PtypString),
    PidTagReceivedByEmailAddress(0x0076, PtypString),
    PidTagReceivedRepresentingAddressType(0x0077, PtypString),
    PidTagReceivedRepresentingEmailAddress(0x0078, PtypString),
    PidTagTransportMessageHeaders(0x007d, PtypString),
    PidTagTnefCorrelationKey(0x007f, PtypBinary),

    // 0x0C00-0x0DFF Recipient property; reserved
    PidTagSenderEntryId(0x0c19, PtypBinary),
    PidTagSenderName(0x0c1a, PtypString),
    PidTagSenderAddressType(0x0c1e, PtypString),
    PidTagRecipientType(0x0c15, PtypInteger32),
    PidTagSenderSearchKey(0x0c1d, PtypBinary),
    PidTagSenderEmailAddress(0x0c1f, PtypString),

    // 0x0E00-0x0FFF Non-transmittable Message property; reserved
    PidTagDisplayBcc(0x0e02, PtypString),
    PidTagDisplayCc(0x0e03, PtypString),
    PidTagDisplayTo(0x0e04, PtypString),
    PidTagMessageDeliveryTime(0x0e06, PtypTime),
    PidTagMessageFlags(0x0e07, PtypInteger32),
    PidTagResponsibility(0x0e0f, PtypBoolean),
    PidTagHasAttachments(0x0e1b, PtypBoolean),
    PidTagNormalizedSubject(0x0e1d, PtypString),
    PidTagRtfInSync(0x0e1f, PtypBoolean),
    PidTagPrimarySendAccount(0x0e28, PtypString),
    PidTagNextSendAcct(0x0e29, PtypString),
    PidTagAccess(0x0ff4, PtypInteger32),
    PidTagInstanceKey(0x0ff6, PtypBinary),
    PidTagAccessLevel(0x0ff7, PtypInteger32),
    PidTagRecordKey(0x0ff9, PtypBinary),
    PidTagObjectType(0x0ffe, PtypInteger32),
    PidTagEntryId(0x0fff, PtypBinary),

    // 0x1000-0x2FFF Message content property; reserved
    PidTagBody(0x1000, PtypString),
    PidTagRtfCompressed(0x1009, PtypBinary),
    PidTagBodyHtml(0x1013, PtypString),
    PidTagHtml(0x1013, PtypBinary),
    PidTagInternetMessageId(0x1035, PtypString),
    PidTagOriginalMessageId(0x1046, PtypString),

    // 0x3000-0x33FF Multi-purpose property that can appear on all or most objects; reserved
    PidTagRowid(0x3000, PtypInteger32),
    PidTagDisplayName(0x3001, PtypString),
    PidTagAddressType(0x3002, PtypString),
    PidTagEmailAddress(0x3003, PtypString),
    PidTagCreationTime(0x3007, PtypTime),
    PidTagLastModificationTime(0x3008, PtypTime),
    PidTagSearchKey(0x300b, PtypBinary),

    // 0x3400-0x35FF Message store property; reserved
    PidTagStoreSupportMask(0x340d, PtypInteger32),

    // 0x3600-0x36FF Folder and address book container property; reserved

    // 0x3700-0x38FF Attachment property; reserved
    PidTagAttachDataBinary(0x3701, PtypBinary),
    PidTagAttachEncoding(0x3702, PtypBinary),
    PidTagAttachExtension(0x3703, PtypString),
    PidTagAttachFilename(0x3704, PtypString),
    PidTagAttachMethod(0x3705, PtypInteger32),
    PidTagAttachLongFilename(0x3707, PtypString),
    PidTagAttachRendering(0x3709, PtypBinary),
    PidTagAttachTag(0x370a, PtypBinary),
    PidTagRenderingPosition(0x370b, PtypInteger32),
    PidTagAttachMimeTag(0x370e, PtypString),
    PidTagAttachContentId(0x3712, PtypString),
    PidTagAttachContentLocation(0x3713, PtypString),
    PidTagAttachFlags(0x3714, PtypInteger32),

    // 0x3900-0x39FF Address Book object property; reserved
    PidTagSmtpAddress(0x39fe, PtypString),
    PidTagAddressBookDisplayNamePrintable(0x39ff, PtypString),

    // 0x3A00 0x3BFF Mail user object property; reserved
    PidTagLanguage(0x3a0c, PtypString),
    PidTagTransmittableDisplayName(0x3a20, PtypString),
    PidTagSendRichInfo(0x3a40, PtypBoolean),

    // 0x3C00 0x3CFF Distribution list property; reserved

    // 0x3D00 0x3DFF Profile section property; reserved

    // 0x3E00 0x3EFF Status object property; reserved

    PidTagInternetCodepage(0x3fde, PtypInteger32),
    PidTagCreatorName(0x3ff8, PtypString),
    PidTagLastModifierName(0x3ffa, PtypString),

    // 0x4000 0x57FF Transport-defined envelope property

    // 0x5800 0x5FFF Transport-defined recipient property
    PidTagSentRepresentingSmtpAddress(0x5d02, PtypString),
    PidTagReceivedBySmtpAddress(0x5d07, PtypString),
    PidTagReceivedRepresentingSmtpAddress(0x5d08, PtypString),
    PidTagRecipientEntryId(0x5ff7, PtypBinary),

    // 0x6000 0x65FF User-defined non-transmittable property

    // 0x6600 0x67FF Provider-defined internal non-transmittable property

    // 0x6800 0x7BFF Message class-defined content property

    // 0x7C00 0x7FFF Message class-defined non-transmittable property
    PidTagAttachmentLinkId(0x7ffa, PtypInteger32),
    PidTagExceptionStartTime(0x7ffb, PtypTime),
    PidTagExceptionEndTime(0x7ffc, PtypTime),
    PidTagAttachmentFlags(0x7ffd, PtypInteger32),
    PidTagAttachmentHidden(0x7ffe, PtypBoolean),
    PidTagAttachmentContactPhoto(0x7fff, PtypBoolean),

    // 0x8000 0xFFFF Reserved for mapping to named properties. The exceptions to this rule are some of the address book tagged properties (those with names beginning with PidTagAddressBook). Many are static property ids but are in this range.

    Unknown(0, null);

    public final int id;
    public final Ptyp typ;

    Pid(int id, Ptyp typ) {
        this.id = id;
        this.typ = typ;
    }

    public static Pid from(int id, Ptyp typ) {
        return Arrays.stream(Pid.values())
                .filter(value -> value.id == id)
                .filter(value -> value.typ == typ)
                .findFirst()
                .orElse(Unknown);
    }
}
