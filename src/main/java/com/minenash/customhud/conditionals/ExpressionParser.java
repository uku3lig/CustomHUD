package com.minenash.customhud.conditionals;

import com.minenash.customhud.HudElements.list.ListProvider;
import com.minenash.customhud.complex.ComplexData;
import com.minenash.customhud.HudElements.HudElement;
import com.minenash.customhud.VariableParser;
import com.minenash.customhud.data.Profile;
import com.minenash.customhud.errors.ErrorException;
import com.minenash.customhud.errors.ErrorType;
import com.minenash.customhud.errors.Errors;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("DuplicatedCode")
public class ExpressionParser {

    public enum TokenType { START_PREN, END_PREN, FULL_PREN, FUNCTION, AND, OR, MATH, COMPARISON, NUMBER, STRING, BOOLEAN, VARIABLE, IF, ELSE, TERNARY }
    public enum Comparison { LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUALS, EQUALS, NOT_EQUALS }
    public enum MathOperator { ADD, SUBTRACT, MULTIPLY, DIVIDE, MOD, EXPONENT }
    public record TernaryTokens(List<Token> conditional, List<Token> left, List<Token> right) {}

    record Token(TokenType type, Object value) {
        public String toString() {
            return type + (value == null ? "" : " (" + value + ")");
        }
    }

    public static Operation parseExpression(String input, String source, Profile profile, int debugLine, ComplexData.Enabled enabled, ListProvider listSupplier) {
        if (input.isBlank() || input.equals(",") || input.equals(", "))
            return new Operation.Literal(1);
        try {
            List<Token> tokens = getTokens(input, profile, debugLine, enabled, listSupplier);
            Operation c = getConditional(tokens);
            System.out.println("Tree for Conditional on line " + debugLine + ":");
            c.printTree(0);
            System.out.println();
            return c;
        }
        catch (ErrorException e) {
            Errors.addError(profile.name, debugLine, source, e.type, e.context);
            System.out.println("[Line: " + debugLine + "] Conditional Couldn't Be Parsed: " + e.getMessage());
            System.out.println("Input: \"" + input + "\"");
            return new Operation.Literal(1);
        }
    }

