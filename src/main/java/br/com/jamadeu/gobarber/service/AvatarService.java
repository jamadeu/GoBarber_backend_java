package br.com.jamadeu.gobarber.service;

import br.com.jamadeu.gobarber.exception.BadRequestException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class AvatarService {
    private final Path root = Paths.get("uploads");

    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new BadRequestException("Could not initialize folder for upload!");
        }
    }

    public Resource save(MultipartFile file) {
        String filename = file.getOriginalFilename();
        assert filename != null;
        try {
            Files.copy(file.getInputStream(), this.root.resolve(filename));
        } catch (FileAlreadyExistsException e) {
            throw new BadRequestException("File already exists - " + filename);
        } catch (IOException e) {
            throw new BadRequestException("Error to save file");
        }
        return this.load(filename);
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new BadRequestException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new BadRequestException("Error: " + e.getMessage());
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

}
