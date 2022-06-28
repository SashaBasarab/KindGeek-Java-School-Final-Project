package basarab.olexandr.springfinalproject.service;

import basarab.olexandr.springfinalproject.dto.request.UserDTO;
import basarab.olexandr.springfinalproject.dto.response.MessageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GroupChatService {

    void createNewGroupChat(Long ownerId, List<Long> participants, String name);

    void updateGroupAvatar(MultipartFile multipartFile, Long groupChatId);

    void addMember(Long groupChatId, Long userToAddId);

    void deleteMember(Long groupChatId, Long userToDeleteId);

    void sendNewMessage(String messageValue, Long userSenderId, Long groupChatId, MultipartFile multipartFile, List<MultipartFile> multipartFiles);

    void sendNewMessage(String messageValue, Long userSenderId, Long groupChatId);

    void sendNewMessage(String messageValue, Long userSenderId, Long groupChatId, MultipartFile multipartFile);

    void sendNewMessage(String messageValue, Long userSenderId, Long groupChatId, List<MultipartFile> multipartFiles);

    void updateMessage(String messageValue, Long messageId, MultipartFile multipartFile, List<MultipartFile> multipartFiles);

    void updateMessage(String messageValue, Long messageId);

    void updateMessage(String messageValue, Long messageId, MultipartFile multipartFile);

    void updateMessage(String messageValue, Long messageId, List<MultipartFile> multipartFiles);

    void deleteExistingMessage(Long messageId);

    MessageDTO getRecentMessage(Long groupChatId);

    List<MessageDTO> getAllMessages(Long groupChatId);

    List<MessageDTO> findSimilarMessages(Long chatId, String messageValue);

}
