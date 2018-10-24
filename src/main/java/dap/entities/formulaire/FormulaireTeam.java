package dap.entities.formulaire;


import dap.entities.team.Team;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "formulaire_team")
public class FormulaireTeam {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NotNull
    @Id
    @GeneratedValue
    private long id;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormulaireTeam formulaireTeam = (FormulaireTeam) o;
        return id == formulaireTeam.id &&
                Objects.equals(formulaire, formulaireTeam.formulaire) &&
                Objects.equals(team, formulaireTeam.team);
    }

    @ManyToOne
    private Formulaire formulaire;

    @ManyToOne
    private Team team;


    @Override
    public int hashCode() {
        return Objects.hash(id, formulaire, team);
    }




}
