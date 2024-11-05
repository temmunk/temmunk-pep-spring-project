package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message message){
        if( !message.getMessageText().isBlank() && message.getMessageText().length() <256 && userExist(message.getPostedBy())){
            return messageRepository.save(message);
        } else {
            return null;
        }
        
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }
    
    public Optional<Message> getMessageById(int messageId){
        return messageRepository.findById(messageId);
    }

    public int deleteMessageById(int messageId){
        if (messageRepository.existsById(messageId)){
            messageRepository.deleteById(messageId);
            return 1;
        } else {
            return 0;
        }
    }

    public int updateMessageById(int messageId, String messageText){
        
        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isPresent()){
            Message updatedMessage = message.get();
            updatedMessage.setMessageText(messageText);
            messageRepository.save(updatedMessage);
            return 1;
        } else {
            return 0;
        }


    }

    public List<Message> getAllMessagesByAccountId(int accountId){
        return messageRepository.getAllMessagesByAccountId(accountId);
    }

    public boolean userExist(int accountId){
        if ( messageRepository.userExist(accountId) > 0){
            return true;
        } else {
            return false;
        }
    }
}
