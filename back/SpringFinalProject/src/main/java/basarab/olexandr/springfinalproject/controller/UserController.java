package basarab.olexandr.springfinalproject.controller;

import basarab.olexandr.springfinalproject.dto.request.FindUserRequest;
import basarab.olexandr.springfinalproject.dto.request.LoginRequest;
import basarab.olexandr.springfinalproject.dto.response.JwtResponse;
import basarab.olexandr.springfinalproject.dto.response.UserResponseDTO;
import basarab.olexandr.springfinalproject.repository.UserRepository;
import basarab.olexandr.springfinalproject.service.AuthService;
import basarab.olexandr.springfinalproject.service.UserAvatarFilesStorageService;
import basarab.olexandr.springfinalproject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAvatarFilesStorageService userAvatarFilesStorageService;

    @GetMapping("/find-user")
    public List<UserResponseDTO> searchUser(@RequestParam Long userId,
                                            @RequestParam(required = false) String username,
                                            @RequestParam(required = false) Integer socialRating,
                                            @RequestParam(required = false) String nationality,
                                            @RequestParam(required = false) String motherTongue,
                                            @RequestParam(required = false) Set<String> strInterests) {
        log.info("Request: userId: {},\n username: {},\n socialRating: {},\n nationality: {},\n motherTongue: {},\n strInterests: {}",
                userId, username, socialRating, nationality, motherTongue, strInterests);
        return userService.searchUser(userId, username, socialRating, nationality, motherTongue, strInterests);
    }

    @GetMapping("/find-user-by-id/{userId}")
    public UserResponseDTO findUserById(@PathVariable Long userId) {
        log.info("Request: userId: {}", userId);
        return userService.findUserById(userId);
    }

    @PutMapping("/update-user-info/{userId}")
    public void updateUserInfo(@PathVariable Long userId,
                                      @RequestParam(required = false) String username,
                                      @RequestParam(required = false) MultipartFile multipartFile,
                                      @RequestBody(required = false) List<String> strInterests ) {
        log.info("Request: userId:{},\n username: {},\n multipartFile: {},\n strInterests: {}",
                userId, username, multipartFile, strInterests);
        userService.updateUserInfo(userId, username, strInterests, multipartFile);
    }

    @PostMapping("/{userId}/add-profile-icon")
    public void saveProfileIcon(@RequestParam MultipartFile multipartFile,
                                @PathVariable Long userId) {
        log.info("Request: multipartFile: {},\n userId: {}", multipartFile, userId);
        userAvatarFilesStorageService.saveImg(multipartFile, userId);
    }

    @PutMapping("/{userId}/update-profile-icon")
    public void updateProfileIcon(@RequestParam MultipartFile multipartFile,
                                  @PathVariable Long userId) {
        log.info("Request: multipartFile: {},\n userId: {}", multipartFile, userId);
        userService.updateAvatarUrl(userId, multipartFile);
    }

    @GetMapping("/{userId}/get-profile-icon")
    public Resource loadByPersonId(@PathVariable Long userId) {
        log.info("Request: userId: {}", userId);
        return userAvatarFilesStorageService.loadByPersonId(userId);
    }

}
