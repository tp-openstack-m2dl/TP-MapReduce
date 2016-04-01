package edu.m2dl.s10.arge.mapreduce.impl;

import edu.m2dl.s10.arge.mapreduce.IMap;
import edu.m2dl.s10.arge.mapreduce.TermFrequence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benoit on 01/04/2016.
 */
public class MapWord implements IMap {
    public List<TermFrequence> map(List<String> textChunk) {
        List<TermFrequence> r = new ArrayList<TermFrequence>();
        for (String chunkPart: textChunk) {
            final TermFrequence f = new TermFrequence();
            f.word = chunkPart;
            f.frequence = 1;
            r.add(f);
        }
        return r;
    }
}
