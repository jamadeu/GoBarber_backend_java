package br.com.jamadeu.gobarber.controller;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
        return new ResponseEntity<Page<User>>(userService.listAll(pageable), HttpStatus.OK);
    }

}
