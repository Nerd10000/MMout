/*    */ package net.minecraft.world.item.enchantment.providers;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ 
/*    */ public interface EnchantmentProvider {
/* 15 */   public static final Codec<EnchantmentProvider> DIRECT_CODEC = BuiltInRegistries.ENCHANTMENT_PROVIDER_TYPE.byNameCodec().dispatch(EnchantmentProvider::codec, Function.identity());
/*    */   
/*    */   void enchant(ItemStack paramItemStack, ItemEnchantments.Mutable paramMutable, RandomSource paramRandomSource, DifficultyInstance paramDifficultyInstance);
/*    */   
/*    */   MapCodec<? extends EnchantmentProvider> codec();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\providers\EnchantmentProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */