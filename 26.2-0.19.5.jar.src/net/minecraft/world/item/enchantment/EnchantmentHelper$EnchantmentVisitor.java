package net.minecraft.world.item.enchantment;

import net.minecraft.core.Holder;

@FunctionalInterface
interface EnchantmentVisitor {
  void accept(Holder<Enchantment> paramHolder, int paramInt);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\EnchantmentHelper$EnchantmentVisitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */