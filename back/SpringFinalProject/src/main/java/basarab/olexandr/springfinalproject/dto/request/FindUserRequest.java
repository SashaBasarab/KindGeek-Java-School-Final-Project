package basarab.olexandr.springfinalproject.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
public class FindUserRequest {

    private String username;

    private String nationality;

    private String motherTongue;

    private Integer socialRating;

    private Set<String> interests = new HashSet<>();

}
