package basarab.olexandr.springfinalproject.service;

import basarab.olexandr.springfinalproject.dto.response.CommentResponseDTO;
import basarab.olexandr.springfinalproject.entity.Comment;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.util.List;

public interface CommentService {

    void addComment(Long postId, String message, Long userId, MultipartFile multipartFile, List<MultipartFile> multipartFiles);

    void addComment(Long postId, String message, Long userId);

    void addComment(Long postId, String message, Long userId, MultipartFile multipartFile);

    void addComment(Long postId, String message, Long userId, List<MultipartFile> multipartFiles);

    void updateComment(Long postId, String message, MultipartFile multipartFile, List<MultipartFile> multipartFiles);

    void updateComment(Long postId, String message);

    void updateComment(Long postId, String message, MultipartFile multipartFile);

    void updateComment(Long postId, String message, List<MultipartFile> multipartFiles);

    void deleteComment(Long commentId);

    List<CommentResponseDTO> getAllCommentsUnderPost(Long postId, int pageNumber, int pageSize);

}
