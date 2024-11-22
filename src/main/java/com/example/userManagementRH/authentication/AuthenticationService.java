package com.example.userManagementRH.authentication;

import com.example.userManagementRH.email.EmailService;
import com.example.userManagementRH.email.EmailTemplateName;
import com.example.userManagementRH.entities.ERole;
import com.example.userManagementRH.entities.Role;
import com.example.userManagementRH.entities.User;
import com.example.userManagementRH.repositories.RoleRepo;
import com.example.userManagementRH.repositories.UserRepo;
import com.example.userManagementRH.security.JwtService;
import com.example.userManagementRH.security.Token;
import com.example.userManagementRH.security.TokenRepo;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final TokenRepo tokenRepository;
    private final RoleRepo roleRepo;
    private final AuthenticationManager authenticationManager;


    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Transactional
    public void register(RegisterRequest request) throws MessagingException {
        ERole eRole;
        try {
            eRole = ERole.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role specified: " + request.getRole());
        }

        String roleName = eRole.toString();
        System.out.println("Role found: " + roleName);


        // Charger le rôle depuis la base de données
        Role userRole = roleRepo.findByName(ERole.valueOf(roleName))
                .orElseThrow(() -> new IllegalStateException("Role not found: " + roleName));

        // Créer un nouvel utilisateur avec le rôle chargé
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .createdDate(request.getCreatedDate())
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))  // Ajouter le rôle ici
                .build();

        // Vérifier le rôle avant la sauvegarde
        System.out.println("Role assigned to user: " + user.getRoles());

        // Sauvegarder l'utilisateur en base de données
        userRepository.save(user);

        // Vérifier le rôle après la sauvegarde
        System.out.println("Role assigned to saved user: " + userRepository.findById(user.getId()).orElseThrow().getRoles());

        // Envoyer l'email de validation
        sendValidationEmail(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.fullName());
        claims.put("userId", user.getId());

        var jwtToken = jwtService.generateToken(claims, user);
        String roles = user.getRoles().stream()
                .map(role -> role.getName().name()) // Assuming Role has a `getName` method returning ERole
                .findFirst() // If the user has only one role
                .orElseThrow(() -> new RuntimeException("User has no roles assigned"));

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(roles)
                .fullName(user.fullName())
                .userId(user.getId())
                .build();
    }

    //@Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    private String generateAndSaveActivationToken(User user) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersWithRoles() {
        return userRepository.findAllUsersWithRoles();
    }

    @Transactional
    public ResponseEntity<String> deleteUser(Long id) {
        // Check if the user exists, if not, throw an exception
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        tokenRepository.deleteByUserId(id);
        // Call the delete method to remove the user from the database
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @Transactional
    public ResponseEntity<String> updateUser(Long id, RegisterRequest request) {
        // Check if the user exists, if not, throw an exception
        User existingUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        // Validate and assign the role
        ERole eRole;
        try {
            eRole = ERole.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role specified: " + request.getRole());
        }

        String roleName = eRole.toString();
        System.out.println("Role found: " + roleName);

        System.out.println("Received request1: " + request.getFirstname());
        System.out.println("Received request2: " + request.getFirstname());
        System.out.println("Received request3: " + request.getLastname());
        System.out.println("Received request4: " + request.getEmail());
        System.out.println("Received request5: " + request.getPhone());
        System.out.println("Received request6: " + request.getRole());
        System.out.println("Received request7: " + request.getLastModifiedDate());

        // Load the role from the database
        Role userRole = roleRepo.findByName(ERole.valueOf(roleName))
                .orElseThrow(() -> new IllegalStateException("Role not found: " + roleName));

        List l = new ArrayList();
        l.add(userRole);
        System.out.println(roleName);
        existingUser.setFirstname(request.getFirstname());
        existingUser.setLastname(request.getLastname());
        existingUser.setEmail(request.getEmail());
        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        existingUser.setRoles( l );
        existingUser.setPhone(request.getPhone());
        existingUser.setLastModifiedDate( LocalDateTime.now() );

        System.out.println(List.of(userRole));

        userRepository.save(existingUser);

        return ResponseEntity.ok("User updated successfully.");
    }


    public ResponseEntity<Long> getUserCount() {
        long count = userRepository.count();  // assuming you have a UserRepository with a count() method
        return ResponseEntity.ok(count);
    }
}
