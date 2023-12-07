package io.github.denkoch.mycosts.category.controllers;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.category.models.CategoryCreationDto;
import io.github.denkoch.mycosts.category.models.CategoryDto;
import io.github.denkoch.mycosts.category.services.CategoryService;
import io.github.denkoch.mycosts.config.mappers.CategoryMapper;
import io.github.denkoch.mycosts.payment.models.PaymentCreationDto;
import io.github.denkoch.mycosts.user.models.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/categories")
@Tag(name = "Category Controller", description = "Operations with Category")
public class CategoryController {

    private CategoryService categoryService;
    private CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    @Operation(summary = "Get all user Categories",
            description = "This method returns all user payments",
            parameters = @Parameter(name = "userId", description = "User identifier", example = "7a44dbc3-30de-4f75-84e9-a3136e45b911"))
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Success")})
    public ResponseEntity<Collection<CategoryDto>> getCategories(@PathVariable UUID userId) {
        Collection<CategoryDto> collection = categoryService.readCategories(userId)
                .stream().map(categoryMapper::categoryToDto).toList();
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user Category by Id",
            description = "This method returns specific user payment by Id",
            parameters = {
                    @Parameter(name = "userId", description = "User identifier", example = "7a44dbc3-30de-4f75-84e9-a3136e45b911"),
                    @Parameter(name = "id", description = "Category identifier", example = "0ded2f68-b2a7-49f3-856e-6960c9f06cc5")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "NotFound", content = @Content)}
    )
    public ResponseEntity<CategoryDto> getCategory(@PathVariable UUID userId, @PathVariable UUID id) {
        Category category = categoryService.readCategory(userId, id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoryMapper.categoryToDto(category));
    }

    @PostMapping
    @Operation(summary = "Create user Category",
            description = "This method creates a new user category",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Category Creation Dto",
                    content = @Content(schema = @Schema(implementation = CategoryCreationDto.class)))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content)}
    )
    public ResponseEntity<CategoryDto> postCategory(@PathVariable UUID userId,
                                                    @RequestBody @Valid CategoryCreationDto categoryCreationDto) {

        if (!userId.equals(categoryCreationDto.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        Category category = categoryMapper.dtoToCategory(categoryCreationDto);
        category = categoryService.createCategory(category);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(category.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryMapper.categoryToDto(category));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user Category by Id",
            description = "This method deletes a user category by Id",
            parameters = {
                    @Parameter(name = "userId", description = "User identifier", example = "7a44dbc3-30de-4f75-84e9-a3136e45b911"),
                    @Parameter(name = "id", description = " Category identifier", example = "0ded2f68-b2a7-49f3-856e-6960c9f06cc5")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NoContent"),
            @ApiResponse(responseCode = "404", description = "NotFound", content = @Content)}
    )
    public void deleteCategory(@PathVariable UUID userId,
                               @PathVariable UUID id) {
        categoryService.deleteCategory(userId, id);
    }
}
