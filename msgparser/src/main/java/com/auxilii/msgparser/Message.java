/*
 * msgparser - http://auxilii.com/msgparser
 * Copyright (C) 2007  Roman Kurmanowytsch
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package com.auxilii.msgparser;

import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.auxilii.msgparser.attachment.MsgAttachment;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hmef.CompressedRTF;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.poi.util.StringUtil.getFromUnicodeLE;

/**
 * Class that represents a .msg file. Some fields from the .msg file are stored
 * in special parameters (e.g., {@link #fromEmail}). Attachments are stored in
 * the property {@link #attachments}). An attachment may be of the type
 * {@link MsgAttachment} which represents another attached (encapsulated) .msg
 * object.
 *
 * @author roman.kurmanowytsch
 */
public class Message {
    private static final Logger LOGGER = LogManager.getLogger(Message.class);
    private static final Pattern PREFIX_PATTERN = Pattern.compile("^[^:\\s\\d]{1,3}:\\s+");
    private static final String DATE_HEADER_KEY = "date:";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder().appendPattern("[EEE, ]d MMM y HH:mm[:ss] Z").toFormatter(Locale.US);

    /**
     * The message class as defined in the .msg file.
     */
    private String messageClass = "IPM.Note";
    /**
     * The message Id.
     */
    private String messageId;
    /**
     * The address part of From: mail address.
     */
    private String fromEmail;

    private String fromSMTPAddress;

    /**
     * The name part of the From: mail address
     */
    private String fromName;
    private String fromAddressType;

    private String toEmail;
    private String toName;
    private String subject;
    private String subjectPrefix;
    private String topic;
    /**
     * The normalized body text.
     */
    private String bodyText;
    /**
     * The displayed To: field
     */
    private String displayTo;
    /**
     * The displayed Cc: field
     */
    private String displayCc;
    /**
     * The displayed Bcc: field
     */
    private String displayBcc;

    private byte[] compressedRTF;

    private String bodyRTF;

    private byte[] bodyHtml;

    /**
     * Email headers (if available)
     */
    private String headers;

    /**
     * Email Date
     */
    private ZonedDateTime date;

