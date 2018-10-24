package dap.entities.contacts;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author
 */
public class ContactsRepository {
    public Contacts getContactsResult() {
        return contactsResult;
    }

    public void setContactsResult(Contacts contactsResult) {
        this.contactsResult = contactsResult;
    }

    static Contacts contactsResult;


    public static List<Contacts> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select p from Contacts p").getResultList()
        );
    }

    public static Contacts save(Contacts contacts) { 
            return JPAService.runInTransaction(em -> em.merge(contacts));
    }



    public static Contacts add(Contacts contacts) {
        JPAService.runInTransaction(em -> {
            em.persist(contacts);
            ContactsRepository.contactsResult = contacts;
            return null;
        });
        return ContactsRepository.contactsResult;
    }


    public static void delete(Contacts contacts) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((long) contacts.getId(), em));
            return null;
        });
    }

    private static Contacts getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from Contacts u where u.id=:id");
        query.setParameter("id", id);

        return (Contacts) query.getResultList().stream().findFirst().orElse(null);
    }

    public static List<String> listAllContacts( EntityManager em) {
        Query query = em.createQuery("select distinct name from Contacts u");
        List<String> listeDesContacts = query.getResultList();
        return listeDesContacts;
    }

}
