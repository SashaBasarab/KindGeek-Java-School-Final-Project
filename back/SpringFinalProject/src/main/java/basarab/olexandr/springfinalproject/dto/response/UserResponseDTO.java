package basarab.olexandr.springfinalproject.dto.response;

import basarab.olexandr.springfinalproject.entity.User;
import basarab.olexandr.springfinalproject.entity.UserInterest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;

    private String email;

    private String username;

    private String userAvatarUrl;

    private String nationality;

    private String motherTongue;

    private Set<String> interests = new HashSet<>();

    public static UserResponseDTO from(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getUserAvatarUrl(),
                user.getNationality(),
                user.getMotherTongue(),
                user.getInterests().stream()
                        .map(userInterest -> userInterest.getInterest().name())
                        .collect(Collectors.toSet())
        );
    }

}
