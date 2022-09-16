package com.licenta.licenta.service;

import com.licenta.licenta.Security.UserPrinciple;
import com.licenta.licenta.dto.ApartamentDto;
import com.licenta.licenta.dto.ApartamentLocatarDto;
import com.licenta.licenta.model.*;
import com.licenta.licenta.repository.ApartamentRepository;
import com.licenta.licenta.repository.AsociatieRepository;
import com.licenta.licenta.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApartamentService {
    @Autowired
    private ApartamentRepository apartamentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AsociatieRepository asociatieRepository;
    @Autowired
    private EmailService emailService;

    Mesaj mesaj = new Mesaj();
    public ResponseEntity<Object> createApartament(OAuth2Authentication auth2Authentication, ApartamentDto apartamentDto, Long asocId) throws ResponseStatusException {

        if(apartamentDto.getNumar().isBlank()){
            mesaj.setMessage("Introduceti numarul apartamentului");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }

        if(apartamentDto.getSuprafataMp()<=0){
            mesaj.setMessage("Introduceti suprafata apartamentului");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(apartamentDto.getNumarLocatari()<=0){
            mesaj.setMessage("Introduceti numarul de locatari");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(apartamentDto.getPrenume().isEmpty()){
            mesaj.setMessage("Introduceti prenumele proprietarului");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(apartamentDto.getNume().isEmpty()){
            mesaj.setMessage("Introduceti numele proprietarului");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(apartamentDto.getTelefon().isEmpty()){
            mesaj.setMessage("Introduceti numarul de telefon al proprietarului");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(apartamentDto.getEmail().isEmpty()){
            mesaj.setMessage("Introduceti email-ul");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(!EmailValidator.getInstance().isValid(apartamentDto.getEmail())){
            mesaj.setMessage("Email-ul nu este valid");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if(asociatieRepository.findById(asocId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if(!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        Apartament apartament = new Apartament();
        apartament.setNumar_apartament(apartamentDto.getNumar());
        apartament.setSuprafata_mp(apartamentDto.getSuprafataMp());
        apartament.setNumar_locatari(apartamentDto.getNumarLocatari());
        apartament.setAsociatie(asociatie);
        if(userRepository.findByUser(apartamentDto.getEmail()).isEmpty())
        {
            User user = new User();
            user.setUser(apartamentDto.getEmail());
            char[] possibleCharacters = (new String("!@#$%^&*()_+-ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")).toCharArray();
            String parolaNoua = RandomStringUtils.random( 8, 0, possibleCharacters.length-1, false, false, possibleCharacters, new SecureRandom() );
            user.setPass(BCrypt.hashpw(parolaNoua, BCrypt.gensalt()));
            user.setRole(new Role(2L));
            user.setNume(apartamentDto.getNume());
            user.setPrenume(apartamentDto.getPrenume());
            user.setTelefon(apartamentDto.getTelefon());
            userRepository.save(user);
            String mesajBody = "Buna ziua," + "\n\n" + "A fost creat userul  " + user.getUser()+ " cu parola " + parolaNoua + "\n\n" + "Multumim pentru ca folositi aceasta platforma";
            emailService.trimiteMail(user.getUser(), "Ati fosti inscris pe platforma asociatiei de proprietari!", mesajBody);
            apartament.setUser(userRepository.findByUser(user.getUser()).get());
        }else{
            User user = userRepository.findByUser(apartamentDto.getEmail()).get();
            apartament.setUser(user);
        }
        apartamentRepository.save(apartament);
        mesaj.setMessage("Apartamentul a fost creat.");
        mesaj.setCode(201);
        return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
    }

    public List<ApartamentDto> getAll(OAuth2Authentication auth2Authentication, Long id) throws ResponseStatusException  {
        if(id.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: introduceti asociatia");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if(asociatieRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        Asociatie asociatie = asociatieRepository.findById(id).get();
        if(!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        Long userId = userRepository.findByUser(email).get().getId();
        List<Apartament> apartamente = apartamentRepository.getApartamenteByUserAndAsociatie(userId, id);
        List<ApartamentDto> apartamenteDtoList = new ArrayList<>();
        for (Apartament apartament: apartamente
        ) {
            ApartamentDto apartamentDto = new ApartamentDto();
            apartamentDto.setId(apartament.getId());
            apartamentDto.setNumar(apartament.getNumar_apartament());
            apartamentDto.setSuprafataMp(apartament.getSuprafata_mp());
            apartamentDto.setNumarLocatari(apartament.getNumar_locatari());
            apartamentDto.setUserId(apartament.getUser().getId());
            apartamentDto.setNume(apartament.getUser().getNume());
            apartamentDto.setPrenume(apartament.getUser().getPrenume());
            apartamentDto.setTelefon(apartament.getUser().getTelefon());
            apartamentDto.setEmail(apartament.getUser().getUser());
            apartamenteDtoList.add(apartamentDto);
        }
        return apartamenteDtoList;
    }

    public ResponseEntity<Object> actualizareApartament(OAuth2Authentication auth2Authentication, Long asocId, Long apId, ApartamentDto apartamentDto) {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        String email = userPrinciple.getUsername();
        if(asociatieRepository.findById(asocId).isEmpty()) {
            mesaj.setMessage("Asociatia nu exista.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(apartamentRepository.findById(apId).isEmpty()) {
            mesaj.setMessage("Apartamentul nu exista.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get())){
            mesaj.setMessage("Neautorizat");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(!asociatie.getApartamente().contains(apartamentRepository.getById(apId))){
            mesaj.setMessage("Actualizare neautorizata");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        Optional<Apartament> apartament = apartamentRepository.findById(apId);

        if(!apartamentDto.getNumar().isBlank()){
            apartament.get().setNumar_apartament(apartamentDto.getNumar());
        }
        if(apartamentDto.getSuprafataMp()>0){
            apartament.get().setSuprafata_mp(apartamentDto.getSuprafataMp());
        }
        if(apartamentDto.getNumarLocatari()>0){
            apartament.get().setNumar_locatari(apartamentDto.getNumarLocatari());
        }
        if(!apartamentDto.getPrenume().isEmpty()){
            apartament.get().getUser().setPrenume(apartamentDto.getPrenume());
        }
        if(!apartamentDto.getNume().isEmpty()){
            apartament.get().getUser().setNume(apartamentDto.getNume());
        }
        if(!apartamentDto.getTelefon().isEmpty()){
            apartament.get().getUser().setTelefon(apartamentDto.getTelefon());
        }
        if(apartamentDto.getEmail().isEmpty()){
            mesaj.setMessage("Introduceti email-ul");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(!EmailValidator.getInstance().isValid(apartamentDto.getEmail())){
            mesaj.setMessage("Email-ul nu este valid");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj,HttpStatus.NOT_ACCEPTABLE);
        }
        if(apartamentDto.getEmail()!=apartament.get().getUser().getUser()){
            if(userRepository.findByUser(apartamentDto.getEmail()).isEmpty())
            {
                User user = new User();
                user.setUser(apartamentDto.getEmail());
                char[] possibleCharacters = (new String("!@#$%^&*()_+-ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")).toCharArray();
                String parolaNoua = RandomStringUtils.random( 8, 0, possibleCharacters.length-1, false, false, possibleCharacters, new SecureRandom() );
                user.setPass(BCrypt.hashpw(parolaNoua, BCrypt.gensalt()));
                user.setRole(new Role(2L));
                user.setNume(apartamentDto.getNume());
                user.setPrenume(apartamentDto.getPrenume());
                user.setTelefon(apartamentDto.getTelefon());
                userRepository.save(user);
                String mesajBody = "Buna ziua," + "\n\n" + "A fost creat userul  " + user.getUser()+ " cu parola " + parolaNoua + "\n\n" + "Multumim pentru ca folositi aceasta platforma";
                emailService.trimiteMail(user.getUser(), "Ati fosti inscris pe platforma asociatiei de proprietari!", mesajBody);
                apartament.get().setUser(userRepository.findByUser(user.getUser()).get());
            }else{
                User user = userRepository.findByUser(apartamentDto.getEmail()).get();
                apartament.get().setUser(user);
            }}
        apartamentRepository.save(apartament.get());
        mesaj.setMessage("Datele apartamentului au fost actualizate");
        mesaj.setCode(200);
        return new ResponseEntity<>(mesaj, HttpStatus.OK);
    }

    public ResponseEntity<Object> stergereApartament(OAuth2Authentication auth2Authentication, Long asocId, Long apId) {
        if(asocId.toString().isBlank()){
            mesaj.setMessage("Asociatia nu exista.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(apId.toString().isBlank()){
            mesaj.setMessage("Apartamentul nu exista.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }

        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        if(asociatieRepository.findById(asocId).isEmpty()){
            mesaj.setMessage("Nu aveti acces la aceasta asociatie");
            mesaj.setCode(406);
            mesaj.setAsociatie(asocId);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        String email = userPrinciple.getUsername();
        if(asociatieRepository.findById(asocId).isEmpty()) {
            mesaj.setMessage("Apartamentul nu exista.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get())) {
            mesaj.setMessage("Apartamentul nu exista.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }

        if(!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get())){
            if(!asociatie.getApartamente().contains(apartamentRepository.getById(apId))){
                mesaj.setMessage("Apartamentul nu a fost sters.");
                mesaj.setCode(406);
                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
            }
            mesaj.setMessage("Neautorizat");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        apartamentRepository.deleteById(apId);
        mesaj.setMessage("Datele apartamentului au fost sterse");
        mesaj.setCode(200);
        return new ResponseEntity<>(mesaj, HttpStatus.OK);

    }

    public List<ApartamentLocatarDto> getApartamente(OAuth2Authentication auth2Authentication)  throws ResponseStatusException {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        Long userId = userRepository.findByUser(email).get().getId();
        List<Apartament> apartamente = apartamentRepository.getApartamenteByUser(userId);
        List<ApartamentLocatarDto> apartamenteDtoList = new ArrayList<>();
        for (Apartament apartament: apartamente
        ) {
            ApartamentLocatarDto apartamentDto = new ApartamentLocatarDto();
            apartamentDto.setId(apartament.getId());
            apartamentDto.setNumar(apartament.getNumar_apartament());
            apartamentDto.setSuprafataMp(apartament.getSuprafata_mp());
            apartamentDto.setNumarLocatari(apartament.getNumar_locatari());
            apartamentDto.setUserId(apartament.getUser().getId());
            apartamentDto.setNume(apartament.getUser().getNume());
            apartamentDto.setPrenume(apartament.getUser().getPrenume());
            apartamentDto.setTelefon(apartament.getUser().getTelefon());
            apartamentDto.setEmail(apartament.getUser().getUser());
            apartamentDto.setStrada(apartament.getAsociatie().getStrada());
            apartamenteDtoList.add(apartamentDto);
        }
        return apartamenteDtoList;
    }

    public ApartamentLocatarDto getApartament(OAuth2Authentication auth2Authentication, Long apartamentId)  throws ResponseStatusException {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        Long userId = userRepository.findByUser(email).get().getId();
        Optional<Apartament> apartament = apartamentRepository.getApartamentByUserAndId(userId, apartamentId);
        if(apartament.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "nu aveti access la acest apartament");
        ApartamentLocatarDto apartamentLocatarDto = new ApartamentLocatarDto();
        apartamentLocatarDto.setAsociatieId(apartament.get().getAsociatie().getId());
        apartamentLocatarDto.setStrada(apartament.get().getAsociatie().getStrada());
        System.out.println(apartamentLocatarDto.getStrada());
        return apartamentLocatarDto;
    }
}
