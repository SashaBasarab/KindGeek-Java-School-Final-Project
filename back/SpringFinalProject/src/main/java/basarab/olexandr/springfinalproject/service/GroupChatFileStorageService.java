package basarab.olexandr.springfinalproject.service;

import basarab.olexandr.springfinalproject.entity.GroupChat;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface GroupChatFileStorageService {

    void saveImg(MultipartFile multipartFile, Long groupChatId);

    void deleteImg(GroupChat groupChat);

    Resource loadByGroupChatId(Long groupChatId);

}
