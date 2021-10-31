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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    public static final Pattern PREFIX_PATTERN = Pattern.compile("^[^:\\s\\d]{1,3}:\\s+");

    /**
     * The message class as defined in the .msg file.
     */
    private String messageClass = "IPM.Note";
    /**
     * The message Id.
     */
    private String messageId = null;
    /**
     * The address part of From: mail address.
     */
    private String fromEmail = null;

    private String fromSMTPAddress = null;

    /**
     * The name part of the From: mail address
     */
    private String fromName = null;
    private String fromAddressType;

    private String toEmail;
    private String toName;
    private final List<String> ccEmail = new ArrayList<>();
    private final List<String> ccName = new ArrayList<>();
    private final List<String> bccEmail = new ArrayList<>();
    private final List<String> bccName = new ArrayList<>();
    /**
     * The mail's subject.
     */
    private String subject = null;
    private String subjectPrefix;
    private String topic;
    /**
     * The normalized body text.
     */
    private String bodyText = null;
    /**
     * The displayed To: field
     */
    private String displayTo = null;
    /**
     * The displayed Cc: field
     */
    private String displayCc = null;
    /**
     * The displayed Bcc: field
     */
    private String displayBcc = null;

    private byte[] compressedRTF;

    private String bodyRTF;

    private String bodyHtml;

    /**
     * Email headers (if available)
     */
    private String headers = null;

    /**
     * Email Date
     */
    private Date date = null;

    /**
     * A list of all attachments (both {@link FileAttachment} and
     * {@link MsgAttachment}).
     */
    private List<Attachment> attachments = new ArrayList<>();
    /**
     * Contains all properties that are not covered by the special properties.
     */
    private final Map<Pid, Object> properties = new HashMap<>();
    /**
     * A list containing all recipients for this message (which can be set in the
     * 'to:', 'cc:' and 'bcc:' field, respectively).
     */
    private List<RecipientEntry> recipients = new ArrayList<>();

    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }

    public void addRecipient(RecipientEntry recipient) {
        this.recipients.add(recipient);
    }

    void setProperty(Property property) {
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
            case PidTagRtfCompressed:
                this.setCompressedRTF((byte[]) property.getValue());
                break;
            case PidTagTransportMessageHeaders:
                // email headers
                String headers = (String) property.getValue();
                this.setHeaders(headers);
                // try to parse the date from the headers
                Date date = Message.getDateFromHeaders(headers);
                if (date != null) {
                    this.setDate(date);
                }
                break;
        }

        // save all properties (incl. those identified above)
        this.properties.put(property.getPid(), property.getValue());
    }

    /**
     * Provides a short representation of this .msg object.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = headerString();
        sb.append(this.attachments.size()).append(" attachments.");
        return sb.toString();
    }

    /**
     * Provides all information of this message object.
     *
     * @return The full message information.
     */
    public String toLongString() {
        StringBuilder sb = headerString();
        sb.append("\n");
        if (this.bodyText != null) {
            sb.append(this.bodyText);
        }
        if (this.attachments.size() > 0) {
            sb.append("\n");
            sb.append(this.attachments.size()).append(" attachments.\n");
            for (Attachment att : this.attachments) {
                sb.append(att.toString()).append("\n");
            }
        }
        return sb.toString();
    }

    private StringBuilder headerString() {
        StringBuilder sb = new StringBuilder();
        sb.append("From: ").append(Message.createMailString(this.fromEmail, this.fromName)).append("\n");
        if (toEmail != null || toName != null) {
            sb.append("To: ");
            sb.append(Message.createMailString(toEmail, toName)).append("\n");
        }
        if (!ccEmail.isEmpty()) {
            sb.append("Cc: ");
            for (int i = 0; i < ccEmail.size(); ++i) {
                sb.append(Message.createMailString(ccEmail.get(i), ccName.get(i))).append("\n");
            }
        }
        if (!bccEmail.isEmpty()) {
            sb.append("Bcc: ");
            for (int i = 0; i < bccEmail.size(); ++i) {
                sb.append(Message.createMailString(bccEmail.get(i), bccName.get(i))).append("\n");
            }
        }
        if (this.date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            sb.append("Date: ").append(formatter.format(this.date)).append("\n");
        }
        if (this.subject != null) {
            sb.append("Subject: ").append(this.subject).append("\n");
        }
        return sb;
    }

    /**
     * Convenience method for creating an email address expression (including the
     * name, the address, or both).
     *
     * @param mail The mail address.
     * @param name The name part of the address.
     * @return A combination of the name and address.
     */
    public static String createMailString(String mail, String name) {
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
        return "\"" + name + "\" <" + mail + ">";
    }

    /**
     * @return the attachments
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * @param attachments the attachments to set
     */
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * @return the recipients
     */
    public List<RecipientEntry> getRecipients() {
        return recipients;
    }

    /**
     * @param recipients the recipients to set
     */
    public void setRecipients(List<RecipientEntry> recipients) {
        this.recipients = recipients;
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
        this.bodyText = nullOrTrim(bodyText);
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
        this.fromEmail = nullOrTrim(fromEmail);
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
        this.fromName = nullOrTrim(fromName);
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
     * @return the messageClass
     */
    public String getMessageClass() {
        return messageClass;
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
        this.subject = nullOrTrim(subject);
        this.subjectPrefix = findSubjectPrefix();
        this.topic = subject.substring(subjectPrefix.length());
    }

    public String getSubjectPrefix() {
        return subjectPrefix.isEmpty() ? "" : subjectPrefix.trim() + " ";
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
        this.toEmail = nullOrTrim(toEmail);
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
        this.toName = nullOrTrim(toName);
    }

    public List<String> getCcEmail() {
        return ccEmail;
    }

    public void addCcEmail(String ccEmail) {
        this.ccEmail.add(nullOrTrim(ccEmail));
    }

    public List<String> getCcName() {
        return ccName;
    }

    public void addCcName(String ccName) {
        this.ccName.add(nullOrTrim(ccName));
    }

    public List<String> getBccEmail() {
        return bccEmail;
    }

    public void addBccEmail(String bccEmail) {
        this.bccEmail.add(nullOrTrim(bccEmail));
    }

    public List<String> getBccName() {
        return bccName;
    }

    public void addBccName(String bccName) {
        this.bccName.add(nullOrTrim(bccName));
    }

    public byte[] getCompressedRTF() {
        return compressedRTF;
    }

    public void setCompressedRTF(byte[] compressedRTF) {
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

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
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
    public static Date getDateFromHeaders(String headers) {
        if (headers == null) {
            return null;
        }
        String[] headerLines = headers.split("\n");
        for (String headerLine : headerLines) {
            if (headerLine.toLowerCase().startsWith("date:")) {
                String dateValue = headerLine.substring("Date:".length()).trim();
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                try {
                    return formatter.parse(dateValue);
                } catch (Exception e) {
                    LOGGER.info("Could not parse date " + dateValue, e);
                }
                // there is only one Date: header; we can exit the loop here
                break;
            }
        }
        return null;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    public Object getProperty(Pid name) {
        return this.properties.get(name);
    }

    private String findSubjectPrefix() {
        Matcher matcher = PREFIX_PATTERN.matcher(subject);
        return matcher.find() ? matcher.group() : "";
    }

    private String display(RecipientType type) {
        return this.recipients.stream()
                .filter(recipientEntry -> recipientEntry.getType() == type)
                .map(recipientEntry -> createMailString(recipientEntry.mailTo(), recipientEntry.getName()))
                .collect(Collectors.joining(";"));
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

    private static String nullOrTrim(String subject1) {
        return subject1 == null ? null : subject1.trim();
    }
}
