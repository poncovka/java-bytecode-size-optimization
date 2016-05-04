package jbyco.optimization;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;

/**
 * Created by vendy on 3.5.16.
 */
public class BytecodeOptimizer implements Optimizer {

    @Override
    public byte[] optimizeClassFile(byte[] bytes) throws IOException {

        // init
        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        // process
        reader.accept(writer, 0);

        // return new array of bytes
        return writer.toByteArray();
    }
}
