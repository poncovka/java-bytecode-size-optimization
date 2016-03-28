package jbyco.analyze.patterns.labels;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Label;

public class NumberedLabelFactory implements AbstractLabelFactory {
	
	int maxNumber;
	Map<Label, NumberedLabel> map;
	
	public NumberedLabelFactory() {
		init();
	}
	
	public void init() {
		maxNumber = 0;
		map = new HashMap<>();
	}
	
	@Override
	public AbstractLabel getLabel(Label label) {
		
		NumberedLabel label2 = map.get(label);
		
		if (label2 == null) {
			label2 = new NumberedLabel(maxNumber++);
			map.put(label, label2);
		}
		
		return label2;
	}

}
