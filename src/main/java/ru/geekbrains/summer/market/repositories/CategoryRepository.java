package ru.geekbrains.summer.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.summer.market.model.Category;
import ru.geekbrains.summer.market.model.Product;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
