package fi.helsinki.coderodde.aoga.chapter01;

import fi.helsinki.coderodde.aoga.Codon;
import fi.helsinki.coderodde.aoga.MultipleGroupPermuter;
import fi.helsinki.coderodde.aoga.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Exercise_1_3 {
    
    public static void main(String[] args) {
        subTask1();
        System.out.println();
        subTask2();
    }
    
    static void subTask1() {
        System.out.println("--- Subtask 1.3.1 ---");
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        System.out.println("seed = " + seed);
        List<String> exons = getRandomExons(random);
        System.out.println("Exons: " + exons);
        Map<String, Double> zMap = computeZMap(exons);
        System.out.println("Z-map: " + zMap);
    }
    
    static void subTask2() {
        System.out.println("--- Subtask 1.3.2 ---");
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        System.out.println("seed = " + seed);
        
        int proteinLength = 2 + random.nextInt(39);
        String protein =
                Utils.generateRandomProteinSequence(
                        Utils.AMINO_ACID_ALPHABET_LIST, 
                        proteinLength, 
                        random);
        
        System.out.println("Source protein S: " + protein);
        
        Map<String, Double> zMap = computeZMap(Arrays.asList(protein));
        System.out.println("f(S): " + f(zMap));
        
        List<Codon> sourceProteinAsCodonList = getRandomCodonSequence(random);
        
        System.out.println(Codon.codonListToString(sourceProteinAsCodonList));
        
        processProtein(protein, sourceProteinAsCodonList);
    }
    
    private static void processProtein(String protein, List<Codon> codonList) {
        Map<Character, List<Integer>> mapAminoAcidToIndices = 
                buildAminoAcidToIndicesMap(protein);
        
        List<List<Integer>> indexGroups = getIndexGroups(mapAminoAcidToIndices);
        
        List<List<List<Integer>>> groupPermutations = 
                new MultipleGroupPermuter<>(indexGroups)
                        .computeGroupPermutations();
        
        List<ProteinPermutation> proteinPermutationList = 
                computeProteinPermutationList(codonList, groupPermutations);
        
        proteinPermutationList.sort(new ProteinPermutationComparator());
    }
    
    private static List<ProteinPermutation> 
        computeProteinPermutationList(
                List<Codon> proteinAsCodonList,
                List<List<List<Integer>>> groupPermutations) {
            
        List<ProteinPermutation> proteinPermutationList = 
                new ArrayList<>(groupPermutations.size());
        
        for (List<List<Integer>> listOfIndexGroups : groupPermutations) {
            List<Codon> permutedCodonList = 
                    computePermutedProtein(proteinAsCodonList, 
                                           listOfIndexGroups);
            
            String permutedAminoAcidString = 
                    convertCodonListToAminoAcidString(permutedCodonList);
            
            Map<String, Double> zMap = 
                    computeZMap(Arrays.asList(permutedAminoAcidString));
            
            double zMapF = f(zMap);
            
            ProteinPermutation proteinPermutation = 
                    new ProteinPermutation(
                            zMapF,
                            permutedAminoAcidString,
                            proteinAsCodonList);
            
            proteinPermutationList.add(proteinPermutation);
        }
        
        return proteinPermutationList;
    }
        
    private static String convertCodonListToAminoAcidString(
            List<Codon> permutedCodonList) {
        StringBuilder stringBuilder = 
                new StringBuilder(permutedCodonList.size());
        
        for (Codon codon : permutedCodonList) {
            char aminoAcid = Utils.MAP_CODON_TO_AMINOA_ACID.get(codon);
            stringBuilder.append(aminoAcid);
        }
        
        return stringBuilder.toString();
    }
        
    private static List<Codon> computePermutedProtein(
            List<Codon> proteinAsCodonList,
            List<List<Integer>> listOfIndexGroups) {
        
        List<Codon> outputProteinAsCodonList = 
                new ArrayList<>(proteinAsCodonList.size());
        
        for (int i = 0; i < proteinAsCodonList.size(); i++) {
            outputProteinAsCodonList.add(null);
        }
        
        int codonIndex = 0;
        
        for (List<Integer> indexGroup : listOfIndexGroups) {
            for (Integer index : indexGroup) {
                outputProteinAsCodonList.set(codonIndex, 
                                             proteinAsCodonList.get(index));
                index++;
            }
        }
        
        return outputProteinAsCodonList;
    }    
    
    private static List<List<Integer>> 
        getIndexGroups(Map<Character, List<Integer>> map) {
        
        List<List<Integer>> indexGroups = new ArrayList<>(map.size());
        
        for (Map.Entry<Character, List<Integer>> e : map.entrySet()) {
            indexGroups.add(new ArrayList<>(e.getValue()));
        }
        
        return indexGroups;
    }
    
    private static Map<Character, List<Integer>> 
        buildAminoAcidToIndicesMap(String protein) {
            
        Map<Character, List<Integer>> mapAminoAcidsToCodonIndexList = 
            computeMapAminoAcidsToCodonIndexList(protein);
        
        for (int i = 0; i < protein.length(); i++) {
            char aminoAcid = protein.charAt(i);
            
            if (!mapAminoAcidsToCodonIndexList.containsKey(aminoAcid)) {
                mapAminoAcidsToCodonIndexList.put(aminoAcid, 
                                                  new ArrayList<>(6));
            }
            
            mapAminoAcidsToCodonIndexList.get(aminoAcid).add(i);
        }
        
        return mapAminoAcidsToCodonIndexList;
    }

    private static Map<Character, List<Integer>>
        computeMapAminoAcidsToCodonIndexList(String protein) {
        
        Map<Character, List<Integer>> map = new HashMap<>();

        for (int i = 0; i < protein.length(); i++) {
            Character aminoAcid = protein.charAt(i);
            
            if (!map.containsKey(aminoAcid)) {
                map.put(aminoAcid, new ArrayList<>(6));
            }

            map.get(aminoAcid).add(i);
        }

        return map;
    }
    
    private static void processCodons(List<Codon> codonList) {
//        Map<Character, List<Integer>> mapAminoAcid
//        
    }
    
    private static List<Codon> getRandomCodonSequence(Random random) {
        int codonListLength = 2 + random.nextInt(9);
        List<Codon> codonList = new ArrayList<>(codonListLength);
        List<Character> alphabet = Utils.AMINO_ACID_ALPHABET_LIST;
        
        for (int i = 0; i < codonListLength; i++) {
            Character aminoAcid = alphabet.get(random.nextInt(alphabet.size()));
            List<String> codonStrings = Utils.getAminoAcidCodons(aminoAcid);
            String codonString = 
                    codonStrings.get(random.nextInt(codonStrings.size()));
            
            Codon codon = new Codon(codonString);
            codonList.add(codon);
        }
        
        return codonList;
    }
    
    static double f(Map<String, Double> m) {
        double sum = 0.0;
        
        for (Map.Entry<String, Double> e : m.entrySet()) {
            sum += e.getValue();
        }
        
        return sum / m.size();
    }
    
    static Map<String, Double> computeZMap(List<String> exons) {
        Map<String, Integer> expectedFrequencies = 
                getCodonPairFrequencies(exons);
        
        Map<String, Double> result = new HashMap<>();
        
        for (String exon : exons) {
            for (int i = 0; i < exon.length() - 1; i++) {
                char ch1 = exon.charAt(i);
                char ch2 = exon.charAt(i + 1);
                String codon = String.format("%c%c", ch1, ch2);
                Double value = result.getOrDefault(codon, 0.0);
                value += 1.0;
                result.put(codon, value);
            }
        }
        
        for (Map.Entry<String, Integer> e : expectedFrequencies.entrySet()) {
            String codonPair = e.getKey();
            
            if (result.containsKey(codonPair)) {
                result.put(codonPair, result.get(codonPair) / e.getValue());
            } else {
                System.out.println("hello");
            }
        }
        
        return result;
    }
    
    static Map<String, Integer> getCodonPairFrequencies(List<String> exons) {
        Map<String, Integer> m = new HashMap<>();
        
        for (String exon : exons) {
            for (int i = 0; i < exon.length() - 1; i++) {
                char x = exon.charAt(i);
                char y = exon.charAt(i + 1);
                m.put("" + x + y, getFrequencyOfCodonPair(x, y));
            }
        }
        
        return m;
    }
    
    static int getFrequencyOfCodonPair(char x, char y) {
        return Utils.MAP_AMINO_ACID_TO_CODON_LISTS.get(x).size() *
               Utils.MAP_AMINO_ACID_TO_CODON_LISTS.get(y).size();
    }
    
    private static List<String> getRandomExons(Random random) {
        int exons = random.nextInt(10) + 1;
        List<String> exonList = new ArrayList<>(exons);
        
        for (int i = 0; i < exons; i++) {
            String exon = getRandomExon(random);
            exonList.add(exon);
        }
        
        return exonList;
    }
    
    private static String getRandomExon(Random random) {
        int exonLength = random.nextInt(9) + 2;
        StringBuilder exonStringBuilder = new StringBuilder(exonLength);
        List<Character> alphabet = Utils.AMINO_ACID_ALPHABET_LIST;
        
        for (int i = 0; i < exonLength; i++) {
            char aminoAcid = alphabet.get(random.nextInt(alphabet.size()));
            exonStringBuilder.append(aminoAcid);
        }
        
        return exonStringBuilder.toString();
    }
}

