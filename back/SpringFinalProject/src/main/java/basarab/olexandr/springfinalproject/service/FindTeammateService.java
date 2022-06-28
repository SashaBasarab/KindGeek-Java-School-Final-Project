package basarab.olexandr.springfinalproject.service;

import basarab.olexandr.springfinalproject.dto.response.UserResponseDTO;

import java.util.List;

public interface FindTeammateService {

    List<UserResponseDTO> findTeammates(Long userId, Integer socialRating, String nationality, String motherTongue, String gameName);

    void setReady(Long userId, String gameName);

}
