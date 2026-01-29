package com.adiwave.books.exercise;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Just some Java 9 code functional programming using .stream()
// Stream API + Lambda expressions
public class FunctionalOperations {

    private List<String> booksTitles = List.of(
            "The Great Gatsby",
            "To Kill a Mockingbird",
            "1984",
            "The Adventures of Huckleberry Finn",
            "The little mermaid",
            "Pride and Prejudice",
            "The Catcher in the Rye"
    );

    private List<String> friends = List.of("Brian", "Natalie", "Neal", "Raju", "Sara", "John", "Jane", "Alice");

    private List<Transaction> transactions = List.of(
            new Transaction("USD", 100.0, "user1"),
            new Transaction("EUR", 200.0, "user2"),
            new Transaction("USD", 150.0, "user1"),
            new Transaction("GBP", 300.0, "user3"),
            new Transaction("EUR", 250.0, "user2"),
            new Transaction("USD", 120.0, "user1")
    );

    public List<String> getBooksStartWithChar(String beginTitle) {
        return booksTitles.stream()
                .filter(book -> book.startsWith(beginTitle))
                .toList();
    }

    public void printAllCapitalBooks() {
        booksTitles.stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    public void printAllCapitalFriends(int skip) {
        friends.stream()
                .skip(skip)
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    // Given a Stream<Transaction>, compute:
    //   - total amount per currency
    //   - average amount per user
    //   - top 3 users by total amount
    public void exerciseOne() {
        Map<String, Double> mapTotalCurrencyTransaction = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::currency,
                        Collectors.summingDouble(Transaction::amount)
                ));
        mapTotalCurrencyTransaction.forEach((key, value) -> System.out.println(key + " " + value));

        Map<String, Double> averagePerUser = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::userId,
                        Collectors.averagingDouble(Transaction::amount)
                ));
        averagePerUser.forEach((key, value) -> System.out.println(key + " " + value));

        Map<String,Double> topUsersByTotalAmount = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::userId,
                        Collectors.summingDouble(Transaction::amount)
                ));
        topUsersByTotalAmount.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> System.out.println(entry.getKey() + " " + entry.getValue()));

    }
    // Short-Circuiting Collector
    // Implement a collector that:
    // stops collecting once sum > threshold
    // returns the partial result, no exception to exit
    // Given a stream of numbers, collect values until their sum exceeds a threshold, then ignore the rest.
    // Core Idea
    // We use a mutable accumulation container with:
    // the current sum
    // collected values
    // a done flag to ignore further elements
    public void exerciseTwo() {
        List<Double> input = List.of(10.0, 20.0, 30.0, 40.0, 50.0);
        List<Double> result =
                input.stream()
                        .collect(new ThresholdCollector(55.0));
        System.out.println(result);
    }

    // Join two streams:
    // a) Stream<Event>
    // b) Stream<Rule>
    // Match when rule.activeAt(event.time)
    public void exerciseThree() {

    }

    public static void main(String[] args) {
        FunctionalOperations fop = new FunctionalOperations();
        System.out.println(fop.getBooksStartWithChar("The"));
        fop.printAllCapitalBooks();
        fop.printAllCapitalFriends(2);
        fop.exerciseOne();
        fop.exerciseTwo();
    }
}
