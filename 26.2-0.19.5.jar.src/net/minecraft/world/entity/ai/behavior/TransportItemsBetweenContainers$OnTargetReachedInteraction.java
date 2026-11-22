package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.PathfinderMob;
import org.apache.commons.lang3.function.TriConsumer;

@FunctionalInterface
public interface OnTargetReachedInteraction extends TriConsumer<PathfinderMob, TransportItemsBetweenContainers.TransportItemTarget, Integer> {}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\TransportItemsBetweenContainers$OnTargetReachedInteraction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */