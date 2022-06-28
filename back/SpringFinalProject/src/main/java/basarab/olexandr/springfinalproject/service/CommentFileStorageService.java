package basarab.olexandr.springfinalproject.service;

import basarab.olexandr.springfinalproject.entity.Comment;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommentFileStorageService {

    void saveImg(MultipartFile  multipartFile, Comment comment);

    void saveImgs(List<MultipartFile>  multipartFiles, Comment comment);

    void deleteImgs(Comment comment);

    Resource loadByCommentId(Long commentId, Integer imageIndex);

}
