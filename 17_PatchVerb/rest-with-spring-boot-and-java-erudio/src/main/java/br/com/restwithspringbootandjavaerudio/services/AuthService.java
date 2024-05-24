package br.com.restwithspringbootandjavaerudio.services;

import br.com.restwithspringbootandjavaerudio.DataTransfers.security.AccountCredentialsDto;
import br.com.restwithspringbootandjavaerudio.DataTransfers.security.TokenDto;
import br.com.restwithspringbootandjavaerudio.repositories.UserRepository;
import br.com.restwithspringbootandjavaerudio.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;

    public ResponseEntity signin( AccountCredentialsDto data ){
        try {
            var username = data.getUserName();
            var password = data.getPassword();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            var user = repository.findByUserName(username);
            var tokenResponse = new TokenDto();
            if (user != null) {
                tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
            }else{
                throw new UsernameNotFoundException("Username "+ username+" Not found!");
            }
            return ResponseEntity.ok(tokenResponse);
        }catch (Exception e){
            throw new BadCredentialsException("Invalid username/password supplied!");
        }

    }

    public ResponseEntity refreshToken( String username, String refreshToken){
        var user = repository.findByUserName(username);
        var tokenResponse = new TokenDto();
        if (user != null) {
            tokenResponse = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " Not found!");
        }
        return ResponseEntity.ok(tokenResponse);
    }
}
