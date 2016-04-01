package edu.m2dl.s10.arge.mapreduce;

import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Runnables;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

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
    public Map<String, Integer> mapReduce(final IMap map, final IReduce reduce, List<String> text) throws ExecutionException, InterruptedException {
        int l = Math.min(text.size(), 8);
        List<List<String>> partitionnedText =  chunks(text, text.size() / l);

        List<Callable<List<TermFrequence>>> mapRunnables = new ArrayList<Callable<List<TermFrequence>>>();
        for (final List<String> textChunk : partitionnedText) {
            mapRunnables.add(
                new Callable<List<TermFrequence>>() {
                    public List<TermFrequence> call() throws Exception {
                        return map.map(textChunk);
                    }
                }
            );
        }
        ForkJoinPool pool = new ForkJoinPool(l);
        final List<Future<List<TermFrequence>>> futures = pool.invokeAll(mapRunnables);
        List<List<TermFrequence>> mapResults = new ArrayList<List<TermFrequence>>();
        for (Future<List<TermFrequence>> r: futures) {
            mapResults.add(r.get());
        }


        final Map<String, List<TermFrequence>> partition = partition(mapResults);
        List<Callable<TermFrequence>> reduceRunnables = new ArrayList<Callable<TermFrequence>>();
        for (final String token: partition.keySet()) {
            reduceRunnables.add(new Callable<TermFrequence>() {
                                 public TermFrequence call() throws Exception {
                                     return reduce.reduce(partition.get(token));
                                 }
                             });
        }
        final List<Future<TermFrequence>> reduceFutures = pool.invokeAll(reduceRunnables);
        List<TermFrequence> reduceResults = new ArrayList<TermFrequence>();
        for (Future<TermFrequence> r: reduceFutures) {
            reduceResults.add(r.get());
        }


        // Convertit les tuples en Map
        Map<String, Integer> result = new HashMap<String, Integer>(reduceResults.size());
        for (TermFrequence f: reduceResults) {
            result.put(f.word, f.frequence);
        }
        return result;
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
