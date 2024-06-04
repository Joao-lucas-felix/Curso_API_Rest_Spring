package br.com.restwithspringbootandjavaerudio.security.jwt;

import br.com.restwithspringbootandjavaerudio.DataTransfers.security.TokenDto;
import br.com.restwithspringbootandjavaerudio.exception.InvalidJwtAuthenticationExeception;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";
    @Value("${security.jwt.token.expire-length:secret: 36000000}")
    private long validityInMilliSeconds = 36000000;

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;
    @PostConstruct //The spring execute this method before accept users request.
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes()); //Encrypt the secret key
        algorithm = Algorithm.HMAC256(secretKey.getBytes());// Define the algorithm
    }

    public TokenDto createAccessToken(String username, List<String> roles){
        Date now = new Date();
        // Get the actual timestamp and adds one hour to take the validity time of token
        Date validity = new Date(now.getTime() + validityInMilliSeconds);
        var accessToken = getAccessToken(username,roles, now ,validity);
        var refreshToken = getRefreshToken(username,roles, now);
        //Create the transfer object
        return new TokenDto(username, true, now, validity, accessToken, refreshToken);
    }

    private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
        //Create the Jwt token access
        String issueUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)//When are created
                .withExpiresAt(validity)//When expire
                .withSubject(username)// the user
                .withIssuer(issueUrl)
                .sign(algorithm)
                .strip();

    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        Date validityRefreshToken = new Date(now.getTime() + (validityInMilliSeconds*3));
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)//Quando foi criado
                .withExpiresAt(validityRefreshToken)//Quando irá expirar
                .withSubject(username)// De quem é o token
                .sign(algorithm)
                .strip();
    }

    public Authentication getAuthentication(String token){
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        // vem no formato Bearer {token}
        if (
                bearerToken != null && bearerToken.startsWith("Bearer ")
        ){
            return  bearerToken.substring("Bearer ".length());
        }
       return null;
    }
    public boolean validateToken(String token){
        DecodedJWT decodedJWT = decodedToken(token);
        try{
            if (decodedJWT.getExpiresAt().before(new Date())){ // verfifica se expira antes de agora.
                return false;
            }
            return true;
        }catch (Exception e){
            throw new InvalidJwtAuthenticationExeception("Expired or invalid JWT Token");
        }
    }
    public TokenDto refreshToken(String refreshToken){
        if (refreshToken.contains("Bearer ")) refreshToken = refreshToken.substring("Bearer ".length());
        //Decoded the refresh token
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        //Get the username and the roles
        String userName = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        //Create a new access token
        return createAccessToken(userName, roles);
    }
}
