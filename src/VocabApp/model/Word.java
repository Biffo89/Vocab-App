package VocabApp.model;

import VocabApp.Main;
import VocabApp.util.Util;
import javafx.scene.image.Image;

import javax.json.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.function.Function;

public class Word {

    public boolean isSameAs(Word word) {
        return this.getBaseLanguageDisplayWord().equals(word.getBaseLanguageDisplayWord()) && this.getTargetLanguageDisplayWord().equals(word.getTargetLanguageDisplayWord());
    }

    public static final int TARGET_TO_BASE_WRITTEN = 0;
    public static final int BASE_TO_TARGET_WRITTEN = 1;
    public static final int TARGET_TO_TARGET_SPOKEN = 2;
    public static final int TARGET_TO_BASE_SPOKEN = 3;
    private String baseLanguage;
    private String targetLanguage;
    private String baseLanguageDisplayWord;
    private String targetLanguageDisplayWord;
    private ArrayList<String> baseLanguageAnswers;
    private ArrayList<String> targetLanguageAnswers;
    private ArrayList<String> spokenAnswers;
    private String audioData;
    private int[] starsEarnt; // an array containing a value for each learning mode
    private int[] timesRightInARow;
    private int[] timesWrongInARow;
    private GregorianCalendar[] lastTimeStarEarnt;
    private GregorianCalendar[] lastTimePractised;

    public Word(String baseLanguage, String targetLanguage, String baseLanguageDisplayWord, String targetLanguageDisplayWord, ArrayList<String> baseLanguageAnswers, ArrayList<String> targetLanguageAnswers, ArrayList<String> spokenAnswers, String audioData, int[] starsEarnt, int[] timesRightInARow, int[] timesWrongInARow, GregorianCalendar[] lastTimeStarEarnt, GregorianCalendar[] lastTimePractised) {
        if (!Main.SUPPORTED_LANGUAGES.contains(baseLanguage)) throw new IllegalArgumentException("Language not supported.");
        if (!Main.SUPPORTED_LANGUAGES.contains(targetLanguage)) throw new IllegalArgumentException("Language not supported.");
        if (baseLanguageDisplayWord == null) throw new IllegalArgumentException();
        if (targetLanguageDisplayWord == null) throw new IllegalArgumentException();
        if (baseLanguageAnswers == null || baseLanguageAnswers.isEmpty()) throw new IllegalArgumentException();
        if (targetLanguageAnswers == null || targetLanguageAnswers.isEmpty()) throw new IllegalArgumentException();
        if (spokenAnswers == null ^ audioData == null) throw new IllegalArgumentException();
        if (spokenAnswers != null && spokenAnswers.size() == 0) throw new IllegalArgumentException();
        if (starsEarnt == null || starsEarnt.length != 4) throw new IllegalArgumentException();
        if (timesRightInARow == null || timesRightInARow.length != 4) throw new IllegalArgumentException();
        if (timesWrongInARow == null || timesWrongInARow.length != 4) throw new IllegalArgumentException();
        if (lastTimeStarEarnt == null || lastTimeStarEarnt.length != 4) throw new IllegalArgumentException();
        if (lastTimePractised == null || lastTimePractised.length != 4) throw new IllegalArgumentException();
        this.baseLanguage = baseLanguage;
        this.targetLanguage = targetLanguage;
        this.baseLanguageDisplayWord = baseLanguageDisplayWord;
        this.targetLanguageDisplayWord = targetLanguageDisplayWord;
        this.baseLanguageAnswers = baseLanguageAnswers;
        this.targetLanguageAnswers = targetLanguageAnswers;
        this.spokenAnswers = spokenAnswers;
        this.audioData = audioData;
        this.starsEarnt = starsEarnt;
        this.timesRightInARow = timesRightInARow;
        this.timesWrongInARow = timesWrongInARow;
        this.lastTimeStarEarnt = lastTimeStarEarnt;
        this.lastTimePractised = lastTimePractised;
        for (int i = 0; i < 4; i++) {
            if (this.lastTimeStarEarnt[i] == null) this.lastTimeStarEarnt[i] = new GregorianCalendar();
            if (this.lastTimePractised[i] == null) this.lastTimePractised[i] = new GregorianCalendar();
        }
    }

    /*private Word(WordParser wordParser) {
        this(wordParser.getBaseLanguage(),wordParser.getTargetLanguage(),wordParser.getBaseLanguageDisplayWord(),wordParser.getTargetLanguageDisplayWord(),wordParser.getBaseLanguageAnswers(),wordParser.getTargetLanguageAnswers(),wordParser.getSpokenAnswers(),wordParser.getAudioData(),wordParser.getStarsEarnt(),wordParser.getTimesRightInARow(),wordParser.getTimesWrongInARow(),wordParser.getLastTimeStarEarnt(),wordParser.getLastTimePractised());
    }*/

    private Word(JSONParser wordParser) {
        this(wordParser.getBaseLanguage(),wordParser.getTargetLanguage(),wordParser.getBaseLanguageDisplayWord(),wordParser.getTargetLanguageDisplayWord(),wordParser.getBaseLanguageAnswers(),wordParser.getTargetLanguageAnswers(),wordParser.getSpokenAnswers(),wordParser.getAudioData(),wordParser.getStarsEarnt(),wordParser.getTimesRightInARow(),wordParser.getTimesWrongInARow(),wordParser.getLastTimeStarEarnt(),wordParser.getLastTimePractised());
    }

    public Word(String wordAsString){
        this(new JSONParser(wordAsString));
    }

