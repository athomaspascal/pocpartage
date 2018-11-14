package dap.entities.user;

import dap.entities.enterprise.Environnement;
import dap.entities.team.Team;
import dap.entities.product.Product;
import org.apache.bval.constraints.Email;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author
 */
@Entity
public class User {


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "NOM", nullable = false)
    private String Nom;

    @Past
    @Column(name = "DATECREATION", nullable = false)
    private Date dateCreation;

    @NotNull
    @Column(name = "MATRICULE", nullable = false)
    private int matricule;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6, max = 100)
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    @ManyToOne
    private Product mainProduct;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    private Set<Product> products = new HashSet<>();

    public Set<Environnement> getEnvironnements() {
        return environnements;
    }

    public void setEnvironnements(Set<Environnement> environnements) {
        this.environnements = environnements;
    }

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    private Set<Environnement> environnements = new HashSet<>();


    public Team getTeamid() {
        return teamid;
    }

    public void setTeamid(Team teamid) {
        this.teamid = teamid;
        /*
        if (teamid !=null)
            this.teamID = teamid.getId();*/
    }

    @ManyToOne
    private Team teamid;

    private Gender gender;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        this.Nom = nom;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMatricule() {
        return matricule;
    }

    public void setMatricule(int matricule) {
        this.matricule = matricule;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Product getMainProduct() {
        return mainProduct;
    }

    public void setMainProduct(Product mainProduct) {
        this.mainProduct = mainProduct;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
