package com.licenta.licenta.service;

import com.licenta.licenta.Security.UserPrinciple;
import com.licenta.licenta.dto.AsociatieCreareDto;
import com.licenta.licenta.dto.AsociatieDetaliiCuJudetDto;
import com.licenta.licenta.dto.AsociatieDetaliiDto;
import com.licenta.licenta.dto.AsociatieDto;
import com.licenta.licenta.model.*;
import com.licenta.licenta.repository.ApartamentRepository;
import com.licenta.licenta.repository.AsociatieRepository;
import com.licenta.licenta.repository.LocalitateRepository;
import com.licenta.licenta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AsociatieService {
    @Autowired
    private AsociatieRepository asociatieRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocalitateRepository localitateRepository;
    @Autowired
    private ApartamentRepository apartamentRepository;
    Mesaj mesaj = new Mesaj();

    public ResponseEntity<Object> creareAsociatie(OAuth2Authentication auth2Authentication, AsociatieCreareDto asociatieCreareDto) {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        Asociatie asociatie = new Asociatie();
        String email = userPrinciple.getUsername();
        User user = userRepository.findByUser(email).get();
//        if(!user.getRole().getNume().equals("ADMIN")){
//            return new ResponseEntity<>("Nu aveti rol de admin", HttpStatus.NOT_ACCEPTABLE);
//        }
        String numeAsociatie = asociatieCreareDto.getNume();
        Long cif = asociatieCreareDto.getCif();
        String autorizatie = asociatieCreareDto.getAutorizatie();
        String strada = asociatieCreareDto.getStrada();
        String numar = asociatieCreareDto.getNumar();
        String bloc = asociatieCreareDto.getBloc();
        String scara = asociatieCreareDto.getScara();
        Long localitateId = asociatieCreareDto.getLocalitate();

        if(numeAsociatie.isBlank()){
            mesaj.setMessage("Introduceti numele asociatiei");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(strada.isBlank()){
            mesaj.setMessage("Introduceti adresa");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(autorizatie.isBlank()){
            mesaj.setMessage("Introduceti autorizatia");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cif.toString().isBlank()){
            mesaj.setMessage("Introduceti codul de identificare fiscal");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(numar.isBlank()){
            mesaj.setMessage("Introduceti numarul strazii");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(bloc.isBlank()){
            mesaj.setMessage("Introduceti blocul");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);        }
        if(scara.isBlank()){
            mesaj.setMessage("Introduceti scara");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(localitateId.toString().isBlank()){
            mesaj.setMessage("Alegeti localitatea in care se afla asociatia de locatari");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        asociatie.setNume(numeAsociatie);
        asociatie.setAutorizatie(autorizatie);
        asociatie.setCif(cif);
        asociatie.setStrada(strada);
        asociatie.setNumar(numar);
        asociatie.setScara(scara);
        asociatie.setBloc(bloc);
        Localitate optionalLocalitate = localitateRepository.findById(localitateId).get();
        asociatie.setLocalitate(optionalLocalitate);
        asociatie.addAdminToAsociatie(user);
        asociatieRepository.save(asociatie);
        mesaj.setMessage("Asociatia a fost creata");
        mesaj.setAsociatie(asociatie.getId());
        mesaj.setCode(201);
        if(user.getRole().getId() == 2){
        user.setRole(new Role(1L));
        userRepository.save(user);}
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
//        updatedAuthorities.set() //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]
//        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
//        SecurityContextHolder.getContext().setAuthentication(newAuth);
//        System.out.println(newAuth);
        return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
       // return new ResponseEntity<>("Asociatia a fost creata", HttpStatus.CREATED);
    }

    public List<AsociatieDto> getAll(OAuth2Authentication auth2Authentication) {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        Optional<User> user = userRepository.findByUser(email);
        List<AsociatieDto> asociatiiDtoList = new ArrayList<>();
        Set<Asociatie> asociatii = user.get().getAdminAsociatii();
        for (Asociatie asociatie: asociatii
             ) {
            AsociatieDto asociatieDto = new AsociatieDto();
            asociatieDto.setId(asociatie.getId());
            asociatieDto.setNume(asociatie.getNume());
            asociatiiDtoList.add(asociatieDto);
        }
        return asociatiiDtoList;
    }

    public AsociatieDetaliiDto getAsociatie(OAuth2Authentication auth2Authentication, Long id) throws ResponseStatusException {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        Optional<User> user = userRepository.findByUser(email);
        Optional<Asociatie> asociatie = asociatieRepository.findById(id);
        if(user.isPresent()&&asociatie.isPresent()&&user.get().findAdminFromAsociatie(asociatie.get())){
            AsociatieDetaliiDto asociatieDetaliiDto = new AsociatieDetaliiDto();
            asociatieDetaliiDto.setAutorizatie(asociatie.get().getAutorizatie());
            asociatieDetaliiDto.setBloc(asociatie.get().getBloc());
            asociatieDetaliiDto.setCif(asociatie.get().getCif());
            asociatieDetaliiDto.setNume(asociatie.get().getNume());
            asociatieDetaliiDto.setLocalitate(asociatie.get().getLocalitate().getId());
            asociatieDetaliiDto.setNumar(asociatie.get().getNumar());
            asociatieDetaliiDto.setScara(asociatie.get().getScara());
            asociatieDetaliiDto.setStrada(asociatie.get().getStrada());
            asociatieDetaliiDto.setJudet(asociatie.get().getLocalitate().getJudet().getId());
            return asociatieDetaliiDto;
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406"); // inseamna ca nu exista asociatie asociata utilizatorului care face query
    }

    public ResponseEntity<Object> actualizareAsociatie(OAuth2Authentication auth2Authentication, Long id, AsociatieCreareDto asociatieCreareDto) {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        Optional<User> user = userRepository.findByUser(email);
        Optional<Asociatie> asociatie = asociatieRepository.findById(id);
        if(user.isPresent()&&asociatie.isPresent()&&user.get().findAdminFromAsociatie(asociatie.get())){

            String numeAsociatie = asociatieCreareDto.getNume();
            Long cif = asociatieCreareDto.getCif();
            String autorizatie = asociatieCreareDto.getAutorizatie();
            String strada = asociatieCreareDto.getStrada();
            String numar = asociatieCreareDto.getNumar();
            String bloc = asociatieCreareDto.getBloc();
            String scara = asociatieCreareDto.getScara();
            Long localitateId = asociatieCreareDto.getLocalitate();
            if(!numeAsociatie.isBlank()) {
                asociatie.get().setNume(numeAsociatie);
            }
            if(!cif.toString().isBlank()) {
                asociatie.get().setCif(cif);
            }
            if(!autorizatie.isBlank()) {
                asociatie.get().setAutorizatie(autorizatie);
            }
            if(!strada.isBlank()) {
                asociatie.get().setStrada(strada);
            }
            if(!numar.isBlank()) {
                asociatie.get().setNumar(numar);
            }
            if(!bloc.isBlank()) {
                asociatie.get().setBloc(bloc);
            }
            if(!scara.isBlank()) {
                asociatie.get().setScara(scara);
            }
            if(!localitateId.toString().isBlank()) {
                asociatie.get().setLocalitate(new Localitate(localitateId,""));
            }
            asociatieRepository.save(asociatie.get());
            mesaj.setMessage("Datele asociatiei au fost modificate.");
            mesaj.setAsociatie(id);
            mesaj.setCode(200);

            return new ResponseEntity<>(mesaj, HttpStatus.OK);
        }

        mesaj.setMessage("A aparut o eroare neprevazuta");
        mesaj.setAsociatie(id);
        mesaj.setCode(406);
        return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
    }

    public AsociatieDetaliiCuJudetDto getAsociatieFactura(OAuth2Authentication auth2Authentication, Long id) throws ResponseStatusException {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        Optional<User> user = userRepository.findByUser(email);
        Optional<Asociatie> asociatie = asociatieRepository.findById(id);
        if(user.isPresent()&&asociatie.isPresent()&&user.get().findAdminFromAsociatie(asociatie.get())){
            AsociatieDetaliiCuJudetDto asociatieDetaliiCuJudetDto = new AsociatieDetaliiCuJudetDto();
            asociatieDetaliiCuJudetDto.setAutorizatie(asociatie.get().getAutorizatie());
            asociatieDetaliiCuJudetDto.setBloc(asociatie.get().getBloc());
            asociatieDetaliiCuJudetDto.setCif(asociatie.get().getCif());
            asociatieDetaliiCuJudetDto.setNume(asociatie.get().getNume());
            asociatieDetaliiCuJudetDto.setLocalitate(asociatie.get().getLocalitate().getNume());
            asociatieDetaliiCuJudetDto.setNumar(asociatie.get().getNumar());
            asociatieDetaliiCuJudetDto.setScara(asociatie.get().getScara());
            asociatieDetaliiCuJudetDto.setStrada(asociatie.get().getStrada());
            asociatieDetaliiCuJudetDto.setJudet(asociatie.get().getLocalitate().getJudet().getNume());
            return asociatieDetaliiCuJudetDto;
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406"); // inseamna ca nu exista asociatie asociata utilizatorului care face query
    }

    public AsociatieDetaliiCuJudetDto getDateFacturaPentruLocatar(OAuth2Authentication auth2Authentication, Long apartamentId) throws ResponseStatusException {
        if (apartamentId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus un apartament");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        Optional<User> user = userRepository.findByUser(email);
        Apartament apartament1 = apartamentRepository.findById(apartamentId).get();
        if (apartament1.getUser().getId()!=userRepository.findByUser(email).get().getId())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu aveti access la acest apartament");
        Long asocId = apartament1.getAsociatie().getId();
        Optional<Asociatie> asociatie = asociatieRepository.findById(asocId);
        if(user.isPresent()&&asociatie.isPresent()){
            AsociatieDetaliiCuJudetDto asociatieDetaliiCuJudetDto = new AsociatieDetaliiCuJudetDto();
            asociatieDetaliiCuJudetDto.setAutorizatie(asociatie.get().getAutorizatie());
            asociatieDetaliiCuJudetDto.setBloc(asociatie.get().getBloc());
            asociatieDetaliiCuJudetDto.setCif(asociatie.get().getCif());
            asociatieDetaliiCuJudetDto.setNume(asociatie.get().getNume());
            asociatieDetaliiCuJudetDto.setLocalitate(asociatie.get().getLocalitate().getNume());
            asociatieDetaliiCuJudetDto.setNumar(asociatie.get().getNumar());
            asociatieDetaliiCuJudetDto.setScara(asociatie.get().getScara());
            asociatieDetaliiCuJudetDto.setStrada(asociatie.get().getStrada());
            asociatieDetaliiCuJudetDto.setJudet(asociatie.get().getLocalitate().getJudet().getNume());
            return asociatieDetaliiCuJudetDto;
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406"); // inseamna ca nu exista asociatie asociata utilizatorului care face query

    }
}
