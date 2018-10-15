package main;

import java.util.*;

public class Operators {

    private static Random rand;

    public static List<Individual> TournamentSelect(List<Individual> competitors, int tournamentSize, int championCount)
    {
        List<Individual> champions = new ArrayList<>(championCount);
        Set<Integer> selected = new HashSet<Integer>();

        while(champions.size() < championCount)
        {
            int best = competitors.size()+1;
            for (int i = 0; i < tournamentSize; i++)
            {
                int drawn;
                do
                {
                    drawn =  rand.nextInt(competitors.size());
                } while(selected.contains(drawn));
                best = drawn < best ? drawn : best;
            }

            if(!selected.contains(best))
            {
                champions.add(competitors.get(best));
                selected.add(best);
            }
        }

        return champions;
    }

    public static List<Individual> BlendXover(Individual mom, Individual dad)
    {
        double[] genotype1 = new double[10];
        double[] genotype2 = new double[10];
        double[] mutationStepSizes1 = new double[10];
        double[] mutationStepSizes2 = new double[10];
        double[] migrationPreferences1 = new double[3];
        double[] migrationPreferences2 = new double[3];
        double bias = 0.5;

        for (int i = 0; i < 10; i++) {
            double dist = Math.abs(mom.Genes.Values[i] - dad.Genes.Values[i]);
            double msdist = Math.abs(mom.Genes.MutationStepSize[i] - dad.Genes.MutationStepSize[i]);

            double diceRoll = rand.nextDouble();
            genotype1[i] = diceRoll * dist * 2 + Math.min(mom.Genes.Values[i] - bias*dist, dad.Genes.Values[i] - bias*dist);
            mutationStepSizes1[i] = diceRoll * msdist * 2 + Math.min(mom.Genes.MutationStepSize[i] - bias*msdist, dad.Genes.MutationStepSize[i] - bias*msdist);
            diceRoll = rand.nextDouble();
            genotype2[i] = diceRoll * dist * 2 + Math.min(mom.Genes.Values[i] - bias*dist, dad.Genes.Values[i] - bias*dist);
            mutationStepSizes2[i] = diceRoll * msdist * 2 + Math.min(mom.Genes.MutationStepSize[i] - bias*msdist, dad.Genes.MutationStepSize[i] - bias*msdist);
        }

        double preferenceSum1 = 0;
        double minPreference1 = 1;
        double preferenceSum2 = 0;
        double minPreference2 = 1;

        for (int i = 0; i < 3; i++) {
            double dist = Math.abs(mom.Genes.MigrationPreference[i] - dad.Genes.MigrationPreference[i]);
            double diceRoll = rand.nextDouble();

            migrationPreferences1[i] = diceRoll * dist * 2 + Math.min(mom.Genes.MigrationPreference[i] - bias*dist, dad.Genes.MigrationPreference[i] - bias*dist);
            preferenceSum1 += migrationPreferences1[i];
            minPreference1 = migrationPreferences1[i] < minPreference1 ? migrationPreferences1[i] : minPreference1;

            diceRoll = rand.nextDouble();
            migrationPreferences2[i] = diceRoll * dist * 2 + Math.min(mom.Genes.MigrationPreference[i] - bias*dist, dad.Genes.MigrationPreference[i] - bias*dist);
            preferenceSum2 += migrationPreferences2[i];
            minPreference2 = migrationPreferences2[i] < minPreference2 ? migrationPreferences2[i] : minPreference2;
        }

        if(minPreference1 < 0){
            preferenceSum1 += Math.abs(minPreference1)*3;
        } else {minPreference1 = 0;}
        if(minPreference2 < 0){
            preferenceSum2 += Math.abs(minPreference2)*3;
        } else {minPreference2 = 0;}

        for (int i = 0; i < 3; i++) {
            migrationPreferences1[i] = (migrationPreferences1[i] + Math.abs(minPreference1)) / preferenceSum1;
            migrationPreferences2[i] = (migrationPreferences2[i] + Math.abs(minPreference2)) / preferenceSum2;
        }

        List<Individual> children = new ArrayList<>();
        children.add(new Individual(new Genotype(genotype1, mutationStepSizes1, migrationPreferences1)));
        children.add(new Individual(new Genotype(genotype2, mutationStepSizes2, migrationPreferences2)));

        return children;
    }

    public static List<Individual> AritmeticalXover(Individual mom, Individual dad)
    {
        double[] genotype1 = new double[10];
        double[] genotype2 = new double[10];
        double[] mutationStepSizes1 = new double[10];
        double[] mutationStepSizes2 = new double[10];
        double[] migrationPreferences1 = new double[3];
        double[] migrationPreferences2 = new double[3];

        for (int i = 0; i < 10; i++) {
            double bias = rand.nextDouble();
            genotype1[i] = bias * mom.Genes.Values[i] + (1 - bias) * dad.Genes.Values[i];
            genotype2[i] = (1-bias) * mom.Genes.Values[i] + bias * dad.Genes.Values[i];
            mutationStepSizes1[i] = bias * mom.Genes.MutationStepSize[i] + (1 - bias) * dad.Genes.MutationStepSize[i];
            mutationStepSizes2[i] = (1-bias) * mom.Genes.MutationStepSize[i] + bias * dad.Genes.MutationStepSize[i];
        }

        double preferenceSum1 = 0;
        double preferenceSum2 = 0;
        for (int i = 0; i < 3; i++) {
            double bias = rand.nextDouble();
            migrationPreferences1[i] = bias * mom.Genes.MigrationPreference[i] + (1 - bias) * dad.Genes.MigrationPreference[i];
            preferenceSum1 += migrationPreferences1[i];
            migrationPreferences2[i] = (1-bias) * mom.Genes.MigrationPreference[i] + bias * dad.Genes.MigrationPreference[i];
            preferenceSum2 += migrationPreferences2[i];
        }

        for (int i = 0; i < 3; i++) {
            migrationPreferences1[i] /= preferenceSum1;
            migrationPreferences2[i] /= preferenceSum2;
        }

        List<Individual> children = new ArrayList<>();
        children.add(new Individual(new Genotype(genotype1, mutationStepSizes1, migrationPreferences1)));
        children.add(new Individual(new Genotype(genotype2, mutationStepSizes2, migrationPreferences2)));

        return children;
    }

    public static Genotype Mutate(Genotype genotype, Parameters parameters)
    {
        double[] mutatedGenes = new double[10];
        double[] mutationStepSizes = new double[10];
        double[] migrationPreference = new double[3];

        double gaussianDiceRoll = rand.nextGaussian();

        for (int i = 0; i < 10; i++) {
            mutationStepSizes[i] = genotype.MutationStepSize[i] * Math.exp(parameters.LearningRate * gaussianDiceRoll);

            mutatedGenes[i] = genotype.Values[i] + mutationStepSizes[i] * rand.nextGaussian();
        }

        double preferenceSum = 0;
        for (int i = 0; i < 3; i++) {
            double newPreference = genotype.MigrationPreference[i] + genotype.MigrationPreference[i] * rand.nextGaussian() / 2;
            migrationPreference[i] = newPreference < 0 ? 0 : newPreference;
            preferenceSum += migrationPreference[i];
        }

        for (int i = 0; i < 3; i++) {
            migrationPreference[i] /= preferenceSum;
        }

        return new Genotype(mutatedGenes, mutationStepSizes, migrationPreference);
    }

    public static void SetRandom(Random rnd_)
    {
        rand = rnd_;
    }
}
