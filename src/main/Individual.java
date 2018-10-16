package main;

import org.vu.contest.ContestEvaluation;

import java.util.*;

public class Individual {
    public Genotype Genes;
    public double Fitness;

    public static Comparator<Individual> Comparator = new SortByFitness();
    private static Random rand;
    private static ContestEvaluation eval;

    public Individual(Genotype genotype){
        this.Genes = genotype;
        this.Fitness = (double)eval.evaluate(genotype.Values);
    }

    public static Individual CreateRandom(){
        Genotype genotype = Genotype.CreateRandom();

        return new Individual(genotype);
    }

    public static void SetRandom(Random rnd_)
    {
        rand = rnd_;
    }

    public static void SetEvaluation(ContestEvaluation evaluation_)
    {
        eval = evaluation_;
    }

    public Individual Mutate(Parameters parameters)
    {
        Genotype mutatedGenotype = Operators.Mutate(this.Genes, parameters);

        return new Individual(mutatedGenotype);
    }

    public List<String> Log()
    {
        List<String> log = new ArrayList<>(Genes.Log());

        log.add(Double.toString(Fitness));

        return log;
    }

    public static List<String> getLogHeader()
    {
        List<String> header = new ArrayList<>();

        header.addAll(Genotype.getLogHeader());
        header.add("Fitness");

        return header;
    }

    public void MutateMigrationProbabilities()
    {
        double[] migrationPreference = new double[3];
        double preferenceSum = 0;

        for (int i = 0; i < 3; i++) {
            double newPreference = Genes.MigrationPreference[i] + Genes.MigrationPreference[i] * rand.nextGaussian() / 2;
            //double newPreference = Genes.MigrationPreference[i] * Math.exp(0.3 * rand.nextGaussian());
            migrationPreference[i] = newPreference < 0 ? 0 : newPreference;
            preferenceSum += migrationPreference[i];
        }

        for (int i = 0; i < 3; i++) {
            migrationPreference[i] /= preferenceSum;
        }

        this.Genes.MigrationPreference = migrationPreference;
    }

    static class SortByFitness implements Comparator<Individual>{

        @Override
        public int compare(Individual first, Individual second) {
            return -Double.compare(first.Fitness, second.Fitness);
        }
    }
}
