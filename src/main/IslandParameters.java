package main;

import java.util.ArrayList;
import java.util.List;

public class IslandParameters
{
    private static final int individual_dimension = 10;
    public static final double LearningRate_Global_PropFactor = 1 / Math.sqrt(individual_dimension);
    private static final int bestTournamentSize = 2;

    public int TournamentSize;
    public int ElitistSurvivors;
    public double MutationChance;
    public double CrossoverChance;
    public double LearningRate;

    public IslandParameters(int tournamentSize, double mutationChance, int elitistSurvivors, double crossoverChance, double learningRate)
    {
        this.TournamentSize = bestTournamentSize;
        this.MutationChance = mutationChance;
        this.CrossoverChance = crossoverChance;
        this.ElitistSurvivors = elitistSurvivors;
        this.LearningRate = learningRate * LearningRate_Global_PropFactor;
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
