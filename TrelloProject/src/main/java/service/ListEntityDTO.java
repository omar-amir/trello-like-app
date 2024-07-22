package service;

import java.util.ArrayList;
import java.util.List;

public class ListEntityDTO {
    private String listId;
    private String name;
    private List<CardDTO> cards = new ArrayList<>();
    // Other attributes as needed

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

    public List<CardDTO> getCards() {
        return cards;
    }

    public void setCards(List<CardDTO> cards) {
        this.cards = cards;
    }

    // Other getters and setters as needed
}

