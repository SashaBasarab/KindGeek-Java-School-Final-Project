package basarab.olexandr.springfinalproject.repository;

import basarab.olexandr.springfinalproject.entity.Post;
import basarab.olexandr.springfinalproject.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndOwner(Long id, User owner);

    List<Post> findAllByOwner(User owner, Pageable pageable);

}
