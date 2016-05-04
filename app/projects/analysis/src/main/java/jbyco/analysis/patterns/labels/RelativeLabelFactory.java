package jbyco.analysis.patterns.labels;

import org.objectweb.asm.Label;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory for creating NamedLabel and NumberedLabel objects.
 * <p>
 * Names and numbers of the labels are relative to the method or
 * to the current sequence of instructions. It means that the
 * label with number 0 is the first label in a given sequence of
 * instructions and the label with the name begin marks the begin of
 * the method.
 */
public class RelativeLabelFactory implements AbstractLabelFactory {

    /**
     * The begin of the current method.
     */
    Label begin;

    /**
     * The end of the current method.
     */
    Label end;

    /**
     * The max number of numbered labels.
     */
    int maxNumber;

    /**
     * The map of labels mapped to numbered labels.
     */
    Map<Label, NumberedLabel> map;

    /**
     * Instantiates a new label factory.
     */
    public RelativeLabelFactory() {
        init(null, null);
    }

    /* (non-Javadoc)
     * @see jbyco.analysis.patterns.labels.AbstractLabelFactory#init(org.objectweb.asm.Label, org.objectweb.asm.Label)
     */
    public void init(Label begin, Label end) {
        this.begin = begin;
        this.end = end;
        this.maxNumber = 0;
        this.map = new HashMap<>();
    }

    /* (non-Javadoc)
     * @see jbyco.analysis.patterns.labels.AbstractLabelFactory#getLabel(org.objectweb.asm.Label)
     */
    @Override
    public AbstractLabel getLabel(Label label) {

        if (label.equals(begin)) {
            return getNamedLabel(label, "BEGIN");
        } else if (label.equals(end)) {
            return getNamedLabel(label, "END");
        } else {
            return getNumberedLabel(label);
        }
    }

    /**
     * Gets the named label.
     *
     * @param label the label
     * @param name  the name
     * @return the named label
     */
    public AbstractLabel getNamedLabel(Label label, String name) {
        return new NamedLabel(name);
    }

    /**
     * Gets the numbered label.
     *
     * @param label the label
     * @return the numbered label
     */
    public AbstractLabel getNumberedLabel(Label label) {

        NumberedLabel label2 = map.get(label);

        if (label2 == null) {
            label2 = new NumberedLabel(maxNumber++);
            map.put(label, label2);
        }

        return label2;
    }

}
