package src.pt.fe.up.cpd.t4g11.main.model;

import java.util.Random;

public class MathGame {

    public static Calculation getRandomCalculation() {
        short num1 = getRandomShort();
        short num2 = getRandomShort();
        Operand op = getRandomOperator();

        switch (op) {
            case DIV -> {
                while (num1 % num2 != 0) {
                    num1 = getRandomShort();
                }
            }
        }

        return new Calculation(num1, op, num2);
    }

    // Helper method to generate a random short number
    private static short getRandomShort() {
        Random random = new Random();
        return (short) (random.nextInt(99) + 1);
    }

    // Helper method to generate a random operator
    private static Operand getRandomOperator() {
        Random random = new Random();
        return Operand.values()[random.nextInt(Operand.values().length)];
    }
}