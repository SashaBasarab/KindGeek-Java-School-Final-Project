package basarab.olexandr.springfinalproject.repository;

import basarab.olexandr.springfinalproject.entity.Role;
import basarab.olexandr.springfinalproject.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(UserRole name);

}
