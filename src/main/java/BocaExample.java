
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hermans.s
 */
public class BocaExample
{

  public static void main(String[] args) throws IOException
  {
    BocaTicket ticket = new BocaTicket();
    ticket.setArrivalDate("TODAY");
    ticket.setDepartureDate("TOMORROW");
    ticket.setCustomerName("Me");
    ticket.setResortName("Maxxton Vacations");
    ticket.setReservationNumber("50");
    ticket.setTemplateContent("template");

    ServerSocket ss = new ServerSocket(9100);
    new Thread()
    {
      public void run()
      {
        try
        {
          Socket s = ss.accept();
          ByteArrayOutputStream buffer = new ByteArrayOutputStream();
          int l;
          byte[] b = new byte[1024];
          while ((l = s.getInputStream().read(b)) != -1)
          {
            buffer.write(b, 0, l);
            buffer.flush();
          }
          System.out.println(new String(buffer.toByteArray()));
        } catch (Throwable e)
        {
          e.printStackTrace();
        }
      }
    }.start();

    BocaPrinter printer = new BocaPrinter(ticket, "localhost");
    printer.print();
    printer.closeSocket();

  }

}
