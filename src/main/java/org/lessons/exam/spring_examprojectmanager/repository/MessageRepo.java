package org.lessons.exam.spring_examprojectmanager.repository;

import java.util.List;

import org.lessons.exam.spring_examprojectmanager.models.Message;
import org.lessons.exam.spring_examprojectmanager.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends JpaRepository<Message, Integer> {
    
    public List<Message> findByTask(Task task);
    public Message findByIdAndTask(Integer id, Task task);

}
