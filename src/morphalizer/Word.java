package morphalizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static morphalizer.Utile.deDiacritic;
import static morphalizer.Utile.diacriticList;

public class Word {

    public int index;
    String value;
    String diac;
    String tag;
    String stem;
    String stemy;
    String stemNodiac;
    public String stemyNodiacDet;
    String enc0;
    String prc;
    String prc0;
    String prc1;
    String prc2;
    String prc3;
    public String encVal;
    String prc0Val;
    String prc1Val;
    String prc2Val;
    String prc3Val;
    String gloss;
    String per;
    String num;
    public String clitic;
    public String nodiac;
    Sentence sentence;

    public Word() {
        value = diac = tag = stem = enc0 = prc = prc0 = prc1 = prc2 = prc3
                = encVal = prc0Val = prc1Val = prc2Val = prc3Val = gloss = per
                = num = clitic = nodiac = stemy = stemyNodiacDet = "";

    }

    public Word(int id, String t, String val) {
        index = id;
        tag = t;
        value = val;
        clitic = "";
    }

    public int index() {
        return index;
    }

    public String tag() {
        return tag;
    }

    public String value() {
        return value;
    }

    public void setValue(String vl) {
        value = vl;
    }

    @Override
    public String toString() {

        return this.encVal.isEmpty() ? stemyNodiacDet : stemyNodiacDet + " " + encVal;
    }

    public void setIndex(int newIndex) {
        this.index = newIndex;
    }

    public String diac() {
        return diac;
    }

    public String getStem() {
        String stemNoDet = this.stemyNodiacDet;

        if (this.stemyNodiacDet != null && !this.stemyNodiacDet.isEmpty()) {
            if (this.stemyNodiacDet.startsWith("ال") && this.hasDT()) {
                stemNoDet = this.stemyNodiacDet.substring(2, this.stemyNodiacDet.length());
            }
        }
        if (stemNoDet.endsWith("ه")) {
            stemNoDet = stemNoDet.substring(0, stemNoDet.length() - 1) + "ة";
        }
        return stemNoDet;
    }

    public String stem() {
        return stem;
    }

    public String enc0() {
        return enc0;
    }

    public String prc0() {
        return prc0;
    }

    public String prc1() {
        return prc1;
    }

    public String prc2() {
        return prc2;
    }

    public String prc3() {
        return prc3;
    }

    public String per() {
        return per;
    }

    public String num() {
        return num;
    }

    public boolean isPunc() {
        return this.tag.equals("punc");
    }

    public boolean isStop() {
        return value.equals(".")
                || value.equals("?")
                || value.equals("!");
    }

    public boolean isPart() {
        return this.tag.startsWith("part");
    }

    public boolean isPartNeg() {
        return this.tag.equals("part_neg");
    }

    public boolean isNoun() {
        return this.tag.equals("noun") && !this.hasDT();
    }

    public boolean isDTnoun() {
        return this.tag.equals("noun_det");
    }

    public boolean isPron() {
        return this.tag.equals("pron");
    }

    public boolean isPronX() {
        return this.tag.startsWith("pron_");
    }

    public boolean isPnoun() {
        return this.tag.equals("noun_prop");
    }

    public boolean isAdj() {
        return this.tag.startsWith("adj") && !this.hasDT();
    }

    public boolean isDTadj() {
        return this.tag.startsWith("adj") && this.hasDT();
    }

    public boolean isVerb() {
        return this.tag.equals("verb");
    }

    public boolean isDTJJ() {
        return this.tag.startsWith("adj") && hasDT();
    }

    public boolean isPrep() {
        return this.tag.equals("prep");
    }

    public boolean hasDT() {
        return this.prc0.equals("Al_det");
    }

    public boolean hasEnc() {
        return !this.enc0.toLowerCase().trim().equals("0")
                && !this.enc0.toLowerCase().trim().equals("na")
                && (enc0.startsWith("1") || enc0.startsWith("2") || enc0.startsWith("3"))
                && !this.enc0.isEmpty();
    }

    public boolean hasPrc() {
        return this.hasPrc0()
                || this.hasPrc1()
                || this.hasPrc2()
                || this.hasPrc3();
    }

    public boolean hasPrep() {

        return this.prc1.startsWith("bi")
                || this.prc1.startsWith("la")
                || this.prc1.startsWith("li")
                || this.prc1.endsWith("prep");
    }

    public boolean hasConj() {

        return this.prc1.startsWith("wa")
                || this.prc2.startsWith("fa")
                || this.prc2.startsWith("wa")
                || this.prc2.endsWith("prep")
                || isConj();

    }

