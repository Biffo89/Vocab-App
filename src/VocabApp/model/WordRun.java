package VocabApp.model;

import VocabApp.Main;
import com.sun.istack.internal.NotNull;

import java.util.NoSuchElementException;

public class WordRun {

    private Word[] data;

    public WordRun() {
        data = new Word[10];
    }

    public boolean isEmpty() {
        boolean isEmpty = true;
        for (int i = 0; i < this.data.length; i++)
            if (this.data[i] != null) {isEmpty = false; break;}
        return isEmpty;
    }

    public boolean isFull() {
        boolean isFull = true;
        for (int i = 0; i < this.data.length; i++)
            if (this.data[i] == null) {isFull = false; break;}
        return isFull;
    }

    public Word get() {
        int index = Main.random.nextInt(5);
        return data[index];
    }

    public void sendToBack(@NotNull Word word) {
        if (word == null) throw new IllegalArgumentException();
        // find item
        int index = -1;
        for (int i = 0; i < this.data.length; i++)
            if (this.data[i] != null && this.data[i].equals(word)) {index = i; break;}
        if (index == -1) throw new NoSuchElementException();
        // shift left
        for (int i = index; i < this.data.length-1; i++) {
            this.data[i] = this.data[i+1];
        }
        this.data[this.data.length-1] = null;
        // place at back
        for (int i = 0; i < this.data.length; i++) {
            if (this.data[i] == null) {
                this.data[this.data.length - 1] = word;
                break;
            }
        }
    }

    public void remove(@NotNull Word word) {
        if (word == null) throw new IllegalArgumentException();
        // find item
        int index = -1;
        for (int i = 0; i < this.data.length; i++)
            if (this.data[i] != null && this.data[i].equals(word)) {index = i; break;}
        if (index == -1) throw new NoSuchElementException();
        // shift left
        for (int i = index; i < this.data.length-1; i++) {
            this.data[i] = this.data[i+1];
        }
        // place null;
        this.data[this.data.length-1] = null;
    }

    public boolean contains(@NotNull Word word) {
        if (word == null) throw new IllegalArgumentException();
        int index = -1;
        for (int i = 0; i < this.data.length; i++) {
            if (this.data[i] != null && this.data[i].equals(word)){
                index = i;
                break;
            }
        }
        if (index == -1) return false;
        return true;
    }

    public boolean add(@NotNull Word word) {
        if (word == null) throw new IllegalArgumentException();
        if (this.contains(word)) return false;
        for (int i = 0; i < this.data.length; i++)
            if (this.data[i] == null) {
                this.data[i] = word;
                return true;
            }
        return false;
    }

    public void resetTimeRight() {
        for (int i = 0; i < this.data.length; i++) {
            if (this.data[i] != null)
                this.data[i].resetTimesRight();
        }
    }

}
