package demo.springmvc.blog.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import demo.springmvc.blog.domain.TaskLog;

@Service
public class TaskLogService {
	private JdbcTemplate jdbcTemplate;
	private final static TaskLogMapper TAKK_LOG_ROW_MAPPER = new TaskLogMapper();

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<TaskLog> getAllTaskLogs() throws DataAccessException{
		System.out.println("getAllTaskLog() here ");
		return this.jdbcTemplate
			.query("select actiontype, prjCode, processID , processInstanceID ,taskID , creationDate from tasklog order by creationDate desc",
					TaskLogService.TAKK_LOG_ROW_MAPPER);
	}
	
	public void createTaskLog(TaskLog taskLog) throws DataAccessException{
		this.jdbcTemplate
				.update("INSERT INTO tasklog (actiontype, prjCode, processID , processInstanceID ,taskID , creationDate ) VALUES (?,?,?,?,?,?)",
				taskLog.getActionType(), taskLog.getPrjCode(), taskLog.getProcessID(), taskLog.getProcessInstanceID()
				, taskLog.getTaskID(), taskLog.getCreationDate());
		System.out.println("Inserted new task log record!");
	}
	
	private static class TaskLogMapper implements RowMapper<TaskLog> {
		@Override
		public TaskLog mapRow(ResultSet resultSet, int line) throws SQLException {
			TaskLog log = new TaskLog();
			log.setActionType(resultSet.getString("actiontype"));
			log.setPrjCode(resultSet.getString("prjCode"));
			log.setProcessID(resultSet.getString("processID"));
			log.setProcessInstanceID(resultSet.getString("processInstanceID"));
			log.setTaskID(resultSet.getString("taskID"));
			log.setCreationDate(resultSet.getDate("creationDate"));
			return log;
		}
	}
}
