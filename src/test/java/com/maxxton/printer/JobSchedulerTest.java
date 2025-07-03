package com.maxxton.printer;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit tests for PrintJobScheduler
 *
 * Copyright Maxxton 2015
 *
 * @author Hermans.S
 * @see PrintJobScheduler
 */
public class JobSchedulerTest {

  private Printer printer1;
  private Printer printer2;

  public JobSchedulerTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
    printer1 = new Printer("192.168.0.1");
    printer1.addPrintListener(new PrintAdapter() {
      @Override
      public void printFailed(PrintEvent event, PrintException e) {
        e.printStackTrace();
      }
    });
    printer2 = new Printer("192.168.0.2");
    printer2.addPrintListener(new PrintAdapter() {
      @Override
      public void printFailed(PrintEvent event, PrintException e) {
        e.printStackTrace();
      }
    });
  }

  @After
  public void tearDown() {
  }

  /**
   * Test if the PrintJobScheduler executes PrintJobs that are scheduled
   */
  @Test
  public void executeJob() {
    final List<Boolean> report = new ArrayList();

    //Create test job
    PrintJob testJob = new PrintJob(printer1, null, PrintProtocol.RAW) {
      @Override
      public void print() throws PrintException {
        //Notify to the report that job has run
        report.add(true);
      }

      @Override
      public void execute(PrinterConnection printerConnection) throws Exception {
      }
    };

    //Schedule the job
    PrintJobScheduler.schedule(testJob);

    //Wait for all jobs to finish
    int timeout = 1000;
    while (PrintJobScheduler.getQueueSize(printer1) > 0) {
      try {
        Thread.sleep(20);
      }
      catch (InterruptedException e) {
      }
      timeout -= 20;
      if (timeout == 0) {
        fail("timeout");
      }
    }

    //Check if report is not empty
    assertFalse(report.isEmpty());
  }

  /**
   * Execute two jobs for the same printer They should be scheduled after each
   * other
   */
  @Test
  public void executeTwoJobs() {
    final List<Boolean> report = new ArrayList();

    //Create first job
    PrintJob testJob1 = new PrintJob(printer1, null, PrintProtocol.RAW) {
      @Override
      public void print() throws PrintException {
        //Check if other test has run
        if (!report.isEmpty()) {
          report.add(false);
          System.err.println("job1 isn't first");
          return;
        }
        try {
          Thread.sleep(50);
        }
        catch (InterruptedException e) {
        }

        if (report.isEmpty()) {
          report.add(true);
        }
        else {
          System.err.println("job1 hasn't finished first");
          report.add(false);
        }
      }

      @Override
      public void execute(PrinterConnection printerConnection) throws Exception {
      }
    };

    //Create second job
    PrintJob testJob2 = new PrintJob(printer1, null, PrintProtocol.RAW) {
      @Override
      public void print() throws PrintException {
        //Check if other test has run
        if (report.isEmpty()) {
          report.add(false);
          System.err.println("job2 has started first");
          return;
        }
        try {
          Thread.sleep(50);
        }
        catch (InterruptedException e) {
        }

        report.add(true);
      }

      @Override
      public void execute(PrinterConnection printerConnection) throws Exception {
      }
    };

    //Schedule the job
    PrintJobScheduler.schedule(testJob1);
    PrintJobScheduler.schedule(testJob2);

    //Wait for all jobs to finish
    int timeout = 1000;
    while (PrintJobScheduler.getQueueSize(printer1) > 0) {
      try {
        Thread.sleep(20);
      }
      catch (InterruptedException e) {
      }
      timeout -= 20;
      if (timeout == 0) {
        fail("timeout");
      }
    }

    //Check if both jobs have run
    assertEquals(report.size(), 2);
    //Check if both jobs ran in the correct order
    assertTrue(report.get(0) && report.get(1));
  }

  /**
   * Execute two jobs for the two different printers They should be scheduled
   * after each
   * other
   */
  @Test
  public void executeTwoJobsForTwoPrinters() {
    final List<Boolean> report = new ArrayList();

    //Create first job
    PrintJob testJob1 = new PrintJob(printer1, null, PrintProtocol.RAW) {
      @Override
      public void print() throws PrintException {
        //Notify that this job has started
        report.add(true);

        try {
          Thread.sleep(50);
        }
        catch (InterruptedException e) {
        }

        //Check if other job is running but not finished
        boolean status = false;
        if (report.size() < 2) {
          System.err.println("Job 2 isn't started");
        }
        else if (report.size() > 2) {
          System.err.println("Job 2 is already finished");
        }
        else {
          status = true;
        }

        try {
          Thread.sleep(50);
        }
        catch (InterruptedException e) {
        }

        report.add(status);
      }

      @Override
      public void execute(PrinterConnection printerConnection) throws Exception {
      }
    };

    //Create second job for different printer
    PrintJob testJob2 = new PrintJob(printer2, null, PrintProtocol.RAW) {
      @Override
      public void print() throws PrintException {
        //Notify that this job has started
        report.add(true);

        try {
          Thread.sleep(50);
        }
        catch (InterruptedException e) {
        }

        //Check if other job is running but not finished
        boolean status = false;
        if (report.size() < 2) {
          System.err.println("Job 1 isn't started");
        }
        else if (report.size() > 2) {
          System.err.println("Job 1 is already finished");
        }
        else {
          status = true;
        }

        try {
          Thread.sleep(50);
        }
        catch (InterruptedException e) {
        }

        report.add(status);
      }

      @Override
      public void execute(PrinterConnection printerConnection) throws Exception {
      }
    };

    //Schedule the job
    PrintJobScheduler.schedule(testJob1);
    PrintJobScheduler.schedule(testJob2);

    //Wait for all jobs to finish
    int timeout = 1000;
    while (PrintJobScheduler.getQueueSize(printer1) > 0 && PrintJobScheduler.getQueueSize(printer2) > 0) {
      try {
        Thread.sleep(20);
      }
      catch (InterruptedException e) {
      }
      timeout -= 20;
      if (timeout == 0) {
        fail("timeout");
      }
    }

    //Check if both jobs have run
    assertEquals(report.size(), 4);
    System.out.println(report);
    //Check if both jobs ran in the correct order
    System.out.println(report.get(0) + " " + report.get(1) + " " + report.get(2) + " " + report.get(3));
    assertTrue(report.get(0) && report.get(1) && report.get(2) && report.get(3));
  }
}
