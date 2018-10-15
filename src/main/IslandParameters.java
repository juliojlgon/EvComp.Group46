package main;

import java.util.ArrayList;
import java.util.List;

public class IslandParameters
{
    private static final int individual_dimension = 10;
    public static final double LearningRate_Global_PropFactor = 1 / Math.sqrt(individual_dimension);
    private static final int bestTournamentSize = 2;
    private static final double bestMutationChance = .95;
    private static final double bestLearningRate = 0.6;
    private static final double bestCrossoverChance = 0.65;

    public int TournamentSize;
    public int ElitistSurvivors;
    public double MutationChance;
    public double CrossoverChance;
    public double LearningRate;
    public int MigrationCount = 3;

    public IslandParameters(int tournamentSize, double mutationChance, int elitistSurvivors, double crossoverChance, double learningRate)
    {
        this.TournamentSize = bestTournamentSize;
        this.MutationChance = bestMutationChance;
        this.CrossoverChance = bestCrossoverChance;
        this.ElitistSurvivors = 1;
        this.LearningRate = learningRate * LearningRate_Global_PropFactor;
    }

    public static IslandParameters GetBestIsland(){
        int tournamentSize = bestTournamentSize;
        double mutationChance = bestMutationChance;
        double crossoverChance = bestCrossoverChance;
        int elitistSurvivors = 1;
        double learningRate = bestLearningRate * LearningRate_Global_PropFactor;


        return new IslandParameters(tournamentSize, mutationChance, elitistSurvivors, crossoverChance, learningRate);
    }

    public List<String> Log()
    {
        List<String> log = new ArrayList<>();

        log.add(Integer.toString(TournamentSize));
        log.add(Integer.toString(ElitistSurvivors));
        log.add(Double.toString(MutationChance));
        log.add(Double.toString(CrossoverChance));
        log.add(Double.toString(LearningRate));

        return log;
    }

    public static List<String> getHeaderLog()
    {
        List<String> header = new ArrayList<>();

        header.add("TournamentSize");
        header.add("ElitistSurvivors");
        header.add("MutationChance");
        header.add("CrossoverChance");
        header.add("LearningRate");

        return header;
    }
}
