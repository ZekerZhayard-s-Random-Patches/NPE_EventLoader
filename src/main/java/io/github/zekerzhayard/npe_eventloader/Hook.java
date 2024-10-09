package io.github.zekerzhayard.npe_eventloader;

import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.race.Race;
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

    public static NpcData checkRace(NpcData data) {
        if (data.race == null) {
            data.race = new Race();
        }
        return data;
    }
}
