package basarab.olexandr.springfinalproject.repository;

import basarab.olexandr.springfinalproject.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
