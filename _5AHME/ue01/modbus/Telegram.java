/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _5AHME._5AHME.ue01.modbus;

/**
 *
 * @author User
 */
public interface Telegram
{
  public void send()
          throws Exception;
  public byte[] recieve()
          throws Exception;
}
