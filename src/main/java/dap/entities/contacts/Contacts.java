package dap.entities.contacts;


import dap.entities.team.Team;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "contacts")
public class Contacts {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = true, length = 50)
    private long id;
    @Basic
    @Column(name = "NAME", nullable = true, length = 50)
    private String name;
    @Basic
    @Column(name = "CONTACT_EMAIL", nullable = true, length = 50)
    private String contactEmail;


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contacts contacts = (Contacts) o;
        return id == contacts.id &&
                Objects.equals(name, contacts.name) &&
                Objects.equals(contactEmail, contacts.contactEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contactEmail);
    }


    @ManyToOne
    private Team team;


    public Team getTeam() {
        return team;
    }

    public void setTeam(Team myteam) {
        team= myteam;
    }

}
