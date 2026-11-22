package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;

public interface Sprite<T extends net.minecraft.core.particles.ParticleOptions> {
  SingleQuadParticle createParticle(T paramT, ClientLevel paramClientLevel, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, RandomSource paramRandomSource);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ParticleProvider$Sprite.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */