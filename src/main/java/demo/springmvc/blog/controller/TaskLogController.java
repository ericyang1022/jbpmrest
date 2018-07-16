package demo.springmvc.blog.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import demo.springmvc.blog.domain.TaskLog;
import demo.springmvc.blog.service.TaskLogService;

@Controller
@RequestMapping("/tasklog")
public class TaskLogController {
	private static final Logger logger = LoggerFactory
			.getLogger(TaskLogController.class);

	@Autowired
	private TaskLogService taskLogService;
	
	@RequestMapping(value = "/new", method = RequestMethod.POST) //, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String createFormLog(@RequestParam(value="actionType") String actionType, 
			@RequestParam(value="prjCode") String prjCode, 
			@RequestParam(value="processID") String processID,
			@RequestParam(value="processInstanceID") String processInstanceID,
			@RequestParam(value="taskID") String taskID) {
//		logger.info("you are in create Form Log: " + formLog);
		logger.info("actionType=" + actionType + ", prjCode=" + prjCode
			+ ", processID=" + processID + ", processInstanceID=" + processInstanceID
			+ ", taskID=" + taskID);
		
		TaskLog tl = new TaskLog();
		tl.setActionType(actionType);
		tl.setPrjCode(prjCode);
		tl.setProcessID(processID);
		tl.setProcessInstanceID(processInstanceID);
		tl.setTaskID(taskID);
		tl.setCreationDate(new Date());
		taskLogService.createTaskLog(tl);
		
		return "{\"result\":\"Completed create task log to another DB!\"}";
	}
	

/*	@RequestMapping(value = "/new", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody String createFormLog(@RequestBody FormLog formLog, Model model) {
		logger.info("you are in create Form Log: " + formLog);
		
//		this.postService.createPost(post);
		
		return "success";
	}*/
	
/*    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FormLog> getAllFormLog () {
//    	return userService.getAllUsers();
    	return null;
	}*/
}
