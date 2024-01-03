package fi.helsinki.coderodde.aoga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class provides a method for generating permutations over groups of 
 * elements. If the groups are {@code <1, 2>, null, <3, 4>, <5>}, the all 
 * permutations produced by the method {@link computeGroupPermutations} are:
 * <ul>
 *  <li>{@code [1, 2] null [3, 4] [5]}</li>
 *  <li>{@code [1, 2] null [4, 3] [5]}</li>
 *  <li>{@code [2, 1] null [3, 4] [5]}</li>
 *  <li>{@code [2, 1] null [4, 3] [5]}</li>
 * </ul>.
 * <p>
 * Unfortunately, this permuter cannot "collapse" equal group permutation in
 * case some groups have equal elements. For example, {@code <null, 1, null>}
 * won't produce 
 * <ul>
 *  <li>{@code [1, null, null]}</li>
 *  <li>{@code [null, 1, null]}</li>
 *  <li>{@code [null, null, 1]},</li>
 * </ul>
 * but instead
 * <ol>
 *  <li>{@code [1, null, null]}</li>
 *  <li>{@code [1, null, null]}</li>
 *  <li>{@code [null, 1, null]}</li>
 *  <li>{@code [null, 1, null]}</li>
 *  <li>{@code [null, null, 1]}</li>
 *  <li>{@code [null, null, 1]}.</li>
 * </ol>
 * There is, however, a simple technique to mitigate the above issue: put all 
 * the group permutations into a {@link java.util.HashSet}, and then call 
 * {@link java.util.HashSet#toArray()}on the hash set in order to obtain the unique group 
 * permutations.
 * 
 * @param <T> the type of the group element.
 */
public final class MultipleGroupPermuter<T> {

    // The actual group list. Will be modified by the computeGroupPermutations 
    // method.
    private final List<List<T>> data;
    private final List<List<List<T>>> result;
    
    /**
     * Constructs this object with a given group list.
     * 
     * @param groupList the list of groups.
     */
    public MultipleGroupPermuter(List<List<T>> groupList) {
        Objects.requireNonNull(groupList, "The input group list is null.");
        this.data = groupList;
        this.result = new ArrayList<>(getNumberOfResultPermutations());
    }
    
    /**
     * Returns the list holding all the group permutations. The algorithm under
     * the hood is an extended 
     * <a href="https://en.wikipedia.org/wiki/Heap%27s_algorithm">
     * Heap's algorithm</a>.
     * 
     * @return the list of all the group permutations.
     */
    public List<List<List<T>>> computeGroupPermutations() {
        Objects.requireNonNull(data, "The input data is null.");
        
        if (data.isEmpty()) {
            return new ArrayList<>(1);
        }
        
        computeGroupPermutationsImpl(getGroupSize(data.get(0)), 0);
        return result;
    }
    
    private void computeGroupPermutationsImpl(int n, int listIndex) {
        if (listIndex == data.size() - 1) {
            if (n == 1 || n == 0) {
                result.add(deepCopyGroupPermutation());
                return;
            }
            
            if (n % 2 == 0) {
                for (int i = 0; i < n - 1; i++) {
                    computeGroupPermutationsImpl(n - 1, listIndex);
                    Collections.swap(data.get(listIndex), i, n - 1);
                }
            } else {
                for (int i = 0; i < n - 1; i++) {
                    computeGroupPermutationsImpl(n - 1, listIndex);
                    Collections.swap(data.get(listIndex), 0, n - 1);
                }
            }
            
            computeGroupPermutationsImpl(n - 1, listIndex);
        } else {
            // Here, listIndex < data.size() - 1:
            if (n == 1 || n == 0) {
                computeGroupPermutationsImpl(
                        getGroupSize(data.get(listIndex + 1)), 
                        listIndex + 1);
                return;
            }
            
            if (n % 2 == 0) {
                for (int i = 0; i < n - 1; i++) {
                    computeGroupPermutationsImpl(n - 1, listIndex);
                    Collections.swap(data.get(listIndex), i, n - 1);
                }
            } else {
                for (int i = 0; i < n - 1; i++) {
                    computeGroupPermutationsImpl(n - 1, listIndex);
                    Collections.swap(data.get(listIndex), 0, n - 1);
                }
            }
            
            computeGroupPermutationsImpl(n - 1, listIndex);
        }
    }
    
    private List<List<T>> deepCopyGroupPermutation() {
        List<List<T>> copy = new ArrayList<>(data.size());
        
        for (List<T> group : data) {
            if (group == null) {
                copy.add(null);
            } else {
                copy.add(new ArrayList<>(group));
            }
        }
        
        return copy;
    }
    
    private int getNumberOfResultPermutations() {
        int numberOfPermutations = 1;
        
        for (List<T> group : data) {
            numberOfPermutations *= factorial(getGroupSize(group));
            
            if (numberOfPermutations <= 0) {
                throw new IllegalArgumentException(
                        "The current invocation would yield too many group " + 
                                "permutations.");
            }
        }
        
        return numberOfPermutations;
    }
    
    private static int factorial(int n) {
        switch (n) {
            case 0:
            case 1:
                return 1;
                
            default:
                return n * factorial(n - 1);
        }
    }
    
    private static <T> int getGroupSize(List<T> list) {
        return list == null ? 0 : list.size();
    }
}
