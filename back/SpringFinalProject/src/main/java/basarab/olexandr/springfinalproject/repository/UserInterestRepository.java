package basarab.olexandr.springfinalproject.repository;

import basarab.olexandr.springfinalproject.entity.UserInterest;
import basarab.olexandr.springfinalproject.enums.Interests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    Optional<UserInterest> findByInterest(Interests interest);

}
