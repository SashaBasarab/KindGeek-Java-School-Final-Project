package basarab.olexandr.springfinalproject.controller;

import basarab.olexandr.springfinalproject.dto.response.CommentResponseDTO;
import basarab.olexandr.springfinalproject.entity.Comment;
import basarab.olexandr.springfinalproject.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/post/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add-new-comment/{postId}")
    public void addComment(@PathVariable Long postId,
                           @RequestParam String message,
                           @RequestParam Long userId,
                           @RequestParam(required = false)List<MultipartFile> multipartFiles,
                           @RequestParam(required = false) MultipartFile multipartFile) {
        log.info("Request: postId: {},\n message: {},\n userId: {},\n  multipartFile: {},\n  multipartFiles: {}", postId, message, userId, multipartFile, multipartFiles);
        commentService.addComment(postId, message, userId, multipartFile, multipartFiles);
    }

    @PutMapping("/update-comment/{commentId}")
    public void updateComment(@PathVariable Long commentId,
                              @RequestParam String message,
                              @RequestParam(required = false) List<MultipartFile> multipartFiles,
                              @RequestParam(required = false) MultipartFile multipartFile) {
        log.info("Request: commentId: {},\n message: {}\n, multipartFile: {}\n, multipartFiles: {}", commentId, message, multipartFile, multipartFiles);
        commentService.updateComment(commentId, message, multipartFile, multipartFiles);
    }

    @DeleteMapping("/delete-comment/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        log.info("Request: commentId: {}", commentId);
        commentService.deleteComment(commentId);
    }

    @GetMapping("/get-all-comments/{postId}")
    public List<CommentResponseDTO> getAllCommentsUnderPost(@PathVariable Long postId,
                                                            @RequestParam int pageNumber,
                                                            @RequestParam int pageSize) {
        log.info("Request: postId: {}\n, pageNumber: {}\n, pageSize: {}", postId, pageNumber, pageSize);
        return commentService.getAllCommentsUnderPost(postId, pageNumber, pageSize);
    }

}
