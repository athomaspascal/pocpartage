package dap.entities.services;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


public class ServicesRepository {

    public static List<Services> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select p " +
                        "from Services p " +
                        "order by serviceFamily").getResultList()
        );
    }

    public static Services save(Services services) {
        return JPAService.runInTransaction(em -> em.merge(services));
    }
    public static void delete(Services services) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((long) services.getId(), em));
            return null;
        });
    }

    private static Services getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from Services u where u.id=:id");
        query.setParameter("id", id);

        return (Services) query.getResultList().stream().findFirst().orElse(null);
    }


}
