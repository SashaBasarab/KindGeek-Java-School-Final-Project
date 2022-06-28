package basarab.olexandr.springfinalproject.service;

import basarab.olexandr.springfinalproject.dto.response.DirectChatResponseDTO;
import basarab.olexandr.springfinalproject.dto.response.MessageDTO;
import basarab.olexandr.springfinalproject.entity.DirectChat;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DirectChatService {

    void createNewDirectChat(Long user1Id, Long user2Id);

    List<DirectChatResponseDTO> getAllDirectChatsByUserId(Long userId);

    void sendNewMessage(String messageValue, Long userSenderId, Long userReceiverId, Long directChatId, MultipartFile multipartFile, List<MultipartFile> multipartFiles);

    void sendNewMessage(String messageValue, Long userSenderId, Long userReceiverId, Long directChatId);

    void sendNewMessage(String messageValue, Long userSenderId, Long userReceiverId, Long directChatId, MultipartFile multipartFile);

    void sendNewMessage(String messageValue, Long userSenderId, Long userReceiverId, Long directChatId, List<MultipartFile> multipartFiles);

    void updateMessage(String messageValue, Long messageId, MultipartFile multipartFile, List<MultipartFile> multipartFiles);

    void updateMessage(String messageValue, Long messageId);

    void updateMessage(String messageValue, Long messageId, MultipartFile multipartFile);

    void updateMessage(String messageValue, Long messageId, List<MultipartFile> multipartFiles);

    void deleteExistingMessage(Long messageId);

    MessageDTO getRecentMessage(Long directChatId);

    List<MessageDTO> getAllMessages(Long chatId);

    List<MessageDTO> findSimilarMessages(Long chatId, String messageValue);

}
