package dap.entities.formulaire;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class FormulaireValueRepository {
    public FormulaireValue getFormulaireValue() {
        return formulaireValue;
    }

    public void setFormulaireValue(FormulaireValue formulaireValue) {
        this.formulaireValue = formulaireValue;
    }

    static FormulaireValue formulaireValue;

    public static List<FormulaireValue> listValues() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select f from FormulaireValue f").getResultList()
        );
    }

    public static List<String> listValues(String CodeParameter, EntityManager em) {
        Query query = em.createQuery("select libelleValue " +
                "from FormulaireValue u " +
                "where u.codeParameter=:CodeParameter");
        query.setParameter("CodeParameter", CodeParameter);
        return (List<String>) query.getResultList();
    }


    public static FormulaireValue save(FormulaireValue formulaireValue) {
        return JPAService.runInTransaction(em -> em.merge(formulaireValue));
    }

    public static FormulaireValue add(FormulaireValue myFormulaireValue) {
        JPAService.runInTransaction(em -> {
            em.persist(myFormulaireValue);
            FormulaireValueRepository.formulaireValue = myFormulaireValue;
            return null;
        });
        return FormulaireValueRepository.formulaireValue;
    }


    public static void delete(FormulaireValue myFormulaireValue) {
        JPAService.runInTransaction(em -> {
            em.remove(getById(myFormulaireValue.getId(), em));
            return null;
        });
    }

    public static FormulaireValue getById(int id, EntityManager em) {
        Query query = em.createQuery("select u " +
                "from FormulaireValue u " +
                "where u.id=:id");
        query.setParameter("id", id);

        return (FormulaireValue) query.getResultList().stream().findFirst().orElse(null);
    }

    public static List<String> listAllFormulaireValue( EntityManager em) {
        Query query = em.createQuery("select distinct name from FormulaireValue u");
        List<String> listeDesFormulaireValue = query.getResultList();
        return listeDesFormulaireValue;
    }


}
