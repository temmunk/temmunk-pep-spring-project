package com.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService= messageService;
    }

    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody Account account){

        try {
            Account newAccount = accountService.register(account);
            return ResponseEntity.ok(newAccount);
        } catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username taken");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("login")
    public ResponseEntity<Optional<Account>> login(@RequestBody Account account){
        Optional<Account> loginAccount = accountService.login(account.getUsername(), account.getPassword());
        
        if (loginAccount.isPresent()){
            return ResponseEntity.ok(loginAccount);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .build();
        }
        

    }

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        Message createdMessage = messageService.createMessage(message);

        if (createdMessage != null){
            return ResponseEntity.status(200)
            .body(message);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .build();
        }
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> messages = messageService.getAllMessages();

        return ResponseEntity.ok(messages);
    }

    @GetMapping("messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable("message_id") int messageId){
        Optional<Message> message = messageService.getMessageById(messageId);

        return message.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.OK).body(null));
        
    }

    @DeleteMapping("messages/{message_id}")
    public ResponseEntity<Object> deleteMessage(@PathVariable("message_id") int messageId){
        
        int deletedRows = messageService.deleteMessageById(messageId);

        if (deletedRows > 0){
            return ResponseEntity.ok(deletedRows);
        } else {
            return ResponseEntity.status(HttpStatus.OK)
            .body(null);
        }
    }

    @PatchMapping("messages/{message_id}")
    public ResponseEntity<Object> updateMessageById(@PathVariable("message_id") int messageId, @RequestBody Map<String, String> requestBody){
            String newMessageText = requestBody.get("messageText");

            if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Message");
            }

            try {
                int updatedRows = messageService.updateMessageById(messageId, newMessageText);
                if (updatedRows > 0 ){
                    return ResponseEntity.ok(updatedRows);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
                }
            } catch (Exception e ){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
            }
        

    }

    @GetMapping("accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessageByAccountId(@PathVariable("account_id") int accountId){
        List<Message> messagesByAccountId = messageService.getAllMessagesByAccountId(accountId);
        return ResponseEntity.ok(messagesByAccountId);
    }

}
