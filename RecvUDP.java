import java.net.*;  // for DatagramSocket and DatagramPacket
import java.io.*;   // for IOException

public class RecvUDP {

  public static void main(String[] args) throws Exception {

     // Test for correct # of args  
    if (args.length != 1 && args.length != 2)       
      throw new IllegalArgumentException("Parameter(s): <Port> [<encoding>]");
    int port = Integer.parseInt(args[0]);

    int GroupID = 1;

    port = port + GroupID;
  
    //Open UDP socket for receiving   
    DatagramSocket sock = new DatagramSocket(port);   

    while (true) {
      DatagramPacket packet = new DatagramPacket(new byte[1024],1024);
      sock.receive(packet);
      
      //Receive incoming request from client
      RequestDecoder decoder = (args.length == 2 ?            
  			  new RequestDecoderBin(args[1]) :
  			  new RequestDecoderBin() );
      Request receivedRequest = decoder.decode(packet);

      //Calculate result
      int result = 0;
      switch (receivedRequest.opCode) {

        case 0: result = (int) receivedRequest.operand1 + (int) receivedRequest.operand2;
        break;

        case 1: result = (int) receivedRequest.operand1 - (int) receivedRequest.operand2;
        break;

        case 2: result = (int) (receivedRequest.operand1 | (int) receivedRequest.operand2);
        break;

        case 3: result = (int) (receivedRequest.operand1 & (int) receivedRequest.operand2);
        break;

        case 4: result = (int) (receivedRequest.operand1 >> (int) receivedRequest.operand2);
        break;

        case 5: result = (int) (receivedRequest.operand1 << (int) receivedRequest.operand2);
        break;

        case 6: result = (int) (~receivedRequest.operand1);
        break;

        default: System.out.println("Something went wrong");
      }

      //Send outgoing response to client
      int errorCode = 0;
      if (packet.getLength() != receivedRequest.tml) {
        errorCode = 127;
      }

      Response response = new Response((byte)7, receivedRequest.requestID, (byte) errorCode, (int) result);
      ResponseEncoder encoder = new ResponseEncoderBin();
      byte[] codedResponse = encoder.encode(response);

      InetAddress IPAddress = packet.getAddress();
      int port2 = packet.getPort();

      DatagramPacket message = new DatagramPacket(codedResponse, codedResponse.length, 
          IPAddress, port2);
      sock.send(message);

      //Print incoming request binary
      System.out.print("\nReceived Binary-Encoded Request : ");
      RequestEncoder encoderReq = new RequestEncoderBin();

      byte[] codedReq = encoderReq.encode(receivedRequest);
      for (byte b : codedReq) {
        String st = String.format("%02X", b);
        System.out.print("0x" + st + " ");
      }

      //Print outgoing response binary
      System.out.print("\nSent Binary-Encoded Response    : ");
      for (byte b : codedResponse) {
        String st = String.format("%02X", b);
        System.out.print("0x" + st + " ");
      }
      System.out.println(" ");

      //Print incoming request plain text
      System.out.println("\nRequest from Client             : ");
      System.out.println(receivedRequest);
      System.out.println("Calculated Result               : " + result + "\n");
  }

    //sock.close();
  }
}
