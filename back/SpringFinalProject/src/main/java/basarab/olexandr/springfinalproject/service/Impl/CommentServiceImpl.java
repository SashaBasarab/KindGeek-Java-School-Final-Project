package basarab.olexandr.springfinalproject.service.Impl;

import basarab.olexandr.springfinalproject.dto.response.CommentResponseDTO;
import basarab.olexandr.springfinalproject.entity.Comment;
import basarab.olexandr.springfinalproject.entity.Post;
import basarab.olexandr.springfinalproject.exceptions.NoSuchCommentException;
import basarab.olexandr.springfinalproject.exceptions.NoSuchPostException;
import basarab.olexandr.springfinalproject.exceptions.UserNotFoundException;
import basarab.olexandr.springfinalproject.repository.CommentRepository;
import basarab.olexandr.springfinalproject.repository.PostRepository;
import basarab.olexandr.springfinalproject.repository.UserRepository;
import basarab.olexandr.springfinalproject.service.CommentFileStorageService;
import basarab.olexandr.springfinalproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentFileStorageService commentFileStorageService;

    @Override
    public void addComment(Long postId, String message, Long userId) {
        Comment comment = addCommentDuplicate(postId, message, userId);
        commentRepository.save(comment);
    }

    private Comment addCommentDuplicate(Long postId, String message, Long userId) {
        Comment comment = new Comment();
        comment.setMessage(message);
        comment.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " is not found")));
        comment.setCreationTime(LocalDateTime.now());
        comment.setPost(postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchPostException("Post with id " + postId + " is not found")));
        return comment;
    }

    @Override
    public void addComment(Long postId, String message, Long userId, MultipartFile multipartFile) {
        Comment comment = addCommentDuplicate(postId, message, userId);
        commentFileStorageService.saveImg(multipartFile, comment);
        commentRepository.save(comment);
    }

    @Override
    public void addComment(Long postId, String message, Long userId, List<MultipartFile> multipartFiles) {
        Comment comment = addCommentDuplicate(postId, message, userId);
        commentFileStorageService.saveImgs(multipartFiles, comment);
        commentRepository.save(comment);
    }

    @Override
    public void addComment(Long postId, String message, Long userId, MultipartFile multipartFile, List<MultipartFile> multipartFiles) {
        if (multipartFiles != null) {
            addComment(postId, message, userId, multipartFiles);
        } else if (multipartFile != null) {
            addComment(postId, message, userId, multipartFile);
        } else {
            addComment(postId, message, userId);
        }
    }

    @Override
    public void updateComment(Long commentId, String message) {
        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new NoSuchCommentException("Comment with id " + commentId + " is not found"));
        comment.setMessage(message);
        commentRepository.save(comment);
    }

    @Override
    public void updateComment(Long commentId, String message, MultipartFile multipartFile) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchCommentException("Comment with id " + commentId + " is not found"));
        comment.setMessage(message);
        commentFileStorageService.deleteImgs(comment);
        commentFileStorageService.saveImg(multipartFile, comment);
    }

    @Override
    public void updateComment(Long commentId, String message, List<MultipartFile> multipartFiles) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchCommentException("Comment with id " + commentId + " is not found"));
        comment.setMessage(message);
        commentFileStorageService.deleteImgs(comment);
        commentFileStorageService.saveImgs(multipartFiles, comment);
    }

    @Override
    public void updateComment(Long commentId, String message, MultipartFile multipartFile, List<MultipartFile> multipartFiles) {
        if (multipartFiles != null) {
            updateComment(commentId, message, multipartFiles);
        } else if (multipartFile != null) {
            updateComment(commentId, message, multipartFile);
        } else {
            updateComment(commentId, message);
        }
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchCommentException("Comment with id " + commentId + " is not found"));
        commentFileStorageService.deleteImgs(comment);
        commentRepository.delete(comment);
    }

    @Override
    public List<CommentResponseDTO> getAllCommentsUnderPost(Long postId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchPostException("No such post"));
        return commentRepository.findAllByPost(post, pageRequest).stream()
                .map(CommentResponseDTO::from)
                .collect(Collectors.toList());
    }
}
