package VocabApp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

public class Topic extends TreeSet<Word> {

    private String name;
    public final String baseLanguage;
    public final String targetLanguage;

    public Topic(String name, String baseLanguage, String targetLanguage) {
        super((o1, o2) -> o1.compareTo(o2));
        setName(name);
        this.baseLanguage = baseLanguage;
        this.targetLanguage = targetLanguage;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (!(o instanceof Topic)) return false;
        if (((Topic) o).getName().equals(this.getName()) && ((Topic) o).baseLanguage.equals(this.baseLanguage) && ((Topic) o).targetLanguage.equals(this.targetLanguage)) return true;
        return false;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String s) {
        this.name = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') this.name += '_';
            else this.name = this.name + s.charAt(i);
        }
    }

    public String nameAsString() {
        String s = "";
        for (int i = 0; i < this.name.length(); i++) {
            if (this.name.charAt(i) == '_') s += ' ';
            else s = s + this.name.charAt(i);
        }
        return s;
    }

    public int getScore() {
        int score = 0;
        for (Word word : this) {
            score += word.getScore();
        }
        return score;
    }

    public int getPotentialScore() {
        int score = 0;
        for (Word word : this) {
            score += word.getPotentialScore();
        }
        return score;
    }

    public int percentageDone() {
        return (int) (100*((double) getScore())/((double) getPotentialScore()));
    }

}
