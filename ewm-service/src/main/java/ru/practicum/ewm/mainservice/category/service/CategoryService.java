package ru.practicum.ewm.mainservice.category.service;

import ru.practicum.ewm.mainservice.category.dto.CategoryDto;
import ru.practicum.ewm.mainservice.category.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto dto);

    CategoryDto update(CategoryDto dto, long catId);

    void delete(long catId);

    List<CategoryDto> getAll(int from, int size);

    CategoryDto get(long catId);

    Category findCategoryOrThrow(long categoryId);
}
