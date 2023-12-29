package io.github.denkoch.mycosts.user.controllers;

import io.github.denkoch.mycosts.user.models.User;
import io.github.denkoch.mycosts.user.models.UserDto;
import io.github.denkoch.mycosts.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Operations with User")
public class UserController {

    private UserService userService;
    private ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @Operation(summary = "Get all Users", description = "This method returns all users")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Success")})
    public ResponseEntity<Collection<UserDto>> getUsers() {
        Collection<UserDto> collection = userService.readUsers()
                .stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by Id",
            description = "This method returns specific user by identifier",
            parameters = @Parameter(name = "id", description = "User identifier", example = "7a44dbc3-30de-4f75-84e9-a3136e45b911")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "NotFound", content = @Content)}
    )
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
        Optional<User> user = userService.readUser(id);
        if (user.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(user, UserDto.class));
    }

    @PostMapping
    @Operation(summary = "Create User",
            description = "This method creates a new user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User entity",
                    content = @Content(schema = @Schema(implementation = User.class)))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content)}
    )
    public ResponseEntity<UserDto> postUser(@RequestBody @Valid User user) {
        user = userService.createUser(user);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(modelMapper.map(user, UserDto.class));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update User by Id",
            description = "This method updates updatedUser info",
            parameters = @Parameter(name = "id", description = "User identifier", example = "7a44dbc3-30de-4f75-84e9-a3136e45b911"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User",
                    content = @Content(schema = @Schema(implementation = User.class)))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content),
            @ApiResponse(responseCode = "404", description = "NotFound", content = @Content)}
    )
    public ResponseEntity<UserDto> putUser(@PathVariable UUID id, @RequestBody @Valid User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(modelMapper.map(user, UserDto.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete User by Id",
            description = "This method deletes a user by Id",
            parameters = @Parameter(name = "id", description = "User identifier", example = "7a44dbc3-30de-4f75-84e9-a3136e45b911")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NoContent"),
            @ApiResponse(responseCode = "404", description = "NotFound", content = @Content)}
    )
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

}
