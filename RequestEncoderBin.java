import java.io.*;  // for ByteArrayOutputStream and DataOutputStream

public class RequestEncoderBin implements RequestEncoder, RequestBinConst {

  private String encoding;  // Character encoding

  public RequestEncoderBin() {
    encoding = DEFAULT_ENCODING;
  }

  public RequestEncoderBin(String encoding) {
    this.encoding = encoding;
  }

  public byte[] encode(Request request) throws Exception {

    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(buf);
    out.writeByte(request.tml);
    out.writeByte(request.requestID);

    out.writeByte(request.opCode);
    out.writeByte(request.numOperands);

    out.writeShort(request.operand1);

    if(request.numOperands == 2) {
      out.writeShort(request.operand2);
    }

    out.flush();
    return buf.toByteArray();
  }
}
