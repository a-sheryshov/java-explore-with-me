package ru.practicum.ewm.mainservice.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainservice.category.dto.CategoryDto;
import ru.practicum.ewm.mainservice.category.mapper.CategoryMapper;
import ru.practicum.ewm.mainservice.category.model.Category;
import ru.practicum.ewm.mainservice.exception.ObjectNotExistsException;
import ru.practicum.ewm.mainservice.exception.OperationFailedException;
import ru.practicum.ewm.mainservice.category.repository.CategoryRepository;
import ru.practicum.ewm.mainservice.event.repository.EventRepository;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto create(CategoryDto dto) {
        Category category = save(dto);
        log.info("Category id = {} was created", category.getId());
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto dto, long catId) {
        Category category = findCategoryOrThrow(catId);

        if (!Objects.equals(dto.getName(), category.getName())) category.setName(dto.getName());
        log.info("Category id = {} was updated", category.getId());

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public void delete(long catId) {
        Category category = findCategoryOrThrow(catId);
        checkDeleteAvailable(category);
        categoryRepository.deleteById(catId);
        log.info("Category id = {} was deleted", catId);
    }

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        List<Category> categories;
        Pageable pageable = PageRequest.of(
                from == 0 ? 0 : (from / size),
                size,
                Sort.by(Sort.Direction.ASC, "id")
        );
        categories = categoryRepository.findAll(pageable).toList();
        return CategoryMapper.toCategoryDto(categories);
    }

    @Override
    public CategoryDto get(long catId) {
        Category category = findCategoryOrThrow(catId);
        return CategoryMapper.toCategoryDto(category);
    }

    public Category findCategoryOrThrow(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotExistsException(
                        String.format("Category id = %s was not found", categoryId))
                );
    }

    private Category save(CategoryDto dto) {
        return categoryRepository.save(CategoryMapper.toCategory(dto));
    }

    private void checkDeleteAvailable(Category category) {
        if (!eventRepository.findAllByCategory(category).isEmpty()) {
            throw new OperationFailedException(
                    String.format("Unable to delete category id = %s. Associated events exist",
                            category.getId())
            );
        }
    }
}
