package edu.m2dl.s10.arge.mapreduce;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by benoit on 01/04/2016.
 */
public class MapReduceService {

    private List<List<String>> chunks(List<String> l, int n) {
        return Lists.partition(l, n);
    }


    public List<TermFrequence> MapReduce()

    //
    //  SINGLETON
    //

    private static final MapReduceService __instance = new MapReduceService();

    private MapReduceService() {}

    public static MapReduceService getInstance() {
        return __instance;
    }
}
