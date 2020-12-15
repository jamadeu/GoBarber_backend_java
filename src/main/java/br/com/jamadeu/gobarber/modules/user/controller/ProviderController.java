package br.com.jamadeu.gobarber.modules.user.controller;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.requests.NewProviderRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ReplaceProviderRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ResetPasswordRequest;
import br.com.jamadeu.gobarber.modules.user.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;


@RestController
@RequestMapping("providers")
@RequiredArgsConstructor
public class ProviderController {
    private final ProviderService providerService;

    @GetMapping
    @Operation(summary = "List all providers paginated",
            description = "The default size is 5, use the parameter to change the default value",
            tags = {"users"}
    )
    public ResponseEntity<Page<GoBarberProvider>> listAll(@ParameterObject Pageable pageable) {
        return new ResponseEntity<>(providerService.listAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Find provider by id",
            tags = {"users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When user does not found")
    })
    public ResponseEntity<GoBarberProvider> findById(@PathVariable long id) {
        return new ResponseEntity<>(providerService.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Create a new user",
            description = "Name, email and password fields are mandatory, " +
                    "password field must have at least 6 characters," +
                    "email must be unique",
            tags = {"users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When there is an error with some mandatory field")
    })
    public ResponseEntity<GoBarberProvider> save(@RequestBody @Valid NewProviderRequest newProviderRequest) {
        return new ResponseEntity<>(providerService.save(newProviderRequest), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Replace an existing user, the user to be replaced must be logged in",
            tags = {"users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When user not found")
    })
    public ResponseEntity<GoBarberProvider> replace(@RequestBody @Valid ReplaceProviderRequest replaceProviderRequest) {
        providerService.replace(replaceProviderRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete an existing user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When user not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        providerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/reset-password")
    @Operation(summary = "Change user's password",
            tags = {"users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When user not found")
    })
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        providerService.resetPassword(resetPasswordRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
