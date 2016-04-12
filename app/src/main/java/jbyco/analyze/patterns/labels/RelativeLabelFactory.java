package jbyco.analyze.patterns.labels;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Label;

public class RelativeLabelFactory implements AbstractLabelFactory {
	
	Label begin;
	Label end;
	
	int maxNumber;
	Map<Label, NumberedLabel> map;
	
	public RelativeLabelFactory() {
		init(null, null);
	}
	
	public void init(Label begin, Label end) {
		this.begin = begin;
		this.end = end;
		this.maxNumber = 0;
		this.map = new HashMap<>();	
	}
	
	@Override
	public AbstractLabel getLabel(Label label) {
		
		if (label.equals(begin)) {
			return getNamedLabel(label, "BEGIN");
		}
		else if (label.equals(end)) {
			return getNamedLabel(label, "END");
		}
		else {
			return getNumberedLabel(label);
		}
	}
	
	public AbstractLabel getNamedLabel(Label label, String name) {
		return new NamedLabel(name);
	}
	
	public AbstractLabel getNumberedLabel(Label label) {
		
		NumberedLabel label2 = map.get(label);
		
		if (label2 == null) {
			label2 = new NumberedLabel(maxNumber++);
			map.put(label, label2);
		}
		
		return label2;
	}

}
