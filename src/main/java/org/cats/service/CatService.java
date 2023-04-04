package org.cats.service;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.cats.model.Cats;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class CatService {

    private static String BASE_URL = "https://api.thecatapi.com/v1/";
    private static String SEARCH_ENDPOINT = BASE_URL+"images/search";
    private static String FAVORITE_ENDPOINT = BASE_URL+"favourites";
    private static String FavoriteMenu = "Opciones: \n"
            + " 1. ver otra imagen \n"
            + " 2. Eliminar Favorito \n"
            + " 3. Volver \n";
    private static String randomCatsMenu = "Opciones: \n"
            + " 1. ver otra imagen \n"
            + " 2. Favorito \n"
            + " 3. Volver \n";

    public static void seeRandomCats() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SEARCH_ENDPOINT).get().build();
        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();

        //delete [] square brackets from response:
        jsonData = jsonData.substring(1, jsonData.length());
        jsonData = jsonData.substring(0, jsonData.length() - 1);

        //Create object GJson:
        Gson gson = new Gson();

        //Convert to Object Cats:
        Cats cats = gson.fromJson(jsonData, Cats.class);

        //resize image:
        Image image = null;
        try{
            URL url = new URL(cats.getUrl());
            image = ImageIO.read(url);

            ImageIcon backgroundCat = new ImageIcon(image);

            if(backgroundCat.getIconWidth() > 800){
                //resize:
                Image background = backgroundCat.getImage();
                Image modified = background.getScaledInstance(800,600, Image.SCALE_SMOOTH);
                backgroundCat = new ImageIcon(modified);
            }

            String menu = "Options: \n"
                    + "1. See other image. \n "
                    + "2. Favorite \n"
                    + "3. Backd \n";

            String[] buttoms = {"See other image", "Favorite", "Back"};
            String id_cat = cats.getId();

            //Graphic interface:
            String option = (String) JOptionPane.showInputDialog(null, menu,id_cat, JOptionPane.INFORMATION_MESSAGE, backgroundCat,buttoms, buttoms[0]);

            //What options choose user:
            int selection = -1; //from 0
            for (int i = 0; i < buttoms.length; i++) {
                selection = i;
            }

            switch (selection){
                case 0:
                    seeRandomCats();
                    break;
                case 1:
                    favoriteCat(cats);
                    break;
                default:
                    break;
            }
        }catch(IOException e) {
            System.out.println(e);
        }
    }

    public static void favoriteCat(Cats cats){
        System.out.println("hola gates");
    }
}
