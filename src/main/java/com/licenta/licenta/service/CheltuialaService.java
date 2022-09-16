package com.licenta.licenta.service;

import com.licenta.licenta.Security.UserPrinciple;
import com.licenta.licenta.dto.CheltuialaDto;
import com.licenta.licenta.dto.FormulaCalculCheltuieliDto;
import com.licenta.licenta.model.*;
import com.licenta.licenta.repository.AsociatieRepository;
import com.licenta.licenta.repository.CalculCheltuialaRepository;
import com.licenta.licenta.repository.CheltuialaRepository;
import com.licenta.licenta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class CheltuialaService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AsociatieRepository asociatieRepository;
    @Autowired
    private CheltuialaRepository cheltuialaRepository;
    @Autowired
            private CalculCheltuialaRepository calculCheltuialaRepository;
    Mesaj mesaj = new Mesaj();


    public ResponseEntity<Object> introducereCheltuiala(OAuth2Authentication auth2Authentication, CheltuialaDto cheltuialaDto, Long asocId) {
        if(asocId.toString().isEmpty()){
            mesaj.setMessage("Asociatia nu a fost selectata");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuialaDto.getNume_cheltuiala().isEmpty()){
            mesaj.setMessage("Introduceti denumirea facturii");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }

        if(cheltuialaDto.getData_introducere().before(firstDayOfMonth())){
            mesaj.setMessage("Data facturii trebuie sa fie din această lună");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuialaDto.getCalcul_id().toString().isEmpty()){
            mesaj.setMessage("Selectati formula de calcul pentru aceasta factura");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuialaDto.getSerie_factura().isEmpty()){
            mesaj.setMessage("Introduceti seria facturii");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuialaDto.getNumar_factura().toString().isEmpty()){
            mesaj.setMessage("Introduceti numarul facturii");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuialaDto.getSuma()<0){
            mesaj.setMessage("Introduceti suma facturii");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if(asociatieRepository.findById(asocId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if(!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        Cheltuiala cheltuiala = new Cheltuiala();
        cheltuiala.setCalculCheltuiala(new CalculCheltuiala(cheltuialaDto.getCalcul_id()));
        cheltuiala.setNume_cheltuiala(cheltuialaDto.getNume_cheltuiala());
        cheltuiala.setData_introducere(cheltuialaDto.getData_introducere());
        cheltuiala.setAsociatie(asociatie);
        cheltuiala.setNumar_factura(cheltuialaDto.getNumar_factura());
        cheltuiala.setSerie_factura(cheltuialaDto.getSerie_factura());
        cheltuiala.setSuma(cheltuialaDto.getSuma());
        cheltuialaRepository.save(cheltuiala);
        mesaj.setMessage("Factura a fost introdusa.");
        mesaj.setCode(201);
        return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> actualizareCheltuiala(OAuth2Authentication auth2Authentication, Long asocId, Long cheltuialaId, CheltuialaDto cheltuialaDto) {
        if(asocId.toString().isEmpty()){
            mesaj.setMessage("Introduceti asociatia");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuialaId.toString().isEmpty()){
            mesaj.setMessage("Introduceti id-ul");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuialaDto.getNume_cheltuiala().isEmpty()){
            mesaj.setMessage("Introduceti denumirea facturii");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuialaDto.getData_introducere().before(firstDayOfMonth())){
            mesaj.setMessage("Data facturii trebuie sa fie din această lună");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuialaDto.getCalcul_id().toString().isEmpty()){
            mesaj.setMessage("Selectati formula de calcul pentru aceasta factura");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuialaDto.getSerie_factura().isEmpty()){
            mesaj.setMessage("Introduceti seria facturii");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuialaDto.getNumar_factura().toString().isEmpty()){
            mesaj.setMessage("Introduceti numarul facturii");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuialaDto.getSuma()<0){
            mesaj.setMessage("Introduceti suma facturii");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if(asociatieRepository.findById(asocId).isEmpty()) {
            mesaj.setMessage("Nu exista asociatia");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if(!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get())){
            mesaj.setMessage("Nu sunteti admin pentru aceasta asociatie");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Optional<Cheltuiala> cheltuiala = cheltuialaRepository.findByIdAndUserAndAsociatie(cheltuialaId, asocId, user.getId());
        if(cheltuiala.isEmpty()){
            mesaj.setMessage("Nu exista factura");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        cheltuiala.get().setCalculCheltuiala(new CalculCheltuiala(cheltuialaDto.getCalcul_id()));
        cheltuiala.get().setNume_cheltuiala(cheltuialaDto.getNume_cheltuiala());
        cheltuiala.get().setAsociatie(asociatie);
        cheltuiala.get().setSerie_factura(cheltuialaDto.getSerie_factura());
        cheltuiala.get().setData_introducere(cheltuialaDto.getData_introducere());
        cheltuiala.get().setNumar_factura(cheltuialaDto.getNumar_factura());
        cheltuiala.get().setSuma(cheltuialaDto.getSuma());
        cheltuialaRepository.save(cheltuiala.get());
        mesaj.setMessage("Factura a fost modificata");
        mesaj.setCode(201);
        return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
    }

    public List<CheltuialaDto> getCheltuieli(OAuth2Authentication auth2Authentication, Long asocId) {
        if(asocId.toString().isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "nu ati introdus o asociatie");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if(asociatieRepository.findById(asocId).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if(!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        List<CheltuialaDto> cheltuialaDtoList = new ArrayList<>();
        List<Cheltuiala> cheltuialaList = cheltuialaRepository.findAllByUserAndAsociatie(asocId, user.getId());
        for (Cheltuiala cheltuiala: cheltuialaList
             ) {
            CheltuialaDto cheltuialaDto = new CheltuialaDto();
            cheltuialaDto.setNume_cheltuiala(cheltuiala.getNume_cheltuiala());
            cheltuialaDto.setId(cheltuiala.getId());
            cheltuialaDto.setData_introducere(cheltuiala.getData_introducere());
            cheltuialaDto.setSuma(cheltuiala.getSuma());
            cheltuialaDto.setNumar_factura(cheltuiala.getNumar_factura());
            cheltuialaDto.setSerie_factura(cheltuiala.getSerie_factura());
            cheltuialaDto.setCalcul_id(cheltuiala.getCalculCheltuiala().getId());
            cheltuialaDtoList.add(cheltuialaDto);
        }
        return cheltuialaDtoList;
    }
    public List<CheltuialaDto> getCheltuielibyData(OAuth2Authentication auth2Authentication, Long asocId, int luna, int an) {
        if(asocId.toString().isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "nu ati introdus o asociatie");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if(asociatieRepository.findById(asocId).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if(!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        List<CheltuialaDto> cheltuialaDtoList = new ArrayList<>();
        List<Cheltuiala> cheltuialaList = cheltuialaRepository.findAllByUserAndAsociatieAndData(asocId, user.getId(),firstDayOfMonth(luna, an), firstDayOfMonth(luna+1, an));
        for (Cheltuiala cheltuiala: cheltuialaList
        ) {
            CheltuialaDto cheltuialaDto = new CheltuialaDto();
            cheltuialaDto.setNume_cheltuiala(cheltuiala.getNume_cheltuiala());
            cheltuialaDto.setId(cheltuiala.getId());
            cheltuialaDto.setData_introducere(cheltuiala.getData_introducere());
            cheltuialaDto.setSuma(cheltuiala.getSuma());
            cheltuialaDto.setNumar_factura(cheltuiala.getNumar_factura());
            cheltuialaDto.setSerie_factura(cheltuiala.getSerie_factura());
            cheltuialaDto.setCalcul_id(cheltuiala.getCalculCheltuiala().getId());
            cheltuialaDtoList.add(cheltuialaDto);
        }
        return cheltuialaDtoList;
    }

    public ResponseEntity<Object> stergereCheltuiala(OAuth2Authentication auth2Authentication, Long asocId, Long cheltuialaId) {
        if(asocId.toString().isBlank()){
            mesaj.setMessage("Asociatia nu exista.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuialaId.toString().isBlank()){
            mesaj.setMessage("Factura nu exista.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        if(asociatieRepository.findById(asocId).isEmpty()) {
            mesaj.setMessage("Nu exista asociatia");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        String email = userPrinciple.getUsername();
        if(!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get())){
            mesaj.setMessage("Nu sunteti admin pentru aceasta asociatie");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Optional<Cheltuiala> cheltuiala = cheltuialaRepository.findByIdAndUserAndAsociatie(cheltuialaId, asocId, user.getId());
        if(cheltuiala.isEmpty()){
            mesaj.setMessage("Nu exista factura");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(cheltuiala.get().getData_introducere().before(firstDayOfMonth())){
            mesaj.setMessage("Puteti șterge facturi doar din luna curentă.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }

        cheltuialaRepository.deleteById(cheltuialaId);
        mesaj.setMessage("Factura a fost ștearsă.");
        mesaj.setCode(200);
        return new ResponseEntity<>(mesaj, HttpStatus.OK);
    }

    public List<FormulaCalculCheltuieliDto> getFormule(OAuth2Authentication auth2Authentication) {
    List<FormulaCalculCheltuieliDto> formulaCalculCheltuieliDtoList = new ArrayList<>();
    List<CalculCheltuiala> calculCheltuialaList = calculCheltuialaRepository.findAll();
        for (CalculCheltuiala calculcheltuiala: calculCheltuialaList) {
            FormulaCalculCheltuieliDto formulaCalculCheltuieliDto = new FormulaCalculCheltuieliDto();
            formulaCalculCheltuieliDto.setId(calculcheltuiala.getId());
            formulaCalculCheltuieliDto.setNume(calculcheltuiala.getNume());
            formulaCalculCheltuieliDtoList.add(formulaCalculCheltuieliDto);
        }
        return formulaCalculCheltuieliDtoList;
    }
    private static Date firstDayOfMonth() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTime();
    }
    public static Date firstDayOfMonth(int luna, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, luna-1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTime();
    }

    public double formule(int formula, double costTotal, double suprafataTotala, double suprafataApartament, long nrPersoaneTotal, int nrPersoaneApartament){
        if((costTotal<=0)||(suprafataApartament<=0)||(suprafataApartament<=0)||(nrPersoaneApartament<=0)||(nrPersoaneTotal<=0))  return 0;
        if(formula==1)return costTotal/nrPersoaneTotal*nrPersoaneApartament;
        if(formula==2)return costTotal/suprafataTotala*suprafataApartament;
        return 0;
    }
    public double formulaPierdere(double consumContorGeneral, double consumTotal, double consumIndividual){
        if ((consumIndividual <= 0) || (consumTotal <= 0) || (consumContorGeneral <= 0)) return 0;
        return (consumIndividual / consumTotal) * (consumContorGeneral - consumTotal);
    }
}
