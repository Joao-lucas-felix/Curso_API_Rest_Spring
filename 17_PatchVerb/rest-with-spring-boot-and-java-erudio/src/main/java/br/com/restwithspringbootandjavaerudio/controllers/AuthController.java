package br.com.restwithspringbootandjavaerudio.controllers;

import br.com.restwithspringbootandjavaerudio.DataTransfers.BookDto;
import br.com.restwithspringbootandjavaerudio.DataTransfers.security.AccountCredentialsDto;
import br.com.restwithspringbootandjavaerudio.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "End for authentication")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService service;

    @Operation(summary = "Authenticates a use and returns a token ")
    @PostMapping(value = "/signin")
    public ResponseEntity signin
            (@RequestBody AccountCredentialsDto dto)
    {
        if (checkParams(dto)) return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                        .body("Invalid Client Request");
        var token = service.signin(dto);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                        .body("Invalid Client Request");
        return token;
    }

    @Operation(summary = "Refresh the token of a authenticated user, and returns a token")
    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity refreshToken
            (@PathVariable("username") String userName,
             @RequestHeader("Authorization") String refreshToken )
    {
        if (checkParams(userName, refreshToken))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Invalid Client Request");
        var token = service.refreshToken(userName, refreshToken);
        if (token == null) return
                ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Invalid Client Request");
        return token;
    }

    private static boolean checkParams(String userName, String refreshToken) {
        return refreshToken == null || refreshToken.isBlank() || userName.isBlank() || userName == null;
    }

    private static boolean checkParams(AccountCredentialsDto dto) {
        return dto == null || dto.getPassword() == null || dto.getPassword().isBlank() || dto.getUserName() == null || dto.getUserName().isBlank();
    }
}
