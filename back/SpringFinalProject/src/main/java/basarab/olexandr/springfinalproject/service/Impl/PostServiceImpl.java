package basarab.olexandr.springfinalproject.service.Impl;

import basarab.olexandr.springfinalproject.dto.response.PostResponseDTO;
import basarab.olexandr.springfinalproject.entity.Post;
import basarab.olexandr.springfinalproject.exceptions.NoSuchPostException;
import basarab.olexandr.springfinalproject.exceptions.UserNotFoundException;
import basarab.olexandr.springfinalproject.repository.PostRepository;
import basarab.olexandr.springfinalproject.repository.UserRepository;
import basarab.olexandr.springfinalproject.service.PostFileStorageService;
import basarab.olexandr.springfinalproject.service.PostService;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostFileStorageService postFileStorageService;

    private Post createNewPostDuplicate(Long userId, String description) {
        Post post = new Post();
        post.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " is not found")));
        post.setDescription(description);
        post.setCreationDate(LocalDateTime.now());
        return post;
    }

    @Override
    public void createNewPost(Long userId, String description) {
        Post post = createNewPostDuplicate(userId, description);
        postRepository.save(post);
    }

    @Override
    public void createNewPost(Long userId, String description, MultipartFile multipartFile) {
        Post post = createNewPostDuplicate(userId, description);
        postFileStorageService.saveImg(multipartFile, post);
        postRepository.save(post);
    }

    @Override
    public void createNewPost(Long userId, String description, List<MultipartFile> multipartFiles) {
        Post post = createNewPostDuplicate(userId, description);
        postFileStorageService.saveImgs(multipartFiles, post);
        postRepository.save(post);
    }

    @Override
    public void createNewPost(Long userId, String description, MultipartFile multipartFile, List<MultipartFile> multipartFiles) {
        if (multipartFiles != null) {
            createNewPost(userId, description, multipartFiles);
        } else if (multipartFile != null) {
            createNewPost(userId, description, multipartFile);
        } else {
            createNewPost(userId, description);
        }
    }

    private Post findPostByIdAndOwner(Long userId, Long postId) {
        return postRepository.findByIdAndOwner(
                        postId,
                        userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " is not found"))
                )
                .orElseThrow(() -> new NoSuchPostException("Post with id " + postId + " and owner id " + userId + " is not found" ));
    }

    @Override
    public void updatePost(Long userId, Long postId, String description) {
        Post post = findPostByIdAndOwner(userId, postId);
        post.setDescription(description);
        postRepository.save(post);
    }

    @Override
    public void updatePost(Long userId, Long postId, String description, MultipartFile multipartFile) {
        Post post = findPostByIdAndOwner(userId, postId);
        post.setDescription(description);
        postFileStorageService.deleteImgs(post);
        postFileStorageService.saveImg(multipartFile, post);
    }

    @Override
    public void updatePost(Long userId, Long postId, String description, List<MultipartFile> multipartFiles) {
        Post post = findPostByIdAndOwner(userId, postId);
        post.setDescription(description);
        postFileStorageService.deleteImgs(post);
        postFileStorageService.saveImgs(multipartFiles, post);
    }

    @Override
    public void updatePost(Long userId, Long postId, String description, MultipartFile multipartFile, List<MultipartFile> multipartFiles) {
        if (multipartFiles != null) {
            updatePost(userId, postId, description, multipartFiles);
        } else if (multipartFile != null) {
            updatePost(userId, postId, description, multipartFile);
        }else {
            updatePost(userId, postId, description);
        }
    }

    @Override
    public void deletePost(Long userId, Long postId) {
        Post post = findPostByIdAndOwner(userId, postId);
        postFileStorageService.deleteImgs(post);
        postRepository.delete(post);
    }

    @Override
    public List<PostResponseDTO> getAllPosts(Long userId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return postRepository.findAllByOwner(userRepository.findById(userId)
                        .orElseThrow(() -> new NoSuchPostException("No such post")), pageRequest).stream()
                .sorted(Comparator.comparing(Post::getCreationDate))
                .map(PostResponseDTO::from)
                .collect(Collectors.toList());
    }

}
