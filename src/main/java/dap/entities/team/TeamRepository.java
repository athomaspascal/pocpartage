package dap.entities.team;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author
 */
public class TeamRepository {

    public static List<Team> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select p from Team p").getResultList()
        );
    }

    public static Team save(Team team) {
        return JPAService.runInTransaction(em -> em.merge(team));
    }

    public static Team add(Team team) {
        Team newTeam = new Team();
        newTeam.setUserDivision(team.getUserDivision());
        newTeam.setTeamBossEmail(team.getTeamBossEmail());
        newTeam.setTeamBossName(team.getTeamBossName());
        newTeam.setNomteam(team.getNomteam());
        return JPAService.runInTransaction(em -> em.merge(newTeam));
    }

    public static void delete(Team team) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((long) team.getId(), em));
            return null;
        });
    }

    public static Team getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from Team u where u.id=:id");
        query.setParameter("id", id);

        return (Team) query.getResultList().stream().findFirst().orElse(null);
    }

    public static List<String> listAllTeam( EntityManager em) {
        Query query = em.createQuery("select distinct nomteam from Team u");
        List<String> listeDesTeam = query.getResultList();
        return listeDesTeam;
    }

}
