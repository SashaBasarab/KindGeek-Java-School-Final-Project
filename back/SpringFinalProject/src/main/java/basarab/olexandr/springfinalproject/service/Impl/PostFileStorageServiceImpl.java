package basarab.olexandr.springfinalproject.service.Impl;

import basarab.olexandr.springfinalproject.entity.Post;
import basarab.olexandr.springfinalproject.entity.PostImage;
import basarab.olexandr.springfinalproject.exceptions.CouldNotDeleteFileException;
import basarab.olexandr.springfinalproject.exceptions.CouldNotReadTheFileException;
import basarab.olexandr.springfinalproject.exceptions.CouldNotStoreFileException;
import basarab.olexandr.springfinalproject.exceptions.NoSuchPostException;
import basarab.olexandr.springfinalproject.repository.PostImageRepository;
import basarab.olexandr.springfinalproject.repository.PostRepository;
import basarab.olexandr.springfinalproject.service.PostFileStorageService;
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
public class PostFileStorageServiceImpl implements PostFileStorageService {

    private final Path root = Paths.get("D:\\SpringFileStorage");

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    @Override
    public void saveImg(MultipartFile multipartFile, Post post) {
        try {
            saveFile(multipartFile, post);
        } catch (IOException e) {
            throw new CouldNotStoreFileException("Could not store the file");
        }
    }

    private void saveFile(MultipartFile multipartFile, Post post) throws IOException {
        Path path = this.root.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        PostImage postImage = new PostImage();
        postImage.setImgUrl(path.toAbsolutePath().toString());
        postImageRepository.save(postImage);
        post.getPostImages().add(postImage);
        postRepository.save(post);
        postImage.setPost(post);
        postImageRepository.save(postImage);
    }

    @Override
    public void saveImgs(List<MultipartFile> multipartFiles, Post post) {
        try {
            for (MultipartFile file : multipartFiles) {
                saveFile(file, post);
            }
        } catch (IOException e) {
            throw new CouldNotStoreFileException("Could not store the file");
        }
    }

    @Override
    public void deleteImgs(Post post) {
        for (PostImage postImage : post.getPostImages()) {
            try {
                Path path = this.root.resolve(Objects.requireNonNull(postImage.getImgUrl()));
                postImageRepository.delete(postImage);
                Files.delete(path);
            } catch (IOException e) {
                throw new CouldNotDeleteFileException("Could not delete file");
            }
        }
        post.getPostImages().clear();
        postRepository.save(post);
    }

    @Override
    public Resource loadByPostId(Long postId, Integer imageIndex) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchPostException("Post is not found"));
        try {
            Path file = Paths.get(post.getPostImages().get(imageIndex).getImgUrl());
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
