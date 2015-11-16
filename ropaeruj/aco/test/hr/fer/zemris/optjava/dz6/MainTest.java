package hr.fer.zemris.optjava.dz6;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by ivan on 11/15/15.
 */
@RunWith(Parameterized.class)
public class MainTest {

    public static Collection<String> allowed = Arrays.asList("bays29", "att48", "ch150", "pr2392");
    private String mFileName;
    private Double mResult;

    public MainTest(String fileName, Double result) {
        mResult = result;
        mFileName = "data/" + fileName + ".tsp";
    }


    @Test
    public void testTSP() throws Exception {
        assertEquals(mResult, Main.run(mFileName, 15, 100, 1000, Main.DEFAULT_ALPHA, Main.DEFAULT_BETA, Main.DEFAULT_A), Math.pow(10, -6));
    }

    @Parameterized.Parameters(name = "TSP {0}")
    public static Iterable<Object[]> data() throws IOException {
        Map<String, Double> resultMap = new HashMap<>();

        Files.lines(new File("data/results.txt").toPath()).forEach(x -> {
            String[] splitted = x.trim().split(" : ");
            resultMap.put(splitted[0], Double.valueOf(splitted[1]));
        });

        File folder = new File("data");
        File[] files = folder.listFiles();
        assert files != null;

        List<Object[]> str = new ArrayList<>();
        for (final File fileEntry : files) {
            if (!fileEntry.isDirectory()) {
                String name = fileEntry.getName();
                if (name.endsWith(".tsp")) {
                    name = name.substring(0, name.length() - 4);
                    if (allowed.contains(name)) {
                        if (Files.lines(fileEntry.toPath()).filter(x -> x.startsWith("EDGE_WEIGHT_TYPE")).limit(1).allMatch(x -> x.trim().endsWith("EUC_2D"))) {
                            str.add(new Object[]{name, resultMap.get(name)});
                        }
                    }
                }
            }
        }
        return str;
    }

}
