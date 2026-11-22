package net.minecraft.world.entity.projectile;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface ProjectileFactory<T extends Projectile> {
  T create(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\Projectile$ProjectileFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */