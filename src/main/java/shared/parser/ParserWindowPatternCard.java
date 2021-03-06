package shared.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.MalformedJsonException;
import server.model.COLOR;
import server.model.WindowPatternCard;
import shared.exceptions.NoSuchColorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that instantiates all the Window Pattern Cards
 * @author Alessio Molinari
 */
public class ParserWindowPatternCard {
    private static final String WPC_JSON = "wpc.json";

    private JsonArray cards;

    /**
     * @return ArrayList of all Window Pattern Cards contained in json file
     * @throws IOException
     */
    public ParserWindowPatternCard() throws IOException {
        ParserSettings settings = new ParserSettings();
        JsonObject WPCCards = settings.extractJsonObject(WPC_JSON);
        cards = WPCCards.get("WindowPatternCards").getAsJsonArray();
    }

    /**
     * @return ArrayList of all Window Pattern Cards contained in json file
     */
    public List<WindowPatternCard> parseAllCards(){
        //ArrayList which will contain every WindowPatternCard
        List<WindowPatternCard> windowPatternCards = new ArrayList<>();

        for (int i = 0; i < cards.size(); i++) {
            //create an empty WindowPatternCard with 20 numbered plain cells
            WindowPatternCard wpc = new WindowPatternCard();

            JsonObject card = cards.get(i).getAsJsonObject();
            parseSingleCard(wpc, card);
            windowPatternCards.add(wpc);
        }
        return windowPatternCards;
    }

    /**
     * @return Specific Window Pattern Card based on its name
     * @throws MalformedJsonException
     */
    public WindowPatternCard parseCardByName(String cardName) throws MalformedJsonException {
        for(JsonElement card : cards){
            if (card.getAsJsonObject().get("name").getAsString().equals(cardName)){
                WindowPatternCard wpc = new WindowPatternCard();
                parseSingleCard(wpc, card.getAsJsonObject());
                return wpc;
            }
        }
        throw new MalformedJsonException("Cannot find such card in deck");
    }

    /**
     * Retrieves a specific Window Pattern Card information from json file and uses it to create a Window Pattern Card
     */
    private void parseSingleCard(WindowPatternCard wpc, JsonObject card){

        String name = card.get("name").getAsString();
        int difficulty = card.get("difficulty").getAsInt();
        wpc.setName(name);
        wpc.setDifficulty(difficulty);

        JsonArray cells = card.get("schema").getAsJsonArray();

        //set constraints for each non-plain cell
        for (int i = 0; i < cells.size(); i++) {
            JsonObject cell = cells.get(i).getAsJsonObject();
            //set valueConstraint
            int row = cell.get("row").getAsInt();
            int column = cell.get("column").getAsInt();
            try{
                Integer valueConstraint = cell.get("valueConstraint").getAsInt();
                wpc.getCell(row, column).setValueConstraint(valueConstraint);
            } catch (NullPointerException e){
                wpc.getCell(row, column).setValueConstraint(null);
            }
            //set colorConstraint
            try {
                String tempstring = cell.get("colorConstraint").getAsString();
                COLOR colorConstraint = COLOR.stringToColor(tempstring);
                wpc.getCell(row, column).setColorConstraint(colorConstraint);
            } catch (NoSuchColorException e) {
                System.out.println("Controllare l'ortografia dei colori nel file json");
            } catch (NullPointerException e){
                wpc.getCell(row, column).setColorConstraint(null);
            }
        }
    }

}
