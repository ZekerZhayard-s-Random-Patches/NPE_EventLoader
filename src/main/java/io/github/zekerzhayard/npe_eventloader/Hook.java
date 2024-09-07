package io.github.zekerzhayard.npe_eventloader;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Hook {
    public static World getWorld(Minecraft mc, TickEvent.WorldTickEvent event) {
        World world = mc.world;
        if (world == null) {
            world = event.world;
        }
        return world;
    }
}
