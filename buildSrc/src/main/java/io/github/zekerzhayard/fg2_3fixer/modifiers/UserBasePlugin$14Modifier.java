package io.github.zekerzhayard.fg2_3fixer.modifiers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class UserBasePlugin$14Modifier implements IClassModifier {
    @Override
    public String getClassName() {
        return "net/minecraftforge/gradle/user/UserBasePlugin$14.class";
    }

    @Override
    public byte[] modify(byte[] classBytes) {
        ClassNode cn = new ClassNode();
        new ClassReader(classBytes).accept(cn, ClassReader.EXPAND_FRAMES);
        for (MethodNode mn : cn.methods) {
            if (mn.name.equals("execute") && mn.desc.equals("(Lorg/gradle/api/Task;)V")) {
                for (AbstractInsnNode ain : mn.instructions.toArray()) {
                    if (ain.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                        MethodInsnNode min = (MethodInsnNode) ain;
                        if (min.owner.equals("java/io/File") && min.name.equals("exists") && min.desc.equals("()Z")) {
                            mn.instructions.insertBefore(ain, new MethodInsnNode(Opcodes.INVOKESTATIC, "io/github/zekerzhayard/fg2_3fixer/modifiers/hooks/UserBasePluginHook", "createWorkspaceXml", "(Ljava/io/File;)Ljava/io/File;", false));
                            break;
                        }
                    }
                }
            }
        }
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cn.accept(cw);
        return cw.toByteArray();
    }
}
