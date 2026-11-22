/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class LocationBasedEffects extends Record implements EnchantmentLocationBasedEffect {
/*    */   private final List<EnchantmentLocationBasedEffect> effects;
/*    */   
/*    */   public LocationBasedEffects(List<EnchantmentLocationBasedEffect> effects) {
/* 52 */     this.effects = effects;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #52	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #52	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #52	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<EnchantmentLocationBasedEffect> effects() {
/* 52 */     return this.effects;
/*    */   }
/*    */   
/* 53 */   public static final MapCodec<LocationBasedEffects> CODEC = AllOf.codec(EnchantmentLocationBasedEffect.CODEC, LocationBasedEffects::new, LocationBasedEffects::effects);
/*    */   
/*    */   public void onChangedBlock(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position, boolean becameActive) {
/* 57 */     for (EnchantmentLocationBasedEffect effect : this.effects)
/* 58 */       effect.onChangedBlock(serverLevel, enchantmentLevel, item, entity, position, becameActive); 
/*    */   }
/*    */   
/*    */   public void onDeactivated(EnchantedItemInUse item, Entity entity, Vec3 position, int level) {
/* 64 */     for (EnchantmentLocationBasedEffect effect : this.effects)
/* 65 */       effect.onDeactivated(item, entity, position, level); 
/*    */   }
/*    */   
/*    */   public MapCodec<LocationBasedEffects> codec() {
/* 71 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\AllOf$LocationBasedEffects.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */