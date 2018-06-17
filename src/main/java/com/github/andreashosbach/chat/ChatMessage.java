package com.github.andreashosbach.chat;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
public class ChatMessage {

	@javax.persistence.Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, length = 30)
	private String user;

	@Column(nullable = true, length = 140)
	private String text;

	public ChatMessage() {

	}

	public ChatMessage(String user, String text) {
		this.user = user;
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean equals(Object other) {

		if (other == this) {
			return true;
		}
		if (!(other instanceof ChatMessage)) {
			return false;
		}
		ChatMessage chatMessage = (ChatMessage) other;
		return Objects.equals(user, chatMessage.user) && Objects.equals(text, chatMessage.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, text);
	}

	@Override
	public String toString() {
		return String.format("%s : %s", user, text);
	}
}
