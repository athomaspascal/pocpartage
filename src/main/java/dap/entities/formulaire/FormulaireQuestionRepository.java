package dap.entities.formulaire;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class FormulaireQuestionRepository {
    public FormulaireQuestion getFormulaireQuestion() {
        return formulaireQuestion;
    }

    public void setFormulaireQuestion(FormulaireQuestion formulaireQuestion) {
        this.formulaireQuestion = formulaireQuestion;
    }

    static FormulaireQuestion formulaireQuestion;

    public static List<FormulaireQuestion> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select f from FormulaireQuestion f").getResultList()
        );
    }

    public static List<FormulaireQuestion> findAll(int numForulaire) {
        return JPAService.runInTransaction(em ->
                em.createQuery("select f from FormulaireQuestion f" +
                        " where idFormulaire = " + numForulaire).getResultList()
        );
    }
    public static List<FormulaireQuestion> findAllByFormulaire(String formulaireName) {
        if (JPAService.getFactory() == null)
            JPAService.init();
        return JPAService.runInTransaction(em ->
                em.createQuery("select f from FormulaireQuestion f,Formulaire fo" +
                        " where f.idFormulaire = fo.id" +
                        " and  fo.libelleFormulaire = '" + formulaireName + "'").getResultList()
        );
    }


    public static FormulaireQuestion save(FormulaireQuestion formulaireQuestion) {
        return JPAService.runInTransaction(em -> em.merge(formulaireQuestion));
    }

    public static FormulaireQuestion add(FormulaireQuestion myFormulaireQuestion) {
        JPAService.runInTransaction(em -> {
            em.persist(myFormulaireQuestion);
            FormulaireQuestionRepository.formulaireQuestion = myFormulaireQuestion;
            return null;
        });
        return FormulaireQuestionRepository.formulaireQuestion;
    }


    public static void delete(FormulaireQuestion myFormulaireQuestion) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((long) myFormulaireQuestion.getId(), em));
            return null;
        });
    }

    private static FormulaireQuestion getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from FormulaireQuestion u where u.id=:id");
        query.setParameter("id", id);

        return (FormulaireQuestion) query.getResultList().stream().findFirst().orElse(null);
    }

    public static List<String> listAllFormulaireQuestion(EntityManager em) {
        Query query = em.createQuery("select distinct name from FormulaireQuestion u");
        List<String> listeDesFormulaireQuestion = query.getResultList();
        return listeDesFormulaireQuestion;
    }


}
