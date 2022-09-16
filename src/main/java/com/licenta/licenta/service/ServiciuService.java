package com.licenta.licenta.service;

import com.licenta.licenta.Security.UserPrinciple;
import com.licenta.licenta.dto.ServiciuDto;
import com.licenta.licenta.model.Asociatie;
import com.licenta.licenta.model.IstoricPret;
import com.licenta.licenta.model.Mesaj;
import com.licenta.licenta.model.Serviciu;
import com.licenta.licenta.repository.AsociatieRepository;
import com.licenta.licenta.repository.IstoricPretRepository;
import com.licenta.licenta.repository.ServiciuRepository;
import com.licenta.licenta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ServiciuService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AsociatieRepository asociatieRepository;
    @Autowired
    private ServiciuRepository serviciuRepository;
    @Autowired
    private IstoricPretRepository istoricPretRepository;
    Mesaj mesaj = new Mesaj();

    public ResponseEntity<Object> createServiciu(OAuth2Authentication auth2Authentication, ServiciuDto serviciuDto) {
        if(serviciuDto.getAsociatie_id().toString().isBlank()){
            mesaj.setMessage("Asociatia nu exista");
            mesaj.setAsociatie(serviciuDto.getAsociatie_id());
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(serviciuDto.getPret()<0){
            mesaj.setMessage("Introduceti pretul");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(serviciuDto.getNume().isBlank()){
            mesaj.setMessage("Introduceti numele");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        Asociatie asociatie = asociatieRepository.findById(serviciuDto.getAsociatie_id()).get();
        String email = userPrinciple.getUsername();
        if(userRepository.findByUser(email).get().findAdminFromAsociatie(asociatie)){
            Serviciu serviciu = new Serviciu();
            serviciu.setNume_serviciu(serviciuDto.getNume());
            serviciu.setPret_serviciu(serviciuDto.getPret());
            serviciu.setAsociatie(asociatie);
            serviciuRepository.save(serviciu);
        }
        this.setIstoricPretRepository(serviciuDto.getPret(), serviciuRepository.findTopByIdDesc().get());
        mesaj.setMessage("Serviciul a fost creat.");
        mesaj.setCode(201);
        return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
    }

    public List<ServiciuDto> getAll(OAuth2Authentication auth2Authentication, Long id) throws ResponseStatusException {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        Asociatie asociatie = asociatieRepository.findById(id).get();
        String email = userPrinciple.getUsername();
        if(!userRepository.findByUser(email).get().findAdminFromAsociatie(asociatie)){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        }
        Long userId = userRepository.findByUser(email).get().getId();
        List<Serviciu> servicii = serviciuRepository.getServiciiByUserAndAsociatie(userId, id);
        List<ServiciuDto> serviciiDto = new ArrayList<>();
        for (Serviciu serviciu:  servicii
             ) {
            ServiciuDto serviciuDto = new ServiciuDto();
            serviciuDto.setId(serviciu.getId());
            serviciuDto.setNume(serviciu.getNume_serviciu());
            serviciuDto.setPret(serviciu.getPret_serviciu());
            serviciiDto.add(serviciuDto);
        }
        return serviciiDto;
    }

    public ResponseEntity<Object> actualizareServiciu(OAuth2Authentication auth2Authentication, Long asocId, Long servId, ServiciuDto serviciuDto) {
        if(serviciuDto.getAsociatie_id().toString().isBlank()){
            mesaj.setMessage("Asociatia nu exista.");
            mesaj.setAsociatie(serviciuDto.getAsociatie_id());
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(serviciuDto.getPret()<0){
            mesaj.setMessage("Introduceti pretul.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(serviciuDto.getNume().isBlank()){
            mesaj.setMessage("Introduceti numele.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(serviciuDto.getId().toString().isBlank()){
            mesaj.setMessage("Introduceti id-ul serviciului.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        Asociatie asociatie = asociatieRepository.findById(serviciuDto.getAsociatie_id()).get();
        String email = userPrinciple.getUsername();
        Optional<Serviciu> serviciu = serviciuRepository.findById(serviciuDto.getId());
        if((userRepository.findByUser(email).get().findAdminFromAsociatie(asociatie))&&(serviciu.get().findServiciuByAsociatie(asociatie))){
            serviciu.get().setNume_serviciu(serviciuDto.getNume());
            serviciu.get().setPret_serviciu(serviciuDto.getPret());
            serviciuRepository.save(serviciu.get());
            Optional<IstoricPret> istoricPret = istoricPretRepository.getByDateAndServiciu(serviciuDto.getId(), new Timestamp(System.currentTimeMillis()));
            if(istoricPret.isPresent()){
                if(istoricPret.get().getPret()!=serviciuDto.getPret()){
                    this.setIstoricPretRepository(serviciuDto.getPret(), serviciu.get());
                }
            } else {
                this.setIstoricPretRepository(serviciuDto.getPret(), serviciu.get());
            }
            mesaj.setMessage("Serviciul a fost actualizat.");
            mesaj.setCode(200);
            return new ResponseEntity<>(mesaj, HttpStatus.OK);
        }
        mesaj.setMessage("Neautorizat");
        mesaj.setCode(401);
        return new ResponseEntity<>(mesaj, HttpStatus.UNAUTHORIZED);
    }
    public ResponseEntity<Object> stergereServiciu(OAuth2Authentication auth2Authentication, Long asocId, Long servId) {
        if(asocId.toString().isBlank()){
            mesaj.setMessage("Asociatia nu exista.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(servId.toString().isBlank()){
            mesaj.setMessage("Serviciul nu exista.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }

        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        String email = userPrinciple.getUsername();
        Optional<Serviciu> serviciu = serviciuRepository.findById(servId);
        if((userRepository.findByUser(email).get().findAdminFromAsociatie(asociatie))&&(serviciu.get().findServiciuByAsociatie(asociatie))){
            serviciuRepository.deleteById(servId);
            mesaj.setMessage("Serviciul a fost sters.");
            mesaj.setCode(200);
            return new ResponseEntity<>(mesaj, HttpStatus.OK);
        }
        mesaj.setMessage("Neautorizat");
        mesaj.setCode(401);
        return new ResponseEntity<>(mesaj, HttpStatus.UNAUTHORIZED);
    }

    private void setIstoricPretRepository(double pret, Serviciu serviciu){
        IstoricPret istoricPrets = new IstoricPret();
        istoricPrets.setPret(pret);
        istoricPrets.setServiciu(serviciu);
        istoricPrets.setData_pret(new Timestamp(System.currentTimeMillis()));
        istoricPretRepository.save(istoricPrets);
    }
}
