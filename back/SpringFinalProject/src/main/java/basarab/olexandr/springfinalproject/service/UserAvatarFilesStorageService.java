package basarab.olexandr.springfinalproject.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface UserAvatarFilesStorageService {

    void saveImg(MultipartFile multipartFile, Long userId);

    Resource loadByPersonId(Long userId);

    void deleteImg(Long userId);

}
