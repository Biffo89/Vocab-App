package VocabApp.util;

import VocabApp.Main;
import VocabApp.model.Word;
import VocabApp.model.WordRun;
import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Util {

    public static String capitalise(String s) {
        if (s == null || s.length() == 0) return s;
        else return s.substring(0,1).toUpperCase() + s.substring(1);
    }

    public static Image scaleWidth(Image source, int targetWidth, boolean preserveRatio) {
        ImageView imageView = new ImageView(source);
        imageView.setPreserveRatio(preserveRatio);
        imageView.setFitWidth(targetWidth);
        return imageView.snapshot(null, null);
    }

    public static Image scaleHeight(Image source, int targetHeight, boolean preserveRatio) {
        ImageView imageView = new ImageView(source);
        imageView.setPreserveRatio(preserveRatio);
        imageView.setFitHeight(targetHeight);
        return imageView.snapshot(null, null);
    }

    public static Object getRandom(List list) {
        if (list == null || list.size() == 0) return null;
        int index = Main.random.nextInt(list.size());
        return list.get(index);
    }

    public static Word getRandomLearnableNotInRun(List<Word> list, WordRun run, int learningMode) {
        ArrayList<Word> learnableList = new ArrayList<Word>();
        for (Word word : list) {
            if (learningMode != 4) {
                if (word.isLearnable(learningMode) && !run.contains(word))
                    learnableList.add(word);
            } else if ((word.isLearnable(Word.TARGET_TO_BASE_WRITTEN) ||
                       word.isLearnable(Word.BASE_TO_TARGET_WRITTEN) ||
                       word.isLearnable(Word.TARGET_TO_TARGET_SPOKEN) ||
                       word.isLearnable(Word.TARGET_TO_BASE_SPOKEN)) && !run.contains(word)) {
                learnableList.add(word);
            }
        }
        return (Word) getRandom(learnableList);
    }

    public static boolean containsLearnableWordNotInRun(List<Word> list, WordRun run, int learningMode) {
        for (Word word : list) {
            if (word.isLearnable(learningMode) && !run.contains(word)) return true;
        }
        return false;
    }

    public static void actionWithDelay(Action action, long delay) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
            @Override
            public void run() {
                action.act();
            }
        }, delay);
    }

    @FunctionalInterface
    public interface Action {
        void act();
    }

    private static BitSet byteToBitSet(byte b) {
        BitSet bits = new BitSet(8);
        for (int i = 0; i < 8; i++) {
            bits.set(i, (b & 1) == 1);
            b >>= 1;
        }
        return bits;
    }

    public static String byteToHex(byte b) {
        BitSet bits = byteToBitSet(b);
        BitSet nibble1 = bits.get(0,4);
        BitSet nibble2 = bits.get(4,8);
        return "" + nibbleToHex(nibble2) + nibbleToHex(nibble1);
    }

    private static char nibbleToHex(BitSet bits) {
        if (!bits.get(3)) {
            if (!bits.get(2)) {
                if (!bits.get(1)) {
                    if (!bits.get(0)) {
                        return '0';
                    } else {
                        return '1';
                    }
                } else {
                    if (!bits.get(0)) {
                        return '2';
                    } else {
                        return '3';
                    }
                }
            } else {
                if (!bits.get(1)) {
                    if (!bits.get(0)) {
                        return '4';
                    } else {
                        return '5';
                    }
                } else {
                    if (!bits.get(0)) {
                        return '6';
                    } else {
                        return '7';
                    }
                }
            }
        } else {
            if (!bits.get(2)) {
                if (!bits.get(1)) {
                    if (!bits.get(0)) {
                        return '8';
                    } else {
                        return '9';
                    }
                } else {
                    if (!bits.get(0)) {
                        return 'A';
                    } else {
                        return 'B';
                    }
                }
            } else {
                if (!bits.get(1)) {
                    if (!bits.get(0)) {
                        return 'C';
                    } else {
                        return 'D';
                    }
                } else {
                    if (!bits.get(0)) {
                        return 'E';
                    } else {
                        return 'F';
                    }
                }
            }
        }
    }

    public static byte stringToByte(String s) {
        int i = 0;
        i += charToNibble(s.charAt(0)) * 16;
        i += charToNibble(s.charAt(1));
        if (i > 127) i -= 256;
        return (byte) i;
    }

    private static byte charToNibble(char c) {
        for (byte b = 0; b < 16; b++) {
            if (c == nibbleToHex(byteToBitSet(b).get(0,4))) return b;
        }
        throw new IllegalArgumentException();
    }

    public static String fileInHex(String path) {
        return fileInHex(new File(path));
    }

    public static String fileInHex(File file) {
        try {
            FileInputStream reader = new FileInputStream(file);
            String s = "";
            while (true) {
                int n = reader.read();
                if (n == -1) break;
                s += Util.byteToHex((byte) n);
            }
            reader.close();
            return s;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void makeTempFile(String name, String data) {
        try {
            //if(!new File(name).setWritable(true)) System.out.println("error");
            //Files.delete(Paths.get(name));
            FileOutputStream writer = new FileOutputStream(name);
            for (int i = 0; i <= data.length() - 2; i += 2)
                writer.write(Util.stringToByte(data.substring(i, i + 2)));
            writer.close();
            writer = new FileOutputStream(name);
            for (int i = 0; i <= data.length() - 2; i += 2)
                writer.write(Util.stringToByte(data.substring(i, i + 2)));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
