package ru.practicum.ewm.mainservice.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainservice.category.dto.CategoryDto;
import ru.practicum.ewm.mainservice.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody CategoryDto dto) {
        return categoryService.create(dto);
    }

    @PatchMapping(path = "/{id}")
    public CategoryDto update(@Valid @RequestBody CategoryDto dto,
                              @PathVariable("id") @Positive long id) {
        return categoryService.update(dto, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") @Positive long id) {
        categoryService.delete(id);
    }
}
