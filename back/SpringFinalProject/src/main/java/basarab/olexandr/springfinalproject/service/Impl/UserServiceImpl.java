package basarab.olexandr.springfinalproject.service.Impl;

import basarab.olexandr.springfinalproject.dto.request.FindUserRequest;
import basarab.olexandr.springfinalproject.dto.request.LoginRequest;
import basarab.olexandr.springfinalproject.dto.response.JwtResponse;
import basarab.olexandr.springfinalproject.dto.response.UserResponseDTO;
import basarab.olexandr.springfinalproject.entity.DirectChat;
import basarab.olexandr.springfinalproject.entity.Game;
import basarab.olexandr.springfinalproject.entity.User;
import basarab.olexandr.springfinalproject.entity.UserInterest;
import basarab.olexandr.springfinalproject.enums.Games;
import basarab.olexandr.springfinalproject.enums.Interests;
import basarab.olexandr.springfinalproject.exceptions.GameNotFoundException;
import basarab.olexandr.springfinalproject.exceptions.InterestNotFoundException;
import basarab.olexandr.springfinalproject.exceptions.UserNotFoundException;
import basarab.olexandr.springfinalproject.repository.*;
import basarab.olexandr.springfinalproject.repository.spec.UserSpecification;
import basarab.olexandr.springfinalproject.service.AuthService;
import basarab.olexandr.springfinalproject.service.UserAvatarFilesStorageService;
import basarab.olexandr.springfinalproject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInterestRepository userInterestRepository;

    @Autowired
    private UserAvatarFilesStorageService userAvatarFilesStorageService;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Override
    public void updateAvatarUrl(Long userId, MultipartFile multipartFile) {
        if (userRepository.getById(userId).getUserAvatarUrl() != null) {
            userAvatarFilesStorageService.deleteImg(userId);
            userAvatarFilesStorageService.saveImg(multipartFile, userId);
        } else {
            userAvatarFilesStorageService.saveImg(multipartFile, userId);
        }
    }

    @Override
    public void updateUserInfo(Long userId, String username, List<String> strInterests, MultipartFile multipartFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " is not found"));
        if (!StringUtils.isBlank(username)) {
            user.setUsername(username);
            userRepository.save(user);
            log.info(username);
        }
        if (strInterests != null) {
            user.getInterests().clear();
            userRepository.save(user);
            strInterests.forEach(strInterest -> {
                switch (strInterest) {
                    case "anime" -> {
                        UserInterest anime = userInterestRepository.findByInterest(Interests.ANIME)
                                .orElseThrow(() -> new InterestNotFoundException("Interest " + strInterest + " is not found"));
                        user.getInterests().add(anime);
                    }
                    case "computer games" -> {
                        UserInterest computerGames = userInterestRepository.findByInterest(Interests.COMPUTER_GAMES)
                                .orElseThrow(() -> new InterestNotFoundException("Interest " + strInterest + " is not found"));
                        user.getInterests().add(computerGames);
                    }
                    case "football" -> {
                        UserInterest football = userInterestRepository.findByInterest(Interests.FOOTBALL)
                                .orElseThrow(() -> new InterestNotFoundException("Interest " + strInterest + " is not found"));
                        user.getInterests().add(football);
                    }
                    case "basketball" -> {
                        UserInterest basketball = userInterestRepository.findByInterest(Interests.BASKETBALL)
                                .orElseThrow(() -> new InterestNotFoundException("Interest " + strInterest + " is not found"));
                        user.getInterests().add(basketball);
                    }
                    case "tennis" -> {
                        UserInterest tennis = userInterestRepository.findByInterest(Interests.TENNIS)
                                .orElseThrow(() -> new InterestNotFoundException("Interest " + strInterest + " is not found"));
                        user.getInterests().add(tennis);
                    }
                    default -> {
                        UserInterest nothing = userInterestRepository.findByInterest(Interests.NOTHING)
                                .orElseThrow(() -> new InterestNotFoundException("Interest is not found"));
                        user.getInterests().add(nothing);
                    }
                }
            });
            userRepository.save(user);
        }
        if (multipartFile != null) {
            updateAvatarUrl(userId, multipartFile);
            userRepository.save(user);
        }
    }

    @Override
    public List<UserResponseDTO> searchUser(Long userId, String username, Integer socialRating, String nationality,
                                            String motherTongue, Set<String> strInterests) {
        List<Long> userFriendsIds = friendRequestRepository.findAllByUserSenderIdOrUserReceiverId(userId, userId).stream()
                .map(directChat -> directChat.getUserSender().getId().equals(userId) ? directChat.getUserReceiver().getId() : directChat.getUserSender().getId())
                .collect(Collectors.toList());
        log.info(String.valueOf(userFriendsIds));
        UserSpecification userSpecification = new UserSpecification(username, socialRating, nationality, motherTongue, strInterests);
        return userRepository.findAll(userSpecification).stream()
                .map(UserResponseDTO::from)
                .filter(userResponseDTO -> userResponseDTO.getId() != userId)
                .filter(userResponseDTO -> !userFriendsIds.contains(userResponseDTO.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO findUserById(Long userId) {
        return UserResponseDTO.from(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " is not found")));
    }
}
