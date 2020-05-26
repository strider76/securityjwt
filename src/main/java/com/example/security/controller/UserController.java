package com.example.security.controller;



import com.example.security.dao.RoleRepository;
import com.example.security.dao.UserRepository;
import com.example.security.dto.MessageResponse;
import com.example.security.dto.UserPostDTO;
import com.example.security.model.ERole;
import com.example.security.model.Roles;
import com.example.security.model.Usuarios;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;
    private static String ERRORTOKENNOTFOUND = "Error: Username is already taken!";

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    @GetMapping("/isloged")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> authenticateUser(@RequestHeader("Authorization") String token) {

        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token.replace("Bearer ",""));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException | SignatureException | MalformedJwtException e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/signup")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> registerUser(@RequestBody UserPostDTO signUpRequest) {
        if (userRepository.findByName(signUpRequest.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Create new user's account
        Usuarios user = new Usuarios(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        List<String> strRoles = signUpRequest.getRoles();
        List<Roles> roles = new ArrayList<>();

        if (strRoles == null) {
            Roles userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(ERRORTOKENNOTFOUND));
            roles.add(userRole);
        } else {
            for (String role: strRoles) {

                switch (role.toUpperCase()) {
                    case "ADMIN":
                        Roles adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(ERRORTOKENNOTFOUND));
                        roles.add(adminRole);

                        break;
                    case "CLIENTE":
                        Roles modRole = roleRepository.findByName(ERole.ROLE_CLIENT)
                                .orElseThrow(() -> new RuntimeException(ERRORTOKENNOTFOUND));
                        roles.add(modRole);

                        break;
                    default:
                        throw new RuntimeException(ERRORTOKENNOTFOUND);
                }
            }
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
