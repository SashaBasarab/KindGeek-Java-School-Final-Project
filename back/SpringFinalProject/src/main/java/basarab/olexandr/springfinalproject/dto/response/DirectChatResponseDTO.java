package basarab.olexandr.springfinalproject.dto.response;

import basarab.olexandr.springfinalproject.entity.DirectChat;
import basarab.olexandr.springfinalproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class DirectChatResponseDTO {

    private Long id;

    private User user1;

    private User user2;

    public static DirectChatResponseDTO from(DirectChat directChat) {
        return new DirectChatResponseDTO(
                directChat.getId(),
                directChat.getUser1(),
                directChat.getUser2()
        );
    }
}
