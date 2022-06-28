package basarab.olexandr.springfinalproject.dto.response;

import basarab.olexandr.springfinalproject.entity.Comment;
import basarab.olexandr.springfinalproject.entity.Post;
import basarab.olexandr.springfinalproject.entity.PostImage;
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
public class PostResponseDTO {

    private Long id;

    private LocalDateTime creationDate;

    private String description;

    private List<String> postImagesUrls = new ArrayList<>();

    private Long ownerId;

    private List<Long> commentsId = new ArrayList<>();

    public static PostResponseDTO from(Post post) {
        if (post == null) {
            return null;
        }

        return new PostResponseDTO(
                post.getId(),
                post.getCreationDate(),
                post.getDescription(),
                post.getPostImages().stream()
                        .map(PostImage::getImgUrl)
                        .collect(Collectors.toList()),
                post.getOwner().getId(),
                post.getComments().stream()
                        .map(Comment::getId)
                        .collect(Collectors.toList())
        );
    }

}
