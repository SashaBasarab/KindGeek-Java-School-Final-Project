package basarab.olexandr.springfinalproject.service.Impl;

import basarab.olexandr.springfinalproject.dto.response.DirectChatResponseDTO;
import basarab.olexandr.springfinalproject.dto.response.MessageDTO;
import basarab.olexandr.springfinalproject.entity.DirectChat;
import basarab.olexandr.springfinalproject.entity.Message;
import basarab.olexandr.springfinalproject.entity.User;
import basarab.olexandr.springfinalproject.enums.MessageStatus;
import basarab.olexandr.springfinalproject.exceptions.DirectChatAlreadyExists;
import basarab.olexandr.springfinalproject.exceptions.NoSuchChatException;
import basarab.olexandr.springfinalproject.exceptions.NoSuchMessageException;
import basarab.olexandr.springfinalproject.exceptions.UserNotFoundException;
import basarab.olexandr.springfinalproject.repository.DirectChatRepository;
import basarab.olexandr.springfinalproject.repository.MessageRepository;
import basarab.olexandr.springfinalproject.repository.UserRepository;
import basarab.olexandr.springfinalproject.service.DirectChatService;
import basarab.olexandr.springfinalproject.service.MessageFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectChatServiceImpl implements DirectChatService {

    @Autowired
    private DirectChatRepository directChatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageFileStorageService messageFileStorageService;

    @Override
    public void createNewDirectChat(Long user1Id, Long user2Id) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + user1Id + " is not found"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + user2Id + " is not found"));
        if (directChatRepository.findByUser1AndUser2(user1, user2).isPresent() || directChatRepository.findByUser1AndUser2(user2, user1).isPresent()) {
            throw new DirectChatAlreadyExists("Direct chat already exists");
        }
        DirectChat directChat = new DirectChat();
        directChat.setUser1(user1);
        directChat.setUser2(user2);
        directChatRepository.save(directChat);
    }

    @Override
    public List<DirectChatResponseDTO> getAllDirectChatsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " is not found"));
        return directChatRepository.findAllByUser1OrUser2(user, user).stream()
                .map(DirectChatResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public void sendNewMessage(String messageValue, Long userSenderId, Long userReceiverId, Long directChatId) {
        Message message = sendMessageDuplicate(messageValue, userSenderId, userReceiverId, directChatId);
        messageRepository.save(message);
    }

    private Message sendMessageDuplicate(String messageValue, Long userSenderId, Long userReceiverId, Long directChatId) {
        Message message = new Message();
        message.setMessageValue(messageValue);
        message.setCreationTime(LocalDateTime.now());
        message.setSender(userRepository.findById(userSenderId)
                .orElseThrow(() -> new UserNotFoundException("Sender with id " + userSenderId + " is not found")));
        message.setReceiver(userRepository.findById(userReceiverId)
                .orElseThrow(() -> new UserNotFoundException("Receiver with id " + userReceiverId + " is not found")));
        message.setMessageStatus(MessageStatus.SENT);
        message.setDirectChat(directChatRepository.findById(directChatId)
                .orElseThrow(() -> new NoSuchChatException("Provided chat id does not exist")));
        return message;
    }

    @Override
    public void sendNewMessage(String messageValue, Long userSenderId, Long userReceiverId, Long directChatId, MultipartFile multipartFile) {
        Message message = sendMessageDuplicate(messageValue, userSenderId, userReceiverId, directChatId);
        messageFileStorageService.saveImg(multipartFile, message);
        messageRepository.save(message);
    }

    @Override
    public void sendNewMessage(String messageValue, Long userSenderId, Long userReceiverId, Long directChatId, List<MultipartFile> multipartFile) {
        Message message = sendMessageDuplicate(messageValue, userSenderId, userReceiverId, directChatId);
        messageFileStorageService.saveImgs(multipartFile, message);
        messageRepository.save(message);
    }

    @Override
    public void sendNewMessage(String messageValue, Long userSenderId, Long userReceiverId, Long directChatId, MultipartFile multipartFile, List<MultipartFile> multipartFiles) {
        if (multipartFiles != null) {
            sendNewMessage(messageValue, userSenderId, userReceiverId, directChatId, multipartFiles);
        } else if (multipartFile != null) {
            sendNewMessage(messageValue, userSenderId, userReceiverId, directChatId, multipartFile);
        } else {
            sendNewMessage(messageValue, userSenderId, userReceiverId, directChatId);
        }
    }

    @Override
    public void updateMessage(String messageValue, Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchMessageException("Message is not found"));
        message.setMessageValue(messageValue);
        messageRepository.save(message);
    }

    @Override
    public void updateMessage(String messageValue, Long messageId, MultipartFile multipartFile) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchMessageException("Message is not found"));
        message.setMessageValue(messageValue);
        messageFileStorageService.deleteImgs(message);
        messageFileStorageService.saveImg(multipartFile, message);
    }

    @Override
    public void updateMessage(String messageValue, Long messageId, List<MultipartFile> multipartFiles) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchMessageException("Message is not found"));
        message.setMessageValue(messageValue);
        messageFileStorageService.deleteImgs(message);
        messageFileStorageService.saveImgs(multipartFiles, message);
    }

    @Override
    public void updateMessage(String messageValue, Long messageId, MultipartFile multipartFile, List<MultipartFile> multipartFiles) {
        if (multipartFiles != null) {
            updateMessage(messageValue, messageId, multipartFiles);
        } else if (multipartFile != null){
            updateMessage(messageValue, messageId, multipartFile);
        } else {
            updateMessage(messageValue, messageId);
        }
    }

    @Override
    public void deleteExistingMessage(Long messageId) {
        messageRepository.deleteMessageById(messageId);
    }

    @Override
    public List<MessageDTO> getAllMessages(Long chatId) {
        DirectChat directChat = directChatRepository.findById(chatId)
                .orElseThrow(() -> new NoSuchChatException("Provided chat id does not exist"));
        return messageRepository.findAllByDirectChat(directChat).stream()
                .map(MessageDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public MessageDTO getRecentMessage(Long directChatId) {
        DirectChat directChat = directChatRepository.findById(directChatId)
                .orElseThrow(() -> new NoSuchChatException("Provided chat id does not exist"));
        return MessageDTO.from(messageRepository.findFirstByDirectChatOrderByIdDesc(directChat));
    }

    @Override
    public List<MessageDTO> findSimilarMessages(Long chatId, String messageValue) {
        DirectChat directChat = directChatRepository.findById(chatId)
                .orElseThrow(() -> new NoSuchChatException("Provided chat id does not exist"));
        return messageRepository.findAllByDirectChatAndMessageValueLike(directChat, "%" + messageValue + "%").stream()
                .map(MessageDTO::from)
                .collect(Collectors.toList());
    }
}
