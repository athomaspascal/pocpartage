package dap.entities.formulaire;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class FormulaireReponseRepository {
    public FormulaireReponse getFormulaireReponse() {
        return formulaireReponse;
    }

    public void setFormulaireReponse(FormulaireReponse formulaireReponse) {
        this.formulaireReponse = formulaireReponse;
    }

    static FormulaireReponse formulaireReponse;

    public static List<FormulaireReponse> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select f from FormulaireReponse f").getResultList()
        );
    }

    public static FormulaireReponse save(FormulaireReponse formulaireReponse) {
        return JPAService.runInTransaction(em -> em.merge(formulaireReponse));
    }

    public static FormulaireReponse add(FormulaireReponse myFormulaireReponse) {
        JPAService.runInTransaction(em -> {
            em.persist(myFormulaireReponse);
            FormulaireReponseRepository.formulaireReponse = myFormulaireReponse;
            return null;
        });
        return FormulaireReponseRepository.formulaireReponse;
    }


    public static void delete(FormulaireReponse myFormulaireReponse) {
        JPAService.runInTransaction(em -> {
            em.remove(getById( myFormulaireReponse.getId(), em));
            return null;
        });
    }

    public static FormulaireReponse getById(int id, EntityManager em) {
        Query query = em.createQuery("select u from FormulaireReponse u where u.id=:id");
        query.setParameter("id", id);

        return (FormulaireReponse) query.getResultList().stream().findFirst().orElse(null);
    }

    public static FormulaireReponse getByIdAndNumQuestion(int id, int numQ,EntityManager em) {
        Query query = em.createQuery("select u " +
                "from FormulaireReponse u " +
                "where u.id=:id " +
                "and u.idQuestion = :numQ");
        query.setParameter("id", id);
        query.setParameter("numQ", numQ);

        return (FormulaireReponse) query.getResultList().stream().findFirst().orElse(null);
    }

    public static List<String> listAllFormulaireReponse( EntityManager em) {
        Query query = em.createQuery("select distinct name from FormulaireReponse u");
        List<String> listeDesFormulaireReponse = query.getResultList();
        return listeDesFormulaireReponse;
    }


}
