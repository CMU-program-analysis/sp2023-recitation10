package instrument;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ExploratoryClassVisitor extends ClassVisitor {
    /** API Version */
    private static final int API = Opcodes.ASM9;

    /** Constructor */
    public ExploratoryClassVisitor() {
        super(API);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new MethodVisitor(API, super.visitMethod(access, name, desc, signature, exceptions)) {
            @Override
            public void visitInsn(int opcode) {
                super.visitInsn(opcode);
                System.out.println("visitInsn visiting " + opcode);
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                System.out.println("visitMethodInsn visiting:\n\t" + opcode + "\n\t" + owner + "\n\t" + name + "\n\t" + descriptor + "\n\t" + isInterface);
            }

            @Override
            public void visitLdcInsn(Object value) {
                super.visitLdcInsn(value);
                System.out.println("visitLdcInsn visiting " + value);
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                super.visitFieldInsn(opcode, owner, name, descriptor);
                System.out.println("visitFieldInsn visiting:\n\t" + opcode + "\n\t" + owner + "\n\t" + name + "\n\t" + descriptor);
            }
        };
    }
}