    public String getBaseLanguage() {
        return baseLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public String getBaseLanguageDisplayWord() {
        return baseLanguageDisplayWord;
    }

    public void setBaseLanguageDisplayWord(String baseLanguageDisplayWord) {
        this.baseLanguageDisplayWord = baseLanguageDisplayWord;
    }

    public String getTargetLanguageDisplayWord() {
        return targetLanguageDisplayWord;
    }

    public void setTargetLanguageDisplayWord(String targetLanguageDisplayWord) {
        this.targetLanguageDisplayWord = targetLanguageDisplayWord;
    }

    public ArrayList<String> getBaseLanguageAnswers() {
        return baseLanguageAnswers;
    }

    public void setBaseLanguageAnswers(ArrayList<String> baseLanguageAnswers) {
        this.baseLanguageAnswers = baseLanguageAnswers;
    }

    public ArrayList<String> getTargetLanguageAnswers() {
        return targetLanguageAnswers;
    }

    public void setTargetLanguageAnswers(ArrayList<String> targetLanguageAnswers) {
        this.targetLanguageAnswers = targetLanguageAnswers;
    }

    public ArrayList<String> getSpokenAnswers() {
        return spokenAnswers;
    }

    public void setSpokenAnswers(ArrayList<String> spokenAnswers) {
        this.spokenAnswers = spokenAnswers;
    }

    public int getScore() {
        return starsEarnt[0] +
               starsEarnt[1] +
               starsEarnt[2] +
               starsEarnt[3];
    }

    public int getPotentialScore() {
        return getScore() +
               (isLearnable(Word.TARGET_TO_BASE_WRITTEN) ? 1 : 0) +
               (isLearnable(Word.BASE_TO_TARGET_WRITTEN) ? 1 : 0) +
               (isLearnable(Word.TARGET_TO_TARGET_SPOKEN) ? 1 : 0) +
               (isLearnable(Word.TARGET_TO_BASE_SPOKEN) ? 1 : 0);
    }

    public int getScore(int learningMode) {
        return starsEarnt[learningMode];
    }

    public int getPotentialScore(int learningMode) {
        return getScore(learningMode) + (isLearnable(learningMode) ? 1 : 0);
    }

    public Image getBlackStar(int learningMode) {
        int stars = this.starsEarnt[learningMode];
        boolean learnable = this.isLearnable(learningMode);
        String filename = "VocabApp/view/stars/" + (learnable ? "bd" : "bs") + stars + ".png";
        return Util.scaleHeight(new Image(filename),20,true);
    }

    /**
     * Returns true if the int corresponds to a learning mode (see fields).
     * Returns false otherwise.
     */
    public static boolean isValidLearningMode(int learningMode) {
        switch (learningMode) {
            case TARGET_TO_BASE_WRITTEN: return true;
            case BASE_TO_TARGET_WRITTEN: return true;
            case TARGET_TO_TARGET_SPOKEN: return true;
            case TARGET_TO_BASE_SPOKEN: return true;
            default: return false;
        }
    }

    /**
     * Return true iff the answer given was a match with one of the acceptable answers.
     */
    public boolean isCorrectAnswer(int learningMode, String answer) {
        if (learningMode == TARGET_TO_BASE_WRITTEN || learningMode == TARGET_TO_BASE_SPOKEN) {
            for (String baseLanguageAnswer : this.baseLanguageAnswers) {
                if (baseLanguageAnswer.equalsIgnoreCase(answer)) {
                    return true;
                }
            }
            return false;
        } else if (learningMode == BASE_TO_TARGET_WRITTEN) {
            for (String targetLanguageAnswer : this.targetLanguageAnswers) {
                if (targetLanguageAnswer.equalsIgnoreCase(answer)) {
                    return true;
                }
            }
            return false;
        } else if (learningMode == TARGET_TO_TARGET_SPOKEN) {
            for (String spokenAnswer : this.spokenAnswers) {
                if (spokenAnswer.equalsIgnoreCase(answer)) {
                    return true;
                }
            }
            return false;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static char charRemoveAccent(char c) {
        int value = (int) c;
        if ((65 <= value && value <= 90) || (97 <= value && value <= 122)) return c;
        switch(c) {
            case 'À':
            case 'Á':
            case 'Â':
            case 'Ã':
            case 'Ä':
            case 'Å':
            case 'Ā':
            case 'Ă':
            case 'Ą':
                return 'A';
            case 'Ç':
            case 'Ć':
            case 'Ĉ':
            case 'Ċ':
            case 'Č':
                return 'C';
            case 'Ď':
            case 'Đ':
                return 'D';
            case 'È':
            case 'É':
            case 'Ê':
            case 'Ë':
            case 'Ē':
            case 'Ĕ':
            case 'Ė':
            case 'Ę':
            case 'Ě':
                return 'E';
            case 'Ĝ':
            case 'Ğ':
            case 'Ġ':
            case 'Ģ':
                return 'G';
            case 'Ĥ':
            case 'Ħ':
                return 'H';
            case 'Ì':
            case 'Í':
            case 'Î':
            case 'Ï':
            case 'Ĩ':
            case 'Ī':
            case 'Ĭ':
            case 'Į':
            case 'İ':
                return 'I';
            case 'Ĵ':
                return 'J';
            case 'Ķ':
                return 'K';
            case 'Ĺ':
            case 'Ļ':
            case 'Ľ':
            case 'Ŀ':
            case 'Ł':
                return 'L';
            case 'Ñ':
            case 'Ń':
            case 'Ņ':
            case 'Ň':
            case 'Ŋ':
                return 'N';
            case 'Ò':
            case 'Ó':
            case 'Ô':
            case 'Õ':
            case 'Ö':
            case 'Ø':
            case 'Ō':
            case 'Ŏ':
            case 'Ő':
                return 'O';
            case 'Ŕ':
            case 'Ŗ':
            case 'Ř':
                return 'R';
            case 'Ś':
            case 'Ŝ':
            case 'Ş':
            case 'Š':
                return 'S';
            case 'Ţ':
            case 'Ť':
            case 'Ŧ':
                return 'T';
            case 'Ù':
            case 'Ú':
            case 'Û':
            case 'Ü':
            case 'Ũ':
            case 'Ū':
            case 'Ŭ':
            case 'Ů':
            case 'Ű':
            case 'Ų':
                return 'U';
            case 'Ŵ':
                return 'W';
            case 'Ý':
            case 'Ŷ':
            case 'Ÿ':
                return 'Y';
            case 'Ź':
            case 'Ż':
            case 'Ž':
                return 'Z';
            case 'à':
            case 'á':
            case 'â':
            case 'ã':
            case 'ä':
            case 'å':
            case 'ā':
            case 'ă':
            case 'ą':
                return 'a';
            case 'ç':
            case 'ć':
            case 'ĉ':
            case 'ċ':
            case 'č':
                return 'c';
            case 'ď':
            case 'đ':
                return 'd';
            case 'è':
            case 'é':
            case 'ê':
            case 'ë':
            case 'ē':
            case 'ĕ':
            case 'ė':
            case 'ę':
            case 'ě':
                return 'e';
            case 'ĝ':
            case 'ğ':
            case 'ġ':
            case 'ģ':
                return 'g';
            case 'ĥ':
            case 'ħ':
                return 'h';
            case 'ì':
            case 'í':
            case 'î':
            case 'ï':
            case 'ĩ':
            case 'ī':
            case 'ĭ':
            case 'į':
            case 'ı':
                return 'i';
            case 'ĵ':
                return 'j';
            case 'ķ':
                return 'k';
            case 'ĺ':
            case 'ļ':
            case 'ľ':
            case 'ŀ':
            case 'ł':
                return 'l';
            case 'ñ':
            case 'ń':
            case 'ņ':
            case 'ň':
            case 'ŉ':
            case 'ŋ':
                return 'n';
            case 'ò':
            case 'ó':
            case 'ô':
            case 'õ':
            case 'ö':
            case 'ø':
            case 'ō':
            case 'ŏ':
            case 'ő':
                return 'o';
            case 'ŕ':
            case 'ŗ':
            case 'ř':
                return 'r';
            case 'ś':
            case 'ŝ':
            case 'ş':
            case 'š':
                return 's';
            case 'ţ':
            case 'ť':
            case 'ŧ':
                return 't';
            case 'ù':
            case 'ú':
            case 'û':
            case 'ü':
            case 'ũ':
            case 'ū':
            case 'ŭ':
            case 'ů':
            case 'ű':
            case 'ų':
                return 'u';
            case 'ŵ':
                return 'w';
            case 'ý':
            case 'ÿ':
            case 'ŷ':
                return 'y';
            case 'ź':
            case 'ż':
            case 'ž':
                return 'z';
            default:
                return c;
        }
    }

    private static boolean charEqualsIgnoreAccentAndCase(char a, char b) {
        if (a == b) return true;
        if (charRemoveAccent(a) == charRemoveAccent(b)) return true;
        if (Character.toUpperCase(charRemoveAccent(a)) == Character.toUpperCase(charRemoveAccent(b))) return true;
        if (Character.toLowerCase(charRemoveAccent(a)) == Character.toLowerCase(charRemoveAccent(b))) return true;
        if (charRemoveAccent(a) == charRemoveAccent(b)) return true;
        return false;
    }

    private static boolean charEqualsIgnoreCase(char a, char b) {
        if (a == b) return true;
        if (Character.toUpperCase(a) == Character.toUpperCase(b)) return true;
        if (Character.toLowerCase(a) == Character.toLowerCase(b)) return true;
        return false;
    }

    /**
     * Returns string distance of two strings.
     * Contains the 2D array used for memoisation.
     * Distance is minimum as follows:
     *      cost 1 for insertion
     *      cost 1 for deletion
     *      cost 1 for substitution (cost 0.5 if only an accent differs between them)
     *      cost 1 for transposition of two adjacent characters
     * Based on the Damerau-Levenshtein metric, with 0.5 cost for accent difference on same letter.
     */
    public static int stringDistance(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";
        int[][] memoDist = new int[a.length()+1][b.length()+1];
        for (int i = 0; i <= a.length(); i++) for (int j = 0; j <= b.length(); j++) memoDist[i][j] = -1;
        return memoStringDistance(a,b,memoDist)/2;
    }

    /**
     * Calculates the actual string distance, memoising the result.
     */
    private static int memoStringDistance(String a, String b, int[][] memo) {
        if (memo[a.length()][b.length()] == -1) {
            int ans;
            if (Math.min(a.length(), b.length()) == 0)
                ans =
                        2 * Math.max(a.length(), b.length());
            else if (a.length() > 1 && b.length() > 1 && charEqualsIgnoreCase(a.charAt(a.length() - 1),b.charAt(b.length() - 2)) && charEqualsIgnoreCase(a.charAt(a.length() - 2), b.charAt(b.length() - 1)))
                ans =
                        Math.min(memoStringDistance(a.substring(0, a.length() - 1), b, memo) + 2,
                                Math.min(memoStringDistance(a, b.substring(0, b.length() - 1), memo) + 2,
                                        Math.min(memoStringDistance(a.substring(0, a.length() - 1), b.substring(0, b.length() - 1), memo) + (charEqualsIgnoreCase(a.charAt(a.length() - 1),b.charAt(b.length() - 1)) ? 0 : (charEqualsIgnoreAccentAndCase(a.charAt(a.length() - 1),b.charAt(b.length() - 1)) ? 1 : 2)),
                                                memoStringDistance(a.substring(0, a.length() - 2), b.substring(0, b.length() - 2), memo) + 2)));
            else if (a.length() > 1 && b.length() > 1 && charEqualsIgnoreAccentAndCase(a.charAt(a.length() - 1),b.charAt(b.length() - 2)) && charEqualsIgnoreCase(a.charAt(a.length() - 2), b.charAt(b.length() - 1)))
                ans =
                        Math.min(memoStringDistance(a.substring(0, a.length() - 1), b, memo) + 2,
                                Math.min(memoStringDistance(a, b.substring(0, b.length() - 1), memo) + 2,
                                        Math.min(memoStringDistance(a.substring(0, a.length() - 1), b.substring(0, b.length() - 1), memo) + (charEqualsIgnoreCase(a.charAt(a.length() - 1),b.charAt(b.length() - 1)) ? 0 : (charEqualsIgnoreAccentAndCase(a.charAt(a.length() - 1),b.charAt(b.length() - 1)) ? 1 : 2)),
                                                memoStringDistance(a.substring(0, a.length() - 2), b.substring(0, b.length() - 2), memo) + 3)));
            else if (a.length() > 1 && b.length() > 1 && charEqualsIgnoreCase(a.charAt(a.length() - 1),b.charAt(b.length() - 2)) && charEqualsIgnoreAccentAndCase(a.charAt(a.length() - 2), b.charAt(b.length() - 1)))
                ans =
                        Math.min(memoStringDistance(a.substring(0, a.length() - 1), b, memo) + 2,
                                Math.min(memoStringDistance(a, b.substring(0, b.length() - 1), memo) + 2,
                                        Math.min(memoStringDistance(a.substring(0, a.length() - 1), b.substring(0, b.length() - 1), memo) + (charEqualsIgnoreCase(a.charAt(a.length() - 1),b.charAt(b.length() - 1)) ? 0 : (charEqualsIgnoreAccentAndCase(a.charAt(a.length() - 1),b.charAt(b.length() - 1)) ? 1 : 2)),
                                                memoStringDistance(a.substring(0, a.length() - 2), b.substring(0, b.length() - 2), memo) + 3)));
            else if (a.length() > 1 && b.length() > 1 && charEqualsIgnoreAccentAndCase(a.charAt(a.length() - 1),b.charAt(b.length() - 2)) && charEqualsIgnoreAccentAndCase(a.charAt(a.length() - 2), b.charAt(b.length() - 1)))
                ans =
                        Math.min(memoStringDistance(a.substring(0, a.length() - 1), b, memo) + 2,
                                Math.min(memoStringDistance(a, b.substring(0, b.length() - 1), memo) + 2,
                                        Math.min(memoStringDistance(a.substring(0, a.length() - 1), b.substring(0, b.length() - 1), memo) + (charEqualsIgnoreCase(a.charAt(a.length() - 1),b.charAt(b.length() - 1)) ? 0 : (charEqualsIgnoreAccentAndCase(a.charAt(a.length() - 1),b.charAt(b.length() - 1)) ? 1 : 2)),
                                                memoStringDistance(a.substring(0, a.length() - 2), b.substring(0, b.length() - 2), memo) + 4)));
            else ans =
                        Math.min(memoStringDistance(a.substring(0, a.length() - 1), b, memo) + 2,
                                Math.min(memoStringDistance(a, b.substring(0, b.length() - 1), memo) + 2,
                                        memoStringDistance(a.substring(0, a.length() - 1), b.substring(0, b.length() - 1), memo) + (charEqualsIgnoreCase(a.charAt(a.length() - 1),b.charAt(b.length() - 1)) ? 0 : (charEqualsIgnoreAccentAndCase(a.charAt(a.length() - 1),b.charAt(b.length() - 1)) ? 1 : 2))));
            memo[a.length()][b.length()] = ans;
        }
        return memo[a.length()][b.length()];
    }

    /**
     * Returns true if the answer was potentially misspelt.
     */
    public boolean isMisspeltAnswer(int learningMode, String answer) {
        int STRICTNESS = 4;
        if (learningMode == TARGET_TO_BASE_WRITTEN || learningMode == TARGET_TO_BASE_SPOKEN) {
            for (String baseLanguageAnswer : this.baseLanguageAnswers) {
                if (baseLanguageAnswer.equalsIgnoreCase(answer)) return true;
                if (STRICTNESS*stringDistance(baseLanguageAnswer,answer) <= baseLanguageAnswer.length()) return true;
            }
            return false;
        } else if (learningMode == BASE_TO_TARGET_WRITTEN) {
            for (String targetLanguageAnswer : this.targetLanguageAnswers) {
                if (targetLanguageAnswer.equalsIgnoreCase(answer)) return true;
                if (STRICTNESS*stringDistance(targetLanguageAnswer,answer) <= targetLanguageAnswer.length()) return true;
            }
            return false;
        } else if (learningMode == TARGET_TO_TARGET_SPOKEN) {
            for (String spokenAnswer : this.spokenAnswers) {
                if (spokenAnswer.equalsIgnoreCase(answer)) return true;
                if (STRICTNESS*stringDistance(spokenAnswer,answer) <= spokenAnswer.length()) return true;
            }
            return false;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns true if a star is available to be learnt.
     * Returns false if no more stars can be learnt currently.
     *
     *  - The 1st star can be learnt at any time.
     *  - The 2nd star can be learnt 1 day after the 1st.
     *  - The 3rd star can be learnt 1 week after the 2nd.
     *  - The 4th star can be learnt 1 month after the 3rd.
     *  - The 5th star can be learnt 3 months after the 4th.
     */
    public boolean isLearnable(int learningMode) {
        if (!isValidLearningMode(learningMode)) throw new IllegalArgumentException();
        if ((learningMode == Word.TARGET_TO_TARGET_SPOKEN || learningMode == Word.TARGET_TO_BASE_SPOKEN) && this.audioData == null) return false;
        GregorianCalendar currentTime = new GregorianCalendar();
        switch (this.starsEarnt[learningMode]) {
            case 0: return true;
            case 1: {
                GregorianCalendar dayLater = (GregorianCalendar) this.lastTimeStarEarnt[learningMode].clone();
                dayLater.add(GregorianCalendar.DAY_OF_MONTH, 1);
                return (dayLater.compareTo(currentTime) <= 0);
            }
            case 2: {
                GregorianCalendar weekLater = (GregorianCalendar) this.lastTimeStarEarnt[learningMode].clone();
                weekLater.add(GregorianCalendar.DAY_OF_MONTH, 7);
                return (weekLater.compareTo(currentTime) <= 0);
            }
            case 3: {
                GregorianCalendar monthLater = (GregorianCalendar) this.lastTimeStarEarnt[learningMode].clone();
                monthLater.add(GregorianCalendar.MONTH, 1);
                return (monthLater.compareTo(currentTime) <= 0);
            }
            case 4: {
                GregorianCalendar threeMonthsLater = (GregorianCalendar) this.lastTimeStarEarnt[learningMode].clone();
                threeMonthsLater.add(GregorianCalendar.MONTH,3);
                return (threeMonthsLater.compareTo(currentTime) <= 0);
            }
            case 5: return false;
            default: throw new AssertionError("Number of stars for word \"" + this.baseLanguageDisplayWord + ", " + this.targetLanguageDisplayWord + "\" was not between 0 and 5. Please report this bug.");
        }
    }

    /**
     * Method called when exiting a lesson prematurely.
     * Resets the number of times right in a row to 0.
     * Ensures no cheating by opening a list of words repeatedly, doing one word, then quitting.
     */
    public void resetTimesRight() {
        for (int i = 0; i < this.timesRightInARow.length; i++) {
            this.timesRightInARow[i] = 0;
        }
    }

    /**
     * Method called when a correct answer has been given for the word.
     */
    public void success(int learningMode) {
        if (!isValidLearningMode(learningMode)) throw new IllegalArgumentException();
        this.timesWrongInARow[learningMode] = 0;
        this.lastTimePractised[learningMode] = new GregorianCalendar();
        if (this.isLearnable(learningMode)) {
            this.timesRightInARow[learningMode]++;
            if (this.timesRightInARow[learningMode] == 3) this.addStar(learningMode);
        }
    }

    /**
     * Method called when an incorrect answer has been given for the word.
     */
    public void failure(int learningMode) {
        if (!isValidLearningMode(learningMode)) throw new IllegalArgumentException();
        this.timesRightInARow[learningMode] = 0;
        if (starsEarnt[learningMode] == 0) return;
        this.timesWrongInARow[learningMode]++;
        if (this.timesWrongInARow[learningMode] == 3) resetStars(learningMode);
    }

    /**
     * Method called when three correct answers have been given in a row, and a star can be earnt.
     */
    private void addStar(int learningMode) {
        if (!isValidLearningMode(learningMode)) throw new IllegalArgumentException();
        this.starsEarnt[learningMode]++;
        this.lastTimeStarEarnt[learningMode] = new GregorianCalendar();
        this.timesRightInARow[learningMode] = 0;
        Main.playStarSound();
    }

    /**
     * Method called when three incorrect answers have been given in a row.
     * Resets number of stars to 0.
     */
    private void resetStars(int learningMode) {
        if (!isValidLearningMode(learningMode)) throw new IllegalArgumentException();
        this.starsEarnt[learningMode] = 0;
        this.timesWrongInARow[learningMode] = 0;
    }

    /**
     * If the word has not been practised in six months, then a star is removed, providing the number of stars is greater than 0.
     */
    public void updateStarsFromDate(int learningMode) {
        if (!isValidLearningMode(learningMode)) throw new IllegalArgumentException();
        if (this.starsEarnt[learningMode] == 0) return;
        GregorianCalendar currentTime = new GregorianCalendar();
        GregorianCalendar sixMonthsLater = (GregorianCalendar) this.lastTimePractised[learningMode].clone();
        sixMonthsLater.add(GregorianCalendar.MONTH,6);
        if (sixMonthsLater.compareTo(currentTime) <= 0) {
            this.lastTimePractised[learningMode] = sixMonthsLater;
            this.starsEarnt[learningMode]--;
            this.timesRightInARow[learningMode] = 0;
            this.timesWrongInARow[learningMode] = 0;
            updateStarsFromDate(learningMode);
        }
    }

    public String timeAsString(GregorianCalendar calendar) {
        if (calendar == null) return "null";
        int year = calendar.get(GregorianCalendar.YEAR);
        int month = calendar.get(GregorianCalendar.MONTH) + 1;
        int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
        int hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
        int minute = calendar.get(GregorianCalendar.MINUTE);
        int second = calendar.get(GregorianCalendar.SECOND);
        return year+"/"+(month<10 ? "0" : "")+month+"/"+(day<10 ? "0" : "")+day+" "+(hour<10 ? "0" : "")+hour+":"+(minute<10 ? "0" : "")+minute+":"+(second<10 ? "0" : "")+second;
    }

    public String timeArrayAsString(GregorianCalendar[] calendar) {
        if (calendar == null) return null;
        String output = "[";
        for (int i = 0; i < calendar.length; i++) {
            output += timeAsString(calendar[i]) + ", ";
        }
        output = output.substring(0,output.length()-2);
        output += "]";
        return output;
    }

    public int compareTo(Word word) {
        if (this.getTargetLanguageDisplayWord().compareTo(word.getTargetLanguageDisplayWord()) != 0) return this.getTargetLanguageDisplayWord().compareTo(word.getTargetLanguageDisplayWord());
        return (this.exportAsString()).compareTo(word.exportAsString());
    }

    public int progressCompareTo(Word word, int learningMode) {
        int compare = (this.isLearnable(learningMode) ? 1 : 0) - (word.isLearnable(learningMode) ? 1 : 0);
        if (compare > 0) return 1;
        if (compare < 0) return -1;
        compare = this.starsEarnt[learningMode] - word.starsEarnt[learningMode];
        if (compare > 0) return 1;
        if (compare < 0) return -1;
        compare = this.targetLanguageDisplayWord.compareTo(word.getTargetLanguageDisplayWord());
        if (compare > 0) return 1;
        if (compare < 0) return -1;
        return 0;
    }

    @Override
    public String toString() {
        return "Word{" +
                "baseLanguage=" + (baseLanguage != null ? '\"' : "") + baseLanguage + (baseLanguage != null ? '\"' : "") +
                ", targetLanguage=" + (targetLanguage != null ? '\"' : "") + targetLanguage + (targetLanguage != null ? '\"' : "") +
                ", baseLanguageDisplayWord=" + (baseLanguageDisplayWord != null ? '\"' : "") + baseLanguageDisplayWord + (baseLanguageDisplayWord != null ? '\"' : "") +
                ", targetLanguageDisplayWord=" + (targetLanguageDisplayWord != null ? '\"' : "") + targetLanguageDisplayWord + (targetLanguageDisplayWord != null ? '\"' : "") +
                ", baseLanguageAnswers=" + baseLanguageAnswers +
                ", targetLanguageAnswers=" + targetLanguageAnswers +
                ", spokenAnswers=" + spokenAnswers +
                //", targetAudioLocation=" + (targetAudioLocation != null ? '\"' : "") + targetAudioLocation + (targetAudioLocation != null ? '\"' : "") +
                ", audioData=" + (audioData != null ? '\"' : "") + audioData + (audioData != null ? '\"' : "") +
                ", starsEarnt=" + Arrays.toString(starsEarnt) +
                ", timesRightInARow=" + Arrays.toString(timesRightInARow) +
                ", timesWrongInARow=" + Arrays.toString(timesWrongInARow) +
                ", lastTimeStarEarnt=" + timeArrayAsString(lastTimeStarEarnt) +
                ", lastTimePractised=" + timeArrayAsString(lastTimePractised) +
                '}';
    }

    public String exportAsString() {
        return "Word{" +
                "baseLanguage=" + (baseLanguage != null ? '\"' : "") + baseLanguage + (baseLanguage != null ? '\"' : "") +
                ", targetLanguage=" + (targetLanguage != null ? '\"' : "") + targetLanguage + (targetLanguage != null ? '\"' : "") +
                ", baseLanguageDisplayWord=" + (baseLanguageDisplayWord != null ? '\"' : "") + baseLanguageDisplayWord + (baseLanguageDisplayWord != null ? '\"' : "") +
                ", targetLanguageDisplayWord=" + (targetLanguageDisplayWord != null ? '\"' : "") + targetLanguageDisplayWord + (targetLanguageDisplayWord != null ? '\"' : "") +
                ", baseLanguageAnswers=" + baseLanguageAnswers +
                ", targetLanguageAnswers=" + targetLanguageAnswers +
                ", spokenAnswer=" + spokenAnswers +
                ", audioData=" + (audioData != null ? '\"' : "") + audioData + (audioData != null ? '\"' : "") +
                '}';
    }

    public String getAudioData() {
        return audioData;
    }

    public void setAudioData(String audioData) {
        this.audioData = audioData;
    }

    /*private static class WordParser {
        private String wordAsString;
        private int pos;

        private boolean initialised = false;
        private String baseLanguage;
        private String targetLanguage;
        private String baseLanguageDisplayWord;
        private String targetLanguageDisplayWord;
        private ArrayList<String> baseLanguageAnswers;
        private ArrayList<String> targetLanguageAnswers;
        private ArrayList<String> spokenAnswers;
        private String audioData;
        private int[] starsEarnt;
        private int[] timesRightInARow;
        private int[] timesWrongInARow;
        private GregorianCalendar[] lastTimeStarEarnt;
        private GregorianCalendar[] lastTimePractised;

        public boolean isInitialised() {
            return initialised;
        }

        public String getBaseLanguage() {
            return baseLanguage;
        }

        public String getTargetLanguage() {
            return targetLanguage;
        }

        public String getBaseLanguageDisplayWord() {
            return baseLanguageDisplayWord;
        }

        public String getTargetLanguageDisplayWord() {
            return targetLanguageDisplayWord;
        }

        public ArrayList<String> getBaseLanguageAnswers() {
            return baseLanguageAnswers;
        }

        public ArrayList<String> getTargetLanguageAnswers() {
            return targetLanguageAnswers;
        }

        public ArrayList<String> getSpokenAnswers() {
            return spokenAnswers;
        }

        public String getAudioData() {
            return audioData;
        }

        public int[] getStarsEarnt() {
            if (!this.isInitialised()) throw new IllegalArgumentException();
            return starsEarnt;
        }

        public int[] getTimesRightInARow() {
            return timesRightInARow;
        }

        public int[] getTimesWrongInARow() {
            return timesWrongInARow;
        }

        public GregorianCalendar[] getLastTimeStarEarnt() {
            return lastTimeStarEarnt;
        }

        public GregorianCalendar[] getLastTimePractised() {
            return lastTimePractised;
        }

        public WordParser(String wordAsString) {
            this.wordAsString = wordAsString;
            this.pos = 0;
            startBraces();
            assertString("baseLanguage=");
            this.baseLanguage = readString();
            assertString(", targetLanguage=");
            this.targetLanguage = readString();
            assertString(", baseLanguageDisplayWord=");
            this.baseLanguageDisplayWord = readString();
            assertString(", targetLanguageDisplayWord=");
            this.targetLanguageDisplayWord = readString();
            assertString(", baseLanguageAnswers=");
            this.baseLanguageAnswers = readStringArrayList();
            assertString(", targetLanguageAnswers=");
            this.targetLanguageAnswers = readStringArrayList();
            assertString(", spokenAnswers=");
            this.spokenAnswers = readStringArrayList();
            assertString(", audioData=");
            this.audioData = readString();
            assertString(", starsEarnt=");
            this.starsEarnt = readIntArray(4);
            assertString(", timesRightInARow=");
            this.timesRightInARow = readIntArray(4);
            assertString(", timesWrongInARow=");
            this.timesWrongInARow = readIntArray(4);
            assertString(", lastTimeStarEarnt=");
            this.lastTimeStarEarnt = readGregorianCalendarArray(4);
            assertString(", lastTimePractised=");
            this.lastTimePractised = readGregorianCalendarArray(4);
            endBraces();
            this.initialised = true;
        }

        private void startBraces() {
            assertString("Word{");
        }

        private void assertString(String s) {
            if (!s.equals(wordAsString.substring(pos,pos+s.length()))) throw new IllegalArgumentException(s);
            pos += s.length();
        }

        private String readString() {
            String out = "";
            if (this.wordAsString.substring(pos,pos+4).equals("null")) {pos += 4; return null;}
            assertString("\"");
            while (this.wordAsString.charAt(pos) != '\"') {
                out += this.wordAsString.charAt(pos);
                pos++;
            }
            assertString("\"");
            return out;
        }

        private ArrayList<String> readStringArrayList() {
            ArrayList<String> out = new ArrayList<String>();
            if (this.wordAsString.substring(pos,pos+4).equals("null")) {pos += 4; return null;}
            assertString("[");
            while (this.wordAsString.charAt(pos) != ']') {
                String newString = "";
                while (this.wordAsString.charAt(pos) != ',' && this.wordAsString.charAt(pos) != ']') {
                    newString += this.wordAsString.charAt(pos);
                    pos++;
                }
                out.add(newString);
                if (this.wordAsString.charAt(pos) == ',') {assertString(", ");}
            }
            assertString("]");
            return out;
        }

        private int[] readIntArray(int n) {
            int[] out = new int[n];
            if (this.wordAsString.substring(pos,pos+4).equals("null")) {pos += 4; return null;}
            assertString("[");
            for (int i = 0; i < n; i++) {
                String intString = "";
                while (this.wordAsString.charAt(pos) != ',' && this.wordAsString.charAt(pos) != ']') {
                    intString += this.wordAsString.charAt(pos);
                    pos++;
                }
                out[i] = Integer.parseInt(intString);
                if (i != n-1) assertString(", ");
            }
            assertString("]");
            return out;
        }

        private GregorianCalendar[] readGregorianCalendarArray(int n) {
            GregorianCalendar[] out = new GregorianCalendar[n];
            if (this.wordAsString.substring(pos,pos+4).equals("null")) {pos += 4; return null;}
            assertString("[");
            for (int i = 0; i < out.length; i++) {
                out[i] = readGregorianCalendar();
                if (i != 3) assertString(", ");
            }
            assertString("]");
            return out;
        }

        private GregorianCalendar readGregorianCalendar() {
            if (this.wordAsString.substring(pos,pos+4).equals("null")) return null;
            String year = this.wordAsString.substring(pos,pos+4);
            pos+=4;
            assertString("/");
            String month = this.wordAsString.substring(pos,pos+2);
            pos+=2;
            assertString("/");
            String day = this.wordAsString.substring(pos,pos+2);
            pos+=2;
            assertString(" ");
            String hourOfDay = this.wordAsString.substring(pos,pos+2);
            pos+=2;
            assertString(":");
            String minute = this.wordAsString.substring(pos,pos+2);
            pos+=2;
            assertString(":");
            String second = this.wordAsString.substring(pos,pos+2);
            pos+=2;
            return new GregorianCalendar(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day),Integer.parseInt(hourOfDay),Integer.parseInt(minute),Integer.parseInt(second));
        }

        private void endBraces() {
            assertString("}");
        }

        public Word returnWord() {
            return new Word(baseLanguage,
                    targetLanguage,
                    baseLanguageDisplayWord,
                    targetLanguageDisplayWord,
                    baseLanguageAnswers,
                    targetLanguageAnswers,
                    spokenAnswers,
                    audioData,
                    starsEarnt,
                    timesRightInARow,
                    timesWrongInARow,
                    lastTimeStarEarnt,
                    lastTimePractised);
        }
    }*/

    public String toStringJSON() {
        JsonObjectBuilder obj = Json.createObjectBuilder()
                .add("type","Word")
                .add("baseLanguage",baseLanguage)
                .add("targetLanguage",targetLanguage)
                .add("baseLanguageDisplayWord",baseLanguageDisplayWord)
                .add("targetLanguageDisplayWord",targetLanguageDisplayWord)
                .add("baseLanguageAnswers",jsonArray(baseLanguageAnswers))
                .add("targetLanguageAnswers",jsonArray(targetLanguageAnswers));
        if (spokenAnswers != null) obj
                .add("spokenAnswers",jsonArray(spokenAnswers))
                .add("audioData",audioData);
        obj
                .add("starsEarnt",jsonArray(starsEarnt))
                .add("timesRightInARow",jsonArray(timesRightInARow))
                .add("timesWrongInARow",jsonArray(timesWrongInARow))
                .add("lastTimeStarEarnt",jsonArray(g -> timeAsString((GregorianCalendar) g),lastTimeStarEarnt))
                .add("lastTimePractised",jsonArray(g -> timeAsString((GregorianCalendar) g),lastTimePractised));
        return obj.build().toString();
    }

    private JsonArray jsonArray(String... strings) {
        if (strings == null) return Json.createArrayBuilder().build();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (String s : strings) arrayBuilder.add(s);
        return arrayBuilder.build();
    }

    private JsonArray jsonArray(Collection<String> strings) {
        if (strings == null) return Json.createArrayBuilder().build();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (String s : strings) arrayBuilder.add(s);
        return arrayBuilder.build();
    }

    private JsonArray jsonArray(int... ints) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (int i : ints) arrayBuilder.add(i);
        return arrayBuilder.build();
    }

    private JsonArray jsonArray(Function<Object,String> func, Object... objs) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object obj : objs) arrayBuilder.add(func.apply(obj));
        return arrayBuilder.build();
    }

    private static class JSONParser {
        private String wordAsString;
        private JsonObject obj;
        int pos = 0;

        private String baseLanguage;
        private String targetLanguage;
        private String baseLanguageDisplayWord;

        public String getBaseLanguageDisplayWord() {
            return baseLanguageDisplayWord;
        }

        public String getTargetLanguageDisplayWord() {
            return targetLanguageDisplayWord;
        }

        public ArrayList<String> getBaseLanguageAnswers() {
            return baseLanguageAnswers;
        }

        public ArrayList<String> getTargetLanguageAnswers() {
            return targetLanguageAnswers;
        }

        public ArrayList<String> getSpokenAnswers() {
            return spokenAnswers;
        }

        public String getAudioData() {
            return audioData;
        }

        public int[] getStarsEarnt() {
            return starsEarnt;
        }

        public int[] getTimesRightInARow() {
            return timesRightInARow;
        }

        public int[] getTimesWrongInARow() {
            return timesWrongInARow;
        }

        public GregorianCalendar[] getLastTimeStarEarnt() {
            return lastTimeStarEarnt;
        }

        public GregorianCalendar[] getLastTimePractised() {
            return lastTimePractised;
        }

        private String targetLanguageDisplayWord;
        private ArrayList<String> baseLanguageAnswers;
        private ArrayList<String> targetLanguageAnswers;
        private ArrayList<String> spokenAnswers;
        private String audioData;
        private int[] starsEarnt;
        private int[] timesRightInARow;
        private int[] timesWrongInARow;
        private GregorianCalendar[] lastTimeStarEarnt;
        private GregorianCalendar[] lastTimePractised;

        public JSONParser(String word) {
            this.wordAsString = word;
            obj = Json.createReader(new StringReader(word)).readObject();
            baseLanguage = obj.getString("baseLanguage");
            targetLanguage = obj.getString("targetLanguage");
            baseLanguageDisplayWord = obj.getString("baseLanguageDisplayWord");
            targetLanguageDisplayWord = obj.getString("targetLanguageDisplayWord");
            baseLanguageAnswers = new ArrayList<>(Arrays.asList(stringArray(obj.getJsonArray("baseLanguageAnswers"))));
            targetLanguageAnswers = new ArrayList<>(Arrays.asList(stringArray(obj.getJsonArray("targetLanguageAnswers"))));
            if (obj.containsKey("spokenAnswers")) {
                spokenAnswers = new ArrayList<>(Arrays.asList(stringArray(obj.getJsonArray("spokenAnswers"))));
                audioData = obj.getString("audioData");
            }
            starsEarnt = intArray(obj.getJsonArray("starsEarnt"));
            timesRightInARow = intArray(obj.getJsonArray("timesRightInARow"));
            timesWrongInARow = intArray(obj.getJsonArray("timesWrongInARow"));
            lastTimeStarEarnt = gregorianCalendarArray(obj.getJsonArray("lastTimeStarEarnt"));
            lastTimePractised = gregorianCalendarArray(obj.getJsonArray("lastTimePractised"));
        }

        public String getBaseLanguage() {
            return baseLanguage;
        }

        public String[] stringArray(JsonArray array) {
            String[] strings = new String[array.size()];
            for (int i = 0; i < array.size(); i++)
                strings[i] = array.getString(i);
            return strings;
        }

        public int[] intArray(JsonArray array) {
            int[] ints = new int[array.size()];
            for (int i = 0; i < array.size(); i++)
                ints[i] = array.getInt(i);
            return ints;
        }

        public GregorianCalendar[] gregorianCalendarArray(JsonArray array) {
            GregorianCalendar[] times = new GregorianCalendar[array.size()];
            for (int i = 0; i < array.size(); i++)
                times[i] = parseTime(array.getString(i));
            return times;
        }

        public GregorianCalendar parseTime(String s) {
            pos = 0;
            String year = s.substring(pos,pos+4);
            pos+=4;
            assertString(s,"/");
            String month = s.substring(pos,pos+2);
            pos+=2;
            assertString(s,"/");
            String day = s.substring(pos,pos+2);
            pos+=2;
            assertString(s," ");
            String hourOfDay = s.substring(pos,pos+2);
            pos+=2;
            assertString(s,":");
            String minute = s.substring(pos,pos+2);
            pos+=2;
            assertString(s,":");
            String second = s.substring(pos,pos+2);
            pos+=2;
            return new GregorianCalendar(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day),Integer.parseInt(hourOfDay),Integer.parseInt(minute),Integer.parseInt(second));
        }

        private void assertString(String text, String search) {
            if (!search.equals(text.substring(pos,pos+search.length()))) throw new IllegalArgumentException(search);
            pos += search.length();
        }

        public String getTargetLanguage() {
            return targetLanguage;
        }
    }
}