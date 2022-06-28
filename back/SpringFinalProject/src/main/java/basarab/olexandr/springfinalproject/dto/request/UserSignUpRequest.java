package basarab.olexandr.springfinalproject.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
public class UserSignUpRequest {

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Email is required")
    @Email(message = "Wrong email")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 4, max = 10)
    private String password;

    @NotNull(message = "Nationality is required")
    private String nationality;

    @NotNull(message = "Mother tongue is required")
    private String motherTongue;

    @NotNull(message = "Interests are required")
    private Set<String> interests = new HashSet<>();

}