class CodonStringPermutator {
    
    private final List<Codon> codons;
    private final List<List<Codon>> codonStringPermutations = new ArrayList<>();
    
    CodonStringPermutator(List<Codon> codons) {
        this.codons = codons;
    }
    
    private List<List<Integer>> getGroupIndices() {
        List<List<Integer>> result = new ArrayList<>(codons.size());
        Map<Character, List<Integer>> mapAminoAcidToAppearanceList = 
                getAminoAcidToApperanceListMap();
        
        
        return result;
    }
    
    private Map<Character, List<Integer>> getAminoAcidToApperanceListMap() {
        Map<Character, List<Integer>> map = new HashMap<>();
        
        int index = 0;
        
        for (Codon codon : codons) {
            Character codonAminoAcid = codon.getAminoAcid();
            
            if (!map.containsKey(codonAminoAcid)) {
                map.put(codonAminoAcid, new ArrayList<>());
            }
            
            map.get(codonAminoAcid).add(index);
            index++;
        }
        
        return map;
    }
}

class ProteinPermutationComparator implements Comparator<ProteinPermutation> {

    @Override
    public int compare(ProteinPermutation o1, ProteinPermutation o2) {
        return Double.compare(o1.getF(), o2.getF());
    }
}

class ProteinPermutation {

    private final double f;
    private final String proteinAsAminoAcidString;
    private final List<Codon> proteinAsCodonList;
    
    ProteinPermutation(double f, String proteinAsAminoAcidString, List<Codon> proteinAsCodonList) {
        this.f = f;
        this.proteinAsAminoAcidString = proteinAsAminoAcidString;
        this.proteinAsCodonList = proteinAsCodonList;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }
    
    double getF() {
        return f;
    }
    
    String getProteinAsAminoAcidString() {
        return proteinAsAminoAcidString;
    }
    
    List<Codon> getProteinAsCodonList() {
        return proteinAsCodonList;
    }
}


