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
        String nextReceiptPath = receiptQueue.peek();
        if (nextReceiptPath == null) {
            return "No receipts in queue.";
        }

        MyList<String> lines = FileStorage.readLines(nextReceiptPath);
        if (lines.size() < 2) {
            return "Invalid receipt format.";
        }

        // Get the expenditure line (should be the second line)
        String expenditureLine = lines.get(1).trim();

        // Parse the expenditure details
        if (expenditureLine.startsWith("Expenditure [") && expenditureLine.endsWith("]")) {
            // Remove "Expenditure [" from start and "]" from end
            String content = expenditureLine.substring(13, expenditureLine.length() - 1);

            // Split by ", " to get individual fields
            String[] fields = content.split(", ");

            StringBuilder result = new StringBuilder();
            result.append("Receipt Details:\n");

            for (String field : fields) {
                result.append(field).append("\n");
            }

            return result.toString().trim(); // Remove the last newline
        }

        return "Could not parse receipt details.";
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

    public void saveReceipt(String filepath, String receiptDetails) {
        MyList<String> lines = new MyArrayList<>();
        filepath = "receipts/" + filepath; // Ensure the path is in the receipts directory

        // Create receipts directory if it doesn't exist
        java.io.File receiptDir = new java.io.File("receipts");
        if (!receiptDir.exists()) {
            receiptDir.mkdirs();
        }

        // Add the receipt details first
        lines.add("Receipt Details:");
        lines.add(receiptDetails);
        lines.add(""); // Empty line for separation

        FileStorage.writeLines(filepath, lines);
        uploadReceipt(filepath);
    }
}