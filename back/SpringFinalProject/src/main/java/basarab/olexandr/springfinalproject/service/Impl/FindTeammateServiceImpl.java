package basarab.olexandr.springfinalproject.service.Impl;

import basarab.olexandr.springfinalproject.dto.response.UserResponseDTO;
import basarab.olexandr.springfinalproject.entity.User;
import basarab.olexandr.springfinalproject.enums.Games;
import basarab.olexandr.springfinalproject.exceptions.GameNotFoundException;
import basarab.olexandr.springfinalproject.exceptions.UserNotFoundException;
import basarab.olexandr.springfinalproject.repository.FriendRequestRepository;
import basarab.olexandr.springfinalproject.repository.GameRepository;
import basarab.olexandr.springfinalproject.repository.UserRepository;
import basarab.olexandr.springfinalproject.repository.spec.FindTeammateSpecification;
import basarab.olexandr.springfinalproject.service.FindTeammateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FindTeammateServiceImpl implements FindTeammateService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Override
    public List<UserResponseDTO> findTeammates(Long userId, Integer socialRating, String nationality, String motherTongue, String gameName) {
        List<Long> userFriendsIds = friendRequestRepository.findAllByUserSenderIdOrUserReceiverId(userId, userId).stream()
                .map(directChat -> directChat.getUserSender().getId().equals(userId) ? directChat.getUserReceiver().getId() : directChat.getUserSender().getId())
                .collect(Collectors.toList());
        FindTeammateSpecification findTeammateSpecification = new FindTeammateSpecification(socialRating, nationality, motherTongue);
        User userRequest = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " is not found"));
        log.info(userRequest.getGame().getGameName().name());
        return userRepository.findAll(findTeammateSpecification).stream()
                .filter(user -> user.getGame().getGameName().name().toLowerCase().equals(userRequest.getGame().getGameName().name().toLowerCase()))
                .filter(user -> user.getId() != userId)
                .filter(user -> !userFriendsIds.contains(user.getId()))
                .map(UserResponseDTO::from)
                .sorted(Comparator.comparing(UserResponseDTO::getUsername))
                .collect(Collectors.toList());
    }

    @Override
    public void setReady(Long userId, String gameName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " is not found"));
        user.setIsLookingForMatch(true);
        switch (gameName) {
            case "dota2" -> user.setGame(gameRepository.findByGameName(Games.DOTA2)
                    .orElseThrow(() -> new GameNotFoundException("Game is not found")));
            case "cs go" -> user.setGame(gameRepository.findByGameName(Games.CS_GO)
                    .orElseThrow(() -> new GameNotFoundException("Game is not found")));
            case "witcher3" -> user.setGame(gameRepository.findByGameName(Games.WITCHER3)
                    .orElseThrow(() -> new GameNotFoundException("Game is not found")));
            case "detroit" -> user.setGame(gameRepository.findByGameName(Games.DETROIT)
                    .orElseThrow(() -> new GameNotFoundException("Game is not found")));
            case "it takes two" -> user.setGame(gameRepository.findByGameName(Games.IT_TAKES_TWO)
                    .orElseThrow(() -> new GameNotFoundException("Game is not found")));
            case "cyberpunk2077" -> user.setGame(gameRepository.findByGameName(Games.CYBERPUNK2077)
                    .orElseThrow(() -> new GameNotFoundException("Game is not found")));
            default -> throw new GameNotFoundException("Game " + gameName + " is not found");
        }

        userRepository.save(user);
    }
}
