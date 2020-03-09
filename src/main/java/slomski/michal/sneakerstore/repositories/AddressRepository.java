package slomski.michal.sneakerstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import slomski.michal.sneakerstore.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

}