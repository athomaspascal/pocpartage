package dap.entities.formulaire;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class FormulaireResultatRepository {
    public FormulaireResultat getFormulaire() {
        return formulaireResultat;
    }

    public void setFormulaire(FormulaireResultat formulaireResultat) {
        this.formulaireResultat = formulaireResultat;
    }

    static FormulaireResultat formulaireResultat;

    public static List<FormulaireResultat> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select f from FormulaireResultat f").getResultList()
        );
    }

    public static List<FormulaireResultat> findAll(long newIdTeam) {
        return JPAService.runInTransaction(em ->
                em.createQuery("select f " +
                        "from FormulaireResultat f " +
                        " where idTeam = " + newIdTeam).getResultList()
        );
    }

    public static FormulaireResultat save(FormulaireResultat formulaireResultat) {
        return JPAService.runInTransaction(em -> em.merge(formulaireResultat));
    }

    public static FormulaireResultat add(FormulaireResultat myFormulaire) {
        JPAService.runInTransaction(em -> {
            em.persist(myFormulaire);
            FormulaireResultatRepository.formulaireResultat = myFormulaire;
            return null;
        });
        return FormulaireResultatRepository.formulaireResultat;
    }


    public static void delete(FormulaireResultat myFormulaire) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((int) myFormulaire.getId(), em));
            return null;
        });
    }

    public static FormulaireResultat getById(int id, EntityManager em) {
        Query query = em.createQuery("select u from FormulaireResultat u where u.id=:id");
        query.setParameter("id", id);

        return (FormulaireResultat) query.getResultList().stream().findFirst().orElse(null);
    }


    public static List<String> listAllFormulaire( EntityManager em) {
        Query query = em.createQuery("select distinct name from FormulaireResultat u");
        List<String> listeDesFormulaire = query.getResultList();
        return listeDesFormulaire;
    }


}
