package hr.fer.zemris.trisat;

/**
 * Created by ivan on 10/9/15.
 *
 * {@inheritDoc}
 *
 * BitVector implementation with mutable vector
 */
public class MutableBitVector extends BitVector {

    public MutableBitVector(boolean... bits) {
        super(bits);
    }

    public MutableBitVector(int n) {
        super(n);
    }

    public MutableBitVector set(int index, boolean value) {
        mUnderlayingArray[index] = value;
        return this;
    }

    public MutableBitVector toggle(int index) {
        mUnderlayingArray[index] ^= true;
        return this;
    }
}
