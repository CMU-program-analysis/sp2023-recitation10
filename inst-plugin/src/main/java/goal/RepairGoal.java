package main;

import instrument.ArithmeticClassVisitor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

@Mojo(name="repair")
public class RepairGoal extends AbstractMojo {
    @Parameter(property="class", required=true)
    private String className;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File f = new File("target/classes/" + className.replace(".", "/") + ".class");
        byte[] classBytes;
        try(FileInputStream fis = new FileInputStream(f)) {
            classBytes = fis.readAllBytes();
        } catch (IOException e) {
            throw new MojoExecutionException(e.toString());
        }
        ClassReader reader = new ClassReader(classBytes);
        ClassWriter writer = new ClassWriter(reader, COMPUTE_FRAMES | COMPUTE_MAXS);
        ClassVisitor visitor = new ArithmeticClassVisitor(writer);
        reader.accept(visitor, 0);
        try(FileOutputStream fos = new FileOutputStream(f, false)) {
            fos.write(writer.toByteArray());
        } catch (IOException e) {
            throw new MojoExecutionException(e.toString());
        }
    }
}
