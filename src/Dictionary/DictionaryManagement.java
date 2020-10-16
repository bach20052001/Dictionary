package Dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class DictionaryManagement {

    private Dictionary dic = new Dictionary();

    public Dictionary getDic() {
        return dic;
    }

    public void insertFromCommandline() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of words: ");
        int n = sc.nextInt();
        String tmp = sc.nextLine();
        for (int i = 0; i < n; i++) {
            System.out.printf("Enter English word: ", i + 1);
            String target = sc.nextLine();
            System.out.printf("Enter mean in Vietnamese: ", i + 1);
            String explain = sc.nextLine();
            dic.addWord(new Word(target, explain));
        }
    }

    public void insertFromFile() throws FileNotFoundException {
        String line;
        Word newWord;
        File myObj = new File("C:\\Users\\Lenovo\\IdeaProjects\\Bachh_Dictionary\\src\\Dictionary\\dictionaries.txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            line = myReader.nextLine();
            newWord = new Word(line);
            dic.addWord(newWord);
        }
    }


    public void insertFromFile(String markUp) throws FileNotFoundException {
        String line;
        File myObj = null;
            if (markUp.equals("markUp")){
                myObj = new File("C:\\Users\\Lenovo\\IdeaProjects\\Bachh_Dictionary\\src\\Dictionary\\DictionaryMark.txt");

            }
            else if (markUp.equals("ViToEn")){
                myObj = new File("C:\\Users\\Lenovo\\IdeaProjects\\Bachh_Dictionary\\src\\Dictionary\\dicForWrite.txt");
            }
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            line = myReader.nextLine();
            int pos = line.indexOf(':');
            dic.addWord(new Word(line.substring(0,pos),line.substring(pos+1)));
        }
    }



    public int dictionaryLookup() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Seach word: ");
        String engWord = sc.nextLine();

        int pos = Collections.binarySearch(dic.getListWord(), new Word(engWord, null));

        if (pos >= 0) {
            System.out.println("Mean: " + dic.getListWord().get(pos).getWord_explain());
        } else {
            System.out.println("The word is not exist in dictionary!");
            pos = -1;
        }
        return pos;
    }

    public int dictionaryLookup(String en) {
        int pos = Collections.binarySearch(dic.getListWord(), new Word(en, null));
        return pos;
    }


    public void addWord() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert English word : ");
        String engWord = scanner.nextLine();
        System.out.println("Insert Viet Nam word : ");
        String vieWord = scanner.nextLine();
        dic.addWord(new Word(engWord, vieWord));
        dic.sortDic();
    }

    public void addWord(String engWord, String vieWord) {
        dic.addWord(new Word(engWord, vieWord));
        dic.sortDic();
    }

    public void addWord(String engWord, String vieWord, String history) {
        dic.addWord(new Word(engWord, vieWord));
    }

    public Dictionary fixWord() {
        System.out.println("The word that you want to fix : ");
        Scanner scanner = new Scanner(System.in);
        String editWord = scanner.nextLine();
        for (int i = 0; i < dic.getListWord().size(); i++) {
            if (editWord.equals(dic.getListWord().get(i).getWord_target())) {
                System.out.println("Fix word: ");
                dic.getListWord().get(i).setWord_target(scanner.nextLine());
                System.out.println("Fix word mean: ");
                dic.getListWord().get(i).setWord_explain(scanner.nextLine());
            }
        }
        return dic;
    }

    public void deleteWord() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Insert the word you want to delete in English : ");
            String word = sc.nextLine();
            for (Word element : dic.getListWord()) {
                if (element.getWord_target().equals(word)) {
                    dic.getListWord().remove(element);

                }
            }
        } catch (Exception ex) {
            System.out.println("removed");
        }
    }

    public void deleteWord(String engWord, String vieWord) {
        dic.removeWord(engWord, vieWord);
    }

    public void modifyWord(String en, String vn) {
        dic.modifyWord(en, vn);
    }

    public void dictionaryExportToFile() {
        try {
            File file = new File("C:\\Users\\Lenovo\\IdeaProjects\\Bachh_Dictionary\\src\\Dictionary\\dicForWrite.txt");
            FileWriter fw = new FileWriter(file);

            for (int i = 0; i < dic.getListWord().size(); i++) {
                fw.write(dic.getListWord().get(i).getWord_explain() + " : " + dic.getListWord().get(i).getWord_target() + "\n");
            }
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> prefixSeach(String prefix) {
        List<String> words = new ArrayList<>();
        for (int i = 0; i < dic.getListWord().size(); i++) {
            if (dic.getWord(i).getWord_target() == prefix) {
                words.add(dic.getWord(i).getWord_explain());
            }
        }
        return words;
    }

    public String get(String selectedItem) {
        for (int i = 0; i < dic.getListWord().size(); i++) {
            if (dic.getWord(i).getWord_target() == selectedItem) {
                return dic.getWord(i).getWord_explain();
            }
        }
        return null;
    }

    public String showSomeWords(String pre) {
        StringBuilder show = new StringBuilder();
        for (int i = 0; i < dic.getListWord().size(); i++) {
            if (dic.getWord(i).getWord_target().indexOf(pre) >= 0)
                show.append(dic.getWord(i).getWord_target()).append(" : ").append(dic.getWord(i).getWord_explain()).append("\n\n");
        }
        return show.toString();
    }

    public String showAllWords() {
        StringBuilder show = new StringBuilder();
        for (int i = 0; i < dic.getListWord().size(); i++) {
            show.append(dic.getWord(i).getWord_target()).append(" : ").append(dic.getWord(i).getWord_explain()).append('\n');
        }
        return show.toString();
    }

    public void saveFile() {
        try {
            File file = new File("C:\\Users\\Lenovo\\IdeaProjects\\Bachh_Dictionary\\src\\Dictionary\\dictionaries.txt");
            FileWriter fw = new FileWriter(file);

            for (int i = 0; i < dic.getListWord().size(); i++) {
                fw.write(dic.getListWord().get(i).getWord_target() + "\t" + dic.getListWord().get(i).getWord_explain() + "\n");
            }
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveFile(String markUp) {
        if (markUp.equals("markUp")) {
            try {
                File file = new File("C:\\Users\\Lenovo\\IdeaProjects\\Bachh_Dictionary\\src\\Dictionary\\DictionaryMark.txt");
                FileWriter fw = new FileWriter(file);

                for (int i = 0; i < dic.getListWord().size(); i++) {
                    fw.write(dic.getListWord().get(i).getWord_target() + ":" + dic.getListWord().get(i).getWord_explain() + "\n");
                }
                fw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}