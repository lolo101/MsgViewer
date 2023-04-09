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
package com.auxilii.msgparser.attachment;

import com.auxilii.msgparser.Message;

/**
 * This {@link Attachment} implementation
 * represents a .msg object attachment. Instead
 * of storing a byte[] of the attachment, this
 * implementation provides an embedded
 * {@link Message} object.
 *
 * @author roman.kurmanowytsch
 */
public class MsgAttachment implements Attachment {

	/**
	 * The encapsulated (attached) message.
	 */
	private final Message message;

	public MsgAttachment(Message message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * Returns the String returned by
	 * {@link Message#toString()}.
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.message == null) {
			return null;
		}
		return "Mail Attachment: " + this.message;
	}
}
