package jbpm.rest.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.KieServerInfo;
import org.kie.server.api.model.definition.ProcessDefinition;
import org.kie.server.api.model.instance.NodeInstance;
import org.kie.server.api.model.instance.ProcessInstance;
import org.kie.server.api.model.instance.TaskAttachment;
import org.kie.server.api.model.instance.TaskComment;
import org.kie.server.api.model.instance.TaskInstance;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.api.model.instance.TaskSummaryList;
import org.kie.server.client.QueryServicesClient;

public class KieServerUtilsTest {

	public static void main(String[] args) {
		listCapabilities();
//		displayProcessInfo();
//		displayProcessNodeInfo();
//		displayTaskInfo();
//		listAndCompleteTask();
//		listProcessInstances();
//		updateProcessVariables();
//		startProcess();
//		listProcessesWithinContainer();
//		listContainers();
//		listProcesses();
		
	}
	
	public static void displayProcessInfo() {
		String containerID = "npou:npproj42:1.0";
		
		System.out.println("== Listing all Process Instances of " + containerID + " ==");
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(1); // active
//		statusList.add(2); // completed
//		statusList.add(3); // aborted 
		List<ProcessInstance> processInstances = KieServerUtils.getQueryServicesClient()
			.findProcessInstancesByContainerId(containerID, statusList, 0, 20);
		for (ProcessInstance instance : processInstances) {
			System.out.println(ReflectionToStringBuilder.reflectionToString(instance));
			
			ProcessInstance instance1 = KieServerUtils.getProcessServiceClient().getProcessInstance(
				containerID, instance.getId(), true);
			System.out.println(ReflectionToStringBuilder.reflectionToString(instance1));
			
			// show process variables
			Map map = KieServerUtils.getProcessServiceClient().getProcessInstanceVariables(containerID, 
				instance.getId());
			System.out.println("process instance variables: " + map);
		}
	}	
	
