package basarab.olexandr.springfinalproject.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDateTime creationTime;

    private String message;

    @ManyToOne
    @JoinColumn(name = "owner_of_comment")
    private User owner;

    @OneToMany(mappedBy = "comment")
    private List<CommentImage> commentImages = new ArrayList<>();
}