    /**
     * A list of all attachments (both {@link FileAttachment} and
     * {@link MsgAttachment}).
     */
    private final List<Attachment> attachments = new ArrayList<>();
    /**
     * Contains all properties that are not covered by the special properties.
     */
    private final Collection<Property> properties = new ArrayList<>();
    /**
     * A list containing all recipients for this message (which can be set in the
     * 'to:', 'cc:' and 'bcc:' field, respectively).
     */
    private final List<RecipientEntry> recipients = new ArrayList<>();

    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }

    public void addRecipient(RecipientEntry recipient) {
        this.recipients.add(recipient);
    }

    void setProperty(Property property) {
        this.properties.add(property);
        switch (property.getPid()) {
            case PidTagMessageClass:
                this.setMessageClass((String) property.getValue());
                break;
            case PidTagInternetMessageId:
                this.setMessageId((String) property.getValue());
                break;
            case PidTagSubject:
                this.setSubject((String) property.getValue());
                break;
            case PidTagSenderEmailAddress:
            case PidTagSentRepresentingEmailAddress:
                String email = (String) property.getValue();
                EmailValidator emailValidator = EmailValidator.getInstance();
                if (emailValidator.isValid(email)) {
                    this.setFromSMTPAddress(email);
                }
                this.setFromEmail(email);
                break;
            case PidTagSenderName:
            case PidTagSentRepresentingName:
                this.setFromName((String) property.getValue());
                break;
            case PidTagSenderAddressType:
                this.setFromAddressType((String) property.getValue());
                break;
            case PidTagReceivedByEmailAddress:
                this.addToEmail((String) property.getValue());
                break;
            case PidTagDisplayName:
                this.addToName((String) property.getValue());
                break;
            case PidTagDisplayTo:
                this.setDisplayTo((String) property.getValue());
                break;
            case PidTagDisplayCc:
                this.setDisplayCc((String) property.getValue());
                break;
            case PidTagDisplayBcc:
                this.setDisplayBcc((String) property.getValue());
                break;
            case PidTagBody:
                this.setBodyText((String) property.getValue());
                break;
            case PidTagBodyHtml:
                this.setBodyHtml(((String) property.getValue()).getBytes(StandardCharsets.UTF_8));
                break;
            case PidTagHtml:
                this.setBodyHtml((byte[]) property.getValue());
                break;
            case PidTagRtfCompressed:
                this.setCompressedRTF((byte[]) property.getValue());
                break;
            case PidTagTransportMessageHeaders:
                // email headers
                String headers = (String) property.getValue();
                this.setHeaders(headers);
                // try to parse the date from the headers
                ZonedDateTime date = Message.getDateFromHeaders(headers);
                if (date != null) {
                    this.setDate(date);
                }
                break;
        }
    }

    /**
     * Provides a short representation of this .msg object.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return headerString() + this.attachments.size() + " attachments.";
    }

    private String headerString() {
        StringBuilder sb = new StringBuilder();
        sb.append("From: ").append(Message.createMailString(this.fromEmail, this.fromName)).append('\n');
        if (toEmail != null || toName != null) {
            sb.append("To: ");
            sb.append(Message.createMailString(toEmail, toName)).append('\n');
        }
        String ccEmail = display(RecipientType.CC);
        if (!ccEmail.isEmpty()) {
            sb.append("Cc: ").append(ccEmail).append('\n');
        }
        String bccEmail = display(RecipientType.BCC);
        if (!bccEmail.isEmpty()) {
            sb.append("Bcc: ").append(bccEmail).append('\n');
        }
        if (this.date != null) {
            sb.append("Date: ").append(this.date.format(DateTimeFormatter.RFC_1123_DATE_TIME)).append('\n');
        }
        if (this.subject != null) {
            sb.append("Subject: ").append(this.subject).append('\n');
        }
        return sb.toString();
    }

    /**
     * Convenience method for creating an email address expression (including the
     * name, the address, or both).
     *
     * @param mail The mail address.
     * @param name The name part of the address.
     * @return A combination of the name and address.
     */
    private static String createMailString(String mail, String name) {
        if (isBlank(mail) && isBlank(name)) {
            return null;
        }
        if (isBlank(name)) {
            return mail;
        }
        if (isBlank(mail)) {
            return name;
        }
        if (mail.equalsIgnoreCase(name)) {
            return mail;
        }
        return '"' + name + "\" <" + mail + '>';
    }

    /**
     * @return the attachments
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * @return the recipients
     */
    public List<RecipientEntry> getRecipients() {
        return recipients;
    }

    /**
     * @return the bodyText
     */
    public String getBodyText() {
        return bodyText;
    }

    /**
     * @param bodyText the bodyText to set
     */
    public void setBodyText(String bodyText) {
        this.bodyText = trimNonNull(bodyText);
    }

    /**
     * @return the fromEmail
     */
    public String getFromEmail() {
        return fromEmail;
    }

    /**
     * @param fromEmail the fromEmail to set
     */
    public void setFromEmail(String fromEmail) {
        this.fromEmail = trimNonNull(fromEmail);
    }

    public String getFromSMTPAddress() {
        return fromSMTPAddress;
    }

    public void setFromSMTPAddress(String fromSMTPAddress) {
        this.fromSMTPAddress = fromSMTPAddress;
    }

    public String getFromAddressType() {
        return this.fromAddressType;
    }

    public void setFromAddressType(String fromAddressType) {
        this.fromAddressType = fromAddressType;
    }

    /**
     * @return the fromName
     */
    public String getFromName() {
        return fromName;
    }

    /**
     * @param fromName the fromName to set
     */
    public void setFromName(String fromName) {
        this.fromName = trimNonNull(fromName);
    }

    public String getDisplayTo() {
        return displayTo != null ? displayTo : display(RecipientType.TO);
    }

    private void setDisplayTo(String displayTo) {
        this.displayTo = displayTo;
    }

    public String getDisplayCc() {
        return displayCc != null ? displayCc : display(RecipientType.CC);
    }

    private void setDisplayCc(String displayCc) {
        this.displayCc = displayCc;
    }

    public String getDisplayBcc() {
        return displayBcc != null ? displayBcc : display(RecipientType.BCC);
    }

    private void setDisplayBcc(String displayBcc) {
        this.displayBcc = displayBcc;
    }

    /**
     * @param messageClass the messageClass to set
     */
    private void setMessageClass(String messageClass) {
        this.messageClass = messageClass;
    }

    /**
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = trimNonNull(subject);
        this.subjectPrefix = findSubjectPrefix();
        this.topic = this.subject.substring(subjectPrefix.length());
    }

    public String getSubjectPrefix() {
        return subjectPrefix.isEmpty() ? "" : subjectPrefix.trim() + ' ';
    }

    public String getTopic() {
        return topic;
    }

    /**
     * @return the toEmail
     */
    public String getToEmail() {
        return toEmail;
    }

    /**
     * @param toEmail the toEmail to set
     */
    private void addToEmail(String toEmail) {
        this.toEmail = trimNonNull(toEmail);
    }

    /**
     * @return the toName
     */
    public String getToName() {
        return toName;
    }

    /**
     * @param toName the toName to set
     */
    private void addToName(String toName) {
        this.toName = trimNonNull(toName);
    }

    public byte[] getCompressedRTF() {
        return compressedRTF;
    }

    private void setCompressedRTF(byte[] compressedRTF) {
        this.compressedRTF = compressedRTF;
    }

    public String getBodyRTF() {
        if (bodyRTF == null && compressedRTF != null) {
            bodyRTF = decompressRTF();
        }

        return bodyRTF;
    }

    public void setBodyRTF(String bodyRTF) {
        this.bodyRTF = bodyRTF;
    }

    public byte[] getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(byte[] bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    /**
     * @return the headers
     */
    public String getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(String headers) {
        this.headers = headers;
    }

    /**
     * Parses the message date from the mail headers.
     *
     * @param headers The headers in a single String object
     * @return The Date object or null, if no valid Date: has been found
     */
    private static ZonedDateTime getDateFromHeaders(String headers) {
        if (headers == null) {
            return null;
        }
        return Stream.of(headers.split("\n"))
                .filter(headerLine -> headerLine.toLowerCase().startsWith(DATE_HEADER_KEY))
                .findFirst()
                .map(headerLine -> headerLine.substring(DATE_HEADER_KEY.length()).trim())
                .map(Message::toDate)
                .orElse(null);
    }

    private static ZonedDateTime toDate(String dateValue) {
        try {
            return ZonedDateTime.from(DATE_TIME_FORMATTER.parse(dateValue));
        } catch (Exception e) {
            LOGGER.info("Could not parse date " + dateValue, e);
            return null;
        }
    }

    /**
     * @return the date
     */
    public ZonedDateTime getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    private String findSubjectPrefix() {
        Matcher matcher = PREFIX_PATTERN.matcher(subject);
        return matcher.find() ? matcher.group() : "";
    }

    private String display(RecipientType type) {
        return this.recipients.stream()
                .filter(recipientEntry -> recipientEntry.getType() == type)
                .map(recipientEntry -> createMailString(recipientEntry.mailTo(), recipientEntry.getName()))
                .collect(joining("; "));
    }

    private String decompressRTF() {
        try {
            CompressedRTF decompressor = new CompressedRTF();
            byte[] decompressedRTF = decompressor.decompress(new ByteArrayInputStream(compressedRTF));
            return new String(decompressedRTF, 0, decompressor.getDeCompressedSize());
        } catch (IOException ex) {
            return getFromUnicodeLE(compressedRTF);
        }
    }

    private static String trimNonNull(String subject1) {
        return subject1 == null ? "" : subject1.trim();
    }
}
