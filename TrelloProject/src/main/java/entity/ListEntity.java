package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "list_entity")
public class ListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_id")
    private String listId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "listEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "board_id") // Assuming this is the foreign key column name
    private Board board;

    // Constructors
    public ListEntity() {
    }

    public ListEntity(String name) {
        this.name = name;
    }

    // Getters and setters
    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
    public Board getBoard() {
    	return board;
    }
    public void setBoard(Board board) {
    	this.board=board;
    }
}
