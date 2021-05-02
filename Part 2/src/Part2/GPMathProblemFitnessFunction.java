package Part2;

import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.terminal.Variable;

/**
 * @author Owen Nicholson - 300130635
 * @credit carlos - tutorial code supplied by lecturer
 * https://cvalcarcel.wordpress.com/2009/08/04/jgap-a-firstsimple-tutorial/
 */
public class GPMathProblemFitnessFunction extends GPFitnessFunction {

  private double[] inputs;
  private double[] outputs;
  private Variable var;

  private static Object[] NO_ARGS = new Object[0];

  public GPMathProblemFitnessFunction(double[] input, double[] output, Variable x) {
    inputs = input;
    outputs = output;
    var = x;
  }

  @Override
  protected double evaluate(final IGPProgram program) {
    double result;

    long error = 0;

    for (int i = 0; i < inputs.length; i++) {
      // Set the input values
      var.set(inputs[i]);
      // Execute the genetically engineered algorithm
      long value = (long)program.execute_double(0, NO_ARGS);

//      error += Math.abs(value - outputs[i]); // Absolute error

      error += Math.pow((long)(value - outputs[i]), 2); //Squared Error
    }

    result = error * (1/(double)inputs.length); // Mean squared error

    return result;
  }

}