package basarab.olexandr.springfinalproject.service.Impl;

import basarab.olexandr.springfinalproject.entity.User;
import basarab.olexandr.springfinalproject.exceptions.UserNotFoundException;
import basarab.olexandr.springfinalproject.repository.UserRepository;
import basarab.olexandr.springfinalproject.service.UserAvatarFilesStorageService;
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
import java.util.Objects;

@Service
public class UserAvatarFilesStorageServiceImpl implements UserAvatarFilesStorageService {

    private final Path root = Paths.get("D:\\SpringFileStorage");

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveImg(MultipartFile multipartFile, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found"));
        try {
            Path path = this.root.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            user.setUserAvatarUrl(path.toAbsolutePath().toString());
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        userRepository.save(user);
    }

    @Override
    public void deleteImg(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found"));
        try {
            Path path = this.root.resolve(Objects.requireNonNull(user.getUserAvatarUrl()));
            Files.delete(path);
            user.setUserAvatarUrl(null);
            userRepository.save(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resource loadByPersonId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User is not found"));
        try {
            Path file = Paths.get(user.getUserAvatarUrl());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
