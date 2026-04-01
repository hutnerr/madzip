import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * MadZip Utility class.
 * 
 * @author Hunter Baker
 */
public class MadZip {

  // Since this is a utility class, we don't need a constructor
  private MadZip() {}

  // ########################################################################
  // NOTE
  // ########################################################################

  // TEST FILES
  // The sizes here repsent the number of bits in the encoding generated using a
  // correctly constructed Huffman tree. They are not the actual file sizes that
  // will result from calling zip.
  // They do contain some overheadm therefore you should not be suprised if some
  // files actually end up bigger after compression.
  // mary.txt - Should be 89 bits
  // bytes.dat - Should be 337918 bits

  // ########################################################################
  // zip
  // ########################################################################

  /**
   * Method to compress and ZIP a file.
   * 
   * @param source The location of the file that should be compressed.
   * @param destination The location where the file will be saved.
   */
  public static void zip(File source, File destination) throws IOException {

    // It must perform the full Huffman coding process
    // 1. it must determine the frequencies of all bytes in the source file
    HashMap<Byte, Integer> freq = buildFrequencyTable(source);
    PriorityQueue<HuffTree> heap = buildHeap(freq);

    // 2. It must build a Huffman tree with the help of a Min-Heap
    HuffTree tree = HuffTree.buildTree(heap);

    // 3. It must use the Huffman tree to build a mapping from bytes to
    // byte-encodings
    HashMap<Byte, String> encodingTable = new HashMap<>();
    buildEncodingTable(tree.root(), "", encodingTable);

    BitSequence encodingSequence = new BitSequence();

    // 4. It must use that mapping to generate a compressed version of the original
    // file. This file will include both the frequency data and a sequence of bits
    // representing the encode version of the original file
    BufferedInputStream stream = new BufferedInputStream(new FileInputStream(source));
    // FileInputStream stream = new FileInputStream(source);
    int byteRead;
    while ((byteRead = stream.read()) != -1) {
      byte b = (byte) byteRead;
      encodingSequence.appendBits(encodingTable.get(b));
    }
    stream.close();

    // Save and write the sequence
    HuffmanSave hms = new HuffmanSave(encodingSequence, freq);
    write(destination, hms);

    // if source file cannot be read or the destination can't be written to throw an
    // IOException
    // It must overwrite the destination file if it already exists.
    // This method must not modify the source file
  }

  // ########################################################################
  // unzip
  // ########################################################################

  /**
   * Method to UNZIP a previously zipped file.
   * 
   * @param source The location of a previously zipped file
   * @param destination The location where the un-compressed version of the file should be saved.
   */
  public static void unzip(File source, File destination)
      throws IOException, ClassNotFoundException {

    // 1. It must reconstruct the Huffman tree from the frequencies stored in the
    // compressed version of the file
    HuffmanSave hms = open(source);
    HuffTree tree = HuffTree.buildTree(buildHeap(hms.getFrequencies()));

    // 2. It must use that Huffman tree to decode the encoded bit sequence in the
    // compressed file, saving all of the recovered bytes to the destination file.
    BitSequence encodedBits = hms.getEncoding();
    HuffBaseNode currentNode = tree.root();
    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(destination));

    for (int bit : encodedBits) {
      if (bit == 1) {
        currentNode = ((HuffInternalNode) currentNode).right();
      } else {
        currentNode = ((HuffInternalNode) currentNode).left();
      }

      if (currentNode.isLeaf()) {
        HuffLeafNode leafNode = (HuffLeafNode) currentNode;
        stream.write((char) leafNode.value());
        currentNode = tree.root();
      }
    }
    stream.close();

    // It must perform the full Huffman coding process

    // This method must throw an IOException if the source file cannot be read or
    // the destination file cannot be written to. It must throw a
    // ClassNotFoundException if that exception occurs during deserialzation

    // The method must overwrite the destination file if it already exists

    // This method must not modify the source file.;
  }

  // ########################################################################
  // helper methods
  // ########################################################################


  /**
   * Private helper method to build the frequency table used.
   * 
   * @param source The file
   * @return The table built
   * @throws IOException exception thrown
   */
  private static HashMap<Byte, Integer> buildFrequencyTable(File source) throws IOException {

    HashMap<Byte, Integer> freq = new HashMap<Byte, Integer>();

    BufferedInputStream stream = new BufferedInputStream(new FileInputStream(source));
    // FileInputStream stream = new FileInputStream(source);
    int byteRead;
    while ((byteRead = stream.read()) != -1) {
      freq.put((byte) byteRead, freq.getOrDefault((byte) byteRead, 0) + 1);
    }
    stream.close();

    return freq;
  }

  private static PriorityQueue<HuffTree> buildHeap(HashMap<Byte, Integer> freq) {

    PriorityQueue<HuffTree> heap = new PriorityQueue<HuffTree>();

    for (byte b : freq.keySet()) {
      heap.add(new HuffTree(b, freq.get(b)));
    }

    return heap;
  }

  /**
   * Recurisve helper to build the encoding table.
   * 
   * @param node The node we're at
   * @param code The Huffman Code
   * @param encoding The table we're building
   */
  private static void buildEncodingTable(HuffBaseNode node, String code,
      HashMap<Byte, String> encoding) {

    if (node == null) {
      return;
    }

    if (node.isLeaf()) {
      encoding.put(((HuffLeafNode) node).value(), code);
    } else {
      buildEncodingTable(((HuffInternalNode) node).left(), code + "0", encoding);
      buildEncodingTable(((HuffInternalNode) node).right(), code + "1", encoding);
    }
  }


  // ########################################################################
  // serialization
  // ########################################################################

  /**
   * Serialization method to open and read a file.
   * 
   * @param filename The filename we're writing
   * @return The file we opened
   * @throws IOException exception thrown
   */
  public static HuffmanSave open(File filename) throws IOException {
    ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
    HuffmanSave save;
    try {
      save = (HuffmanSave) in.readObject();
    } catch (ClassNotFoundException cnfe) {
      save = new HuffmanSave(null, null);
    }
    in.close();

    return save;
  }



  /**
   * Serialization method to write to a file.
   * 
   * @param filename The filename we're writing
   * @param hms The HuffmanSave object we're writing
   * @throws IOException exception thrown
   */
  public static void write(File filename, HuffmanSave hms) throws IOException {
    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));

    out.writeObject(hms);
    out.flush();
    out.close();
  }

}