	public static void displayProcessNodeInfo() {
		String containerID = "com.banking.app:BankingApp:1.0";
		String processID = "LoanProcess";
		
		System.out.println("== Listing active Process Instances of " + containerID + " ==");
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(1); // active
		List<ProcessInstance> processInstances = KieServerUtils.getQueryServicesClient()
			.findProcessInstancesByContainerId(containerID, statusList, 0, 20);
		for (ProcessInstance instance : processInstances) {
			// find completed nodes
			List<NodeInstance> completedNodes = KieServerUtils.getQueryServicesClient().findCompletedNodeInstances(
					instance.getId(), 0, 10);
			for (NodeInstance ni: completedNodes) {
				System.out.println("\t######### Completed node: " + ni);				
			}

			// find active nodes 
			List<NodeInstance> activeNodes = KieServerUtils.getQueryServicesClient().findActiveNodeInstances(
					instance.getId(), 0, 10);
			for (NodeInstance ni: activeNodes) {
				System.out.println("\t######### Active node: " + ni);				
			}
		}
	}
		


	
	public static void displayTaskInfo() {
		// find available tasks
		List<TaskSummary> tasks = KieServerUtils.getUserTaskServiceClient().findTasksAssignedAsPotentialOwner(
			"eric", 0, 10);
		
		TaskSummary ts = tasks.get(0);
		System.out.println("\t######### Task summary: " + ts);
		
		TaskInstance ti = KieServerUtils.getUserTaskServiceClient().getTaskInstance(ts.getContainerId(), ts.getId());
		System.out.println("\t######### task instance: " + ReflectionToStringBuilder.reflectionToString(ti));
		
		// show task input variables		
		Map map = KieServerUtils.getUserTaskServiceClient().getTaskInputContentByTaskId(ts.getContainerId(), ts.getId());
		System.out.println("task input variables: " + map);
		
		// show task output variables		
		map = KieServerUtils.getUserTaskServiceClient().getTaskOutputContentByTaskId(ts.getContainerId(), ts.getId());
		System.out.println("task output variables: " + map);
		
		List<TaskComment> tcList = KieServerUtils.getUserTaskServiceClient().getTaskCommentsByTaskId(
				ts.getContainerId(), ts.getId());
		for (TaskComment tc: tcList) {
			System.out.println("\t######### TaskComment: " + tc);
		}
		
		List<TaskAttachment> taList = KieServerUtils.getUserTaskServiceClient().getTaskAttachmentsByTaskId(
			ts.getContainerId(), ts.getId());
		for (TaskAttachment ta: taList) {
			System.out.println("\t######### TaskAttachment: " + ta);
		}
	}
	
	
	public static void listAndCompleteTask() {
		// find available tasks
		List<TaskSummary> tasks = KieServerUtils.getUserTaskServiceClient().findTasksAssignedAsPotentialOwner(
			"eric", 0, 10);
		
		TaskSummary ts = tasks.get(0);
		System.out.println("\t######### Tasks: " + ts);
		
		// claim task
		KieServerUtils.getUserTaskServiceClient().claimTask(ts.getContainerId(), ts.getId(), "eric");
		System.out.println("task claimed");
		
		// save task wtih variables
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tv_loan_out_requesterName", "john-01");
		params.put("tv_loan_out_requesterLastName", "carter-01");
		params.put("tv_loan_out_bankAccount", "bankac-01");
		KieServerUtils.getUserTaskServiceClient().saveTaskContent(ts.getContainerId(), ts.getId(), params);
		System.out.println("saved task wtih variables");
		
		// start task
		KieServerUtils.getUserTaskServiceClient().startTask(ts.getContainerId(), ts.getId(), "eric");
		System.out.println("task started");

		// show task variables		
		Map map = KieServerUtils.getUserTaskServiceClient().getTaskOutputContentByTaskId(ts.getContainerId(), ts.getId());
		System.out.println("task output variables: " + map);
		
		// complete task with variables
		params.put("tv_loan_out_requesterName", "john-01-A");
		KieServerUtils.getUserTaskServiceClient().completeTask(ts.getContainerId(), ts.getId(), "eric", params);
		System.out.println("task completed");
	}
	
	public static void updateProcessVariables() {
		String containerID = "com.banking.app:BankingApp:1.0";
		String processID = "LoanProcess";
		
		System.out.println("Display process instance");
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(1); // active
		List<ProcessInstance> piList =  KieServerUtils.getQueryServicesClient().findProcessInstancesByProcessId(
			processID, statusList, 0, 10);
		for (ProcessInstance pi: piList) {
			System.out.println(pi);
			
			// show process variables
			Map<String, Object> map = KieServerUtils.getProcessServiceClient().getProcessInstanceVariables(
				containerID, pi.getId());
			System.out.println("process instance variables: " + map);
			
			// update process variable
			KieServerUtils.getProcessServiceClient().setProcessVariable(containerID, pi.getId(), 
				"pv_bankAccount", "bankac0001-AAA");
			System.out.println("updated process variable pv_bankAccount");
			
			// show process variables again
			map = KieServerUtils.getProcessServiceClient().getProcessInstanceVariables(
				containerID, pi.getId());
			System.out.println("show process instance variables again: " + map);
		}
	}
	
