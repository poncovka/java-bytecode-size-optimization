package jbyco.analyze.patterns.instr.label;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Label;

public class NumberedLabelFactory implements AbstractLabelFactory {
	
	int maxNumber = 0;
	Map<Label, NumberedLabel> map = new HashMap<>();
	
	@Override
	public AbstractLabel getLabel(Label label) {
		
		NumberedLabel label2 = map.get(label);
		
		if (label2 == null) {
			label2 = new NumberedLabel(maxNumber++);
			map.put(label, label2);
		}
		
		return label2;
	}
	
	public void restart() {
		maxNumber = 0;
		map = new HashMap<>();
	}


}
