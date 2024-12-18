package com.app.service.impl;
import com.app.domain.model.*;
import com.app.domain.model.DashboardWishlist.PendingInvitations;
import com.app.domain.model.DashboardWishlist.SubscriberRequests;
import com.app.domain.model.ResponseDTO.ListUsersDTO;
import com.app.domain.model.ResponseDTO.UpdateUserDTO;
import com.app.domain.model.ResponseDTO.UserDTO;
import com.app.domain.model.Utilities.DashboardRequestsAndPending;
import com.app.domain.model.Utilities.Pending;
import com.app.domain.model.Utilities.Requests;
import com.app.domain.repository.ConnectionsUser.RequestsByOthersRepository;
import com.app.domain.repository.ConnectionsUser.RequestsByYouRepository;
import com.app.domain.repository.User.ConnectionsDashboardRepository;
import com.app.domain.repository.User.DashboardEventsRepository;
import com.app.domain.repository.User.DashboardWishlistsRepository;
import com.app.domain.repository.User.UserRepository;
import com.app.domain.repository.Wishlist.*;
import com.app.events.users.UserUpdatedEvent;
import com.app.exception.BusinessRuleException;
import com.app.service.UserService;
import com.app.utils.AuthenticationUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordServiceImpl passwordService;

    @Autowired
    ConnectionsDashboardRepository connectionsRepository;

    @Autowired
    DashboardWishlistsRepository dashboardWishlistsRepository;

    @Autowired
    DashboardEventsRepository dashboardEventsRepository;

    @Autowired
    DashboardRequestsAndPendingRepository dashboardRequestsAndPendingRepository;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    RequestsByYouRepository requestsByYouRepository;

    @Autowired
    RequestsByOthersRepository requestsByOthersRepository;

    @Autowired
    PendingRepository pendingRepository;

    @Autowired
    MySubscriberRequestsRepository mySubscriberRequestsRepository;

    @Autowired
    PendingInvitationsRepository pendingInvitationsRepository;

    @Autowired
    SubscribersRequestsRepository subscribersRequestsRepository;


    @Override
    public Users findById(Integer id_user) {
        return userRepository.findById(id_user).orElseThrow(NoSuchElementException::new);
    }
    @Override
    @Transactional
    public UserDTO getUserById(Integer id_user) {
        return userRepository.findUserById(id_user);
    }

    @Override
    @Transactional
    public List<ListUsersDTO> getAllUsers() {
        return userRepository.findAllUsers();
    }


    @Override
    @Transactional
    public UserDTO createNewUser(Users user, UserDTO newUser, ConnectionsDashboard connections, DashboardRequestsAndPending dashboardRequestsAndPending,
                                 DashboardWishlists dashboardWishlists, DashboardEvents dashboardEvents) {
        validationUsername(user);
        validationEmail(user);

        passwordService.validationsPassword(user.getPassword(), user.getConfirm_password());
        String encryptedPassword = passwordService.encryptPassword(user.getPassword());
        String encryptedConfirmPassword = passwordService.encryptPassword(user.getPassword());

        Users userCreated = new Users(
                newUser.first_name(),
                newUser.last_name(),
                newUser.email(),
                newUser.username(),
                newUser.date_birthday(),
                newUser.gender(),
                encryptedPassword,
                encryptedConfirmPassword,
                newUser.role(),
                newUser.status(),
                connections,
                dashboardRequestsAndPending,
                dashboardWishlists,
                dashboardEvents
        );

        userCreated.setPassword(encryptedPassword);
        userCreated.setFirst_name(user.getFirst_name());
        userCreated.setLast_name(user.getLast_name());
        userCreated.setEmail(user.getEmail());
        userCreated.setUsername(user.getUsername());
        userCreated.setDate_birthday(user.getDate_birthday());
        userCreated.setGender(user.getGender());
        userCreated.setConnectionsDashboard(connections);
        userCreated.setDashboardRequestsAndPending(dashboardRequestsAndPending);
        userCreated.setDashboardWishlists(dashboardWishlists);
        userCreated.setDashboardEvents(dashboardEvents);

        Users savedUser = userRepository.save(userCreated);

        validateEntitys(connections,dashboardWishlists, dashboardEvents, dashboardRequestsAndPending, userCreated);

        return responseRegisterUserDTO(savedUser);

    }

    public void validateEntitys(ConnectionsDashboard connections, DashboardWishlists dashboardWishlists,
                                DashboardEvents dashboardEvents, DashboardRequestsAndPending dashboardRequestsAndPending,
                                Users userCreated){
        validateFieldsConnectionsDashboard(connections, userCreated);
        validateFieldsWishlistDashboard(dashboardWishlists, userCreated);
        validateFieldsEventsDashboard(dashboardEvents, userCreated);
        validateFieldsDashboardRequestsPending(dashboardRequestsAndPending, userCreated);
    }

    public void validateFieldsDashboardRequestsPending(DashboardRequestsAndPending dashboardRequestsAndPending, Users userCreated) {

        if(userCreated != null) {
            dashboardRequestsAndPending.setId_responsible_user(userCreated.getId_user());
            dashboardRequestsAndPending.setResponsible_username(userCreated.getUsername());

            dashboardRequestsAndPendingRepository.save(dashboardRequestsAndPending);

        }

    }

    public void validateFieldsConnectionsDashboard(ConnectionsDashboard connections, Users userCreated) {

        if(userCreated != null) {
            connections.setId_responsible_user(userCreated.getId_user());
            connections.setResponsible_username(userCreated.getUsername());
            connections.setResponsible_user_email(userCreated.getEmail());

            connectionsRepository.save(connections);

        }

    }

    public void validateFieldsWishlistDashboard(DashboardWishlists dashboardWishlists, Users userCreated) {

        if(userCreated != null) {
            dashboardWishlists.setId_responsible_user(userCreated.getId_user());
            dashboardWishlists.setResponsible_username(userCreated.getUsername());

            dashboardWishlistsRepository.save(dashboardWishlists);

        }

    }

    public void validateFieldsEventsDashboard(DashboardEvents dashboardEvents, Users userCreated) {

        if(userCreated != null) {
            dashboardEvents.setId_responsible_user(userCreated.getId_user());
            dashboardEvents.setResponsible_username(userCreated.getUsername());

            dashboardEventsRepository.save(dashboardEvents);

        }

    }


    public UserDTO responseRegisterUserDTO(Users savedUser) {
        UserDTO userDto = new UserDTO(
                savedUser.getId_user(),
                savedUser.getFirst_name(),
                savedUser.getLast_name(),
                savedUser.getEmail(),
                savedUser.getUsername(),
                savedUser.getDate_birthday(),
                savedUser.getGender(),
                savedUser.getPassword(),
                savedUser.getRole(),
                savedUser.getStatus(),
                savedUser.getConnectionsDashboard().getId_dashboard(),
                savedUser.getDashboardRequestsAndPending().getId_dashboard_requests_and_pending(),
                savedUser.getDashboardWishlists().getId_dashboard_wishlists(), savedUser.getDashboardEvents().getId_dashboard_events()
        );

        return userDto;

    }

    @Override
    public UpdateUserDTO toUpdateUser(Users userAccount, Integer id_user, UpdateUserDTO updateUserDTO) {

        AuthenticationUtils.getAuthenticatedUser();

        Users selectedUser = findById(id_user);

        Users authenticated = AuthenticationUtils.validateUser(() -> selectedUser.getId_user());


        if (updateUserDTO.username() != null && !updateUserDTO.username().equals(selectedUser.getUsername())) {
            validationUsername(new Users(updateUserDTO.username(), null));
        }

        if (updateUserDTO.email() != null && !updateUserDTO.email().equals(selectedUser.getEmail())) {
            validationEmail(new Users(updateUserDTO.email(), null));
        }


        selectedUser.setFirst_name(updateUserDTO.first_name());
        selectedUser.setLast_name(updateUserDTO.last_name());
        selectedUser.setEmail(updateUserDTO.email());
        selectedUser.setUsername(updateUserDTO.username());
        selectedUser.setDate_birthday(updateUserDTO.date_birthday());
        selectedUser.setGender(updateUserDTO.gender());

        savingNewUpdatesInTheField(userAccount, selectedUser);

        Users savedNewInfoUser = userRepository.save(userAccount);

        eventPublisher.publishEvent(new UserUpdatedEvent(this, savedNewInfoUser));
        validateOthersFields(savedNewInfoUser, authenticated);
        validateFieldsRequestsByYou(savedNewInfoUser, authenticated);
        validateFieldsRequestsByOthers(savedNewInfoUser, authenticated);

        return responseUpdateUserDTO(savedNewInfoUser);

    }

    private void validateOthersFields(Users savedNewInfoUser, Users authenticated) {
        //REFATORAR PARA NAO FICAR REPETITIVO
        validateFieldsRequestsByYou(savedNewInfoUser, authenticated);
        validateFieldsRequestsByOthers(savedNewInfoUser, authenticated);
        validateFieldsMyPending(savedNewInfoUser, authenticated);
        validateFieldsMyRequests(savedNewInfoUser, authenticated);
        validateFieldsPendingInvitation(savedNewInfoUser, authenticated);
        validateFieldsSubscribersRequests(savedNewInfoUser, authenticated);
    }

    private void validateFieldsPendingInvitation(Users savedNewInfoUser, Users authenticated) {
        List<PendingInvitations> invitationsExist = pendingInvitationsRepository.findAll();

        for(PendingInvitations req : invitationsExist) {
            if(req.getId_user_guest().equals(authenticated.getId_user())) {
                req.setUsername_guest(savedNewInfoUser.getUsername());

                pendingInvitationsRepository.saveAll(invitationsExist);
            }
        }
    }

    private void validateFieldsSubscribersRequests(Users savedNewInfoUser, Users authenticated) {
        List<SubscriberRequests> subscribersRequestsExist = subscribersRequestsRepository.findAll();

        for(SubscriberRequests req : subscribersRequestsExist) {
            if(req.getId_user().equals(authenticated.getId_user())) {
                req.setUsername(savedNewInfoUser.getUsername());

                subscribersRequestsRepository.saveAll(subscribersRequestsExist);
            }
        }
    }

    private void validateFieldsMyRequests(Users savedNewInfoUser, Users authenticated) {
        List<Requests> requestsExist = mySubscriberRequestsRepository.findAll();

        for(Requests req : requestsExist) {
            if(req.getId_user().equals(authenticated.getId_user())) {
                req.setUsername(savedNewInfoUser.getUsername());

                mySubscriberRequestsRepository.saveAll(requestsExist);
            }
        }
    }

    private void validateFieldsMyPending(Users savedNewInfoUser, Users authenticated) {
        List<Pending> pendingExist = pendingRepository.findAll();

        for(Pending req : pendingExist) {
            if(req.getId_user_guest().equals(authenticated.getId_user())) {
                req.setUsername_guest(savedNewInfoUser.getUsername());

                pendingRepository.saveAll(pendingExist);
            }
        }
    }
    private void validateFieldsRequestsByOthers(Users savedNewInfoUser, Users authenticated) {
        List<RequestsByOthers> requestsByOthersExist = requestsByOthersRepository.findAll();

        for(RequestsByOthers req : requestsByOthersExist) {
            if(req.getId_user_requestor().equals(authenticated.getId_user())) {
                req.setUsername(savedNewInfoUser.getUsername());

                requestsByOthersRepository.saveAll(requestsByOthersExist);
            }
        }
    }

    private void validateFieldsRequestsByYou(Users savedNewInfoUser, Users authenticated) {
        List<RequestsByYou> requestsByYouExist = requestsByYouRepository.findAll();

            for(RequestsByYou req : requestsByYouExist) {
                if(req.getId_user_to_add().equals(authenticated.getId_user())) {
                    req.setUsername(savedNewInfoUser.getUsername());

                    requestsByYouRepository.saveAll(requestsByYouExist);
                }
            }
    }

    public UpdateUserDTO responseUpdateUserDTO(Users savedNewInfoUser) {
        UpdateUserDTO UpdateDTO = new UpdateUserDTO(
                savedNewInfoUser.getId_user(),
                savedNewInfoUser.getFirst_name(),
                savedNewInfoUser.getLast_name(),
                savedNewInfoUser.getEmail(),
                savedNewInfoUser.getUsername(),
                savedNewInfoUser.getDate_birthday(),
                savedNewInfoUser.getGender()
        );

        return UpdateDTO;

    }


    // To not return null, if the user updates only 1 field/attribute
    public void savingNewUpdatesInTheField(Users users, Users selectedUser) {

        if(selectedUser.getFirst_name() != null) {
            users.setFirst_name(selectedUser.getFirst_name());
        }

        if(selectedUser.getLast_name() != null) {
            users.setLast_name(selectedUser.getLast_name());
        }

        if(selectedUser.getEmail() != null) {
            users.setEmail(selectedUser.getEmail());
        }

        if(selectedUser.getUsername() != null) {
            users.setUsername(selectedUser.getUsername());
        }

        if(selectedUser.getDate_birthday() != null) {
            users.setDate_birthday(selectedUser.getDate_birthday());
        }
        if(selectedUser.getGender() != null) {
            users.setGender(selectedUser.getGender());
        }
    }


    @Override
    public Users disableAccount(Users user, Integer id_user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessRuleException("Authentication is required to disable your account.", HttpStatus.BAD_REQUEST);
        }

        Users authenticatedUser = (Users) authentication.getPrincipal();

        if (!authenticatedUser.getId_user().equals(id_user)) {
            throw new BusinessRuleException("Access denied!", HttpStatus.BAD_REQUEST);
        }

        user.setStatus(Status.valueOf("DISABLED"));
        user.setPassword(null);

        return userRepository.save(user);

    }


    // Validations
    private void validationEmail(Users user) {
        Users existingEmail = userRepository.findByEmail(user.getEmail());

        if (existingEmail != null) {
            throw new BusinessRuleException("Email already exists.", HttpStatus.CONFLICT);
        }
    }


    private void validationUsername(Users user) {
        Users existingUsername = userRepository.findByUsername(user.getUsername());

        if (existingUsername != null) {
            throw new BusinessRuleException("Username already exists! ", HttpStatus.CONFLICT);
        }


        if (!isValidUsername(user.getUsername())) {
            throw new BusinessRuleException("Username must contain at least 3 characters and cannot contain spaces.", HttpStatus.BAD_REQUEST);
        }


    }

    public boolean isValidUsername(String username) {
        if (username.length() < 3) {
            return false;
        }

        if (username.contains(" ")) {
            return false;
        }
        return true;
    }




}
