package br.com.jamadeu.gobarber;

import br.com.jamadeu.gobarber.service.AvatarService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class GobarberApplication implements CommandLineRunner {

    @Resource
    AvatarService avatarService;

    public static void main(String[] args) {
        SpringApplication.run(GobarberApplication.class, args);
    }

    public void run(String... arg) throws Exception {
        avatarService.deleteAll();
        avatarService.init();
    }

}
