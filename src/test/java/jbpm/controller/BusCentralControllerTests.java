package jbpm.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jbpm.rest.controller.BusCentralController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/spring/root-context.xml",
	"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
	})
public class BusCentralControllerTests {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    private BusCentralController controller;

    @Before
    public void setUp() {
//       this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    	mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getProcessFormURL() throws Exception {
//        mockMvc.perform(get("/jbpm/process/instances")).andExpect(status().isOk())
    	MvcResult result = mockMvc.perform(MockMvcRequestBuilders
    	         .get("/rest/{deploymentUnitID}/process/{processID}/startform",
    	        		 "npou:npproj42:1.0", "npproj42.SaveTimesheet")
    	         .param("userName", "eric")
    	         .accept(MediaType.APPLICATION_JSON))
    		.andDo(MockMvcResultHandlers.print())
    		.andReturn();
    	System.out.println(result.getResponse().getContentAsString());
/*      this.mockMvc.perform(get("/rest/equipment/{Number}", 3))
           .andExpect(status().isOk())*/
    }
    
/*    public void testGetUserByName() throws Exception {
        String firstName = "Jack";
        String lastName = "s";       
        this.userClientObject = client.createClient();
        mockMvc.perform(get("/byName")
                .sessionAttr("userClientObject", this.userClientObject)
                .param("firstName", firstName)
                .param("lastName", lastName)               
        ).andDo(print())
         .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].fn").value("Marge"));
    }*/
}
