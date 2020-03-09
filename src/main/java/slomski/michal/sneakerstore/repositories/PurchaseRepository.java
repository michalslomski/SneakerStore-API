package slomski.michal.sneakerstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import slomski.michal.sneakerstore.model.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase,Long> {
}
