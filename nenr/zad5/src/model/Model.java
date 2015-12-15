package model;

import ann.BackpropAlg;
import ann.FFANN;
import ann.SigmoidTransferFunction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ivan on 12/12/15.
 */
public class Model {

    public static final String SUFFIX = ".char";
    public static final String ROOT_PATH = "res";
    private List<CharacterObject> mCharacters;

    public Model() {
        mCharacters = new ArrayList<>();
    }


    public void onPointsCollected(Clazz canvasClazz, int[] xes, int[] yes) {
        CharacterObject character = new CharacterObject(canvasClazz, xes, yes);
        mCharacters.add(character);
        try {
            character.toFile(Paths.get(ROOT_PATH,canvasClazz.name(),Long.toHexString(System.currentTimeMillis())+SUFFIX).toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onUndo() {
        if (mCharacters.size() > 0) {
            mCharacters.remove(mCharacters.size() - 1).deleteFromDisk();
        }
    }

    public void onTrainSelected() {
        if(mCharacters.size()>0) {
            int featureSize = 20;
            FFANN ffann = FFANN.create(new int[]{featureSize, 6, Clazz.values().length}, new SigmoidTransferFunction());

            List<double[][]> rawDataset = mCharacters.stream().map(x -> {
                double[] output = new double[Clazz.values().length];
                output[x.getClazzId()] = 1;
                return new double[][]{x.getFeatures(featureSize), output};
            }).collect(Collectors.toList());

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < rawDataset.get(0)[0].length / 2; i++) {
                sb.append(rawDataset.get(0)[0][i]);
                sb.append(",");
                sb.append(rawDataset.get(0)[0][i*2]);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length()-1);
            System.out.println(sb);
            rawDataset.stream().forEach(x-> System.out.println(Arrays.toString(x[0])+" -> " + Arrays.toString(x[1])));

            BatchReadOnlyDataset batchReadOnlyDataset = new BatchReadOnlyDataset(rawDataset, 1);

            BackpropAlg alg = new BackpropAlg(ffann, batchReadOnlyDataset);
            double[] weights = alg.run(0.01, 100000);
            System.out.println(Arrays.toString(weights));
        }
    }

    public void onTestSelected() {

    }

    public String getDatasetInfo() {
        int[] count = new int[Clazz.values().length];
        for (CharacterObject c : mCharacters) {
            count[c.getClazzId()]++;
        }
        StringBuilder sb = new StringBuilder();
        for (Clazz c : Clazz.values()) {
            sb.append(c.name()).append("[").append(count[c.ordinal()]).append("]\n");
        }
        return sb.toString();
    }

    public void preloadFromDisk() throws IOException {
        String root = ROOT_PATH;
        for (Clazz c:Clazz.values()) {
            Path dir = Paths.get(root,c.name());
            if(!Files.exists(dir)){
                Files.createDirectory(dir);
            } else{
                File[] files = dir.toFile().listFiles((dir1, name) -> {
                    return name.endsWith(SUFFIX);
                });

                for(File file : files){
                    mCharacters.add(CharacterObject.fromFile(file));
                }
            }
        }
    }
}
