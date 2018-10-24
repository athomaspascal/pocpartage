package dap.entities.team;

import dap.entities.enterprise.Division;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(  name = "TEAM")
    public class Team {
    @NotNull
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nomteam;
    private String teamBossName;
    private String teamBossEmail;

    @ManyToOne
    private Division userDivision;

    @Basic
    @Column(name = "TEAMBOSSEMAIL", nullable = true, length = 50)
    public String getTeamBossEmail() {
        return teamBossEmail;
    }
    public void setTeamBossEmail(String teamBossEmail) {
        this.teamBossEmail = teamBossEmail;
    }

    @Basic
    @Column(name = "TEAMBOSSNAME", nullable = true, length = 50)
    public String getTeamBossName() {
        return teamBossName;
    }
    public void setTeamBossName(String teamBossName) {
        this.teamBossName = teamBossName;
    }

    @Basic
    @Column(name = "NOMTEAM", nullable = true, length = 50)
    public String getNomteam() {
        return nomteam;
    }
    public void setNomteam(String nomTeam) {
        this.nomteam = nomTeam;
    }


    @ManyToOne
    public Division getUserDivision() {
        return userDivision;
    }
    public void setUserDivision(Division userDivision) {
        this.userDivision = userDivision;
    }

    @GeneratedValue
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id &&
                Objects.equals(nomteam, team.nomteam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomteam, id);
    }
}
