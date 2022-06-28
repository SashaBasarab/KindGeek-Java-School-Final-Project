package basarab.olexandr.springfinalproject.repository;

import basarab.olexandr.springfinalproject.entity.DirectChat;
import basarab.olexandr.springfinalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectChatRepository extends JpaRepository<DirectChat, Long> {

    Optional<DirectChat> findById(Long id);

    Optional<DirectChat> findByUser1AndUser2(User user1, User user2);

    List<DirectChat> findAllByUser1OrUser2(User user1, User user2);

}
