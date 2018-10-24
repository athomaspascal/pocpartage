package dap.entities.enterprise;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author
 */
public class EnvironnementRepository {

    public static List<Environnement> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select p from Environnement p").getResultList()
        );
    }

    public static Environnement save(Environnement environnement) {
        return JPAService.runInTransaction(em -> em.merge(environnement));
    }
    public static void delete(Environnement environnement) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((long) environnement.getId(), em));
            return null;
        });
    }

    private static Environnement getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from Environnement u where u.id=:id");
        query.setParameter("id", id);

        return (Environnement) query.getResultList().stream().findFirst().orElse(null);
    }
}
