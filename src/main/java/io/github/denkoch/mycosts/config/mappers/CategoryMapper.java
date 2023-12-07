package io.github.denkoch.mycosts.config.mappers;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.category.models.CategoryCreationDto;
import io.github.denkoch.mycosts.category.models.CategoryDto;

public class CategoryMapper {
    public Category dtoToCategory(CategoryCreationDto categoryCreationDto){
        return new Category(null, categoryCreationDto.getLabel(), categoryCreationDto.getUserId());
    }

    public CategoryDto categoryToDto(Category category){
        return new CategoryDto(category.getId(), category.getLabel());
    }
}
