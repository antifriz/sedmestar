package hr.fer.zemris.fuzzy.controller;

import hr.fer.zemris.fuzzy.sets.DomainElement;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;

import java.util.stream.StreamSupport;

/**
 * Created by ivan on 11/9/15.
 */
public class COADefuzzifier {
    static int indexOfDefuzzified(IFuzzySet set){
        int i = 0;
        double sum = 0;
        double values = 0;
        for(DomainElement element:set.getDomain()){
            double value = set.getValueAt(element);
            values += value;
            sum += value * i++;
        }
        return (int) Math.round(sum / values);
    }
}
