package dap.entities.formulaire;

import dap.entities.JPAService;

import javax.persistence.EntityManager;

public class test {
    public static void main(String[] args) {
        JPAService.init();
        EntityManager em = JPAService.getFactory().createEntityManager();
        //User u = UserRepository.getById(1L, em);

        FormulaireResultatRepository.getById(1185,JPAService.getFactory().createEntityManager());

        JPAService.close();
    }
}
