package jbyco.analysis.max;

import jbyco.analysis.Analyzer;
import jbyco.io.BytecodeFilesIterator;
import jbyco.io.CommonFile;
import jbyco.io.TemporaryFiles;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

/**
 * A class for analysing maximal numbers of locals and maximal depths of stacks.
 */
public class MaxAnalyzer implements Analyzer {

    Map<Integer, Integer> stacks = new TreeMap<>();
    Map<Integer, Integer> locals = new TreeMap<>();

    @Override
    public void processClassFile(InputStream in) throws IOException {

        ClassReader reader = new ClassReader(in);
        ClassVisitor cv = new MaximumsClassVisitor();
        reader.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

    }

    class MaximumsClassVisitor extends ClassVisitor {

        public MaximumsClassVisitor() {
            super(Opcodes.ASM5);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            return new MaximumxMethodVisitor();
        }
    }

    class MaximumxMethodVisitor extends MethodVisitor {

        public MaximumxMethodVisitor() {
            super(Opcodes.ASM5);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            stacks.put(maxStack, stacks.getOrDefault(maxStack, 0) + 1);
            locals.put(maxLocals, locals.getOrDefault(maxLocals, 0) + 1);
        }
    }

    @Override
    public void writeResults(PrintWriter out) {

        // set format
        String format = "%-10s %-10s\n";

        // print max stack
        out.printf(format,
                "MAX_STACK",
                "METHODS"
        );

        for (int max : stacks.keySet()) {
            out.printf(format,
                    max,
                    stacks.get(max)
            );
        }

        // print max locals
        out.printf(format,
                "MAX_LOCALS",
                "METHODS"
        );


        for (int max : locals.keySet()) {
            out.printf(format,
                    max,
                    locals.get(max)
            );
        }
    }

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {

        // init analyzer
        Analyzer analyzer = new MaxAnalyzer();

        // create temporary directory
        Path workingDirectory = TemporaryFiles.createDirectory();

        try {

            // process files
            for (String str : args) {

                // get path
                Path path = Paths.get(str);

                // process files on the path
                for (CommonFile file : (new BytecodeFilesIterator(path, workingDirectory))) {

                    InputStream in = file.getInputStream();
                    analyzer.processClassFile(in);
                    in.close();
                }
            }

        } finally {
            TemporaryFiles.deleteDirectory(workingDirectory);
        }

        // print results
        PrintWriter writer = new PrintWriter(System.out, true);
        analyzer.writeResults(writer);
        writer.close();
    }
}
