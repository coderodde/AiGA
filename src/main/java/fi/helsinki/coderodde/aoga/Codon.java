package fi.helsinki.coderodde.aoga;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Codon {
    private final char nucleobase1;
    private final char nucleobase2;
    private final char nucleobase3;
    
    public Codon(String codonString) {
        this(codonString.charAt(0),
             codonString.charAt(1),
             codonString.charAt(2));
    }
    
    public Codon(char nucleobase1,
                 char nucleobase2,
                 char nucleobase3) {
        this.nucleobase1 = nucleobase1;
        this.nucleobase2 = nucleobase2;
        this.nucleobase3 = nucleobase3;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(3);
        
        sb.append(nucleobase1);
        sb.append(nucleobase2);
        sb.append(nucleobase3);
        
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nucleobase1,
                            nucleobase2,
                            nucleobase3);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        
        if (o == null) {
            return false;
        }
        
        if (!getClass().equals(o.getClass())) {
            return false;
        }
        
        Codon otherCodon = (Codon) o;
        return nucleobase1 == otherCodon.nucleobase1 &&
               nucleobase2 == otherCodon.nucleobase2 &&
               nucleobase3 == otherCodon.nucleobase3;
    }
    
    public char getNucleobase(int index) {
        switch (index) {
            case 0:
                return nucleobase1;
                
            case 1:
                return nucleobase2;
                
            case 2:
                return nucleobase3;
                
            default:
                throw new IndexOutOfBoundsException("index = " + index);
        }
    }
    
    public Character getAminoAcid() {
        return Utils.getAminoAcid(toString());
    }
    
    public List<Codon> getListOfFellowCodons() {
        Character aminoAcid = getAminoAcid();
        List<String> fellowCodonStrings = 
                Utils.MAP_AMINO_ACID_TO_CODON_LISTS.get(aminoAcid);
        
        List<Codon> fellowCodons = new ArrayList<>();
        
        for (String fellowCodonString : fellowCodonStrings) {
            Codon fellowCodon = new Codon(fellowCodonString);
            fellowCodons.add(fellowCodon);
        }
        
        return fellowCodons;
    }
    
    public static String codonListToString(List<Codon> codonList) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        
        for (Codon codon : codonList) {
            if (first) {
                first = false;
            } else {
                sb.append(' ');
            }
            
            sb.append(codon.toString());
        }
        
        return sb.toString();
    }
    
    public static List<Codon> convertProteinToRandomCodonList(String protein,
                                                              Random random) {
        Utils.checkIsProtein(protein);
        List<Codon> outputCodonList = new ArrayList<>(protein.length());
        
        for (char ch : protein.toCharArray()) {
            List<String> codonStringList = 
                    Utils.MAP_AMINO_ACID_TO_CODON_LISTS.get(ch);
            
            String codonString = 
                    codonStringList.get(
                            random.nextInt(codonStringList.size()));
            
            Codon codon = new Codon(codonString);
            outputCodonList.add(codon);
        }
        
        return outputCodonList;
    }
}
