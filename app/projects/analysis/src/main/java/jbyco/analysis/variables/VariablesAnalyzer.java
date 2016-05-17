package jbyco.analysis.variables;

import jbyco.analysis.Analyzer;
import jbyco.io.BytecodeFilesIterator;
import jbyco.io.CommonFile;
import jbyco.io.TemporaryFiles;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;
import org.apache.bcel.util.ByteSequence;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A tool for analysis of the variables' usage.
 */
public class VariablesAnalyzer implements Analyzer {

    /**
     * The map with collected data.
     */
    VariablesMap map;

    /**
     * Instantiates a new variables analyzer.
     */
    public VariablesAnalyzer() {
        this.map = new VariablesMap();
    }

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {

        // init analyzer
        Analyzer analyzer = new VariablesAnalyzer();

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

    /* (non-Javadoc)
     * @see jbyco.analysis.Analyzer#processClassFile(java.jbyco.io.InputStream)
     */
    public void processClassFile(InputStream in) throws IOException {

        // parse class file
        ClassParser parser = new ClassParser(in, null);
        JavaClass klass = parser.parse();

        // process code
        processMethods(klass.getMethods());

    }

    /**
     * Process methods.
     *
     * @param methods the BCEL methods
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected void processMethods(Method[] methods) throws IOException {

        // for all methods
        for (Method m : methods) {

            // get code
            Code code = m.getCode();
            if (code != null) {

                // get total number of variables
                int nlocals = code.getMaxLocals();

                // get number of parameters
                int nparams = m.isStatic() ? 0 : 1;

                for (Type type : m.getArgumentTypes()) {
                    nparams += type.getSize();
                }

                // add method
                map.addMethod(nparams, nlocals);

                // read bytecode
                ByteSequence seq = new ByteSequence(code.getCode());
                while (seq.available() > 0) {

                    // get an instruction
                    Instruction i = Instruction.readInstruction(seq);

                    // process instruction
                    processInstruction(i);
                }
            }
        }
    }

    /**
     * Process instruction.
     *
     * @param i the BCEL instruction
     */
    protected void processInstruction(Instruction i) {

        if (i instanceof LocalVariableInstruction) {

            int var = ((LocalVariableInstruction) i).getIndex();

            if (i instanceof LoadInstruction) {
                map.add(var, "LOAD");
            } else if (i instanceof StoreInstruction) {
                map.add(var, "STORE");
            } else {
                map.add(var, "OTHER");
            }
        }
    }

    /* (non-Javadoc)
     * @see jbyco.analysis.Analyzer#writeResults(java.jbyco.io.PrintWriter)
     */
    public void writeResults(PrintWriter out) {
        map.write(out);
    }
}
