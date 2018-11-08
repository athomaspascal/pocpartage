package dap.entities.actions;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import java.util.List;

public class testRepository {
    public static void main(String[] args) {
        EntityManager entityManager = JPAService.getFactory().createEntityManager();
        List<String> strings = HistoryActionsRepository.listAllHistoryActions(entityManager);
        for(String s:strings){
            System.out.println(s);
        }
        entityManager.close();
        System.exit(0);
    }
}
