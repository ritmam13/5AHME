/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _5AHME._5AHME.ue01.serial;

import java.util.*;
import jssc.*;

/**
 *
 * @author User
 */
public class SimpleSerial implements AutoCloseable
{
  private final String portName;
  private SerialPort serialPort = null;
  
  public SimpleSerial(String portName) throws Exception
  {
    final List<String> portNames = findSerialComms();
    if(!portNames.contains(portName))
    {
      throw new Exception(String.format("Schnittstelle %s nicht gefunden", portName));
    }
    this.portName = portName;
  }
  
  public void open() throws Exception
  {
    try
    {
      if(serialPort==null)
        serialPort = new SerialPort(portName);
      if(!serialPort.isOpened())
        serialPort.openPort();
    }
    catch(Exception ex)
    {
      serialPort = null;
      throw ex; // Exception weiterwerfen 
    }
    
  }
  @Override
  public void close() throws Exception
  {
    if(serialPort!=null)
    {
      if(serialPort.isOpened())
        serialPort.closePort();
    }
  }

  public boolean setParams(int baudRate, int dataBits, int stopBits, int parity) throws SerialPortException
  {
    return serialPort.setParams(baudRate, dataBits, stopBits, parity);
  }

  public boolean writeBytes(byte[] buffer) throws SerialPortException
  {
    return serialPort.writeBytes(buffer);
  }
  public byte[] readBytes(int byteCount, int timeout) throws SerialPortException, SerialPortTimeoutException
  {
    return serialPort.readBytes(byteCount, timeout);
  }
  
  public void purgeInput() throws SerialPortException
  {
    serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
  }
  public final static List<String> findSerialComms()
  {
    return Arrays.asList(SerialPortList.getPortNames());
  }
  
  public static void main(String[] args)
  {
    try
    {
      final List<String> comms = findSerialComms();
      System.out.format("%d Schnittstellen gefunden:%n ",comms.size());
      for(String comm : comms)
      {
        System.out.println(comm);
      }
      try (final SimpleSerial ser = new SimpleSerial("COM1"))
      {
        ser.open();
        System.out.println("Hi");
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
