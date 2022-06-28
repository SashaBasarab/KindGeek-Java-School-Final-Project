package basarab.olexandr.springfinalproject.repository;

import basarab.olexandr.springfinalproject.entity.CommentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentImageRepository extends JpaRepository<CommentImage, Long> {
}
