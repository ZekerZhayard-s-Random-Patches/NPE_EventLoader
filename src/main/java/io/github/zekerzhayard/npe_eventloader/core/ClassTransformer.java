package io.github.zekerzhayard.npe_eventloader.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("com.trhsy.sim.loader.EventLoader".equals(transformedName)) {
            ClassNode cn = new ClassNode();
            new ClassReader(basicClass).accept(cn, ClassReader.EXPAND_FRAMES);
            for (MethodNode mn : cn.methods) {
                if (RemapUtils.checkMethodName(cn.name, mn.name, mn.desc, "worldTick") && RemapUtils.checkMethodDesc(mn.desc, "(Lnet/minecraftforge/fml/common/gameevent/TickEvent$WorldTickEvent;)V")) {
                    int ordinal = 0;
                    for (AbstractInsnNode ain : mn.instructions.toArray()) {
                        if (ain.getOpcode() == Opcodes.GETFIELD) {
                            FieldInsnNode fin = (FieldInsnNode) ain;
                            if (RemapUtils.checkClassName(fin.owner, "com/trhsy/sim/npcCode/NpcData") && RemapUtils.checkFieldName(fin.owner, fin.name, fin.desc, "entity") && RemapUtils.checkFieldDesc(fin.desc, "Lcom/trhsy/sim/entity/EntityFolk;")) {
                                ordinal++;
                                if (ordinal == 2) {
                                    LabelNode ln;
                                    AbstractInsnNode ain0 = ain;
                                    while (ain0.getOpcode() != Opcodes.IFNULL) {
                                        ain0 = ain0.getPrevious();
                                    }
                                    ln = ((JumpInsnNode) ain0).label;

                                    ain0 = ain.getPrevious();
                                    mn.instructions.insertBefore(ain, new FieldInsnNode(fin.getOpcode(), fin.owner, fin.name, fin.desc));
                                    mn.instructions.insertBefore(ain, new JumpInsnNode(Opcodes.IFNULL, ln));
                                    mn.instructions.insertBefore(ain, new VarInsnNode(ain0.getOpcode(), ((VarInsnNode) ain0).var));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            cn.accept(cw);
            basicClass = cw.toByteArray();
        }
        return basicClass;
    }
}
