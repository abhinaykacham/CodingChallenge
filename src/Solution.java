import java.io.*;
import java.util.*;

public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        String inputPath = "."+File.separator+"files"+File.separator+"passage.txt";
        String outputPath = "."+File.separator+"files"+File.separator+"output.txt";
        int k = 10;
        solution.compute(inputPath, outputPath, k, false);
    }

    /**
     * compute method performs
     *  Reading a file and do the following:
     *     Gives a total word count
     *     Identifies the top 10 words used and displays them in sorted order
     *     Finds and displays the last sentence on the file that contains the most used word
     * @param inputPath where given input file is located
     * @param outputPath where output is logged to
     * @param k represents number of top words to be determined
     * @param caseSensitive to consider case-sensitivity of a word
     */
    public void compute(String inputPath, String outputPath, int k,boolean caseSensitive) {
        //To keep track of total number of words in a file
        int wordCountInFile = 0;
        //Holds current line while reading the file
        String line;
        //To keep track of a word and the last line it occurred at
        Map<String, Integer> wordWithLastSentence = new HashMap<>();
        //To track a word and its frequency
        Map<String, Integer> wordFrequency = new HashMap<>();
        //Min-heap to track top k frequent words
        PriorityQueue<String> topKWords = new PriorityQueue<>(
                Comparator.comparingInt(wordFrequency::get)
        );
        String lastSentenceOfMostFreqWord = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))){
            int lineNumber = 0;
            //Reading the input file line by line
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("[ ,.:\"]+");
                for (String word : words) {
                    String validWord = caseSensitive?word:word.toLowerCase();
                    int freq = wordFrequency.getOrDefault(validWord, 0) + 1;
                    wordFrequency.put(validWord, freq);
                    wordWithLastSentence.put(validWord, lineNumber);
                    wordCountInFile++;
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        wordFrequency
                .forEach((key, value) -> {
                    topKWords.add(key);
                    if (topKWords.size() > k)
                        topKWords.poll();
                });
        //Writing output to the console and the log
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))){
            String task1 = "Total word count:" + wordCountInFile;
            writer.write(task1);
            writer.newLine();
            System.out.println(task1);

            String task2 = "Top "+k+" words used and display them in sorted order(ascending): ";
            writer.write(task2);
            System.out.print(task2);

            String word="";
            while (!topKWords.isEmpty()){
                word = topKWords.poll();
                writer.write(word);
                writer.write(topKWords.isEmpty()?".":", ");
                System.out.print(word);
                System.out.print(topKWords.isEmpty()?".":", ");
            }
            System.out.println();
            writer.newLine();
            try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
                for (int i = 0; i < wordWithLastSentence.get(word); i++) {
                    reader.readLine();
                }
                lastSentenceOfMostFreqWord = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String task3 = "The last sentence on the file that contains the most used word: " + lastSentenceOfMostFreqWord;
            writer.write(task3);
            System.out.println(task3);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
