## Setup

Install Maven by
following the [directions](https://maven.apache.org/install.html) from Apache
or using your favored package installer on Mac or Linux.

Build the code in this repo using
```[shell script]
cd inst-plugin && mvn clean install
cd ../sample-programs && mvn clean compile
cd ..
```

The code in `sample-programs` and the `goals` package in `inst-plugin` is for
running and testing purposes - you can and should look at this source code,
but you won't need to modify it. The code to be modified is in the
`instrument` package of `inst-plugin`.

## Bytecode

Java compiles into JVM bytecode in `.class` files, and these (not the original
`.java` files) are run on the JVM when a Java program is run.

To see what that bytecode actually looks like, we can use `javap -c`, which
gives the bytecode stored in `.class` files. For example, in the 
`sample-programs/target/classes` directory, try running:
```shell script
javap -c HelloWorld
```
It should print the following:
```java
Compiled from "HelloWorld.java"
public class HelloWorld {
  public HelloWorld();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
       3: ldc           #3                  // String Hello, World!
       5: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
       8: return
}
```

The first method, `HelloWorld()`, is the constructor, which all
Java objects have. It's recognizable by its invocation of `<init>`. 

Of more interest is the second method, `main(String[])`, which is actually
defined in the program in `HelloWorld.java` as a single invocation of
the method `System.out.println` with the string `Hello, World!`. The bytecode
for this method first gets the object `System.out` with `getstatic`, then
loads the constant string `Hello, World!` with `ldc`, and finally invokes the
`println` method from `System.out` before returning.

We can tell what constants are loaded and stored from the comments printed
alongside the bytecode instructions. These are actually retrieved from the
numbers next to the instructions, which make references to the constant pool
(visible by running `javap -v HelloWorld`).

Not only does `javap` show us the general form of Java bytecode, it gives us the
specific instructions we need in order to print something, which we'll use
in the next section.

## API: `org.objectweb.asm`

For manipulating bytecode, we'll be using the `org.objectweb.asm` package from
ow2. It's built on a visitor pattern, providing the ability to change what the
program does when processing each bytecode instruction. For an example of how
this works, see `ExploratoryClassVisitor` in the `inst-plugin` folder, which overrides
several methods of `MethodVisitor`. The `ExploratoryClassVisitor` will be
helpful for this recitation's exercises by printing out what the arguments to
each of these methods we want to use or override should look like.

To run it on a sample program (`HelloWorld` or `SelectionSort`), run
```shell script
mvn recitation-10:explore -Dclass="HelloWorld"
```
or
```shell script
mvn recitation-10:explore -Dclass="SelectionSort"
```
while in the `sample-programs` directory.

## Challenge 1: Coverage Instrumentation

We're going to instrument bytecode in order to get code coverage information.
The code we're going to change for this section is in `CoverageClassVisitor`.

To get line number information, we're going to override the ClassVisitor's
method for visiting a line number by having it additionally execute the
instructions that we saw print a string with `javap`:
- `getstatic` for `System.out`
- `ldc` the string we want to load (here, the line number)
- `invokevirtual` for `println`

When you think you've correctly overridden the method, build it by running
```shell script
mvn clean install
```
in the `inst-plugin` folder and test it by running
```shell script
mvn recitation-10:coverage -Dclass="HelloWorld"
mvn compile exec:java -Dexec.mainClass="HelloWorld"
```
in the `sample-programs` folder.

## Challenge 2: Program Repair

Our next challenge is to change the program behavior itself. Try running the
other sample program, an implementation of Selection Sort, using:
```shell script
mvn compile exec:java -Dexec.mainClass="SelectionSort" -Dexec.args="3 2 1"
```
in the `sample-programs` folder.
We want it to sort the output, printing `[1, 2, 3]`, but, instead, it throws an
`ArrayIndexOutOfBoundsException`! There's a bug in this sort method (available
to view in `SelectionSort.java`). We could look for it to fix it 
manually, but, for this exercise, we're going to try instrumenting the code
instead.

Through magic intuition, we know that the problem will be fixed by changing the
subtractions in the program to additions. Try to do this by changing
`visitInsn` in `ArithmeticClassVisitor`.

When you think you've correctly overridden the method, build it by running
```shell script
mvn clean install
```
in the `inst-plugin` folder and test it by running
```shell script
mvn recitation-10:repair -Dclass="SelectionSort"
mvn compile exec:java -Dexec.mainClass="SelectionSort" -Dexec.args="3 2 1"
```
in the `sample-programs` folder.

## Resources
[ASM Javadoc](https://asm.ow2.io/javadoc/index.html)
