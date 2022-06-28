package basarab.olexandr.springfinalproject.controller;

import basarab.olexandr.springfinalproject.dto.response.DirectChatResponseDTO;
import basarab.olexandr.springfinalproject.dto.response.MessageDTO;
import basarab.olexandr.springfinalproject.entity.DirectChat;
import basarab.olexandr.springfinalproject.service.DirectChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/chat")
public class DirectChatController {

    @Autowired
    private DirectChatService directChatService;

    @PostMapping("/create-new-chat/{user1Id}/{user2Id}")
    public void createNewDirectChat(@PathVariable Long user1Id,
                                    @PathVariable Long user2Id) {
        log.info("Request: user1Id: {},\n user2Id: {}", user1Id, user2Id);
        directChatService.createNewDirectChat(user1Id, user2Id);
    }

    @GetMapping("/get-all-direct-chats-by-user-id/{userId}")
    public List<DirectChatResponseDTO> getAllDirectChatsByUserId(@PathVariable Long userId) {
        log.info("Request: user1Id: {}", userId);
        return directChatService.getAllDirectChatsByUserId(userId);
    }

    @PostMapping("/send-new-message/{userSenderId}/{userReceiverId}/{directChatId}")
    public void sendNewMessage(@RequestParam String messageValue,
                               @PathVariable Long userSenderId,
                               @PathVariable Long userReceiverId,
                               @PathVariable Long directChatId,
                               @RequestParam(required = false) List<MultipartFile> multipartFiles,
                               @RequestParam(required = false) MultipartFile multipartFile) {
        log.info("Request: messageValue: {},\n userSenderId: {},\n userReceiverId: {},\n directChatId: {},\n multipartFile: {},\n multipartFiles: {}",
                messageValue, userSenderId, userReceiverId, directChatId, multipartFile, multipartFiles);
        directChatService.sendNewMessage(messageValue, userSenderId, userReceiverId, directChatId, multipartFile, multipartFiles);
    }

    @PutMapping("/update-message/{messageId}")
    public void updateMessage(@RequestParam String messageValue,
                              @PathVariable Long messageId,
                              @RequestParam(required = false) List<MultipartFile> multipartFiles,
                              @RequestParam(required = false) MultipartFile multipartFile) {
        log.info("Request: messageValue: {},\n messageId: {},\n multipartFile: {},\n multipartFiles: {}",
                messageValue, messageId, multipartFile, multipartFiles);
        directChatService.updateMessage(messageValue, messageId, multipartFile ,multipartFiles);
    }

    @DeleteMapping("/delete-message/{messageId}")
    public void deleteExistingMessage(@PathVariable Long messageId) {
        log.info("Request: messageId: {}", messageId);
        directChatService.deleteExistingMessage(messageId);
    }

    @GetMapping("/get-all-messages/{chatId}")
    public List<MessageDTO> getAllMessages(@PathVariable Long chatId) {
        log.info("Request: chatId: {}", chatId);
        return directChatService.getAllMessages(chatId);
    }

    @GetMapping("/{directChatId}/get-recent-message")
    public MessageDTO getRecentMessage(@PathVariable Long directChatId) {
        log.info("Request: directChatId: {}", directChatId);
        return directChatService.getRecentMessage(directChatId);
    }

    @GetMapping("/{chatId}/find-similar-messages")
    public List<MessageDTO> findSimilarMessages(@PathVariable Long chatId,
                                                @RequestParam String messageValue) {
        log.info("Request: chatId: {},\n messageValue: {}", chatId, messageValue);
        return directChatService.findSimilarMessages(chatId, messageValue);
    }

}
