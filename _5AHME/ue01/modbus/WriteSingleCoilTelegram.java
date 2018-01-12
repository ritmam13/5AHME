/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _5AHME._5AHME.ue01.modbus;

import _5AHME._5AHME.ue01.serial.*;
import gnu.io.*;
import java.util.*;

/**
 *
 * @author User
 */
public class WriteSingleCoilTelegram extends AbstractTelegram
{
  public WriteSingleCoilTelegram(SimpleSerial serial, byte busAddress, int coil, boolean set)
  {
    super(serial, busAddress, (byte)5, buildXmtData(coil, set), 4);
  }
  private static byte[] buildXmtData(int coil, boolean set)
  {
    final byte[] xmt = new byte[4];
    xmt[0] = (byte)((coil>>8)&0xFF);  //Hi-Byte
    xmt[1] = (byte)(coil&0xFF);       //Lo-Byte
    xmt[2] = set ? (byte)0xFF : (byte)0x00; // IF-Abfrage
    xmt[3] = 0;
    return xmt;
  }
  
  public static void main(String[] args)
  {
    try (SimpleSerial serial = new SimpleSerial("COM10"))
    {
      serial.open();
      serial.setParams(57600, 8, 2, SerialPort.PARITY_NONE);
      final WriteSingleCoilTelegram tel = 
              new WriteSingleCoilTelegram(serial, (byte)2, 2, true);
      tel.send();
      final byte[] answer = tel.recieve();
      System.out.println("Answer:" + Arrays.toString(answer));
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
