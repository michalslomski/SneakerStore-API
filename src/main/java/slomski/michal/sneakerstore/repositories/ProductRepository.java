package slomski.michal.sneakerstore.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import slomski.michal.sneakerstore.model.Brand;
import slomski.michal.sneakerstore.model.Gender;
import slomski.michal.sneakerstore.model.Product;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findAllByGender(Gender gender);
    List<Product> findAllByBrand(Brand brand);
    List<Product> findAllByBrandAndGender(Brand brand,Gender gender);
}