package io.github.denkoch.mycosts.user.controllers;

import io.github.denkoch.mycosts.config.mappers.UserMapper;
import io.github.denkoch.mycosts.user.models.User;
import io.github.denkoch.mycosts.user.models.UserCreationDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Operations with User")
public class UserController {

    private UserService userService;
    private UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    @Operation(summary = "Get all Users", description = "This method returns all users")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Success")})
    public ResponseEntity<Collection<UserDto>> getUsers() {
        Collection<UserDto> collection = userService.readUsers()
                .stream().map(userMapper::userToDto).toList();
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by Id",
            description = "This method returns specific user by identifier",
            parameters = @Parameter(name = "id", description = "User identifier", example = "21")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "NotFound", content = @Content)}
    )
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        User user = userService.readUser(id);
        return ResponseEntity.ok(userMapper.userToDto(user));
    }

    @PostMapping
    @Operation(summary = "Create User",
            description = "This method creates a new user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User Creation Dto",
                    content = @Content(schema = @Schema(implementation = UserCreationDto.class)))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content)}
    )
    public ResponseEntity<UserDto> postUser(@RequestBody @Valid UserCreationDto userCreationDto) {
        User user = userMapper.dtoToUser(userCreationDto);
        user = userService.createUser(user);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(userMapper.userToDto(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update User by Id",
            description = "This method updates user info",
            parameters = @Parameter(name = "id", description = "User identifier", example = "21"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User Creation Dto",
                    content = @Content(schema = @Schema(implementation = UserCreationDto.class)))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content)}
    )
    public ResponseEntity<UserDto> putUser(@PathVariable Long id, @RequestBody @Valid UserCreationDto userCreationDto) {
        User user = userMapper.dtoToUser(userCreationDto);
        user.setId(id);
        user = userService.updateUser(user);
        return ResponseEntity.ok(userMapper.userToDto(user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete User by Id",
            description = "This method deletes a user by Id",
            parameters = @Parameter(name = "id", description = "User identifier", example = "21")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NoContent"),
            @ApiResponse(responseCode = "404", description = "NotFound", content = @Content)}
    )
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
