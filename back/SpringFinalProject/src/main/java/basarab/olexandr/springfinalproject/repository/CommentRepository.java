package basarab.olexandr.springfinalproject.repository;

import basarab.olexandr.springfinalproject.entity.Comment;
import basarab.olexandr.springfinalproject.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post, Pageable pageable);

}
