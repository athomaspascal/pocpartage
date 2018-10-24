package dap.entities.formulaire;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class FormulaireTeamRepository {
    public FormulaireTeam getFormulaire() {
        return formulaire;
    }

    public void setFormulaire(FormulaireTeam formulaire) {
        this.formulaire = formulaire;
    }

    static FormulaireTeam formulaire;

    public static List<FormulaireTeam> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select f from FormulaireTeam f").getResultList()
        );
    }

    public static FormulaireTeam save(FormulaireTeam formulaire) {
        return JPAService.runInTransaction(em -> em.merge(formulaire));
    }

    public static FormulaireTeam add(FormulaireTeam myFormulaire) {

        JPAService.runInTransaction(em -> {
            em.persist(myFormulaire);
            FormulaireTeamRepository.formulaire = myFormulaire;
            return null;
        });
        return FormulaireTeamRepository.formulaire;
    }


    public static void delete(FormulaireTeam myFormulaire) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((long) myFormulaire.getId(), em));
            return null;
        });
    }

    private static FormulaireTeam getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from FormulaireTeam u where u.id=:id");
        query.setParameter("id", id);

        return (FormulaireTeam) query.getResultList().stream().findFirst().orElse(null);
    }

    public static List<String> listAllFormulaire( EntityManager em) {
        Query query = em.createQuery("select distinct name from FormulaireTeam u");
        List<String> listeDesFormulaire = query.getResultList();
        return listeDesFormulaire;
    }


}
