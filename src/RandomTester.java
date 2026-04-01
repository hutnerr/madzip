import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class RandomTester {

	public static void main(String[] args) {

		HashMap<Byte, Integer> freq = new HashMap<Byte, Integer>();

		// READ THE FILE AND CREATE A FREQUENCY TABLE
		try {
			FileInputStream fis = new FileInputStream("mary.txt");
			int byteRead;
			while ((byteRead = fis.read()) != -1) {
				freq.put((byte) byteRead, freq.getOrDefault((byte) byteRead, 0) + 1);
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// CREATE A MIN HEAP OF HUFFMAN TREES
		PriorityQueue<HuffTree> pq = new PriorityQueue<HuffTree>();

		// ADD THE LEAVES TO THE PRIORITY QUEUE
		for (byte b : freq.keySet()) {
			pq.add(new HuffTree(b, freq.get(b)));
		}

		// while (!pq.isEmpty()) {
		// 	HuffTree t = pq.poll(); // Retrieve and remove the head of the queue (minimum element)
		// 	System.out.println(t.root().weight());
		// }

		HuffTree tree = HuffTree.buildTree(pq);

		HuffBaseNode root = tree.root();
		HashMap<Byte, String> encoding = new HashMap<Byte, String>();

		traverseTree(root, "", encoding);

		for (Byte b : encoding.keySet()) {
			System.out.println("Key: " + b.byteValue() + ", Value: " + encoding.get(b));
		}

		BitSequence encodingSequence = new BitSequence();


		// ENCODE THE FILE USING THE ENCODING MAP
		try {
			FileInputStream fis = new FileInputStream("mary.txt");
			int byteRead;
			while ((byteRead = fis.read()) != -1) {
				byte b = (byte) byteRead;
				System.out.println((char) b);
				System.out.println(encoding.get(b));
				String code = encoding.get(b);
				encodingSequence.appendBits(code);
			}
			try {
				FileOutputStream fos = new FileOutputStream("output.txt");
				fos.write(encodingSequence.toString().getBytes());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		HuffmanSave hms = new HuffmanSave(encodingSequence, freq);
		
		try {
			write("test", hms);
		}
		catch (Exception e ) {
			// do nothing
		}

		


	}

	public static void write(String filename, HuffmanSave hms) throws IOException
	{
	  ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename + ".mz"));
	  
	  out.writeObject(hms);
	  out.flush();
	  out.close();
	}

	private static void traverseTree(HuffBaseNode node, String code, HashMap<Byte, String> encoding) {
		if (node.isLeaf()) {
			// Process leaf node
			encoding.put(((HuffLeafNode) node).value(), code);
		} else {
			// Process internal node
			traverseTree(((HuffInternalNode) node).left(), code + "0", encoding);
			traverseTree(((HuffInternalNode) node).right(), code + "1", encoding);
		}
	}
		// 4. It must use that mapping to generate a compressed version of the original
		// file. This file will include both the frequency data and a sequence of bits
		// representing the encode version of the original file

		// this has a long numBits to story the number of bits
		// then it has byte[] bytes to store the bits

		// it has multiple append methods
		// appendBit(int bit) - appends a bit to the end of the sequence. 0 is 0, 1 is everything else
		// appendBits(BitSequence bits) - appends the bits from the given BitSequence to the end of this sequence
		// appendBits(String bits) - appends the bits from the given string to the end of this sequence

		// BitSequence encoding = new BitSequence();






		// HuffmanSave hms = new HuffmanSave(encoding, freq);


	
}
