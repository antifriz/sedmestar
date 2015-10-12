package hr.fer.zemris.trisat;

/**
 * Created by ivan on 10/10/15.
 * Simple factory that creates algorithm objects used to solve 3-SAT problems
 */
public class AlgorithmFactory {
    /**
     * @param tag tag related to certain algorithm
     * @return algorithm class
     */
    public static Algorithm createFromTag(String tag) {
        try {
            Class<Algorithm> clazz = (Class<Algorithm>) Class.forName("hr.fer.zemris.trisat.Algorithm" + tag);
            return clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }
}
