import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException

public class SendUDP {

  public static void main(String args[]) throws Exception {

    long timerStart;
    long timerDif;
    float timerElapsed;
    int reqID = 1;
    Request request;


    // Test for correct # of args and open socket 
    if (args.length != 2 && args.length != 3)       
      throw new IllegalArgumentException("Parameter(s): <Destination>" +
					     " <Port> [<encoding]");
    InetAddress destAddr = InetAddress.getByName(args[0]);  // Destination address
    int destPort = Integer.parseInt(args[1]);               // Destination port    
    DatagramSocket sock = new DatagramSocket();

    //Collect user input
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    while(true) {

      int tml = 6;
      int opCode = 7;
      while (opCode >6) {
        System.out.println("\nEnter an Op Code : " +
          "\n\t0 for addition" +
          "\n\t1 for subtraction" +
          "\n\t2 for bitwise OR" +
          "\n\t3 for bitwise AND" +
          "\n\t4 for SHIFT RIGHT" +
          "\n\t5 for SHIFT LEFT" +
          "\n\t6 for bitwise NOT");
        opCode = Integer.parseInt(inFromUser.readLine());
        if (opCode > 6 || opCode < 0) {
          System.out.println("\nINVALID : You must enter a value between 0 and 6");
          opCode = 7;
        }
      }

      int op1 = 32768;
      while (op1 >32767) {
        System.out.println("\nEnter an operand between -32,768 to 32,767 : ");
        op1 = Integer.parseInt(inFromUser.readLine());
        if (op1 > 32767 || op1 < -32768) {
          System.out.println("\nINVALID : This value is out of range");
          op1 = 32768;
        }
      }

      int numOp = 1;
      int op2 = 32768;

      if (opCode < 6) {
        numOp = 2;
        tml = 8;
        while (op2 >32767) {
          System.out.println("\nEnter another operand between -32,768 to 32,767 : ");
          op2 = Integer.parseInt(inFromUser.readLine());
          if (op2 > 32767 || op2 < -32768) {
            System.out.println("\nINVALID : This value is out of range");
            op2 = 32768;
          }
        }
      }

      if (numOp == 2) {
        request = new Request( (byte) tml, (byte) reqID, (byte) opCode, (byte) numOp,
    				(short) op1, (short) op2);
      }

      else {
        request = new Request( (byte) tml, (byte) reqID, (byte) opCode, (byte) numOp,
          (short) op1);
      }
        
      // Send outgoing request to server
      RequestEncoder encoder = (args.length == 3 ?
  			  new RequestEncoderBin(args[2]) :
  			  new RequestEncoderBin());
      byte[] codedRequest = encoder.encode(request);
      DatagramPacket message = new DatagramPacket(codedRequest, codedRequest.length, 
  					  destAddr, destPort);

      timerStart = System.nanoTime(); //start timer
      sock.send(message);

      // Print outgoing request binary
      System.out.print("\nSent Binary-Encoded Request      : ");
      for (byte b : codedRequest) {
          String st = String.format("%02X", b);
          System.out.print("0x" + st + " ");
      }
      System.out.println(" ");

      // Receive incoming response from server
      DatagramPacket messageBack = new DatagramPacket(new byte[1024],1024);
      sock.receive(messageBack);
      timerDif = System.nanoTime() - timerStart; //stop timer
      timerElapsed = (float) timerDif / 1000000;
      ResponseDecoder decoder = new ResponseDecoderBin();
      Response receivedResponse = decoder.decode(messageBack);

      //Print incoming response binary
      System.out.print("Received Binary-Encoded Response : ");
      ResponseEncoder encoderResp = new ResponseEncoderBin();
      byte[] codedResponse = encoderResp.encode(receivedResponse);
      for (byte b : codedResponse) {
        String st = String.format("%02X", b);
        System.out.print("0x" + st + " ");
      }
      System.out.println(" ");
      System.out.println(" ");

      String equation = "";
      if (opCode == 6) {
        equation = "~" + Integer.toString(op1) + " = ";
      }

      else{

        equation = Integer.toString(op1);
        String symbol = "";

        switch (opCode) {
          case 0: symbol = " + ";
          break;
          case 1: symbol = " - ";
          break;
          case 2: symbol = " OR ";
          break;
          case 3: symbol = " AND ";
          break;
          case 4: symbol = " >> ";
          break;
          case 5: symbol = " << ";
          break;
          default: System.out.println("Something went wrong");
        }

        equation = equation + symbol + Integer.toString(op2) + " = ";
      }

      equation = equation + Integer.toString(receivedResponse.result);

      String errorString = "";

      if (receivedResponse.errorCode == 0) {
        errorString += " (Request has no errors)";
      }

      else{
        errorString += (" (Request was invalid)");
      }

      //Print incoming response in plain text
      System.out.println("Total Message Length of Request  : " + Integer.toString(tml) + " Bytes");
      System.out.println("Total Message Length of Response : " + Integer.toString(receivedResponse.tml) + " Bytes");
      System.out.println("Request Identification Number    : " + Integer.toString(receivedResponse.requestID));
      System.out.println("Error Code                       : " + Integer.toString(receivedResponse.errorCode) + errorString);
      System.out.println("Result                           : " + equation);
     // System.out.println(receivedResponse);
      System.out.println("Elapsed time                     : " + Float.toString(timerElapsed) + " milliseconds\n");

      if(reqID == 127){
        reqID = 0;
      }
      reqID++;

      System.out.println("Press 'Enter' to continue");
      inFromUser.readLine();
    }
    
  }
  //sock.close();
}
