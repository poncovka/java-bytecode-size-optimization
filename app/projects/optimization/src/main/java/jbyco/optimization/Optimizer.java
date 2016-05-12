package jbyco.optimization;

import jbyco.optimization.reductions.ConstantNumbersSubstitution;
import jbyco.optimization.reductions.InfoAttributesRemoval;
import jbyco.optimization.peephole.*;
import jbyco.optimization.simplifications.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.IOException;

/**
 * Created by vendy on 3.5.16.
 */
public class Optimizer {

    Runner phase2runner;

    public Optimizer() {
        this.phase2runner = getPhase2Runner();
    }

    public byte[] optimizeClassFile(byte[] input) throws IOException {

        // create class node
        ClassNode result1 = phase1(input);

        // peephole
        ClassNode result2 = phase2(result1);

        // create byte array
        byte[] result3 = phase3(result2);

        // checkInput the output
        check(result3);

        return result3;
    }

    public ClassNode phase1(byte[] input) {

        // create the chain of visitors
        ClassNode node = new ClassNode(Opcodes.ASM5);
        ClassVisitor cv = node;
        cv = new InfoAttributesRemoval(cv);

        // process input
        ClassReader reader = new ClassReader(input);
        reader.accept(cv, ClassReader.SKIP_DEBUG /*|ClassReader.SKIP_FRAMES*/);

        // return the node
        return node;
    }

    public Runner getPhase2Runner() {
        Runner runner = new Runner();
        runner.loadPatterns(
                ArithmeticSimplifications.class,
                VariablesOptimizations.class,
                JumpOptimizations.class,
                MachineIdiomsUsage.class,
                ObjectInsnOptimizations.class,
                StackInsnOptimizations.class,
                StringAppendOptimizations.class
        );

        return runner;
    }

    public ClassNode phase2(ClassNode input) {

        // apply peephole optimizations
        phase2runner.findAndReplace(input);

        // return the node
        return input;
    }

    public byte[] phase3(ClassNode input) {

        // create the chain of visitors
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS /*|ClassWriter.COMPUTE_FRAMES*/);
        ClassVisitor cv = writer;
        cv = new CheckClassAdapter(cv, false);
        cv = new ClassVisitor(Opcodes.ASM5, cv) {

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                mv = new ConstantNumbersSubstitution(mv);
                return mv;
            }

        };

        // process input
        input.accept(cv);

        // return the byte array
        return writer.toByteArray();
    }

    public void check(byte[] input) {

        /*
        // init
        ClassReader reader = new ClassReader(input);
        StringWriter writer = new StringWriter();

        // verify
        CheckClassAdapter.verify(reader, false, new PrintWriter(writer));
        String message = writer.toString();

        // throw exception if there is an error
        if (!message.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        */

        // prepare visitors
        ClassWriter writer = new ClassWriter(0);
        ClassVisitor cv = writer;
        cv = new CheckClassAdapter(cv, true);

        // process input
        ClassReader reader = new ClassReader(input);
        reader.accept(cv, 0);

    }

}