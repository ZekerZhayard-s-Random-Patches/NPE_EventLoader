package io.github.zekerzhayard.fg2_3fixer.modifiers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class UserBasePluginModifier implements IClassModifier {
    @Override
    public String getClassName() {
        return "net/minecraftforge/gradle/user/UserBasePlugin.class";
    }

    @Override
    public byte[] modify(byte[] classBytes) {
        ClassNode cn = new ClassNode();
        new ClassReader(classBytes).accept(cn, ClassReader.EXPAND_FRAMES);
        for (MethodNode mn : cn.methods) {
            if (mn.name.equals("injectIntellijRuns") && mn.desc.equals("(Lorg/w3c/dom/Document;Ljava/lang/String;)V")) {
                boolean foundAddXml = false;
                for (AbstractInsnNode ain : mn.instructions.toArray()) {
                    if (ain.getOpcode() == Opcodes.ARRAYLENGTH) {
                        mn.instructions.insertBefore(ain, new VarInsnNode(Opcodes.ALOAD, 1));
                        mn.instructions.insertBefore(ain, new VarInsnNode(Opcodes.ALOAD, 3));
                        mn.instructions.insertBefore(ain, new MethodInsnNode(Opcodes.INVOKESTATIC, "io/github/zekerzhayard/fg2_3fixer/modifiers/hooks/UserBasePluginHook", "configureRunManagerElement", "(Lorg/w3c/dom/Document;Lorg/w3c/dom/Element;)Lorg/w3c/dom/Element;", false));
                        mn.instructions.insertBefore(ain, new VarInsnNode(Opcodes.ASTORE, 3));
                    } else if (!foundAddXml && ain.getOpcode() == Opcodes.INVOKESTATIC) {
                        MethodInsnNode min = (MethodInsnNode) ain;
                        if (min.owner.equals("net/minecraftforge/gradle/common/Constants") && min.name.equals("addXml") && min.desc.equals("(Lorg/w3c/dom/Node;Ljava/lang/String;Ljava/util/Map;)Lorg/w3c/dom/Element;")) {
                            foundAddXml = true;
                            mn.instructions.insert(ain, new MethodInsnNode(Opcodes.INVOKESTATIC, "io/github/zekerzhayard/fg2_3fixer/modifiers/hooks/UserBasePluginHook", "addGradleBeforeRunTask", "(Lorg/w3c/dom/Element;)Lorg/w3c/dom/Element;", false));
                        }
                    } else if (ain.getOpcode() == Opcodes.BIPUSH) {
                        IntInsnNode iin = (IntInsnNode) ain;
                        if (iin.operand == '_') {
                            iin.operand = '.';
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
