package com.licenta.licenta.service;

import com.licenta.licenta.Security.UserPrinciple;
import com.licenta.licenta.dto.*;
import com.licenta.licenta.model.*;
import com.licenta.licenta.repository.*;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class FacturaService {
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

    public List<FacturaTabelDto> getAll(OAuth2Authentication auth2Authentication, Long asocId) throws ResponseStatusException{
        if (asocId.toString().isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "nu ati introdus o asociatie");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (asociatieRepository.findById(asocId).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if (!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "eroare 406: nu sunteti admin pentru aceasta asociatie");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();

        List<FacturaTabelDto> facturaTabelDtoList = new ArrayList<>();
        List<PlataIntretinere> plataIntretinereList = plataIntretinereRepository.getByAsociatieAndUser(asocId, user.getId());
        for (PlataIntretinere plataIntretinere : plataIntretinereList
        ) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(plataIntretinere.getData_intocmire());
            calendar.add(Calendar.MONTH, 1);
            Date dataLimita = calendar.getTime();
            FacturaTabelDto facturaTabelDto = new FacturaTabelDto();
            facturaTabelDto.setApartamentNume(plataIntretinere.getApartament().getNumar_apartament());
            facturaTabelDto.setDataScadenta(dataLimita);
            facturaTabelDto.setId(plataIntretinere.getId());
            facturaTabelDto.setData(plataIntretinere.getData_intocmire());
            facturaTabelDto.setValoare(Double.valueOf(df.format(plataIntretinere.getSuma())));
            facturaTabelDto.setStatus(this.statusFactura(Double.valueOf(df.format(plataIntretinere.getSuma())), plataIntretinere.getId()));
            if(plataIntretinere.getAchitatComplet()==1) {
                facturaTabelDto.setValoareRestante(0);
            }else {
                facturaTabelDto.setValoareRestante(this.calculRestante(plataIntretinere.getData_intocmire(), plataIntretinere.getSuma()));
            }
            facturaTabelDtoList.add(facturaTabelDto);
        }
        return facturaTabelDtoList;
    }

    public ResponseEntity<Object> generareFacturi(OAuth2Authentication auth2Authentication, GenerareFacturi generareFacturi, Long asocId) {
        if (asocId.toString().isEmpty()) {
            mesaj.setMessage("Nu ati introdus o asociatie.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (asociatieRepository.findById(asocId).isEmpty()) {
            mesaj.setMessage("Nu sunteti admin pentru aceasta asociatie.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if (!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get())) {
            mesaj.setMessage("Nu sunteti admin pentru aceasta asociatie.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, generareFacturi.getAn());
        cal.set(Calendar.MONTH, generareFacturi.getLuna()-1);
        Date lunaCurenta = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date lunaPrecedenta = cal.getTime();
        List<PlataIntretinere> plataIntretinereList = plataIntretinereRepository.getByAsociatieAndUserAndDate(asocId, user.getId(), generareFacturi.getLuna(), generareFacturi.getAn());
        List<Cheltuiala> cheltuialaList = cheltuialaRepository.findAllByUserAndAsociatieAndData(asocId, user.getId(), cheltuialaService.firstDayOfMonth(generareFacturi.getLuna(), generareFacturi.getAn()), cheltuialaService.firstDayOfMonth(generareFacturi.getLuna() + 1, generareFacturi.getAn()));
        List<Apartament> apartamentList = apartamentRepository.getApartamenteByUserAndAsociatie(user.getId(), asocId);
        Optional<NumarPersoane> numarPersoane = apartamentRepository.getNumarPersoaneByUserAndAsociatie(user.getId(), asocId);
        Optional<SuprafataTotala> suprafataTotala = apartamentRepository.getSuprafataTotalaByUserAndAsociatie(user.getId(), asocId);
        Map<Long, ContorIndex> contorIndexListGeneral = new HashMap();
        Map<Long, Double> pretServiciuMap = new HashMap<>();
        for (Serviciu serviciu : asociatie.getServiciuList()) {
            for (Contor contor : serviciu.getContoare()
            ) {
                if (contor.isContor_general()) {

                    Optional<Index> indexCurent = indexRepository.getIndexbyContorAndDate(contor.getId(), lunaCurenta);
                    if (indexCurent.isEmpty()) {
                        mesaj.setMessage("Nu ati introdus indexul pentru serviciul " + serviciu.getNume_serviciu() + " pentru contorul " + contor.getNume_contor() + ", pentru luna " + generareFacturi.getLuna() + "." + generareFacturi.getAn());
                        mesaj.setCode(406);
                        return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
                    }
                    Optional<Index> indexPrecedent = indexRepository.getIndexbyContorAndDate(contor.getId(), lunaPrecedenta);
                    if (indexPrecedent.isEmpty()) {
                        mesaj.setMessage("Nu ati introdus indexul pentru serviciul " + serviciu.getNume_serviciu() + " pentru contorul " + contor.getNume_contor() + ", pentru luna precedenta ");
                        mesaj.setCode(406);
                        return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
                    }
                    ContorIndex contorIndexGeneral = new ContorIndex();
                    contorIndexGeneral.setContorId(contor.getId());
                    contorIndexGeneral.setServiciuId(serviciu.getId());
                    contorIndexGeneral.setIndexCurent(indexCurent.get().getValoare_index());
                    contorIndexGeneral.setIndexTrecut(indexPrecedent.get().getValoare_index());
                    contorIndexListGeneral.put(serviciu.getId(), contorIndexGeneral);
                    Optional<IstoricPret> istoricPret = istoricPretRepository.getByDateAndServiciu(serviciu.getId(), new Timestamp(System.currentTimeMillis()));
                    pretServiciuMap.put(serviciu.getId(), istoricPret.get().getPret());
                }
            }
        }

        Map<Long, Double> contorServiciuPeToateApartamentele = new HashMap();
        Map<Long, PlataIntretinere> plataIntretinereMap = new HashMap<>();
        List<ConsumIndividualPentruFactura> consumIndividualPentruFacturaList = new ArrayList<>();
        List<PlataIntretinereDetalii> plataIntretinereDetaliiList = new ArrayList<>();
        for (Apartament apartament : apartamentList) {
            PlataIntretinere plataIntretinere = new PlataIntretinere();
            plataIntretinere.setAn(generareFacturi.getAn());
            plataIntretinere.setLuna(generareFacturi.getLuna());
            plataIntretinere.setApartament(apartament);
            plataIntretinere.setUser(apartament.getUser());
            plataIntretinere.setData_intocmire(new Date());
            plataIntretinere.setAchitatComplet(0);
            double suma = 0;
            String detalii = "";
            for (Cheltuiala cheltuiala : cheltuialaList
            ) {
                double cheltuialaIndividuala = cheltuialaService.formule(cheltuiala.getCalculCheltuiala().getFormula_calcul(), cheltuiala.getSuma(),
                        suprafataTotala.get().getSuprafataTotala(), apartament.getSuprafata_mp(),
                        numarPersoane.get().getNumarPersoane(), apartament.getNumar_locatari());
                Serviciu serviciuPtCheltuieli = serviciuRepository.getById(1L);
                suma = suma + cheltuialaIndividuala;
                detalii = detalii + cheltuiala.getNume_cheltuiala() + " (Pret serviciu: " + cheltuiala.getSuma() + " RON)." + " Trebuie să plătiți: " + df.format(cheltuialaIndividuala) + System.lineSeparator();
                String detaliiDeSalvat = cheltuiala.getNume_cheltuiala() + " (Pret: " + cheltuiala.getSuma() + " RON).";
                PlataIntretinereDetalii plataIntretinereDetalii = new PlataIntretinereDetalii();
                plataIntretinereDetalii.setTextDetalii(detaliiDeSalvat);
                plataIntretinereDetalii.setSuma(cheltuialaIndividuala);
                plataIntretinereDetalii.setServiciu(serviciuPtCheltuieli);
                plataIntretinereDetalii.setConsum(1);
                plataIntretinereDetalii.setApartmentId(apartament.getId());

                plataIntretinereDetaliiList.add(plataIntretinereDetalii);
            }
            for (Serviciu serviciu : asociatie.getServiciuList()) {
                double valoareConsum = 0;
                List<Contor> contorList = contorRepository.getContoareByAsociatieAndApartamentAndService(user.getId(), asocId, apartament.getId(), serviciu.getId());
                if (!contorList.isEmpty()) {
                    for (Contor contor : contorList) {
                        if (!contor.isContor_general()) {
                            Optional<Index> indexCurent = indexRepository.getSingleIndexByUserAndApartamentAndServiciuAndContorAndDate(user.getId(), apartament.getId(), serviciu.getId(), contor.getId(), lunaCurenta);
                            if (indexCurent.isEmpty()) {
                                mesaj.setMessage("Nu ati introdus indexul pentru apartamentul: " + apartament.getNumar_apartament() + " pentru serviciul " + serviciu.getNume_serviciu() + " pentru contorul " + contor.getNume_contor() + ", pentru luna " + generareFacturi.getLuna() + "." + generareFacturi.getAn());
                                mesaj.setCode(406);
                                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
                            }
                            Optional<Index> indexPrecedent = indexRepository.getSingleIndexByUserAndApartamentAndServiciuAndContorAndDate(apartament.getUser().getId(), apartament.getId(), serviciu.getId(), contor.getId(), lunaPrecedenta);
                            if (indexPrecedent.isEmpty()) {
                                mesaj.setMessage("Nu ati introdus indexul pentru apartamentul: " + apartament.getNumar_apartament() + " pentru serviciul " + serviciu.getNume_serviciu() + " pentru contorul " + contor.getNume_contor() + ", pentru luna precedenta.");
                                mesaj.setCode(406);
                                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
                            }
                            if (indexPrecedent.get().getValoare_index() > indexCurent.get().getValoare_index()) {
                                mesaj.setMessage("Valoare indexului curent este mai mica decat indexul precedent, pentru apartamentul : " + apartament.getNumar_apartament() + " pentru serviciul " + serviciu.getNume_serviciu() + " pentru contorul " + contor.getNume_contor() + ", pentru luna " + generareFacturi.getLuna() + "." + generareFacturi.getAn());
                                mesaj.setCode(406);
                                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
                            }
                            double valScadere = indexCurent.get().getValoare_index() - indexPrecedent.get().getValoare_index();
                            valoareConsum = valoareConsum + valScadere;
                        }
                    }
                }
                Optional<IstoricPret> istoricPret = istoricPretRepository.getByDateAndServiciu(serviciu.getId(), new Timestamp(System.currentTimeMillis()));
                suma = suma + (valoareConsum * istoricPret.get().getPret());
                detalii = detalii + serviciu.getNume_serviciu() + " (pret serviciu: " + istoricPret.get().getPret() + " RON)." + " Suma pe care trebuie sa o platiti este: " + df.format(valoareConsum * istoricPret.get().getPret()) + " RON" + System.lineSeparator();
                String detaliiDeSalvat = serviciu.getNume_serviciu() + " (pret: " + istoricPret.get().getPret() + " RON).";
                PlataIntretinereDetalii plataIntretinereDetalii = new PlataIntretinereDetalii();
                plataIntretinereDetalii.setTextDetalii(detaliiDeSalvat);
                plataIntretinereDetalii.setSuma(valoareConsum * istoricPret.get().getPret());
                plataIntretinereDetalii.setConsum(valoareConsum);
                plataIntretinereDetalii.setServiciu(serviciu);
                plataIntretinereDetalii.setApartmentId(apartament.getId());
                plataIntretinereDetaliiList.add(plataIntretinereDetalii);

                ConsumIndividualPentruFactura consumIndividualPentruFactura = new ConsumIndividualPentruFactura();
                consumIndividualPentruFactura.setServiciuId(serviciu.getId());
                consumIndividualPentruFactura.setApartamentId(apartament.getId());
                consumIndividualPentruFactura.setValoareIndex(valoareConsum);
                consumIndividualPentruFacturaList.add(consumIndividualPentruFactura);
                if (contorServiciuPeToateApartamentele.containsKey(serviciu.getId())) {
                    contorServiciuPeToateApartamentele.put(serviciu.getId(), contorServiciuPeToateApartamentele.get(serviciu.getId()) + valoareConsum);
                } else {
                    contorServiciuPeToateApartamentele.put(serviciu.getId(), valoareConsum);
                }
            }
            plataIntretinere.setSuma(suma);
            plataIntretinereMap.put(apartament.getId(), plataIntretinere);
        }

        Iterator<Long> itContorIndexListGeneral = contorIndexListGeneral.keySet().iterator();
        while (itContorIndexListGeneral.hasNext()) {
            Long key = itContorIndexListGeneral.next();
            double diferenteServiciu = contorIndexListGeneral.get(key).getIndexCurent() - contorIndexListGeneral.get(key).getIndexTrecut() - contorServiciuPeToateApartamentele.get(key);
            if (diferenteServiciu<=0) {
                mesaj.setMessage("Valoare consumului "+ serviciuRepository.getById(contorIndexListGeneral.get(key).getServiciuId()).getNume_serviciu() + " este mai mic decat consumul tuturor apartamentelor pentru " + generareFacturi.getLuna() + "." + generareFacturi.getAn());
                mesaj.setCode(406);
                return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
            }
            if (diferenteServiciu > 0) {
                Iterator<Long> itPlataIntretinereMap = plataIntretinereMap.keySet().iterator();
                while (itPlataIntretinereMap.hasNext()) {
                    Long keyPlataIntretinere = itPlataIntretinereMap.next();
                    double valoare=0;
                    for (ConsumIndividualPentruFactura consumIndividualPentruFactura : consumIndividualPentruFacturaList
                         ) {
                        if((consumIndividualPentruFactura.getServiciuId()==key)&&(consumIndividualPentruFactura.getApartamentId()==keyPlataIntretinere))valoare=consumIndividualPentruFactura.getValoareIndex();
                    }

                    double diferentaApartament = cheltuialaService.formulaPierdere(contorIndexListGeneral.get(key).getIndexCurent() - contorIndexListGeneral.get(key).getIndexTrecut(), contorServiciuPeToateApartamentele.get(key), valoare);
                    PlataIntretinere newplata = plataIntretinereMap.get(keyPlataIntretinere);
                    newplata.setSuma(newplata.getSuma() + (diferentaApartament * pretServiciuMap.get(key)));
                    String detalii = serviciuRepository.getById(key).getNume_serviciu() + " (Pierderi - pret de referinta " + serviciuRepository.getById(key).getPret_serviciu() + " RON). " + " Trebuie să plătiți: " + df.format((diferentaApartament * serviciuRepository.getById(key).getPret_serviciu())) + " RON." + System.lineSeparator();
                    String detaliiDeSalvat = serviciuRepository.getById(key).getNume_serviciu() + " (Pierderi)";
                    PlataIntretinereDetalii plataIntretinereDetalii = new PlataIntretinereDetalii();
                    plataIntretinereDetalii.setTextDetalii(detaliiDeSalvat);
                    plataIntretinereDetalii.setSuma(diferentaApartament * serviciuRepository.getById(key).getPret_serviciu());
                    plataIntretinereDetalii.setApartmentId(keyPlataIntretinere);
                    plataIntretinereDetalii.setServiciu(serviciuRepository.getById(key));
                    plataIntretinereDetalii.setConsum(diferentaApartament);
                    plataIntretinereDetaliiList.add(plataIntretinereDetalii);
                    plataIntretinereMap.put(keyPlataIntretinere, newplata);
                }
            }
        }
        Date dateDeVerificare = new Date();
        Calendar calendarDeVerificare = Calendar.getInstance();
        calendarDeVerificare.setTime(dateDeVerificare);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        if (generareFacturi.getMethod() == 2) {
            plataIntretinereRepository.deleteAll(plataIntretinereList);
        }
//        if ((generareFacturi.getMethod() == 2)&&(generareFacturi.getLuna()==month)&&(generareFacturi.getAn()==year)) {
//            plataIntretinereRepository.deleteAll(plataIntretinereList);
//        } else {
//            mesaj.setMessage("Puteti genera facturi doar pentru luna in curs");
//            mesaj.setCode(406);
//            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
//        }
        Iterator<Long> itPlataIntretinereMapLast = plataIntretinereMap.keySet().iterator();
        while (itPlataIntretinereMapLast.hasNext()) {
            Long key = itPlataIntretinereMapLast.next();
                PlataIntretinere plataIntretinere = new PlataIntretinere();
                // plataIntretinere.setDetails(plataIntretinereMap.get(key).getDetails());
                plataIntretinere.setSuma(plataIntretinereMap.get(key).getSuma());
                plataIntretinere.setLuna(generareFacturi.getLuna());
                plataIntretinere.setAn(generareFacturi.getAn());
                plataIntretinere.setData_intocmire(new Date());
                plataIntretinere.setAchitatComplet(0);
                plataIntretinere.setApartament(plataIntretinereMap.get(key).getApartament());
                plataIntretinere.setUser(plataIntretinereMap.get(key).getUser());
            for (PlataIntretinereDetalii plataIntretinereDetalii: plataIntretinereDetaliiList
            ) {
                if(key.equals(plataIntretinereDetalii.getApartmentId())) plataIntretinereDetalii.setPlataIntretinere(plataIntretinere);
            }
                plataIntretinere.setPlataIntretinereDetaliiList(plataIntretinereDetaliiList);
                plataIntretinereRepository.save(plataIntretinere);
        }

        mesaj.setMessage("Facturile au fost generate.");
        mesaj.setCode(201);
        return new ResponseEntity<>(mesaj, HttpStatus.CREATED);
    }
    public FacturaDateDto getFactura(OAuth2Authentication auth2Authentication, Long asocId, Long facturaId) throws ResponseStatusException{
        if (asocId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus o asociatie");
        if (facturaId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati selectat o factura");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (asociatieRepository.findById(asocId).isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if (!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get())) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        if(plataIntretinereRepository.getByIdAndUserAndAsociatie(asocId,user.getId(),facturaId).isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Factura nu exista.");
        PlataIntretinere plataIntretinere = plataIntretinereRepository.getByIdAndUserAndAsociatie(asocId,user.getId(),facturaId).get();
        FacturaDateDto facturaDateDto = new FacturaDateDto();
        facturaDateDto.setAnul(plataIntretinere.getAn());
        facturaDateDto.setLuna(plataIntretinere.getLuna());
        facturaDateDto.setDataIntocmire(plataIntretinere.getData_intocmire());
        facturaDateDto.setDataScadenta(calculDataScadenta(plataIntretinere.getData_intocmire()));

        facturaDateDto.setValoare(Double.valueOf(df.format(plataIntretinere.getSuma())));
        facturaDateDto.setId(plataIntretinere.getId());
        double sumaRamasa=chitantePlatite(plataIntretinere.getSuma(), plataIntretinere.getId());
        facturaDateDto.setValoareAchitata(Double.valueOf(df.format(sumaRamasa)));
        if(plataIntretinere.getAchitatComplet()==1) {
            facturaDateDto.setValoareRestante(0);
        }else {
            facturaDateDto.setValoareRestante(Double.valueOf(df.format((calculRestante(plataIntretinere.getData_intocmire(), plataIntretinere.getSuma()-sumaRamasa)))));
        }
        facturaDateDto.setApartamentNumar(plataIntretinere.getApartament().getNumar_apartament());
        facturaDateDto.setApartamentNume(plataIntretinere.getApartament().getUser().getNume() + " " + plataIntretinere.getApartament().getUser().getPrenume());
    return facturaDateDto;
    }
    public List<FacturaDateDetaliiDto> getFacturaDetalii(OAuth2Authentication auth2Authentication, Long asocId, Long facturaId) {
        if (asocId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus o asociatie");
        if (facturaId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati selectat o factura");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (asociatieRepository.findById(asocId).isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if (!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get())) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        if(plataIntretinereRepository.getByIdAndUserAndAsociatie(asocId,user.getId(),facturaId).isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Factura nu exista.");
        List<PlataIntretinereDetalii> plataIntretinereDetaliiList = plataIntretinereRepository.getByIdAndUserAndAsociatieAndFactura(asocId,user.getId(),facturaId);
        List<FacturaDateDetaliiDto> facturaDateDetaliiDtoList = new ArrayList<>();
        for (PlataIntretinereDetalii plataIntretinereDetalii: plataIntretinereDetaliiList
             ) {
            FacturaDateDetaliiDto facturaDateDetaliiDto = new FacturaDateDetaliiDto();
            facturaDateDetaliiDto.setDetalii(plataIntretinereDetalii.getTextDetalii());
            facturaDateDetaliiDto.setValoare(Double.valueOf(df.format(plataIntretinereDetalii.getSuma())));
            facturaDateDetaliiDtoList.add(facturaDateDetaliiDto);
        }
        return  facturaDateDetaliiDtoList;
    }

    private double calculRestante(Date data, double value){

        Calendar c= Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.DATE, 60);
        Date d=c.getTime();

        Calendar c1= Calendar.getInstance();
        c1.setTime(new Date());
        Date d1=c1.getTime();

        long diff = ChronoUnit.DAYS.between(d.toInstant(),d1.toInstant());
        if (diff<=0) return 0;
        double restante = (diff*value*0.02);
        if(restante>value) return Double.valueOf(df.format(value));
        return Double.valueOf(df.format(diff*value*0.02));
    }

    private Date calculDataScadenta(Date data) {

        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.add(Calendar.DATE, 30);
        Date d = c.getTime();
        return d;
    }
    private String statusFactura(Double suma, Long plataIntretinereId) {
        double totalChitante = 0;
        List<Chitanta> chitantaList = chitantaRepository.findAllByPlataIntretinereId(plataIntretinereId);
        for (Chitanta chitanta : chitantaList
        ) {
            totalChitante = totalChitante + chitanta.getSuma();
        }
        if (suma == totalChitante) return "Achitat complet";
        if (totalChitante == 0) return "Neachitat";
        if ((totalChitante > 0) && (suma > totalChitante)) return "Achitat partial (" + Double.valueOf(df.format(totalChitante)) + "RON)" ;
        return "Neachitat";
    }
    private double restantaFactura(Double suma, Long plataIntretinereId) {
        double totalChitante = 0;
        List<Chitanta> chitantaList = chitantaRepository.findAllByPlataIntretinereId(plataIntretinereId);
        for (Chitanta chitanta : chitantaList
        ) {
            totalChitante = totalChitante + chitanta.getSuma();
        }
        double rest = suma-totalChitante;
        if(rest<0) return 0;
        return rest;
    }
    private double chitantePlatite(Double suma, Long plataIntretinereId) {
        double totalChitante = 0;
        List<Chitanta> chitantaList = chitantaRepository.findAllByPlataIntretinereId(plataIntretinereId);
        for (Chitanta chitanta : chitantaList
        ) {
            totalChitante = totalChitante + chitanta.getSuma();
        }
        return totalChitante;
    }

    public ResponseEntity<Object> plataFactura(OAuth2Authentication auth2Authentication, PlataFacturaDto plataFacturaDto, Long asocId, Long facturaId) {

        if (asocId.toString().isEmpty()){
            mesaj.setMessage("Nu ati introdus asociatia");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if (facturaId.toString().isEmpty()) {
            mesaj.setMessage("Nu ati selectat factura");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (asociatieRepository.findById(asocId).isEmpty()) {
            mesaj.setMessage("Nu sunteti admin pentru aceasta asociatie.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if (!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get())) {
            mesaj.setMessage("Nu sunteti admin pentru aceasta asociatie.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        if(plataIntretinereRepository.getByIdAndUserAndAsociatie(asocId,user.getId(),facturaId).isEmpty()){
            mesaj.setMessage("Factura nu exista");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        PlataIntretinere plataIntretinere = plataIntretinereRepository.getByIdAndUserAndAsociatie(asocId,user.getId(),facturaId).get();
        double sumaRamasa=restantaFactura(plataIntretinere.getSuma(), plataIntretinere.getId());
        if(plataFacturaDto.getValoare()>Double.valueOf(df.format(plataIntretinere.getSuma()+sumaRamasa))){
            mesaj.setMessage("Ati introdus o suma mai mare");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(plataFacturaDto.getValoare()==Double.valueOf(df.format(plataIntretinere.getSuma()+sumaRamasa))) {
            PlataIntretinere plataIntretinereAchitat = plataIntretinereRepository.getById(plataIntretinere.getId());
            plataIntretinereAchitat.setAchitatComplet(1);
            plataIntretinereRepository.save(plataIntretinereAchitat);
        }
        String metodaDePlata = "Cu card la ghiseu.";
        String serieChitanta = "ASC2022";
        if(plataFacturaDto.getMetoda()==1)metodaDePlata="Cu card la ghiseu.";
        if(plataFacturaDto.getMetoda()==2)metodaDePlata="Numerar la ghiseu.";
        if(plataFacturaDto.getMetoda()==3)metodaDePlata="Card online.";
        Chitanta chitanta=new Chitanta();
        chitanta.setDetalii(metodaDePlata);
        chitanta.setPlataIntretinere(plataIntretinere);
        chitanta.setSuma(plataFacturaDto.getValoare());
        chitanta.setSerie_chitanta(serieChitanta);
        chitanta.setData_achitare(new Date());
        chitanta.setNumar_chitanta(0);
        chitantaRepository.save(chitanta);
        mesaj.setMessage("Factura a fost achitata");
        mesaj.setCode(200);
        return new ResponseEntity<>(mesaj, HttpStatus.OK);
    }

    public List<ChitanteDto> getFacturaChitante(OAuth2Authentication auth2Authentication, Long asocId, Long facturaId) throws ResponseStatusException{
        if (asocId.toString().isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus o asociatie");
        if (facturaId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati selectat factura");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (asociatieRepository.findById(asocId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pt aceasta asociatie");
        Asociatie asociatie = asociatieRepository.findById(asocId).get();
        if (!asociatie.findAdminFromAsociatie(userRepository.findByUser(email).get())) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru asociatie");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        if(plataIntretinereRepository.getByIdAndUserAndAsociatie(asocId,user.getId(),facturaId).isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Factura nu exista");
        List<Chitanta> chitantaList = chitantaRepository.findAllByPlataIntretinereId(facturaId);
        List<ChitanteDto> chitanteDtoList = new ArrayList<>();
        for (Chitanta chitanta : chitantaList
        ) {
            ChitanteDto chitantaDto = new ChitanteDto();
            chitantaDto.setDetalii(chitanta.getDetalii());
            chitantaDto.setId(chitanta.getId());
            chitantaDto.setSerieChitanta(chitanta.getSerie_chitanta());
            chitantaDto.setSuma(chitanta.getSuma());
            chitantaDto.setDataAchitare(chitanta.getData_achitare());
            chitanteDtoList.add(chitantaDto);
        }
        return chitanteDtoList;
    }

    public List<FacturaTabelDto> getFacturiApartament(OAuth2Authentication auth2Authentication, Long apartamentId) throws ResponseStatusException{
        if (apartamentId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus un apartament");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (apartamentRepository.findById(apartamentId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu este apartamentul dumneavoastra");
        Apartament apartament1 = apartamentRepository.findById(apartamentId).get();
        if (apartament1.getUser().getId()!=userRepository.findByUser(email).get().getId())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti admin pentru aceasta asociatie.");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        List<FacturaTabelDto> facturaTabelDtoList = new ArrayList<>();
        List<PlataIntretinere> plataIntretinereList = plataIntretinereRepository.getByApartamentAndUser(apartamentId, user.getId());
        for (PlataIntretinere plataIntretinere : plataIntretinereList
        ) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(plataIntretinere.getData_intocmire());
            calendar.add(Calendar.MONTH, 1);
            Date dataLimita = calendar.getTime();
            FacturaTabelDto facturaTabelDto = new FacturaTabelDto();
            facturaTabelDto.setApartamentNume(plataIntretinere.getApartament().getNumar_apartament());
            facturaTabelDto.setDataScadenta(dataLimita);
            facturaTabelDto.setId(plataIntretinere.getId());
            facturaTabelDto.setData(plataIntretinere.getData_intocmire());
            facturaTabelDto.setValoare(Double.valueOf(df.format(plataIntretinere.getSuma())));
            facturaTabelDto.setStatus(this.statusFactura(Double.valueOf(df.format(plataIntretinere.getSuma())), plataIntretinere.getId()));
            if(plataIntretinere.getAchitatComplet()==1) {
                facturaTabelDto.setValoareRestante(0);
            }else {
                facturaTabelDto.setValoareRestante(this.calculRestante(plataIntretinere.getData_intocmire(), plataIntretinere.getSuma()));
            }
            facturaTabelDtoList.add(facturaTabelDto);
        }
        return facturaTabelDtoList;

    }

    public FacturaDateDto getFacturaApartament(OAuth2Authentication auth2Authentication, Long apartamentId, Long facturaId) {
        if (facturaId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati selectat o factura");
        if (apartamentId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus un apartament");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (apartamentRepository.findById(apartamentId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus apartament");
        Apartament apartament1 = apartamentRepository.findById(apartamentId).get();
        if (apartament1.getUser().getId()!=userRepository.findByUser(email).get().getId())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus.");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        if(plataIntretinereRepository.getByIdAndUserAndApartament(apartamentId,user.getId(),facturaId).isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Factura nu exista.");
        PlataIntretinere plataIntretinere = plataIntretinereRepository.getByIdAndUserAndApartament(apartamentId,user.getId(),facturaId).get();
        FacturaDateDto facturaDateDto = new FacturaDateDto();
        facturaDateDto.setAnul(plataIntretinere.getAn());
        facturaDateDto.setLuna(plataIntretinere.getLuna());
        facturaDateDto.setDataIntocmire(plataIntretinere.getData_intocmire());
        facturaDateDto.setDataScadenta(calculDataScadenta(plataIntretinere.getData_intocmire()));

        facturaDateDto.setValoare(Double.valueOf(df.format(plataIntretinere.getSuma())));
        facturaDateDto.setId(plataIntretinere.getId());
        double sumaRamasa=chitantePlatite(plataIntretinere.getSuma(), plataIntretinere.getId());
        facturaDateDto.setValoareAchitata(Double.valueOf(df.format(sumaRamasa)));
        if(plataIntretinere.getAchitatComplet()==1) {
            facturaDateDto.setValoareRestante(0);
        }else {
            facturaDateDto.setValoareRestante(Double.valueOf(df.format((calculRestante(plataIntretinere.getData_intocmire(), plataIntretinere.getSuma()-sumaRamasa)))));
        }
        facturaDateDto.setApartamentNumar(plataIntretinere.getApartament().getNumar_apartament());
        facturaDateDto.setApartamentNume(plataIntretinere.getApartament().getUser().getNume() + " " + plataIntretinere.getApartament().getUser().getPrenume());
        return facturaDateDto;
    }

    public List<FacturaDateDetaliiDto> getFacturaApartamentDetalii(OAuth2Authentication auth2Authentication, Long apartamentId, Long facturaId) {
        if (facturaId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati selectat o factura");
        if (apartamentId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus un apartament");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (apartamentRepository.findById(apartamentId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus apartament");
        Apartament apartament1 = apartamentRepository.findById(apartamentId).get();
        if (apartament1.getUser().getId()!=userRepository.findByUser(email).get().getId())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus.");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        if(plataIntretinereRepository.getByIdAndUserAndApartament(apartamentId,user.getId(),facturaId).isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Factura nu exista.");
        List<PlataIntretinereDetalii> plataIntretinereDetaliiList = plataIntretinereRepository.getByIdAndUserAndApartamentAndFactura(apartamentId,user.getId(),facturaId);
        List<FacturaDateDetaliiDto> facturaDateDetaliiDtoList = new ArrayList<>();
        for (PlataIntretinereDetalii plataIntretinereDetalii: plataIntretinereDetaliiList
        ) {
            FacturaDateDetaliiDto facturaDateDetaliiDto = new FacturaDateDetaliiDto();
            facturaDateDetaliiDto.setDetalii(plataIntretinereDetalii.getTextDetalii());
            facturaDateDetaliiDto.setValoare(Double.valueOf(df.format(plataIntretinereDetalii.getSuma())));
            facturaDateDetaliiDtoList.add(facturaDateDetaliiDto);
        }
        return  facturaDateDetaliiDtoList;
    }

    public ResponseEntity<Object> plataFacturaApartament(OAuth2Authentication auth2Authentication, PlataFacturaDeLocatar plataFacturaDto, Long apartamentId, Long facturaId) {
        if (plataFacturaDto.getAnExpirare().toString().isEmpty()){
            mesaj.setMessage("Nu ati introdus anul expirarii cardului");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if (plataFacturaDto.getLunaExpirare().toString().isEmpty()){
            mesaj.setMessage("Nu ati introdus luna expirarii cardului");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if (plataFacturaDto.getCvx().toString().isEmpty()){
            mesaj.setMessage("Nu ati introdus codul CVC");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if (plataFacturaDto.getCard().isEmpty()){
            mesaj.setMessage("Nu ati introdus numarul cardului");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        Date data = new Date();
        Calendar c= Calendar.getInstance();
        Calendar ac=Calendar.getInstance();
        Date acum = ac.getTime();
        c.setTime(data);
        c.set(Calendar.YEAR, plataFacturaDto.getAnExpirare());
        c.set(Calendar.MONTH, plataFacturaDto.getLunaExpirare());
        Date d=c.getTime();
        if(d.before(acum)){
            mesaj.setMessage("Cardul a expirat.");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }

        if (apartamentId.toString().isEmpty()){
            mesaj.setMessage("Nu ati introdus apartamentul");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if (facturaId.toString().isEmpty()) {
            mesaj.setMessage("Nu ati selectat factura");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(valideazaCard(plataFacturaDto.getCard())){
            mesaj.setMessage("Card invalid");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (apartamentRepository.findById(apartamentId).isEmpty()) {
            mesaj.setMessage("Nu ati introdus apartament");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        Apartament apartament1 = apartamentRepository.findById(apartamentId).get();
        if (apartament1.getUser().getId()!=userRepository.findByUser(email).get().getId()){
            mesaj.setMessage("Nu aveti acces la acest apartament");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        if(plataIntretinereRepository.getByIdAndUserAndApartament(apartamentId,user.getId(),facturaId).isEmpty()){
            mesaj.setMessage("Factura nu exista");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        PlataIntretinere plataIntretinere = plataIntretinereRepository.getByIdAndUserAndApartament(apartamentId,user.getId(),facturaId).get();
        double sumaRamasa=restantaFactura(plataIntretinere.getSuma(), plataIntretinere.getId());
        if(plataFacturaDto.getValoare()>Double.valueOf(df.format(plataIntretinere.getSuma()+sumaRamasa))){
            mesaj.setMessage("Ati introdus o suma mai mare");
            mesaj.setCode(406);
            return new ResponseEntity<>(mesaj, HttpStatus.NOT_ACCEPTABLE);
        }
        if(plataFacturaDto.getValoare()==Double.valueOf(df.format(plataIntretinere.getSuma()+sumaRamasa))) {
            PlataIntretinere plataIntretinereAchitat = plataIntretinereRepository.getById(plataIntretinere.getId());
            plataIntretinereAchitat.setAchitatComplet(1);
            plataIntretinereRepository.save(plataIntretinereAchitat);
        }
        String serieChitanta = "ASC2022";
        Chitanta chitanta=new Chitanta();
        chitanta.setDetalii("Card online.");
        chitanta.setPlataIntretinere(plataIntretinere);
        chitanta.setSuma(plataFacturaDto.getValoare());
        chitanta.setSerie_chitanta(serieChitanta);
        chitanta.setData_achitare(new Date());
        chitanta.setNumar_chitanta(0);
        chitantaRepository.save(chitanta);
        mesaj.setMessage("Factura a fost achitata");
        mesaj.setCode(200);
        return new ResponseEntity<>(mesaj, HttpStatus.OK);
    }

    public List<ChitanteDto> getFacturaChitanteApartament(OAuth2Authentication auth2Authentication, Long apartamentId, Long facturaId) {
        if (facturaId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati selectat o factura");
        if (apartamentId.toString().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus un apartament");
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        String email = userPrinciple.getUsername();
        if (apartamentRepository.findById(apartamentId).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus apartament");
        Apartament apartament1 = apartamentRepository.findById(apartamentId).get();
        if (!apartament1.getUser().getId().equals(userRepository.findByUser(email).get().getId()))throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu ati introdus.");
        User user = userRepository.findByUser(userPrinciple.getUsername()).get();
        if(plataIntretinereRepository.getByIdAndUserAndApartament(apartamentId,user.getId(),facturaId).isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Factura nu exista");
        List<Chitanta> chitantaList = chitantaRepository.findAllByPlataIntretinereId(facturaId);
        List<ChitanteDto> chitanteDtoList = new ArrayList<>();
        for (Chitanta chitanta : chitantaList
        ) {
            ChitanteDto chitantaDto = new ChitanteDto();
            chitantaDto.setDetalii(chitanta.getDetalii());
            chitantaDto.setId(chitanta.getId());
            chitantaDto.setSerieChitanta(chitanta.getSerie_chitanta());
            chitantaDto.setSuma(chitanta.getSuma());
            chitantaDto.setDataAchitare(chitanta.getData_achitare());
            chitanteDtoList.add(chitantaDto);
        }
        return chitanteDtoList;
    }

    private boolean valideazaCard(String str){
        char primulNumar = str.charAt(0);
        return switch (primulNumar) {
            case 3 -> CreditCardValidator.AMEX_VALIDATOR.isValid(str);
            case 4 -> CreditCardValidator.VISA_VALIDATOR.isValid(str);
            case 5 -> CreditCardValidator.MASTERCARD_VALIDATOR.isValid(str);
            case 6 -> CreditCardValidator.DISCOVER_VALIDATOR.isValid(str);
            default -> false;
        };
    }
}
