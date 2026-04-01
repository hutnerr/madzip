/**
 * Huffman Coding Tree leaf node.
 * 
 * @author OpenDSA Textbook & Hunter Baker
 */
public class HuffLeafNode extends HuffBaseNode {

  private byte element;

  /**
   * Constructor.
   * 
   * @param element The element of the node
   * @param weight The weight of the element
   */
  public HuffLeafNode(byte element, int weight) {
    super(weight);
    this.element = element;
  }

  /**
   * Getter for the node's value.
   * 
   * @return the value
   */
  public byte value() {
    return element;
  }

  @Override
  public boolean isLeaf() {
    return true;
  }

}
