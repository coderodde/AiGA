package fi.helsinki.coderodde.aoga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Utils {
    
    public static final SortedMap<Character, SortedSet<String>> 
            MAP_AMINO_ACID_TO_CODON_SETS;    

    public static final SortedMap<Character, List<String>> 
            MAP_AMINO_ACID_TO_CODON_LISTS;
    
    public static final List<Character> AMINO_ACID_ALPHABET_LIST;
    public static final Set<Character> AMINO_ACID_ALPHABET_SET;
    
    public static final Map<String, Character> MAP_CODON_TO_AMINO_ACID;
    
    // The list of all codons:
    public static final List<String> CODONS;
    
    static {
        SortedMap<Character, List<String>> mapAminoAcidToCodonLists = 
                new TreeMap<>();
        
        mapAminoAcidToCodonLists.put('A', Arrays.asList("GCT", "GCC", "GCA", "GCG"));
        mapAminoAcidToCodonLists.put('C', Arrays.asList("TGT", "TGC"));
        mapAminoAcidToCodonLists.put('D', Arrays.asList("GAT", "GAC"));
        mapAminoAcidToCodonLists.put('E', Arrays.asList("GAA", "GAG"));
        mapAminoAcidToCodonLists.put('F', Arrays.asList("TTT", "TTC"));
        mapAminoAcidToCodonLists.put('G', Arrays.asList("GGT", "GGC", "GGA", "GGG"));
        mapAminoAcidToCodonLists.put('H', Arrays.asList("CAT", "CAC"));
        mapAminoAcidToCodonLists.put('I', Arrays.asList("ATT", "ATC", "ATA"));
        mapAminoAcidToCodonLists.put('K', Arrays.asList("AAA", "AAG"));
        mapAminoAcidToCodonLists.put('L', Arrays.asList("TTA", "TTG", "CTT", "CTC", "CTA", "CTG"));
        mapAminoAcidToCodonLists.put('M', Arrays.asList("ATG"));
        mapAminoAcidToCodonLists.put('>', Arrays.asList("ATG"));
        mapAminoAcidToCodonLists.put('N', Arrays.asList("AAT", "AAC"));
        mapAminoAcidToCodonLists.put('P', Arrays.asList("CCT", "CCC", "CCA", "CCG"));
        mapAminoAcidToCodonLists.put('Q', Arrays.asList("CAA", "CAG"));
        mapAminoAcidToCodonLists.put('R', Arrays.asList("CGT", "CGC", "CGA", "CGG", "AGA", "AGG"));
        mapAminoAcidToCodonLists.put('S', Arrays.asList("TCT", "TCC", "TCA", "TCG", "AGT", "AGC"));
        mapAminoAcidToCodonLists.put('T', Arrays.asList("ACT", "ACC", "ACA", "ACG"));
        mapAminoAcidToCodonLists.put('V', Arrays.asList("GTT", "GTC", "GTA", "GTG"));
        mapAminoAcidToCodonLists.put('W', Arrays.asList("TGG"));
        mapAminoAcidToCodonLists.put('Y', Arrays.asList("TAT", "TAC"));
        mapAminoAcidToCodonLists.put('<', Arrays.asList("TAA", "TGA", "TAG"));
        
        MAP_AMINO_ACID_TO_CODON_LISTS =
                Collections.unmodifiableSortedMap(mapAminoAcidToCodonLists);
        
        SortedMap<Character, SortedSet<String>> mapAminoAcidToCodonsSets = 
                new TreeMap<>();
        
        MAP_AMINO_ACID_TO_CODON_SETS = Collections.unmodifiableSortedMap(mapAminoAcidToCodonsSets);
        
        // Order the codons in the alphabetic order:
        for (Map.Entry<Character, List<String>> e : MAP_AMINO_ACID_TO_CODON_LISTS.entrySet()) {
            e.getValue().sort(String::compareTo);
        }
        
        // Copy list codons to the (sorted) set codons:
        for (Map.Entry<Character, List<String>> e : MAP_AMINO_ACID_TO_CODON_LISTS.entrySet()) {   
            SortedSet<String> aminoAcidCodons = new TreeSet<>();
            mapAminoAcidToCodonsSets.put(e.getKey(), aminoAcidCodons);
            
            for (String codon : e.getValue()) {
                aminoAcidCodons.add(codon);
            }
        }
        
        Set<Character> aminoAcidAlphabet = new HashSet<>();
        
        aminoAcidAlphabet.add('A');
        aminoAcidAlphabet.add('C');
        aminoAcidAlphabet.add('D');
        aminoAcidAlphabet.add('E');
        aminoAcidAlphabet.add('F');
        aminoAcidAlphabet.add('G');
        aminoAcidAlphabet.add('H');
        aminoAcidAlphabet.add('I');
        aminoAcidAlphabet.add('K');
        aminoAcidAlphabet.add('L');
        aminoAcidAlphabet.add('M');
        aminoAcidAlphabet.add('N');
        aminoAcidAlphabet.add('P');
        aminoAcidAlphabet.add('Q');
        aminoAcidAlphabet.add('R');
        aminoAcidAlphabet.add('S');
        aminoAcidAlphabet.add('T');
        aminoAcidAlphabet.add('V');
        aminoAcidAlphabet.add('W');
        aminoAcidAlphabet.add('Y');
        
        AMINO_ACID_ALPHABET_LIST = 
                Collections.unmodifiableList(
                        new ArrayList<>(aminoAcidAlphabet));
        
        AMINO_ACID_ALPHABET_SET = 
                Collections.unmodifiableSet(aminoAcidAlphabet);
        
        Map<String, Character> mapCodonToAminoAcidCharacter = new HashMap<>();
        
        for (Map.Entry<Character, List<String>> e : MAP_AMINO_ACID_TO_CODON_LISTS.entrySet()) {
            for (String codon : e.getValue()) {
                mapCodonToAminoAcidCharacter.put(codon, e.getKey());
            }
        }
        
        MAP_CODON_TO_AMINO_ACID =
                Collections.unmodifiableMap(mapCodonToAminoAcidCharacter);
        
        List<String> codons = new ArrayList<>(64);
        
        for (Map.Entry<Character, List<String>> e : MAP_AMINO_ACID_TO_CODON_LISTS.entrySet()) {
            for (String codon : e.getValue()) {
                codons.add(codon);
            }
        }
        
        codons.sort(Utils::compare);
        CODONS = Collections.unmodifiableList(codons);
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
    
    public static Character getAminoAcid(String codon) {
        return MAP_CODON_TO_AMINO_ACID.get(codon);
    }
    
    public static String generateRandomProteinSequence(List<Character> alphabet,
                                                       int length,
                                                       Random random) {
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.get(random.nextInt(alphabet.size())));
        }
        
        return sb.toString();
    }
}