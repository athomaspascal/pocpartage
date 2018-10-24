package dap.entities.formulaire;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "formulaire_list")
public class Formulaire {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "LIBELLE_FORMULAIRE", nullable = true, length = 50)
    String libelleFormulaire;

    @Column(name = "DATE_CREATION", nullable = true, length = 50)
    Date dateCreation;

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateFormulaire) {
        this.dateCreation = dateFormulaire;
    }


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelleFormulaire() {
        return libelleFormulaire;
    }

    public void setLibelleFormulaire(String libelleFormulaire) {
        this.libelleFormulaire = libelleFormulaire;
    }



}
