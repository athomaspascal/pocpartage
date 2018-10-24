package dap.entities.formulaire;

import dap.entities.JPAService;

import javax.persistence.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Entity
@Table(name = "FORMULAIRE_RESULTAT", schema = "PUBLIC", catalog = "H2")
public class FormulaireResultat {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "DATE_CREATION", nullable = true)
    private Date dateCreation;

    @Basic
    @Column(name = "TEAM_ID", nullable = true)
    private long idTeam;

    @ManyToOne
    private Formulaire formulaire;

    public String libelleComplet()
    {
        DateFormat dateformat = new SimpleDateFormat("dd/MM/YYYY");
        Date myDate = dateCreation;
        return dateformat.format(myDate) + ":" + formulaire.getLibelleFormulaire();
    }

    @ManyToOne
    public Formulaire getFormulaire() {
        return formulaire;
    }

    public void setFormulaire(Formulaire formulaire) {
        this.formulaire = formulaire;
    }

    public FormulaireResultat()
    {
    }

    public FormulaireResultat(long idteam,long idformulaire)
    {
        idTeam =idteam;
        //idFormulaire=idformulaire;
        EntityManager em = JPAService.getFactory().createEntityManager();
        formulaire = FormulaireRepository.getById(idformulaire,em);
        em.close();

        java.util.Date d1 = new java.util.Date();
        dateCreation = new Date(d1.getTime());
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }


    public long getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(long idTeam) {
        this.idTeam = idTeam;
    }
/*
    @Basic
    @Column(name = "FORMULAIRE_ID", nullable = true)
    public long getIdFormulaire() {
        return idFormulaire;
    }

    public void setIdFormulaire(long idFormulaire) {
        this.idFormulaire = idFormulaire;
    }
*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormulaireResultat that = (FormulaireResultat) o;
        return id == that.id &&
                Objects.equals(dateCreation, that.dateCreation) &&
                Objects.equals(idTeam, that.idTeam) /*&&
                Objects.equals(idFormulaire, that.idFormulaire) */;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateCreation, idTeam /*, idFormulaire */);
    }
}
