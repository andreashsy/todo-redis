package ssf.todo.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ssf.todo.Constants;
import ssf.todo.services.TaskService;

@Controller
@RequestMapping(path="/task", produces=MediaType.TEXT_HTML_VALUE)
public class TaskController {
    private final Logger logger = Logger.getLogger(TaskController.class.getName());
    public String username;
    @Autowired
    private TaskService taskSvc;
    
    @PostMapping
    public String postTask(@RequestBody MultiValueMap<String, String> form, Model model) {
        
        String contents = form.getFirst("contents");
        String taskName = form.getFirst("taskName");
        logger.log(Level.INFO, "taskName from form: %s".formatted(taskName));
        logger.log(Level.INFO, "hidden contents from form: %s".formatted(contents));
        logger.log(Level.INFO, "DELIMITER is: %s".formatted(Constants.DELIMITER));

        // split the contents into List, delimited by |
        List<String> tasks = new LinkedList<>();
        if (contents.trim().length() != 0) {
            contents = "%s%s%s".formatted(contents, Constants.DELIMITER, taskName);
            tasks = Arrays.asList(contents.split(Constants.RE_DELIMITER));
       } else {
           contents = taskName;
           tasks.add(contents);
       }

        logger.log(Level.INFO, "tasks: %s".formatted(tasks));
        
        model.addAttribute("contents", contents);
        model.addAttribute("tasks", tasks);

        return "form";
    }

    @PostMapping("/save")
    public String saveToDb(@RequestBody MultiValueMap<String, String> form, Model model) {
        String contents = form.getFirst("contents");
        logger.log(Level.INFO, "Saving information to database: %s".formatted(contents));
        taskSvc.save(username, contents);
        return "form";
    }   
    
    @PostMapping("/login")
    public String login(@RequestBody MultiValueMap<String, String> form, Model model) {
        username = form.getFirst("username");
        logger.log(Level.INFO, "username: %s".formatted(username));
        // checks db for username, if exists, load the current todo list
        if (taskSvc.hasKey(username)) {
            List<String> tasks = taskSvc.get(username);            
            String contents = tasks.stream().collect(Collectors.joining("|"));
            logger.log(Level.INFO, "loaded tasks %s".formatted(tasks));
            logger.log(Level.INFO, "loaded contents %s".formatted(contents));
            model.addAttribute("tasks", tasks);
            model.addAttribute("contents", contents);
        } else {
            logger.log(Level.INFO, "user %s does not exist, created new user".formatted(username));
        }
        model.addAttribute("username", username);
        return "form";
    }
    
}
