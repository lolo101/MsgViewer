package com.auxilii.msgparser;

import java.util.Arrays;
import java.util.Set;

import static com.auxilii.msgparser.Ptyp.*;

/**
 * [MS-OXPROPS]
 */
public enum Pid {
    PidTagGuidStream(0x0002, PtypBinary),
    PidTagEntryStream(0x0003, PtypBinary),
    PidTagStringStream(0x0004, PtypBinary),

    // 0x0001-0x0BFF Message object envelope property; reserved
    PidTagMessageClass(0x001a, PtypString, PtypString8),
    PidTagSensitivity(0x0036, PtypInteger32),
    PidTagSubject(0x0037, PtypString, PtypString8),
    PidTagClientSubmitTime(0x0039, PtypTime),
    PidTagSentRepresentingSearchKey(0x003b, PtypBinary),
    PidTagSubjectPrefix(0x003d, PtypString, PtypString8),
    PidTagReceivedByEntryId(0x003f, PtypBinary),
    PidTagReceivedByName(0x0040, PtypString, PtypString8),
    PidTagSentRepresentingEntryId(0x0041, PtypBinary),
    PidTagSentRepresentingName(0x0042, PtypString, PtypString8),
    PidTagReceivedRepresentingEntryId(0x0043, PtypBinary),
    PidTagReceivedRepresentingName(0x0044, PtypString, PtypString8),
    PidTagOriginalAuthorName(0x004d, PtypString, PtypString8),
    PidTagReplyRecipientEntries(0x004f, PtypBinary),
    PidTagReplyRecipientNames(0x0050, PtypString, PtypString8),
    PidTagReceivedBySearchKey(0x0051, PtypBinary),
    PidTagReceivedRepresentingSearchKey(0x0052, PtypBinary),
    PidTagOriginalSenderName(0x005a, PtypString, PtypString8),
    PidTagSentRepresentingAddressType(0x0064, PtypString, PtypString8),
    PidTagSentRepresentingEmailAddress(0x0065, PtypString, PtypString8),
    PidTagConversationTopic(0x0070, PtypString, PtypString8),
    PidTagConversationIndex(0x0071, PtypBinary),
    PidTagReceivedByAddressType(0x0075, PtypString, PtypString8),
    PidTagReceivedByEmailAddress(0x0076, PtypString, PtypString8),
    PidTagReceivedRepresentingAddressType(0x0077, PtypString, PtypString8),
    PidTagReceivedRepresentingEmailAddress(0x0078, PtypString, PtypString8),
    PidTagTransportMessageHeaders(0x007d, PtypString, PtypString8),
    PidTagTnefCorrelationKey(0x007f, PtypBinary),

    // 0x0C00-0x0DFF Recipient property; reserved
    PidTagSenderEntryId(0x0c19, PtypBinary),
    PidTagSenderName(0x0c1a, PtypString, PtypString8),
    PidTagSenderAddressType(0x0c1e, PtypString, PtypString8),
    PidTagRecipientType(0x0c15, PtypInteger32),
    PidTagSenderSearchKey(0x0c1d, PtypBinary),
    PidTagSenderEmailAddress(0x0c1f, PtypString, PtypString8),

    // 0x0E00-0x0FFF Non-transmittable Message property; reserved
    PidTagDisplayBcc(0x0e02, PtypString, PtypString8),
    PidTagDisplayCc(0x0e03, PtypString, PtypString8),
    PidTagDisplayTo(0x0e04, PtypString, PtypString8),
    PidTagMessageDeliveryTime(0x0e06, PtypTime),
    PidTagMessageFlags(0x0e07, PtypInteger32),
    PidTagResponsibility(0x0e0f, PtypBoolean),
    PidTagHasAttachments(0x0e1b, PtypBoolean),
    PidTagNormalizedSubject(0x0e1d, PtypString, PtypString8),
    PidTagRtfInSync(0x0e1f, PtypBoolean),
    PidTagPrimarySendAccount(0x0e28, PtypString, PtypString8),
    PidTagNextSendAcct(0x0e29, PtypString, PtypString8),
    PidTagAccess(0x0ff4, PtypInteger32),
    PidTagInstanceKey(0x0ff6, PtypBinary),
    PidTagAccessLevel(0x0ff7, PtypInteger32),
    PidTagRecordKey(0x0ff9, PtypBinary),
    PidTagObjectType(0x0ffe, PtypInteger32),
    PidTagEntryId(0x0fff, PtypBinary),

    // 0x1000-0x2FFF Message content property; reserved
    PidTagBody(0x1000, PtypString, PtypString8),
    PidTagRtfCompressed(0x1009, PtypBinary),
    PidTagBodyHtml(0x1013, PtypString, PtypString8),
    PidTagHtml(0x1013, PtypBinary),
    PidTagInternetMessageId(0x1035, PtypString, PtypString8),
    PidTagOriginalMessageId(0x1046, PtypString, PtypString8),

