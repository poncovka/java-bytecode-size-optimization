package jbyco.optimization;

import jbyco.optimization.expansion.DuplicationExpansion;
import jbyco.optimization.reductions.ConstantNumbersSubstitution;
import jbyco.optimization.reductions.DuplicationReduction;
import jbyco.optimization.reductions.InfoAttributesRemoval;
import jbyco.optimization.peephole.*;
import jbyco.optimization.reductions.JumpReductions;
import jbyco.optimization.simplifications.*;
import jbyco.optimization.transformation.ClassTransformer;
import jbyco.optimization.transformation.MethodTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.IOException;
import java.util.List;

/**
 * Created by vendy on 3.5.16.
 */
public class Optimizer {

    Runner phase1runner;
    Runner phase2runner;
    Runner phase3runner;
    Statistics stats;

    public Optimizer() {
        this.phase1runner = getPhase1Runner();
        this.phase2runner = getPhase2Runner();
        this.phase3runner = getPhase3Runner();
    }

    public void setStatistics(Statistics stats) {
        this.stats = stats;
    }

    public byte[] optimizeClassFile(byte[] input) throws IOException {

        // statistics
        if (stats != null) {
            stats.addSizeBefore(input.length);
        }

        // create class node
        ClassNode result1 = phase1(input);

        // peephole
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
        cv = new InfoAttributesRemoval(cv);

        // process input
        ClassReader reader = new ClassReader(input);
        reader.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.EXPAND_FRAMES /*|ClassReader.SKIP_FRAMES*/);

        // apply peephole optimizations runner
        phase1runner.setStatistics(stats);
        phase1runner.findAndReplace(node);

        // return the node
        return node;
    }

    public Runner getPhase1Runner() {
        Runner runner = new Runner();
        runner.loadPatterns(
                DuplicationExpansion.class
        );
        return runner;
    }

    public Runner getPhase2Runner() {
        // from specific to general
        // duplication simplifications are last
        Runner runner = new Runner();
        runner.loadPatterns(
                ObjectSimplifications.class,
                StringAppendSimplifications.class,
                ConversionsSimplifications.class,
                AlgebraicSimplifications.class,
                JumpSimplifications.class,
                StackSimplifications.class
        );
        return runner;
    }

    public ClassNode phase2(ClassNode input) {

        // apply peephole optimizations
        phase2runner.setStatistics(stats);
        phase2runner.findAndReplace(input);

        // apply jump optimizations
        ClassTransformer transformer = new ClassTransformer() {
            @Override
            public void transform(ClassNode cn) {

                MethodTransformer mt = new JumpReductions(stats);
                for (MethodNode mn : (List<MethodNode>)cn.methods) {
                    mt.transform(mn);
                }
            }
        };

        transformer.transform(input);

        // return the node
        return input;
    }

    public Runner getPhase3Runner() {
        Runner runner = new Runner();
        runner.loadPatterns(
                DuplicationReduction.class
        );
        return runner;
    }

    public byte[] phase3(ClassNode input) {

        // apply peephole optimizations
        phase3runner.setStatistics(stats);
        phase3runner.findAndReplace(input);

        // create the chain of visitors
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = writer;
        cv = new ClassVisitor(Opcodes.ASM5, cv) {

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                mv = new ConstantNumbersSubstitution(mv);
                mv = new LocalVariablesSorter(access, desc, mv);
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