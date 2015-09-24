
package com.maxxton.printer.lpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Schedule PrintJobs for each printer.
 * Each printer has it own queue.
 * This scheduler prevent printers to receive more than one job at the time.
 * 
 * @author Hermans.S
 * @copyright Maxxton 2015
 */
public class PrintJobScheduler
{

  /**
   * Queue of PrintJobs for each printer host
   */
  private static final Map<String, ArrayList<IPrintJob>> PRINTJOBS = new HashMap();

  private static final Map<String, Thread> THREADS = new HashMap();

  /**
   * Schedule this printJob
   * 
   * @param printJob
   */
  protected static void schedule(IPrintJob printJob)
  {
    String printerHost = printJob.getPrinter().getHost() + ":" + printJob.getPrinter().getPort();
    if (PRINTJOBS.containsKey(printerHost))
    {
      ArrayList<IPrintJob> printQueue = PRINTJOBS.get(printerHost);

      if (printQueue.contains(printJob))
      {
        // This job is already scheduled
        return;
      }

      if (printQueue.isEmpty())
      {
        // No other jobs queued, can run directly
        printQueue.add(printJob);
        start(printJob);
      }
      else
      {
        // Wait for other jobs
        printQueue.add(printJob);
      }
    }
    else
    {
      // Never printed before to this host, create new queue
      ArrayList<IPrintJob> printQueue = new ArrayList();
      printQueue.add(printJob);
      PRINTJOBS.put(printerHost, printQueue);
      start(printJob);
    }
  }
  
  /**
   * Get the queue length for a specific printer
   * @param printer For which printer the queue is
   * @return Size of the queue, 0 if the queue doesn't exists
   */
  protected static int getQueueSize(LPRPrinter printer){
    String printerHost = printer.getHost() + ":" + printer.getPort();
    if(PRINTJOBS.containsKey(printerHost)){
        return PRINTJOBS.get(printerHost).size();
    }else{
        return 0;
    }
  }

  /**
   * Start new thread for PrintJob
   * 
   * @param printJob
   */
  private static void start(IPrintJob printJob)
  {
    final String printerHost = printJob.getPrinter().getHost() + ":" + printJob.getPrinter().getPort();
    // Check if thread for this host is already running
    if (THREADS.containsKey(printerHost))
    {
      if (THREADS.get(printerHost).isAlive())
      {
        return;
      }
    }

    // Startup new Thread
    Thread thread = new Thread("PrintJobs for " + printerHost)
    {

      @Override
      public void run()
      {
        ArrayList<IPrintJob> printQueue = PRINTJOBS.get(printerHost);
        while (!printQueue.isEmpty())
        {
          IPrintJob printJob = printQueue.get(0);
          try
          {
            printJob.print();
          }
          catch (LPRException ex)
          {
            PrintJob.LOG.log(Level.SEVERE, "PrintJob failed", ex);
          }
          printQueue.remove(0);
        }
      }
    };
    THREADS.put(printerHost, thread);
    thread.start();
  }

}
