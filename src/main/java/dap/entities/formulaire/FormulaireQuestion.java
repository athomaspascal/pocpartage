package dap.entities.formulaire;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "FORMULAIRE_QUESTION")
public class FormulaireQuestion {
    @NotNull
    @Id
    @Column(name = "ID", length = 50)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "ID_FORMULAIRE", nullable = true, length = 50)
    int idFormulaire;

    @Column(name = "CODE_PARAMETER", nullable = true, length = 50)
    String codeParameter;
    @Column(name = "LIBELLE", nullable = true, length = 50)
    String libelleField;
    @Column(name = "DESCRIPTION", nullable = true, length = 50)
    String descriptionField;
    @Column(name = "FLAG_MANDATORY", nullable = true, length = 50)
    int flagObligatoire;
    @Column(name = "QUESTION_ORDER", nullable = true, length = 50)
    int questionOrder;
    @Column(name="LARGEUR", nullable = true)
    String largeur;
    @Column(name="TYPE_FIELD", nullable = true)
    String typeField;

    public int getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(int questionOrder) {
        this.questionOrder = questionOrder;
    }


    public String getTypeField() {
        return typeField;
    }

    public void setTypeField(String typeField) {
        this.typeField = typeField;
    }


    public String getLargeur() {
        return largeur;
    }

    public void setLargeur(String largeur) {
        this.largeur = largeur;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeParameter() {
        return codeParameter;
    }

    public void setCodeParameter(String codeParameter) {
        this.codeParameter = codeParameter;
    }

    public String getLibelleField() {
        return libelleField;
    }

    public void setLibelleField(String libelleField) {
        this.libelleField = libelleField;
    }

    public String getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(String descriptionField) {
        this.descriptionField = descriptionField;
    }

    public int getFlagObligatoire() {
        return flagObligatoire;
    }

    public void setFlagObligatoire(int flagObligatoire) {
        this.flagObligatoire = flagObligatoire;
    }

    public int getIdFormulaire() {
        return idFormulaire;
    }

    public void setIdFormulaire(int idFormulaire) {
        this.idFormulaire = idFormulaire;
    }
}
