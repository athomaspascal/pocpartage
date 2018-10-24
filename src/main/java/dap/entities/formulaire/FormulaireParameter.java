package dap.entities.formulaire;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "FORMULAIRE_PARAMETER")
public class FormulaireParameter {

    private String libelle;
    private String typeRepresentation;
    private String codeParameter;

    @Id
    @Column(name = "CODE", nullable = false, length = 50)
    public String getCodeParameter() {
        return codeParameter;
    }

    public void setCodeParameter(String codeParameter) {
        this.codeParameter = codeParameter;
    }

    @Basic
    @Column(name = "LIBELLE", nullable = true, length = 50)
    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Basic
    @Column(name = "TYPE_REPRESENTATION", nullable = true, length = 50)
    public String getTypeRepresentation() {
        return typeRepresentation;
    }

    public void setTypeRepresentation(String typeRepresentation) {
        this.typeRepresentation = typeRepresentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormulaireParameter that = (FormulaireParameter) o;
        return Objects.equals(codeParameter, that.codeParameter) &&
                Objects.equals(libelle, that.libelle) &&
                Objects.equals(typeRepresentation, that.typeRepresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeParameter, libelle, typeRepresentation);
    }
}
