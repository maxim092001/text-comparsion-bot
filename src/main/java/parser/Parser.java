package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Parser {
    private String text;
    private int pointer;

    public void setText(String text) {
        this.text = text;
    }

    private Set<Character> punctuation = Set.of(
            '.',
            ',',
            '!',
            '?',
            ';',
            ':',
            '-',
            ')',
            '('
    );


    public Parser(String text) {
        this.text = text;
    }

    private void skipAllWhiteSpace() {
        while (pointer < text.length() && Character.isWhitespace(text.charAt(pointer))) {
            pointer++;
        }
    }

    private void skipAllPunctuationSymbols() {
        while (pointer < text.length() && punctuation.contains(text.charAt(pointer))) {
            pointer++;
        }
    }

    public List<String> parse() {
        pointer = 0;

        List<String> res = new ArrayList<>();

        while (pointer < text.length()) {
            StringBuilder sb = new StringBuilder();
            skipAllWhiteSpace();
            skipAllPunctuationSymbols();
            while (pointer < text.length() &&
                    !Character.isWhitespace(text.charAt(pointer)) &&
                    !punctuation.contains(text.charAt(pointer))) {
                sb.append(text.charAt(pointer));
                pointer++;
            }

            String word = sb.toString();

            if (!word.isEmpty()) {
                word = Porter.stem(word);
                res.add(word);
            }
        }

        return res;
    }
}
