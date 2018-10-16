package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genotype {
    public static final int GenotypeLength = 10;
    private static Random rand;

    public double[] Values;
    public double[] MutationStepSize;
    public double[] MigrationPreference;

    public Genotype(double[] values, double[] mutationSizes, double[] migrationPreference){
        this.Values = Bounded(values);
        this.MutationStepSize = mutationSizes;
        this.MigrationPreference = migrationPreference;
    }

    public static void SetRandom(Random rnd_)
    {
        rand = rnd_;
    }

    private double[] Bounded(double[] values)
    {
        for (int i = 0; i < 10; i++) {
            values[i] = Bounded(values[i]);
        }

        return values;
    }

    private double Bounded(double value)
    {
        return value < -5 ?
                -5 :
                value > 5 ?
                        5 :
                        value;
    }

    public static Genotype CreateRandom() {
        double[] values = new double[GenotypeLength];
        double[] stepSize = new double[GenotypeLength];
        double[] migrationPreference = new double[]{1/3., 1/3., 1/3.};

        for (int i = 0; i < GenotypeLength; i++) {
            values[i] = (rand.nextDouble() - 0.5) * 10;
            stepSize[i] = rand.nextDouble() + 0.5;
        }

        return new Genotype(values, stepSize, migrationPreference);
    }

    public List<String> Log()
    {
        List<String> log = new ArrayList<>();

        for (int i = 0; i < GenotypeLength; i++) {
            log.add(Double.toString(Values[i]));
        }
        for (int i = 0; i < GenotypeLength; i++) {
            log.add(Double.toString(MutationStepSize[i]));
        }
        for (int i = 0; i < 3; i++) {
            log.add(Double.toString(MigrationPreference[i]));
        }

        return log;
    }

    public static List<String> getLogHeader()
    {
        List<String> header = new ArrayList<>();

        for (int i = 0; i < GenotypeLength; i++) {
            header.add("X_" + i);
        }
        for (int i = 0; i < GenotypeLength; i++) {
            header.add("MutStepSize_" + i);
        }
        header.add("MigPref_Random");
        header.add("MigPref_Ring");
        header.add("MigPref_Distance");

        return header;
    }
}