    private static final List<TokenType> SUBTRACTABLE = List.of(TokenType.NUMBER, TokenType.BOOLEAN,
            TokenType.STRING, TokenType.VARIABLE, TokenType.END_PREN);
    private static List<Token> getTokens(String original, Profile profile, int debugLine, ComplexData.Enabled enabled, ListProvider listSupplier) throws ErrorException {

        List<Token> tokens = new ArrayList<>();
        char[] chars = original.toCharArray();



        for (int i = 0; i < chars.length;) {
            char c = chars[i];
            if (c == '(') tokens.add(new Token(TokenType.START_PREN, null));
            else if (c == ')') tokens.add(new Token(TokenType.END_PREN, null));
            else if (c == '?') tokens.add(new Token(TokenType.IF, null));
            else if (c == ';') tokens.add(new Token(TokenType.ELSE, null));
            else if (c == '|') {
                if (i + 1 != chars.length && chars[i + 1] == '|') i++;
                tokens.add(new Token(TokenType.OR, null));
            }
            else if (c == '&') {
                if (i + 1 != chars.length && chars[i + 1] == '&') i++;
                tokens.add(new Token(TokenType.AND, null));
            }
            else if (c == '=') {
                if (i + 1 != chars.length && chars[i + 1] == '=') i++;
                tokens.add(new Token(TokenType.COMPARISON, Comparison.EQUALS));
            }
            else if (c == '+') tokens.add(new Token(TokenType.MATH, MathOperator.ADD));
            else if (c == '*') tokens.add(new Token(TokenType.MATH, MathOperator.MULTIPLY));
            else if (c == '/') tokens.add(new Token(TokenType.MATH, MathOperator.DIVIDE));
            else if (c == '%') tokens.add(new Token(TokenType.MATH, MathOperator.MOD));
            else if (c == '^') tokens.add(new Token(TokenType.MATH, MathOperator.EXPONENT));
            else if (c == '-' && i+1 < chars.length && (!tokens.isEmpty() && SUBTRACTABLE.contains(tokens.get(tokens.size() - 1).type)))
                tokens.add(new Token(TokenType.MATH, MathOperator.SUBTRACT));
            else if (c == '!') {
                if (i + 1 == chars.length || chars[i + 1] != '=')
                    throw new ErrorException(ErrorType.CONDITIONAL_UNEXPECTED_VALUE, "!");
                tokens.add(new Token(TokenType.COMPARISON, Comparison.NOT_EQUALS));
                i += 2;
                continue;
            }
            else if (c == '>') {
                boolean hasEqual =  i + 1 != chars.length && chars[i + 1] == '=';
                tokens.add(new Token(TokenType.COMPARISON, hasEqual ? Comparison.GREATER_THAN_OR_EQUALS : Comparison.GREATER_THAN));
                i += hasEqual ? 2 : 1;
                continue;
            }
            else if (c == '<') {
                boolean hasEqual =  i + 1 != chars.length && chars[i + 1] == '=';
                tokens.add(new Token(TokenType.COMPARISON, hasEqual ? Comparison.LESS_THAN_OR_EQUAL : Comparison.LESS_THAN));
                i += hasEqual ? 2 : 1;
                continue;
            }
            else if (c == 'f' && i + 4 < chars.length && original.startsWith("false", i)) {
                tokens.add(new Token(TokenType.BOOLEAN, false));
                i+=5;
                continue;
            }
            else if (c == 't' && i + 3 < chars.length && original.startsWith("true", i)) {
                tokens.add(new Token(TokenType.BOOLEAN, true));
                i+=4;
                continue;
            }
            else if (c == '"') {
                StringBuilder builder = new StringBuilder();
                i++;
                while (i < chars.length && chars[i] != '"')
                    builder.append(chars[i++]);
                tokens.add(new Token(TokenType.STRING, builder.toString()));
            }
            else if (isNum(c) || c == '-') {
                StringBuilder builder = new StringBuilder();
                builder.append(chars[i++]);
                while (i < chars.length && isNum(chars[i]))
                    builder.append(chars[i++]);
                tokens.add(new Token(TokenType.NUMBER, Double.parseDouble(builder.toString())));
                continue;
            }
            else if (isVar(c)) {
                Pair<Token,Integer> func = getFunctionStart(chars, i);
                if (func != null) {
                    tokens.add(func.getLeft());
                    i += func.getRight();
                    continue;
                }

                StringBuilder builder = new StringBuilder();
                builder.append('{');
                while (i < chars.length && isVar(chars[i])) {
                    builder.append(chars[i]);
                    i++;
                }
                builder.append('}');
                HudElement element = VariableParser.parseElement(builder.toString(), profile, debugLine, enabled, listSupplier);
                if (element == null)
                    tokens.add(new Token(TokenType.BOOLEAN, false));
                else
                    tokens.add(new Token(TokenType.VARIABLE, element));
                continue;
            }
            i++;

        }

//        for (Token token : tokens)
//            System.out.println("[A]" + token);

        int start = -1;
        for (int i = 0; i < tokens.size(); i++) {
            TokenType type = tokens.get(i).type();
            if (type == TokenType.START_PREN) {
                start = i;
            }
            else if (type == TokenType.END_PREN) {
                reducePren(tokens, start, i);
                start = -1;
                i = -1;
            }
        }

        reduceTernary0(tokens);

//        System.out.println("---------------");
//        for (Token token : tokens)
//            System.out.println("[B]" + token);
        return tokens;

    }

    private static boolean isNum(char c) {
        return c == '.' || (c >= '0' && c <= '9');
    }
    private static boolean isVar(char c) {
        return c == '.' || c == ':' || c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')
                || c == '[' || c == ']' || c == ',';
    }


