package com.licenta.licenta.service;

import com.licenta.licenta.Security.UserPrinciple;
import com.licenta.licenta.dto.AvizierDto;
import com.licenta.licenta.dto.AvizierRandDto;
import com.licenta.licenta.dto.CheltuialaDto;
import com.licenta.licenta.dto.ServiciuDto;
import com.licenta.licenta.model.*;
import com.licenta.licenta.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class AvizierService {
    @Autowired
    private ApartamentRepository apartamentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AsociatieRepository asociatieRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    CheltuialaRepository cheltuialaRepository;
    @Autowired
    private PlataIntretinereRepository plataIntretinereRepository;
    @Autowired
    private ContorRepository contorRepository;
    @Autowired
    private IndexRepository indexRepository;
    @Autowired
    private ServiciuRepository serviciuRepository;
    @Autowired
    private CheltuialaService cheltuialaService;
    @Autowired
    private ChitantaRepository chitantaRepository;
    @Autowired
    private IstoricPretRepository istoricPretRepository;
    Mesaj mesaj = new Mesaj();
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public List<CheltuialaDto> getCheltuieli(OAuth2Authentication auth2Authentication, Long asocId, Integer anul, Integer luna) throws ResponseStatusException {
        if (asocId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus o asociatie");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (asociatieRepository.findById(asocId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if (!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get())) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, anul);
        cal.set(Calendar.MONTH, luna-1);
        Date lunaCurenta = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date lunaPrecedenta = cal.getTime();
        List<Cheltuiala> cheltuialaList = cheltuialaRepository.findAllByUserAndAsociatieAndData(asocId, user.getId(), cheltuialaService.firstDayOfMonth(luna, anul), cheltuialaService.firstDayOfMonth(luna + 1, anul));
        List<CheltuialaDto> cheltuialaDtoList = new ArrayList<>();
        for (Cheltuiala cheltuiala: cheltuialaList
             ) {
            CheltuialaDto cheltuialaDto = new CheltuialaDto();
            cheltuialaDto.setNume_cheltuiala(cheltuiala.getNume_cheltuiala());
            cheltuialaDto.setSuma(Double.valueOf(df.format(cheltuiala.getSuma())));
            cheltuialaDto.setCalcul_nume(cheltuiala.getCalculCheltuiala().getNume());
            cheltuialaDtoList.add(cheltuialaDto);
        }
        return  cheltuialaDtoList;
    }

    public List<ServiciuDto> getServicii(OAuth2Authentication auth2Authentication, Long asocId, Integer anul, Integer luna) throws ResponseStatusException {
        if (asocId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus o asociatie");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (asociatieRepository.findById(asocId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if (!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get())) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, anul);
        cal.set(Calendar.MONTH, luna-1);
        Date lunaCurenta = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date lunaPrecedenta = cal.getTime();
        List<ServiciuDto> serviciuDtoList = new ArrayList<>();
        for (Serviciu serviciu : asociatie.getServiciuList()) {
            ServiciuDto serviciuDto = new ServiciuDto();
            serviciuDto.setPret(serviciu.getPret_serviciu());
            serviciuDto.setNume(serviciu.getNume_serviciu());

            for (Contor contor : serviciu.getContoare()
            ) {
                if (contor.isContor_general()) {

                    Optional<Index> indexCurent = indexRepository.getIndexbyContorAndDate(contor.getId(), lunaCurenta);
                    if (indexCurent.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus indexul pentru serviciul " + serviciu.getNume_serviciu() + " pentru contorul " + contor.getNume_contor() + ", pentru luna " + luna + "." + anul);
                    Optional<Index> indexPrecedent = indexRepository.getIndexbyContorAndDate(contor.getId(), lunaPrecedenta);
                    if (indexPrecedent.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus indexul pentru serviciul " + serviciu.getNume_serviciu() + " pentru contorul " + contor.getNume_contor() + ", pentru luna precedenta ");
                    Optional<IstoricPret> istoricPret = istoricPretRepository.getByDateAndServiciu(serviciu.getId(), new Timestamp(lunaCurenta.getTime()));
                    serviciuDto.setPret(istoricPret.get().getPret());
                    serviciuDto.setConsumLunar(indexCurent.get().getValoare_index()-indexPrecedent.get().getValoare_index());
                }
            }
            serviciuDtoList.add(serviciuDto);
        }
        return serviciuDtoList;
    }

    public List<AvizierDto> getAvizier(OAuth2Authentication auth2Authentication, Long asocId, Integer anul, Integer luna) throws ResponseStatusException{
        if (asocId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus o asociatie");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (asociatieRepository.findById(asocId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if (!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get())) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        List<Apartament> apartamentList = apartamentRepository.getApartamenteByUserAndAsociatie(user.getId(), asocId);
        Optional<SuprafataTotala> suprafataTotala = apartamentRepository.getSuprafataTotalaByUserAndAsociatie(user.getId(), asocId);
        List<AvizierDto> avizierDtoList = new ArrayList<>();

        for (Apartament apartament : apartamentList) {
            AvizierDto avizierDto = new AvizierDto();
            avizierDto.setNumarApartament(apartament.getNumar_apartament());
            avizierDto.setNumarLocatari(apartament.getNumar_locatari());
            avizierDto.setSuprafataApartament(Double.valueOf(df.format(apartament.getSuprafata_mp())));
            Optional<PlataIntretinere> plataIntretinere = plataIntretinereRepository.getByApartamentAndLunaAndAn(apartament.getId(), luna, anul);
            if(plataIntretinere.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Plata intretinerii pentru apartamentul " + apartament.getNumar_apartament() + "nu a fost creata pentru luna " + luna + " " + anul);
            avizierDto.setSumaDePlatit(Double.valueOf(df.format(plataIntretinere.get().getSuma())));

           List <PlataIntretinereDetalii> plataIntretinereDetaliiListRepository = plataIntretinereRepository.getByIdAndUserAndAsociatieAndFactura(asocId, user.getId(), plataIntretinere.get().getId());
           List<AvizierRandDto> avizierRandDtoList = new ArrayList<>();
            for (PlataIntretinereDetalii plataIntretinereDetalii: plataIntretinereDetaliiListRepository
                    ) {
                AvizierRandDto avizierRandDto = new AvizierRandDto();
                avizierRandDto.setSuma(Double.valueOf(df.format(plataIntretinereDetalii.getSuma())));
                avizierRandDto.setConsum(Double.valueOf(df.format(plataIntretinereDetalii.getConsum())));
                avizierRandDto.setText(plataIntretinereDetalii.getTextDetalii());
                avizierRandDtoList.add(avizierRandDto);
            }
            double cpi = (apartament.getSuprafata_mp()/suprafataTotala.get().getSuprafataTotala())*100;
            avizierDto.setCpi(Double.valueOf(df.format(cpi)));
            avizierDto.setAvizierRandDtoList(avizierRandDtoList);
            avizierDtoList.add(avizierDto);
        }
        return avizierDtoList;
    }

    public List<AvizierDto> getAvizierLocatar(OAuth2Authentication auth2Authentication, Long aptId, Integer anul, Integer luna) throws ResponseStatusException {

        if (aptId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus o asociatie");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (apartamentRepository.findById(aptId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        Apartament apartament1 = apartamentRepository.findById(aptId).get();
        Long asocId = apartament1.getAsociatie().getId();
        if (apartament1.getUser().getId()!=userRepository.findByUser(email).get().getId())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        List<Apartament> apartamentList = apartamentRepository.getApartamenteByAsociatie(asocId);
        Optional<SuprafataTotala> suprafataTotala = apartamentRepository.getSuprafataTotalaByAsociatie(asocId);
        List<AvizierDto> avizierDtoList = new ArrayList<>();

        for (Apartament apartament : apartamentList) {
            AvizierDto avizierDto = new AvizierDto();
            avizierDto.setNumarApartament(apartament.getNumar_apartament());
            avizierDto.setNumarLocatari(apartament.getNumar_locatari());
            avizierDto.setSuprafataApartament(Double.valueOf(df.format(apartament.getSuprafata_mp())));
            Optional<PlataIntretinere> plataIntretinere = plataIntretinereRepository.getByApartamentAndLunaAndAn(apartament.getId(), luna, anul);
            if(plataIntretinere.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Plata intretinerii pentru apartamentul " + apartament.getNumar_apartament() + "nu a fost creata pentru luna " + luna + " " + anul);
            avizierDto.setSumaDePlatit(Double.valueOf(df.format(plataIntretinere.get().getSuma())));

            List <PlataIntretinereDetalii> plataIntretinereDetaliiListRepository = plataIntretinereRepository.getByIdAndAsociatieAndFactura(asocId, plataIntretinere.get().getId());
            List<AvizierRandDto> avizierRandDtoList = new ArrayList<>();
            for (PlataIntretinereDetalii plataIntretinereDetalii: plataIntretinereDetaliiListRepository
            ) {
                AvizierRandDto avizierRandDto = new AvizierRandDto();
                avizierRandDto.setSuma(Double.valueOf(df.format(plataIntretinereDetalii.getSuma())));
                avizierRandDto.setConsum(Double.valueOf(df.format(plataIntretinereDetalii.getConsum())));
                avizierRandDto.setText(plataIntretinereDetalii.getTextDetalii());
                avizierRandDtoList.add(avizierRandDto);
            }
            double cpi = (apartament.getSuprafata_mp()/suprafataTotala.get().getSuprafataTotala())*100;
            avizierDto.setCpi(Double.valueOf(df.format(cpi)));
            avizierDto.setAvizierRandDtoList(avizierRandDtoList);
            avizierDtoList.add(avizierDto);
        }
        return avizierDtoList;

    }

    public List<CheltuialaDto> getCheltuieliLocatar(OAuth2Authentication auth2Authentication, Long aptId, Integer anul, Integer luna) throws ResponseStatusException{

        if (aptId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus o asociatie");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (apartamentRepository.findById(aptId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        Apartament apartament1 = apartamentRepository.findById(aptId).get();
        Long asocId = apartament1.getAsociatie().getId();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, anul);
        cal.set(Calendar.MONTH, luna-1);
        Date lunaCurenta = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date lunaPrecedenta = cal.getTime();
        List<Cheltuiala> cheltuialaList = cheltuialaRepository.findAllByAndAsociatieAndData(asocId, cheltuialaService.firstDayOfMonth(luna, anul), cheltuialaService.firstDayOfMonth(luna + 1, anul));
        List<CheltuialaDto> cheltuialaDtoList = new ArrayList<>();
        for (Cheltuiala cheltuiala: cheltuialaList
        ) {
            CheltuialaDto cheltuialaDto = new CheltuialaDto();
            cheltuialaDto.setNume_cheltuiala(cheltuiala.getNume_cheltuiala());
            cheltuialaDto.setSuma(Double.valueOf(df.format(cheltuiala.getSuma())));
            cheltuialaDto.setCalcul_nume(cheltuiala.getCalculCheltuiala().getNume());
            cheltuialaDtoList.add(cheltuialaDto);
        }
        return  cheltuialaDtoList;
    }

    public List<ServiciuDto> getServiciiLocatar(OAuth2Authentication auth2Authentication, Long aptId, Integer anul, Integer luna) {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (apartamentRepository.findById(aptId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        Apartament apartament1 = apartamentRepository.findById(aptId).get();
        Long asocId = apartament1.getAsociatie().getId();
        if (apartament1.getUser().getId()!=userRepository.findByUser(email).get().getId())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, anul);
        cal.set(Calendar.MONTH, luna-1);
        Date lunaCurenta = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date lunaPrecedenta = cal.getTime();
        List<ServiciuDto> serviciuDtoList = new ArrayList<>();
        for (Serviciu serviciu : asociatie.getServiciuList()) {
            ServiciuDto serviciuDto = new ServiciuDto();
            serviciuDto.setPret(serviciu.getPret_serviciu());
            serviciuDto.setNume(serviciu.getNume_serviciu());

            for (Contor contor : serviciu.getContoare()
            ) {
                if (contor.isContor_general()) {

                    Optional<Index> indexCurent = indexRepository.getIndexbyContorAndDate(contor.getId(), lunaCurenta);
                    if (indexCurent.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus indexul pentru serviciul " + serviciu.getNume_serviciu() + " pentru contorul " + contor.getNume_contor() + ", pentru luna " + luna + "." + anul);
                    Optional<Index> indexPrecedent = indexRepository.getIndexbyContorAndDate(contor.getId(), lunaPrecedenta);
                    if (indexPrecedent.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus indexul pentru serviciul " + serviciu.getNume_serviciu() + " pentru contorul " + contor.getNume_contor() + ", pentru luna precedenta ");
                    Optional<IstoricPret> istoricPret = istoricPretRepository.getByDateAndServiciu(serviciu.getId(), new Timestamp(lunaCurenta.getTime()));
                    serviciuDto.setPret(istoricPret.get().getPret());
                    serviciuDto.setConsumLunar(indexCurent.get().getValoare_index()-indexPrecedent.get().getValoare_index());
                }
            }
            serviciuDtoList.add(serviciuDto);
        }
        return serviciuDtoList;
    }
}

