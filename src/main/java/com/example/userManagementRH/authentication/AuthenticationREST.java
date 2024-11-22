package com.example.userManagementRH.authentication;

import com.example.userManagementRH.entities.User;
import com.example.userManagementRH.repositories.UserRepo;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationREST {

    private final AuthenticationService service;
    @Transactional
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegisterRequest request
    ) throws MessagingException {
        service.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }


    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
    }

    @GetMapping("/users")
    public List<User> getUsersWithRoles() {
        return service.getAllUsersWithRoles();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        try {
            // Call the service method to delete the user
            return service.deleteUser(id); // The service will return a ResponseEntity with the success message
        } catch (EntityNotFoundException ex) {
            // Handle the exception if user is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (Exception ex) {
            // Handle any other exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user.");
        }
    }

    @PutMapping("/updateUsers/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody RegisterRequest request) {

        try {
            service.updateUser(id,request);

            return ResponseEntity.ok("User updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: An unexpected error occurred."+e.getMessage());
        }
    }

    @GetMapping("/users/count")
    public HashMap<String, Long> getUserCount() {
        HashMap<String, Long> hm = new HashMap<>();
        ResponseEntity<Long> nb = service.getUserCount();
        hm.put("nbTotal", nb.getBody());
        return hm;
    }

}
