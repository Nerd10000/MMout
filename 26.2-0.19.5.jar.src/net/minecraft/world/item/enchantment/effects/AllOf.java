/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface AllOf {
/*    */   static <T, A extends T> MapCodec<A> codec(Codec<T> topLevelCodec, Function<List<T>, A> constructor, Function<A, List<T>> accessor) {
/* 19 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)topLevelCodec.listOf().fieldOf("effects").forGetter(accessor)).apply((Applicative)i, constructor));
/*    */   }
/*    */   
/*    */   static EntityEffects entityEffects(EnchantmentEntityEffect... effects) {
/* 25 */     return new EntityEffects(List.of(effects));
/*    */   }
/*    */   
/*    */   static LocationBasedEffects locationBasedEffects(EnchantmentLocationBasedEffect... effects) {
/* 29 */     return new LocationBasedEffects(List.of(effects));
/*    */   }
/*    */   
/*    */   static ValueEffects valueEffects(EnchantmentValueEffect... effects) {
/* 33 */     return new ValueEffects(List.of(effects));
/*    */   }
/*    */   
/*    */   public static final class EntityEffects extends Record implements EnchantmentEntityEffect {
/*    */     private final List<EnchantmentEntityEffect> effects;
/*    */     
/*    */     public EntityEffects(List<EnchantmentEntityEffect> effects) {
/* 36 */       this.effects = effects;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/AllOf$EntityEffects;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$EntityEffects;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/AllOf$EntityEffects;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$EntityEffects;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/AllOf$EntityEffects;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$EntityEffects;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public List<EnchantmentEntityEffect> effects() {
/* 36 */       return this.effects;
/*    */     }
/*    */     
/* 37 */     public static final MapCodec<EntityEffects> CODEC = AllOf.codec(EnchantmentEntityEffect.CODEC, EntityEffects::new, EntityEffects::effects);
/*    */     
/*    */     public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 41 */       for (EnchantmentEntityEffect effect : this.effects)
/* 42 */         effect.apply(serverLevel, enchantmentLevel, item, entity, position); 
/*    */     }
/*    */     
/*    */     public MapCodec<EntityEffects> codec() {
/* 48 */       return CODEC;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class LocationBasedEffects extends Record implements EnchantmentLocationBasedEffect {
/*    */     private final List<EnchantmentLocationBasedEffect> effects;
/*    */     
/*    */     public LocationBasedEffects(List<EnchantmentLocationBasedEffect> effects) {
/* 52 */       this.effects = effects;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #52	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #52	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #52	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$LocationBasedEffects;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public List<EnchantmentLocationBasedEffect> effects() {
/* 52 */       return this.effects;
/*    */     }
/*    */     
/* 53 */     public static final MapCodec<LocationBasedEffects> CODEC = AllOf.codec(EnchantmentLocationBasedEffect.CODEC, LocationBasedEffects::new, LocationBasedEffects::effects);
/*    */     
/*    */     public void onChangedBlock(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position, boolean becameActive) {
/* 57 */       for (EnchantmentLocationBasedEffect effect : this.effects)
/* 58 */         effect.onChangedBlock(serverLevel, enchantmentLevel, item, entity, position, becameActive); 
/*    */     }
/*    */     
/*    */     public void onDeactivated(EnchantedItemInUse item, Entity entity, Vec3 position, int level) {
/* 64 */       for (EnchantmentLocationBasedEffect effect : this.effects)
/* 65 */         effect.onDeactivated(item, entity, position, level); 
/*    */     }
/*    */     
/*    */     public MapCodec<LocationBasedEffects> codec() {
/* 71 */       return CODEC;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class ValueEffects extends Record implements EnchantmentValueEffect {
/*    */     private final List<EnchantmentValueEffect> effects;
/*    */     
/*    */     public ValueEffects(List<EnchantmentValueEffect> effects) {
/* 75 */       this.effects = effects;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/AllOf$ValueEffects;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #75	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$ValueEffects;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/AllOf$ValueEffects;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #75	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$ValueEffects;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/AllOf$ValueEffects;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #75	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/AllOf$ValueEffects;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public List<EnchantmentValueEffect> effects() {
/* 75 */       return this.effects;
/*    */     }
/*    */     
/* 76 */     public static final MapCodec<ValueEffects> CODEC = AllOf.codec(EnchantmentValueEffect.CODEC, ValueEffects::new, ValueEffects::effects);
/*    */     
/*    */     public float process(int enchantmentLevel, RandomSource random, float value) {
/* 80 */       for (EnchantmentValueEffect effect : this.effects)
/* 81 */         value = effect.process(enchantmentLevel, random, value); 
/* 83 */       return value;
/*    */     }
/*    */     
/*    */     public MapCodec<ValueEffects> codec() {
/* 88 */       return CODEC;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\AllOf.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */