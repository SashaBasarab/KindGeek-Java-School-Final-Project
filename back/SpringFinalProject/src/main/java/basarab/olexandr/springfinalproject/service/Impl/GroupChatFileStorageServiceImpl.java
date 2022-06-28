package basarab.olexandr.springfinalproject.service.Impl;

import basarab.olexandr.springfinalproject.entity.GroupChat;
import basarab.olexandr.springfinalproject.exceptions.CouldNotDeleteFileException;
import basarab.olexandr.springfinalproject.exceptions.CouldNotReadTheFileException;
import basarab.olexandr.springfinalproject.exceptions.CouldNotStoreFileException;
import basarab.olexandr.springfinalproject.exceptions.NoSuchGroupChatException;
import basarab.olexandr.springfinalproject.repository.GroupChatRepository;
import basarab.olexandr.springfinalproject.service.GroupChatFileStorageService;
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
public class GroupChatFileStorageServiceImpl implements GroupChatFileStorageService {

    private final Path root = Paths.get("D:\\SpringFileStorage");

    @Autowired
    private GroupChatRepository groupChatRepository;

    @Override
    public void saveImg(MultipartFile multipartFile, Long groupChatId) {
        GroupChat groupChat = groupChatRepository.findById(groupChatId)
                .orElseThrow(() -> new NoSuchGroupChatException("Group chat is not found"));
        try {
            Path path = this.root.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            groupChat.setGroupAvatarUrl(path.toAbsolutePath().toString());
        } catch (IOException e) {
            throw new CouldNotStoreFileException("Could not store the file");
        }
        groupChatRepository.save(groupChat);
    }

    @Override
    public void deleteImg(GroupChat groupChat) {
        try {
            Path path = this.root.resolve(Objects.requireNonNull(groupChat.getGroupAvatarUrl()));
            Files.delete(path);
            groupChat.setGroupAvatarUrl(null);
            groupChatRepository.save(groupChat);
        } catch (IOException e) {
            throw new CouldNotDeleteFileException("Could not delete file");
        }
    }

    @Override
    public Resource loadByGroupChatId(Long groupChatId) {
        GroupChat groupChat = groupChatRepository.findById(groupChatId)
                .orElseThrow(() -> new NoSuchGroupChatException("Group chat is not found"));
        try {
            Path file = Paths.get(groupChat.getGroupAvatarUrl());
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
