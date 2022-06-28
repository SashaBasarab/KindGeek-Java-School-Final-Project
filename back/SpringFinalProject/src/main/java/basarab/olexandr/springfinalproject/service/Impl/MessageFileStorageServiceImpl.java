package basarab.olexandr.springfinalproject.service.Impl;

import basarab.olexandr.springfinalproject.entity.Message;
import basarab.olexandr.springfinalproject.entity.MessageImage;
import basarab.olexandr.springfinalproject.exceptions.CouldNotDeleteFileException;
import basarab.olexandr.springfinalproject.exceptions.CouldNotReadTheFileException;
import basarab.olexandr.springfinalproject.exceptions.CouldNotStoreFileException;
import basarab.olexandr.springfinalproject.exceptions.NoSuchMessageException;
import basarab.olexandr.springfinalproject.repository.MessageImageRepository;
import basarab.olexandr.springfinalproject.repository.MessageRepository;
import basarab.olexandr.springfinalproject.service.MessageFileStorageService;
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
public class MessageFileStorageServiceImpl implements MessageFileStorageService {

    private final Path root = Paths.get("D:\\SpringFileStorage");

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageImageRepository messageImageRepository;

    @Override
    public void saveImg(MultipartFile multipartFile, Message message) {
        try {
            saveFile(multipartFile, message);
        } catch (IOException e) {
            throw new CouldNotStoreFileException("Could not store file");
        }
    }

    private void saveFile(MultipartFile multipartFile, Message message) throws IOException {
        Path path = this.root.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        MessageImage messageImage = new MessageImage();
        messageImage.setImgUrl(path.toAbsolutePath().toString());
        messageImageRepository.save(messageImage);
        message.getMessageImages().add(messageImage);
        messageRepository.save(message);
        messageImage.setMessage(message);
        messageImageRepository.save(messageImage);
    }

    @Override
    public void saveImgs(List<MultipartFile> multipartFile, Message message) {
        try {
            for (MultipartFile file : multipartFile) {
                saveFile(file, message);
            }
        } catch (Exception e) {
            throw new CouldNotStoreFileException("Could not store file");
        }
    }

    @Override
    public void deleteImgs(Message message) {
        for (MessageImage messageImage : message.getMessageImages()) {
            try {
                Path path = this.root.resolve(Objects.requireNonNull(messageImage.getImgUrl()));
                messageImageRepository.delete(messageImage);
                Files.delete(path);
            } catch (IOException e) {
                throw new CouldNotDeleteFileException("Could not delete file");
            }
        }
        message.getMessageImages().clear();
        messageRepository.save(message);
    }

    @Override
    public Resource loadByMessageId(Long messageId, Integer imageIndex) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchMessageException("Message is not found"));
        try {
            Path file = Paths.get(message.getMessageImages().get(imageIndex).getImgUrl());
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
