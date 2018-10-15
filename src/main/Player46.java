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
	private static final int number_of_runs = 100;
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

	public static void main(String args[]) throws IOException, InterruptedException {
		ProcessBuilder term = new ProcessBuilder("/bin/bash");
		Process p = term.start();

		InputStream is = p.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader processOutput = new BufferedReader(isr);
		BufferedWriter processInput = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

		Logger log = new Logger("AdaptiveMigration");
		List<String> logHeader = new ArrayList<>();
		logHeader.add("RunNumber.Seed");
		logHeader.add("Score");
		log.AddRow(logHeader);

		String function = "SchaffersEvaluation";
		String output;

		String[] policies = new String[]{"Random", "Ring", "Distance"};

		double avgScore = 0;
		for (int run = 0; run < number_of_runs; run++) {
			String currentDir = System.getProperty("user.dir");

			String command = String.format("java -Djava.library.path=%s/files -Dfile.encoding=UTF-8 -jar %s/files/testrun.jar -submission=main.Player46 -evaluation=%s -nosec -seed=%d",
					currentDir,
					currentDir,
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

					List<String> result = new ArrayList<>();
					result.add(Integer.toString(run));
					result.add(Double.toString(score));
					log.AddRow(result);
					log.Print(result);
				}
				linesRead++;
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
		Parameters parameters = new Parameters();// GetParameters(); //Parameters.GetBestIsland();
		Logger islandLog = new Logger("IslandEvolution" + sdf.format(new Timestamp(System.currentTimeMillis())));

		Population population = Population
				.Create(population_size, island_count, parameters);

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
			//population.Parameters.LearningRate *= 0.8;
			epochs = 0;
		}
	}

	private Parameters GetParameters()
	{
		//int tournamentSize = Integer.parseInt(System.getProperty(("ts")));
		//double crossoverChance = Double.valueOf(System.getProperty(("cc")));
		//double mutationChance = Double.valueOf(System.getProperty("mc"));
		//double learningRate = Double.valueOf(System.getProperty("lr"));
		String migrationPolicy = System.getProperty("mp");
		int migrantCount = Integer.valueOf(System.getProperty("mn"));

		return new Parameters();
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
