package jbpm.controller;

import java.nio.charset.Charset;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.junit.Test;

public class TaskQueryTests {

	private static final String APP_URL = "http://192.168.56.101:8080/business-central";
	private static final String USER = "eric";
	private static final String PASSWORD = "p@ssword123";
	private static final int PIID = 1;

	
	@Test
	public void doTest() throws Exception {
		// Get task by process instance ID
		String url = APP_URL + "/rest/task/query?processInstanceId=" + PIID;
		TaskSummaryListResponse response = createRequest(url).get(
				TaskSummaryListResponse.class).getEntity(
				TaskSummaryListResponse.class);
		for (TaskSummary t : response.getTasks()) {
			System.out.println(t.getId() + " - "+  t.getName());
			System.out.println("--");
		}
		
	}

	private ClientRequest createRequest(String url) {
		return new ClientRequestFactory().createRequest(url).header(
				"Authorization", getAuthHeader());
	}

	/**
	 * 
	 * Creates the authentication header
	 * 
	 * @return
	 */
	private String getAuthHeader() {
		String auth = USER + ":" + PASSWORD;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset
				.forName("US-ASCII")));
		return "Basic " + new String(encodedAuth);
	}

	/*
	 * The wrapper element for the response list
	 */
	@XmlRootElement(name = "task-summary-list-response")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TaskSummaryListResponse {
		@XmlElement(name ="task-summary")
		List<TaskSummary> tasks;

		public List<TaskSummary> getTasks() {
			return tasks;
		}

		public void setTasks(List<TaskSummary> tasks) {
			this.tasks = tasks;
		}

	}

	/*
	 * mapping only two fields, you can map more
	 */
	@XmlRootElement(name = "task-summary")
	public static class TaskSummary {
		private int id;
		private String name;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}