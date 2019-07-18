import java.io.*;  // for ByteArrayInputStream
import java.net.*; // for DatagramPacket

public class RequestDecoderBin implements RequestDecoder, RequestBinConst {

  private String encoding;  // Character encoding

  public RequestDecoderBin() {
    encoding = DEFAULT_ENCODING;
  }

  public RequestDecoderBin(String encoding) {
    this.encoding = encoding;
  }

  public Request decode(InputStream wire) throws IOException {
    DataInputStream src = new DataInputStream(wire);
    byte tml = src.readByte();
    byte requestID = src.readByte();
    byte opCode = src.readByte();
    byte numOperands = src.readByte();
    short operand1  = src.readShort();

    if (numOperands == 2) {
      short operand2 = src.readShort();
      return new Request(tml, requestID, opCode, numOperands, operand1, operand2);
    }

    return new Request(tml, requestID, opCode, numOperands, operand1);
  }

  public Request decode(DatagramPacket p) throws IOException {
    ByteArrayInputStream payload =
      new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
    return decode(payload);
  }
}
