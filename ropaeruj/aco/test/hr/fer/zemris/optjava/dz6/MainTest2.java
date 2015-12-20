package hr.fer.zemris.optjava.dz6;

import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by ivan on 11/15/15.
 */
public class MainTest2 {
    @Test
    public void testTSP() throws Exception {
        List<String> strings = Files.readAllLines(Paths.get("data/bookmarks.kml")).stream().map(String::trim).filter(x -> x.trim().startsWith("<coordinates>")).map(x -> x.substring(x.indexOf(">") + 1, x.indexOf("</")).replaceAll(","," ")).filter(x->{
            String[] s = x.split(" ");
            return Double.valueOf(s[0]) >=16.33161103 && Double.valueOf(s[0])<16.4;
        }).collect(Collectors.toList());
        List<String> collect = IntStream.range(0, strings.size()).mapToObj(i -> i + " " + strings.get(i)).collect(Collectors.toList());

        String tspFile = "data/bookmarks.tsp";
        Files.write(Paths.get(tspFile), collect, Charset.defaultCharset());

        //Main.run(tspFile, 15, 100, 1000, Main.DEFAULT_ALPHA, Main.DEFAULT_BETA, Main.DEFAULT_A);
    }

}
