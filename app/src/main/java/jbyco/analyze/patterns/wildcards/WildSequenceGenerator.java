package jbyco.analyze.patterns.wildcards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

/**
 * A class that generates sequences of instructions with wild cards.
 * Wild cards are represented by null and they represent any instruction, 
 * that is not a label or a jump.
 */
public class WildSequenceGenerator implements Iterator<Collection<AbstractInsnNode>> {

	/** The number of wild cards in one sequence. */
	int wildcards;
	
	/** The iterator over all allowed indexes of wild cards in a sequence. */
	CombinationIterator iterator;
	
	/** The sequence of instructions. */
	ArrayList<AbstractInsnNode> list;
	
	/** The forbidden indexes. */
	ArrayList<Integer> forbidden;
	
	/** The wild card. */
	AbstractInsnNode wildcard;
	
	/**
	 * Instantiates a new wild sequence generator.
	 *
	 * @param list the list of instructions
	 * @param wildcard the wild card
	 * @param wildcards the number of wild cards
	 */
	public WildSequenceGenerator(Collection<AbstractInsnNode> list, AbstractInsnNode wildcard, int wildcards) {
		
		this.wildcard = wildcard;
		this.wildcards = wildcards;
		this.list = new ArrayList<>(list);
		this.forbidden = getForbiddenIndexes(this.list);
		this.iterator = getIterator(this.list, this.forbidden);
	}
	
	/**
	 * Checks if it is a forbidden instruction.
	 *
	 * @param node the node with instruction
	 * @return true, if it is a forbidden instruction
	 */
	boolean isForbiddenNode(AbstractInsnNode node) {
		return (node instanceof LabelNode 
				|| node instanceof JumpInsnNode 
				|| node instanceof LookupSwitchInsnNode 
				|| node instanceof TableSwitchInsnNode);
	}
	
	/**
	 * Gets the forbidden indexes.
	 *
	 * @param list the list of instruction
	 * @return the forbidden indexes
	 */
	public ArrayList<Integer> getForbiddenIndexes(ArrayList<AbstractInsnNode> list) {
		
		ArrayList<Integer> indexes = new ArrayList<>();
		
		for (int i = 0; i < list.size(); i++) {
			
			// add indexes of the first, last and forbidden nodes
			if (i == 0 || i + 1 == list.size() || isForbiddenNode(list.get(i))) {
				indexes.add(i);
			}
		}
		
		return indexes;
	}
	
	/**
	 * Gets the iterator over combinations.
	 *
	 * @param list the list of instructions
	 * @param forbidden the forbidden indexes
	 * @return the iterator over combinations
	 */
	public CombinationIterator getIterator(ArrayList<AbstractInsnNode> list, ArrayList<Integer> forbidden) {
		
		int n = wildcards;
		int min = 0;
		int max = this.list.size() - this.forbidden.size() - 1;
		
		return new CombinationIterator(n, min, max);
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Collection<AbstractInsnNode> next() {
		
		// get next combination of indexes
		int[] combination = iterator.next();
		
		// return list of values and wild cards
		return applyCombination(combination);
	}
	
	/**
	 * Apply combination.
	 *
	 * @param combination the combination
	 * @return the list of instructions
	 */
	public Collection<AbstractInsnNode> applyCombination(int[] combination) {
		
		// get size of the array
		int length = list.size();
		
		// create a list of nodes
		ArrayList<AbstractInsnNode> result = new ArrayList<>();
		
		int offset = 0;
		int ci = 0;
		int fi = 0;
		
		for (int i = 0; i < list.size(); i++) {
						
			// copy node at forbidden indexes
			if (fi < forbidden.size() && i == forbidden.get(fi)) {
				result.add(list.get(i));
				fi++;
				offset++;
			}
			
			// create wild card
			else if (ci < combination.length && i == combination[ci] + offset) {
				result.add(wildcard);
				ci++;
			}
			
			// copy node
			else {
				result.add(list.get(i));
			}
		}
				
		return result;
	}
	
}
