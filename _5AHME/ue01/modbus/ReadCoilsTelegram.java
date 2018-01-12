/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _5AHME._5AHME.ue01.modbus;

import _5AHME._5AHME.ue01.serial.*;
import com.sun.jndi.cosnaming.*;
import gnu.io.*;
import java.util.*;

/**
 *
 * @author User
 */
public class ReadCoilsTelegram extends AbstractTelegram
{
  private final int start, quantity;
  public ReadCoilsTelegram(SimpleSerial serial, byte busAddress, int start, int quantity)
  {
    super(serial, busAddress, (byte)1, buildXmtData(start, quantity), 1 + (quantity/8) + (((quantity % 8)!=0) ? 1 : 0));
    this.start = start;
    this.quantity = quantity;
  }
  private static byte[] buildXmtData(int start, int quantity)
  {
    final byte[] xmt = new byte[4];
    xmt[0] = (byte)((start>>8)&0xFF);
    xmt[1] = (byte)(start&0xFF);
    xmt[2] = (byte)((quantity>>8)&0xFF);
    xmt[3] = (byte)(quantity&0xFF);
    return xmt;
  }
  
  public boolean getCoil(int address)
          throws Exception
  {
    if(address<start || address>= start+quantity)
      throw new Exception("illegal address");
    final byte[] received = getRecieved();
    final int 
            byteIndex   = 3 + (address-start)/8,
            bitIndex    = (address-start)%8;
    return (received[byteIndex] & (1<<bitIndex)) != 0;
    
  }
  public static void main(String[] args)
  {
    try(SimpleSerial serial = new SimpleSerial("COM6"))
    {
      serial.open();
      serial.setParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_2, SerialPort.PARITY_NONE);
      final ReadCoilsTelegram tel = new ReadCoilsTelegram(serial, (byte)2, 0, 4);
      tel.send();
      final byte[] answer = tel.recieve();
      System.out.println("Recieved" + Arrays.toString(answer));
      for(int i=0; i<4; i++)
      {
        System.out.format("coil %d ist %b%n", i, tel.getCoil(i));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
