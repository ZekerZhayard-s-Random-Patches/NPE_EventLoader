package io.github.zekerzhayard.fg2_3fixer.modifiers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class GetAssetTaskModifier implements IClassModifier {
    @Override
    public String getClassName() {
        return "net/minecraftforge/gradle/tasks/DownloadAssetsTask$GetAssetTask.class";
    }

    @Override
    public byte[] modify(byte[] classBytes) {
        ClassNode cn = new ClassNode();
        new ClassReader(classBytes).accept(cn, ClassReader.EXPAND_FRAMES);
        for (MethodNode mn : cn.methods) {
            if (mn.name.equals("call") && mn.desc.equals("()Ljava/lang/Boolean;")) {
                for (AbstractInsnNode ain : mn.instructions.toArray()) {
                    if (ain.getOpcode() == Opcodes.LDC) {
                        LdcInsnNode lin = (LdcInsnNode) ain;
                        if ("http://resources.download.minecraft.net/".equals(lin.cst)) {
                            lin.cst = "https://resources.download.minecraft.net/";
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
