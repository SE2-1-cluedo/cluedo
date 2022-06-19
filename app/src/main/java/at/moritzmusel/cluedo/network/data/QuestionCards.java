package at.moritzmusel.cluedo.network.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import at.moritzmusel.cluedo.network.pojo.Card;

public class QuestionCards {
    private final int questionsAmount = 39;
    private List<Card> questions;
    private AssetManager mngr;
    private InputStream is;
    public QuestionCards(Context ctx){
         questions = new ArrayList<>(questionsAmount);
         this.mngr = ctx.getAssets();
        try {
            is = mngr.open("questions.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        initCards();
    }

    public List<Card> getQuestionCards() {
        if (questions == null) initCards();
        return questions;
    }
    private List<Card> initCards(){
        List<Card> q = new ArrayList<>(questionsAmount);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            Log.d("QuestionCards", String.valueOf(br));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                q.add(new Card(i+questionsAmount,line.split(":")[1]+ " " + line.split(":")[2]));
                i++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return q;
    }
}
