package basarab.olexandr.springfinalproject.dto.response;

import basarab.olexandr.springfinalproject.entity.Comment;
import basarab.olexandr.springfinalproject.entity.CommentImage;
import basarab.olexandr.springfinalproject.entity.Post;
import basarab.olexandr.springfinalproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@AllArgsConstructor
public class CommentResponseDTO {

    private Long id;

    private Long postId;

    private LocalDateTime creationTime;

    private String message;

    private Long ownerId;

    private List<Long> commentImagesId = new ArrayList<>();

    public static CommentResponseDTO from(Comment comment) {
        if (comment == null) {
            return null;
        }

        return new CommentResponseDTO(
                comment.getId(),
                comment.getPost().getId(),
                comment.getCreationTime(),
                comment.getMessage(),
                comment.getOwner().getId(),
                comment.getCommentImages().stream()
                        .map(CommentImage::getId)
                        .collect(Collectors.toList())
        );
    }

}
