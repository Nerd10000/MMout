package net.minecraft.client.particle;

@FunctionalInterface
interface SpriteParticleRegistration<T extends net.minecraft.core.particles.ParticleOptions> {
  ParticleProvider<T> create(SpriteSet paramSpriteSet);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ParticleResources$SpriteParticleRegistration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */