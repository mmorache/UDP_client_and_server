import java.io.*;  // for ByteArrayInputStream
import java.net.*; // for DatagramPacket

public class ResponseDecoderBin implements ResponseDecoder, ResponseBinConst {

  private String encoding;  // Character encoding

  public ResponseDecoderBin() {
    encoding = DEFAULT_ENCODING;
  }

  public ResponseDecoderBin(String encoding) {
    this.encoding = encoding;
  }

  public Response decode(InputStream wire) throws IOException {

    DataInputStream src = new DataInputStream(wire);
    byte tml = src.readByte();
    byte requestID = src.readByte();
    byte errorCode = src.readByte();
    int result = src.readInt();

    return new Response(tml,requestID, errorCode, result);
  }

  public Response decode(DatagramPacket p) throws IOException {
    ByteArrayInputStream payload =
      new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
    return decode(payload);
  }
}
