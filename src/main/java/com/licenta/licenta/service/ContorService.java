package com.licenta.licenta.service;

import com.licenta.licenta.Security.UserPrinciple;
import com.licenta.licenta.dto.ContorDto;
import com.licenta.licenta.model.*;
import com.licenta.licenta.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContorService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AsociatieRepository asociatieRepository;
    @Autowired
    private ApartamentRepository apartamentRepository;
    @Autowired
    private ServiciuRepository serviciuRepository;
    @Autowired
    private ContorRepository contorRepository;
    Mesaj mesaj = new Mesaj();
    public ResponseEntity<Object> createContor(OAuth2Authentication auth2Authentication, ContorDto contorDto, Long asocId, Long servId) {

        if((contorDto.getTip()==1||contorDto.getTip()==2)&&(contorDto.getNume().isEmpty())){
            mesaj.setMessage("Introduceti numele contorului");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if(asociatieRepository.findById(asocId).isEmpty()) {
            mesaj.setMessage("Nu sunteti admin pentru aceasta asociatie");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if(serviciuRepository.findById(servId).isEmpty())
            if(asociatieRepository.findById(asocId).isEmpty()) {
                mesaj.setMessage("Nu aveti acces la acest serviciu");
                mesaj.setCode(406);
                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
            }
        Serviciu serviciu = serviciuRepository.findById(servId).get();
        if(!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get()))
        {
            mesaj.setMessage("Nu sunteti admin pentru aceasta asociatie");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(!asociatie.getServiciuList().contains(serviciu)){
            mesaj.setMessage("Nu sunteti admin pentru aceasta asociatie");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(contorDto.getTip()==0){
            Long userId = userRepository.findByUser(email).get().getId();
            if(contorRepository.existsContorGeneralByAsociatieAndServiciuAndAdmin(asocId,servId,userId)){
                mesaj.setMessage("Exista un contor general");
                mesaj.setCode(406);
                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
            }
            Contor contor = new Contor();
            contor.setContor_general(true);
            contor.setNume_contor("Contor general");
            contor.setApartament(null);
            contor.setServiciu(serviciu);
            contorRepository.save(contor);
            mesaj.setMessage("Contorul general a fost creat.");
            mesaj.setCode(201);
            return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
        }
        if(contorDto.getTip()==1){
            if(apartamentRepository.findById(contorDto.getIdApartament()).isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu aveti acces la acest apartament");
            Apartament apartament = apartamentRepository.findById(contorDto.getIdApartament()).get();
            if(!asociatie.getApartamente().contains(apartament))
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu aveti acces la acest apartament");

            Contor contor = new Contor();
            contor.setContor_general(false);
            contor.setNume_contor(contorDto.getNume());
            contor.setApartament(apartament);
            contor.setServiciu(serviciu);
            contorRepository.save(contor);
            mesaj.setMessage("Contorul a fost creat.");
            mesaj.setCode(201);
            return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
        }
        if(contorDto.getTip()==2){
            for (Apartament apartament1 :asociatie.getApartamente()
            ) {
                Contor contor = new Contor();
                contor.setContor_general(false);
                contor.setNume_contor(contorDto.getNume());
                contor.setServiciu(serviciu);
                contor.setApartament(apartament1);
                contorRepository.save(contor);
            }
            mesaj.setMessage("Contoarele au fost create.");
            mesaj.setCode(201);
            return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
        }

        mesaj.setMessage("A aparut o eroare.");
        mesaj.setCode(406);
        return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
    }

    public List<ContorDto> getAll(OAuth2Authentication auth2Authentication, Long asocId, Long servId)  throws ResponseStatusException {
        if(asocId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: introduceti asociatia");
        if(servId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: introduceti serviciul");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if(asociatieRepository.findById(asocId).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if(!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 407: nu sunteti admin pentru aceasta asociatie");
        Long userId = userRepository.findByUser(email).get().getId();
        if(!asociatie.getServiciuList().contains(serviciuRepository.getById(servId)))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 408: nu sunteti admin pentru aceasta asociatie");
        List<Contor> contoare = contorRepository.getContoareByAsociatieAndServiciuAndAdmin(asocId,servId,userId);
        List<ContorDto> contoareDtoList = new ArrayList<>();
        for (Contor contor: contoare
        ) {
            ContorDto contorDto = new ContorDto();
            contorDto.setId(contor.getId());
            contorDto.setGeneral(contor.isContor_general());
            contorDto.setNume(contor.getNume_contor());
            if(contor.getApartament()!=null) {
                contorDto.setIdApartament(contor.getApartament().getId());
                contorDto.setNumeApartament(contor.getApartament().getNumar_apartament());
            }
            contoareDtoList.add(contorDto);
        }
        return contoareDtoList;
    }

    public ResponseEntity<Object> actualizareContor(OAuth2Authentication auth2Authentication, Long asocId, Long servId, Long contorId, ContorDto contorDto) {
        if(asocId.toString().isEmpty() || servId.toString().isEmpty() || contorId.toString().isEmpty()){
            mesaj.setMessage("A aparut o eroare.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        Long userId = userRepository.findByUser(email).get().getId();
        if(contorRepository.getContoareByAsociatieAndServiciuAndAdminAndContor(asocId, servId, userId, contorId).get().getId()==contorId)
        {
            Optional<Contor> contor=contorRepository.findById(contorId);
            if(contor.isPresent()) {
                if (!contor.get().isContor_general()){
                    contor.get().setNume_contor(contorDto.getNume());
                    contorRepository.save(contor.get());
                    mesaj.setMessage("Contorul a fost actualizat.");
                    mesaj.setCode(201);
                    return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
                }
            }
        }
        mesaj.setMessage("A aparut o eroare.");
        mesaj.setCode(406);
        return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);

    }

    public ResponseEntity<Object> stergereContor(OAuth2Authentication auth2Authentication, Long asocId, Long servId, Long contorId) {

        if(asocId.toString().isEmpty() || servId.toString().isEmpty() || contorId.toString().isEmpty()){
            mesaj.setMessage("A aparut o eroare.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        Long userId = userRepository.findByUser(email).get().getId();
        if(contorRepository.getContoareByAsociatieAndServiciuAndAdminAndContor(asocId, servId, userId, contorId).get().getId()==contorId)
        {
            contorRepository.deleteById(contorId);
            mesaj.setMessage("Contorul a fost sters.");
            mesaj.setCode(201);
            return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
        }
        mesaj.setMessage("A aparut o eroare.");
        mesaj.setCode(406);
        return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
    }
}
