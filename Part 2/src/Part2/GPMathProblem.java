package Part2;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPProblem;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;

/**
 * @author Owen Nicholson - 300130635
 * @credit carlos - tutorial code supplied by lecturer
 * https://cvalcarcel.wordpress.com/2009/08/04/jgap-a-firstsimple-tutorial/
 */
public class GPMathProblem extends GPProblem {
  @SuppressWarnings("boxing")
  private static double[] inputs = { -2.00, -1.75, -1.50, -1.25, -1.00, -0.75, -0.50, -0.25, 0.00, 0.25, 0.50, 0.75, 1.00, 1.25, 1.50, 1.75, 2.00, 2.25, 2.50, 2.75 };

  private static double[] outputs = { 37.00000, 24.16016, 15.06250, 8.91016, 5.00000, 2.72266, 1.56250, 1.09766, 1.00000, 1.03516, 1.06250, 1.03516, 1.00000, 1.09766, 1.56250, 2.72266, 5.00000, 8.91016, 15.06250, 24.16016 };

  private Variable var;

  public GPMathProblem() throws InvalidConfigurationException {
    super(new GPConfiguration());

    GPConfiguration config = getGPConfiguration();

    var = Variable.create(config, "x", CommandGene.DoubleClass);

    config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
    config.setMaxInitDepth(4);
    config.setPopulationSize(800);
    config.setMutationProb(0.1f);
    config.setCrossoverProb(0.8f);
    config.setMaxCrossoverDepth(6);
    config.setFitnessFunction(new GPMathProblemFitnessFunction(inputs, outputs, var));
    config.setStrictProgramCreation(true);
  }

  @Override
  public GPGenotype create() throws InvalidConfigurationException {
    GPConfiguration config = getGPConfiguration();

    // The return type of the GP program.
    Class[] types = { CommandGene.DoubleClass };

    // Arguments of result-producing chromosome: none
    Class[][] argTypes = { {} };

    // Next, we define the set of available GP commands and terminals to
    // use.
    CommandGene[][] nodeSets = {
          {
                var,
                new Add(config, CommandGene.DoubleClass),
                new Multiply(config, CommandGene.DoubleClass),
                new Subtract(config, CommandGene.DoubleClass),
                new Pow(config, CommandGene.DoubleClass), // pow added to prevent the system to have to generate it
                new Terminal(config, CommandGene.DoubleClass, 0.0, 4.0, true) // Random constant variable between the range 0.0 and 4.0
          }
    };

    GPGenotype result = GPGenotype.randomInitialGenotype(config, types, argTypes, nodeSets, 20, true);

    return result;
  }

  public static void main(String[] args) throws Exception {

    boolean stopAtSolution = false;
    if(args.length == 1 && args[0].equals("true")){
      stopAtSolution = true;
    }
    GPProblem problem = new GPMathProblem();
    int generations = 100;

    /*
      Iterates over a number of evolutions, 1 at a time, allowing me to check each generation and to determine when the fitness == 0 (correct solution)

      If argument stopAtSolution is true, then cease generations at a fitness of 0.

      Commented out lines were used for testing - performs 200 loops, stopping after it finds a best solution and recording how many generations it took.
      Then prints the min and max number of generations.
    */

//    int min = -1;
//    int max = 0;
//    for(int r = 0; r < 200; r++) {

    GPGenotype gp = problem.create();
    for (int i = 0; i < generations; i++) {
      gp.setVerboseOutput(true);
      gp.evolve(1); // will evolve a single generation each time

        if (stopAtSolution && gp.getFittestProgram().getFitnessValue() == 0) {
//          if(min == -1){
//            min = i; max = i;
//          }
//          if(i < min) min = i;
//          if(i > max) max = i;
          break;
        }
      gp.outputSolution(gp.getAllTimeBest());
    }
//    }
//    System.out.println("Min generations: " + min);
//    System.out.println("Max generations: " + max);
  }

}