package graph;

import utils.*;

public class AccountGraph {

    private final MyMap<String, MyList<String>> adjacencyList;

    public AccountGraph() {
        adjacencyList = new MyHashMap<>();
    }

    public void addAccount(String accountId) {
        adjacencyList.putIfAbsent(accountId, new MyArrayList<>());
    }

    public void addTransfer(String fromAccount, String toAccount) {
        addAccount(fromAccount);
        addAccount(toAccount);
        adjacencyList.get(fromAccount).add(toAccount);
    }

    public MyList<String> getConnections(String accountId) {
        return adjacencyList.getOrDefault(accountId, new MyArrayList<>());
    }

    public MySet<String> getReachableAccounts(String startAccount) {
        MySet<String> visited = new MyHashSet<>();
        MyQueue<String> queue = new MyLinkedQueue<>();
        queue.enqueue(startAccount);

        while (!queue.isEmpty()) {
            String current = queue.dequeue();
            if (!visited.contains(current)) {
                visited.add(current);
                for (String neighbor : getConnections(current)) {
                    queue.enqueue(neighbor);
                }
            }
        }

        visited.remove(startAccount); // Remove self from results
        return visited;
    }

    public void displayGraph() {
        if (adjacencyList.isEmpty()) {
            System.out.println("❌ No account transfers recorded.");
            return;
        }

        System.out.println("🌐 Internal Account Transfers:");
        for (String acc : adjacencyList.keySet()) {
            MyList<String> neighbors = adjacencyList.get(acc);
            for (String neighbor : neighbors) {
                System.out.println("- " + acc + " ➝ " + neighbor);
            }
        }
    }
}
