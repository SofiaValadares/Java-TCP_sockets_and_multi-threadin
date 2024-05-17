package src.pt.fe.up.cpd.t4g11.main.model;

public class Calculation {
    private final short num1;
    private final Operand op;
    private final short num2;

    public Calculation(short num1, Operand op, short num2) {
        this.num1 = num1;
        this.op = op;
        this.num2 = num2;
    }

    public short getResult() {
        switch (op) {
            case SUM -> {
                return  (short) (this.num1 + this.num2);
            }
            case SUB -> {
                return (short) (this.num1 - this.num2);
            }
            case MULT -> {
                return (short) (this.num1 * this.num2);
            }
            case DIV -> {
                return (short) (this.num1 / this.num2);
            }
            default -> {
                return 0;
            }
        }
    }

    public short givePoints() {
        return 5;
    }

    private String getOperandChar() {
        switch (op) {
            case SUM -> {
                return  "+";
            }
            case SUB -> {
                return "-";
            }
            case MULT -> {
                return "*";
            }
            case DIV -> {
                return "/";
            }
            default -> {
                return "none";
            }
        }
    }

    @Override
    public String toString() {
        return this.num1 + " " + getOperandChar() + " " + this.num2;
    }
}