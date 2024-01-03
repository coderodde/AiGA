package fi.helsinki.coderodde.aoga.chapter01;

import fi.helsinki.coderodde.aoga.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

class Exercise_1_2 {

    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        System.out.println("seed = " + seed);
        
        Map<String, Integer> mapCodonToFrequencyMap = 
                getFrequencyMapForEveryCodon(random);
        
        Map<Character, Map<String, Double>> mapCodonToProbability = 
                getGlobalFrequencyMap(mapCodonToFrequencyMap);
        
        List<Character> alphabet = getAlphabet(mapCodonToProbability);
        
        String sourceProtein = Utils.generateRandomProteinSequence(alphabet, 
                                                                   10, 
                                                                   random);
        
        System.out.println("Source protein:  " + sourceProtein);
        
        String randomDNASequence =
                generateRandomDNASequence(
                        mapCodonToProbability, 
                        sourceProtein, 
                        random);
        
        System.out.println("Random sequence: " + randomDNASequence);
    }
    
    private static List<Character> 
        getAlphabet(Map<Character, Map<String, Double>> m) {
        Set<Character> s = new HashSet<>();
        
        for (Map.Entry<Character, Map<String, Double>> e : m.entrySet()) {
            s.add(e.getKey());
        }
        
        return new ArrayList<>(s);
    }
    
    static String generateRandomDNASequence(
            Map<Character, Map<String, Double>> frequencyMap, 
            String proteinSequence, 
            Random random) {
        
        Utils.checkIsProtein(proteinSequence);
        
        StringBuilder dnaSequenceBuilder = new StringBuilder();
        
        for (char aminoAcid : proteinSequence.toCharArray()) {
            String aminoAcidCodon = sampleRandomCodon(aminoAcid, 
                                                      frequencyMap,
                                                      random);
            
            dnaSequenceBuilder.append(aminoAcidCodon);
        }
        
        return dnaSequenceBuilder.toString();
    }
    
    private static String 
        sampleRandomCodon(Character aminoAcid,
                          Map<Character, Map<String, Double>> frequencyMap,
                          Random random) {
        
        double coin = random.nextDouble();
        Map<String, Double> m = frequencyMap.get(aminoAcid);
        
        for (Map.Entry<String, Double> e : m.entrySet()) {
            String codon = e.getKey();
            Double probability = e.getValue();
            
            if (coin <= probability) {
                return codon;
            }
            
            coin -= probability;
        }
        
        throw new IllegalStateException("Should not get here.");
    }
    
    private static Map<Character, Integer> 
        getAminoAcidMap(Map<String, Integer> localMap) {
        Map<Character, Integer> result = new HashMap<>();
        
        for (Map.Entry<String, Integer> e : localMap.entrySet()) {
            String codon = e.getKey();
            Integer count = e.getValue();
            Character aminoAcid = Utils.getAminoAcid(codon);
            Integer currentCount = result.getOrDefault(aminoAcid, 0) + count;
            result.put(aminoAcid, currentCount);
        }
        
        return result;
    }
    
    static Map<Character, Map<String, Double>>
         getGlobalFrequencyMap(Map<String, Integer> localMap) {
             
        Map<Character, Map<String, Double>> result = new HashMap<>();
        Map<Character, Integer> aminoAcidMap = getAminoAcidMap(localMap);
        
        // Count the local frequencies:
        for (Map.Entry<String, Integer> e : localMap.entrySet()) {
            String codon = e.getKey();
            Character aminoAcid = Utils.getAminoAcid(codon);
            
            if (!result.containsKey(aminoAcid)) {
                result.put(aminoAcid, new HashMap<>());
            }
            
            if (!result.get(aminoAcid).containsKey(codon)) {
                result.get(aminoAcid).put(codon, 0.0);
            }
            
            Double currentCodonFrequency = result.get(aminoAcid).get(codon);
            result.get(aminoAcid).put(codon, 
                                      currentCodonFrequency + 
                                      localMap.get(codon));
        }
        
        // Normalize the frequencies into probabilites:
        for (Map.Entry<Character, Map<String, Double>> e : result.entrySet()) {
            Character aminoAcid = e.getKey();
            Integer aminoAcidCount = aminoAcidMap.get(aminoAcid);
            
            for (Map.Entry<String, Double> ee : e.getValue().entrySet()) {
                Double value = ee.getValue();
                Double probability = value / aminoAcidCount;
                ee.setValue(probability);
            }
        }
        
        return result;
    }
         
    private static Map<String, Integer> getFrequencyMapForEveryCodon(Random random) {
        int samples = random.nextInt(200) + 1;
        Map<String, Integer> frequencyMapCodonToFrequency = new HashMap<>();
        
        List<String> allCodons = Utils.CODONS;
        
        for (int i = 0; i < samples; i++) {
            String codon = allCodons.get(random.nextInt(allCodons.size()));
            Integer codonFrequency = 1 + random.nextInt(10);
            frequencyMapCodonToFrequency.put(codon, codonFrequency);
        }
        
        return frequencyMapCodonToFrequency;
    }
}
