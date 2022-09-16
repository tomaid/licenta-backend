package com.licenta.licenta.service;

import com.licenta.licenta.Security.UserPrinciple;
import com.licenta.licenta.dto.ContorIndexDto;
import com.licenta.licenta.dto.ContorIndexExtendedDto;
import com.licenta.licenta.dto.IndexDto;
import com.licenta.licenta.model.*;
import com.licenta.licenta.repository.ApartamentRepository;
import com.licenta.licenta.repository.ContorRepository;
import com.licenta.licenta.repository.IndexRepository;
import com.licenta.licenta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class IndexService {
    @Autowired
    private IndexRepository indexRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContorRepository contorRepository;
    @Autowired
    private ApartamentRepository apartamentRepository;
    Mesaj mesaj = new Mesaj();
    public ResponseEntity<Object> introducereIndex(OAuth2Authentication auth2Authentication, IndexDto indexDto, Long apartamentId, Long asocId, Long servId, Long contorId) {
        if(indexDto.getValoareIndex()<=0){
            mesaj.setMessage("Introduceti indexul");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(apartamentId.toString().isEmpty()){
            mesaj.setMessage("Introduceti numarul apartamentului");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(asocId.toString().isEmpty()){
            mesaj.setMessage("Selectati asociatia");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(servId.toString().isEmpty()){
            mesaj.setMessage("Alegeti serviciul");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(contorId.toString().isEmpty()){
            mesaj.setMessage("Alegeti contorul");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Optional<Contor> contor = contorRepository.getContoareByAsociatieAndServiciuAndAdminAndContor(asocId, servId, user.getId(),contorId);
        if(contor.isEmpty()){
            mesaj.setMessage("Contorul nu exista");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }

       Optional<Index> index = indexRepository.getSingleIndexByUserAndApartamentAndServiciuAndContor(user.getId(),apartamentId,servId,contorId);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date lunaPrecedenta = cal.getTime();
        Optional<Index> contorGetPrecedent =indexRepository.getIndexbyContorAndDate(contorId,lunaPrecedenta);
        if(contorGetPrecedent.isPresent()){
            if(contorGetPrecedent.get().getValoare_index()> indexDto.getValoareIndex()){
                mesaj.setMessage("Indexul introdus este mai mic decat indexul introdus anterior.");
                mesaj.setCode(406);
                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
            }
        }

        if(index.isEmpty()){
            Index newIndex = new Index();
            newIndex.setContor(contor.get());
            newIndex.setData_citire(new Date());
            if(user.getRole().getId()==1)newIndex.setAutocitit(true);
            newIndex.setValoare_index(indexDto.getValoareIndex());
            indexRepository.save(newIndex);
            mesaj.setMessage("Indexul a fost introdus");
            mesaj.setCode(201);
            return new ResponseEntity<>(mesaj, HttpStatus.CREATED);

        }else {
            if(!index.get().isAutocitit() || user.getRole().getId()==1){
                if(user.getRole().getId()==1)index.get().setAutocitit(true);
                index.get().setValoare_index(indexDto.getValoareIndex());
                indexRepository.save(index.get());
                mesaj.setMessage("Indexul a fost actualizat");
                mesaj.setCode(200);
                return new ResponseEntity<>(mesaj, HttpStatus.OK);
            }
        }
        mesaj.setMessage("A aparut o eroare.");
        mesaj.setCode(406);
        return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
    }

    public List<IndexDto> getAll(OAuth2Authentication auth2Authentication, Long apartamentId, Long asocId, Long servId, Long contorId) throws ResponseStatusException {
        if(apartamentId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Introduceti numarul apartamentului");
        if(asocId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Selectati asociatia");
        if(servId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Alegeti serviciul");
        if(contorId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Alegeti contorul");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Optional<Contor> contor = contorRepository.getContoareByAsociatieAndServiciuAndAdminAndContor(asocId, servId, user.getId(),contorId);
        if(contor.get().getId().toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Contorul nu exista");
        List<IndexDto> indecsiDto = new ArrayList<>();
        List<Index> indecsi = indexRepository.getIndexByUserAndApartamentAndServiciuAndContor(user.getId(),apartamentId,servId, contorId);
        for (Index index: indecsi
             ) {
            IndexDto indexDto = new IndexDto();
            indexDto.setValoareIndex(index.getValoare_index());
            indexDto.setId(index.getId());
            indexDto.setDataCitire(index.getData_citire());
            indecsiDto.add(indexDto);
        }
        return indecsiDto;
    }

    public List<IndexDto> getIndexAllNoApt(OAuth2Authentication auth2Authentication, Long asocId, Long servId, Long contorId) throws ResponseStatusException{
        if(asocId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Selectati asociatia");
        if(servId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Alegeti serviciul");
        if(contorId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Alegeti contorul");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Optional<Contor> contor = contorRepository.getContoareByAsociatieAndServiciuAndAdminAndContor(asocId, servId, user.getId(),contorId);
        if(contor.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Contorul nu exista");
        List<IndexDto> indecsiDto = new ArrayList<>();
        List<Index> indecsi = indexRepository.getIndexByUserAndApartamentAndServiciuAndContor(user.getId(),servId, contorId);
        double valoareAnterioara = 0;
        int valIndex=0;
        Long valId=0L;
        Date valDate=new Date();
        double valcitireIndex=0;
        for (Index index: indecsi) {
            if(valIndex>0) {
                IndexDto indexDto = new IndexDto();
                indexDto.setValoareIndex(valcitireIndex);
                indexDto.setId(valId);
                indexDto.setDataCitire(valDate);
                indexDto.setConsum(scadere(index.getValoare_index(), valoareAnterioara));
                indecsiDto.add(indexDto);
            }
            else{
                if(indecsi.size()==1){
                    IndexDto indexDto = new IndexDto();
                    indexDto.setValoareIndex(index.getValoare_index());
                    indexDto.setId(index.getId());
                    indexDto.setDataCitire(index.getData_citire());
                    indexDto.setConsum(scadere(index.getValoare_index(), valoareAnterioara));
                    indecsiDto.add(indexDto);
                }
            }
            valId=index.getId();
            valDate=index.getData_citire();
            valcitireIndex = index.getValoare_index();
            valoareAnterioara = index.getValoare_index();
            valIndex++;
        }
        return indecsiDto;
    }

    double scadere(double valoareCurenta, double valoareAnterioara) {
        if (valoareAnterioara==0){
            return 0;
        }
        else{
            return valoareAnterioara-valoareCurenta;
        }
    }

    public List<ContorIndexDto> getIndexNoService(OAuth2Authentication auth2Authentication, Long apartamentId, Long asocId) throws ResponseStatusException {
        if(asocId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Selectati asociatia");
        if(apartamentId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Selectati apartamentul");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date lunaPrecedenta = cal.getTime();
        Date lunaCurenta = new Date();
        List<ContorIndexDto> contorIndexDtoList = new ArrayList<>();
        List<Contor> contoare = contorRepository.getContoareByAsociatieAndApartament(user.getId(),asocId, apartamentId);
        for (Contor contorin: contoare) {
            ContorIndexDto contorIndexDto = new ContorIndexDto();
            contorIndexDto.setNumeContor(contorin.getNume_contor());
            contorIndexDto.setIdServiciu(contorin.getServiciu().getId());
            contorIndexDto.setNumeServiciu(contorin.getServiciu().getNume_serviciu());
            contorIndexDto.setId(contorin.getId());

            Optional<Index> contorGetCurrent =indexRepository.getIndexbyContorAndDate(contorin.getId(),lunaCurenta);
            if(contorGetCurrent.isPresent()){
                contorIndexDto.setIndexCurent(contorGetCurrent.get().getValoare_index());
            } else {
                contorIndexDto.setIndexCurent(0);
            }
            Optional<Index> contorGetPrecedent =indexRepository.getIndexbyContorAndDate(contorin.getId(),lunaPrecedenta);
            if(contorGetPrecedent.isPresent()){
                contorIndexDto.setUltimulIndex(contorGetPrecedent.get().getValoare_index());
            } else {
                contorIndexDto.setUltimulIndex(0);
            }
            contorIndexDtoList.add(contorIndexDto);
        }
        return contorIndexDtoList;
    }

    public List<ContorIndexExtendedDto> getIndexNoServiceLocatar(OAuth2Authentication auth2Authentication, Long apartamentId) throws ResponseStatusException {
        if(apartamentId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Selectati apartamentul");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        if (apartamentRepository.findById(apartamentId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Apartamentul nu exista in baza de date");
        Apartament apartament = apartamentRepository.findById(apartamentId).get();
        if(apartament.getUser().getId()!=user.getId())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti proprietarul apartamentului");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date lunaPrecedenta = cal.getTime();
        Date lunaCurenta = new Date();
        List<ContorIndexExtendedDto> contorIndexDtoList = new ArrayList<>();
        List<Contor> contoare = contorRepository.getContoareByLocatarAndApartament(user.getId(), apartamentId);
        for (Contor contorin: contoare) {
            ContorIndexExtendedDto contorIndexDto = new ContorIndexExtendedDto();
            contorIndexDto.setNumeContor(contorin.getNume_contor());
            contorIndexDto.setIdServiciu(contorin.getServiciu().getId());
            contorIndexDto.setNumeServiciu(contorin.getServiciu().getNume_serviciu());
            contorIndexDto.setId(contorin.getId());

            Optional<Index> contorGetCurrent =indexRepository.getIndexbyContorAndDateAutocitit(contorin.getId(),lunaCurenta);
            if(contorGetCurrent.isPresent()){
                contorIndexDto.setIndexCurent(contorGetCurrent.get().getValoare_index());
                contorIndexDto.setAutoCitit(contorGetCurrent.get().isAutocitit());
            } else {
                contorIndexDto.setIndexCurent(0);
            }
            Optional<Index> contorGetPrecedent =indexRepository.getIndexbyContorAndDate(contorin.getId(),lunaPrecedenta);
            if(contorGetPrecedent.isPresent()){
                contorIndexDto.setUltimulIndex(contorGetPrecedent.get().getValoare_index());
            } else {
                contorIndexDto.setUltimulIndex(0);
            }
            contorIndexDtoList.add(contorIndexDto);
        }
        return contorIndexDtoList;
    }

    public ContorIndexDto getIndexByService(OAuth2Authentication auth2Authentication, Long asocId, Long serviciuId)  throws ResponseStatusException {
        if(asocId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Selectati asociatia");
        if(serviciuId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Selectati serviciul");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date lunaPrecedenta = cal.getTime();
        Date lunaCurenta = new Date();
        Optional<Contor> contor = contorRepository.getContoareByAsociatieAndServiciu(user.getId(),asocId, serviciuId);
        if(contor.isPresent()){
            ContorIndexDto contorIndexDto = new ContorIndexDto();
            contorIndexDto.setIdServiciu(contor.get().getServiciu().getId());
            contorIndexDto.setNumeContor(contor.get().getNume_contor());
            contorIndexDto.setId(contor.get().getId());
            contorIndexDto.setNumeServiciu(contor.get().getServiciu().getNume_serviciu());
            Optional<Index> contorGetCurrent =indexRepository.getIndexbyContorAndDate(contor.get().getId(),lunaCurenta);
            if(contorGetCurrent.isPresent()){
                contorIndexDto.setIndexCurent(contorGetCurrent.get().getValoare_index());
            } else {
                contorIndexDto.setIndexCurent(0);
            }
            Optional<Index> contorGetPrecedent =indexRepository.getIndexbyContorAndDate(contor.get().getId(),lunaPrecedenta);
            if(contorGetPrecedent.isPresent()){
                contorIndexDto.setUltimulIndex(contorGetPrecedent.get().getValoare_index());
            } else {
                contorIndexDto.setUltimulIndex(0);
            }
            return contorIndexDto;

        }
        else {throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "A aparut o eroare");
        }
    }

    public ResponseEntity<Object> introducereIndexGeneral(OAuth2Authentication auth2Authentication, IndexDto indexDto, Long asocId, Long servId, Long contorId) {
            if(indexDto.getValoareIndex()<=0){
                mesaj.setMessage("Introduceti indexul");
                mesaj.setCode(406);
                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
            }
            if(asocId.toString().isEmpty()){
                mesaj.setMessage("Selectati asociatia");
                mesaj.setCode(406);
                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
            }
            if(servId.toString().isEmpty()){
                mesaj.setMessage("Alegeti serviciul");
                mesaj.setCode(406);
                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
            }
            if(contorId.toString().isEmpty()){
                mesaj.setMessage("Alegeti contorul");
                mesaj.setCode(406);
                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
            }
            UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
            User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date lunaPrecedenta = cal.getTime();
        Optional<Index> contorGetPrecedent =indexRepository.getIndexbyContorAndDate(contorId,lunaPrecedenta);
        if(contorGetPrecedent.isPresent()){
            if(contorGetPrecedent.get().getValoare_index()> indexDto.getValoareIndex()){
                mesaj.setMessage("Indexul introdus este mai mic decat indexul introdus anterior.");
                mesaj.setCode(406);
                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        Optional<Contor> contor = contorRepository.getContoareByAsociatieAndServiciuAndAdminAndContor(asocId, servId, user.getId(),contorId);
            if(contor.isEmpty()){
                mesaj.setMessage("Contorul nu exista");
                mesaj.setCode(406);
                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
            }
            Optional<Index> index = indexRepository.getGeneralIndexByUserAndAndServiciuAndContor(user.getId(),servId,contorId);
            if(index.isEmpty()){
                Index newIndex = new Index();
                newIndex.setContor(contor.get());
                newIndex.setData_citire(new Date());
                if(user.getRole().getId()==1)newIndex.setAutocitit(true);
                newIndex.setValoare_index(indexDto.getValoareIndex());
                indexRepository.save(newIndex);
                mesaj.setMessage("Indexul a fost introdus");
                mesaj.setCode(201);
                return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
            }else {
                if(!index.get().isAutocitit() || user.getRole().getId()==1){
                    if(user.getRole().getId()==1)index.get().setAutocitit(true);
                    index.get().setValoare_index(indexDto.getValoareIndex());
                    indexRepository.save(index.get());
                    mesaj.setMessage("Indexul a fost actualizat");
                    mesaj.setCode(201);
                    return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
                }
            }
            mesaj.setMessage("A aparut o eroare.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }

    public ResponseEntity<Object> introducereIndexLocatar(OAuth2Authentication auth2Authentication, IndexDto indexDto, Long apartamentId, Long servId, Long contorId) {

        if(indexDto.getValoareIndex()<=0){
            mesaj.setMessage("Introduceti indexul");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(apartamentId.toString().isEmpty()){
            mesaj.setMessage("Introduceti numarul apartamentului");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if (apartamentRepository.findById(apartamentId).isEmpty()) {
            mesaj.setMessage("Apartamentul nu exista in baza de date.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        Apartament apartament = apartamentRepository.findById(apartamentId).get();
        if(servId.toString().isEmpty()){
            mesaj.setMessage("Alegeti serviciul");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(contorId.toString().isEmpty()){
            mesaj.setMessage("Alegeti contorul");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        if(apartament.getUser().getId()!=user.getId()){
            mesaj.setMessage("Nu sunteti detinatorul apartamentului");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        Optional<Contor> contor = contorRepository.getContoareByAsociatieAndServiciuAndLocatarAndContor(apartamentId, servId, user.getId(),contorId);
        if(contor.isEmpty()){
            mesaj.setMessage("Contorul nu exista");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        Optional<Index> index = indexRepository.getSingleIndexByUserAndApartamentAndServiciuAndContor(user.getId(),apartamentId,servId,contorId);
        if(index.isPresent()){
            if(index.get().isAutocitit()){
                mesaj.setMessage("Indexul a fost autocitit de administrator");
                mesaj.setCode(406);
                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date lunaPrecedenta = cal.getTime();
        Optional<Index> contorGetPrecedent =indexRepository.getIndexbyContorAndDate(contorId,lunaPrecedenta);
        if(contorGetPrecedent.isPresent()){
            if(contorGetPrecedent.get().getValoare_index()> indexDto.getValoareIndex()){
                mesaj.setMessage("Indexul introdus este mai mic decat indexul introdus anterior.");
                mesaj.setCode(406);
                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
            }
        }

        if(index.isEmpty()){
            Index newIndex = new Index();
            newIndex.setContor(contor.get());
            newIndex.setData_citire(new Date());
            newIndex.setAutocitit(false);
            newIndex.setValoare_index(indexDto.getValoareIndex());
            indexRepository.save(newIndex);
            mesaj.setMessage("Indexul a fost introdus");
            mesaj.setCode(201);
            return new ResponseEntity<>(mesaj, HttpStatus.CREATED);

        }else {
                index.get().setAutocitit(false);
                index.get().setValoare_index(indexDto.getValoareIndex());
                indexRepository.save(index.get());
                mesaj.setMessage("Indexul a fost actualizat");
                mesaj.setCode(200);
                return new ResponseEntity<>(mesaj, HttpStatus.OK);
        }
    }
    public List<IndexDto> getIndexAllLocatar(OAuth2Authentication auth2Authentication, Long apartamentId, Long servId, Long contorId) {
        if(apartamentId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Introduceti numarul apartamentului");
        if(servId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Alegeti serviciul");
        if(contorId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Alegeti contorul");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Optional<Contor> contor = contorRepository.getContoareByAsociatieAndServiciuAndLocatarAndContor(apartamentId, servId, user.getId(),contorId);
        if(contor.get().getId().toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Contorul nu exista");
        List<IndexDto> indecsiDto = new ArrayList<>();
        List<Index> indecsi = indexRepository.getIndexByUserLocatarAndApartamentAndServiciuAndContor(user.getId(),apartamentId,servId, contorId);
        for (Index index: indecsi
        ) {
            IndexDto indexDto = new IndexDto();
            indexDto.setValoareIndex(index.getValoare_index());
            indexDto.setId(index.getId());
            indexDto.setDataCitire(index.getData_citire());
            indecsiDto.add(indexDto);
        }
        return indecsiDto;
    }

    public List<IndexDto> getIndexAllApt(OAuth2Authentication auth2Authentication, Long apartamentId, Long servId, Long contorId) {
        if(apartamentId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Selectati asociatia");
        if(servId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Alegeti serviciul");
        if(contorId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Alegeti contorul");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Optional<Contor> contor = contorRepository.getContoareByAsociatieAndServiciuAndLocatarAndContor(apartamentId, servId, user.getId(),contorId);
        if(contor.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Contorul nu exista");
        List<IndexDto> indecsiDto = new ArrayList<>();
        List<Index> indecsi = indexRepository.getIndexByUserLocatarAndApartamentAndServiciuAndContor(user.getId(),apartamentId, servId, contorId);
        double valoareAnterioara = 0;
        int valIndex=0;
        Long valId=0L;
        Date valDate=new Date();
        double valcitireIndex=0;
        for (Index index: indecsi) {
            if(valIndex>0) {
                IndexDto indexDto = new IndexDto();
                indexDto.setValoareIndex(valcitireIndex);
                indexDto.setId(valId);
                indexDto.setDataCitire(valDate);
                indexDto.setConsum(scadere(index.getValoare_index(), valoareAnterioara));
                indecsiDto.add(indexDto);
            }
            else{
                if(indecsi.size()==1){
                    IndexDto indexDto = new IndexDto();
                    indexDto.setValoareIndex(index.getValoare_index());
                    indexDto.setId(index.getId());
                    indexDto.setDataCitire(index.getData_citire());
                    indexDto.setConsum(scadere(index.getValoare_index(), valoareAnterioara));
                    indecsiDto.add(indexDto);
                }
            }
            valId=index.getId();
            valDate=index.getData_citire();
            valcitireIndex = index.getValoare_index();
            valoareAnterioara = index.getValoare_index();
            valIndex++;
        }
        return indecsiDto;
    }
}
