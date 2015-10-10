package hr.fer.zemris.trisat;

/**
 * Created by ivan on 10/10/15.
 */
public class AlgorithmFactory {
    public static Algorithm createFromTag(String arg) {
        try {
            Class<Algorithm> clazz = (Class<Algorithm>) Class.forName("hr.fer.zemris.trisat.Algorithm" + arg);
            return (Algorithm) clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }
}
