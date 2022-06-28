package basarab.olexandr.springfinalproject.dto.response;

import basarab.olexandr.springfinalproject.entity.DirectChat;
import basarab.olexandr.springfinalproject.entity.Message;
import basarab.olexandr.springfinalproject.entity.MessageImage;
import basarab.olexandr.springfinalproject.entity.User;
import basarab.olexandr.springfinalproject.enums.MessageStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class MessageDTO {

    private Long id;

    private LocalDateTime creationTime;

    private String messageValue;

    private Long senderId;

    private Long receiverId;

    private Long directChatId;

    private MessageStatus messageStatus;

    private List<String> messageImageUrl;

    public static MessageDTO from(Message message) {
        if (message == null) {
            return null;
        }

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setCreationTime(message.getCreationTime());
        messageDTO.setMessageValue(message.getMessageValue());
        messageDTO.setSenderId(message.getSender().getId());
        messageDTO.setReceiverId(message.getReceiver().getId());
        messageDTO.setDirectChatId(message.getDirectChat().getId());
        messageDTO.setMessageStatus(message.getMessageStatus());
        messageDTO.setMessageImageUrl(message.getMessageImages().stream()
                .map(MessageImage::getImgUrl)
                .collect(Collectors.toList()));
        return messageDTO;
    }

}
