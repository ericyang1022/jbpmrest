package jbpm.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.kie.server.api.model.instance.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jbpm.rest.util.KieServerUtils;

@Controller
@RequestMapping("/jbpm")
public class KieServerController {
	private static final Logger logger = LoggerFactory
			.getLogger(KieServerController.class);
	
    @RequestMapping(
    		value="/{containerID}/process/instances", 
    		method = RequestMethod.GET
    		, produces = MediaType.APPLICATION_JSON_VALUE
    		)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ProcessInstance> getProcessInstances(@PathVariable String containerID) {
//		String containerID = "npou:npproj42:1.0";
//		String processID = "LoanProcess";
		
		System.out.println("== Listing all Process Instances of " + containerID + " ==");
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(1); // active
		statusList.add(2); // completed
		statusList.add(3); // aborted 
		List<ProcessInstance> processInstances = KieServerUtils.getQueryServicesClient()
			.findProcessInstancesByContainerId(containerID, statusList, 0, 20);
		for (ProcessInstance instance : processInstances) {
			System.out.println(ReflectionToStringBuilder.reflectionToString(instance));
		}
    	return processInstances;
	}
}
