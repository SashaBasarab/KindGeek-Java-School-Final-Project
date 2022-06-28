package basarab.olexandr.springfinalproject.service.Impl;

import basarab.olexandr.springfinalproject.entity.Comment;
import basarab.olexandr.springfinalproject.entity.CommentImage;
import basarab.olexandr.springfinalproject.exceptions.CouldNotDeleteFileException;
import basarab.olexandr.springfinalproject.exceptions.CouldNotReadTheFileException;
import basarab.olexandr.springfinalproject.exceptions.CouldNotStoreFileException;
import basarab.olexandr.springfinalproject.exceptions.NoSuchCommentException;
import basarab.olexandr.springfinalproject.repository.CommentImageRepository;
import basarab.olexandr.springfinalproject.repository.CommentRepository;
import basarab.olexandr.springfinalproject.service.CommentFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class CommentFileStorageServiceImpl implements CommentFileStorageService {

    private final Path root = Paths.get("D:\\SpringFileStorage");

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentImageRepository commentImageRepository;

    @Override
    public void saveImg(MultipartFile multipartFile, Comment comment) {
        try {
            saveFile(multipartFile, comment);
        } catch (IOException e) {
            throw new CouldNotStoreFileException("Could not store the file");
        }
    }

    private void saveFile(MultipartFile multipartFile, Comment comment) throws IOException {
        Path path = this.root.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        CommentImage commentImage = new CommentImage();
        commentImage.setImgUrl(path.toAbsolutePath().toString());
        commentImageRepository.save(commentImage);
        comment.getCommentImages().add(commentImage);
        commentRepository.save(comment);
        commentImage.setComment(comment);
        commentImageRepository.save(commentImage);
    }

    @Override
    public void saveImgs(List<MultipartFile> multipartFiles, Comment comment) {
        try {
            for (MultipartFile file: multipartFiles) {
                saveFile(file, comment);
            }
        } catch (IOException e) {
            throw new CouldNotStoreFileException("Could not store the file");
        }
    }

    @Override
    public void deleteImgs(Comment comment) {
        for (CommentImage commentImage : comment.getCommentImages()) {
            try {
                Path path = this.root.resolve(Objects.requireNonNull(commentImage.getImgUrl()));
                commentImageRepository.delete(commentImage);
                Files.delete(path);
            } catch (IOException e) {
                throw new CouldNotDeleteFileException("Could not delete file");
            }
        }
        comment.getCommentImages().clear();
        commentRepository.save(comment);
    }

    @Override
    public Resource loadByCommentId(Long commentId, Integer imageIndex) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchCommentException("Comment not found"));
        try {
            Path file = Paths.get(comment.getCommentImages().get(imageIndex).getImgUrl());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new CouldNotReadTheFileException("Could not read the file");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
