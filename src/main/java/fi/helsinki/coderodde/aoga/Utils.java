package fi.helsinki.coderodde.aoga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Utils {
    
    public static final SortedMap<Character, SortedSet<String>> 
            MAP_AMINO_ACID_TO_CODON_SETS = new TreeMap<>();    

    public static final SortedMap<Character, List<String>> 
            MAP_AMINO_ACID_TO_CODON_LISTS = new TreeMap<>();
    
    static {
        
        MAP_AMINO_ACID_TO_CODON_LISTS.put('A', Arrays.asList("GCT", "GCC", "GCA", "GCG"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('C', Arrays.asList("TGT", "TGC"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('D', Arrays.asList("GAT", "GAC"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('E', Arrays.asList("GAA", "GAG"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('F', Arrays.asList("TTT", "TTC"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('G', Arrays.asList("GGT", "GGC", "GGA", "GGG"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('H', Arrays.asList("CAT", "CAC"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('I', Arrays.asList("ATT", "ATC", "ATA"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('K', Arrays.asList("AAA", "AAG"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('L', Arrays.asList("TTA", "TTG", "CTT", "CTC", "CTA", "CTG"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('M', Arrays.asList("ATG"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('>', Arrays.asList("ATG"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('N', Arrays.asList("AAT", "AAC"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('P', Arrays.asList("CCT", "CCC", "CCA", "CCG"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('Q', Arrays.asList("CAA", "CAG"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('R', Arrays.asList("CGT", "CGC", "CGA", "CGG", "AGA", "AGG"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('S', Arrays.asList("TCT", "TCC", "TCA", "TCG", "AGT", "AGC"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('T', Arrays.asList("ACT", "ACC", "ACA", "ACG"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('V', Arrays.asList("GTT", "GTC", "GTA", "GTG"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('W', Arrays.asList("TGG"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('Y', Arrays.asList("TAT", "TAC"));
        MAP_AMINO_ACID_TO_CODON_LISTS.put('<', Arrays.asList("TAA", "TGA", "TAG"));
        
        // Order the codons in the alphabetic order:
        for (Map.Entry<Character, List<String>> e : MAP_AMINO_ACID_TO_CODON_LISTS.entrySet()) {
            e.getValue().sort(String::compareTo);
        }
        
        // Copy list codons to the (sorted) set codons:
        for (Map.Entry<Character, List<String>> e : MAP_AMINO_ACID_TO_CODON_LISTS.entrySet()) {   
            SortedSet<String> aminoAcidCodons = new TreeSet<>();
            MAP_AMINO_ACID_TO_CODON_SETS.put(e.getKey(), aminoAcidCodons);
            
            for (String codon : e.getValue()) {
                aminoAcidCodons.add(codon);
            }
        }
    }
    
    public static int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
    
    public static boolean isAminoAcid(char ch) {
        switch (ch) {
            case 'A':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'V':
            case 'W':
            case 'Y':
                return true;
                
            default:
                return false;
        }
    }
    
    public static void checkIsProtein(String proteinCandidate) {
        for (int index = 0; index < proteinCandidate.length(); index++) {
            char ch = proteinCandidate.charAt(index);
            
            if (!isAminoAcid(ch)) {
                throw new IllegalArgumentException(
                        "Character at index " 
                                + index 
                                + " (" 
                                + ch 
                                + ")is not an amino acid!");
            }
        }
    }
    
    public static List<String> getAminoAcidCodons(char aminoAcid) {
        return MAP_AMINO_ACID_TO_CODON_LISTS.get(aminoAcid);
    }
    
    public static List<List<String>> getProteinCodons(String protein) {
        checkIsProtein(protein);
        
        List<List<String>> proteinCodons = new ArrayList<>(protein.length());
        
        for (char ch : protein.toCharArray()) {
            List<String> codons = MAP_AMINO_ACID_TO_CODON_LISTS.get(ch);
            proteinCodons.add(codons);
        }
        
        return proteinCodons;
    }
    
    public static int getIntegerStringLength(int i) {
        return Integer.toString(i).length();
    }
}
