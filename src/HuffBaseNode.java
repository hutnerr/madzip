/**
 * Base node for a Huffman Tree.
 * 
 * @author OpenDSA Textbook & Hunter Baker
 */
public abstract class HuffBaseNode {

  private int weight;

  /**
   * Constructor.
   * 
   * @param weight The node's weight
   */
  public HuffBaseNode(int weight) {
    this.weight = weight;
  }

  /**
   * Getter for the weight.
   * 
   * @return the weight
   */
  public int weight() {
    return weight;
  }

  /**
   * Determines if the node is a leaf node or not.
   * 
   * @return True if leaf, false otherwise
   */
  public abstract boolean isLeaf();
}
