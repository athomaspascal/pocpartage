package dap.entities.user;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class UserRepository {

    public static List<User> findAllOderById(String filter) {
        System.out.println("Filtre:"+ filter);
        if (filter.equalsIgnoreCase(""))
            return JPAService.runInTransaction(em ->
                    em.createQuery("select u from User u order by id").getResultList()
            );
        else {
            String requete =" select  u " +
                    " from User u order by id" +
                    " where teamid in (select t.id " +
                    " from Team t" +
                    " where t.nomteam = '" + filter + "')";
            return JPAService.runInTransaction(em ->
                    em.createQuery(requete).getResultList());

        }

    }

    public static List<User> findAll(String filter) {
        System.out.println("Filtre:"+ filter);
        if (filter.equalsIgnoreCase(""))
            return JPAService.runInTransaction(em ->
                em.createQuery("select u from User u").getResultList()
            );
        else {
            String requete =" select  u " +
                    " from User u " +
                    " where teamid in (select t.id " +
                    " from Team t" +
                    " where t.nomteam = '" + filter + "')";
            return JPAService.runInTransaction(em ->
                em.createQuery(requete).getResultList());

        }

    }

    public static User save(User user) {

        if (user.getDateCreation() == null)
            user.setDateCreation(new Date());
        return JPAService.runInTransaction(em -> em.merge(user)

        );
    }

    public static User add(User user) {
        User newUser = new User();

        return JPAService.runInTransaction(em -> em.merge(newUser));
    }

    public static void delete(User user) {
        JPAService.runInTransaction(em -> {
            em.remove(getById(user.getId(), em));
            return null;
        });
    }

    public static User getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from User u where u.id=:id");
        query.setParameter("id", id);

        return (User) query.getResultList().stream().findFirst().orElse(null);
    }


    public static User getByName(String name, EntityManager em) {
        Query query = em.createQuery("select u from User u where u.Nom =:name");
        query.setParameter("name", name);

        return (User) query.getResultList().stream().findFirst().orElse(null);
    }
}
