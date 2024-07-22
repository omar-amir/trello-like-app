package entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sprint_id;

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    private List<Task> toDoTasks = new ArrayList<>();

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    private List<Task> inProgressTasks = new ArrayList<>();

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    private List<Task> doneTasks = new ArrayList<>();

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public Sprint() {
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now().plusDays(14); // End date is 14 days after start date
    }

    // Getters and setters

    public int getId() {
        return sprint_id;
    }

    public void setId(int id) {
        this.sprint_id = id;
    }

    public List<Task> getToDoTasks() {
        return toDoTasks;
    }

    public void setToDoTasks(List<Task> toDoTasks) {
        this.toDoTasks = toDoTasks;
    }

    public List<Task> getInProgressTasks() {
        return inProgressTasks;
    }

    public void setInProgressTasks(List<Task> inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }

    public List<Task> getDoneTasks() {
        return doneTasks;
    }

    public void setDoneTasks(List<Task> doneTasks) {
        this.doneTasks = doneTasks;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
