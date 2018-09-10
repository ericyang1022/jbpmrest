package jbpm.rest.controller;

import java.nio.charset.Charset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/rest")
public class BusCentralController {
	private static final String APP_URL = "http://192.168.56.101:8080/business-central/rest";
	private static final String USER = "eric";
	private static final String PASSWORD = "p@ssword123";
	
    @RequestMapping(
    		value="/{deploymentUnitID}/process/{processID}/startform", 
    		method = RequestMethod.GET
    		, produces = MediaType.APPLICATION_JSON_VALUE
    		)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ProcessInstanceFormResponse getProcessFormUrl (
			@RequestParam( value="userName", required = true) String userName, 
			@PathVariable String deploymentUnitID, 
			@PathVariable String processID
			) {
//		String deploymentUnitID = "npou:npproj42:1.0";
//		String processID = "npproj42.SaveTimesheet";

		String url = APP_URL + "/runtime/npou:npproj42:1.0/process/" + processID + "/startform";
		System.out.println("url: " + url + ", userName:" + userName);
		
		System.out.println("== Getting process form URL:" + deploymentUnitID + " ==");
		ProcessInstanceFormResponse response = new ProcessInstanceFormResponse();
		try {
			response = createRequest(url).get(
					ProcessInstanceFormResponse.class).getEntity(
					ProcessInstanceFormResponse.class);
			System.out.println("status=" + response.getStatus());
			System.out.println("formURL=" + response.getFormUrl());			
		} catch (Exception e) {
			System.out.println("Error occurred: " + e);
			e.printStackTrace();
		}
		return response;
	}
    
/*    @RequestMapping(value = "/byName", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    String getUserByName(@RequestParam( value="firstName",required = false) String firstName,
                             @RequestParam(value="lastName",required = false) String lastName, @ModelAttribute("userClientObject") UserClient userClient) {

        return client.getUserByName(userClient, firstName, lastName);
    }*/
    
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
	 * process instance form response
	 */
	@XmlRootElement(name = "process-instance-form-response")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class ProcessInstanceFormResponse {
		private String status;
		private String formUrl;
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getFormUrl() {
			return formUrl;
		}
		public void setFormUrl(String formUrl) {
			this.formUrl = formUrl;
		}
	}

	/*
	 * process instance form response
	 */
	@XmlRootElement(name = "task-form-response")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TaskFormResponse {
		private String status;
		private String formUrl;
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getFormUrl() {
			return formUrl;
		}
		public void setFormUrl(String formUrl) {
			this.formUrl = formUrl;
		}
	}
}
