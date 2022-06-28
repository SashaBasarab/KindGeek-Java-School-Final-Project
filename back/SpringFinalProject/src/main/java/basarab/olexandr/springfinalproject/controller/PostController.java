package basarab.olexandr.springfinalproject.controller;

import basarab.olexandr.springfinalproject.dto.response.PostResponseDTO;
import basarab.olexandr.springfinalproject.entity.Post;
import basarab.olexandr.springfinalproject.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/create-new-post")
    public void createNewPost(@RequestParam Long userId,
                              @RequestParam String description,
                              @RequestParam(required = false) List<MultipartFile> multipartFiles,
                              @RequestParam(required = false) MultipartFile multipartFile) {
        log.info("Request: userId: {},\n description: {},\n multipartFile: {},\n multipartFiles: {}",
                userId, description, multipartFile, multipartFiles);
        postService.createNewPost(userId, description, multipartFile, multipartFiles);
    }

    @PutMapping("/update-post")
    public void updatePost(@RequestParam Long userId,
                           @RequestParam Long postId,
                           @RequestParam String description,
                           @RequestParam(required = false) List<MultipartFile> multipartFiles,
                           @RequestParam(required = false) MultipartFile multipartFile) {
        log.info("Request: userId: {},\n postId: {},\n description: {},\n multipartFile: {},\n multipartFiles: {}",
                userId, postId, description, multipartFile, multipartFiles);
        postService.updatePost(userId, postId, description, multipartFile ,multipartFiles);
    }

    @DeleteMapping("/delete-post")
    public void deletePost(@RequestParam Long userId,
                           @RequestParam Long postId) {
        log.info("Request: userId: {},\n postId: {}", userId, postId);
        postService.deletePost(userId, postId);
    }

    @GetMapping("/get-all-posts")
    public List<PostResponseDTO> getAllPosts(@RequestParam Long userId,
                                             @RequestParam int pageNumber,
                                             @RequestParam int pageSize) {
        log.info("Request: userId: {},\n pageNumber: {},\n pageSize: {}", userId, pageNumber, pageSize);
        return postService.getAllPosts(userId, pageNumber, pageSize);
    }

}
