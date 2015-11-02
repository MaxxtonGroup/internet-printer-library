package com.maxxton.printer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Executes PrintJob
 *
 * @author Hermans.S Copyright Maxxton 2015
 */
public abstract class PrintJob
{

  public static final Logger LOG = Logger.getLogger(PrintJob.class.getName());

  private final PrintDocument document;
  private final Printer printer;
  private final PrintProtocol protocol;
  private final int port;

  private JobStatus status = JobStatus.PENDING;
  private PrintException error = null;

  /**
   * Create new PrintJob
   *
   * @param printer  LPR Printer
   * @param document Document to be printed
   * @param protocol PrintProtocol
   */
  public PrintJob(Printer printer, PrintDocument document, PrintProtocol protocol)
  {
    this.printer = printer;
    this.document = document;
    this.protocol = protocol;
    if (printer.getPort() > 0)
    {
      port = printer.getPort();
    } else
    {
      port = protocol.getPort();
    }
  }

  /**
   * Start print job
   *
   * @throws PrintException
   */
  public void print() throws PrintException
  {
    if (!getStatus().equals(JobStatus.PENDING))
    {
      throw new PrintException("PrintJob is already started");
    }

    LOG.info("Start PrintJob [" + protocol.name() + "] '" + getDocument().getDocumentName() + "'");
    setStatus(JobStatus.RUNNING);

    PrinterConnection printerConnection = null;
    try
    {
      // Connect to printer
      printerConnection = connect(port);

      //Execute command
      execute(printerConnection);

//      byte[] buffer = new byte[1024];
//      int l;
//      try
//      {
//        while ((l = printerConnection.getInputStream().read(buffer)) != -1)
//        {
//          for (int i = 0; i < l; i++)
//          {
//            System.out.println("Received: " + String.valueOf(buffer[i]));
//          }
//        }
//      } catch (IOException e)
//      {
//      }

      close(printerConnection);
      LOG.info("PrintJob Compleet!");

      // Trigger printSucceed event
      try
      {
        PrintEvent event = new PrintEvent(getPrinter(), getDocument(), this);
        for (PrinterListener listener : getPrinter().getPrintListeners())
        {
          listener.printSucceed(event);
        }
      } catch (Throwable ee)
      {
        LOG.log(Level.SEVERE, "printSucceed event failed", ee);
      }
      setStatus(JobStatus.FINISHED);
    } catch (Throwable e)
    {
      //Close connection
      if (printerConnection != null)
      {
        close(printerConnection);
      }

      // Convert Throwable into PrintException
      PrintException exception;
      if (e instanceof PrintException)
      {
        exception = (PrintException) e;
      } else
      {
        exception = new PrintException(e);
      }

      // Trigger printFailed event
      try
      {
        PrintEvent event = new PrintEvent(getPrinter(), getDocument(), this);
        for (PrinterListener listener : getPrinter().getPrintListeners())
        {
          listener.printFailed(event, exception);
        }
      } catch (Throwable ee)
      {
        LOG.log(Level.SEVERE, "printFailed event failed", ee);
      }

      setError(exception);
      setStatus(JobStatus.FAILED);

      // Throws Exception
      throw exception;
    }
  }

  /**
   * Print the document
   *
   * @param printerConnection Connection with the printer
   * @throws PrintException
   */
  public abstract void execute(PrinterConnection printerConnection) throws Exception;

  /**
   * Close printer connection
   *
   * @param connection printer connection
   */
  protected void close(PrinterConnection connection)
  {
    try
    {
      connection.close();
    } catch (Exception e)
    {
    }
  }

  protected PrinterConnection connect(int port) throws PrintException
  {
    return printer.connect(port);
  }

  public JobStatus getStatus()
  {
    return status;
  }

  public void setStatus(JobStatus status)
  {
    this.status = status;
  }

  /**
   * Get the error if one occurred
   *
   * @return PrintException if one has occurred
   */
  public PrintException getError()
  {
    return error;
  }

  /**
   * Wait for job to finish
   *
   * @throws PrintException
   */
  public void waitFor() throws PrintException
  {
    try
    {
      while (isRunning())
      {
        Thread.sleep(20);
      }
    } catch (InterruptedException e)
    {
    }
    if (error != null)
    {
      throw error;
    }
  }

  public boolean isRunning()
  {
    return status == JobStatus.PENDING || status == JobStatus.RUNNING;
  }

  public Printer getPrinter()
  {
    return printer;
  }

  /**
   * Status for PrintJobs
   */
  public static enum JobStatus
  {

    PENDING(), RUNNING(), FINISHED(), FAILED();
  }

  public Logger getLogger()
  {
    return LOG;
  }

  public PrintDocument getDocument()
  {
    return document;
  }

  public void setError(PrintException error)
  {
    this.error = error;
  }

}