    public boolean isConj() {

        return this.tag.startsWith("conj");

    }

    public boolean isRel() {
        return this.tag.equals("pron_rel");
    }

    public boolean hasClitic() {

        return this.hasEnc()
                || this.hasPrc();

    }

    public boolean hasPos() {
        return this.tag() != null && !this.tag().isEmpty();
    }

    public boolean hasPrc1() {
        return !this.prc1.equals("0") && !this.prc1.equals("na") && !this.prc1.isEmpty();
    }

    public boolean hasPrc2() {
        return !this.prc2.equals("0") && !this.prc2.equals("na") && !this.prc2.isEmpty();
    }

    public boolean hasPrc3() {
        return this.prc3 != null && !this.prc3.equals("0") && !this.prc3.equals("na") && !this.prc3.isEmpty();
    }

    public boolean hasPrc0() {
        return !this.prc0.equals("0") && !this.prc0.equals("na")
                && !this.prc0.isEmpty();
    }

    public boolean isShifted() {
        return this.isNoun() && !this.hasPrep() && !this.hasConj();
    }

    public boolean isNominal() {
        return this.tag.startsWith("noun")
                || this.isPron()
                || this.isPnoun()
                || this.isPronX();
    }

    public boolean isInterrogation() {
        return this.tag.contains("interrog")
                || this.tag.equals("adv_rel");
    }

    String precliticing() {

        this.prc3Val = this.hasPrc3() ? this.getprc3() : "";
        this.prc2Val = this.hasPrc2() ? this.getprc2() : "";
        this.prc1Val = this.hasPrc1() ? this.getprc1() : "";
        String pr;
        pr = !this.prc3Val.isEmpty() ? prc3Val + "_" : "";
        pr += !this.prc2Val.isEmpty() ? prc2Val + "_" : "";
        pr += !this.prc1Val.isEmpty() ? prc1Val + "_" : "";
//       pl(pr);
        return pr;
    }

    String getprc3() {
        return this.diac().substring(0, 2);
    }

    String getprc2() {
        return this.hasPrc3() ? this.diac().substring(2, 4) : this.diac().substring(0, 2);
    }

    String getprc1() {
        int length = this.prc1.split("_")[0].length();
        int inx = this.hasPrc3() ? 2 : 0;
        inx += this.hasPrc2() ? 2 : 0;
        String temp = this.diac().substring(inx, length + inx);
        return temp;

    }

    String getprc0() {
        int length = this.prc0.split("_")[0].length();
        int inx = !prc3Val.isEmpty() ? 2 : 0;
        inx += !prc2Val.isEmpty() ? 2 : 0;
        inx += !prc1Val.isEmpty() ? prc1Val.length() : 0;
        String temp = this.diac().substring(inx, length + inx);
        return temp;

    }

    String encliticing() {
        if (this.value().endsWith("ة")) {
            this.stemNodiac = stemNodiac + "ة";
            return "";
        }
        String enc, enc1 = "", enc2;

        String[] slices = this.nodiac.split(stemNodiac);
        if (slices.length > 1) {
            enc1 = slices[1];
        }

        enc2 = this.encMap().get(this.enc0().split("_")[0]);
//        pl(enc1+enc2);
        enc = this.value().endsWith(enc2) ? enc2 : enc1;
        if (stemNodiac.endsWith(enc)) {
            stemNodiac = stemNodiac.substring(0, stemNodiac.length() - enc.length());
        }
        return enc;
    }

    Map<String, String> encMap() {
        Map map = new HashMap();
        map.put("1s", "ي");
        map.put("1s", "ني");
        map.put("1p", "نا");
        map.put("2d", "كما");
        map.put("2p", "كم");
        map.put("2ms", "ك");
        map.put("2mp", "كم");
        map.put("2fs", "ك");
        map.put("2fp", "كن");
        map.put("3d", "هما");
        map.put("3p", "هم");
        map.put("3ms", "ه");
        map.put("3mp", "هم");
        map.put("3fs", "ها");
        map.put("3fp", "هن");
        return map;
    }

    short encDiacLength() {

        short L = 0;
        String enc = this.encVal;
        String diacr = this.diac();
        for (int i = 0; i < enc.length(); i++) {

            while (diacriticList().contains(diacr.charAt(diacr.length() - 1))) {
                diacr = diacr.substring(0, diacr.length() - 1);
                L++;
            }
            diacr = diacr.substring(0, diacr.length() - 1);
            L++;
        }

        return L;
    }

}
