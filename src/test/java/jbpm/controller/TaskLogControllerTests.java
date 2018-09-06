package jbpm.controller;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@ContextConfiguration({
//	"file:src/main/webapp/WEB-INF/spring/root-context.xml",
	"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
	})
@RunWith(SpringJUnit4ClassRunner.class)
public class TaskLogControllerTests { 

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
/*       this.mockMvc = MockMvcBuilders.standaloneSetup(new Object[] {
    		   TaskLogController.class, 
    		   KieServerController.class,
    		   UserController.class, 
    		   PostController.class
       }).build();
       */
    	mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGetAllTaskLogs() throws Exception {
    	System.out.println("=======================================");
/*    	System.out.println(context.getBean("taskLogController"));
    	System.out.println(context.getBean("kieServerController"));*/
    	
        mockMvc.perform(get("/tasklogs")).andDo(MockMvcResultHandlers.print()).andExpect(
        		MockMvcResultMatchers.status().isOk());
//        .andExpect(status().isOk());
//        mockMvc.perform(get("/springmvc/jbpm/process/instances/")).andExpect(status().isOk());
/*      this.mockMvc.perform(get("/rest/equipment/{Number}", 3))
           .andExpect(status().isOk())*/
    }
}
