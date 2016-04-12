package jbyco.analyze.patterns.wildcards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

public class WildSequenceGenerator implements Iterator<Collection<AbstractInsnNode>> {

	// number of wild cards in a list
	int wildcards;
	
	// iterator over all allowed indexes of wild cards in a list
	CombinationIterator iterator;
	
	// list of values
	ArrayList<AbstractInsnNode> list;
	
	// list of forbidden indexes
	ArrayList<Integer> forbidden;
	
	// wild card
	AbstractInsnNode wildcard;
	
	public WildSequenceGenerator(Collection<AbstractInsnNode> list, AbstractInsnNode wildcard, int wildcards) {
		
		this.wildcard = wildcard;
		this.wildcards = wildcards;
		this.list = new ArrayList<>(list);
		this.forbidden = getForbiddenIndexes(this.list);
		this.iterator = getIterator(this.list, this.forbidden);
	}
	
	boolean isForbiddenNode(AbstractInsnNode node) {
		return (node instanceof LabelNode 
				|| node instanceof JumpInsnNode 
				|| node instanceof LookupSwitchInsnNode 
				|| node instanceof TableSwitchInsnNode);
	}
	
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
	
	public CombinationIterator getIterator(ArrayList<AbstractInsnNode> list, ArrayList<Integer> forbidden) {
		
		int n = wildcards;
		int min = 0;
		int max = this.list.size() - this.forbidden.size() - 1;
		
		return new CombinationIterator(n, min, max);
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Collection<AbstractInsnNode> next() {
		
		// get next combination of indexes
		int[] combination = iterator.next();
		
		// return list of values and wild cards
		return applyCombination(combination);
	}
	
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
