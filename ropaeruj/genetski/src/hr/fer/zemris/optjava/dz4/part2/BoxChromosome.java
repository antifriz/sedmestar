package hr.fer.zemris.optjava.dz4.part2;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ivan on 11/1/15.
 */
final class BoxChromosome extends ArrayList<BoxFragment> implements Comparable<BoxChromosome> {

    private static final double CONSTANT_K = 1;
    public static final double MUTATION_FACTOR = 0.05;
    public double mFitness;
    private int mHeight;
    private PriorityQueue<Stick> mMissing = new PriorityQueue<>(Collections.<Stick>reverseOrder());

    public BoxChromosome(int height) {
        mHeight = height;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (BoxFragment fragment : this) {
            sb.append('\u2588');
            String str = fragment.toString();
            sb.append(str);
            for (int i = str.length(); i < mHeight; i++) {
                sb.append(' ');
            }
            sb.append('\u2588');
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public void fill(List<Stick> sticks) {
        int curHeight = 0;
        add(new BoxFragment());
        for (Stick stick : sticks) {
            if (curHeight + stick.height > mHeight) {
                curHeight = 0;
                add(new BoxFragment());
            }

            curHeight += stick.height;
            get(size() - 1).add(stick);
        }
    }

    @Override
    public int compareTo(BoxChromosome o) {
        return Double.compare(mFitness, o.mFitness);
    }

    public void evaluateSelf() {
        for (BoxFragment fragment : this) {
            int sum = 0;
            for (Stick i : fragment) {
                sum += i.height;
            }
            mFitness += Math.pow(sum, CONSTANT_K);
        }
        mFitness /= Math.pow(mHeight, CONSTANT_K) * size();
    }

    public BoxChromosome duplicate() {
        BoxChromosome boxChromosome = new BoxChromosome(mHeight);
        boxChromosome.addAll(stream().map(BoxFragment::new).collect(Collectors.toList()));
        return boxChromosome;
    }

    public List<BoxFragment> rip(int crossoverStartPoint, int crossoverEndPoint) {
        mMissing.clear();
        List<BoxFragment> subList = new ArrayList<>(subList(crossoverStartPoint, crossoverEndPoint + 1));


        for (int i = crossoverEndPoint; i >= crossoverStartPoint; i--) {
            mMissing.addAll(get(i));
            remove(i);
        }

        subList.iterator();
        return subList;
    }


    public void insert(List<BoxFragment> toBeInserted, int crossoverStartPoint) {

        List<Stick> toBeInsertedFlatten = new ArrayList<>();
        toBeInserted.iterator();

        toBeInserted.forEach(toBeInsertedFlatten::addAll);

        for (BoxFragment fragment : this) {
            for (int i = 0; i < fragment.size(); ) {
                if (toBeInsertedFlatten.contains(fragment.get(i))) {
                    fragment.remove(i);
                } else {
                    i++;
                }
            }
        }
        addAll(crossoverStartPoint, toBeInserted);

        Iterator<BoxFragment> it = iterator();
        while (it.hasNext()){
            if(it.next().isEmpty()){
                it.remove();
            }
        }



        mMissing.removeAll(toBeInsertedFlatten);

        fillMissing();

    }

    private void fillMissing() {
        while (!mMissing.isEmpty()) {
            Stick stick = mMissing.poll();

            boolean fit = false;
            for (List<Stick> list : this) {
                int sum = list.stream().mapToInt(x -> x.height).sum();
                if (sum + stick.height <= mHeight) {
                    fit = true;
                    list.add(stick);
                    break;
                }
            }
            if (!fit) {
                BoxFragment sticks = new BoxFragment();
                sticks.add(stick);
                add(sticks);
            }
        }
    }

    public void mutate(Random random) {
        for (int i = 0; i < size(); ) {
            if (random.nextDouble() < MUTATION_FACTOR) {
                mMissing.addAll(get(i));
                remove(i);
            } else {
                i++;
            }
        }

        fillMissing();
    }


}
