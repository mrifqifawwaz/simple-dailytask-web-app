package com.personal.taskapp.task;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
public class JDBCTaskRepository implements TaskRepository {
    
    private final JdbcClient jdbcClient;

    public JDBCTaskRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Task> findAll() {
        return jdbcClient.sql("select * from task")
                .query(Task.class)
                .list();
    }

    public Optional<Task> findById(Integer id) {
        return jdbcClient.sql("SELECT id,task_title,started_on,completed_on,time_set FROM task WHERE id = :id" )
                .param("id", id)
                .query(Task.class)
                .optional();
    }

    public void create(Task task) {
        var updated = jdbcClient.sql("INSERT INTO task(id,task_title,started_on,completed_on,time_set) values(?,?,?,?,?)")
                .params(List.of(
                task.id(),
                task.taskTitle(),
                task.startedOn(),
                task.completedOn(),
                task.timeSet().toString()))
                .update();

        Assert.state(updated == 1, "Failed to create task " + task.taskTitle());
    }

    public void update(Task task, Integer id) {
        var updated = jdbcClient.sql("update task set task_title = ?, started_on = ?, completed_on = ?, time_set = ? where id = ?")
                .params(List.of(
                task.taskTitle(),
                task.startedOn(),
                task.completedOn(),
                task.timeSet().toString(), id))
                .update();

        Assert.state(updated == 1, "Failed to update task " + task.taskTitle());
    }

    public void delete(Integer id) {
        var updated = jdbcClient.sql("delete from task where id = :id")
                .param("id", id)
                .update();

        Assert.state(updated == 1, "Failed to delete task " + id);
    }

    public int count() {
        return jdbcClient.sql("select * from task").query().listOfRows().size();
    }

    public void saveAll(List<Task> tasks) {
        tasks.stream().forEach(this::create);
    }

    public List<Task> findByTimeSet(String timeSet) {
        return jdbcClient.sql("select * from task where time_set = :time_set")
                .param("time_set", timeSet)
                .query(Task.class)
                .list();
    }

}
