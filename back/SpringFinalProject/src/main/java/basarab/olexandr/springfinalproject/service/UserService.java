package basarab.olexandr.springfinalproject.service;

import basarab.olexandr.springfinalproject.dto.request.FindUserRequest;
import basarab.olexandr.springfinalproject.dto.request.LoginRequest;
import basarab.olexandr.springfinalproject.dto.request.UserSignInRequest;
import basarab.olexandr.springfinalproject.dto.request.UserSignUpRequest;
import basarab.olexandr.springfinalproject.dto.response.JwtResponse;
import basarab.olexandr.springfinalproject.dto.response.UserResponseDTO;
import basarab.olexandr.springfinalproject.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface UserService {

    void updateAvatarUrl(Long userId, MultipartFile multipartFile);

    void updateUserInfo(Long userId, String username, List<String> strInterests, MultipartFile multipartFile);

    List<UserResponseDTO> searchUser(Long userId, String username, Integer socialRating, String nationality, String motherTongue, Set<String> strInterests);

    UserResponseDTO findUserById(Long userId);

}
