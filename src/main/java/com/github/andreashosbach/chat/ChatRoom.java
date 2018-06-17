package com.github.andreashosbach.chat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "ChatRoom.findAll", query = "SELECT r FROM ChatRoom r")
public class ChatRoom {

	@Id
	@Column(nullable = false, length = 100)
	String name;

	@OneToMany(cascade = CascadeType.ALL)
	List<ChatMessage> messages = new ArrayList<>();

	public ChatRoom() {
	}

	public ChatRoom(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void add(ChatMessage message) {
		messages.add(message);
	}

	public List<ChatMessage> getMessages() {
		return messages;
	}
}
