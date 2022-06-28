package basarab.olexandr.springfinalproject.service;

import basarab.olexandr.springfinalproject.entity.Post;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostFileStorageService {

    void saveImg(MultipartFile multipartFile, Post post);

    void saveImgs(List<MultipartFile> multipartFiles, Post post);

    public void deleteImgs(Post post);

    Resource loadByPostId(Long postId, Integer imageIndex);

}
