package igc.mirror.ldapuser.controller;

import igc.mirror.ldapuser.model.LdapUser;
import igc.mirror.ldapuser.service.LdapUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("ldapuser")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Права пользователей AD")
public class LdapUserController {
    @Autowired
    LdapUserService ldapUserService;

    @Operation(summary = "Получение списка прав пользователя mirror из AD")
    @GetMapping("{name}")
    public LdapUser getLdapUser(@PathVariable String name) {
        return ldapUserService.getLdapUser(name);
    }
    @Operation(summary = "Получение списка СУЗ mirror из AD")
    @GetMapping("svc_users")
    public List<LdapUser> ldapUserSvc() {
        return ldapUserService.getLdapUserSvc();
    }
}