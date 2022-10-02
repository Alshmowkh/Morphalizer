/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package morphalizer;

import edu.columbia.ccls.madamira.configuration.OutSeg;
import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import madamiray.Madamiray;

/**
 *
 * @author bakee
 */
public class Morphalizer {

    public static Sentence morphalize(String sent) {

        if (sent == null) {
            return null;
        }

        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;

        mada = new Madamiray(sent);
        outseg = mada.getMorpholizing().get(0);
        sentence = new Sentence(outseg);
        return sentence;
    }

    public static Sentence morphalize(String sent, boolean outsegPrint) {

        if (sent == null) {
            return null;
        }
        if (!outsegPrint) {
            return morphalize(sent);
        } else {
            Sentence sentence = morphalize(sent);
            new Utile().printSentence(sentence);
            return sentence;
        }
    }

    public static Sentence morphalize(String pathFile, int id) {

        Utile ut = new Utile();
        ut.setFilePath(pathFile);
        String rawSent = ut.sentence(id);
        return morphalize(rawSent);
    }

    public static List<Sentence> morphalize(File document) {
        List<Sentence> sentences = new ArrayList();
        Utile ut = new Utile();
        ut.setFilePath(document);
        List<String> rawSents = ut.sentences();

        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;
        {
            mada = new Madamiray(rawSents);
            for (int i = 0; i < rawSents.size(); i++) {
                outseg = mada.getMorpholizing().get(i);
                sentence = new Sentence(outseg);
                sentence.id(i);
                sentences.add(sentence);
            }
        }

        return sentences;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String path = "F:\\Master\\Thesis\\Prototype\\Papers\\Indentification of Arabic Cognate (or Unrestarined Verb)\\Simple congnator corpus.txt";
        String sample = "اللهم صلي على سيدي ومولاي محمد صلى الله عليه وسلم.";
        File file = new File(path);
        if (args.length == 0) {
//            System.out.println(LocalTime.now().getSecond() + LocalTime.now().getMinute() * 60);
            new Morphalizer().morphalize(sample, true);
//            System.out.println(LocalTime.now().getSecond() + LocalTime.now().getMinute() * 60);

        } else if (new File(args[0]).exists()) {
            new Morphalizer().morphalize(new File(args[0]));
        } else {
            new Morphalizer().morphalize(args[0], true);
        }
    }
}
