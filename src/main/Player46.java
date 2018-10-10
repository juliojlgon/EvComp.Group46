package main;

import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Player46 implements ContestSubmission
{
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	private static final int number_of_runs = 40;
	private static final boolean WithLogging = false;

	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
	private int island_count = 4;
	private int population_size = 100;
	private int epoch_length = 20;
	public Player46()
	{
		rnd_ = new Random();
	}

	public static void main(String args[]) throws IOException {
		System.out.println("HOLO");
		ProcessBuilder term = new ProcessBuilder("/bin/bash");
		Process p = term.start();

		InputStream is = p.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader processOutput = new BufferedReader(isr);
		BufferedWriter processInput = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

		Logger log = new Logger("ParamTuning");
		List<String> logHeader = new ArrayList<>();
		logHeader.add("MutationChance");
		logHeader.add("CrossoverChance");
		logHeader.add("LearningRate");
		logHeader.add("AverageScore");
		log.AddRow(logHeader);

		String function = "SchaffersEvaluation";
		String output;

		for (double mutationChance = 0.85; mutationChance <= 0.96; mutationChance += 0.05) {
			for (double crossoverChance = 0.65; crossoverChance <= 0.76; crossoverChance += 0.05)
				for (int tournamentSize = 2; tournamentSize < 3; tournamentSize += 1)
					for (double learningRate = 0.45; learningRate <= 0.61; learningRate += 0.05)
					{
						double avgScore = 0;
						for (int run = 0; run < number_of_runs; run++) {
							String command = String.format("java -Djava.library.path=~/files -Dmc=%f -Dcc=%f -Dts=%d -Dlr=%f -Dfile.encoding=UTF-8 -jar /root/UvA/EC/EvComp.Group46/files/testrun.jar -submission=main.Player46 -evaluation=%s -nosec -seed=%d",
									mutationChance,
									crossoverChance,
									tournamentSize,
									learningRate,
									function,
									run);

							processInput.write(command);
							processInput.newLine();
							processInput.flush();

							int linesRead = 0;
							double score;

							while (linesRead < 2 && (output = processOutput.readLine()) != null) {
								if (linesRead == 0) {
									score = Double.parseDouble(output.substring(6));
									avgScore += score;
								}
								linesRead++;
							}
						}

						List<String> result = new ArrayList<>();
						result.add(Double.toString(mutationChance));
						result.add(Double.toString(crossoverChance));
						result.add(Double.toString(learningRate));
						result.add(Double.toString(avgScore / number_of_runs));
						log.AddRow(result);
						log.Print(result);
					}
		}

		processInput.write("exit");
		processInput.newLine();
		processInput.flush();

		processInput.close();
		processOutput.close();
		log.WriteLog();
	}

	public void setSeed(long seed)
	{
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;
		Individual.SetEvaluation(evaluation_);

		// Get evaluation properties
		Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }
    
	public void run()
	{
		SetRandom();
		IslandParameters islandParameters = GetIslandParameters();
		Logger islandLog = new Logger("IslandEvolution" + sdf.format(new Timestamp(System.currentTimeMillis())));

		Population population = Population
				.Create(population_size, island_count, islandParameters);

		islandLog.AddRow(population.getLogHeader());
		islandLog.AddRows(population.GetGenerationLog());

		int epochs = 1;

		LOOP:
        while(true)
        {
            while(epochs < epoch_length)
			{
				for (int island = 0; island < island_count; island++) {
					try{
						population.Islands.get(island).Evolve(evaluation_);
					}catch(Exception e){
						if(WithLogging)
							islandLog.WriteLog();
						break LOOP;
					}
				}
				islandLog.AddRows(population.GetGenerationLog());
				epochs++;
			}

			MigrationPolicies.Migrate(population, island_count);
			epochs = 0;
		}
	}

	private IslandParameters GetIslandParameters()
	{
		int tournamentSize = Integer.parseInt(System.getProperty(("ts")));
		double crossoverChance = Double.parseDouble(System.getProperty(("cc")));
		double mutationChance = Double.parseDouble(System.getProperty("mc"));
		double learningRate = Double.parseDouble(System.getProperty("lr"));
		int elites = 1;

		return new IslandParameters(tournamentSize, mutationChance, elites, crossoverChance, learningRate);
	}

	private void SetRandom()
	{
		Genotype.SetRandom(rnd_);
		Individual.SetRandom(rnd_);
		Island.SetRandom(rnd_);
		MigrationPolicies.SetRandom(rnd_);
		Operators.SetRandom(rnd_);
		Population.SetRandom(rnd_);
	}
}
