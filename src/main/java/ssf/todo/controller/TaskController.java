package ssf.todo.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ssf.todo.Constants;

@Controller
@RequestMapping(path="/task", produces=MediaType.TEXT_HTML_VALUE)
public class TaskController {
    private final Logger logger = Logger.getLogger(TaskController.class.getName());
    @Autowired
    RedisTemplate<String, String> template;
    

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

        return "index";
    }

    @PostMapping("/save")
    public String saveToDb(@RequestBody MultiValueMap<String, String> form, Model model) {
        String contents = form.getFirst("contents");
        logger.log(Level.INFO, "Saving information to database: %s".formatted(contents));
        return "index";
    }    
}
