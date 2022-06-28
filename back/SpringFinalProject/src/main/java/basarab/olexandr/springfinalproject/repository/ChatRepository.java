package basarab.olexandr.springfinalproject.repository;

import basarab.olexandr.springfinalproject.entity.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<GroupChat, Long> {
}
