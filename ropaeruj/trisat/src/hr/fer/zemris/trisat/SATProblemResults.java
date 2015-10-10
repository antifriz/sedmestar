package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by ivan on 10/10/15.
 */
public class SATProblemResults {
    private Collection<BitVector> mSatisfiableList = new ArrayList<>();

    public Collection<BitVector> getSatisfiableList() {
        return Collections.unmodifiableCollection(mSatisfiableList);
    }

    public void addResult(BitVector result){
        mSatisfiableList.add(result);
        System.out.println("Added result: "+result);
    }
}
