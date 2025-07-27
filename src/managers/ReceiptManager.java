package managers;

import utils.FileStorage;
import utils.MyLinkedQueue;
import utils.MyQueue;
import utils.MyList;
import utils.MyArrayList;

/**
 * Simulates a receipt review queue (FIFO).
 * Accountants validate receipts in order of submission.
 */
public class ReceiptManager {

    private MyQueue<String> receiptQueue;

    public ReceiptManager() {
        receiptQueue = new MyLinkedQueue<>();
    }

    /**
     * Upload (enqueue) a receipt file path for review.
     */
    public void uploadReceipt(String receiptPath) {
        receiptQueue.enqueue(receiptPath);
    }

    /**
     * Review (dequeue) the next receipt in queue.
     */
    public String reviewReceipt() {
        return receiptQueue.dequeue(); // removes the first receipt, returns null if empty
    }

    /**
     * Peek at the next receipt without removing it.
     */
    public String peekNextReceipt() {
        return receiptQueue.peek();
    }

    /**
     * Get the number of pending receipts.
     */
    public int getQueueSize() {
        return receiptQueue.size();
    }

    /**
     * Check if queue is empty.
     */
    public boolean isEmpty() {
        return receiptQueue.isEmpty();
    }

    // ✅ Save to receipts.txt
    public void saveToFile(String filepath) {
        MyList<String> lines = new MyArrayList<>();
        for (String path : receiptQueue) {
            lines.add(path);
        }
        FileStorage.writeLines(filepath, lines);
    }

    // ✅ Load from receipts.txt
    public void loadFromFile(String filepath) {
        for (String line : FileStorage.readLines(filepath)) {
            if (!line.isBlank()) {
            	receiptQueue.enqueue(line.trim());
            }
        }
    }
}
