package morphalizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Utile {

    String filePath;

    public void setFilePath(Object path) {
        if (path instanceof String) {
            filePath = (String) path;
        } else if (path instanceof File) {
            filePath = ((File) path).getAbsolutePath();
        }
    }

    public String sentence(int num) {
        if (filePath == null) {
            pl("You must set file path befor...");
            return null;
        }
        BufferedReader reader;
        String sentence = null;
        try {

            reader = new BufferedReader(new FileReader(filePath));

            int line = 0;

            while ((sentence = reader.readLine()) != null) {
                line++;
                if (num == line) {
                    reader.close();
                    break;
                }
            }
        } catch (Exception e) {

        }
        return sentence;
    }

    public List<String> sentences() {
        StringBuilder stringBuilder = new StringBuilder();
        if (filePath == null) {
            pl("You must set file path befor...");
            return null;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = null;
            String ls = System.getProperty("line.separator");

            try {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }

            } finally {
                reader.close();
            }
        } catch (Exception e) {

        }
        List<String> pureSent = new ArrayList<>();
        String[] sentences = stringBuilder.toString().split("\\.|:|\n|\\?");
        for (String sentence : sentences) {
            String sent = sentence.trim();
            if (!"".equals(sent)) {
                pureSent.add(sent + ".");
            }
        }
        return pureSent;
    }

    public static List<Character> diacriticList() {
        Character[] diacritics = new Character[]{'َ', 'ِ', 'ُ', 'ْ', 'ّ', 'ً', 'ٍ', 'ٌ'};
        return Arrays.asList(diacritics);

    }

    public static String deDiacritic(String term) {
        String res = "";
        if (term != null) {
            char[] chars = term.toCharArray();

            for (Character ch : chars) {
                if (diacriticList().contains(ch)) {
                    continue;
                } else if (ch.equals('ٱ')) {
                    ch = 'ا';
                }
                res = res + ch;
            }
        }

        return res;
    }

    public static void pl(Object o) {
        System.out.println(o);
    }

    void p(Object o) {
        System.out.print(o);
    }

    void pt(Object o) {
        System.out.print(o + "\t:");
    }

    public static void next() {
        try {
            char c = (char) System.in.read();
            if (c == 'q' || c == '0') {
                System.exit(1);
            }
        } catch (Exception e) {

        }

    }

    public void printSentence(Sentence sentence) {
        Word w;
        Iterator<Word> itr = sentence.iterator();
        while (itr.hasNext()) {
            w = itr.next();
            pt(w.value());
            pt(w.diac());
            pt(w.getStem());
            pt(w.tag());
            pt("clitics[" + w.prc3Val + "," + w.prc2Val + "," + w.prc1Val + "," + w.stemyNodiacDet + "," + w.encVal + "]");
            pl("");
        }
        pl("---------------------------------------------");
    }

   public static void printList(List list) {
        for (Object o : list) {
            pl(o);
        }
    }
}
