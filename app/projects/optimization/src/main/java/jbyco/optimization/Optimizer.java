package jbyco.optimization;

import jbyco.optimization.expansion.DuplicationExpansion;
import jbyco.optimization.jump.*;
import jbyco.optimization.reduction.*;
import jbyco.optimization.peephole.*;
import jbyco.optimization.simplification.*;
import jbyco.optimization.common.ClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.IOException;

/**
 * A class for java bytecode optimization.
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
        LabelTransformer t1 = new LabelTransformer(collector, stats);
        FrameTransformer t2 = new FrameTransformer(t1, collector, stats);
        LookupSwitchTransformer t3 = new LookupSwitchTransformer(t2, collector, stats);
        TableSwitchTransformer t4 = new TableSwitchTransformer(t3, collector, stats);

        t1.loadActions(
                JumpReductions.class
        );

        t2.loadActions(
                JumpReductions.class
        );

        t3.loadActions(
                SwitchSubstitution.class
        );

        t4.loadActions(
                SwitchSubstitution.class
        );

        jumpSimplifier = new JumpTransformer(t4, collector, stats);
    }

    public void initPhase3() {
        reducer = new PeepholeRunner(stats);
        reducer.loadActions(
                DuplicationReduction.class
        );

    }

    public byte[] optimizeClassFile(byte[] input) throws IOException {

        // init
        byte[] output = null;
        boolean change = true;

        // statistics
        if (stats != null) {
            stats.addSizeBefore(input.length);
        }

        // optimize while there are changes
        while (change) {

            // expansion
            ClassNode result1 = phase1(input);

            // simplification
            ClassNode result2 = phase2(result1);

            // reduction
            output = phase3(result2);

            // check the result
            check(output);

            // check the size
            if (input.length <= output.length) {
                change = false;
            }

            // set the input
            input = output;
        }

        // statistics
        if (stats != null) {
            stats.addSizeAfter(output.length);
        }

        return output;
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
        cv = new CheckClassAdapter(cv, false);
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