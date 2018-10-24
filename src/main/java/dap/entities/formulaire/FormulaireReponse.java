package dap.entities.formulaire;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "FORMULAIRE_REPONSE", schema = "PUBLIC", catalog = "H2")
public class FormulaireReponse {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "REPONSE", nullable = true, length = 2000)
    private String reponse;


    @Basic
    @Column(name = "ID_RESULTAT", nullable = true)
    private int idResultat;


    @Basic
    @Column(name = "ID_QUESTION", nullable = true)
    private int idQuestion;

    public FormulaireReponse()
    {

    }


    public FormulaireReponse (int idresultat, int idquestion, String rep)
    {
        idResultat = idresultat;
        idQuestion = idquestion ;
        reponse = rep;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormulaireReponse that = (FormulaireReponse) o;
        return id == that.id &&
                Objects.equals(reponse, that.reponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reponse);
    }

    public int getIdResultat() {
        return idResultat;
    }

    public void setIdResultat(int idreponse) {
        this.idResultat = idreponse;
    }


    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }
}
