package dap.entities.actions;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author
 */
public class ServersRepository {
    public Servers getServersResult() {
        return serversResult;
    }

    public void setServersResult(Servers serversResult) {
        this.serversResult = serversResult;
    }

    static Servers serversResult;


    public static List<Servers> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select p from Servers p").getResultList()
        );
    }

    public static Servers save(Servers servers) { 
            return JPAService.runInTransaction(em -> em.merge(servers));
    }



    public static Servers add(Servers servers) {
        JPAService.runInTransaction(em -> {
            em.persist(servers);
            ServersRepository.serversResult = servers;
            return null;
        });
        return ServersRepository.serversResult;
    }


    public static void delete(Servers servers) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((long) servers.getId(), em));
            return null;
        });
    }

    private static Servers getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from Servers u where u.id=:id");
        query.setParameter("id", id);

        return (Servers) query.getResultList().stream().findFirst().orElse(null);
    }

    public static List<String> listAllServers( EntityManager em) {
        Query query = em.createQuery("select distinct serverName from Servers u");
        List<String> listeDesServers = query.getResultList();
        return listeDesServers;
    }

    public static Servers listAllServers(String myServerName,EntityManager em) {
        Query query = em.createQuery("select u " +
                " from Servers u " +
                " where u.serverName =:myServerName");
        query.setParameter("myServerName", myServerName);
        List<Servers> listeDesServers = query.getResultList();

        return listeDesServers.get(0);
    }

}
