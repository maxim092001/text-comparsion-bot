package cosinedist;

import parser.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CosineDistance {

    public static double CosineSimilarityScore(String firstText, String secondText) {

        Parser parser = new Parser(firstText);

        List<String> firstTextWords = parser.parse();
        parser.setText(secondText);
        List<String> secondTextWords = parser.parse();

        List<String> distinctWords = new ArrayList<>();
        Map<String, values> dictionary = new HashMap<>();

        for (String word : firstTextWords) {
            if (dictionary.containsKey(word)) {
                values val = dictionary.get(word);
                val.setFirstCounter(val.getSecondCounter() + 1);
                dictionary.put(word, val);
            } else {
                dictionary.put(word, new values(1, 0));
                distinctWords.add(word);
            }
        }

        for (String word : secondTextWords) {
            if (dictionary.containsKey(word)) {
                values val = dictionary.get(word);
                val.setSecondCounter(val.getSecondCounter() + 1);
                dictionary.put(word, val);
            } else {
                dictionary.put(word, new values(0, 1));
                distinctWords.add(word);
            }
        }

        double bothVectors = 0.0000000;
        double firstVector = 0.0000000;
        double secondVector = 0.0000000;

        for (String distinctWord : distinctWords) {
            values tempValue = dictionary.get(distinctWord);

            double firstFreq = tempValue.getFirstCounter();
            double secondFreq = tempValue.getSecondCounter();

            bothVectors = bothVectors + (firstFreq * secondFreq);

            firstVector = firstVector + firstFreq * firstFreq;
            secondVector = secondVector + secondFreq * secondFreq;
        }


        return ((bothVectors) / (Math.sqrt(firstVector) * Math.sqrt(secondVector)));
    }

    public static class values {
        private int firstCounter;
        private int secondCounter;

        values(int firstCounter, int secondCounter) {
            this.firstCounter = firstCounter;
            this.secondCounter = secondCounter;
        }

        public int getFirstCounter() {
            return firstCounter;
        }

        public void setFirstCounter(int firstCounter) {
            this.firstCounter = firstCounter;
        }

        public int getSecondCounter() {
            return secondCounter;
        }

        public void setSecondCounter(int secondCounter) {
            this.secondCounter = secondCounter;
        }
    }
}
