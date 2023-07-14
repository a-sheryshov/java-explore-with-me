package ru.practicum.ewm.mainservice.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.mainservice.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
