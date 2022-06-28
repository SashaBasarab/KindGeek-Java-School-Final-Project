package basarab.olexandr.springfinalproject.repository;

import basarab.olexandr.springfinalproject.entity.Game;
import basarab.olexandr.springfinalproject.enums.Games;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findByGameName(Games games);

}
