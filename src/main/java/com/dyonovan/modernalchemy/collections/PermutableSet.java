package com.dyonovan.modernalchemy.collections;

import java.util.*;

public class PermutableSet<E> {
    private static final int ELEMENT_LIMIT = 12;
    private List<E> inputList;
    public int N;
    private Map<Integer, List<List<E>>> map =
            new HashMap<Integer, List<List<E>>>();

    public PermutableSet(List<E> list) {
        inputList = list;
        N = list.size();
        if (N > ELEMENT_LIMIT) {
            throw new RuntimeException(
                    "List with more then " + ELEMENT_LIMIT + " elements is too long...");
        }
    }

    public List<List<E>> getPermutationsList(int elementCount) {
        if (elementCount < 1 || elementCount > N) {
            throw new IndexOutOfBoundsException(
                    "Can only generate permutations for a count between 1 to " + N);
        }
        if (map.containsKey(elementCount)) {
            return map.get(elementCount);
        }

        ArrayList<List<E>> list = new ArrayList<List<E>>();

        if (elementCount == N) {
            list.add(new ArrayList<E>(inputList));
        } else if (elementCount == 1) {
            for (int i = 0 ; i < N ; i++) {
                List<E> set = new ArrayList<E>();
                set.add(inputList.get(i));
                list.add(set);
            }
        } else {
            list = new ArrayList<List<E>>();
            for (int i = 0 ; i <= N - elementCount ; i++) {
                @SuppressWarnings("unchecked")
                ArrayList<E> subList = (ArrayList<E>)((ArrayList<E>)inputList).clone();
                for (int j = i ; j >= 0 ; j--) {
                    subList.remove(j);
                }
                PermutableSet<E> subPowerSet =
                        new PermutableSet<E>(subList);

                List<List<E>> pList =
                        subPowerSet.getPermutationsList(elementCount-1);
                for (List<E> s : pList) {
                    List<E> set = new ArrayList<E>();
                    set.add(inputList.get(i));
                    set.addAll(s);
                    list.add(set);
                }
            }
        }
        map.put(elementCount, list);
        return map.get(elementCount);
    }
}
