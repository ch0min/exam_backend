package datafacades;

import entities.*;
import errorhandling.API_Exception;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TenantFacadeTest {
    private static EntityManagerFactory emf;
    private static TenantFacade facade;

    Role userRole, adminRole;
    User admin, user, user2, user3, user4, user5;
    House house1, house2, house3;
    Rental rental1, rental2, rental3;
    Tenant tenant1, tenant2, tenant3, tenant4;

    public TenantFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = TenantFacade.getTenantFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("EXECUTION OF ALL TESTS IN TENANTFACADETEST DONE");
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Rental.deleteAllRows").executeUpdate();
            em.createNamedQuery("Tenant.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("House.deleteAllRows").executeUpdate();

            adminRole = new Role("admin");
            userRole = new Role("user");


            admin = new User("admin", "admin@gmail.com", "test123");
            user = new User("user", "user@gmail.com", "test123");
            user2 = new User("mark", "mark@gmail.com", "test123");
            user3 = new User("nick", "nick@gmail.com", "test123");
            user4 = new User("fido", "fido@gmail.com", "test123");
            user5 = new User("perle", "perle@gmail.com", "test123");

            house1 = new House("Lyngborgvej 3", "Kastrup", 5);
            house2 = new House("Kløvervej 7", "Kongens Lyngby", 3);
            house3 = new House("Tvingsager 10", "Hvidovre", 4);

            rental1 = new Rental("18-01-2023", "18-01-2030", 144000, 30000, "Susanne Lundgaard", house1);
            rental2 = new Rental("01-01-2020", "01-01-2025", 199000, 50000, "Briand Jensen", house2);
            rental3 = new Rental("31-12-2019", "31-12-2028", 122000, 25000, "Olfert Treflo", house3);

            tenant1 = new Tenant("Mark Lundgaard", 29842984, "Pakkemand", user2);
            tenant2 = new Tenant("Nick Jensen", 27332733, "Pædagog", user3);
            tenant3 = new Tenant("Fido Odif", 42070842, "McDonalds Medarbejder", user4);
            tenant4 = new Tenant("Perle Elrep", 98769876, "Lærer", user5);


            // Adding
            admin.addRole(adminRole);
            user.addRole(userRole);
            user2.addRole(adminRole);
            user2.addRole(userRole);
            user3.addRole(userRole);
            user4.addRole(userRole);
            user5.addRole(userRole);

            tenant1.addRental(rental1);
            tenant2.addRental(rental1);
            tenant3.addRental(rental2);
            tenant4.addRental(rental3);

            tenant1.addHouse(house1);
            tenant2.addHouse(house1);
            tenant3.addHouse(house2);
            tenant4.addHouse(house3);

            // Persisting
            em.persist(userRole);
            em.persist(adminRole);

            em.persist(user);
            em.persist(admin);
            em.persist(user2);
            em.persist(user3);
            em.persist(user4);
            em.persist(user5);

            em.persist(house1);
            em.persist(house2);
            em.persist(house3);

            em.persist(rental1);
            em.persist(rental2);
            em.persist(rental3);

            em.persist(tenant1);
            em.persist(tenant2);
            em.persist(tenant3);
            em.persist(tenant4);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
        System.out.println("EXECUTION OF TEST DONE");
    }


    @Test
    void getAllTenantsTest() throws API_Exception {
        System.out.println("Testing getAllTenants...");
        List<Tenant> actual = facade.getAllTenants();
        int expected = 4;
        assertEquals(expected, actual.size());
    }

    @Test
    void getTenantsByIDTest() throws API_Exception {
        System.out.println("Testing getAllTenantsByID...");
        Tenant tenant = facade.getTenantByID(tenant1.getTenantID());
        assertEquals(tenant1, tenant);
    }

    @Test
    void createTenantTest() throws API_Exception {
        System.out.println("Testing createRental...");
        Tenant newTenant = new Tenant("Fætter Guf", 55445544, "Agent", user2);
        facade.createTenant(newTenant);
        int actualSize = facade.getAllTenants().size();
        assertEquals(5, actualSize);
    }

}
