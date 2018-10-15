package main;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

public class MigrationPolicies
{
    private static Random rand;


    public static Population Migrate_RandomPool(Population population, int island_count)
    {
        List<Individual> pool = new ArrayList<>();

        for (Island island: population.Islands){
            List<Individual> migrants = Operators.TournamentSelect(island.IslandPopulation, 1, island.IslandParameters.MigrationCount);
            island.IslandPopulation.removeAll(migrants);
            pool.addAll(migrants);
        }

        for (Individual ind: pool)
        {
            int destinationIsland;
            do{
                destinationIsland = rand.nextInt(island_count);
            } while(population.Islands.get(destinationIsland).IslandPopulation.size() == 25);

            population.Islands.get(destinationIsland).IslandPopulation.add(ind);
        }

        population.Epoch++;
        return population;
    }

    public static Population Migrate_MaxDistance(Population population, int island_count) {
        List<Individual> pool = new ArrayList<>();

        for (Island island : population.Islands) {
            List<Individual> migrants = Operators.TournamentSelect(island.IslandPopulation, 1, island.IslandParameters.MigrationCount);
            island.IslandPopulation.removeAll(migrants);
            pool.addAll(migrants);
        }

        List<double[]> islandCentroids = population.getIslandCentroids();

        for (Individual ind : pool) {
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
            } while (population.Islands.get(destinationIsland).IslandPopulation.size() == 25);

            population.Islands.get(destinationIsland).IslandPopulation.add(ind);
        }

        population.Epoch++;
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


    public static Population Migrate_Ring(Population population, int island_count)
    {
        List<Individual> pool = new ArrayList<>();

        for (Island island: population.Islands){
            List<Individual> migrants = Operators.TournamentSelect(island.IslandPopulation, 1, island.IslandParameters.MigrationCount);
            island.IslandPopulation.removeAll(migrants);
            pool.addAll(migrants);
        }

        for (int i = 0; i < pool.size(); i++)
        {
            int migrationIsland = ((i/3) + 1) % 4;
            population.Islands.get(migrationIsland).IslandPopulation.add(pool.get(i));
        }

        population.Epoch++;
        return population;
    }

    public static void SetRandom(Random rnd_)
    {
        rand = rnd_;
    }
}
