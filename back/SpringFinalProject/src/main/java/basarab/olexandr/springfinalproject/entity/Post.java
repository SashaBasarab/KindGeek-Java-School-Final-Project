package basarab.olexandr.springfinalproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private LocalDateTime creationDate;

    private String description;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private List<PostImage> postImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner_of_post")
    private User owner;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

}
