import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This was an attempt to solve part 2 of day 10 using a similar approach to part 1,
// which works for the test data but runs out of memory on the full input!

public class DayTen2a {
    public static void main(String[] args) throws IOException {

        System.out.println("Day 10 Part 2");

        // create machines from each line of input and solve
        List<String> lines = Files.readAllLines(new File("input10.txt").toPath());
        int total = 0;
        for (String line : lines) {
            Machine machine = new Machine(line);
            total += machine.solve();
        }
        System.out.println("Part 1: " + total);
    }
}

class Machine {

    ArrayList<Button> buttons = new ArrayList<Button>();
    int[] targetState;

    // parse input string to create machine
    public Machine(String target) {
        String[] parts = target.split(" "); 

        System.out.println("Parsing machine: " + target);  
        
        int length = 0;
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].substring(1, parts[i].length() - 1);
            if (i == 0) {
                //use the wiring diagram to quickly get the length
                length = parts[i].length();
            } else if (i == parts.length - 1) {
                //parse the joltage
                System.out.println("Parsing target state: " + parts[i]);
                String[] joltageParts = parts[i].split(",");
                targetState = new int[joltageParts.length];
                for (int j = 0; j < joltageParts.length; j++) {
                    targetState[j] = Integer.parseInt(joltageParts[j]);
                }
                System.out.println("Target State: " + Arrays.toString(targetState));
            } else {
                // for the remaining parts, use them to create buttons
                System.out.println("Cresting button from: " + parts[i]);

                buttons.add(new Button(parts[i], length));
            }
        }
    }

    // solve the machine and return the minimum number of presses
    public int solve() {

        // list to hold all attempts at the current depth
        ArrayList<Attempt> currentAttempts = new ArrayList<Attempt>();

        // crate an intial attempt with 0 button presses
        Attempt attempt = new Attempt(this.targetState, new ArrayList<Button>());
        int depth = 0;
        currentAttempts.add(attempt);

        while (true) {
            // at the new depth, spawn a new layer of attempts from the current attempts
            // adding an extra button to each one
            depth++;
            System.out.println("Depth: " + depth + " Current Attempts: " + currentAttempts.size());
            ArrayList<Attempt> newAttempts = new ArrayList<Attempt>();
            for (Attempt currentAttempt : currentAttempts) {
                if (!currentAttempt.isStuck()) {
                    for (Button button : this.buttons) {
                        Attempt child = currentAttempt.getChild(button);
                        if (child.isComplete()) {
                            // if the child is compelte, return the depth and finish
                            return depth;
                        } else if (!child.isStuck()) {
                            // if the child is not stuck, add it to the new attempts for the next layer
                            // otherwise it can be abandoned
                            newAttempts.add(child);
                        }
                    }
                }
            }
            currentAttempts = newAttempts;
        }
    }
}

class Button {

    int[] mask;

    public Button(String input, int numLights) {

        //build the bit mask for this button, i.e if it toggles a light
        //then that bit is 1, otherwise 0
        mask = new int[numLights];
        String[] parts = input.split(",");
        for (int i = 0; i < numLights; i++) {
            for (int j = 0; j < parts.length; j++) {
                if (Integer.parseInt(parts[j]) == i) {
                    mask[i] = 1;
                }
            }
        }
    }
}

class Attempt {

    //HashSet<int[]> seenStates = new HashSet<int[]>();
    boolean stuck = false;
    boolean complete = false;
    Button nextButton;
    ArrayList<Button> buttonSequence;
    int[] targetState;

    // an attempt at solving the puzzle with a sequence of button presses
    // and a target state
    public Attempt(int[] targetState, ArrayList<Button> buttonSequence) {

        this.targetState = targetState;
        this.buttonSequence = buttonSequence;

        int[] currentState = new int[targetState.length];

        // run the sequence of button presses
        for (Button b : buttonSequence) {
            //System.out.println("Current State before: " + Arrays.toString(currentState));
            //System.out.println("Applying mask: " + Arrays.toString(b.mask));
            for (int i = 0; i < currentState.length; i++) {
                currentState[i] = currentState[i] + b.mask[i];
            }
             //System.out.println("Current State after: " + Arrays.toString(currentState));
        }

        //check if anything has overshot and mark us as stuck
        for (int i = 0; i < currentState.length; i++) {
            if (currentState[i] > targetState[i]) {
                stuck = true;
                //System.out.println("Overshoot!");
            }
        }

        //if it mathches the target state, we're complete
        if (Arrays.equals(currentState, targetState)) {
            complete = true;
        }
    }

    public Attempt getChild(Button button) {
        // create a new attempt by adding a button press to the sequence
        ArrayList<Button> newButtonsTried = new ArrayList<Button>(buttonSequence);
        newButtonsTried.add(button);
        return new Attempt(targetState, newButtonsTried);
    }

    public boolean isStuck() {

        return stuck;
    }

    public boolean isComplete() {

        return complete;
    }

}