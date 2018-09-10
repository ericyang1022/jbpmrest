package jbpm.rest.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.kie.server.api.model.definition.QueryDefinition;
import org.kie.server.api.model.definition.QueryFilterSpec;
import org.kie.server.api.model.instance.ProcessInstance;
import org.kie.server.api.model.instance.TaskInstance;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.api.model.instance.TaskSummaryList;
import org.kie.server.api.util.QueryFilterSpecBuilder;
import org.kie.server.client.QueryServicesClient;

import npou.npproj42.timesheet;

public class KieServerQueryTest2 {

	public static void main(String[] args) {
//		abortProcess();
//		registerCustomQuery1();
		registerCustomQuery();
//		startProcess();
//		registerQuery();
//		registerQuery1();
//		displayQueryDef();
	}
	
	public static void registerQuery() {
		String QUERY_NAME = "getAllTimesheets";
		// building the query
		QueryDefinition queryDefinition = QueryDefinition.builder().name(QUERY_NAME)
//				.expression("select id, employee_no, employee_name, charge_item_name from timesheet")
				.expression("select distinct * from timesheet")
				.source("java:jboss/datasources/jbpmDS")
				.target("CUSTOM").build();
		
		// two queries cannot have the same name
		KieServerUtils.getQueryServicesClient().unregisterQuery(QUERY_NAME);
		         
		KieServerUtils.getQueryServicesClient().registerQuery(queryDefinition);
		System.out.println("Registered new Query");
		
		QueryFilterSpec filterSpec = new QueryFilterSpecBuilder().equalsTo("employee_no", 
			"no1").addColumnMapping("employee_no", "string").get();
		
		List<List> instances = KieServerUtils.getQueryServicesClient().query(QUERY_NAME, 
//				QueryServicesClient.QUERY_MAP_RAW, 
				"RawListExtended", // custom mapper
//				filterSpec,
				0, 10, List.class);
		System.out.println(instances);
		
		for (List l: instances) {
			for (Object o: l) {
				System.out.print(" " + o);	
			}
			System.out.println("");
		}

	}
	
	
	public static void abortProcess() {
		String containerID = "npou:npproj42:1.0";
		// at the end abort process instance
		Long processInstanceId = 9L;
		KieServerUtils.getProcessServiceClient().abortProcessInstance(containerID, processInstanceId);
		System.out.println("Process aborted successfully, processInstanceId=" + 9);
		 
		ProcessInstance processInstance = KieServerUtils.getQueryServicesClient().findProcessInstanceById(
			processInstanceId);
		System.out.println("\t######### ProcessInstance: " + processInstance);
	}
	
	public static void registerCustomQuery() {
		String QUERY_NAME = "getMyTimesheetTaskInstanceWithVars";
		// building the query
		QueryDefinition queryDefinition = QueryDefinition.builder().name(QUERY_NAME)
			.expression("select ti.*, ts.id as t_id, ts.employee_no, ts.employee_name, ts.charge_item_name \n" + 
				"from AuditTaskImpl ti inner join TaskVariableImpl tv \n" + 
				"on (ti.taskId = tv.taskId) inner join ( \n" + 
				"select taskid, processId, max(id) as max_id from TaskVariableImpl \n" + 
				"group by taskid, processId) v \n" + 
				"on (v.max_id = tv.id and v.taskId = tv.taskId and v.processId=tv.processId) \n" +
				"inner join timesheet ts on (ts.id = (SUBSTRING_INDEX(SUBSTRING_INDEX(tv.value, ',', 1), '=', -1))) \n")
			.source("java:jboss/datasources/jbpmDS")
			.target("TASK").build();
		
		QueryFilterSpec filterSpec = new QueryFilterSpecBuilder()
				.equalsTo("id", "23")
//				.equalsTo("status", "Ready")
//				.equalsTo("employee_no", "c7888")
				.addColumnMapping("t_id", "integer")
				.addColumnMapping("employee_no", "string")
				.addColumnMapping("employee_name", "string")
				.addColumnMapping("charge_item_name", "charge_item_name")
			.get();
		
		// two queries cannot have the same name
		KieServerUtils.getQueryServicesClient().unregisterQuery(QUERY_NAME);
		// register the query
		KieServerUtils.getQueryServicesClient().registerQuery(queryDefinition);
		// execute the query with parameters: query name, mapping type (to map the
        // fields to an object), page number, page size and return type
		List<TaskInstance> query = KieServerUtils.getQueryServicesClient().query(QUERY_NAME,
			QueryServicesClient.QUERY_MAP_TASK_WITH_CUSTOM_VARS, filterSpec, 0, 100, TaskInstance.class);
		
		for (TaskInstance taskInstance : query) {
			System.out.println(taskInstance);
			System.out.println("timesheet object: " + taskInstance.getInputData());
//			System.out.println(taskInstance.getOutputData());
		}
	}

	
	public static void startProcess() {
		String containerID = "npou:npproj42:1.0";
		String processID = "npproj42.SaveTimesheet";
		// start process instance with process variable 
		Map<String, Object> params = new HashMap<String, Object>();
		timesheet t = new timesheet();
		t.setEmployee_no("c7888");
		t.setEmployee_name("carter");
		t.setCharge_item_name("n18cr");
 		params.put("p_timesheet", t);
		Long processInstanceId = KieServerUtils.getProcessServiceClient().startProcess(containerID, processID, params);
		System.out.println("\t######### new process instance id: " + processInstanceId);
		
		System.out.println("Display process instance");
		ProcessInstance pi =  KieServerUtils.getQueryServicesClient().findProcessInstanceById(processInstanceId);
		System.out.println(pi);
		
		// show active user tasks
		TaskSummaryList tsList = pi.getActiveUserTasks();
		for (TaskSummary ts: tsList.getTasks()) {
			System.out.println("\t######### active task: " + ts);
		}
	}
	

