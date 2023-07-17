package com.example.sweater.services;

import com.example.sweater.models.Role;
import com.example.sweater.models.User;
import com.example.sweater.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserService{

    private final EmailSender emailSender;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(EmailSender emailSender, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.emailSender = emailSender;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username){
        return userRepo.findByUsername(username);
    }
    @Transactional
    public void save(User user){
        user.setRoles(Collections.singletonList(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        sendMessage(user);
    }

    private void sendMessage(User user) {
        if(!user.getEmail().isEmpty()){
            String message = String.format(
                    "Hello, %s! \n"+
                            "Welcome to Sweater. Please , click the next link : http://localhost:8080/activate/%s", user.getUsername(), user.getActivationCode()
            );
            emailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public List<User> findAll(){
        return userRepo.findAll();
    }

    public Optional<User> findById(int id){return userRepo.findById(id);}

    @Transactional
    public void update(int id, User updatedUser){
        updatedUser.setId(id);
        userRepo.save(updatedUser);
    }
    @Transactional
    public boolean activateUser(String code) {
        Optional<User> optUser = userRepo.findByActivationCode(code);

        if(optUser.isEmpty()) return false;
        User user = optUser.get();
        user.setActive(true);
        user.setActivationCode(null);

        update(user.getId(), user);
        return true;
    }

    @Transactional
    public void saveUserRoles(User user, int id){
        User oldUser = userRepo.findById(id).get();
        oldUser.setUsername(user.getUsername());
        oldUser.setRoles(user.getRoles());
        update(id,oldUser);
    }

    @Transactional
    public void updateProfile(User authUser, User user){
        String userEmail = authUser.getEmail();
        String email = user.getEmail();
        String userPassword = authUser.getPassword();
        String password = user.getPassword();
        boolean isEmailChanged = (!email.isEmpty() && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));
        boolean isPasswordChanged = (!password.isEmpty()&&!password.equals(userPassword));

        if(isPasswordChanged) authUser.setPassword(passwordEncoder.encode(user.getPassword()));

        if(isEmailChanged){
            authUser.setEmail(email);
            authUser.setActivationCode(UUID.randomUUID().toString());
            sendMessage(authUser);
            authUser.setActive(false);
        }

        if(isEmailChanged || isPasswordChanged){
            update(authUser.getId(), authUser);
        }




    }

}
