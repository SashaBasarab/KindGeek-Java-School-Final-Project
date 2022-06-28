package basarab.olexandr.springfinalproject.repository;

import basarab.olexandr.springfinalproject.entity.FindTeammate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FindTeammateRepository extends JpaRepository<FindTeammate, Long> {
}
