package service;

import entity.Sprint;
import entity.Task;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/sprints")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SprintService {

    @PersistenceContext
    private EntityManager entityManager;
    
    
    @POST
    @Path("/createSprint")
    public Response createSprint() {
        try {
            Sprint sprint = new Sprint();
            entityManager.persist(sprint);
            return Response.ok(sprint).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating sprint: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/createTask/{sprintId}")
    public Response createTask(@PathParam("sprintId") int sprintId) {
        try {
            Sprint sprint = entityManager.find(Sprint.class, sprintId);
            if (sprint != null) {
                Task newTask = new Task("task1name");
                newTask.setSprint(sprint);
                entityManager.persist(newTask);
                sprint.getToDoTasks().add(newTask); 
                entityManager.merge(sprint);
                entityManager.flush();
                return Response.ok("Task created successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Sprint not found").build();
            }
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }


    @PUT
    @Path("/startTask/{sprintId}/{taskId}")
    public Response startTask(
            @PathParam("sprintId") int sprintId,
            @PathParam("taskId") int taskId) {
        try {
            Sprint sprint = entityManager.find(Sprint.class, sprintId);
            if (sprint != null) {
                Task task = entityManager.find(Task.class, taskId);
                if (task != null && sprint.getToDoTasks().contains(task)) {
                    sprint.getInProgressTasks().add(task);
                    entityManager.merge(sprint);
                    return Response.ok("Task started successfully").build();
                }
                else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Task not found in the sprint or already started").build();
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Sprint not found").build();
            }
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }




    @PUT
    @Path("/endTask/{sprintId}/{taskId}")
    public Response endTask(
            @PathParam("sprintId") int sprintId,
            @PathParam("taskId") int taskId) {
        try {
            Sprint sprint = entityManager.find(Sprint.class, sprintId);
            if (sprint != null) {
                Task task = entityManager.find(Task.class, taskId);
                if (task != null && sprint.getInProgressTasks().contains(task)) {
                    sprint.getDoneTasks().add(task);
                    entityManager.merge(sprint);
                    return Response.ok("Task ended successfully").build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Task not found in progress or already ended").build();
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Sprint not found").build();
            }
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }
    
    @GET
    @Path("/{sprintId}")
    public Response getSprint(@PathParam("sprintId") int sprintId) {
        try {
            Sprint sprint = entityManager.find(Sprint.class, sprintId);
            if (sprint != null) {
                SprintDTO sprintDTO = convertToDTO(sprint);
                return Response.ok(sprintDTO).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Sprint not found").build();
            }
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }

    private SprintDTO convertToDTO(Sprint sprint) {
        SprintDTO sprintDTO = new SprintDTO();
        sprintDTO.setId(sprint.getId());
        sprintDTO.setStartDate(sprint.getStartDate());
        sprintDTO.setEndDate(sprint.getEndDate());
        sprintDTO.setToDoTasks(convertTasksToDTO(sprint.getToDoTasks()));
        sprintDTO.setInProgressTasks(convertTasksToDTO(sprint.getInProgressTasks()));
        sprintDTO.setDoneTasks(convertTasksToDTO(sprint.getDoneTasks()));
        return sprintDTO;
    }

    private List<TaskDTO> convertTasksToDTO(List<Task> tasks) {
        List<TaskDTO> taskDTOs = new ArrayList<>();
        for (Task task : tasks) {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(task.getId());
            taskDTO.setTaskName(task.getName());
            taskDTO.setStoryPoint(task.getStoryPoint());
            taskDTO.setSprint(task.getSprint());
            taskDTOs.add(taskDTO);
        }
        return taskDTOs;
    }

    
    @DELETE
    @Path("/{sprintId}")
    public Response endSprint(@PathParam("sprintId") int sprintId) {
        try {
            Sprint sprint = entityManager.find(Sprint.class, sprintId);
            if (sprint != null) {
                Sprint newSprint = new Sprint();
                newSprint.setInProgressTasks(sprint.getInProgressTasks());
                newSprint.setToDoTasks(sprint.getToDoTasks());
                entityManager.persist(newSprint);
                entityManager.remove(sprint);
                return Response.ok("Sprint ended successfully").build();
            }
            else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Sprint not found").build();
            }
        } catch (RuntimeException err) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage()).build();
        }
    }
}
