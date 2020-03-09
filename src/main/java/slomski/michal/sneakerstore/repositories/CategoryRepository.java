package slomski.michal.sneakerstore.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import slomski.michal.sneakerstore.model.Category;
import slomski.michal.sneakerstore.model.CategoryName;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByCategoryName(CategoryName categoryName);
}
