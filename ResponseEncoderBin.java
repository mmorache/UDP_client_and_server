import java.io.*;  // for ByteArrayOutputStream and DataOutputStream

public class ResponseEncoderBin implements ResponseEncoder, ResponseBinConst {

  private String encoding;  // Character encoding

  public ResponseEncoderBin() {
    encoding = DEFAULT_ENCODING;
  }

  public ResponseEncoderBin(String encoding) {
    this.encoding = encoding;
  }

  public byte[] encode(Response response) throws Exception {

    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(buf);
    out.writeByte(response.tml);
    out.writeByte(response.requestID);
    out.writeByte(response.errorCode);
    out.writeInt(response.result);

    out.flush();
    return buf.toByteArray();
  }
}
