package net.minecraft.client.renderer.debug;

import net.minecraft.world.phys.Vec3;

@FunctionalInterface
interface ListenerVisitor {
  void accept(Vec3 paramVec3, int paramInt);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\GameEventListenerRenderer$ListenerVisitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */