package main;

import instrument.ExploratoryClassVisitor;
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

@Mojo(name="explore")
public class ExploreGoal extends AbstractMojo {
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
        ClassVisitor visitor = new ExploratoryClassVisitor();
        reader.accept(visitor, 0);
    }
}
