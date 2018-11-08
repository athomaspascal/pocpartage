package dap.entities.actions;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author
 */
public class HistoryActionsRepository {
    public HistoryActions getHistoryActionsResult() {
        return historyActions;
    }

    public void setHistoryActionsResult(HistoryActions historyActionsResult) {
        this.historyActions = historyActionsResult;
    }

    static HistoryActions historyActions;


    public static List<HistoryActions> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select p from HistoryActions p").getResultList()
        );
    }

    public static HistoryActions save(HistoryActions historyActions) { 
            return JPAService.runInTransaction(em -> em.merge(historyActions));
    }



    public static HistoryActions add(HistoryActions historyActions) {
        JPAService.runInTransaction(em -> {
            em.persist(historyActions);
            HistoryActionsRepository.historyActions = historyActions;
            return null;
        });
        return HistoryActionsRepository.historyActions;
    }


    public static void delete(HistoryActions historyActions) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((long) historyActions.getId(), em));
            return null;
        });
    }

    private static HistoryActions getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from HistoryActions u where u.id=:id");
        query.setParameter("id", id);

        return (HistoryActions) query.getResultList().stream().findFirst().orElse(null);
    }

    public static List<String> listAllHistoryActions( EntityManager em) {
        Query query = em.createNativeQuery("select  TO_CHAR(DATE_ACTION,'DD/MM/YYYY') || ':' || SERVER_NAME || ':' || ACTION || ':' " +
                "from HISTORY_ACTIONS u");
        List<String> listeDesHistoryActions = query.getResultList();
        return listeDesHistoryActions;
    }

    public static HistoryActions listAllHistoryActions(String myServerName,EntityManager em) {
        Query query = em.createQuery("select u " +
                " from HistoryActions u " +
                " where u.serverName =:myServerName");
        query.setParameter("myServerName", myServerName);
        List<HistoryActions> listeDesHistoryActions = query.getResultList();

        return listeDesHistoryActions.get(0);
    }

}
