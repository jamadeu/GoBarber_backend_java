package br.com.jamadeu.gobarber.controller;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.requests.NewUserRequest;
import br.com.jamadeu.gobarber.service.UserService;
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
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "List all users paginated",
            description = "The default size is 5, use the parameter to change the default value",
            tags = {"users"}
    )
    public ResponseEntity<Page<User>> listAll(@ParameterObject Pageable pageable) {
        return new ResponseEntity<>(userService.listAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Find user by id",
            tags = {"users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When user does not found")
    })
    public ResponseEntity<User> findById(@PathVariable long id) {
        return new ResponseEntity<>(userService.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
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
    public ResponseEntity<User> save(@RequestBody @Valid NewUserRequest newUserRequest) {
        return new ResponseEntity<>(userService.save(newUserRequest), HttpStatus.CREATED);
    }

}
