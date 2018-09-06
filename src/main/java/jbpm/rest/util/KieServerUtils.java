package jbpm.rest.util;

import java.util.HashSet;
import java.util.Set;

import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.QueryServicesClient;
import org.kie.server.client.UserTaskServicesClient;

public class KieServerUtils {

//    private static final String BASE_URL = "http://localhost:8080/kie-server/services/rest/server";
    private static final String BASE_URL = "http://192.168.56.101:8080/kie-server/services/rest/server";
    private static final String DEFAULT_USERNAME = "eric";
//    private static final String DEFAULT_USERNAME = "sunny";
    private static final String DEFAULT_PASSWORD = "p@ssword123";

//    public static final String COMTAINER_ID = "MyFirstContainer";
//    public static final String COMTAINER_ID = "npou:npproj42:1.0";
    
    public static KieServicesClient getKieServicesClient() {
    	// create configuration
        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(
        	BASE_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD); 
        
        // other formats supported MarshallingFormat.JSON or MarshallingFormat.XSTREAM
        config.setMarshallingFormat(MarshallingFormat.JAXB);
	    // in case of custom classes shall be used they need to be added and 
        // client needs to be created with class loader that has these classes available 
        Set<Class<?>> extraClassList = new HashSet<Class<?>>();
//        extraClassList.add(timesheet.class);
        config.addExtraClasses(extraClassList);
        
        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
        return client;
    }

    public static ProcessServicesClient getProcessServiceClient() {
        return getKieServicesClient().getServicesClient(ProcessServicesClient.class);
    }
    
    public static UserTaskServicesClient getUserTaskServiceClient() {
        return getKieServicesClient().getServicesClient(UserTaskServicesClient.class);
    }

    public static QueryServicesClient getQueryServicesClient() {
        return KieServerUtils.getKieServicesClient().getServicesClient(QueryServicesClient.class);
    }

/*    public static ProcessServicesClient getProcessServiceClient(String username, String password) {

        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(BASE_URL, username, password);
        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);

        ProcessServicesClient proessServicesClient = client.getServicesClient(ProcessServicesClient.class);

        return proessServicesClient;
    }*/


/*    public static UserTaskServicesClient getUserTaskServiceClient(String username, String password) {

        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(BASE_URL, username, password);
        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);

        UserTaskServicesClient userTaskServiceClient = client.getServicesClient(UserTaskServicesClient.class);

        return userTaskServiceClient;
    }*/
}
