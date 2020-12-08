package br.com.jamadeu.gobarber.shared.storage.controller;

import br.com.jamadeu.gobarber.shared.storage.service.AvatarService;
import br.com.jamadeu.gobarber.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.user.requests.ReplaceUserRequest;
import br.com.jamadeu.gobarber.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RestController
@RequestMapping("/avatar")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;
    private final UserService userService;

    @GetMapping("/files/{filename:.+}")
    @Operation(summary = "Find file by name",
            tags = {"avatar"}
    )
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = avatarService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload/{id}")
    @Transactional
    @Operation(summary = "Upload the avatar file and save url",
            tags = {"avatar"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When user not found")
    })
    public ResponseEntity<GoBarberUser> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) {
        GoBarberUser user = userService.findByIdOrThrowBadRequestException(id);
        Resource avatarSaved = avatarService.save(avatar);
        String url = MvcUriComponentsBuilder
                .fromMethodName(AvatarController.class, "getFile", avatarSaved.getFilename()).build().toString();
        ReplaceUserRequest userUpdated = ReplaceUserRequest.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .avatar(url)
                .build();
        userService.replace(userUpdated);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
