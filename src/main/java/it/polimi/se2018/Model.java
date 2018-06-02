package it.polimi.se2018;

import it.polimi.se2018.parser.ParserPrivateObjectiveCard;
import it.polimi.se2018.parser.ParserPublicObjectiveCard;
import it.polimi.se2018.parser.ParserWindowPatternCard;
import it.polimi.se2018.public_obj_cards.PublicObjectiveCard;
import it.polimi.se2018.toolcards.ToolCard;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


/**
 * This is the model, the class that maintain the State of the game
 * It is an Observable from a VirtualView (the Observer).
 * The Virtual View just send "broadcast" all graphical changes of the board
 *
 * The controller directly modifies the Model.
 *
 */

public class Model extends Observable{

    private ArrayList<Observer> observers;
    private DiceBag diceBag;

    private ArrayList<PrivateObjectiveCard> privateObjectiveCardDeck;

    private ArrayList<PublicObjectiveCard> publicObjectiveCardDeck;
    private ArrayList<PublicObjectiveCard> extractedPublicObjectiveCard;

    private ArrayList<ToolCard> toolCardDeck;
    private ArrayList<ToolCard> extractedToolCard;

    private ArrayList<WindowPatternCard> windowPatternCardDeck;

    //Changed from ClientConnection to Players (The conotroller knows to which player correspond its Client)
    private ArrayList<Player> connectedPlayers;
    private ArrayList<Player> disconnectedPlayers;

    private ArrayList<Player> gamePlayers;
    private RoundTrack roundTrack;
    private Round currentRound;
    private ArrayList<Round> gameRounds;

    /**
     * Constructor: generates a game by
     * uploading all WindowPatternCards, PublicObjectiveCards, PrivateObjectiveCards and ToolCards
     * extracting 3 PublicObjectiveCards, creating 10 rounds
     * initializing the diceBag, the game players list, the roundTrack
     * @param players list of game players
     */
    public Model(ArrayList<Player> players){
        gamePlayers = players;
        gameRounds = createRounds();
        diceBag = DiceBag.getInstance();
        roundTrack = new RoundTrack();
        // TODO CREATE ALL CARDS FROM JSON FILES
        ParserPrivateObjectiveCard parserPrivateObjectiveCard = new ParserPrivateObjectiveCard();
        ParserPublicObjectiveCard parserPublicObjectiveCard = new ParserPublicObjectiveCard();
        ParserWindowPatternCard parserWindowPatternCard = new ParserWindowPatternCard();

        try {
            windowPatternCardDeck = parserWindowPatternCard.parseCards();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            privateObjectiveCardDeck = parserPrivateObjectiveCard.parseCards();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            publicObjectiveCardDeck = parserPublicObjectiveCard.parseCards();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extracting three cards
        Collections.shuffle(publicObjectiveCardDeck);
        extractedPublicObjectiveCard = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            extractedPublicObjectiveCard.add(publicObjectiveCardDeck.remove(0));
        }
        // ora aspetto che il controller esegua comandi
    }


    /**
     * Initializes all 10 rounds with all attributes except draftPool dice (they are extracted every time)
     */
    public ArrayList<Round> createRounds() {
        ArrayList<Round> roundList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            roundList.add(new Round(i+1, gamePlayers.get(((i+1)%4)-1), gamePlayers, diceBag));
        }
        return roundList;
    }

    public ArrayList<PublicObjectiveCard> getExtractedPublicObjectiveCard(){
        for (int i=0; i<3; i++){
            int index = ThreadLocalRandom.current().nextInt(0,  publicObjectiveCardDeck.size());
            extractedPublicObjectiveCard.set(i, publicObjectiveCardDeck.remove(index));
        }
        return extractedPublicObjectiveCard;
    }

    /**
     * Returns an ArrayList of 4 WindowPatternCards
     * @return list of extracted cards
     */
    public ArrayList<WindowPatternCard> extractWindowPatternCard(){
        for (int i=0; i<4; i++){
            int index = ThreadLocalRandom.current().nextInt(0,  windowPatternCardDeck.size());
            extractWindowPatternCard().set(i, windowPatternCardDeck.remove(index));
        }
        return extractWindowPatternCard();
    }

    public ArrayList<ToolCard> getExtractedToolCard(){
        for (int i=0; i<3; i++){
            int index = ThreadLocalRandom.current().nextInt(0,  toolCardDeck.size());
            extractedToolCard.set(i, toolCardDeck.remove(index));
        }
        return extractedToolCard;
    }

    public Round getCurrentRound(){
        return currentRound;
    }

    public Round getRound(int roundNumber){
        return gameRounds.get(roundNumber);
    }

    public RoundTrack getRoundTrack(){
        return roundTrack;
    }

    /**
     * Adds a player to the disconnected ones
     * @param pl to be added player
     */
    public void addDisconnectedPlayer(Player pl){
        disconnectedPlayers.add(pl);
        connectedPlayers.remove(pl);
    }

    /**
     * Adds a player to the connected ones
     * @param pl to be added player
     */
    public void addReconnectedPlayer(Player pl){
        disconnectedPlayers.remove(pl);
        connectedPlayers.add(pl);
    }

    /**
     * Notifies Controller of Model changes
     */
    public void notifyModelChanges(){
        for (Observer o : observers)
            o.update(this, this);
    }

    /**
     * Adds an observer
     * @param o to be added observer
     */
    public void addObserver(Observer o){
        observers.add(o);
    }


    public ArrayList<Player> getGamePlayers() {
        return gamePlayers;
    }

    public ArrayList<Round> getGameRounds() {
        return gameRounds;
    }

    public DiceBag getDiceBag() {
        return diceBag;
    }


    public ArrayList<Player> getConnectedPlayers() {
        return connectedPlayers;
    }

    public ArrayList<Player> getDisconnectedPlayers() {
        return disconnectedPlayers;
    }


}
