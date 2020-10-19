package TranslateServices;

import Dictionary.DictionaryManagement;

import java.util.concurrent.Callable;

public class TranslateOffline extends Thread implements Callable<String> {

    private final String Word;
    private  String result = "";
    private final DictionaryManagement manage;

    public TranslateOffline(String Word,DictionaryManagement manage) {
        this.Word = Word;
        this.manage = manage;
    }

    public String getResult() {
        return result;
    }

    @Override
    public void run() {
        if (manage.dictionaryLookup(Word) >= 0){
            this.result = manage.getDic().getListWord().get(manage.dictionaryLookup(Word)).getWord_explain();
        }
        else {
            this.result = manage.Search(Word);
        }
//        System.out.println(result);
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public String call() throws Exception {
        return result;
    }
}