    private static final double DR = 0.017453292519943295;
    private static final double RD = 57.29577951308232;
    private static final Function<Double,Double> SIN  = (in) -> round (Math.sin(in * DR) );
    private static final Function<Double,Double> COS  = (in) -> round (Math.cos(in * DR) );
    private static final Function<Double,Double> TAN  = (in) -> round (Math.tan(in * DR) );
    private static final Function<Double,Double> CSC  = (in) -> round (1 / (Math.sin(in * DR)) );
    private static final Function<Double,Double> SEC  = (in) -> round (1 / (Math.cos(in * DR)) );
    private static final Function<Double,Double> COT  = (in) -> round (1 / (Math.tan(in * DR)) );
    private static final Function<Double,Double> ASIN = (in) -> round (RD * Math.asin(in) );
    private static final Function<Double,Double> ACOS = (in) -> round (RD * Math.acos(in) );
    private static final Function<Double,Double> ATAN = (in) -> round (RD * Math.atan(in) );
    private static final Function<Double,Double> ACSC = (in) -> round (RD * Math.asin(1 / in) );
    private static final Function<Double,Double> ASEC = (in) -> round (RD * Math.acos(1 / in) );
    private static final Function<Double,Double> ACOT = (in) -> round (RD * Math.atan(1 / in) );
    private static final Function<Double,Double> ROUND = (in) -> (double) Math.round(in);
    private static double round(double in) { return Math.round(in * 100000) / 100000D; }

    private static Pair<Token,Integer> getFunctionStart(char[] chars, int i) {
        int pren = -1;
        for (int j = i; j < chars.length; j++) {
            if (chars[j] == '(') {
                pren = j;
                break;
            }
            if (!isFunc(chars[j]) )
                return null;
        }

        String funcStr = new String(Arrays.copyOfRange(chars, i, pren));
        Function<Double,Double> func = switch ( funcStr ) {
            case "sin" -> SIN;
            case "cos" -> COS;
            case "tan" -> TAN;
            case "csc" -> CSC;
            case "sec" -> SEC;
            case "cot" -> COT;
            case "asin" -> ASIN;
            case "acos" -> ACOS;
            case "atan" -> ATAN;
            case "acsc" -> ACSC;
            case "asec" -> ASEC;
            case "acot" -> ACOT;

            case "round" -> ROUND;
            case "ceil" -> Math::ceil;
            case "floor" -> Math::floor;
            case "ln" -> Math::log;
            case "log" -> Math::log10;
            case "sqrt" -> Math::sqrt;
            case "abs" -> Math::abs;
            default -> null;
        };
        return func == null ? null : new Pair<>(new Token(TokenType.START_PREN, func), funcStr.length()+1);

    }
    private static boolean isFunc(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }


    private static void reducePren(List<Token> original, int start, int end) {
        Function<Double,Double> func = (Function<Double, Double>) original.get(start).value;
        if (func == null)
            original.set(start, new Token(TokenType.FULL_PREN, new ArrayList<>(original.subList(start+1, end))));
        else
            original.set(start, new Token(TokenType.FUNCTION, new Pair<>( func, new ArrayList<>(original.subList(start+1, end)))));
        for (; end > start; end--)
            original.remove(end);
    }

    private static void reduceTernary0(List<Token> original) {
        for (Token token : original)
            if (token.type == TokenType.FULL_PREN)
                reduceTernary0( (List<Token>) token.value );
        reduceTernary(original);
    }

    private static void reduceTernary(List<Token> original) {
        boolean foundIf;
        do {
            int start = 0;
            int ifIndex = -1;
            int elseIndex = -1;
            foundIf = false;

            for (int i = 0; i < original.size(); i++) {
                TokenType type = original.get(i).type();
                if (type == TokenType.IF) {
                    foundIf = true;
                    start = elseIndex != -1 ? elseIndex + 1 : ifIndex + 1;
                    ifIndex = i;
                    elseIndex = -1;
                } else if (type == TokenType.ELSE && ifIndex != -1) {
                    if (elseIndex == -1) {
                        elseIndex = i;
                    } else {
                        List<Token> conditional = new ArrayList<>(original.subList(start, ifIndex));
                        List<Token> left = new ArrayList<>(original.subList(ifIndex + 1, elseIndex));
                        List<Token> right = new ArrayList<>(original.subList(elseIndex + 1, i));
                        Token ter = new Token(TokenType.TERNARY, new TernaryTokens(conditional, left, right));
                        original.set(start, ter);
                        while (--i > start)
                            original.remove(i);
                        i = -1;
                        start = 0;
                        ifIndex = elseIndex = -1;
                    }
                }
            }
            if (elseIndex != -1) {
                List<Token> conditional = new ArrayList<>(original.subList(start, ifIndex));
                List<Token> left = new ArrayList<>(original.subList(ifIndex + 1, elseIndex));
                List<Token> right = new ArrayList<>(original.subList(elseIndex + 1, original.size()));
                Token ter = new Token(TokenType.TERNARY, new TernaryTokens(conditional, left, right));
                original.set(start, ter);
                if (original.size() > start + 1)
                    original.subList(start + 1, original.size()).clear();
            } else if (ifIndex != -1) {
                List<Token> conditional = new ArrayList<>(original.subList(start, ifIndex));
                List<Token> left = new ArrayList<>(original.subList(ifIndex + 1, original.size()));
                List<Token> right = Collections.emptyList();
                Token ter = new Token(TokenType.TERNARY, new TernaryTokens(conditional, left, right));
                original.set(start, ter);
                if (original.size() > start + 1)
                    original.subList(start + 1, original.size()).clear();
            }
        } while (foundIf);



    }

