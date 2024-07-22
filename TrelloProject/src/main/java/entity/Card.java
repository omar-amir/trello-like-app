package entity;

import javax.persistence.*;

@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private String cardId;

    @Column(name = "description")
    private String description;

    @Column(name = "assignee_id")
    private String assigneeId;

    @Column(name = "status")
    private String status;

    @Column(name = "comment")
    private String comment;
    
    @ManyToOne
    @JoinColumn(name = "list_id") // assuming this is the foreign key column name
    private ListEntity listEntity;

    // Constructors
    public Card() {
    }

    public Card(String description, String assigneeId, String status, String comment) {
        this.description = description;
        this.assigneeId = assigneeId;
        this.status = status;
        this.comment = comment;
    }

    // Getters and setters
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

	public void setListEntity(ListEntity list) {
		listEntity=list;
		
	}

	public ListEntity getListEntity() {
		return listEntity;
	}

}
