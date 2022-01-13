package ssf.todo.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssf.todo.repositories.TaskRepo;

@Service
public class TaskService {
    private final Logger logger = Logger.getLogger(TaskService.class.getName());
    @Autowired
    private TaskRepo taskRepo;

    public boolean hasKey(String key) {
        Optional<String> opt = taskRepo.get(key);
        logger.log(Level.INFO, "checking for " + key + ", which contains: " + opt);
        return opt.isPresent();
    }
    
    public List<String> get(String key) {
        Optional<String> opt = taskRepo.get(key);
        List<String> list = new LinkedList<>();
        if (opt.isPresent()) {
            for (String t: opt.get().split("\\|"))
                list.add(t);
        }
        return list;
    }

    public void save(String key, List<String> values) {
        String l = values.stream()
        .collect(Collectors.joining("|"));
        this.save(key,l);
    }

    public void save(String key, String value) {
        taskRepo.save(key, value);
    }
}
