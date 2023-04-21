package instrument;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;

public class CoverageClassVisitor extends ClassVisitor {
    /** API Version */
    private static final int API = Opcodes.ASM9;

    /** Constructor */
    public CoverageClassVisitor(ClassWriter cw) {
        super(API, cw);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new MethodVisitor(API, super.visitMethod(access, name, desc, signature, exceptions)) {
            @Override
            public void visitLineNumber(int line, Label start) {
                //TODO: Print the line number of each line that is run in the program!
                //  (currently, this doesn't print anything)
                super.visitLineNumber(line, start);
            }
        };
    }
}
