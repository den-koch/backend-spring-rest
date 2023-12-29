package io.github.denkoch.mycosts.payment.controllers;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.category.services.CategoryService;
import io.github.denkoch.mycosts.exceptions.ResourceNotFoundException;
import io.github.denkoch.mycosts.payment.models.Payment;
import io.github.denkoch.mycosts.payment.models.PaymentDto;
import io.github.denkoch.mycosts.payment.models.PaymentRequestDto;
import io.github.denkoch.mycosts.payment.services.PaymentService;
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
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/payments")
@Tag(name = "Payment Controller", description = "Operations with Payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    @Operation(summary = "Get all user Payments by filter",
            description = "This method returns all user payments by filters",
            parameters = {
                    @Parameter(name = "userId", description = "User identifier", example = "7a44dbc3-30de-4f75-84e9-a3136e45b911"),
                    @Parameter(name = "before", description = " Time before filter", example = "2023-11-28"),
                    @Parameter(name = "after", description = " Time after filter", example = "2023-12-06"),
                    @Parameter(name = "categoryId", description = " Category identifier", example = "0ded2f68-b2a7-49f3-856e-6960c9f06cc5"),
                    @Parameter(name = "page", description = "Page number", example = "2"),
            }
    )
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Success")})
    public ResponseEntity<Collection<PaymentDto>> getPayments(@PathVariable UUID userId,
                                                              @RequestParam(required = false) LocalDate before,
                                                              @RequestParam(required = false) LocalDate after,
                                                              @RequestParam(required = false) UUID categoryId,
                                                              @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page) {

        User user = userService.readUser(userId).orElseThrow(() ->
                new ResourceNotFoundException("User {" + userId + "} not found"));

        Collection<PaymentDto> collection = paymentService.readPayments(user, before, after, categoryId, page)
                .stream().map(payment -> modelMapper.map(payment, PaymentDto.class)).toList();
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user Payment by Id",
            description = "This method returns specific user payment by Id",
            parameters = {
                    @Parameter(name = "userId", description = "User identifier", example = "7a44dbc3-30de-4f75-84e9-a3136e45b911"),
                    @Parameter(name = "id", description = " Payment identifier", example = "6c65a3a1-ca59-4874-a086-90c7c3c964df")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "NotFound", content = @Content)}
    )
    public ResponseEntity<PaymentDto> getPayment(@PathVariable UUID userId, @PathVariable UUID id) {
        User user = userService.readUser(userId).orElseThrow(() ->
                new ResourceNotFoundException("User {" + userId + "} not found"));

        Payment payment = paymentService.readPayment(user, id).orElseThrow(() ->
                new ResourceNotFoundException("Payment {" + id + "} not found"));

        return ResponseEntity.ok(modelMapper.map(payment, PaymentDto.class));
    }

    @PostMapping
    @Operation(summary = "Create user Payment",
            description = "This method creates a new user payment",
            parameters = @Parameter(name = "userId", description = "User identifier", example = "7a44dbc3-30de-4f75-84e9-a3136e45b911"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payment Entity",
                    content = @Content(schema = @Schema(implementation = Payment.class)))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content)}
    )
    public ResponseEntity<PaymentDto> postPayment(@PathVariable UUID userId,
                                                  @RequestBody @Valid PaymentRequestDto paymentRequestDto) {

        User user = userService.readUser(userId).orElseThrow(() ->
                new ResourceNotFoundException("User {" + userId + "} not found"));

        UUID categoryId = paymentRequestDto.getCategoryId();
        Category category = categoryService.readCategory(user, categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category {" + categoryId + "} not found"));

        Payment payment = modelMapper.map(paymentRequestDto, Payment.class);
        payment.setUser(user);
        payment.setCategory(category);

        payment = paymentService.createPayment(payment);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(payment.getId()).toUri();
        return ResponseEntity.created(uri).body(modelMapper.map(payment, PaymentDto.class));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user Payment by Id",
            description = "This method updates payment info",
            parameters = {
                    @Parameter(name = "userId", description = "User identifier", example = "7a44dbc3-30de-4f75-84e9-a3136e45b911"),
                    @Parameter(name = "id", description = " Payment identifier", example = "6c65a3a1-ca59-4874-a086-90c7c3c964df")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payment Creation Dto",
                    content = @Content(schema = @Schema(implementation = Payment.class)))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content),
            @ApiResponse(responseCode = "404", description = "NotFound", content = @Content)}
    )
    public ResponseEntity<PaymentDto> putPayment(@PathVariable UUID userId,
                                                 @PathVariable UUID id,
                                                 @RequestBody @Valid PaymentRequestDto paymentRequestDto) {

        Payment payment = paymentService.updatePayment(userId, id, paymentRequestDto);
        return ResponseEntity.ok(modelMapper.map(payment, PaymentDto.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user Payment by Id",
            description = "This method deletes a user payment by Id",
            parameters = {
                    @Parameter(name = "userId", description = "User identifier", example = "7a44dbc3-30de-4f75-84e9-a3136e45b911"),
                    @Parameter(name = "id", description = " Payment identifier", example = "6c65a3a1-ca59-4874-a086-90c7c3c964df")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "NoContent"),
            @ApiResponse(responseCode = "404", description = "NotFound", content = @Content)}
    )
    public void deletePayment(@PathVariable UUID userId,
                              @PathVariable UUID id) {

        paymentService.deletePayment(userId, id);
    }

}
