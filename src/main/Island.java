package main;

import org.vu.contest.ContestEvaluation;

import java.util.*;

public class Island {

    public int IslandIndex;
    public int Generation;
    public List<Individual> IslandPopulation;
    public Parameters Parameters;

    private static Random rand;

    public Island(int islandIndex, List<Individual> island, Parameters parameters)
    {
        this.IslandIndex = islandIndex;
        this.Generation = 1;
        this.IslandPopulation = island;
        this.Parameters = parameters;
    }

    public static void SetRandom(Random rnd_)
    {
        rand = rnd_;
    }

    public void Evolve(ContestEvaluation eval)
    {
        List<Individual> children = MakeChildren(eval);

        List<Individual> pool = new ArrayList<>(IslandPopulation);
        pool.addAll(children);
        Collections.sort(pool, Individual.Comparator);

        List<Individual> elites = Parameters.ElitistSurvivors != 0 ?
                pool.subList(0, Parameters.ElitistSurvivors) :
                new ArrayList<>();

        List<Individual> survivors = Operators.TournamentSelect(pool.subList(Parameters.ElitistSurvivors, pool.size()), Parameters.TournamentSize, IslandPopulation.size() - elites.size());
        survivors.addAll(elites);
        Collections.sort(survivors, Individual.Comparator);

        this.IslandPopulation = survivors;
        this.Generation++;
    }

    private List<Individual> MakeChildren(ContestEvaluation eval)
    {
        List<Individual> allChildren = new ArrayList<>();
        while(allChildren.size() < (IslandPopulation.size()))
        {

            Individual mom = Operators.TournamentSelect(IslandPopulation, Parameters.TournamentSize,1).get(0);
            Individual dad;
            do {
                dad = Operators.TournamentSelect(IslandPopulation, Parameters.TournamentSize,1).get(0);
            } while(mom.equals(dad));

            double crossoverDiceRoll = rand.nextDouble();
            List<Individual> newChildren = new ArrayList<>();

            if(crossoverDiceRoll < Parameters.CrossoverChance)
            { //do whole arithmetic xover
                newChildren = Operators.BlendXover(mom, dad);
            }

            double mutationDiceRoll = rand.nextDouble();

            if(mutationDiceRoll < Parameters.MutationChance)
            {
                Individual mutantChild = mom.Mutate(Parameters);
                newChildren.add(mutantChild);
            }

            mutationDiceRoll = rand.nextDouble();

            if(mutationDiceRoll < Parameters.MutationChance)
            {
                Individual mutantChild = dad.Mutate(Parameters);
                newChildren.add(mutantChild);
            }

            allChildren.addAll(newChildren);
        }

        return allChildren;
    }

    public List<String[]> Log(String epoch)
    {
        List<String[]> log = new ArrayList<>();
        List<String> metaIslandLog = GetIslandMeta();

        for (Individual ind: IslandPopulation)
        {
            List<String> indLog = ind.Log();
            indLog.addAll(metaIslandLog);
            indLog.add(epoch);

            log.add(indLog.toArray(new String[0]));
        }

        return log;
    }

    public List<String> GetIslandMeta()
    {
        List<String> islandMeta = new ArrayList<>();

        islandMeta.add(Integer.toString(IslandIndex));
        islandMeta.add(Integer.toString(Generation));
        islandMeta.addAll(Parameters.Log());

        return islandMeta;
    }

    public static List<String> getLogHeader()
    {
        List<String> header = new ArrayList<>();

        header.addAll(Individual.getLogHeader());
        header.add("IslandIndex");
        header.add("Generation");
        header.addAll(main.Parameters.getHeaderLog());

        return header;
    }

    public double[] getCentroid()
    {
        double[] centroid = new double[10];

        for (Individual ind: IslandPopulation)
        {
            for (int i = 0; i < 10; i++)
            {
                centroid[i] += ind.Genes.Values[i] / IslandPopulation.size();
            }
        }

        return centroid;
    }
}
