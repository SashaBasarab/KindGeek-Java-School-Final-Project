package basarab.olexandr.springfinalproject.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class SignupRequest {

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Password is required")
    @Size(min = 8, max = 16)
    private String password;

    @NotNull(message = "Email is required")
    @Email
    private String email;

    @NotNull(message = "Nationality is required")
    private String nationality;

    @NotNull(message = "Nationality is required")
    private String motherTongue;

    @NotNull(message = "Interests are required")
    private Set<String> interests = new HashSet<>();

}
