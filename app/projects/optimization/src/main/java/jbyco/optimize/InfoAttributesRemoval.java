package jbyco.optimize;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * A class visitor for removing attributes with debugging information.
 * 
 * Removed attributes are:
 *   SourceFile - class
 *   SourceDebugExtension - class
 *   Deprecated - class, field, method
 *   LineNumberTable - code
 *   LocalVariableTable - code
 *   LocalVariableTypeTable - code
 */
public class InfoAttributesRemoval extends ClassVisitor {

    /**
     * Instantiates a new class visitor for info attributes removal.
     */
    public InfoAttributesRemoval() {
        super(Opcodes.ASM5);
    }

    /**
     * Instantiates a new class visitor for info attributes removal.
     *
     * @param cv the class visitor
     */
    public InfoAttributesRemoval(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    /* (non-Javadoc)
     * @see org.objectweb.asm.ClassVisitor#visitSource(java.lang.String, java.lang.String)
     */
    @Override
    public void visitSource(String source, String debug) {
        // remove SourceFile and SourceDebugExtension attributes
    }

    /* (non-Javadoc)
     * @see org.objectweb.asm.ClassVisitor#visit(int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

        // remove Deprecated attribute from access
        access &= ~Opcodes.ACC_DEPRECATED;

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature,
            String[] exceptions) {

        // remove Deprecated attribute from access
        access &= ~Opcodes.ACC_DEPRECATED;

        // call super visit method
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        // remove info attributes in the code of the method
        return new InfoAttributesMethodRemoval(mv);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {

        // remove Deprecated attribute from access
        access &= ~Opcodes.ACC_DEPRECATED;

        return super.visitField(access, name, desc, signature, value);
    }

    public class InfoAttributesMethodRemoval extends MethodVisitor {

        public InfoAttributesMethodRemoval() {
            super(Opcodes.ASM5);
        }

        public InfoAttributesMethodRemoval(MethodVisitor mv) {
            super(Opcodes.ASM5, mv);
        }

        @Override
        public void visitLineNumber(int n, Label label) {
            // remove LineNumberTable
        }

        @Override
        public void visitLocalVariable(String arg0, String arg1, String arg2, Label arg3, Label arg4, int arg5) {
            // remove LocalVariableTable
        }
    }
}
