package service;

import java.util.List;

public class BoardDTO {
    private String boardId;
    private String name;
    private UserDTO owner;
    private List<UserDTO> collaborators;
    private List<ListEntityDTO> listsInBoard; 

    // Constructors, getters, and setters

    public BoardDTO() {
    }

    public BoardDTO(String boardId, String name, UserDTO owner, List<UserDTO> collaborators, List<ListEntityDTO> listsInBoard) {
        this.boardId = boardId;
        this.name = name;
        this.owner = owner;
        this.collaborators = collaborators;
        this.listsInBoard = listsInBoard;
      
    }

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

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public List<UserDTO> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<UserDTO> collaborators) {
        this.collaborators = collaborators;
    }
    public List<ListEntityDTO> getListsInBoard() {
        return listsInBoard;
    }
	public void setListsInBoard(List<ListEntityDTO> listsInBoard) {
		this.listsInBoard=listsInBoard;
	}

}

