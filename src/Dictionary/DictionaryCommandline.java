package Dictionary;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class DictionaryCommandline {

    private DictionaryManagement manage = new DictionaryManagement();

    public DictionaryManagement getManage() {
        return manage;
    }

    public void showAllWords() {
        System.out.printf("%-6s%c %-15s%c %-20s%n", "No", '|', "English", '|', "Vietnamese");
        for (int i = 0; i < manage.getDic().getListWord().size(); i++) {
            System.out.printf("%-6d%c %-15s%c %-15s%n", i + 1, '|', manage.getDic().getWord(i).getWord_target(), '|', manage.getDic().getWord(i).getWord_explain());
        }
    }

    public void dictionaryBasic() {
        Dictionary dic = new Dictionary();
        manage.insertFromCommandline();
        manage.dictionaryExportToFile();
        System.out.println("DICTIONARY: ");
        showAllWords();
    }

    public void dictionaryAdvanced() throws FileNotFoundException {
        Dictionary dic = new Dictionary();
        manage.insertFromFile();
        System.out.println("DICTIONARY: ");
        showAllWords();
        manage.dictionaryExportToFile();
        manage.addWord();
        showAllWords();
        manage.dictionaryLookup();
        dictionarySearcher();
    }

    public void dictionarySearcher() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter prefix: ");
        String prefix = sc.nextLine();
        prefix = prefix.toLowerCase();
        int start = Collections.binarySearch(manage.getDic().getListWord(), new Word(prefix,null));
        if(start < 0) {
            start = -start - 1;
        }
        int end = start;
        while(end < manage.getDic().getListWord().size() && manage.getDic().getListWord().get(end).getWord_target().startsWith(prefix)) {
            end++;
        }
        if(start != end) {
            List<Word> prefixWords = manage.getDic().getListWord().subList(start, end);
            System.out.printf("%-6s%c %-20s%c %-20s%n","No", '|' ,"English", '|', "Vietnamese");
            for(int i=0; i<prefixWords.size(); i++) {
                System.out.printf("%-6d%c %-20s%c %-20s%n",i+1,'|',prefixWords.get(i).getWord_target(),'|',prefixWords.get(i).getWord_explain());
            }
        }
        else {
            System.out.println("No matching prefix word!");
        }
    }

}
