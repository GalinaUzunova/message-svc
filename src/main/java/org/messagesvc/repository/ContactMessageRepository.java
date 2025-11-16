package org.messagesvc.repository;

import org.messagesvc.model.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, UUID> {

    @Query("SELECT cm FROM ContactMessage cm WHERE DATE(cm.createdAt) = :date")
    List<ContactMessage> findByCreatedAtDate(@Param("date") LocalDate date);

    List<ContactMessage>findAllByOrderByCreatedAtDesc();

    Optional<ContactMessage> getAllById(UUID id);
}
