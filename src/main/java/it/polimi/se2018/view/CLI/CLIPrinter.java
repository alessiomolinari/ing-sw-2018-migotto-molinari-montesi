package it.polimi.se2018.view.CLI;

import it.polimi.se2018.exceptions.EmptyCellException;
import it.polimi.se2018.model.COLOR;
import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.WindowPatternCard;

import java.util.List;

class CLIPrinter {

    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";

    CLIPrinter() {
    }

    void printCellList(List<Cell> list){
        String[][] table = new String[4][list.size()];
        for (int i = 0; i < list.size(); i++){
            Cell cell = list.get(i);
            try{
                Die die = cell.getAssociatedDie();
                insertDieValue(table, 0, i, die.getValue(), die.getColor());
            } catch (EmptyCellException e){
                insertDieValue(table, 0, i);
            }
        }
    }

    void printWPC(WindowPatternCard wpc){
        String[][] table = new String[16][5];
        System.out.println(wpc.getCardName() + " - " + wpc.getDifficulty() + "\n");

        for(int i = 0; i < wpc.getSchema().size(); i++){
            int row = wpc.getCell(i).getRow();
            int column = wpc.getCell(i).getColumn();
            Cell cell = wpc.getCell(i);
            try {
                Die die = cell.getAssociatedDie();
                insertDieValue(table, row, column, die.getValue(), die.getColor());
            } catch (EmptyCellException e) {
                if (cell.getColorConstraint() != null){
                    insertDieValue(table, row, column, cell.getColorConstraint());
                }
                else if (cell.getValueConstraint() != null){
                    insertDieValue(table, row, column, cell.getValueConstraint());
                } else {
                    insertDieValue(table, row, column);
                }
            }
        }
        printTable(table);
        System.out.println("\n");
    }

    private void insertDieValue(String[][] table, int row, int column, int value, COLOR color){
        String background = stringColorBackground(color);
        String[] die = stringDieValue(value);
        addStringToMatrix(table, row, column, background, die, true);
    }

    private void insertDieValue(String[][] table, int row, int column, int value){
        String[] die = stringDieValue(value);
        addStringToMatrix(table, row, column, ANSI_RESET, die);
    }

    private void insertDieValue(String[][] table, int row, int column, COLOR color){
        String background = stringColorBackground(color);
        String[] die = stringDieValue(0);
        addStringToMatrix(table, row, column, background, die);
    }

    private void insertDieValue(String[][] table, int row, int column){
        String[] die = stringDieValue(0);
        addStringToMatrix(table, row, column, ANSI_RESET, die);
    }

    private String stringColorBackground(COLOR color){
        String background;
        switch(color){
            case YELLOW:
                background = ANSI_YELLOW_BACKGROUND;
                break;
            case GREEN:
                background = ANSI_GREEN_BACKGROUND;
                break;
            case BLUE:
                background = ANSI_BLUE_BACKGROUND;
                break;
            case VIOLET:
                background = ANSI_PURPLE_BACKGROUND;
                break;
            case RED:
                background = ANSI_RED_BACKGROUND;
                break;
            default:
                background = ANSI_RESET;
        }
        return background;
    }

    private String[] stringDieValue(int value){
        String[] die = new String[3];

        switch (value){
            case 1:
                die[0] = "       ";
                die[1] = "   o   ";
                die[2] = "       ";
                break;
            case 2:
                die[0] = "       ";
                die[1] = " o   o ";
                die[2] = "       ";
                break;
            case 3:
                die[0] = "     o ";
                die[1] = "   o   ";
                die[2] = " o     ";
                break;
            case 4:
                die[0] = " o   o ";
                die[1] = "       ";
                die[2] = " o   o ";
                break;
            case 5:
                die[0] = " o   o ";
                die[1] = "   o   ";
                die[2] = " o   o ";
                break;
            case 6:
                die[0] = " o   o ";
                die[1] = " o   o ";
                die[2] = " o   o ";
                break;
            default:
                die[0] = "       ";
                die[1] = "       ";
                die[2] = "       ";
        }
        return die;
    }

    //sinceramente dovrebbe stampare i pallini neri sui dadi colorati: in realtà sono bianchi ma hanno abbastanza constrasto
    private void addStringToMatrix(String[][] table, int row, int column, String background, String[] die, boolean printBlack){
        int i = row*4;
        for(int j = 0; j < 3; j++){
            table[i+j][column]= background;
            if(printBlack){
                table[i+j][column]+= ANSI_BLACK;
            }
            table[i+j][column]+= die[j];
            table[i+j][column]+= ANSI_RESET;
            table[i+j][column]+= "|";
        }
        table[i+3][column] = "--------";
    }

    private void addStringToMatrix(String[][] table, int row, int column, String background, String[] die) {
        addStringToMatrix(table, row, column, background, die, false);
    }

    private void printTable(String[][] table){
        StringBuilder line;
        for (int i = 0; i < table.length; i++){
            line = new StringBuilder();
            for(int j = 0; j < table[i].length; j++){
                line.append(table[i][j]);
            }
            System.out.println(line);
        }
    }

}
