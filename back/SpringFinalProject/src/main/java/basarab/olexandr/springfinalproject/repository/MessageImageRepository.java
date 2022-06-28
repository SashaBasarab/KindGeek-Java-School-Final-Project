package basarab.olexandr.springfinalproject.repository;

import basarab.olexandr.springfinalproject.entity.MessageImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageImageRepository extends JpaRepository<MessageImage, Long> {
}
