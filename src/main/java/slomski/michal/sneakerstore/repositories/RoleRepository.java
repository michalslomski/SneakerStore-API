package slomski.michal.sneakerstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import slomski.michal.sneakerstore.model.Role;
import slomski.michal.sneakerstore.model.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName roleName);
}
