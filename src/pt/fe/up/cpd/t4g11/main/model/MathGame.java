package src.pt.fe.up.cpd.t4g11.main.model;

import java.util.Random;

public class MathGame {

    public static short getRandomCalculation() {
        short num1 = getRandomShort();
        short num2 = getRandomShort();
        Operand op = getRandomOperator();

        Calculation calculation = new Calculation(num1, op, num2);
        return calculation.getResult();
    }

    // Helper method to generate a random short number
    private static short getRandomShort() {
        Random random = new Random();
        return (short) random.nextInt(Short.MAX_VALUE + 1);
    }

    // Helper method to generate a random operator
    private static Operand getRandomOperator() {
        Random random = new Random();
        return Operand.values()[random.nextInt(Operand.values().length)];
    }
}