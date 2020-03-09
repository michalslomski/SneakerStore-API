package slomski.michal.sneakerstore.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import slomski.michal.sneakerstore.model.ShoppingCart;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    Optional<ShoppingCart> findByUser_UserId(Long userId);
}
