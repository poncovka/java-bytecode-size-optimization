package jbyco.optimization;

import jbyco.optimization.adapters.MultiMethodAdapter;
import jbyco.optimization.peephole.PeepholeRunner;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by vendy on 3.5.16.
 */
public class BytecodeOptimizer implements Optimizer {

    @Override
    public byte[] optimizeClassFile(byte[] input) throws IOException {

        // create class node
        ClassNode result1 = phase1(input);

        // peephole
        ClassNode result2 = phase2(result1);

        // create byte array
        byte[] result3 = phase3(result2);

        // check the output
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

    public ClassNode phase2(ClassNode input) {

        // run peephole optimizer
        PeepholeRunner runner = new PeepholeRunner();
        runner.loadPatterns(PeepholeOptimizations.class);
        runner.run(input);

        // return the node
        return input;
    }

    public byte[] phase3(ClassNode input) {

        // create the chain of visitors
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS /*|ClassWriter.COMPUTE_FRAMES*/);
        ClassVisitor cv = writer;
        cv = new CheckClassAdapter(cv, false);
        cv = new MultiMethodAdapter(cv, ConstantNumbersSubstitution::new);

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