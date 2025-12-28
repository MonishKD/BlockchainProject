package src.functionalities;

import src.utils.StringUtil;

import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash; // holds digital signature
    public String previousHash; // holds previous block's hash and data
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //our data will be a simple message.
    private long timeStamp; // displayed in number of milliseconds
    private int nonce;

    // src.functionalities.Block Constructor.
    public Block(String previousHash ) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash(); // To be done after setting values
    }
    // Applying SHA 256 helper method to calculate the hash
    public String calculateHash() {
        String dataToHash = previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleRoot;
        return StringUtil.applySha256(dataToHash);
    }

    // Increases nonce value until hash target is reached.
    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDifficultyString(difficulty); // Create a string with difficulty * "0"
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("src.functionalities.Block Mined!!! : " + hash);
    }
    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) return false;
        if((previousHash != "0")) {
            if((!transaction.processTransaction())) {
                System.out.println("src.functionalities.Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("src.functionalities.Transaction Successfully added to src.functionalities.Block");
        return true;
    }
}
/* The mineBlock() method takes in an int called difficulty; this is the number of 0’s
they must solve for. Low difficulty like 1 or 2 can be solved nearly instantly on most computers. */
