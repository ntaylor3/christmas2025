import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DayTen1 {
    public static void main(String[] args) throws IOException {

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

    ArrayList<Button> buttons;
    int targetState = 0;

    // parse input string to create machine
    public Machine(String target) {
        String[] parts = target.split(" ");
        buttons = new ArrayList<Button>();
        int length = 0;

        for (int i = 0; i < parts.length; i++) {
            if (i == 0) {
                // for the first chunk, build the target state as an integer
                // each bit is a light, 1 is on (#) and 0 is off (.)
                String convertedString = "";
                length = parts[i].length();
                for (int j = 1; j < length; j++) {
                    if (parts[i].charAt(j) == '.') {
                        convertedString += "0";
                    } else if (parts[i].charAt(j) == '#') {
                        convertedString += "1";
                    }

                }
                targetState = Integer.parseInt(convertedString, 2);
            } else if (i == parts.length - 1) {
                // ignore joltage for now
            } else {
                // for the remaining parts, use them to create buttons
                parts[i] = parts[i].substring(1, parts[i].length() - 1);
                buttons.add(new Button(parts[i], length - 2));
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
            ArrayList<Attempt> newAttempts = new ArrayList<Attempt>();
            for (Attempt currentAttempt : currentAttempts) {
                for (Button button : this.buttons) {
                    Attempt child = currentAttempt.getChild(button);
                    // if the child is compelte, return the depth and finish
                    if (child.isComplete()) {
                        return depth;
                    }
                    newAttempts.add(child);
                }
            }
            currentAttempts = newAttempts;
        }
    }
}

class Button {

    int mask = 0;
    String text;

    public Button(String input, int numLights) {

        // build the bit mask for this button, i.e if it toggles a light
        // then that bit is 1, otherwise 0
        String[] parts = input.split(",");
        this.text = input;
        String maskString = "";
        for (int i = 0; i < numLights; i++) {
            char c = '0';
            for (int j = 0; j < parts.length; j++) {
                if (Integer.parseInt(parts[j]) == i) {
                    c = '1';
                }
            }
            maskString += c;
        }
        mask = Integer.parseInt(maskString, 2);
    }
}

class Attempt {

    int targetState;
    HashSet<Integer> seenStates = new HashSet<Integer>();
    boolean complete = false;
    Button nextButton;
    ArrayList<Button> buttonSequence;

    // an attempt at solving the puzzle with a sequence of button presses
    // and a target state
    public Attempt(int targetState, ArrayList<Button> buttonSequence) {

        this.targetState = targetState;
        this.buttonSequence = buttonSequence;

        int currentState = 0;

        // run the sequence of button presses
        for (Button b : buttonSequence) {
            currentState = currentState ^ b.mask;
        }
        // if it mathches the target state, we're complete
        if (currentState == targetState) {
            complete = true;
        }
    }

    public Attempt getChild(Button button) {
        // create a new attempt by adding a button press to the sequence
        ArrayList<Button> newButtonsTried = new ArrayList<Button>(buttonSequence);
        newButtonsTried.add(button);
        return new Attempt(targetState, newButtonsTried);
    }

    public boolean isComplete() {

        return complete;
    }
}