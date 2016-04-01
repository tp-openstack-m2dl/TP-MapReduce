package edu.m2dl.s10.arge.mapreduce;

import java.util.List;
import java.util.Map;

/**
 * Created by benoit on 01/04/2016.
 */
public interface IReduce {
    TermFrequence reduce(List<TermFrequence> frequences);
}
