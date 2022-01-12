package ssf.todo.model;

import java.io.Serializable;

import ssf.todo.Constants;

public class Tasklist implements Serializable{
    private static final long serialVersionUID = 1L;
    private String contentlist;
    private final String DELIMITER = Constants.DELIMITER;
    private final String RE_DELIMITER = Constants.RE_DELIMITER;
    
    public Tasklist() {
    }

    public Tasklist(String contentlist) {
        this.contentlist = contentlist;
    }

    public void addTask(String task) {
        this.contentlist = this.contentlist + DELIMITER + task;
    }

    public String getContentlist() {
        return this.contentlist;
    }
    
    public String[] getAllTasks() {
        return this.contentlist.split(RE_DELIMITER);
    }
}
