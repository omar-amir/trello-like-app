package service;
import java.time.LocalDate;
import java.util.List;

public class SprintDTO {
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<TaskDTO> toDoTasks;
    private List<TaskDTO> inProgressTasks;
    private List<TaskDTO> doneTasks;

    public SprintDTO() {
    	
    }

    public SprintDTO(int id, LocalDate startDate, LocalDate endDate, List<TaskDTO> toDoTasks, List<TaskDTO> inProgressTasks, List<TaskDTO> doneTasks) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.toDoTasks = toDoTasks;
        this.inProgressTasks = inProgressTasks;
        this.doneTasks = doneTasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<TaskDTO> getToDoTasks() {
        return toDoTasks;
    }

    public void setToDoTasks(List<TaskDTO> toDoTasks) {
        this.toDoTasks = toDoTasks;
    }

    public List<TaskDTO> getInProgressTasks() {
        return inProgressTasks;
    }

    public void setInProgressTasks(List<TaskDTO> inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }

    public List<TaskDTO> getDoneTasks() {
        return doneTasks;
    }

    public void setDoneTasks(List<TaskDTO> doneTasks) {
        this.doneTasks = doneTasks;
    }
}
