import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите что нужно рассчитать: ");
        String expression = scanner.nextLine();

        String[] parts = expression.split("\\s+");
        if (parts.length != 3) {
            System.out.println("Некоррктный формат");
            return;
        }

        try {
            int result = Calculator.calculate(parts[0], parts[1], parts[2]);
            if (RomanNumeral.isValidRomanNumeral(parts[0]) && RomanNumeral.isValidRomanNumeral(parts[2])) {
                System.out.println("Результат: " + RomanNumeral.toRomanNumeral(result));
            } else {
                System.out.println("Результат: " + result);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
    private static final Map<Character, Integer> ROMAN_NUMERALS = new HashMap<>();
    static {
        ROMAN_NUMERALS.put('I', 1);
        ROMAN_NUMERALS.put('V', 5);
        ROMAN_NUMERALS.put('X', 10);
    }

    public static int calculate(String operand1, String operator, String operand2) {
        int a = parseOperand(operand1);
        int b = parseOperand(operand2);

        switch (operator) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return a / b;
            default:
                throw new IllegalArgumentException("Некорректный оператор " + operator);
        }
    }

    private static int parseOperand(String operand) {
        if (operand.matches("^\\d+$")) {
            int value = Integer.parseInt(operand);
            if (value < 1 || value > 10) {
                throw new IllegalArgumentException("Некорректное значение " + value);
            }
            return value;
        } else if (operand.matches("^[IVX]+$")) {
            int value = 0;
            for (int i = 0; i < operand.length(); i++) {
                char c = operand.charAt(i);
                int v = ROMAN_NUMERALS.get(c);
                if (i + 1 < operand.length()) {
                    char next = operand.charAt(i + 1);
                    int nextV = ROMAN_NUMERALS.get(next);
                    if (nextV > v) {
                        value -= v;
                    } else {
                        value += v;
                    }
                } else {
                    value += v;
                }
            }
            if (value < 1 || value > 10) {
                throw new IllegalArgumentException("Некорректное значение " + operand);
            }
            return value;
        } else {
            throw new IllegalArgumentException("Некорректное значение" + operand);
        }
    }

    public class RomanNumeral {
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

        public static int toArabicNumeral(String s) {
            int result = 0;
            int prev = 0;
            for (int i = s.length() - 1; i >= 0; i--) {
                int curr = MAP.get(s.charAt(i));
                if (i == s.length() - 1) {
                    result += curr;
                } else {
                    if (curr >= prev) {
                        result += curr;
                    } else {
                        result -= curr;
                    }
                }
                prev = curr;
            }
            return result;
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