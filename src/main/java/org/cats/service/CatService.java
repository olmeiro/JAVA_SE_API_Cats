package org.cats.service;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

import io.github.cdimascio.dotenv.Dotenv;
import org.cats.model.Cats;
import org.cats.model.FavoriteCat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class CatService {

    Dotenv dotenv = Dotenv.load();

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
                    + "2. FavoriteCat \n"
                    + "3. Backd \n";

            String[] buttoms = {"See other image", "FavoriteCat", "Back"};
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
        try{
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"image_id\":\""+cats.getId()+"\"\n}");
            Request request = new Request.Builder()
                    .url(FAVORITE_ENDPOINT)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", cats.getApikey())
                    .build();
            Response response = client.newCall(request).execute();

            if(!response.isSuccessful()) {
                response.body().close();
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void seeFavoriteCats(String api_key) throws IOException {
        System.out.println(api_key);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(FAVORITE_ENDPOINT)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", api_key)
                .build();

        Response response = client.newCall(request).execute();

        String jsonData = response.body().string();

        if(!response.isSuccessful()) {
            response.body().close();
        }

        Gson gson = new Gson();

        FavoriteCat[] catsArray = gson.fromJson(jsonData,FavoriteCat[].class);
        if(catsArray.length > 0){
            int min = 1;
            int max  = catsArray.length;
            int aleatorio = (int) (Math.random() * ((max-min)+1)) + min;
            int indice = aleatorio-1;
            FavoriteCat favoriteCat = catsArray[indice];


            Image image = null;
            try{
                URL url = new URL(favoriteCat.image.getUrl());
                image = ImageIO.read(url);

                ImageIcon catImageIcon = new ImageIcon(image);

                if(catImageIcon.getIconWidth() > 800){

                    Image background = catImageIcon.getImage();
                    Image modified = background.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                    catImageIcon = new ImageIcon(modified);
                }

                String[] buttoms = { "See other image", "Delete fav", "Back" };
                String catId = favoriteCat.getId();
                String option = (String) JOptionPane.showInputDialog(null, FavoriteMenu, catId, JOptionPane.INFORMATION_MESSAGE, catImageIcon, buttoms,buttoms[0]);

                int selection = -1;

                for(int i=0;i<buttoms.length;i++){
                    if(option.equals(buttoms[i])){
                        selection = i;
                    }
                }

                switch (selection){
                    case 0:
                        seeFavoriteCats(api_key);
                        break;
                    case 1:
                        deleteFavorite(favoriteCat);
                        break;
                    default:
                        break;
                }

            }catch(IOException e){
                System.out.println(e);
            }
        }
    }

    public static void deleteFavorite(FavoriteCat favoriteCat){
        try{
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(FAVORITE_ENDPOINT+favoriteCat.getId()+"")
                    .delete(null)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", favoriteCat.getApikey())
                    .build();

            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()) {
                response.body().close();
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
