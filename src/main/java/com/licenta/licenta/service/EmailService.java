package com.licenta.licenta.service;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    public void trimiteMail(String toAddress, String subiect, String mesaj) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("asociatiadelocatarict@gmail.com");
        email.setTo(toAddress);
        email.setSubject(subiect);
        email.setText(mesaj);
        javaMailSender.send(email);
    }
}
