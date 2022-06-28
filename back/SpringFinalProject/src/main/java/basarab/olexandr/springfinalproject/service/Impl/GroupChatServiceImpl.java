package basarab.olexandr.springfinalproject.service.Impl;

import basarab.olexandr.springfinalproject.dto.request.UserDTO;
import basarab.olexandr.springfinalproject.dto.response.MessageDTO;
import basarab.olexandr.springfinalproject.entity.GroupChat;
import basarab.olexandr.springfinalproject.entity.Message;
import basarab.olexandr.springfinalproject.entity.User;
import basarab.olexandr.springfinalproject.enums.MessageStatus;
import basarab.olexandr.springfinalproject.exceptions.NoSuchGroupChatException;
import basarab.olexandr.springfinalproject.exceptions.NoSuchMessageException;
import basarab.olexandr.springfinalproject.exceptions.UserNotFoundException;
import basarab.olexandr.springfinalproject.repository.GroupChatRepository;
import basarab.olexandr.springfinalproject.repository.MessageRepository;
import basarab.olexandr.springfinalproject.repository.UserRepository;
import basarab.olexandr.springfinalproject.service.GroupChatFileStorageService;
import basarab.olexandr.springfinalproject.service.GroupChatService;
import basarab.olexandr.springfinalproject.service.MessageFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupChatServiceImpl implements GroupChatService {

    @Autowired
    private GroupChatRepository groupChatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageFileStorageService messageFileStorageService;

    @Autowired
    private GroupChatFileStorageService groupChatFileStorageService;

    @Override
    public void createNewGroupChat(Long ownerId, List<Long> participantsId, String name) {
        GroupChat groupChat = new GroupChat();
        groupChat.setOwner(userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User is not found")));
        groupChat.setName(name);
        groupChat.setParticipants(participantsId.stream()
                .map(participatnId -> userRepository.findById(participatnId)
                        .orElseThrow(() -> new UserNotFoundException("User is not found")))
                .collect(Collectors.toSet()));
        groupChatRepository.save(groupChat);
    }

    @Override
    public void updateGroupAvatar(MultipartFile multipartFile, Long groupChatId) {
        GroupChat groupChat = groupChatRepository.findById(groupChatId)
                .orElseThrow(() -> new NoSuchGroupChatException("Group chat is not found"));
        groupChatFileStorageService.deleteImg(groupChat);
        groupChatFileStorageService.saveImg(multipartFile, groupChatId);
    }

    @Override
    public void addMember(Long groupChatId, Long userToAddId) {
        GroupChat groupChat = groupChatRepository.findById(groupChatId)
                .orElseThrow(() -> new NoSuchGroupChatException("Group chat is not found"));
        groupChat.getParticipants().add(userRepository.findById(userToAddId)
                .orElseThrow(() -> new UserNotFoundException("User is not found")));
        groupChatRepository.save(groupChat);
    }

    @Override
    public void deleteMember(Long groupChatId, Long userToDeleteId) {
        GroupChat groupChat = groupChatRepository.findById(groupChatId)
                .orElseThrow(() -> new NoSuchGroupChatException("Group chat is not found"));
        User user = userRepository.findById(userToDeleteId)
                .orElseThrow(() -> new UserNotFoundException("User is not found"));
        if (groupChat.getParticipants().contains(user)) {
            groupChat.getParticipants().remove(user);
        }
        groupChatRepository.save(groupChat);
    }

    private Message sendNewMessageDuplicate(String messageValue, Long userSenderId, Long groupChatId) {
        Message message = new Message();
        message.setMessageValue(messageValue);
        message.setCreationTime(LocalDateTime.now());
        message.setMessageStatus(MessageStatus.SENT);
        message.setGroupChat(groupChatRepository.findById(groupChatId)
                .orElseThrow(() -> new NoSuchGroupChatException("Group chat with id " + groupChatId + " is not found")));
        message.setSender(userRepository.findById(userSenderId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userSenderId + " is not found")));
        return message;
    }

    @Override
    public void sendNewMessage(String messageValue, Long userSenderId, Long groupChatId) {
        Message message = sendNewMessageDuplicate(messageValue, userSenderId, groupChatId);
        messageRepository.save(message);
    }

    @Override
    public void sendNewMessage(String messageValue, Long userSenderId, Long groupChatId, MultipartFile multipartFile) {
        Message message = sendNewMessageDuplicate(messageValue, userSenderId, groupChatId);
        messageFileStorageService.saveImg(multipartFile, message);
        messageRepository.save(message);
    }

    @Override
    public void sendNewMessage(String messageValue, Long userSenderId, Long groupChatId, List<MultipartFile> multipartFiles) {
        Message message = sendNewMessageDuplicate(messageValue, userSenderId, groupChatId);
        messageFileStorageService.saveImgs(multipartFiles, message);
        messageRepository.save(message);
    }

    @Override
    public void sendNewMessage(String messageValue, Long userSenderId, Long groupChatId, MultipartFile multipartFile, List<MultipartFile> multipartFiles) {
        if (multipartFile != null) {
            sendNewMessage(messageValue, userSenderId, groupChatId, multipartFile);
        } else if (multipartFiles != null) {
            sendNewMessage(messageValue, userSenderId, groupChatId, multipartFiles);
        } else {
            sendNewMessage(messageValue, userSenderId, groupChatId);
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
        if (multipartFile != null) {
            updateMessage(messageValue, messageId, multipartFile);
        } else if (multipartFiles != null) {
            updateMessage(messageValue, messageId, multipartFiles);
        } else {
            updateMessage(messageValue, messageId);
        }
    }

    @Override
    public void deleteExistingMessage(Long messageId) {
        messageRepository.deleteMessageById(messageId);
    }

    @Override
    public MessageDTO getRecentMessage(Long groupChatId) {
        GroupChat groupChat = groupChatRepository.findById(groupChatId)
                .orElseThrow(() -> new NoSuchGroupChatException("Group chat is not found"));
        return MessageDTO.from(messageRepository.findFirstByGroupChatOrderByIdDesc(groupChat));
    }

    @Override
    public List<MessageDTO> getAllMessages(Long groupChatId) {
        GroupChat groupChat = groupChatRepository.findById(groupChatId)
                .orElseThrow(() -> new NoSuchGroupChatException("Group chat is not found"));
        return groupChat.getMessages().stream()
                .map(MessageDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageDTO> findSimilarMessages(Long chatId, String messageValue) {
        GroupChat groupChat = groupChatRepository.findById(chatId)
                .orElseThrow(() -> new NoSuchGroupChatException("Group chat is not found"));
        return messageRepository.findAllByMessageValueLikeAndGroupChat(messageValue, groupChat).stream()
                .map(MessageDTO::from)
                .collect(Collectors.toList());
    }
}
