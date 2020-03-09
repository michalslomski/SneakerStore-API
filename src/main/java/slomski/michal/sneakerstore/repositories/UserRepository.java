package slomski.michal.sneakerstore.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import slomski.michal.sneakerstore.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmailAddress(String emailAddress);
    Boolean existsByEmailAddress(String emailAddress);


}
