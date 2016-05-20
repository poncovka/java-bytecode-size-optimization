package jbyco.optimization.simplifications;

import jbyco.optimization.peephole.InsnUtils;
import jbyco.optimization.peephole.Pattern;
import jbyco.optimization.peephole.PeepholeAction;
import jbyco.optimization.peephole.Symbols;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * A library of patterns and actions to optimize jumps, labels and comparisons.
 */
public class JumpSimplifications {

    // -------------------------------------------------------------------------------------------- useless

    @Pattern({Symbols.GOTO   /*x*/, Symbols.LABEL /*x*/}) /* => LABEL x */
    public static class GotoJumpToNextInsnRemoval implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (((JumpInsnNode) matched[0]).label.equals(matched[1])) {
                list.remove(matched[0]);
                return true;
            }
            return false;
        }
    }

    @Pattern({Symbols.IF /*x*/, Symbols.LABEL /*x*/}) /* => POP; LABEL x */
    public static class IfJumpToNextInsnRemoval implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (((JumpInsnNode) matched[0]).label.equals(matched[1])) {
                list.set(matched[0], new InsnNode(Opcodes.POP));
                return true;
            }
            return false;
        }
    }

    @Pattern({Symbols.IF_CMP /*x*/, Symbols.LABEL /*x*/}) /* => POP2; LABEL x */
    public static class IfCmpJumpToNextInsnRemoval implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (((JumpInsnNode) matched[0]).label.equals(matched[1])) {
                list.set(matched[0], new InsnNode(Opcodes.POP2));
                return true;
            }
            return false;
        }
    }

    @Pattern({Symbols.LABEL, Symbols.FRAME, Symbols.LABEL}) /* => LABEL; LABEL; */
    public static class FrameRemoval implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.remove(matched[1]);
            return true;
        }
    }

    @Pattern({Symbols.GOTO, Symbols.NOTLABEL})      /* => GOTO */
    @Pattern({Symbols.RETURN, Symbols.NOTLABEL})    /* => RETURN */
    @Pattern({Symbols.ARETURN, Symbols.NOTLABEL})   /* => ARETURN */
    @Pattern({Symbols.IRETURN, Symbols.NOTLABEL})   /* => IRETURN */
    @Pattern({Symbols.LRETURN, Symbols.NOTLABEL})   /* => LRETURN */
    @Pattern({Symbols.FRETURN, Symbols.NOTLABEL})   /* => FRETURN */
    @Pattern({Symbols.DRETURN, Symbols.NOTLABEL})   /* => DRETURN */
    public static class DeadCodeAfterJumpRemoval implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.remove(matched[1]);
            return true;
        }
    }

    // -------------------------------------------------------------------------------------------- lookupswitch

    @Pattern({Symbols.LOOKUPSWITCH /* default */}) /* => POP; GOTO default */
    public static class DefaultLookupSwitchRemoval implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            LookupSwitchInsnNode i = ((LookupSwitchInsnNode) matched[0]);

            if (i.labels.isEmpty()) {
                list.insertBefore(matched[0], new InsnNode(Opcodes.POP));
                list.set(matched[0], new JumpInsnNode(Opcodes.GOTO, i.dflt));
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.LOOKUPSWITCH /* l l l l */}) /* => POP; */
    public static class LookupSwitchWithSameLabelsRemoval implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            LookupSwitchInsnNode i = ((LookupSwitchInsnNode) matched[0]);

            boolean sameLabels = true;
            for (LabelNode label : (List<LabelNode>)i.labels) {

                if (!label.equals(i.dflt)) {
                    sameLabels = false;
                    break;
                }
            }

            if (sameLabels) {
                list.set(matched[0], new InsnNode(Opcodes.POP));
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.LOOKUPSWITCH /* i i+1 i+2 */}) /* => TABLESWITCH */
    public static class LookupSwitchWithTableSwitchReplacement implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            LookupSwitchInsnNode lookup = ((LookupSwitchInsnNode) matched[0]);
            int size = lookup.labels.size();

            if (size >= 2) {

                // create dictionary of keys and labels
                TreeMap<Integer, LabelNode> dictionary = new TreeMap<>();

                for (int i = 0; i < size; i++) {
                    dictionary.put((int)lookup.keys.get(i), (LabelNode)lookup.labels.get(i));
                }

                int min = dictionary.firstKey();
                int max = dictionary.lastKey();

                // can be lookupswitch optimized?
                if (2 * (long)size > 2 + ((long)max - (long)min + 1)) {

                    LabelNode[] labels = new LabelNode[max - min + 1];

                    // set labels
                    for (Integer key : dictionary.keySet()) {
                        labels[key - min] = dictionary.get(key);
                    }

                    // set default labels
                    for (int i = 0; i < labels.length; i++) {
                        if (labels[i] == null) {
                            labels[i] = lookup.dflt;
                        }
                    }

                    // replace lookup with table
                    TableSwitchInsnNode table = new TableSwitchInsnNode(min, max, lookup.dflt, labels);
                    list.set(lookup, table);
                    return true;
                }
            }
            return false;
        }
    }

    // -------------------------------------------------------------------------------------------- tableswitch

    @Pattern({Symbols.TABLESWITCH /* i1 i2 l l l l l l */}) /* => POP; */
    public static class TableSwitchWithSameLabelsRemoval implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            TableSwitchInsnNode i = ((TableSwitchInsnNode) matched[0]);

            boolean sameLabels = true;
            for (LabelNode label : (List<LabelNode>)i.labels) {

                if (!label.equals(i.dflt)) {
                    sameLabels = false;
                    break;
                }
            }

            if (sameLabels) {
                list.set(matched[0], new InsnNode(Opcodes.POP));
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.TABLESWITCH /* i i l0 l */}) /* => LDC i; IF_ICMPEQ l; GOTO l0; */
    public static class TableSwitchWhereMinEqMaxReplacement implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            TableSwitchInsnNode i = ((TableSwitchInsnNode) matched[0]);

            if (i.min == i.max) {
                int value = i.min;
                LabelNode trueLabel = (LabelNode)i.labels.get(0);
                LabelNode falseLabel = i.dflt;

                list.insertBefore(matched[0], new LdcInsnNode(new Integer(value)));
                list.insertBefore(matched[0], new JumpInsnNode(Opcodes.IF_ICMPEQ, trueLabel));
                list.set(matched[0], new JumpInsnNode(Opcodes.GOTO, falseLabel));
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.TABLESWITCH /* i1 i2 l0 l l l l l */}) /* => rewritten with IF */
    public static class TableSwitchWithSameNonDefLabelsReplacement implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            TableSwitchInsnNode i = ((TableSwitchInsnNode) matched[0]);
            if (i.labels.size() >= 2) {

                LabelNode first = (LabelNode) i.labels.get(0);

                boolean sameLabels = true;
                for (LabelNode label : (List<LabelNode>) i.labels) {

                    if (!label.equals(first)) {
                        sameLabels = false;
                        break;
                    }
                }

                if (sameLabels) {
                    InsnList sublist = new InsnList();
                    sublist.add(new InsnNode(Opcodes.DUP));
                    sublist.add(new LdcInsnNode(new Integer(i.min)));
                    LabelNode dflt2 = new LabelNode();
                    sublist.add(new JumpInsnNode(Opcodes.IF_ICMPLT, dflt2));
                    sublist.add(new LdcInsnNode(new Integer(i.max)));
                    sublist.add(new JumpInsnNode(Opcodes.IF_ICMPGT, i.dflt));
                    sublist.add(new JumpInsnNode(Opcodes.GOTO, first));
                    sublist.add(dflt2);
                    sublist.add(new InsnNode(Opcodes.POP));
                    sublist.add(new JumpInsnNode(Opcodes.GOTO, i.dflt));

                    list.insert(i, sublist);
                    list.remove(i);
                    return true;
                }
            }
            return false;
        }
    }

    // -------------------------------------------------------------------------------------------- use smaller instructions

    @Pattern({Symbols.INT/*0*/, Symbols.IF_CMP/*l*/}) /* => IF l; */
    public static class IfZeroReplacement implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.getIntValue(matched[0]) == 0) {

                int opcode = Opcodes.IFEQ + (matched[1].getOpcode() - Opcodes.IF_ICMPEQ);
                LabelNode label = ((JumpInsnNode)matched[1]).label;

                list.remove(matched[0]);
                list.set(matched[1], new JumpInsnNode(opcode, label));
                return true;
            }

            return false;
        }
    }

    // -------------------------------------------------------------------------------------------- join same path

    @Pattern({Symbols.IF/*l*/, Symbols.GOTO/*l*/}) /* => POP; GOTO l; */
    public static class IfSamePathsUnion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.getLabelNode(matched[0]).equals(InsnUtils.getLabelNode(matched[1]))) {
                list.set(matched[0], new InsnNode(Opcodes.POP));
                return true;
            }

            return false;
        }
    }

    @Pattern({Symbols.IF_CMP/*l*/, Symbols.GOTO/*l*/}) /* => POP2; GOTO l; */
    public static class IfCmpSamePathsUnion implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            if (InsnUtils.getLabelNode(matched[0]).equals(InsnUtils.getLabelNode(matched[1]))) {
                list.set(matched[0], new InsnNode(Opcodes.POP2));
                return true;
            }

            return false;
        }
    }

    // -------------------------------------------------------------------------------------------- compare null

    @Pattern({Symbols.ACONST_NULL, Symbols.IFNULL/*l*/}) /* => GOTO l; */
    public static class NullCompareSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.remove(matched[0]);
            list.set(matched[1], new JumpInsnNode(Opcodes.GOTO, ((JumpInsnNode)matched[1]).label));
            return true;
        }
    }

    @Pattern({Symbols.ACONST_NULL, Symbols.IFNONNULL/*l*/}) /* => nothing */
    public static class NonNullCompareSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.remove(matched[0]);
            list.remove(matched[1]);
            return true;
        }
    }

    // -------------------------------------------------------------------------------------------- simplify comparison

    @Pattern({Symbols.INT, Symbols.IF/*l*/}) /* => GOTO l; or nothing */
    public static class IntZeroComparisonSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            // true
            if (InsnUtils.compareInt(matched[1], matched[0], InsnUtils.IZERO)) {
                list.remove(matched[0]);
                list.set(matched[1], new JumpInsnNode(Opcodes.GOTO, ((JumpInsnNode)matched[1]).label));
            }
            // false
            else {
                list.remove(matched[0]);
                list.remove(matched[1]);
            }

            return true;
        }
    }

    @Pattern({Symbols.INT, Symbols.INT, Symbols.IF_CMP/*l*/}) /* => GOTO l; or nothing */
    public static class IntIntComparisonSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            // true
            if (InsnUtils.compareInt(matched[2], matched[0], matched[1])) {
                list.remove(matched[0]);
                list.remove(matched[1]);
                list.set(matched[2], new JumpInsnNode(Opcodes.GOTO, ((JumpInsnNode)matched[2]).label));
            }
            // false
            else {
                list.remove(matched[0]);
                list.remove(matched[1]);
                list.remove(matched[2]);
            }

            return true;
        }
    }

    @Pattern({Symbols.LONG, Symbols.LONG, Symbols.LCMP /*l*/}) /* => result */
    public static class LongComparisonSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            int result = InsnUtils.compareLong(matched[2], matched[0], matched[1]);

            list.remove(matched[0]);
            list.remove(matched[1]);
            list.set(matched[2], new LdcInsnNode(new Integer(result)));
            return true;
        }
    }

    @Pattern({Symbols.FLOAT, Symbols.FLOAT, Symbols.FCMPG /*l*/}) /* => result */
    @Pattern({Symbols.FLOAT, Symbols.FLOAT, Symbols.FCMPL /*l*/}) /* => result */
    public static class FloatComparisonSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            int result = InsnUtils.compareFloat(matched[2], matched[0], matched[1]);

            list.remove(matched[0]);
            list.remove(matched[1]);
            list.set(matched[2], new LdcInsnNode(new Integer(result)));
            return true;
        }
    }

    @Pattern({Symbols.DOUBLE, Symbols.DOUBLE, Symbols.DCMPG /*l*/}) /* => result */
    @Pattern({Symbols.DOUBLE, Symbols.DOUBLE, Symbols.DCMPL /*l*/}) /* => result */
    public static class DoubleComparisonSimplification implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            int result = InsnUtils.compareDouble(matched[2], matched[0], matched[1]);

            list.remove(matched[0]);
            list.remove(matched[1]);
            list.set(matched[2], new LdcInsnNode(new Integer(result)));
            return true;
        }
    }

    // -------------------------------------------------------------------------------------------- compare same values

    @Pattern({Symbols.DUP, Symbols.IF_ICMPEQ/*l*/}) /* => POP; GOTO l */
    @Pattern({Symbols.DUP, Symbols.IF_ICMPLE/*l*/}) /* => POP; GOTO l */
    @Pattern({Symbols.DUP, Symbols.IF_ICMPGE/*l*/}) /* => POP; GOTO l */
    public static class IntEqualRemoval implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.set(matched[0], new InsnNode(Opcodes.POP));
            list.set(matched[1], new JumpInsnNode(Opcodes.GOTO, InsnUtils.getLabelNode(matched[1])));
            return true;
        }
    }

    @Pattern({Symbols.DUP, Symbols.IF_ICMPNE/*l*/}) /* => POP; */
    @Pattern({Symbols.DUP, Symbols.IF_ICMPLT/*l*/}) /* => POP; */
    @Pattern({Symbols.DUP, Symbols.IF_ICMPGT/*l*/}) /* => POP; */
    public static class IntNotEqualRemoval implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.set(matched[0], new InsnNode(Opcodes.POP));
            list.remove(matched[1]);
            return true;
        }
    }

    @Pattern({Symbols.DUP2, Symbols.LCMP}) /* => 0 */
    // DCMP a FCMP cannot be optimized, because NaN != NaN
    public static class LongEqualRemoval implements PeepholeAction {

        @Override
        public boolean replace(InsnList list, AbstractInsnNode[] matched) {
            list.remove(matched[0]);
            list.set(matched[1], new InsnNode(Opcodes.ICONST_0));
            return true;
        }
    }
}
