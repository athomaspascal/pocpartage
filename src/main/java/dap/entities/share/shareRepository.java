package dap.entities.share;

import dap.entities.JPAService;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class shareRepository {

    public static List<ShareSpace> findAllOderById(String filter) {
        System.out.println("Filtre:"+ filter);
        if (filter.equalsIgnoreCase(""))
            return JPAService.runInTransaction(em ->
                    em.createQuery("select u from ShareSpace u order by id").getResultList()
            );
        else {
            String requete =" select  u " +
                    " from ShareSpace u order by id" +
                    " where teamid in (select t.id " +
                    " from Team t" +
                    " where t.nomteam = '" + filter + "')";
            return JPAService.runInTransaction(em ->
                    em.createQuery(requete).getResultList());

        }

    }

    public static List<ShareSpace> findAll(String filter) {
        System.out.println("Filtre:"+ filter);
        if (filter.equalsIgnoreCase(""))
            return JPAService.runInTransaction(em ->
                em.createQuery("select u from ShareSpace u").getResultList()
            );
        else {
            String requete =" select  u " +
                    " from ShareSpace u " +
                    " where teamid in (select t.id " +
                    " from Team t" +
                    " where t.nomteam = '" + filter + "')";
            return JPAService.runInTransaction(em ->
                em.createQuery(requete).getResultList());

        }

    }

    public static ShareSpace save(ShareSpace shareSpace) {

        if (shareSpace.getDateCreation() == null)
            shareSpace.setDateCreation(new Date());
        return JPAService.runInTransaction(em -> em.merge(shareSpace)

        );
    }

    public static ShareSpace add(ShareSpace shareSpace) {
        ShareSpace newShareSpace = new ShareSpace();

        return JPAService.runInTransaction(em -> em.merge(newShareSpace));
    }

    public static void delete(ShareSpace shareSpace) {
        JPAService.runInTransaction(em -> {
            em.remove(getById(shareSpace.getId(), em));
            return null;
        });
    }

    public static ShareSpace getById(int id, EntityManager em) {
        Query query = em.createQuery("select u from ShareSpace u where u.id=:id");
        query.setParameter("id", id);

        return (ShareSpace) query.getResultList().stream().findFirst().orElse(null);
    }


    public static ShareSpace getByName(String name, EntityManager em) {
        Query query = em.createQuery("select u from ShareSpace u where u.Nom =:name");
        query.setParameter("name", name);

        return (ShareSpace) query.getResultList().stream().findFirst().orElse(null);
    }
}
