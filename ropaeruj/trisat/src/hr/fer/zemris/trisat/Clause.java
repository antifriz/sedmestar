package hr.fer.zemris.trisat;

import java.util.Arrays;

/**
 * Created by ivan on 10/9/15.
 */
public class Clause {
    private final int[] mIndexes;

    public Clause(int[] indexes) {
        mIndexes = Arrays.copyOf(indexes, indexes.length);
    }

    public int getSize() {
        return mIndexes.length;
    }

    public int getLiteral(int index) {
        return mIndexes[index];
    }

    public boolean isSatisified(BitVector assignment) {
        return Arrays.stream(mIndexes).anyMatch((int index) -> index > 0 && assignment.get(index - 1) || index < 0 && !assignment.get(-index - 1));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(mIndexes.length*4);
        for (int index : mIndexes) {
            if(index<0){
                sb.append("~");
            }
            sb.append("X");
            sb.append(Math.abs(index));
            sb.append(" ");
        }
        return sb.toString();
    }
}
