package basarab.olexandr.springfinalproject.service;

import basarab.olexandr.springfinalproject.entity.Message;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MessageFileStorageService {

    void saveImg(MultipartFile multipartFile, Message messageEntity);

    void saveImgs(List<MultipartFile> multipartFile, Message messageEntity);

    void deleteImgs(Message message);

    Resource loadByMessageId(Long messageId, Integer imageIndex);

}
