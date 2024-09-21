package net.andreabattista.InOutFlow.rs;

import net.andreabattista.InOutFlow.model.SmartCard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginResourceTest {
    private static EntityManager entityManager;

    @BeforeClass
    public static void init() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("TestPersistenceUnit");
        entityManager = factory.createEntityManager();

        if (entityManager != null)
            return;
    }

    @Test @Order(0)
    public void smartCard() {
        entityManager.getTransaction().begin();

        SmartCard smartCard = new SmartCard();
        smartCard.setUniversalId("12345");
        entityManager.persist(smartCard);
        entityManager.getTransaction().commit();
        SmartCard saved = entityManager.find(SmartCard.class, smartCard.getId());
        Assertions.assertNotNull(saved);
        Assertions.assertEquals("12345", saved.getUniversalId());
    }
}
