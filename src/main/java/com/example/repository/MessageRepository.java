package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.accountId = ?1")
    public int userExist(int accountId);

    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.messageText = :messageText WHERE m.messageId = :messageId")
    public int updateMessageById(@Param("messageId") int messageId, @Param("messageText") String messageText);

    @Query("SELECT m FROM Message m WHERE m.postedBy = :accountId")
    public List<Message> getAllMessagesByAccountId(@Param("accountId") int accountId);

    @Query("SELECT m FROM Message m WHERE m.messageId = ?1")
    public int messageIdExist(int messageId);

}