	public static void displayQueryDef() {
		List<QueryDefinition> queryDefs = KieServerUtils.getQueryServicesClient().getQueries(0, 10);
//		System.out.println(queryDefs);
		for (QueryDefinition queryDef: queryDefs) {
			System.out.println("name: " + queryDef.getName());
			System.out.println("source: " + queryDef.getSource());
			System.out.println("target: " + queryDef.getTarget());
			System.out.println("\t######### expression: " + queryDef.getExpression());
//			System.out.println(ReflectionToStringBuilder.reflectionToString(queryDef));
		}
	}
	

	
	public static void registerCustomQuery1() {
		String QUERY_NAME = "getMyUserTaskInstanceWithVars";
		// building the query
		QueryDefinition queryDefinition = QueryDefinition.builder().name(QUERY_NAME)
			.expression("select ti.*, t.id as t_id, t.employee_no, t.employee_name, t.charge_item_name\r\n" + 
					"from AuditTaskImpl ti inner join timesheet t \r\n" + 
					"on (ti.taskId = t.id)")
			.source("java:jboss/datasources/jbpmDS")
			.target("TASK").build();

		QueryFilterSpec filterSpec = new QueryFilterSpecBuilder()
//				.equalsTo("processInstanceId", "3")
//				.equalsTo("status", "Ready")
				.equalsTo("status", "InProgress")
//				.equalsTo("employee_no", "c7383")
			.get();
		
		
		// two queries cannot have the same name
		KieServerUtils.getQueryServicesClient().unregisterQuery(QUERY_NAME);
		// register the query
		KieServerUtils.getQueryServicesClient().registerQuery(queryDefinition);
		// execute the query with parameters: query name, mapping type (to map the
        // fields to an object), page number, page size and return type
		List<TaskInstance> query = KieServerUtils.getQueryServicesClient().query(QUERY_NAME,
			QueryServicesClient.QUERY_MAP_TASK_WITH_VARS, 
			filterSpec,
			0, 100, TaskInstance.class);
		
		// read the result
		for (TaskInstance taskInstance : query) {
//			System.out.println(taskInstance);
//			System.out.println(taskInstance.getInputData());
//			System.out.println(taskInstance.getOutputData());
			System.out.println(ReflectionToStringBuilder.reflectionToString(taskInstance));
		}
	}
	
	
	public static void registerQuery1() {
		String QUERY_NAME = "getAllMyTasks";
		// building the query
		QueryDefinition queryDefinition = QueryDefinition.builder().name(QUERY_NAME)
//				.expression("select * from Task t").source("java:jboss/datasources/jbpmDS")
				.expression("select ti.*, t.id as t_id, t.employee_no, t.employee_name, t.charge_item_name\r\n" + 
						"from AuditTaskImpl ti inner join timesheet t \r\n" + 
						"on (ti.taskId = t.id)")
				.source("java:jboss/datasources/jbpmDS")
				.target("TASK").build();
		// two queries cannot have the same name
		KieServerUtils.getQueryServicesClient().unregisterQuery(QUERY_NAME);
		// register the query
		KieServerUtils.getQueryServicesClient().registerQuery(queryDefinition);
		// execute the query with parameters: query name, mapping type (to map the
        // fields to an object), page number, page size and return type
		List<TaskInstance> query = KieServerUtils.getQueryServicesClient().query(QUERY_NAME,
		QueryServicesClient.QUERY_MAP_TASK, 0, 100, TaskInstance.class);
		// read the result
		for (TaskInstance taskInstance : query) {
			System.out.println(taskInstance);
		}
	}
	

}
