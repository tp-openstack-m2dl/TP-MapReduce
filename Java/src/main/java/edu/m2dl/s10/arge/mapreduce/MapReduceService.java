package edu.m2dl.s10.arge.mapreduce;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.*;

/**
 * Created by benoit on 01/04/2016.
 */
public class MapReduceService {

    /**
     *
     * @param l
     * @param n
     * @return
     */
    private List<List<String>> chunks(List<String> l, int n) {
        return Lists.partition(l, n);
    }

    /**
     *
     * @param mapResult
     * @return
     */
    private Map<String, List<TermFrequence>> partition(List<List<TermFrequence>> mapResult) {
        List<TermFrequence> flattenResults = new ArrayList<TermFrequence>();
        for (List<TermFrequence> l: mapResult) {
            flattenResults.addAll(l);
        }

        Map<String, List<TermFrequence>> result = new HashMap<String, List<TermFrequence>>();
        for (TermFrequence f: flattenResults) {
            if (result.containsKey(f.word) == false) {
                result.put(f.word, new ArrayList<TermFrequence>());
            }
            result.get(f.word).add(f);
        }

        return result;
    }

    /**
     * 
     * @param map
     * @param reduce
     * @param text
     * @return
     */
    public List<TermFrequence> mapReduce(IMap map, IReduce reduce, String text) {

    }

    //
    //  SINGLETON
    //

    private static final MapReduceService __instance = new MapReduceService();

    private MapReduceService() {}

    public static MapReduceService getInstance() {
        return __instance;
    }
}
