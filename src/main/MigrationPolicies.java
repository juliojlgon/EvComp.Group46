package main;

import java.util.*;

import static java.util.Map.Entry.comparingByValue;

public class MigrationPolicies
{
    private static Random rand;


    public static Population Migrate(Population population, int island_count)
    {
        List<Individual> migrationPool = ExtractMigrationPool(population, island_count);

        for (int i = 0; i < migrationPool.size(); i++)
        {
            Individual ind = migrationPool.get(i);
            double diceRoll = rand.nextDouble();

            if(diceRoll < ind.Genes.MigrationPreference[0])
                population = Migrate_RandomPool(ind, population, island_count);
            else if (diceRoll < ind.Genes.MigrationPreference[0] + ind.Genes.MigrationPreference[1])
                population = Migrate_Ring(ind, population, island_count, i/population.Parameters.MigrationCount);
            else
                population = Migrate_MaxDistance(ind, population, island_count);
        }

        return population;
    }

    private static List<Individual> ExtractMigrationPool(Population population, int island_count)
    {
        List<Individual> pool = new ArrayList<>();

        for (Island island: population.Islands){
            List<Individual> migrants = Operators.TournamentSelect(island.IslandPopulation, 1, island.Parameters.MigrationCount);
            island.IslandPopulation.removeAll(migrants);
            pool.addAll(migrants);
        }

        return pool;
    }

    private static Population Migrate_RandomPool(Individual ind, Population population, int island_count)
    {
        int destinationIsland;
        do{
            destinationIsland = rand.nextInt(island_count);
        } while(population.Islands.get(destinationIsland).IslandPopulation.size() >= 25);

        population.Islands.get(destinationIsland).IslandPopulation.add(ind);

        return population;
    }

    private static Population Migrate_MaxDistance(Individual ind, Population population, int island_count)
    {
        List<double[]> islandCentroids = population.getIslandCentroids();

        Map<Integer, Double> distances = new HashMap<>();

        for (int i = 0; i < island_count; i++) {
            double distance = ComputeDistance(ind.Genes.Values, islandCentroids.get(i));
            distances.put(i, distance);
        }

        List<Map.Entry<Integer, Double>> distanceList =
                new LinkedList<>(distances.entrySet());
        Collections.sort(distanceList, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> e1, Map.Entry<Integer, Double> e2) {
                return (e2.getValue().compareTo(e1.getValue()));
            }
        });

        List<Integer> sorted = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry: distanceList) {
            sorted.add(entry.getKey());
        }

        int distanceIndex = 0;
        int destinationIsland;

        do {
            destinationIsland = sorted.get(distanceIndex++);
        } while (population.Islands.get(destinationIsland).IslandPopulation.size() >= 25);

        population.Islands.get(destinationIsland).IslandPopulation.add(ind);

        return population;
    }

    private static double ComputeDistance(double[] a, double[] b)
    {
        double distance = 0;

        for (int i = 0; i < 10; i++)
        {
            distance += Math.pow(a[i] - b[i], 2);
        }

        return Math.sqrt(distance);
    }

    private static Population Migrate_Ring(Individual ind, Population population, int island_count, int originalIsland)
    {
        int migrationIsland = ((originalIsland) + 1) % 4;
        population.Islands.get(migrationIsland).IslandPopulation.add(ind);

        return population;
    }

    public static void SetRandom(Random rnd_)
    {
        rand = rnd_;
    }

}
