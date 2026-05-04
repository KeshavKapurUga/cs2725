package cs2725.util;

/**
 * A class that can visualize stack frames.
 */
public class StackViz {

    static int fCount = 0;
    static int timeStep = 0;

    /**
     * Account for new stack frame and draw a snapshot of the stack at the moment.
     * 
     * @param functionLabel a Stirng that represent the current invocation of the
     *                      function and its parameters.
     */
    public static void start(String functionLabel) {
        ++fCount; // Stack frame count.

        System.out.printf("t%03d |", ++timeStep);
        System.out.println("_____|".repeat(fCount) + "s:" + functionLabel + ": ?");
    }

    /**
     * Draw a snapshot of the stack at the moment. Then, account for the stack frame
     * that will be popped.
     * 
     * @param functionLabel a Stirng that represent the current invocation of the
     *                      function and its parameters.
     * @param returnVal     the return value of the function.
     */
    public static void end(String functionLabel, String returnVal) {
        System.out.printf("t%03d |", ++timeStep);
        System.out.println("_____|".repeat(fCount) + "e:" + functionLabel + ": " + returnVal);

        --fCount; // Stack frame count.
    }

}