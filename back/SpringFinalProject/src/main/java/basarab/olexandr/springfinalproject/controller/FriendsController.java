package basarab.olexandr.springfinalproject.controller;

import basarab.olexandr.springfinalproject.dto.request.UserDTO;
import basarab.olexandr.springfinalproject.dto.response.UserResponseDTO;
import basarab.olexandr.springfinalproject.entity.User;
import basarab.olexandr.springfinalproject.service.FriendRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/user/friends")
public class FriendsController {

    @Autowired
    private FriendRequestService friendRequestService;

    @GetMapping("/get-friends/{userId}")
    public Map<String, Set<UserResponseDTO>> getAllFriends(@PathVariable Long userId) {
        log.info("Request: userId: {}", userId);
        return friendRequestService.getAllFriends(userId);
    }

    @GetMapping("/find-by-username")
    public List<UserResponseDTO> findFriendByUsername(@RequestParam Long userId,
                                              @RequestParam String friendUsername,
                                              @RequestParam int pageNumber,
                                              @RequestParam int pageSize) {
        log.info("Request: userId: {},\n friendUsername: {},\n pageNumber: {},\n pageSize: {}",
                userId, friendUsername, pageNumber, pageSize);
        return friendRequestService.findFriendByUsername(userId, friendUsername, pageNumber, pageSize);
    }

    @PostMapping("/add-new-friend")
    public void addFriendRequest(@RequestParam Long userId,
                                 @RequestParam Long friendId) {
        log.info("Request: userId: {},\n friendId: {}", userId, friendId);
        friendRequestService.addFriendRequest(userId, friendId);
    }

    @PutMapping("/accept-request")
    public void acceptRequest(@RequestParam Long userId,
                              @RequestParam Long friendId) {
        log.info("Request: userId: {},\n friendId: {}", userId, friendId);
        friendRequestService.acceptRequest(userId, friendId);
    }

    @DeleteMapping("/deny-request")
    public void denyRequest(@RequestParam Long userId,
                            @RequestParam Long friendId) {
        log.info("Request: userId: {},\n friendId: {}", userId, friendId);
        friendRequestService.denyRequest(userId, friendId);
    }

    @DeleteMapping("/delete-friend")
    public void deleteFriend(@RequestParam Long userId,
                             @RequestParam Long friendId) {
        log.info("Request: userId: {},\n friendId: {}", userId, friendId);
        friendRequestService.deleteFriend(userId, friendId);
    }

    @GetMapping("/search-friend")
    public List<UserResponseDTO> searchFriend(@RequestParam(required = false) Long userId,
                                              @RequestParam(required = false) String username,
                                              @RequestParam(required = false) Integer socialRating,
                                              @RequestParam(required = false) String nationality,
                                              @RequestParam(required = false) String motherTongue) {
        log.info("Request: userId: {},\n username: {},\n socialRating: {},\n nationality: {},\n motherTongue: {}",
                userId, username, socialRating, nationality, motherTongue);
        return friendRequestService.searchFriend(userId, username, socialRating, nationality, motherTongue);
    }

}
