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

/**
 * This class represents a recipient's entry of the parsed .msg file. It provides informations like the
 * email address and the display name.
 * @author thomas.misar
 * @author roman.kurmanowytsch
 */
public class RecipientEntry {

    private String email;
    private String smtp;
    private String addressType;
    private String name;
    private RecipientType type;

    void setProperty(Property property) {
        switch(property.getPid()) {
            case PidTagRecipientType:
                setType(RecipientType.from((int) property.getValue()));
                break;
            case PidTagDisplayName:
                setName((String) property.getValue());
                break;
            case PidTagAddressType:
                setAddressType((String) property.getValue());
                break;
            case PidTagEmailAddress:
                setEmail((String) property.getValue());
                break;
            case PidTagSmtpAddress:
                setSmtp((String) property.getValue());
                break;
        }
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RecipientType getType() {
        return type;
    }

    public void setType(RecipientType type) {
        this.type = type;
    }

    /**
     * Provides a short representation of this recipient object <br>
     * (e.g. 'Firstname Lastname &lt;firstname.lastname@domain.tld&gt;').
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        if (sb.length() > 0) {
            sb.append(" ");
        }
        if ((this.email != null) && (this.email.length() > 0)) {
            sb.append("<").append(this.email).append(">");
        }
        return sb.toString();
    }
}
