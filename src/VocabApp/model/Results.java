package VocabApp.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class Results {

    private ArrayList<Result> results;

    public Results() {
        this.results = new ArrayList<Result>();
    }

    public void getRight(Word word) {
        if (!this.contains(word))
            this.results.add(new Result(word));
        for (Result result : this.results)
            if (result.word.equals(word)) {
                result.timesRight++;
                break;
            }
    }

    public void getWrong(Word word) {
        if (!this.contains(word))
            this.results.add(new Result(word));
        for (Result result : this.results)
            if (result.word.equals(word)) {
                result.timesWrong++;
                break;
            }
    }

    public double percentageRight() {
        double num = 0;
        double denom = 0;
        for (Result result : this.results) {
            num += result.timesRight;
            denom += result.timesWrong + result.timesRight;
        }
        return num / denom;
    }

    public double percentageRight(Word word) {
        for (Result result : this.results)
            if (result.word.equals(word))
                return result.percentageRight();
        throw new NoSuchElementException();
    }

    public ArrayList<Word> toWords() {
        this.results.sort(Comparator.naturalOrder());
        ArrayList<Word> output = new ArrayList<>();
        for (Result result : results)
            output.add(result.word);
        return output;
    }

    private boolean contains(Word word) {
        for (Result result : this.results)
            if (result.word.equals(word))
                return true;
        return false;
    }

    private class Result implements Comparable<Result> {

        private Word word;
        private int timesRight;
        private int timesWrong;

        private Result(Word word) {
            this.word = word;
            this.timesRight = 0;
            this.timesWrong = 0;
        }

        public int compareTo(Result result) {
            if (this.timesRight * (result.timesWrong + result.timesRight) < result.timesRight * (this.timesWrong + this.timesRight)) return -1;
            if (this.timesRight * (result.timesWrong + result.timesRight) > result.timesRight * (this.timesWrong + this.timesRight)) return 1;
            if (this.timesRight == 0 && result.timesRight == 0) {
                if (this.timesWrong > result.timesWrong) return -1;
                if (this.timesWrong < result.timesWrong) return 1;
            }
            if (this.timesWrong == 0 && result.timesWrong == 0) {
                if (this.timesRight < result.timesRight) return -1;
                if (this.timesRight > result.timesRight) return 1;
            }
            return word.compareTo(result.word);
        }

        private double percentageRight() {
            return (double) (this.timesRight) / (double) (this.timesWrong + this.timesRight);
        }

    }

}
