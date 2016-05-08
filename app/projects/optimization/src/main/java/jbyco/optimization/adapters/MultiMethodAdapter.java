package jbyco.optimization.adapters;

import jbyco.optimization.adapters.MethodAdapterFactory;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by vendy on 6.5.16.
 */
public class MultiMethodAdapter extends ClassVisitor {

    MethodAdapterFactory[] adapters;

    public MultiMethodAdapter(int api, ClassVisitor cv, MethodAdapterFactory...adapters) {
        super(api, cv);
        this.adapters = adapters;
    }

    public MultiMethodAdapter(ClassVisitor cv, MethodAdapterFactory...adapters) {
        this(Opcodes.ASM5, cv, adapters);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        // get the last method visitor
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);

        // create the chain of method visitors
        if (mv != null) {

            for (int i = adapters.length - 1; i >= 0; i--) {
                mv = adapters[i].create(mv);
            }
        }

        // return the first method visitor in a chain
        return mv;
    }
}