	public static void startProcess() {
		String containerID = "com.banking.app:BankingApp:1.0";
		String processID = "LoanProcess11";
		// start process instance with process variable 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_requesterName", "john");
		params.put("pv_requesterLastName", "carter");
		params.put("pv_bankAccount", "bankac0001");
		Long processInstanceId = KieServerUtils.getProcessServiceClient().startProcess(containerID, processID, params);
		System.out.println("\t######### Process instance id: " + processInstanceId);
		
		System.out.println("Display process instance");
		ProcessInstance pi =  KieServerUtils.getQueryServicesClient().findProcessInstanceById(processInstanceId);
		System.out.println(ReflectionToStringBuilder.reflectionToString(pi));
		
		// show active user tasks
		TaskSummaryList tsList = pi.getActiveUserTasks();
		for (TaskSummary ts: tsList.getTasks()) {
			System.out.println("\t######### active task: " + ts);
		}
	}
	
/*	public static void listAvailabeTasks() {
		// find available tasks
		List<TaskSummary> tasks = KieServerUtils.getUserTaskServiceClient().findTasksAssignedAsPotentialOwner(
			"eric", 0, 10);
		for (TaskSummary ts: tasks) {
			System.out.println("\t######### Tasks: " + ts);
		}
		
		// complete task
		TaskSummary ts = tasks.get(0);
		System.out.println("doing task: " + ts);
		Long taskId = ts.getId();
		
		KieServerUtils.getUserTaskServiceClient().startTask(ts.getContainerId(), taskId, "eric");
		Map map = KieServerUtils.getUserTaskServiceClient().getTaskInputContentByTaskId(ts.getContainerId(), taskId);
		System.out.println("task started, map: " + map);
		
		VariablesDefinition vd = KieServerUtils.getProcessServiceClient().getProcessVariableDefinitions(ts.getContainerId(), 
				ts.getProcessId());
		System.out.println("process instance Variables Definition, vd: " + vd);
		
		map = KieServerUtils.getProcessServiceClient().getProcessInstanceVariables(ts.getContainerId(), 
			ts.getProcessInstanceId());
		System.out.println("process instance variables, map: " + map);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tv_loan_out_requesterName", "john-01");
		params.put("tv_loan_out_requesterLastName", "carter-01");
		params.put("tv_loan_out_bankAccount", "bankac-01");
		
		params.put("tv_disp_in_requesterName", "john-01-disp");
		params.put("tv_disp_in_requesterLastName", "carter-01-disp");
		params.put("tv_disp_in_bankAccount", "bankac-01-disp");
		
		
		KieServerUtils.getUserTaskServiceClient().saveTaskContent(ts.getContainerId(), taskId, params);
		System.out.println("saved task");
		
		map = KieServerUtils.getProcessServiceClient().getProcessInstanceVariables(ts.getContainerId(), 
				ts.getProcessInstanceId());
		System.out.println("process instance variables 222, map: " + map);
		
		map = KieServerUtils.getUserTaskServiceClient().getTaskInputContentByTaskId(ts.getContainerId(), taskId);
		System.out.println("getTaskInputContentByTaskId(), map=" + map);
		
		map = KieServerUtils.getUserTaskServiceClient().getTaskOutputContentByTaskId(ts.getContainerId(), taskId);
		System.out.println("getTaskOutputContentByTaskId(), map=" + map);
		
//		KieServerUtils.getUserTaskServiceClient().releaseTask(ts.getContainerId(), taskId, "eric");
		
		KieServerUtils.getUserTaskServiceClient().completeTask(ts.getContainerId(), taskId, "eric", null);
		
		System.out.println("processInstanceID: " + ts.getProcessInstanceId());
		List<NodeInstance> completedNodes = KieServerUtils.getQueryServicesClient().findCompletedNodeInstances(
			ts.getProcessInstanceId(), 0, 10);
		System.out.println("\t######### Completed nodes: " + completedNodes);
		
		List<NodeInstance> activeNodes = KieServerUtils.getQueryServicesClient().findActiveNodeInstances(
				ts.getProcessInstanceId(), 0, 10);
			System.out.println("\t######### Active nodes: " + activeNodes);
	}*/
	
	public static void listProcessInstances() {
		String containerID = "com.banking.app:BankingApp:1.0";
		String processID = "LoanProcess";
		
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
		
/*		System.out.println("== findProcessInstanceById ==");
		ProcessInstance pi = queryClient.findProcessInstanceById(Long.valueOf(1));
		System.out.println(ReflectionToStringBuilder.reflectionToString(pi));
		
		TaskSummaryList tsList = pi.getActiveUserTasks();
		
		for (TaskSummary ts: tsList.getTasks()) {
			System.out.println(ReflectionToStringBuilder.reflectionToString(ts));
		}*/

	}
	

	
	public static void listProcessesWithinContainer() {
		// query for all available process definitions
		QueryServicesClient queryClient = KieServerUtils.getKieServicesClient().getServicesClient(
			QueryServicesClient.class);
		// container ID can be retrieved by listContainer()
		String containerID = "com.banking.app:BankingApp:1.0";
		// find process within container with pagination
		List<ProcessDefinition> processes = queryClient.findProcessesByContainerId(containerID, 0, 10);
		
		for (ProcessDefinition processDef: processes) {
			System.out.println("Available processes" + processDef);
			
			// get details of process definition
/*			ProcessServicesClient processClient = KieServerUtils.getKieServicesClient().getServicesClient(
				ProcessServicesClient.class);
			ProcessDefinition definition = processClient.getProcessDefinition(containerID, processDef.getId());
			System.out.println("\t Definition details: " + definition);*/
		}
		
	}
	
	public static void listContainers() {
		KieContainerResourceList containersList = KieServerUtils.getKieServicesClient().listContainers().getResult();
		List<KieContainerResource> kieContainers = containersList.getContainers();
		System.out.println("Available containers: ");
		
		boolean isDeployed = false;
		for (KieContainerResource container : kieContainers) {
			System.out.println("\t" + container.getContainerId() + " (" + container.getReleaseId() + ")");
			// check if container "npou:npproj42:1.0" created
			if (!isDeployed) {
				if (StringUtils.equals(container.getContainerId(), "npou:npproj42:1.0")) {
					System.out.println("\t" + "Container npou:npproj42:1.0 deployed");
					isDeployed = true;
				}
			}
		}
		
		// deploy container "npou:npproj42:1.0" if not there yet 
/*		if (!isDeployed) {
			String containerID = "npou:npproj42:1.0";
		    KieContainerResource resource = new KieContainerResource(containerID, 
		    		new ReleaseId("npou", "npproj42", "1.0"));
		    System.out.println("\t######### Deployed container " + containerID);
		    KieServerUtils.getKieServicesClient().createContainer(containerID, resource);
		}*/
	}

	public static void listCapabilities() {
		KieServerInfo serverInfo = KieServerUtils.getKieServicesClient().getServerInfo().getResult();
		System.out.print("Server capabilities:");
		for (String capability : serverInfo.getCapabilities()) {
			System.out.print(" " + capability);
		}
		System.out.println(KieServerUtils.getKieServicesClient().getServerInfo().getResult().getName());
		System.out.println(KieServerUtils.getKieServicesClient().getServerInfo().getResult().getServerId());
//		System.out.println(KieServerUtils.getKieServicesClient().getServerInfo().getMsg());
		System.out.println("-----end -------");
	}

/*	public static void listProcessInstances() {
		System.out.println("== Listing Business Process Instances ==");
		QueryServicesClient queryClient = KieServerUtils.getKieServicesClient().getServicesClient(QueryServicesClient.class);
		List<Integer> statusList = new ArrayList<Integer>();
//		statusList.add(0);
		statusList.add(1);
//		statusList.add(2);
		List<ProcessInstance> processInstances = queryClient.findProcessInstancesByContainerId("MyContainer1", statusList, 1, 20);
		for (ProcessInstance instance : processInstances) {
			System.out.println(ReflectionToStringBuilder.reflectionToString(instance));
		}
		
		System.out.println("== findProcessInstanceById ==");
		ProcessInstance pi = queryClient.findProcessInstanceById(Long.valueOf(1));
		System.out.println(ReflectionToStringBuilder.reflectionToString(pi));
		
		TaskSummaryList tsList = pi.getActiveUserTasks();
		
		for (TaskSummary ts: tsList.getTasks()) {
			System.out.println(ReflectionToStringBuilder.reflectionToString(ts));
		}

	}*/
}