    private static Operation getConditional(List<Token> tokens) throws ErrorException {
        if (tokens.isEmpty())
            throw new ErrorException(ErrorType.EMPTY_TERNARY_SECTION, "");
        List<List<Token>> ors = split(tokens, TokenType.OR);
        List<Operation> conditionals = new ArrayList<>();
        for (var or : ors)
            conditionals.add(getAndConditional(or));

        return conditionals.size() == 1 ? conditionals.get(0) : new Operation.Or(conditionals);

    }

    private static Operation getAndConditional(List<Token> tokens) throws ErrorException {
        List<List<Token>> ands = split(tokens, TokenType.AND);
        List<Operation> conditionals = new ArrayList<>();
        for (var and : ands)
            conditionals.add(getComparisonOperation(and));

        return conditionals.size() == 1 ? conditionals.get(0) : new Operation.And(conditionals);
    }

    private static Operation getComparisonOperation(List<Token> tokens) throws ErrorException {
        if (tokens.size() == 1)
            return getPrimitiveOperation(tokens.get(0));

        int comparatorIndex = -1;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).type == TokenType.COMPARISON) {
                comparatorIndex = i;
                break;
            }
        }

        if (comparatorIndex == -1)
            return getMathOperation(tokens);
        else if (comparatorIndex == 0 )
            throw new ErrorException(ErrorType.CONDITIONAL_WRONG_NUMBER_OF_TOKENS, "No values on the left of comparison");
        else if (comparatorIndex == tokens.size()-1)
            throw new ErrorException(ErrorType.CONDITIONAL_WRONG_NUMBER_OF_TOKENS, "No values on the right of comparison");

        HudElement left = comparatorIndex == 1 ?
                getValueElement(tokens.get(0)) :
                new SudoElements.Op(getMathOperation(tokens.subList(0, comparatorIndex)));
        HudElement right = comparatorIndex == tokens.size()-2 ?
                getValueElement(tokens.get(tokens.size()-1)) :
                new SudoElements.Op(getMathOperation(tokens.subList(comparatorIndex+1, tokens.size())));

        return new Operation.Comparison(left, right, (Comparison) tokens.get(comparatorIndex).value());
    }

    private static Operation getMathOperation(List<Token> tokens) throws ErrorException {
        if (tokens.size() == 1)
            return getPrimitiveOperation(tokens.get(0));

        Pair<List<List<Token>>, List<MathOperator>> addPairs = split(tokens, List.of(MathOperator.ADD, MathOperator.SUBTRACT));
        List<Operation> ops = new ArrayList<>();

        for (var partTokens : addPairs.getLeft()) {
            Pair<List<List<Token>>, List<MathOperator>> multiplyPairs = split(partTokens, List.of(MathOperator.MULTIPLY, MathOperator.DIVIDE, MathOperator.MOD));
            List<Operation> ops2 = new ArrayList<>();

            for (var partPartToken : multiplyPairs.getLeft()) {
                Pair<List<List<Token>>, List<MathOperator>> exponentPairs = split(partPartToken, List.of(MathOperator.EXPONENT));
                List<HudElement> elements = new ArrayList<>();
                for (var partPartPartToken : exponentPairs.getLeft()) {
                    if (partPartPartToken.size() > 1)
                        throw new ErrorException(ErrorType.CONDITIONAL_WRONG_NUMBER_OF_TOKENS, "No operation between values");
                    elements.add( getValueElement(partPartPartToken.get(0)) );
                }
                if (elements.size() == 1)
                    ops2.add(new Operation.Element(elements.get(0)));
                else
                    ops2.add(new Operation.MathOperation(elements, exponentPairs.getRight()));
            }
            ops.add(ops2.size() == 1 ? ops2.get(0) : new Operation.MathOperationsOp(ops2, multiplyPairs.getRight()));
        }
        return ops.size() == 1 ? ops.get(0) : new Operation.MathOperationsOp(ops, addPairs.getRight());

    }

    @SuppressWarnings("unchecked")
    private static HudElement getValueElement(Token token) throws ErrorException {
        return switch (token.type()) {
            case VARIABLE -> (HudElement) token.value();
            case STRING -> new SudoElements.Str((String) token.value());
            case NUMBER -> new SudoElements.Num((Number) token.value());
            case BOOLEAN -> new SudoElements.Bool((Boolean) token.value());
            case FULL_PREN -> new SudoElements.Op(getConditional((List<Token>) token.value()));
            case FUNCTION -> {
                Pair<Function<Double,Double>,List<Token>> pair = (Pair<Function<Double, Double>, List<Token>>) token.value();
                yield new SudoElements.Op(new Operation.Func(pair.getLeft(), getConditional(pair.getRight())));
            }
            case TERNARY -> {
                TernaryTokens tokens = (TernaryTokens) token.value();
                yield new SudoElements.Op( new Operation.Ternary(
                        getConditional( tokens.conditional),
                        getConditional( tokens.left),
                        getConditional( tokens.right)
                ) );
            }
            case IF -> throw new ErrorException(ErrorType.CONDITIONAL_UNEXPECTED_VALUE, "? (IF)");
            case ELSE -> throw new ErrorException(ErrorType.CONDITIONAL_UNEXPECTED_VALUE, "; (ELSE)");

            default -> throw new ErrorException(ErrorType.CONDITIONAL_UNEXPECTED_VALUE, token.type().toString());
        };
    }

    @SuppressWarnings("unchecked")
    private static Operation getPrimitiveOperation(Token token) throws ErrorException {
        return switch (token.type) {
            case FULL_PREN -> getConditional((List<Token>) token.value());
            case BOOLEAN -> new Operation.Literal(((Boolean) token.value()) ? 1 : 0);
            case NUMBER -> new Operation.Literal((Double) token.value());
            case VARIABLE -> new Operation.Element((HudElement) token.value());
            case FUNCTION -> {
                Pair<Function<Double,Double>,List<Token>> pair = (Pair<Function<Double, Double>, List<Token>>) token.value();
                yield new Operation.Func(pair.getLeft(), getConditional(pair.getRight()));
            }
            case TERNARY -> {
                TernaryTokens tokens = (TernaryTokens) token.value();
                yield new Operation.Ternary(
                        getConditional( tokens.conditional),
                        getConditional( tokens.left),
                        getConditional( tokens.right)
                );
            }
            case IF -> throw new ErrorException(ErrorType.CONDITIONAL_UNEXPECTED_VALUE, "? (IF)");
            case ELSE -> throw new ErrorException(ErrorType.CONDITIONAL_UNEXPECTED_VALUE, "; (ELSE)");

            default -> throw new ErrorException(ErrorType.CONDITIONAL_UNEXPECTED_VALUE, token.type().toString());
        };
    }

    private static List<List<Token>> split(List<Token> tokens, TokenType type) {
        List<List<Token>> sections = new ArrayList<>();
        List<Token> current = new ArrayList<>();

        for (Token token : tokens) {
            if (token.type() == type) {
                sections.add(current);
                current = new ArrayList<>();
            }
            else
                current.add(token);
        }
        sections.add(current);
        return sections;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private static Pair<List<List<Token>>, List<MathOperator>> split(List<Token> tokens, List<MathOperator> ops) {
        List<List<Token>> sections = new ArrayList<>();
        List<MathOperator> operators = new ArrayList<>();
        List<Token> current = new ArrayList<>();

        for (Token token : tokens) {
            if ( token.type == TokenType.MATH && ops.contains(token.value())) {
                sections.add(current);
                operators.add((MathOperator) token.value());
                current = new ArrayList<>();
            }
            else
                current.add(token);
        }
        sections.add(current);
        return new Pair<>(sections, operators);
    }



}