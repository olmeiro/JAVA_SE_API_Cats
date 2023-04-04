package org.cats;

import org.cats.model.Cats;

import javax.swing.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        int menuOption = -1;
        String[] buttoms = {"1. See cats.", "2. See Fav.", "3. Go out."};
        do{
            String option = (String) JOptionPane.showInputDialog(null, "Java CATS", "Main menu",JOptionPane.INFORMATION_MESSAGE, null, buttoms, buttoms[0]);

            // for (int i = 0; i < buttoms.length; i++) {
            //     if(option.equals(buttoms[i])){
            //         menuOption = i;
            //     }
            // } //este for lo podemos escribir como:
            menuOption = Arrays.asList(buttoms).indexOf(option);

            switch (menuOption){
                case 0:
                    //CatService.seeRandomCats();
                    break;
                case 1:
                    Cats cat = new Cats();
                    //CatService.seeFavoriteCats(cat.getApikey());
                default:
                    break;
            }


        }while(menuOption != 2);
    }
}