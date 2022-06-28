package basarab.olexandr.springfinalproject.service;

import basarab.olexandr.springfinalproject.dto.request.UserDTO;
import basarab.olexandr.springfinalproject.dto.response.UserResponseDTO;
import basarab.olexandr.springfinalproject.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FriendRequestService {

    Map<String, Set<UserResponseDTO>> getAllFriends(Long id);

    List<UserResponseDTO> findFriendByUsername(Long userId , String friendUsername, int pageNumber, int pageSize);

    void addFriendRequest(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    void acceptRequest(Long userId, Long friendId);

    void denyRequest(Long userId, Long friendId);

    List<UserResponseDTO> searchFriend(Long userId, String username, Integer socialRating, String nationality, String motherTongue);

}
