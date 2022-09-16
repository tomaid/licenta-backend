package com.licenta.licenta.service;

import com.licenta.licenta.Security.UserPrinciple;
import com.licenta.licenta.dto.*;
import com.licenta.licenta.model.Mesaj;
import com.licenta.licenta.model.Role;
import com.licenta.licenta.model.User;
import com.licenta.licenta.repository.RoleRepository;
import com.licenta.licenta.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;
    Mesaj mesaj = new Mesaj();

    @Autowired
    public List<UserRegistrationDto> getALL(){
        List<User> users=userRepository.findAll();
        List<UserRegistrationDto> userRegistrationDtoList=new ArrayList<>();
        for (User user: users) {
            UserRegistrationDto userRegistrationDto=new UserRegistrationDto(user);
            userRegistrationDtoList.add(userRegistrationDto);
        }
        return userRegistrationDtoList;
    }

     public ResponseEntity<Mesaj> creareUser(UserRegistrationDto userRegistrationDto) {
        User user = new User(userRegistrationDto);
        Role role = new Role();
        String userID=userRegistrationDto.getUser();
        String userPass=userRegistrationDto.getPass();
        String userNume=userRegistrationDto.getNume();
        String userPrenume=userRegistrationDto.getPrenume();
        String userTelefon = userRegistrationDto.getTelefon();
        Long userRoleId=userRegistrationDto.getRole().getId();

        if(userID.isEmpty()){
            mesaj.setMessage("Introduceti userul");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
        }
        if(!EmailValidator.getInstance().isValid(userID)){
            mesaj.setMessage("Userul nu este valid");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
        }
         if(userPass.isEmpty()){
             mesaj.setMessage("Introduceti parola");
             mesaj.setCode(406);
             return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
         }
         if(userPrenume.isEmpty()){
             mesaj.setMessage("Introduceti prenumele");
             mesaj.setCode(406);
             return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
         }
         if(userNume.isEmpty()){
             mesaj.setMessage("Introduceti numele");
             mesaj.setCode(406);
             return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
         }
         if(userTelefon.isEmpty()){
             mesaj.setMessage("Introduceti numărul de telefon");
             mesaj.setCode(406);
             return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
         }
         if(!userTelefon.matches("[0-9]+")){
             mesaj.setMessage("Reintroduceti numărul de telefon (introduceți doar cifre)");
             mesaj.setCode(406);
             return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
         }
         if((userTelefon.length()<10)||(userTelefon.length()>15)){
             mesaj.setMessage("Reintroduceti numărul de telefon complet.");
             mesaj.setCode(406);
             return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
         }
         Optional<Role> optionalRole = roleRepository.findById(userRoleId);
         if(optionalRole.isEmpty()){
             mesaj.setMessage("Rol inexistent");
             mesaj.setCode(406);
             return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
         }
         else {
             role.setId(userRoleId);
         }
        Optional<User> optionalEmployee = userRepository.findByUser(userID);
        if(optionalEmployee.isPresent()) {
            mesaj.setMessage("Userul exista");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
        }
        user.setPass(BCrypt.hashpw(userPass, BCrypt.gensalt()));
        user.setRole(role);
        userRepository.save(user);
         mesaj.setMessage("Userul a fost creat");
         mesaj.setCode(201);
        return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
    }

    public UserActualizareDto getProfile(OAuth2Authentication auth2Authentication) {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        Optional<User> user = userRepository.findByUser(email);
        UserActualizareDto userActualizareDto = new UserActualizareDto();
        userActualizareDto.setUser(user.get().getUser());
        userActualizareDto.setNume(user.get().getNume());
        userActualizareDto.setPrenume(user.get().getPrenume());
        userActualizareDto.setTelefon(user.get().getTelefon());
        return userActualizareDto;
    }

    public RoleDto getRole(OAuth2Authentication auth2Authentication) {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        Optional<User> user = userRepository.findByUser(email);
        RoleDto roleDto = new RoleDto();
        roleDto.setId(user.get().getRole().getId());
        return roleDto;
    }

    public ResponseEntity<Mesaj> resetareParola(ResetParolaDto resetParolaDto) {
        String userID=resetParolaDto.getUsername();

        if(userID.isEmpty()){
            mesaj.setMessage("Introduceti email-ul!");
            mesaj.setCode(406);
            return new ResponseEntity<Mesaj>(mesaj,HttpStatus.NOT_ACCEPTABLE);
        }
        User username = userRepository.findByUser(userID).get();
        if(username.getId().toString().isEmpty()){
            mesaj.setMessage("Utilizator invalid");
            mesaj.setCode(406);
            return new ResponseEntity<Mesaj>(mesaj,HttpStatus.NOT_ACCEPTABLE);
        }
        char[] possibleCharacters = (new String("!@#$%^&*()+-_ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")).toCharArray();
        String parolaNoua = RandomStringUtils.random( 8, 0, possibleCharacters.length-1, false, false, possibleCharacters, new SecureRandom() );
        username.setPass(BCrypt.hashpw(parolaNoua, BCrypt.gensalt()));
        String mesajBody = "Buna ziua," + "\n\n" + "Noua parola pentru contul de utilizator " + username.getUser()  + " este " + parolaNoua + ".\n\n O zi buna!";
        emailService.trimiteMail(username.getUser(), "Parola a fost schimbata!", mesajBody);
        userRepository.save(username);
        mesaj.setMessage("Parola a fost schimbata");
        mesaj.setCode(200);
        return new ResponseEntity<Mesaj>(mesaj,HttpStatus.OK);
    }

    public ResponseEntity<Object> actualizareDate(OAuth2Authentication auth2Authentication, UserActualizareDto userActualizareDto) {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        User user = userRepository.findByUser(email).get();
        Optional<User> userEmail = userRepository.findByUser(userActualizareDto.getUser());
        if(user.getId().toString().isEmpty()){
            mesaj.setMessage("Utilizator invalid");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
        }
        if(userEmail.isPresent()&&!userEmail.get().getUser().equals(user.getUser())){
            mesaj.setMessage("Exista un cont cu acest email!");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
        }
        String nume = userActualizareDto.getNume();
        String prenume = userActualizareDto.getPrenume();
        String telefon = userActualizareDto.getTelefon();
        String eMail = userActualizareDto.getUser();
        if(!nume.isBlank()) {
            user.setNume(nume);
        }
        if(!prenume.isBlank()) {
            user.setPrenume(prenume);
        }
        if(!telefon.isBlank()) {
            user.setTelefon(telefon);
        }
        if(!eMail.isBlank()) {
            user.setUser(eMail);
        }
        userRepository.save(user);
        mesaj.setMessage("Datele au fost actualizate!");
        mesaj.setCode(200);
        return new ResponseEntity<>(mesaj,HttpStatus.OK);
    }

    public ResponseEntity<Object> actualizareParola(OAuth2Authentication auth2Authentication, UserActualizareParolaDto userActualizareParolaDto) {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        User user = userRepository.findByUser(email).get();
        String dbPassword = user.getPass();
        if(user.getId().toString().isEmpty()){
            mesaj.setMessage("Utilizator invalid");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
        }
        String parolaVeche = userActualizareParolaDto.getParolaVeche();
        String parolaNoua = userActualizareParolaDto.getParolaNoua();

        if(parolaVeche.isEmpty()||parolaNoua.isEmpty()){
            mesaj.setMessage("Introduceti o parola formata din minim 8 caractere.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
        }
        if(!BCrypt.checkpw(parolaVeche,dbPassword)) {
            mesaj.setMessage("Nu ati introdus parola corecta.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
        }

        if(!parolaNoua.isBlank()) {
            user.setPass(BCrypt.hashpw(parolaNoua, BCrypt.gensalt()));
        }
        userRepository.save(user);
        mesaj.setMessage("Parola a fost actualizata!");
        mesaj.setCode(200);
        return new ResponseEntity<>(mesaj,HttpStatus.OK);
    }
}
