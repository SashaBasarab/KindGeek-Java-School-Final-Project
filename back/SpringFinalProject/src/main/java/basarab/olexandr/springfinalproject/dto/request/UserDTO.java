package basarab.olexandr.springfinalproject.dto.request;

import basarab.olexandr.springfinalproject.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class UserDTO {

    private Long id;

    private String email;

    private String username;

    public static UserDTO from(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }

}
