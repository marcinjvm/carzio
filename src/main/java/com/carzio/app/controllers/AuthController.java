package com.carzio.app.controllers;

import com.carzio.app.exceptions.ErrCode;
import com.carzio.app.models.ERole;
import com.carzio.app.models.Role;
import com.carzio.app.models.User;
import com.carzio.app.payload.request.LoginRequest;
import com.carzio.app.payload.request.SignupRequest;
import com.carzio.app.payload.response.JwtResponse;
import com.carzio.app.payload.response.MessageResponse;
import com.carzio.app.repository.RoleRepository;
import com.carzio.app.repository.UserRepository;
import com.carzio.app.security.jwt.JwtUtils;
import com.carzio.app.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        if (!authentication.isAuthenticated()) {
            return new ResponseEntity(new MessageResponse("Unauthorized"), HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @RequestMapping(value = "/refresh-token", method = RequestMethod.GET)
    public ResponseEntity<?> refreshtoken(@RequestBody String name, HttpServletRequest request) throws RuntimeException {
        String username = jwtUtils.generateRefreshToken(request, name);
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("username is empty");
        }
        return ResponseEntity.ok(new MessageResponse(username));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User already exists"));
        }

        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException(ErrCode.ROLE_NOT_FOUND.getMessage(Locale.ENGLISH)));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity(new MessageResponse("Created"), HttpStatus.CREATED);
    }
}
