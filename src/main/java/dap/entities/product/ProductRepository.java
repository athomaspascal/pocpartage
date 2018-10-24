package dap.entities.product;

import dap.entities.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author
 */
public class ProductRepository {

    public static List<Product> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select p from Product p").getResultList()
        );
    }

    public static Product save(Product product) {
        return JPAService.runInTransaction(em -> em.merge(product));
    }
    public static void delete(Product product) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((long) product.getId(), em));
            return null;
        });
    }

    private static Product getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from Product u where u.id=:id");
        query.setParameter("id", id);

        return (Product) query.getResultList().stream().findFirst().orElse(null);
    }


}
