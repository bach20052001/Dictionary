package Dictionary;

public class Word implements Comparable<Word> {
    private String word_target;
    private String word_explain;
    private boolean mark = false;

    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    public Word() {
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    public String getWord_explain() {
        return word_explain;
    }

    public String getWord_target() {
        return word_target;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public Word(String lineInFile) {
        String[] arr = lineInFile.split("\\t");
        if (arr.length > 1) {
            this.word_target = arr[0];
            this.word_explain = arr[1];
        } else if (arr.length == 1) {
            word_target = arr[0];
        } else {
            word_target = "";
            word_explain = "";
        }
    }


    @Override
    public int compareTo(Word w) {
        return this.word_target.compareToIgnoreCase(w.word_target);
    }
}