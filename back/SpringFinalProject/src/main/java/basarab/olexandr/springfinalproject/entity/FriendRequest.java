package basarab.olexandr.springfinalproject.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@EqualsAndHashCode
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userSender")
    private User userSender;

    @ManyToOne
    @JoinColumn(name = "userReceiver")
    private User userReceiver;

    private Boolean accepted;

}
