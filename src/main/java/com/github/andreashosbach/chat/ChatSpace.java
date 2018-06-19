package com.github.andreashosbach.chat;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

@Component
public class ChatSpace {

	@PersistenceContext
	EntityManager em;

	@Transactional
	public void addRoom(String name) {
		ChatRoom room = new ChatRoom(name);
		em.persist(room);
	}

	public List<ChatRoom> getRooms() {
		TypedQuery<ChatRoom> query = em.createNamedQuery("ChatRoom.findAll", ChatRoom.class);
		return query.getResultList();
	}

	public ChatRoom getRoom(String name) {
		return em.find(ChatRoom.class, name);
	}

	@Transactional
	public void deleteRoom(String name) {
		Optional.ofNullable(getRoom(name)).ifPresent(r -> em.remove(r));
	}

	public boolean addMessage(String name, ChatMessage message) {
		return Optional.ofNullable(getRoom(name)).map((r) -> {
			r.add(message);
			em.persist(message);
			em.persist(r);
			return true;
		}).orElse(false);
	}
}