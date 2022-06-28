package basarab.olexandr.springfinalproject.service;

import basarab.olexandr.springfinalproject.dto.response.PostResponseDTO;
import basarab.olexandr.springfinalproject.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.util.List;

public interface PostService {

    void createNewPost(Long userId, String description, MultipartFile multipartFile, List<MultipartFile> multipartFiles);

    void createNewPost(Long userId, String description);

    void createNewPost(Long userId, String description, MultipartFile multipartFile);

    void createNewPost(Long userId, String description, List<MultipartFile> multipartFiles);

    void updatePost(Long userId, Long postId, String description, MultipartFile multipartFile, List<MultipartFile> multipartFiles);

    void updatePost(Long userId, Long postId, String description);

    void updatePost(Long userId, Long postId, String description, MultipartFile multipartFile);

    void updatePost(Long userId, Long postId, String description, List<MultipartFile> multipartFiles);

    void deletePost(Long userId, Long postId);

    List<PostResponseDTO> getAllPosts(Long userId, int pageNumber, int pageSize);

}
