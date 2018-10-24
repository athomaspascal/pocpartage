package dap.entities.enterprise;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "environnement")
public class Environnement {

    @NotNull
    @Id
    @GeneratedValue
    private Long id;

    public String getEnvironnementName() {
        return environnementName;
    }

    public void setEnvironnementName(String environnementName) {
        this.environnementName = environnementName;
    }

    @Basic
    @Column(name = "ENVIRONNEMENT_NAME", nullable = true, length = 50)
    @NotNull
    private String environnementName;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Environnement environnement = (Environnement) o;

        return id != null ? id.equals(environnement.id) : environnement.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}