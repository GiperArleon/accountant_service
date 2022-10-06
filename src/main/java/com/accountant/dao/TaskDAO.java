package com.accountant.dao;

import com.accountant.model.Task;
import com.accountant.util.ConnectionMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDAO {
    private static TaskDAO instance = new TaskDAO();

    public static TaskDAO getInstance() {
        if(instance == null) {
            instance = new TaskDAO();
        }
        return instance;
    }

    private final static Logger log = LoggerFactory.getLogger(TaskDAO.class);

    private static final String SQL_FIND_ALL_TASK                 = "SELECT * FROM accountant.tasks";
    private static final String SQL_FIND_ALL_TASKS_BY_DATE        = "SELECT * FROM accountant.tasks WHERE record_date >= ?";
    private static final String SQL_FIND_TASK_BY_USER_ID          = "SELECT * FROM accountant.tasks WHERE id=?";
    private static final String SQL_FIND_TASK_BY_USER_ID_AND_DATE = "SELECT * FROM accountant.tasks WHERE user_id = ? AND record_date >= ?";
    private static final String SQL_CREATE_TASK                   = "INSERT INTO accountant.tasks(user_id, record_date, description, hours, minutes) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_EDIT_TASK                     = "UPDATE accountant.tasks SET user_id=?, record_date=?, description=?, hours=?, minutes=? WHERE id=?";
    private static final String SQL_DELETE_TASK                   = "DELETE FROM accountant.tasks WHERE id=?";

    public List<Task> getAll() {
        List<Task> tasks = new ArrayList<>();
        try(Connection conn = ConnectionMaker.getInstance().getConnection();
            Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_TASK);
            getListTasks(tasks, resultSet);
        } catch(SQLException se) {
            log.error("Can't connect to database", se);
        } catch(ClassNotFoundException e) {
            log.error("Can't find database driver", e);
        }
        return tasks;
    }

    public List<Task> getAllByDate(LocalDate date) {
        List<Task> tasks = new ArrayList<>();
        try(Connection conn = ConnectionMaker.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL_TASKS_BY_DATE)) {
            ps.setDate(1, Date.valueOf(date));
            ResultSet resultSet = ps.executeQuery();
            getListTasks(tasks, resultSet);
        } catch(SQLException se) {
            log.error("Can't connect to database", se);
        } catch(ClassNotFoundException e) {
            log.error("Can't find database driver", e);
        }
        return tasks;
    }

    public Optional<Task> getById(Integer id) {
        try(Connection conn = ConnectionMaker.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_FIND_TASK_BY_USER_ID)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()) {
                Integer user_id = resultSet.getInt("user_id");
                LocalDate date = Date.valueOf(resultSet.getString("record_date")).toLocalDate();
                String description = resultSet.getString("description");
                Integer hour = resultSet.getInt("hours");
                Integer minutes = resultSet.getInt("minutes");
                return Optional.of(new Task(id, user_id, date, description, hour, minutes));
            }
        } catch(SQLException se) {
            log.error("Can't connect to database", se);
        } catch(ClassNotFoundException e) {
            log.error("Can't find database driver", e);
        }
        return Optional.empty();
    }

    public List<Task> findTaskByUserIdAndDate(Integer userId, LocalDate date) {
        List<Task> tasks = new ArrayList<>();
        try(Connection conn = ConnectionMaker.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_FIND_TASK_BY_USER_ID_AND_DATE)) {
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(date));
            ResultSet resultSet = ps.executeQuery();
            getListTasks(tasks, resultSet);
        } catch(SQLException se) {
            log.error("Can't connect to database", se);
        } catch(ClassNotFoundException e) {
            log.error("Can't find database driver", e);
        }
        return tasks;
    }

    public void create(Task task) {
        try(Connection conn = ConnectionMaker.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_CREATE_TASK)) {
            ps.setInt(1, task.getUserId());
            ps.setDate(2, Date.valueOf(task.getDate()));
            ps.setString(3, task.getDescription());
            ps.setInt(4, task.getHours());
            ps.setInt(5, task.getMinutes());
            ps.executeUpdate();
        } catch(SQLException se) {
            log.error("Can't connect to database", se);
        } catch(ClassNotFoundException e) {
            log.error("Can't find database driver", e);
        }
    }

    public void edit(Task task) {
        try(Connection conn = ConnectionMaker.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_EDIT_TASK)) {
            ps.setInt(1, task.getUserId());
            ps.setDate(2, Date.valueOf(task.getDate()));
            ps.setString(3, task.getDescription());
            ps.setInt(4, task.getHours());
            ps.setInt(5, task.getMinutes());
            ps.setInt(6, task.getId());
            ps.executeUpdate();
        } catch(SQLException se) {
            log.error("Can't connect to database", se);
        } catch(ClassNotFoundException e) {
            log.error("Can't find database driver", e);
        }
    }

    public void delete(Integer id) {
        try(Connection conn = ConnectionMaker.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_DELETE_TASK)) {
            ps.setInt(1, id);
            ps.execute();
        } catch(SQLException se) {
            log.error("Can't connect to database", se);
        } catch(ClassNotFoundException e) {
            log.error("Can't find database driver", e);
        }
    }

    private void getListTasks(List<Task> tasks, ResultSet resultSet) throws SQLException {
        while(resultSet.next()) {
            Task task = new Task();
            task.setId(resultSet.getInt("id"));
            task.setUserId(resultSet.getInt("user_id"));
            task.setDate(resultSet.getDate("record_date").toLocalDate());
            task.setDescription(resultSet.getString("description"));
            task.setHours(resultSet.getInt("hours"));
            task.setMinutes(resultSet.getInt("minutes"));
            tasks.add(task);
        }
    }
}