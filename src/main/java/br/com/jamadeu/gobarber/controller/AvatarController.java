package br.com.jamadeu.gobarber.controller;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.requests.ReplaceUserRequest;
import br.com.jamadeu.gobarber.service.AvatarService;
import br.com.jamadeu.gobarber.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;

@Log4j2
@RestController
@RequestMapping("/avatar")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;
    private final UserService userService;

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = avatarService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<User> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) {
        User user = userService.findByIdOrThrowBadRequestException(id);
        log.info(user);
        log.info(user.isProvider());
        Resource avatarSaved = avatarService.save(avatar);
        String url = MvcUriComponentsBuilder
                .fromMethodName(AvatarController.class, "getFile", avatarSaved.getFilename()).build().toString();
        ReplaceUserRequest userUpdated = ReplaceUserRequest.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .isProvider(user.isProvider())
                .avatar(url)
                .build();
        userService.replace(userUpdated);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


}
