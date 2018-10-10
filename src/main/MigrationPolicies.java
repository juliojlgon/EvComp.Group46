package main;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MigrationPolicies
{
    private static Random rand;

    public static Population Migrate(Population population, int island_count)
    {
        List<Island> Islands = population.Islands;
        int migrationCount = 3;

        List<Individual> island0to1 = Operators.TournamentSelect(Islands.get(0).IslandPopulation.subList(13, 25), 1, migrationCount);
        Islands.get(0).IslandPopulation.removeAll(island0to1);

        List<Individual> island1to2 = Operators.TournamentSelect(Islands.get(1).IslandPopulation.subList(13, 25), 1, migrationCount);
        Islands.get(1).IslandPopulation.removeAll(island1to2);

        List<Individual> island1to0 = Operators.TournamentSelect(Islands.get(1).IslandPopulation.subList(0, 13), 1, migrationCount);
        Islands.get(1).IslandPopulation.removeAll(island1to0);

        List<Individual> island2to3 = Operators.TournamentSelect(Islands.get(2).IslandPopulation.subList(13, 25), 1, migrationCount);
        Islands.get(2).IslandPopulation.removeAll(island2to3);

        List<Individual> island2to1 = Operators.TournamentSelect(Islands.get(2).IslandPopulation.subList(0, 13), 1, migrationCount);
        Islands.get(2).IslandPopulation.removeAll(island2to1);

        List<Individual> island3to2 = Operators.TournamentSelect(Islands.get(3).IslandPopulation.subList(0, 13), 1, migrationCount);
        Islands.get(3).IslandPopulation.removeAll(island3to2);

        Islands.get(0).IslandPopulation.addAll(island1to0);
        Islands.get(1).IslandPopulation.addAll(island0to1);
        Islands.get(1).IslandPopulation.addAll(island2to1);
        Islands.get(2).IslandPopulation.addAll(island1to2);
        Islands.get(2).IslandPopulation.addAll(island3to2);
        Islands.get(3).IslandPopulation.addAll(island2to3);

        for (int i = 0; i < island_count; i++) {
            Collections.sort(Islands.get(i).IslandPopulation, Individual.Comparator);
        }


        population.Epoch++;
        population.Islands = Islands;
        return population;
    }

    public static void SetRandom(Random rnd_)
    {
        rand = rnd_;
    }
}
