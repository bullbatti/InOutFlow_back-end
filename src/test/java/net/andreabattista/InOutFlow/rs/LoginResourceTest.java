package net.andreabattista.InOutFlow.rs;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andreabattista.InOutFlow.business.LoginManager;
import net.andreabattista.InOutFlow.dto.EmployeeDto;
import net.andreabattista.InOutFlow.dto.LoginDto;
import net.andreabattista.InOutFlow.model.Employee;
import net.andreabattista.InOutFlow.model.SmartCard;
import org.h2.engine.User;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.Dispatcher;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OrderBy;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginResourceTest {
    
    private static Dispatcher dispatcher;
    private static EntityManager entityManager;
    
    
    @BeforeAll
    public static void dispatcherInit() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        dispatcher.getProviderFactory().registerProvider(JacksonConfiguration.class);
        dispatcher.getRegistry().addSingletonResource(new LoginResource(), "/api");
        
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("TestPersistenceUnit");
        entityManager = factory.createEntityManager();
        
        entityManager.getTransaction().begin();
        
        Employee employee = new Employee();
        employee.setFirstName("Fabio");
        employee.setLastName("Rossi");
        employee.setBirthdate(LocalDate.of(2000, 4, 4));
        employee.setAccountType(Employee.Type.ADMINISTRATOR);
        employee.setPassword("test");
        employee.setPhoneNumber("1234567890");
        employee.setEmailAddress("fabiorossi@email.com");
        employee.setRollNumber("000000");
        
        entityManager.persist(employee);
        entityManager.getTransaction().commit();
    }
    
    @Test @Order(0)
    public void user() {
        entityManager.getTransaction().begin();
        
        Employee employee = new Employee();
        employee.setFirstName("Marco");
        employee.setLastName("Verdi");
        employee.setBirthdate(LocalDate.of(2000, 2, 17));
        employee.setAccountType(Employee.Type.USER);
        employee.setPassword("test");
        employee.setPhoneNumber("0987654321");
        employee.setEmailAddress("marcoverdi@email.com");
        employee.setRollNumber("111111");
        
        entityManager.persist(employee);
        entityManager.getTransaction().commit();
        
        Employee saved = entityManager.find(Employee.class, employee.getId());
        
        Assertions.assertNotNull(saved);
    }
    
    // LOGIN TESTS
    /**
     * Tests a correct login credentials provided by user
     *
     * @throws Exception for ObjectMapper and MockHttpRequest
     */
    @Test @Order(10)
    public void correctLogin() throws Exception {
        LoginDto dto = new LoginDto();
        dto.setEmail("fabiorossi@email.com");
        dto.setPassword("test");
        
        String json = new ObjectMapper().writeValueAsString(dto);
        
        MockHttpRequest request = MockHttpRequest.post("/api/login/").contentType(MediaType.APPLICATION_JSON)
            .content(json.getBytes(StandardCharsets.UTF_8));
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);
        
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertNotNull(response.getContentAsString());
    }
    
    /**
     * Tests a correct login credentials provided by user
     *
     * @throws Exception for ObjectMapper and MockHttpRequest
     */
    @Test
    public void incorrectLoginEmail() throws Exception {
        LoginDto dto = new LoginDto();
        dto.setEmail("fabiorossi@email.com");
        dto.setPassword("test");

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpRequest request = MockHttpRequest.post("/api/login/").contentType(MediaType.APPLICATION_JSON)
            .content(json.getBytes(StandardCharsets.UTF_8));
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        Assertions.assertEquals(401, response.getStatus());
    }
//
//    /**
//     * Tests a correct login credentials provided by user
//     *
//     * @throws Exception for ObjectMapper and MockHttpRequest
//     */
//    @Test
//    public void incorrectLoginPassword() throws Exception {
//        LoginDto dto = new LoginDto();
//        dto.setEmail("test@email.it");
//        dto.setPassword("pas");
//
//        String json = new ObjectMapper().writeValueAsString(dto);
//
//        MockHttpRequest request = MockHttpRequest.post("/api/login/").contentType(MediaType.APPLICATION_JSON)
//            .content(json.getBytes(StandardCharsets.UTF_8));
//        MockHttpResponse response = new MockHttpResponse();
//        dispatcher.invoke(request, response);
//
//        Assertions.assertEquals(401, response.getStatus());
//        Assertions.assertEquals("Invalid password", response.getContentAsString());
//    }
//
//    /**
//     * TODO
//     * @throws Exception
//     */
//
//    // GET USER INFORMATION AFTER LOGIN TESTS
//    @Test
//    public void getUserCorrect() throws Exception {
//        String token = "65a33caf-1b2b-4751-9d59-62b78930ac58";
//
//        MockHttpRequest request = MockHttpRequest.get("/api/login/user")
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", token);
//
//        MockHttpResponse response = new MockHttpResponse();
//        dispatcher.invoke(request, response);
//
//        Assertions.assertEquals(200, response.getStatus());
//    }
//
//    /**
//     * TODO
//     *
//     * @throws Exception
//     */
//    @Test
//    public void getUserUncorrect() throws Exception {
//        String token = "65a33caf-1b2b-4751-9d59-62b58";
//
//        MockHttpRequest request = MockHttpRequest.get("/api/login/user")
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", token);
//
//        MockHttpResponse response = new MockHttpResponse();
//        dispatcher.invoke(request, response);
//
//        Assertions.assertEquals(400, response.getStatus());
//    }
}
