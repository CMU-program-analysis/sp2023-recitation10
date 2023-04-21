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
                super.visitLineNumber(line, start);

                super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                super.visitLdcInsn("covered line " + line);
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
        };
    }
}
