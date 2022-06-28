package basarab.olexandr.springfinalproject.repository;

import basarab.olexandr.springfinalproject.entity.DirectChat;
import basarab.olexandr.springfinalproject.entity.GroupChat;
import basarab.olexandr.springfinalproject.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByMessageValueLike(String messageValue);

    @Transactional
    void deleteMessageById(Long messageId);

    List<Message> findAllByDirectChat(DirectChat directChat);

    List<Message> findAllBySenderIdAndReceiverId(Long senderId, Long receiverId);

    List<Message> findAllByDirectChatAndMessageValueLike(DirectChat directChat, String messageValue);

    List<Message> findAllByMessageValueLikeAndGroupChat(String messageValue, GroupChat groupChat);

    Message findFirstByDirectChatOrderByIdDesc(DirectChat directChat);

    Message findFirstByGroupChatOrderByIdDesc(GroupChat groupChat);
}
