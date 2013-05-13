/**
 * Timer class allows us to run a Timer concurrently to the main thread; we can
 * manage there timing for Riddle or real-time fights with creatures.
 *
 */
package lu.uni.programming2.team8.gameEngine;

/**
 *
 * @author FX
 */

public class Timer implements Runnable {

    // INSTANCE VARIABLES
    Thread timeCounter;
    private boolean stopTimeCounter = false;
    private boolean timerEnded = false;
    private long time;

    /**
     *
     * @param time in millisecs
     */
    public Timer(long time) {
        timeCounter = new Thread(this, "Timer thread");
        this.time = time;
        timeCounter.start();
    }

    // METHODS
    // Setter/Getter
    public void setStopTimeCounter(boolean stopTimeCounter) {
        this.stopTimeCounter = stopTimeCounter;
    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean getTimerEnded() {
        return timerEnded;
    }

    // Other Methods
    
    /**
     * We make here a decreasing loop while the counter is not stopped by the main thread (via a boolean <code>stopTimeCounter</code>)
     * or the time has gone.
     */
    @Override
    public void run() {
        int sec = 0;
        try {
            long i = time;

            while (!stopTimeCounter && i >= 0) {
                sec++;

                if (i > 10000 && (sec == 100)) {
                    sec = 0;
                    if (i%30000 == 0) {
                        System.out.println("Remaining Time: " + (i / 1000) + " seconds");
					}
                } else if ((i <= 10000) && (i >= 1000) && (sec == 100)) {
                    sec = 0;
                }
                Thread.sleep(10);
                i -= 10;
            }
            
            if (!stopTimeCounter) {
                timerEnded = true;
            }

        } catch (InterruptedException e) {
            System.out.println("Time Counter interrupted.");
            e.printStackTrace();
        }
    } // end of run method
}
