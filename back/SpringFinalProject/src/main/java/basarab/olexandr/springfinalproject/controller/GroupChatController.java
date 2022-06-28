package basarab.olexandr.springfinalproject.controller;

import basarab.olexandr.springfinalproject.dto.request.UserDTO;
import basarab.olexandr.springfinalproject.dto.response.MessageDTO;
import basarab.olexandr.springfinalproject.service.GroupChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/group-chat")
public class GroupChatController {

    @Autowired
    private GroupChatService groupChatService;

    @PostMapping("/create-new-group-chat/{name}")
    public void createNewGroupChat(@RequestParam Long ownerId,
                                   @RequestBody List<Long> participantsId,
                                   @PathVariable String name) {
        log.info("Request: ownerId: {},\n participantsId: {},\n name: {}", ownerId, participantsId, name);
        groupChatService.createNewGroupChat(ownerId, participantsId, name);
    }

    @PutMapping("/update-group-avatar/{groupChatId}")
    @PreAuthorize("hasRole ('ADMIN')")
    public void updateGroupAvatar(@RequestParam MultipartFile multipartFile,
                                  @PathVariable Long groupChatId){
        log.info("Request: multipartFile: {},\n groupChatId: {}", multipartFile, groupChatId);
        groupChatService.updateGroupAvatar(multipartFile, groupChatId);
    }

    @PutMapping("/add-member/{groupChatId}/{userToAddId}")
    @PreAuthorize("hasRole ('ADMIN')")
    public void addMember(@PathVariable Long groupChatId,
                          @PathVariable Long userToAddId) {
        log.info("Request: groupChatId: {},\n userToAddId: {}", groupChatId, userToAddId);
        groupChatService.addMember(groupChatId, userToAddId);
    }

    @PutMapping("/delete-member/{groupChatId}/{userToDeleteId}")
    @PreAuthorize("hasRole ('ADMIN')")
    public void deleteMember(@PathVariable Long groupChatId,
                             @PathVariable Long userToDeleteId) {
        log.info("Request: groupChatId: {},\n userToDeleteId: {}", groupChatId, userToDeleteId);
        groupChatService.deleteMember(groupChatId, userToDeleteId);
    }

    @PostMapping("/send-new-message/{userSenderId}/{groupChatId}")
    public void sendNewMessage(@RequestParam String messageValue,
                               @PathVariable Long userSenderId,
                               @PathVariable Long groupChatId,
                               @RequestParam(required = false) List<MultipartFile> multipartFiles,
                               @RequestParam(required = false) MultipartFile multipartFile) {
        log.info("Request: messageValue: {},\n userSenderId: {},\n groupChatId: {},\n multipartFile: {},\n multipartFiles{}",
                messageValue, userSenderId, groupChatId, multipartFile, multipartFiles);
        groupChatService.sendNewMessage(messageValue, userSenderId, groupChatId, multipartFile, multipartFiles);
    }

    @PutMapping("/update-message/{messageId}")
    public void updateMessage(@RequestParam String messageValue,
                              @PathVariable Long messageId,
                              @RequestParam(required = false) MultipartFile multipartFile,
                              @RequestParam(required = false) List<MultipartFile> multipartFiles) {
        log.info("Request: messageValue: {},\n messageId: {},\n multipartFile: {},\n multipartFiles: {}");
        groupChatService.updateMessage(messageValue, messageId, multipartFile, multipartFiles);
    }

    @DeleteMapping("/delete-existing-message/{messageId}")
    public void deleteExistingMessage(@PathVariable Long messageId) {
        log.info("Request: messageId: {}", messageId);
        groupChatService.deleteExistingMessage(messageId);
    }

    @GetMapping("/get-recent-message/{groupChatId}")
    public MessageDTO getRecentMessage(@PathVariable Long groupChatId) {
        log.info("Request: groupChatId: {}", groupChatId);
        return groupChatService.getRecentMessage(groupChatId);
    }

    @GetMapping("/get-all-messages/{groupChatId}")
    public List<MessageDTO> getAllMessages(@PathVariable Long groupChatId) {
        log.info("Request: groupChatId: {}", groupChatId);
        return groupChatService.getAllMessages(groupChatId);
    }

    @GetMapping("/find-similar-messages/{chatId}")
    public List<MessageDTO> findSimilarMessages(@PathVariable Long chatId,
                                                @RequestParam String messageValue) {
        log.info("Request: chatId: {},\n messageValue: {}", chatId, messageValue);
        return groupChatService.findSimilarMessages(chatId, messageValue);
    }

}
