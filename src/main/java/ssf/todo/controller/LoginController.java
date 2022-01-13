package ssf.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import ssf.todo.services.TaskService;

@Controller
@RequestMapping(path="/login", produces=MediaType.TEXT_HTML_VALUE)
public class LoginController {
    private final Logger logger = Logger.getLogger(TaskController.class.getName());
    public String username;
    @Autowired
    private TaskService taskSvc;

    @PostMapping
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
