package demo.springmvc.blog.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import demo.springmvc.blog.domain.TaskLog;

@Service
public class TaskLogService {
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void createTaskLog(TaskLog taskLog) {
		this.jdbcTemplate
				.update("INSERT INTO tasklog (actiontype, prjCode, processID , processInstanceID ,taskID , creationDate ) VALUES (?,?,?,?,?,?)",
				taskLog.getActionType(), taskLog.getPrjCode(), taskLog.getProcessID(), taskLog.getProcessInstanceID()
				, taskLog.getTaskID(), taskLog.getCreationDate());
		System.out.println("Inserted new task log record!");
	}
}
