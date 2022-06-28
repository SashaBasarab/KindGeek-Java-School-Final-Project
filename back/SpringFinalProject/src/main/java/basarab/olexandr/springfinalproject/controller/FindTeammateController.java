package basarab.olexandr.springfinalproject.controller;

import basarab.olexandr.springfinalproject.dto.response.UserResponseDTO;
import basarab.olexandr.springfinalproject.service.FindTeammateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/find-teammate")
public class FindTeammateController {

    @Autowired
    private FindTeammateService findTeammateService;

    @PutMapping("/ready-to-find-teammates/{userId}")
    public void setReady(@PathVariable Long userId,
                         @RequestParam String gameName) {
        log.info("Request: userId: {},\n gameName: {}", userId, gameName);
        findTeammateService.setReady(userId, gameName);
    }

    @GetMapping("/begin")
    public List<UserResponseDTO> findTeammates(@RequestParam Long userId,
                                               @RequestParam(required = false) Integer socialRating,
                                               @RequestParam(required = false) String nationality,
                                               @RequestParam(required = false) String motherTongue,
                                               @RequestParam(required = false) String gameName) {
        log.info("Request: userId: {},\n socialRating: {},\n nationality: {},\n motherTongue: {},\n gameName: {}",
                userId, socialRating, nationality, motherTongue, gameName);
        return findTeammateService.findTeammates(userId, socialRating, nationality, motherTongue, gameName);
    }

}
