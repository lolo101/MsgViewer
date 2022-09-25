package at.redeye.FrameWork.utilities.WorkerThread;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkerThread extends Thread {
    private final ConcurrentLinkedQueue<WorkInterface> queue_to_work = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<WorkInterface> queue_done = new ConcurrentLinkedQueue<>();
    private boolean currently_idle = true;

    public WorkerThread(String name) {
        super(name);
    }


    @Override
    public void run() {
        do {
            for (WorkInterface work; (work = queue_to_work.poll()) != null; ) {
                currently_idle = false;
                work.work();
                queue_done.add(work);
                currently_idle = true;
            }

            idle();

        } while (true);
    }

    /**
     * called when idle
     */
    private static void idle() {
        try {
            sleep(300);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * call this function when you want that all the finnish function
     * of the worker is done
     */
    public void callFinishedWork()
    {
        queue_done.forEach(WorkInterface::workDone);
    }

    public void add( WorkInterface work )
    {
        queue_to_work.add(work);
    }

    public boolean isIdle()
    {
        return queue_to_work.isEmpty() && queue_done.isEmpty() && currently_idle;
    }
}
