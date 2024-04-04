import java.io.PrintWriter;

public class Validation {

    public Validation() {}

    public int validInteger(String number) {
        int parsedNumber;
        try {
            parsedNumber = Integer.parseInt(number);
            return parsedNumber;
        } catch (NumberFormatException n) {
            return -1;
        }
    }
}
