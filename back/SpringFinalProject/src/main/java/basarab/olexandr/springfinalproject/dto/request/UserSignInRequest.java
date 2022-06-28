package basarab.olexandr.springfinalproject.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSignInRequest {

    private String email;

    private String password;

}
