package morphalizer;

import edu.columbia.ccls.madamira.configuration.MorphFeatureSet;
import edu.columbia.ccls.madamira.configuration.OutSeg;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static morphalizer.Utile.deDiacritic;

public class Sentence extends ArrayList<Word> {

    public int count;
    private int id;
//    Boolean hasFullStop;
    OutSeg outseg;
    int seek;

    public Sentence(OutSeg outs) {
        outseg = outs;
        wording();
    }

    public int count() {
        return count;
    }

    public int id() {
        return this.id;
    }

    public void reorder() {
        for (int i = 0; i < this.size(); i++) {
            this.get(i).index = i;
        }
    }

    public void replace(int i, Word w) {
        this.remove(i);
        this.add(i, w);

    }

    public Sentence newInstance() {
        return this;
    }

    private void wording() {
//        sentence = new Sentence();
        Word clitic = null, word = null;
        List<edu.columbia.ccls.madamira.configuration.Word> morphs = outseg.getWordInfo().getWord();
        int index = 0;;
        MorphFeatureSet wf = null;
        Iterator<edu.columbia.ccls.madamira.configuration.Word> itr = morphs.iterator();
        Iterator itr2;
        while (itr.hasNext()) {
            edu.columbia.ccls.madamira.configuration.Word w = itr.next();
            word = new Word();
            word.value = w.getWord();
//            try {
            itr2 = w.getAnalysis().iterator();
            if (itr2.hasNext()) {
                wf = w.getAnalysis().get(0).getMorphFeatureSet();
                word.diac = wf.getDiac();
                word.gloss = wf.getGloss();
                word.tag = wf.getPos();
                word.enc0 = wf.getEnc0();
                word.prc0 = wf.getPrc0();
                word.prc1 = wf.getPrc1();
                word.prc2 = wf.getPrc2();
                word.prc3 = wf.getPrc3();
                word.stem = wf.getStem();
                word.per = wf.getPer();
                word.num = wf.getNum();
                word.nodiac = deDiacritic(word.diac());
                word.stemNodiac = deDiacritic(word.stem);

                if (word.hasClitic()) {
                    cliticing(word, index);
                    index = this.size();
                } else {
                    word.stemy = word.diac();
                    word.stemyNodiacDet = deDiacritic(word.stemy);
                    word.stemNodiac = deDiacritic(word.value());
                }
                word.index = index++;
//               
//            } catch (Exception e) {
//            }
            }
            if (word.hasPos()) {
                this.add(word);
            }
        }
        count = index;
        //id = outseg.getId();
//        if (wf != null && wf.getPos().equals("punc")) {
//            hasFullStop = true;
//        } else {
//            hasFullStop = false;
//        }

    }

    void cliticing(Word word, int index) {

        Word clitic;
        if (word.hasPrc3()) {
            clitic = new Word();
            clitic.value = word.getprc3();
            word.prc3Val = clitic.value();
            clitic.stemyNodiacDet = clitic.value().substring(0, 1);
            clitic.tag = "ques";
            clitic.index = index++;
            this.add(clitic);
        }
        if (word.hasPrc2()) {
            clitic = new Word();
            clitic.value = word.getprc2();
            word.prc2Val = clitic.value();
            clitic.stemyNodiacDet = clitic.value().substring(0, 1);
            clitic.tag = "conj";
            clitic.index = index++;
            this.add(clitic);
        }
        if (word.hasPrc1()) {
            clitic = new Word();
            clitic.value = word.getprc1();
            word.prc1Val = clitic.value();
            clitic.stemyNodiacDet = deDiacritic(clitic.value());
            clitic.tag = clitic.value().startsWith("و") || clitic.value().startsWith("ف") ? "conj"
                    : clitic.value().startsWith("س") ? "fut"
                            : clitic.value().startsWith("ي") ? "voc" : "prep";
            clitic.index = index++;
            this.add(clitic);
        }
        if (word.hasPrc0() && !word.prc0.contains("det")) {
            clitic = new Word();
            clitic.value = word.getprc0();
            word.prc0Val = clitic.value();
            clitic.stemyNodiacDet = deDiacritic(clitic.value());
            clitic.tag = "part_neg";
            clitic.index = index++;
            this.add(clitic);
        }
        if (word.hasDT()) {
            if (!word.stemNodiac.substring(0, 2).startsWith("ال")) {
                word.stemNodiac = "ال" + word.stemNodiac;
                word.tag = "noun_det";
            }

        }
        if (word.value().endsWith("ة") && !word.stem().endsWith("ة")) {
            word.stemyNodiacDet = word.stemyNodiacDet + "ة";
            word.enc0 = "0";
        }
        word.encVal = word.hasEnc() ? word.encliticing() : "";

        word.stemy = word.diac().substring((word.prc3Val + word.prc2Val + word.prc1Val + word.prc0Val).length(), word.diac().length() - word.encDiacLength());

        word.stemyNodiacDet = deDiacritic(word.stemy);

        if (word.hasDT()) {
            if (!word.stemyNodiacDet.substring(0, 2).startsWith("ال")) {
                if (word.stemyNodiacDet.startsWith("ل")) {
                    word.stemyNodiacDet = "ا" + word.stemyNodiacDet;
                } else {
                    word.stemyNodiacDet = "ال" + word.stemyNodiacDet;
                }
            }
        }
    }

    void id(int id) {
        this.id=id;
    }

}
