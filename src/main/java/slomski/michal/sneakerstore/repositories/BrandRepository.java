package slomski.michal.sneakerstore.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import slomski.michal.sneakerstore.model.Brand;
import slomski.michal.sneakerstore.model.BrandName;


import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Long> {
    Optional<Brand> findByBrandName(BrandName brandName);

}

