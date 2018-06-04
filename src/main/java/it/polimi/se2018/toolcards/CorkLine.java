package it.polimi.se2018.toolcards;

import it.polimi.se2018.Player;

public class CorkLine extends ToolCard {

    String description = "After drafting, place the die in a spot that is not adjacent to another die" +
            "You must obey all other placement restrictions";


    public CorkLine(String name, String description, int tokenCount, String description1) {
        super(name, description, tokenCount);
        this.description = description1;
    }
}
