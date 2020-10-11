package Dictionary;

import java.util.ArrayList;
import java.util.Collections;


public class Dictionary {
    private ArrayList<Word> listWord = new ArrayList<>();

    public void setListWord(ArrayList<Word> listWord) {
        this.listWord = listWord;
    }

    public ArrayList<Word> getListWord() {
        return listWord;
    }

    public void addWord(Word w) {
        listWord.add(w);
    }

    public void addWord(String en, String vn) {
        listWord.add(new Word(en, vn));
    }

    public void removeWord(String en, String vn) {
        int pos = -1;
        for (int i=0;i<listWord.size();i++)
        {
           if ((listWord.get(i).getWord_target()).equals(en))
           {
               pos = i;
           }
        }
        listWord.remove(listWord.get(pos));
    }
    public void modifyWord(String en, String vn) {
        int pos = -1;
        for (int i=0;i<listWord.size();i++)
        {
            if ((listWord.get(i).getWord_target()).equals(en))
            {
                pos = i;
            }
        }
        listWord.get(pos).setWord_explain(vn);
    }

    public Word getWord(int i) {
        return listWord.get(i);
    }

    public void sortDic() {
        Collections.sort(listWord);
    }
}