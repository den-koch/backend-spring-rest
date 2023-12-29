package io.github.denkoch.mycosts.category.controllers;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.category.models.CategoryDto;
import io.github.denkoch.mycosts.category.models.CategoryRequestDto;
import io.github.denkoch.mycosts.category.services.CategoryService;
import io.github.denkoch.mycosts.exceptions.ResourceNotFoundException;
import io.github.denkoch.mycosts.user.models.User;
import io.github.denkoch.mycosts.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/categories")
@Tag(name = "Category Controller", description = "Operations with Category")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    @Operation(summary = "Get all user Categories",
            description = "This method returns all user payments",
            parameters = @Parameter(name = "userId", description = "User identifier", example = "7a44dbc3-30de-4f75-84e9-a3136e45b911"))
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Success")})
    public ResponseEntity<Collection<CategoryDto>> getCategories(@PathVariable UUID userId) {
        User user = userService.readUser(userId).orElseThrow(() ->
                new ResourceNotFoundException("User {" + userId + "} not found"));

        Collection<CategoryDto> collection = categoryService.readCategories(user)
                .stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();
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

        User user = userService.readUser(userId).orElseThrow(() ->
                new ResourceNotFoundException("User {" + userId + "} not found"));

        Category category = categoryService.readCategory(user, id).orElseThrow(() ->
                new ResourceNotFoundException("Categoty {" + id + "} not found"));

        return ResponseEntity.ok(modelMapper.map(category, CategoryDto.class));
    }

    @PostMapping
    @Operation(summary = "Create user Category",
            description = "This method creates a new user category",
            parameters = @Parameter(name = "userId", description = "User identifier", example = "7a44dbc3-30de-4f75-84e9-a3136e45b911"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Category Entity",
                    content = @Content(schema = @Schema(implementation = Category.class)))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content)}
    )
    public ResponseEntity<CategoryDto> postCategory(@PathVariable UUID userId,
                                                    @RequestBody @Valid CategoryRequestDto categoryRequestDto) {

        User user = userService.readUser(userId).orElseThrow(() ->
                new ResourceNotFoundException("User {" + userId + "} not found"));

        Category category = modelMapper.map(categoryRequestDto, Category.class);
        category.setUser(user);

        category = categoryService.createCategory(category);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(category.getId()).toUri();
        return ResponseEntity.created(uri).body(modelMapper.map(category, CategoryDto.class));
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
