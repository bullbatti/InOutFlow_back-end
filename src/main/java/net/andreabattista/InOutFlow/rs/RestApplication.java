package net.andreabattista.InOutFlow.rs;

import com.fasterxml.jackson.core.util.JacksonFeature;
import net.andreabattista.InOutFlow.business.SmartCardReader;
import net.andreabattista.InOutFlow.dao.BaseDao;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Configures the JAX-RS application and provides the set of resource classes to be registered.
 * This class extends {@link javax.ws.rs.core.Application} and is annotated with {@link javax.ws.rs.ApplicationPath}
 * to specify the base path for the RESTful web services provided by this application.
 */
@ApplicationPath("/api")
public class RestApplication extends Application {
    
    /**
     * Retrieves the set of resource classes to be registered by this application.
     *
     * @return A set containing the resource classes to be registered.
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        classes.add(LoginResource.class);
        classes.add(TrackingResource.class);
        classes.add(EmployeeResource.class);
        classes.add(SmartCardResource.class);
        classes.add(MessageResource.class);
        classes.add(CompanyResource.class);
        
        classes.add(JacksonConfiguration.class);
        classes.add(HeadersFilter.class);
        
        BaseDao.initFactory("DefaultPersistenceUnit");

        /*
        Algorithm algorithm = Algorithm.HMAC256("inoutflow");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Baeldung")
                .build();
         */
        
        return classes;
    }
}
