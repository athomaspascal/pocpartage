package dap.entities.formulaire;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "FORMULAIRE_VALUE")
public class FormulaireValue {
    private int id;
    private String codeValue;
    private String libelleValue;
    private FormulaireParameter parameter;

    @Column(name ="FORMULAIREPARAMETER_CODE")
    public String getCodeParameter() {
        return codeParameter;
    }

    public void setCodeParameter(String codeParameter) {
        this.codeParameter = codeParameter;
    }

    private String codeParameter;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = FormulaireParameter.class)
    public FormulaireParameter getFormulaireParameter() {
        return parameter;
    }

    public void setFormulaireParameter(FormulaireParameter formulaireParameter) {
        this.parameter = formulaireParameter;
    }

    @Basic
    @Column(name = "CODE_VALUE", nullable = true, length = 50)
    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    @Basic
    @Column(name = "LIBELLE_VALUE", nullable = true, length = 50)
    public String getLibelleValue() {
        return libelleValue;
    }

    public void setLibelleValue(String libelleValue) {
        this.libelleValue = libelleValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormulaireValue that = (FormulaireValue) o;
        return id == that.id &&
                Objects.equals(codeValue, that.codeValue) &&
                Objects.equals(libelleValue, that.libelleValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codeValue, libelleValue);
    }
}
