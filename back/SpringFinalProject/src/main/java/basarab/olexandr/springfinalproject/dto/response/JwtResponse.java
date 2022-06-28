package basarab.olexandr.springfinalproject.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {

    private Long id;

    private String token;

    private String username;

    private String email;

    private List<String> roles;

}
