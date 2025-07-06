package managers;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Simulates a receipt review queue (FIFO).
 * Accountants validate receipts in order of submission.
 */
public class ReceiptManager {

    private Queue<String> receiptQueue;

    public ReceiptManager() {
        receiptQueue = new LinkedList<>();
    }

    /**
     * Upload (enqueue) a receipt file path for review.
     */
    public void uploadReceipt(String receiptPath) {
        receiptQueue.offer(receiptPath);
    }

    /**
     * Review (dequeue) the next receipt in queue.
     */
    public String reviewNextReceipt() {
        return receiptQueue.poll(); // returns null if empty
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
}
