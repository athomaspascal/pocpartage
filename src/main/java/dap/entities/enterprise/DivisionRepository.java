package dap.entities.enterprise;

import dap.entities.JPAService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author
 */
public class DivisionRepository {
    private static Division divisionResult;
    static Logger logger = LogManager.getLogger("elastic-generator");

    public static List<Division> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select p from Division p").getResultList()
        );
    }

    public static Division save(Division division) {
        return JPAService.runInTransaction(em -> em.merge(division));
    }



    public static Division add(Division division,EntityManager em) {

        Division d= new Division();
        d.setDescription(division.getDescription());
        d.setNameDivision(division.getNameDivision());
        JPAService.runInTransaction(em2 -> {
            logger.info("ID DIVISION:" + d.getId());
            em2.persist(d);
            divisionResult = d;
            return null;
        });

        return divisionResult;

    }



    public static void delete(Division division) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((int) division.getId(), em));
            return null;
        });
    }

    public static Division getById(int id, EntityManager em) {
        Query query = em.createQuery("select u from Division u where u.id=:id");
        query.setParameter("id", id);

        return (Division) query.getResultList().stream().findFirst().orElse(null);
    }
}
