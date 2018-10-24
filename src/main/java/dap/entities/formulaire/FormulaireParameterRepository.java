package dap.entities.formulaire;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class FormulaireParameterRepository {
    public FormulaireParameter getFormulaireParameter() {
        return formulaireParameter;
    }

    public void setFormulaireParameter(FormulaireParameter formulaireParameter) {
        this.formulaireParameter = formulaireParameter;
    }

    static FormulaireParameter formulaireParameter;

    public static List<FormulaireParameter> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select f from FormulaireParameter f").getResultList()
        );
    }

    public static FormulaireParameter save(FormulaireParameter formulaireParameter) {
        return JPAService.runInTransaction(em -> em.merge(formulaireParameter));
    }

    public static FormulaireParameter add(FormulaireParameter myFormulaireParameter) {
        JPAService.runInTransaction(em -> {
            em.persist(myFormulaireParameter);
            FormulaireParameterRepository.formulaireParameter = myFormulaireParameter;
            return null;
        });
        return FormulaireParameterRepository.formulaireParameter;
    }


    public static void delete(FormulaireParameter myFormulaireParameter) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((String) myFormulaireParameter.getCodeParameter(), em));
            return null;
        });
    }

    public static FormulaireParameter getById(String id, EntityManager em) {
        Query query = em.createQuery("select u from FormulaireParameter u where u.id=:id");
        query.setParameter("id", id);

        return (FormulaireParameter) query.getResultList().stream().findFirst().orElse(null);
    }

    public static List<String> listAllFormulaireParameter( EntityManager em) {
        Query query = em.createQuery("select distinct name from FormulaireParameter u");
        List<String> listeDesFormulaireParameter = query.getResultList();
        return listeDesFormulaireParameter;
    }


}
