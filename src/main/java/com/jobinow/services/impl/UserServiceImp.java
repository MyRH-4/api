package com.jobinow.services.impl;

import com.jobinow.exceptions.NoAuthenticateUser;
import com.jobinow.exceptions.ResourceNotFoundException;
import com.jobinow.mapper.UserMapper;
import com.jobinow.model.dto.requests.ChangePasswordRequest;
import com.jobinow.model.dto.responses.UserResponses;
import com.jobinow.model.entities.Token;
import com.jobinow.model.entities.User;
import com.jobinow.model.enums.Role;
import com.jobinow.model.enums.TokenType;
import com.jobinow.model.enums.UserStatus;
import com.jobinow.repositories.TokenRepository;
import com.jobinow.repositories.UserRepository;
import com.jobinow.services.spec.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing user-related operations.
 *
 * <p>This service class provides methods for retrieving user data based on various criteria,
 * updating user information, and handling user status changes.</p>
 *
 * @author Ouharri Outman
 * @version 1.0
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserMapper mapper;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    /**
     * Retrieves a paginated list of all users.
     *
     * @param pageable The pagination information.
     * @return A paginated list of all users.
     */
//    @Cacheable("users")
    public Page<UserResponses> getAllUsers(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    /**
     * Retrieves the currently authenticated user.
     * <p>
     * This method fetches the current user's details from the Spring Security context.
     * It performs checks to ensure that there is an authenticated user and that the user
     * is not an instance of {@link AnonymousAuthenticationToken}.
     * </p>
     */
    public UserResponses getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken)
            throw new NoAuthenticateUser("User not authenticated");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapper.toResponse(user);
    }


    /**
     * Retrieves a paginated list of manager users.
     *
     * @param pageable The pagination information.
     * @return A paginated list of manager users.
     */
//    @Cacheable("managers")
    public Page<UserResponses> getAllManager(Pageable pageable) {
        return repository.findAllByRole(Role.MANAGER, pageable)
                .map(mapper::toResponse);
    }

    /**
     * Retrieves a paginated list of agent users.
     *
     * @param pageable The pagination information.
     * @return A paginated list of agent users.
     */
//    @Cacheable("agents")
    public Page<UserResponses> getAllAgent(Pageable pageable) {
        return repository.findAllByRole(Role.AGENT, pageable)
                .map(mapper::toResponse);
    }

    /**
     * Retrieves a paginated list of job seeker users.
     *
     * @param pageable The pagination information.
     * @return A paginated list of job seeker users.
     */
//    @Cacheable("jobSeekers")
    public Page<UserResponses> getAllJobSeeker(Pageable pageable) {
        return repository.findAllByRole(Role.JOB_SEEKER, pageable)
                .map(mapper::toResponse);
    }


    /**
     * Retrieves a paginated list of recruiter users.
     *
     * @param pageable The pagination information.
     * @return A paginated list of recruiter users.
     */
//    @Cacheable("recruiters")
    public Page<UserResponses> getAllRecruiters(Pageable pageable) {
        return repository.findAllByRole(Role.RECRUITER, pageable)
                .map(mapper::toResponse);
    }

    /**
     * Retrieves a user by email.
     *
     * @param email The email of the user to retrieve.
     * @return Optional containing the user if found, otherwise empty.
     */
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param username The ID of the user to retrieve.
     * @return Optional containing the user if found, otherwise empty.
     */
    public Optional<User> findById(UUID username) {
        return repository.findById(username);
    }

    /**
     * Changes the password of the currently logged-in user.
     *
     * @param request       The change password request.
     * @param connectedUser The principal representing the currently connected user.
     * @throws IllegalStateException If the current password is incorrect, or if the new passwords do not match.
     */
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new IllegalStateException("Passwords are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        repository.save(user);
    }

    /**
     * Updates the status of the specified user to offline.
     *
     * @param user The user to update.
     */
    public void disconnect(User user) {
        if (user != null) {
            user.setStatus(UserStatus.OFFLINE);
            repository.save(user);
        }
    }

    /**
     * Retrieves a paginated list of connected users.
     *
     * @param pageable The pagination information.
     * @return A paginated list of connected users.
     */
    public Page<UserResponses> findConnectedUsers(Pageable pageable) {
        return repository.findAllByStatus(UserStatus.ONLINE, pageable)
                .map(mapper::toResponse);
    }


    /**
     * Saves a new user token to the database.
     *
     * @param user     User for whom the token is generated
     * @param jwtToken JWT token to be saved
     */
    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    /**
     * Revokes all valid tokens for a user by marking them as expired and revoked.
     *
     * @param user User for whom tokens are revoked
     */
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }
}