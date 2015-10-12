package hr.fer.zemris.trisat;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by ivan on 10/10/15.
 *
 * Generator that generates neighbourhood for given BitVector
 */
public class BitVectorNGenerator implements Iterable<MutableBitVector> {
    private final BitVector mAssignment;

    public BitVectorNGenerator(BitVector assignment){
        mAssignment = assignment;
    }


    @Override
    public Iterator<MutableBitVector> iterator() {
        return new Iterator<MutableBitVector>() {
            int idx = 0;

            @Override
            public boolean hasNext() {
                return idx<mAssignment.getSize();
            }

            @Override
            public MutableBitVector next() {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                return mAssignment.copy().toggle(idx++);
            }
        };
    }

    public MutableBitVector[] createNeighbourhood(){
        MutableBitVector[] mutableBitVectors = new MutableBitVector[mAssignment.getSize()];
        int i = 0;
        for(MutableBitVector mutableBitVector : this){
            mutableBitVectors[i++] = mutableBitVector;
        }
        return mutableBitVectors;
    }
}
