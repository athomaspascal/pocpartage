package dap.entities.enterprise;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Division {
    @NotNull
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "NAME_DIVISION", nullable = false, length = 50)
    private String nameDivision;
    private String description;


    @GeneratedValue
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NAME_DIVISION", nullable = false, length = 50)
    public String getNameDivision() {
        return nameDivision;
    }

    public void setNameDivision(String nameDivision) {
        this.nameDivision = nameDivision;
    }

    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 150)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Division division = (Division) o;
        return id == division.id &&
                Objects.equals(nameDivision, division.nameDivision) &&
                Objects.equals(description, division.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameDivision, description);
    }
}
