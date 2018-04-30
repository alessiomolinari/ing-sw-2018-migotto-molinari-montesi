package it.polimi.se2018;

import java.util.HashSet;

public abstract class PublicObjectiveCard {

    private String description;
    private int score;
    private String name;

    public int calculateScore(WindowPatternCard w){

    }
}


class ColorDiagonals extends PublicObjectiveCard {
    public int calculateScore(WindowPatternCard w) {
        int countScore = 0;
        COLOR currentColor;
        for (int row = 0; row < 3; row++) { //eccetto l'ultima riga
            for (int col = 0; col < 5; col++) {
                // checking if there's a die in the current cell
                if (w.getSchema().get(row * 5 + col).getAssociatedDie() != null) {
                    currentColor = w.getSchema().get(row * 5 + col).getAssociatedDie().getColor();
                    if (col > 0) { //checking sud-ovest cell die color
                        if (w.getSchema().get((row + 1) * 5 + (col - 1)).getAssociatedDie() != null) {
                            if (w.getSchema().get((row + 1) * 5 + (col - 1)).getAssociatedDie().getColor() == currentColor) {
                                countScore++;
                            }
                        }
                    }
                    if (col < 4) { //checking sud-est cell die color
                        if (w.getSchema().get((row + 1) * 5 + (col + 1)).getAssociatedDie() != null) {
                            if (w.getSchema().get((row + 1) * 5 + (col + 1)).getAssociatedDie().getColor() == currentColor) {
                                countScore++;
                            }
                        }
                    }
                }
            }
        }
        return countScore;
    }
}

class RowColorVariety extends PublicObjectiveCard{
    private int score = 6;
    private int total = 0;

    //what if a cell is empty?
    public int calculateScore(WindowPatternCard w){
        for (int i = 0; i < 4; i++){
            HashSet<COLOR> colors = new HashSet<>();
            for(int j = 0; j < 5; j++){
                colors.add(w.getCell(i, j).getAssociatedDie().getColor());
            }
            if(colors.size() == 5){
                total += score;
            }
        }
        return total;
    }
}

class ColumnColorVariety extends PublicObjectiveCard{
    private int score = 5;
    private int total = 0;
    //what if a cell is empty?
    public int calculateScore(WindowPatternCard w){
        for (int j = 0; j < 5; j++){
            HashSet<COLOR> colors = new HashSet<>();
            for(int i = 0; i < 4; i++){
                colors.add(w.getCell(i, j).getAssociatedDie().getColor());
            }
            if(colors.size() == 4){
                total += score;
            }
        }
        return total;
    }
}

class ColumnShadeVariety extends PublicObjectiveCard{
    private int score = 4;
    private int total = 0;
    //what if a cell is empty?
    public int calculateScore(WindowPatternCard w){
        for (int j = 0; j < 5; j++){
            HashSet<Integer> numbers = new HashSet<>();
            for(int i = 0; i < 4; i++){
                numbers.add(w.getCell(i, j).getAssociatedDie().getValue());
            }
            if(numbers.size() == 4){
                total += score;
            }
        }
        return total;
    }
}

class ColorVariety extends PublicObjectiveCard{
    private int score = 4;
    private int total = 0;
    public int calculateScore(WindowPatternCard w){
        HashSet<COLOR> colors = new HashSet<>();
        for (int i = 0; i < 4; i++){
            for(int j = 0; j < 5; j++){
                colors.add(w.getCell(i, j).getAssociatedDie().getColor());
            }
            if(colors.size() == 5){
                total += score;
            }
        }
        return total;
    }
}

class RowShadeVariety extends PublicObjectiveCard{
    private int score = 5;
    private int total = 0;

    //what if a cell is empty?
    public int calculateScore(WindowPatternCard w){
        for (int i = 0; i < 4; i++){
            HashSet<Integer> numbers = new HashSet<>();
            for(int j = 0; j < 5; j++){
                numbers.add(w.getCell(i, j).getAssociatedDie().getValue());
            }
            if(numbers.size() == 5){
                total += score;
            }
        }
        return total;
    }
}

class ShadeVariety extends PublicObjectiveCard{
    private int score = 5;
    private int total = 0;
    public int calculateScore(WindowPatternCard w){
        HashSet<Integer> numbers = new HashSet<>();
        for (int i = 0; i < 4; i++){
            for(int j = 0; j < 5; j++){
                numbers.add(w.getCell(i, j).getAssociatedDie().getValue());
            }
            if(numbers.size() == 6){
                total += score;
            }
        }
        return total;
    }
}

class DeepShade extends PublicObjectiveCard{
    private int score = 2;
    private int total = 0;
    public int calculateScore(WindowPatternCard w){
        HashSet<Integer> numbers = new HashSet<>();
        for (int i = 0; i < 4; i++){
            for(int j = 0; j < 5; j++){
                numbers.add(w.getCell(i, j).getAssociatedDie().getValue());
            }
            if(numbers.contains(5)&&(numbers.contains(6))){
                total += score;
            }
        }
        return total;
    }
}

class MediumShade extends PublicObjectiveCard{
    private int score = 2;
    private int total = 0;
    public int calculateScore(WindowPatternCard w){
        HashSet<Integer> numbers = new HashSet<>();
        for (int i = 0; i < 4; i++){
            for(int j = 0; j < 5; j++){
                numbers.add(w.getCell(i, j).getAssociatedDie().getValue());
            }
            if(numbers.contains(3)&&(numbers.contains(4))){
                total += score;
            }
        }
        return total;
    }
}

class  LightShade extends PublicObjectiveCard{
    private int score = 2;
    private int total = 0;
    public int calculateScore(WindowPatternCard w){
        HashSet<Integer> numbers = new HashSet<>();
        for (int i = 0; i < 4; i++){
            for(int j = 0; j < 5; j++){
                numbers.add(w.getCell(i, j).getAssociatedDie().getValue());
            }
            if(numbers.contains(1)&&(numbers.contains(2))){
                total += score;
            }
        }
        return total;
    }
}
