package basarab.olexandr.springfinalproject.entity;

import basarab.olexandr.springfinalproject.enums.MessageStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime creationTime;

    private String messageValue;

    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "group_chat_id")
    private GroupChat groupChat;

    @ManyToOne
    @JoinColumn(name = "direct_chat_id")
    private DirectChat directChat;

    private MessageStatus messageStatus;

    @OneToMany(mappedBy = "message")
    private List<MessageImage> messageImages = new ArrayList<>();

}