    // 0x3000-0x33FF Multi-purpose property that can appear on all or most objects; reserved
    PidTagRowid(0x3000, PtypInteger32),
    PidTagDisplayName(0x3001, PtypString, PtypString8),
    PidTagAddressType(0x3002, PtypString, PtypString8),
    PidTagEmailAddress(0x3003, PtypString, PtypString8),
    PidTagCreationTime(0x3007, PtypTime),
    PidTagLastModificationTime(0x3008, PtypTime),
    PidTagSearchKey(0x300b, PtypBinary),
    PidTagTargetEntryId(0x3010, PtypBinary),
    PidTagConversationId(0x3013, PtypBinary),

    // 0x3400-0x35FF Message store property; reserved
    PidTagStoreSupportMask(0x340d, PtypInteger32),

    // 0x3600-0x36FF Folder and address book container property; reserved

    // 0x3700-0x38FF Attachment property; reserved
    PidTagAttachDataBinary(0x3701, PtypBinary),
    PidTagAttachEncoding(0x3702, PtypBinary),
    PidTagAttachExtension(0x3703, PtypString, PtypString8),
    PidTagAttachFilename(0x3704, PtypString, PtypString8),
    PidTagAttachMethod(0x3705, PtypInteger32),
    PidTagAttachLongFilename(0x3707, PtypString, PtypString8),
    PidTagAttachRendering(0x3709, PtypBinary),
    PidTagAttachTag(0x370a, PtypBinary),
    PidTagRenderingPosition(0x370b, PtypInteger32),
    PidTagAttachMimeTag(0x370e, PtypString, PtypString8),
    PidTagAttachContentId(0x3712, PtypString, PtypString8),
    PidTagAttachContentLocation(0x3713, PtypString, PtypString8),
    PidTagAttachFlags(0x3714, PtypInteger32),

    // 0x3900-0x39FF Address Book object property; reserved
    PidTagDisplayType(0x3900, PtypInteger32),
    PidTagDisplayTypeEx(0x3905, PtypInteger32),
    PidTagSmtpAddress(0x39fe, PtypString, PtypString8),
    PidTagAddressBookDisplayNamePrintable(0x39ff, PtypString, PtypString8),

    // 0x3A00 0x3BFF Mail user object property; reserved
    PidTagAccount(0x3a00, PtypString, PtypString8),
    PidTagLanguage(0x3a0c, PtypString, PtypString8),
    PidTagTransmittableDisplayName(0x3a20, PtypString, PtypString8),
    PidTagSendRichInfo(0x3a40, PtypBoolean),

    // 0x3C00 0x3CFF Distribution list property; reserved

    // 0x3D00 0x3DFF Profile section property; reserved

    // 0x3E00 0x3EFF Status object property; reserved

    PidTagInternetCodepage(0x3fde, PtypInteger32),
    PidTagCreatorName(0x3ff8, PtypString, PtypString8),
    PidTagCreatorEntryId(0x3ff9, PtypBinary),
    PidTagLastModifierName(0x3ffa, PtypString, PtypString8),
    PidTagLastModifierEntryId(0x3ffb, PtypBinary),

    // 0x4000 0x57FF Transport-defined envelope property

    // 0x5800 0x5FFF Transport-defined recipient property
    PidTagSenderSmtpAddress(0x5d01, PtypString, PtypString8),
    PidTagSentRepresentingSmtpAddress(0x5d02, PtypString, PtypString8),
    PidTagReceivedBySmtpAddress(0x5d07, PtypString, PtypString8),
    PidTagReceivedRepresentingSmtpAddress(0x5d08, PtypString, PtypString8),
    PidTagRecipientOrder(0x5fdf, PtypInteger32),
    PidTagRecipientDisplayName(0x5ff6, PtypString, PtypString8),
    PidTagRecipientEntryId(0x5ff7, PtypBinary),
    PidTagRecipientFlags(0x5ffd, PtypInteger32),
    PidTagRecipientTrackStatus(0x5fff, PtypInteger32),

    // 0x6000 0x65FF User-defined non-transmittable property
    PidTagSourceKey(0x65e0, PtypBinary),
    PidTagParentSourceKey(0x65e1, PtypBinary),
    PidTagChangeKey(0x65e2, PtypBinary),
    PidTagPredecessorChangeList(0x65e3, PtypBinary),

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

    Unknown(0);

    public final int id;
    public final Set<Ptyp> typ;

    Pid(int id, Ptyp... typ) {
        this.id = id;
        this.typ = Set.of(typ);
    }

    public static Pid from(int id, Ptyp typ) {
        return Arrays.stream(Pid.values())
                .filter(value -> value.id == id)
                .filter(value -> value.typ.contains(typ))
                .findFirst()
                .orElse(Unknown);
    }
}
