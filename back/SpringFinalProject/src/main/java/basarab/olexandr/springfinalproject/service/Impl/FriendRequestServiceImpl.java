package basarab.olexandr.springfinalproject.service.Impl;

import basarab.olexandr.springfinalproject.dto.request.UserDTO;
import basarab.olexandr.springfinalproject.dto.response.UserResponseDTO;
import basarab.olexandr.springfinalproject.entity.FriendRequest;
import basarab.olexandr.springfinalproject.entity.User;
import basarab.olexandr.springfinalproject.exceptions.FriendshipAlreadyExistsException;
import basarab.olexandr.springfinalproject.exceptions.NoSuchFriendshipException;
import basarab.olexandr.springfinalproject.exceptions.UserNotFoundException;
import basarab.olexandr.springfinalproject.repository.FriendRequestRepository;
import basarab.olexandr.springfinalproject.repository.UserRepository;
import basarab.olexandr.springfinalproject.repository.spec.FriendRequestSpecification;
import basarab.olexandr.springfinalproject.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Map<String, Set<UserResponseDTO>> getAllFriends(Long userId) {
        List<FriendRequest> friendRequests = friendRequestRepository.findAllByUserSenderIdOrUserReceiverId(userId, userId);

        Map<Boolean, List<FriendRequest>> requestMap = friendRequests.stream()
                .collect(Collectors.partitioningBy(FriendRequest::getAccepted));

        Map<String, Set<UserResponseDTO>> allRequests = new HashMap<>();
        allRequests.put("UnacceptedRequests", notAcceptedRequestsToUser(requestMap, userId));
        allRequests.put("PendingRequests", notAcceptedRequestsByOtherUsers(requestMap, userId));
        allRequests.put("Friends", friends(requestMap, userId));
        return allRequests;
    }

    private Set<UserResponseDTO> notAcceptedRequestsToUser(Map<Boolean, List<FriendRequest>> requestMap, Long userId) {
        return requestMap.get(false).stream()
                .filter(friendRequest -> friendRequest.getUserReceiver().getId().equals(userId))
                .map(FriendRequest::getUserSender)
                .sorted(Comparator.comparing(User::getUsername))
                .map(UserResponseDTO::from)
                .collect(Collectors.toSet());
    }

    private Set<UserResponseDTO> notAcceptedRequestsByOtherUsers(Map<Boolean, List<FriendRequest>> requestMap, Long userId) {
        return requestMap.get(false).stream()
                .filter(friendRequest -> friendRequest.getUserSender().getId().equals(userId))
                .map(FriendRequest::getUserReceiver)
                .sorted(Comparator.comparing(User::getUsername))
                .map(UserResponseDTO::from)
                .collect(Collectors.toSet());
    }

    private Set<UserResponseDTO> friends(Map<Boolean, List<FriendRequest>> requestMap, Long userId) {
        return requestMap.get(true).stream()
                .map(friendRequest ->
                        friendRequest.getUserSender().getId().equals(userId) ? friendRequest.getUserReceiver() : friendRequest.getUserSender())
                .sorted(Comparator.comparing(User::getUsername))
                .map(UserResponseDTO::from)
                .collect(Collectors.toSet());
    }

    @Override
    public List<UserResponseDTO> findFriendByUsername(Long userId , String friendUsername, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return friendRequestRepository.findAllByFriendUsername(userId ,"%" + friendUsername + "%", pageRequest).stream()
                .map(friendRequest ->
                        friendRequest.getUserSender().getId().equals(userId) ? friendRequest.getUserReceiver() : friendRequest.getUserSender())
                .map(UserResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public void addFriendRequest(Long userId, Long friendId) {
        if (friendRequestRepository.findFriendRequestByUserSenderIdAndUserReceiverId(
                userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("User is not found")),
                userRepository.findById(friendId)
                        .orElseThrow(() -> new UserNotFoundException("User is not found"))
        ).isPresent()) {
            throw new FriendshipAlreadyExistsException("Friendship already exists");
        } else {
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setUserSender(userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User is not found")));
            friendRequest.setUserReceiver(userRepository.findById(friendId)
                    .orElseThrow(() -> new UserNotFoundException("User is not found")));
            friendRequest.setAccepted(false);
            friendRequestRepository.save(friendRequest);
        }
    }

    @Override
    public void acceptRequest(Long userId, Long friendId) {
        FriendRequest friendRequest = friendRequestRepository.findFriendRequestByReceiverIdAndSenderId(userId, friendId)
                .orElseThrow(() -> new NoSuchFriendshipException("Friend request is not found"));
        friendRequest.setAccepted(true);
        friendRequestRepository.save(friendRequest);
    }

    @Override
    public void denyRequest(Long userId, Long friendId) {
        FriendRequest friendRequest = friendRequestRepository.findFriendRequestByReceiverIdAndSenderId(userId, friendId)
                .orElseThrow(() -> new NoSuchFriendshipException("Friend request is not found"));
        friendRequestRepository.delete(friendRequest);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        FriendRequest friendRequest = friendRequestRepository.findFriendRequestByUserSenderIdAndUserReceiverId(
                userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " is not found")),
                userRepository.findById(friendId)
                        .orElseThrow(() -> new UserNotFoundException("Friend with id " + friendId + " is not found")))
                .orElseThrow(() -> new NoSuchFriendshipException("Friend request with users` ids " + userId + " and " + friendId + " is not found"));
        friendRequestRepository.delete(friendRequest);
    }

    @Override
    public List<UserResponseDTO> searchFriend(Long userId, String username, Integer socialRating, String nationality, String motherTongue) {
        FriendRequestSpecification friendRequestSpecification = new FriendRequestSpecification(username, socialRating, nationality, motherTongue);
        return friendRequestRepository.findAll(friendRequestSpecification).stream()
                .map(friendRequest ->
                        friendRequest.getUserSender().getId().equals(userId) ? friendRequest.getUserReceiver() : friendRequest.getUserSender())
                .map(UserResponseDTO::from)
                .collect(Collectors.toList());
    }
}
