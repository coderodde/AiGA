package fi.helsinki.coderodde.aoga.chapter01;

import fi.helsinki.coderodde.aoga.Utils;
import java.util.ArrayList;
import java.util.List;

class Exercise_1_1 {
    
    static List<String> listAllDNASequences(String protein) {
        Utils.checkIsProtein(protein);
        List<List<String>> listOfCodonLists = Utils.getProteinCodons(protein);
        List<String> container = new ArrayList<>();
        listAllDNASequences(listOfCodonLists, "", container, 0);
        return container;
    }
    
    private static void listAllDNASequences(List<List<String>> listOfCodonLists,
                                            String currentCodonList,
                                            List<String> container,
                                            int currentIndex) {
        if (currentIndex == listOfCodonLists.size()) {
            container.add(currentCodonList);
            return;
        }
        
        List<String> codonList = listOfCodonLists.get(currentIndex);
        
        for (String codon : codonList) {
            String nextCodonList = currentCodonList + codon;
            listAllDNASequences(listOfCodonLists,
                                nextCodonList, 
                                container,
                                currentIndex + 1);
        }
    }
    
    public static void main(String[] args) {
        List<String> strings = listAllDNASequences("LNI");
        int size = strings.size();
        int lineNumberWidth = Utils.getIntegerStringLength(size);
        
        for (int lineNumber = 1, i = 0; lineNumber <= size; lineNumber++, i++) {
            System.out.printf(
                    "%" + lineNumberWidth + "d: %s\n",
                    lineNumber,
                    strings.get(i));
        }
    }
}
