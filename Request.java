public class Request {

    public byte tml;
    public byte requestID; 
    public byte opCode;
    public byte numOperands;
    public short operand1;
    public short operand2;
    

  public Request(byte tml, byte requestID, byte opCode, byte numOperands, short operand1, 
		short operand2)  {
      this.tml = tml;
      this.requestID = requestID;
      this.opCode = opCode;
      this.numOperands = numOperands;
      this.operand1 = operand1;
      this.operand2 = operand2;
  }

    public Request(byte tml, byte requestID, byte opCode, byte numOperands, short operand1)  {
      this.tml = tml;
      this.requestID = requestID;
      this.opCode = opCode;
      this.numOperands = numOperands;
      this.operand1 = operand1;
  }


  public String toString() {
    final String EOLN = java.lang.System.getProperty("line.separator");

    String value = "";

    value =  "\tTotal Message Length    : " + tml + EOLN +
             "\tRequest ID              : " + requestID + EOLN +
             "\tOp Code                 : " + opCode + EOLN +
             "\tNumber of Operands      : " + numOperands + EOLN +
             "\tOperand 1               : " + operand1 + EOLN;

    if(this.numOperands == 2) {
      value = value + "\tOperand 2               : " + operand2 + EOLN;
    }

    return value;
  }
}
