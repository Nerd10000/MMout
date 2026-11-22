/*    */ package net.minecraft.world.item.enchantment.providers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public interface EnchantmentProviderTypes {
/*    */   static MapCodec<? extends EnchantmentProvider> bootstrap(Registry<MapCodec<? extends EnchantmentProvider>> registry) {
/*  8 */     Registry.register(registry, "by_cost", EnchantmentsByCost.CODEC);
/*  9 */     Registry.register(registry, "by_cost_with_difficulty", EnchantmentsByCostWithDifficulty.CODEC);
/* 10 */     return (MapCodec<? extends EnchantmentProvider>)Registry.register(registry, "single", SingleEnchantment.CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\providers\EnchantmentProviderTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */