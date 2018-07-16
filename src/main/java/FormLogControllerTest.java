public class FormLogControllerTest {
/*
	@RunWith(SpringJUnit4ClassRunner.class)
	@WebAppConfiguration
	@ContextConfiguration(classes = MyWebConfig.class)
	public class ControllerTest {

	    @Autowired
	    private WebApplicationContext wac;

	    private MockMvc mockMvc;

	    @Before
	    public void setup () {
	        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
	        this.mockMvc = builder.build();
	    }

	    @Test
	    public void testUserController () throws Exception {

	        MockHttpServletRequestBuilder builder =
	                            MockMvcRequestBuilders.post("/users/register")
	                                            .contentType(MediaType.APPLICATION_JSON)
	                                            .content(createUserInJson("joe",
	                                                                      "joe@example.com",
	                                                                      "abc"));

	        this.mockMvc.perform(builder)
	                    .andExpect(MockMvcResultMatchers.status()
	                                                    .isCreated());

	       // create one more user
	        builder = MockMvcRequestBuilders.post("/users/register")
	                                        .contentType(MediaType.APPLICATION_JSON)
	                                        .content(createUserInJson("mike",
	                                                                  "mike@example.com",
	                                                                  "123"));

	        this.mockMvc.perform(builder)
	                    .andExpect(MockMvcResultMatchers.status()
	                                                    .isCreated());

	        // get all users
	        builder = MockMvcRequestBuilders.get("/users")
	                                        .accept(MediaType.APPLICATION_JSON);
	        this.mockMvc.perform(builder)
	                    .andExpect(MockMvcResultMatchers.status()
	                                                    .isOk())
	                    .andDo(MockMvcResultHandlers.print());

	    }

	    private static String createUserInJson (String name, String email, String password) {
	        return "{ \"name\": \"" + name + "\", " +
	                            "\"emailAddress\":\"" + email + "\"," +
	                            "\"password\":\"" + password + "\"}";
	    }
	}*/

}
