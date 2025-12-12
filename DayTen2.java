import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// Bit of a weird solution - I had already built the linear equations in Java but don't
// know how to solve them here, so I output Python code to use Z3 to solve them.
// I don't really know Python so this was the best I could do!
// There is a Java interface to Z3 but I couldn't find good examples of setting up
// the solver and equations so I went with this horrible approach instead.

public class DayTen2 {
    public static void main(String[] args) throws IOException {

        // create machines from each line of input and construct neccessary Python code
        String python = "from z3 import * \n\n";
        python += "runningTotal = 0\n\n";
        List<String> lines = Files.readAllLines(new File("input10.txt").toPath());
        for (String line : lines) {
            EquationSet machine = new EquationSet(line);
            python += machine.getPython() + "\n\n";
        }

        // output the python code to a file
        Files.write(new File("DayTen2.py").toPath(), python.getBytes());
        System.out.println("Python code written to DayTen2.py");
    }
}

// Represents a single equation of the form a + b + c + ... = result
class Equation {
    HashSet<Character> variables;
    int result;

    // initiate with the result value
    public Equation(int result) {
        this.variables = new HashSet<Character>();
        this.result = result;
    }

    // add a variable to the equation
    public void addVariable(char variable) {
        this.variables.add(variable);
    }

    // convert the equation to a string representation that will be used in Python
    // code
    public String toString() {

        String equation = "";
        for (Character variable : variables) {
            equation += variable + " + ";
        }
        if (!equation.equals(""))
            equation = equation.substring(0, equation.length() - 3);
        return equation + " == " + result;
    }
}

// a set of simultaneous equations representing the machine
class EquationSet {

    ArrayList<Equation> equestions = new ArrayList<Equation>();
    int numButtons = 0;

    // parse input string to create machine
    public EquationSet(String source) {

        // split into parts and trim off the brackets
        String[] parts = source.split(" ");
        numButtons = parts.length - 2;
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].substring(1, parts[i].length() - 1);
        }

        // get the last part which is the target
        String target = parts[parts.length - 1];

        // break up the target into each joltage dial...
        String[] targetParts = target.split(",");

        // for each target part, create an equation
        for (int i = 0; i < targetParts.length; i++) {
            targetParts[i] = targetParts[i].trim();
            equestions.add(new Equation(Integer.parseInt(targetParts[i])));
        }

        // now go back to the original input and look at the other bits...
        for (int i = 1; i < parts.length - 1; i++) {

            // add this button's variable to each equation it affects
            String[] subParts = parts[i].split(",");
            for (String subPart : subParts) {
                int j = Integer.parseInt(subPart.trim());
                equestions.get(j).addVariable((char) ('a' + i - 1));
            }
        }
    }

    // generate Python code to solve the equations using Z3
    // I'm so sorry
    public String getPython() {
        String python = "";

        // create variables and solver
        for (int i = 0; i < numButtons; i++) {
            python += (char) ('a' + i) + " = Int('" + (char) ('a' + i) + "')\n";
        }
        python += "x = Int('x')\n";
        python += "s = Solver()\n";

        // add equations to solver
        for (Equation eq : equestions) {
            python += "s.add(" + eq.toString().replace(" = ", " == ") + ")\n";
        }

        // add the x variable to represent total presses
        python += "s.add(x == ";
        for (int i = 0; i < numButtons; i++) {
            python += (char) ('a' + i) + " + ";
        }
        python = python.substring(0, python.length() - 3) + ")\n";

        // add constraints that all variables are non-negative
        python += "s.add(x >= 0,";
        for (int i = 0; i < numButtons; i++) {
            python += (char) ('a' + i) + " >= 0,";
        }
        python = python.substring(0, python.length() - 1) + ")\n";

        // final chunk of code finds the minimum x value satisfying the equations
        python += "tempTotal = 0\n";
        python += "while s.check() == sat:\n";
        python += "    if tempTotal==0 or s.model()[x].as_long() < tempTotal:\n";
        python += "        tempTotal = s.model()[x].as_long()\n";
        python += "    s.add(x < s.model()[x])\n";
        python += "print(tempTotal)\n";
        python += "runningTotal += tempTotal\n";
        python += "print(runningTotal)\n";
        python += "print(\"Done\")\n";

        return python;
    }
}