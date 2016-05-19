package jbyco.optimization;

import jbyco.optimization.expansion.DuplicationExpansion;
import jbyco.optimization.jump.*;
import jbyco.optimization.reductions.*;
import jbyco.optimization.peephole.*;
import jbyco.optimization.simplifications.*;
import jbyco.optimization.common.ClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.IOException;

/**
 * Created by vendy on 3.5.16.
 */
public class Optimizer {

    Statistics stats;
    PeepholeRunner expander;
    PeepholeRunner patternSimplifier;
    ClassTransformer jumpSimplifier;
    PeepholeRunner reducer;

    public Optimizer(Statistics stats) {

        this.stats = stats;
        initPhase1();
        initPhase2();
        initPhase3();
    }

    public void initPhase1() {
        expander = new PeepholeRunner(stats);
        expander.loadActions(
                DuplicationExpansion.class
        );
    }

    public void initPhase2() {

        // from specific to general
        // duplication simplifications are last
        patternSimplifier = new PeepholeRunner(stats);
        patternSimplifier.loadActions(
                ObjectSimplifications.class,
                StringAppendSimplifications.class,
                ConversionsSimplifications.class,
                AlgebraicSimplifications.class,
                JumpSimplifications.class,
                StackSimplifications.class
        );

        // jump transformations
        JumpCollector collector = new JumpCollector();
        LabelTransformer lt = new LabelTransformer(collector, stats);
        FrameTransformer ft = new FrameTransformer(lt, collector, stats);
        LookupSwitchTransformer lst = new LookupSwitchTransformer(ft, collector, stats);
        TableSwitchTransformer tst = new TableSwitchTransformer(lst, collector, stats);

        lt.loadActions(
                //JumpReductions.class
                new JumpReductions.LabelsUnion(),
                new JumpReductions.UselessLabelRemoval(),
                new JumpReductions.JumpWithReturnReplacement(),
                new JumpReductions.DoubleJumpRemoval()
        );

        ft.loadActions(
                JumpReductions.class
        );

        lst.loadActions(
                JumpReductions.class
        );

        tst.loadActions(
                JumpReductions.class
        );

        jumpSimplifier = new JumpTransformer(lst, collector, stats);
    }

    public void initPhase3() {
        reducer = new PeepholeRunner(stats);
        reducer.loadActions(
                DuplicationReduction.class
        );

    }

    public byte[] optimizeClassFile(byte[] input) throws IOException {

        // statistics
        if (stats != null) {
            stats.addSizeBefore(input.length);
        }

        // create class frame
        ClassNode result1 = phase1(input);

        // actions
        ClassNode result2 = phase2(result1);

        // create byte array
        byte[] result3 = phase3(result2);

        // checkInput the output
        check(result3);

        // statistics
        if (stats != null) {
            stats.addSizeAfter(result3.length);
        }

        return result3;
    }

    public ClassNode phase1(byte[] input) {

        // create the chain of visitors
        ClassNode node = new ClassNode(Opcodes.ASM5);
        ClassVisitor cv = node;

        // remove attributes
        cv = new InfoAttributesRemoval(cv, stats);

        // process input
        ClassReader reader = new ClassReader(input);
        reader.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.EXPAND_FRAMES);

        // apply expansions
        expander.findAndReplace(node);

        // return the frame
        return node;
    }

    public ClassNode phase2(ClassNode input) {

        // apply actions optimizations
        patternSimplifier.findAndReplace(input);

        // apply jump optimizations
        jumpSimplifier.transform(input);

        // return the frame
        return input;
    }

    public byte[] phase3(ClassNode input) {

        // reduce the code
        reducer.findAndReplace(input);

        // create the chain of visitors
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = writer;
        cv = new ClassVisitor(Opcodes.ASM5, cv) {

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                mv = new ConstantNumbersSubstitution(mv, stats);
                mv = new LocalVariablesRealocation(access, desc, mv, stats);
                return mv;
            }

        };

        // process input
        input.accept(cv);

        // return the byte array
        return writer.toByteArray();
    }

    public void check(byte[] input) {
        // prepare visitors
        ClassWriter writer = new ClassWriter(0);
        ClassVisitor cv = writer;
        cv = new CheckClassAdapter(cv, true);

        // process input
        ClassReader reader = new ClassReader(input);
        reader.accept(cv, 0);
    }
}