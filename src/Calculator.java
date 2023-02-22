import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Calculator {

    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+$");
    private static final Pattern ROMAN_PATTERN = Pattern.compile("^[IVX]+$");
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите две операнды и один оператор (+, -, /, *) для рассчёта: ");
        String expression = scanner.nextLine();
        String[] parts = expression.split("\\s+");

        if (parts.length < 3) {
            throw new Exception("Строка не является математической операцией");
        } else if (parts.length > 3) {
            throw new Exception("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }

            int result = calculate(parts[0], parts[1], parts[2]);

            if (RomanNumeral.isValidRomanNumeral(parts[0]) && RomanNumeral.isValidRomanNumeral(parts[2])) {
                if (RomanNumeral.toRomanNumeral(result).equals("")) {
                    throw new Exception("В римской системе нет отрицательных чисел");
                }
                System.out.println("Результат: " + RomanNumeral.toRomanNumeral(result));
            } else {
                System.out.println("Результат: " + result);
            }

    }

    public static int calculate(String operand1, String operator, String operand2) throws Exception {

        if (RomanNumeral.isValidRomanNumeral(operand1) && !RomanNumeral.isValidRomanNumeral(operand2) ||
        RomanNumeral.isValidRomanNumeral(operand2) && !RomanNumeral.isValidRomanNumeral(operand1)) {
            throw new Exception("Используются одновременно разные системы счисления");
        }

        int a = parseOperand(operand1);
        int b = parseOperand(operand2);

        return switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            default -> throw new Exception("Некорректный оператор " + operator);
        };
    }

    private static int parseOperand(String operand) {
        try {
            if (NUMERIC_PATTERN.matcher(operand).matches()) {
                int value = Integer.parseInt(operand);
                if (value < 1 || value > 10) {
                    throw new NumberFormatException("Калькулятор может принимать на вход числа от 1 до 10 включительно");
                }
                return value;
            } else if (ROMAN_PATTERN.matcher(operand).matches()) {
                int value = 0;
                for (int i = 0; i < operand.length(); i++) {
                    char c = operand.charAt(i);
                    int v = RomanNumeral.MAP.get(c);
                    if (i + 1 < operand.length()) {
                        char next = operand.charAt(i + 1);
                        int nextV = RomanNumeral.MAP.get(next);
                        if (nextV > v) {
                            value -= v;
                        } else {
                            value += v;
                        }
                    } else {
                        value += v;
                    }
                }
                if (value > 10 || value < 1) {
                    throw new Exception("Калькулятор может принимать на вход числа от 1 до 10 включительно");
                }
                return value;
            } else {
                throw new IllegalArgumentException("Некорректное значение операнды: " + operand);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    public static class RomanNumeral {
        private static final Map<Character, Integer> MAP = new HashMap<>();
        static {
            MAP.put('I', 1);
            MAP.put('V', 5);
            MAP.put('X', 10);
        }

        public static boolean isValidRomanNumeral(String s) {

            for (char c : s.toCharArray()) {
                if (!MAP.containsKey(c)) {
                    return false;
                }
            }
            return true;
        }

        public static String toRomanNumeral(int num) {

            StringBuilder roman = new StringBuilder();
            int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
            String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

            for (int i = 0; i < values.length; i++) {
                while (values[i] <= num) {
                    num -= values[i];
                    roman.append(symbols[i]);
                }
            }

            return roman.toString();
        }
    }
}