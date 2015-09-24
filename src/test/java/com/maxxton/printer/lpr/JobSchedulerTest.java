package com.maxxton.printer.lpr;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for PrintJobScheduler
 *
 * @see PrintJobScheduler
 *
 * @author Hermans.s
 * @copyright Maxxton 2015
 */
public class JobSchedulerTest
{
  
  private LPRPrinter printer1;
  private LPRPrinter printer2;

  public JobSchedulerTest()
  {
  }

  @BeforeClass
  public static void setUpClass()
  {
  }

  @AfterClass
  public static void tearDownClass()
  {
  }

  @Before
  public void setUp()
  {
    printer1 = new LPRPrinter("192.168.0.1");
    printer1.addPrintListener(new PrintAdapter() {
      @Override
      public void printFailed(PrintEvent event, LPRException e)
      {
        e.printStackTrace();
      }
    });
    printer2 = new LPRPrinter("192.168.0.2");
    printer2.addPrintListener(new PrintAdapter() {
      @Override
      public void printFailed(PrintEvent event, LPRException e)
      {
        e.printStackTrace();
      }
    });
  }

  @After
  public void tearDown()
  {
  }

  /**
   * Test if the PrintJobScheduler executes PrintJobs that are scheduled
   */
  @Test
  public void executeJob()
  {
    final ArrayList<Boolean> report = new ArrayList();

    //Create test job
    TestJob testJob = new TestJob(printer1)
    {
      @Override
      public void print() throws LPRException
      {
        //Notify to the report that job has run
        report.add(true);
      }
    };

    //Schedule the job
    PrintJobScheduler.schedule(testJob);

    //Wait for all jobs to finish
    while (PrintJobScheduler.getQueueSize(printer1) > 0)
    {
      try
      {
        Thread.sleep(20);
      } catch (InterruptedException e)
      {
      }
    }

    //Check if report is not empty
    assertFalse(report.isEmpty());
  }

  /**
   * Execute two jobs for the same printer
   * They should be scheduled after each other
   */
  @Test
  public void executeTwoJobs()
  {
    final ArrayList<Boolean> report = new ArrayList();

    //Create first job
    TestJob testJob1 = new TestJob(printer1)
    {
      @Override
      public void print() throws LPRException
      {
        //Check if other test has run
        if(!report.isEmpty()){
          report.add(false);
          System.err.println("job1 isn't first");
          return;
        }
        try{
          Thread.sleep(50);
        }catch(InterruptedException e){}
        
        if(report.isEmpty()){
          report.add(true);
        }else{
          System.err.println("job1 hasn't finished first");
          report.add(false);
        }
      }
    };

    //Create second job
    TestJob testJob2 = new TestJob(printer1)
    {
      @Override
      public void print() throws LPRException
      {
        //Check if other test has run
        if(report.isEmpty()){
          report.add(false);
          System.err.println("job2 has started first");
          return;
        }
        try{
          Thread.sleep(50);
        }catch(InterruptedException e){}
        
        report.add(true);
      }
    };

    //Schedule the job
    PrintJobScheduler.schedule(testJob1);
    PrintJobScheduler.schedule(testJob2);

    //Wait for all jobs to finish
    while (PrintJobScheduler.getQueueSize(printer1) > 0)
    {
      try
      {
        Thread.sleep(20);
      } catch (InterruptedException e)
      {
      }
    }

    //Check if both jobs have run
    assertEquals(report.size(), 2);
    //Check if both jobs ran in the correct order
    assertTrue(report.get(0) && report.get(1));
  }

  /**
   * Execute two jobs for the two different printers
   * They should be scheduled after each other
   */
  @Test
  public void executeTwoJobsForTwoPrinters()
  {
    final ArrayList<Boolean> report = new ArrayList();

    //Create first job
    TestJob testJob1 = new TestJob(printer1)
    {
      @Override
      public void print() throws LPRException
      {
        //Notify that this job has started
        report.add(true);
        
        try{
          Thread.sleep(50);
        }catch(InterruptedException e){}
        
        //Check if other job is running but not finished
        boolean status = false;
        if(report.size() < 2){
          System.err.println("Job 2 isn't started");
        }else if(report.size() > 2){
          System.err.println("Job 2 is already finished");
        }else{
          status = true;
        }
        
        try{
          Thread.sleep(50);
        }catch(InterruptedException e){}
        
        report.add(status);
      }
    };

    //Create second job for different printer
    TestJob testJob2 = new TestJob(printer2)
    {
      @Override
      public void print() throws LPRException
      {
        //Notify that this job has started
        report.add(true);
        
        try{
          Thread.sleep(50);
        }catch(InterruptedException e){}
        
        //Check if other job is running but not finished
        boolean status = false;
        if(report.size() < 2){
          System.err.println("Job 1 isn't started");
        }else if(report.size() > 2){
          System.err.println("Job 1 is already finished");
        }else{
          status = true;
        }
        
        try{
          Thread.sleep(50);
        }catch(InterruptedException e){}
        
        report.add(status);
      }
    };

    //Schedule the job
    PrintJobScheduler.schedule(testJob1);
    PrintJobScheduler.schedule(testJob2);

    //Wait for all jobs to finish
    while (PrintJobScheduler.getQueueSize(printer1) > 0 && PrintJobScheduler.getQueueSize(printer2) > 0)
    {
      try
      {
        Thread.sleep(20);
      } catch (InterruptedException e)
      {
      }
    }

    //Check if both jobs have run
    assertEquals(report.size(), 4);
    //Check if both jobs ran in the correct order
    assertTrue(report.get(0) && report.get(1) && report.get(2) && report.get(3));
  }
}
