package fi.helsinki.coderodde.aoga;

import java.util.ArrayList;
import java.util.Arrays;
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
        
        int proteinLength = 2 + random.nextInt(9);
        String protein =
                Utils.generateRandomProteinSequence(
                        Utils.AMINO_ACID_ALPHABET_LIST, 
                        proteinLength, 
                        random);
        
        System.out.println("Source protein S: " + protein);
        
        Map<String, Double> zMap = computeZMap(Arrays.asList(protein));
        System.out.println("f(S): " + f(zMap));
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
