public class Response {

    public byte tml;
    public byte requestID; 
    public byte errorCode;
    public int result;
    

  public Response(byte tml, byte requestID, byte errorCode, int result)  {
      this.tml = tml;
      this.requestID = requestID;
      this.errorCode = errorCode;
      this.result = result;
  }

  public String toString() {
    final String EOLN = java.lang.System.getProperty("line.separator");
    String value = "\tTotal Message Length     : " + tml + EOLN +
                   "\tRequest ID               : " + requestID + EOLN +
                   "\tError Code               : " + errorCode + EOLN +
                   "\tResult                   : " + result + EOLN;

    return value;
  }
}
