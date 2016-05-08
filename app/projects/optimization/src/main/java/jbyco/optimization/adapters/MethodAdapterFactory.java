package jbyco.optimization.adapters;

import org.objectweb.asm.MethodVisitor;

/**
 * A functional interface for a method visitor constructor.
 */

@FunctionalInterface
public interface MethodAdapterFactory {
    MethodVisitor create(MethodVisitor mv);
}
