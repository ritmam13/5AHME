/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _5AHME._5AHME.ue01.modbus;

import _5AHME._5AHME.ue01.serial.*;
import java.util.*;

/**
 *
 * @author User
 */
public abstract class AbstractTelegram implements Telegram
{
  private final SimpleSerial serial;
  private final byte busAddress, functionCode;
  private final byte[] xmtData;
  private final int lenghtOfAnswer;
  private byte[] recieved;

  public AbstractTelegram(SimpleSerial serial, byte busAddress, byte functionCode, byte[] xmtData, int lenghtOfAnswer)
  {
    this.serial = serial;
    this.busAddress = busAddress;
    this.functionCode = functionCode;
    this.xmtData = xmtData;
    this.lenghtOfAnswer = lenghtOfAnswer;
  }
  
  @Override
  public void send() throws Exception
  {
    serial.purgeInput();
    final int bytesToSend = xmtData.length + 4;
    final byte[] toSend = new byte[bytesToSend];
    toSend[0] = busAddress;
    toSend[1] = functionCode;
    System.arraycopy(xmtData, 0, toSend, 2, xmtData.length);
    final int crc = calcCrc(toSend,0, bytesToSend-2);
    toSend[bytesToSend-2] = (byte)(crc & 0xFF);
    toSend[bytesToSend-1] = (byte) ((crc>>8) & 0xFF);
    System.out.println("Gesendet:" + Arrays.toString(toSend));
    serial.writeBytes(toSend);
  }

  @Override
  public byte[] recieve() throws Exception
  {
    final int bytestoRecieve = lenghtOfAnswer +4;
    recieved = serial.readBytes(bytestoRecieve,5000);                  //  TODO
    // Plausikontrolle 
    if (recieved[0] != busAddress)
      throw new Exception("Illegal device address returned");
    if (recieved[1] != functionCode)
      throw new Exception("Illegal Function Code");
    // TODO: CRC überprüfen 
    return recieved;
  }

  protected byte[] getRecieved()
  {
    return recieved;
  }
  
  private static int calcCrc(byte[] data, int start, int cnt)
  {
    int crc = 0xFFFF;
    for (int i=start; i<start+cnt; i++)
      crc=crc16(crc, data[i]);
    return crc;
  }
  
  private static int crc16(int crc, int data)
  {
    final int poly16 = 0xA001;
    int lsb;
    crc = ((crc ^ data) | 0xFF00) & (crc | 0x00FF);
    for (int i = 0; i < 8; i++)
    {
      lsb = (crc & 0x0001);
      crc = crc >> 1;
      if(lsb !=0)
        crc ^= poly16;
    }
    return crc;
  }
  
}
