package entity;

import javax.persistence.*;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "Task_Name")
    private String TaskName;

    @Column(name = "story_point")
    private int storyPoint;

    @ManyToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    public Task() {
        // Set the story point to a random Fibonacci number
        this.storyPoint = 5;
    }
    
    public Task(String name) {
    	this.TaskName=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return TaskName;
    }

    public void setName(String name) {
        this.TaskName=name;
    }

    public int getStoryPoint() {
        return storyPoint;
    }

    public void setStoryPoint(int storyPoint) {
        this.storyPoint = storyPoint;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }
}
