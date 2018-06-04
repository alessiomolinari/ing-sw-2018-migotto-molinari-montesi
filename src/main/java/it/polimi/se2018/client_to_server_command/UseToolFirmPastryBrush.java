package it.polimi.se2018.client_to_server_command;

public class UseToolFirmPastryBrush extends ClientToServerCommand{
    public Integer getDieValue() {
        return dieValue;
    }

    public Integer getDiePosition() {
        return diePosition;
    }

    public Integer getDieOldPosition() {
        return dieOldPosition;
    }

    /**
     * Decides the die of DraftPool he wants to roll again
     * message = .. dieColor
     *
     * PENNELLO PASTA SALDA
     *
     *  Decides if to place the changed value die or if to put in the draftPool
     *  To let the Controller know, the answer message has to indicate PLACE if he wants to place or DRAFTPOOL if not
     *  message = UseToolFirmPastryBrush2 PLACE/DRAFTPOOL
     *
     *  Integer diePosition
     * Integer dieValue
     *
     * I HAVE TO COMUNICATE TO THE SERVER
     * - WHere i want to place the die
     * - in which position
     */
    //nameClass DRAFTPOOL/SCHEMA dieColor

    private Integer dieValue;

    private Integer diePosition; // in  Schema ony ( can be null)

    private Integer dieOldPosition;

    public UseToolFirmPastryBrush(String message, Integer dieValue, Integer dieOldPosition, Integer diePosition) {
        this.message = message;
        this.dieValue = dieValue;
        this.diePosition = diePosition;
        this.dieOldPosition = dieOldPosition;
    }
}