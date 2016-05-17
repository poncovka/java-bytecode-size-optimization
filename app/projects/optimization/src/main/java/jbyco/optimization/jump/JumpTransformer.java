package jbyco.optimization.jump;

import jbyco.optimization.Statistics;
import jbyco.optimization.reductions.JumpReductions;
import jbyco.optimization.transformation.MethodTransformer;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Map;

/**
 * Created by vendy on 17.5.16.
 */
public class JumpTransformer extends MethodTransformer {

    JumpCollector collector = new JumpCollector();
    Statistics stats;

    LabelAction[] labelActions;
    FrameAction[] frameActions;

    public void setStatistics(Statistics stats) {
        this.stats = stats;
    }

    public void loadLabelActions(LabelAction ...actions) {
        this.labelActions = actions;
    }

    public void loadFrameActions(FrameAction ...actions) {
        this.frameActions = actions;
    }

    @Override
    public void transform(MethodNode mn) {

        InsnList list = mn.instructions;

        loadLabelActions(
                JumpReductions::joinLabels,
                JumpReductions::removeDoubleJumps,
                //JumpReductions::removeUselessLabel,
                JumpReductions::replaceGotoWithReturn
        );

        loadFrameActions(
                JumpReductions::removeUselessFrame
        );

        // every optimization can compromise the validity of info
        //replaceLookupswitch(list, frame, labels);
        //replaceTableswitch(list, frame, labels);

        boolean change = true;
        while(change) {

            // no change
            change = false;

            // collect data
            collector.collect(mn);

            // transform labels
            if (transformLabels(list, labelActions)) {
                change = true;
                continue;
            }
            else if (transformFrames(list, frameActions)) {
                change = true;
                continue;
            }
        }

        super.transform(mn);
    }

    boolean transformLabels(InsnList list, LabelAction[] actions) {

        boolean change = false;
        for (LabelNodeInfo info : collector.labels.values()) {

            for (LabelAction action : actions) {

                // do action
                change = action.replace(list, info.label, info);

                // update statistics
                if (change && stats != null) {
                    stats.addPepphole(action.toString());
                }

                // stop
                if (change) {
                    break;
                }
            }

            // stop
            if (change) {
                break;
            }
        }

        return change;
    }

    boolean transformFrames(InsnList list, FrameAction[] actions) {

        boolean change = false;

        Map<FrameNode, FrameNodeInfo> frames = collector.frames;
        Map<LabelNode, LabelNodeInfo> labels = collector.labels;

        for (FrameNodeInfo info : frames.values()) {

            for (FrameAction action : actions) {
                if (action.replace(list, info.frame, info, labels)) {

                    // update statistics
                    if (change && stats != null) {
                        stats.addPepphole(action.toString());
                    }

                    // stop
                    if (change) {
                        break;
                    }
                }
            }

            // stop
            if (change) {
                break;
            }
        }

        return change;
    }

}
