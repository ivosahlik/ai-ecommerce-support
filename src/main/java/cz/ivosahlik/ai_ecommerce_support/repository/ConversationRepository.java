package cz.ivosahlik.ai_ecommerce_support.repository;

import cz.ivosahlik.ai_ecommerce_support.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
