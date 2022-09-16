package com.licenta.licenta.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="Roles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @Getter @Setter
    private String nume;

    public Role(Long id){
        this.id=id;
    }

}
