package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private String boardId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_teamleader_id")
    private user ownerTeamLeader;

    @ManyToMany
    @JoinTable(
            name = "board_collaborators",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public List<user> collaborators;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ListEntity> listsInBoard;

    // Constructors
    public Board() {
    	 this.collaborators = new ArrayList<>();
         this.listsInBoard = new ArrayList<>();
    }

    public Board(String name, user ownerTeamLeader) {
        this.name = name;
        this.ownerTeamLeader = ownerTeamLeader;
        this.collaborators = new ArrayList<>();
        this.listsInBoard = new ArrayList<>();
    }

    // Getters and setters
    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public user getOwnerTeamLeader() {
        return ownerTeamLeader;
    }

    public void setOwnerTeamLeader(user ownerTeamLeader) {
        this.ownerTeamLeader = ownerTeamLeader;
    }

    public void setCollaborators(List<user> collaborators) {
        this.collaborators = collaborators;
    }

    public void setListsInBoard(List<ListEntity> listsInBoard) {
        this.listsInBoard = listsInBoard;
    }

    
    public List<user> getCollaborators() {
        return collaborators;
    }

    public void addCollaborator(user u) {
    	  if (!collaborators.contains(u)) {
    	        collaborators.add(u);
    	    }
    }

    public List<ListEntity> getListsInBoard(){
        return listsInBoard;
    }
    //testsave

}
