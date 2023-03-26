package touch.event.gradle.asm.matcher;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ParameterNode;

import java.util.List;

/**
 * @Description: {@link org.objectweb.asm.tree.MethodNode}
 * @Author wangjianzhou
 * @Date 2022/6/15 12:19
 * @Version c
 */
public class MethodInfo {
    /**
     * The method's access flags (see {@link Opcodes}). This field also indicates if the method is
     * synthetic and/or deprecated.
     */
    public int access;

    /** The method's name. */
    public String name;

    /** The method's descriptor (see {@link Type}). */
    public String desc;

    /** The method's signature. May be {@literal null}. */
    public String signature;

    /** The internal names of the method's exception classes (see {@link Type#getInternalName()}). */
    public List<String> exceptions;

    /** The method parameter info (access flags and name). */
    public List<ParameterNode> parameters;

    public MethodInfo(int access, String name, String desc) {
        this.access = access;
        this.name = name;
        this.desc = desc;
    }

    public MethodInfo(int access, String name, String desc, String signature, List<String> exceptions, List<ParameterNode> parameters) {
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
        this.parameters = parameters;
    }
}
