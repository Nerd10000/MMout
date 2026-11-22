/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class DamageEntity extends Record implements EnchantmentEntityEffect {
/*    */   private final LevelBasedValue minDamage;
/*    */   
/*    */   private final LevelBasedValue maxDamage;
/*    */   
/*    */   private final Holder<DamageType> damageType;
/*    */   
/*    */   public static final MapCodec<DamageEntity> CODEC;
/*    */   
/*    */   public DamageEntity(LevelBasedValue minDamage, LevelBasedValue maxDamage, Holder<DamageType> damageType) {
/* 15 */     this.minDamage = minDamage;
/* 15 */     this.maxDamage = maxDamage;
/* 15 */     this.damageType = damageType;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/DamageEntity;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/DamageEntity;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/DamageEntity;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/DamageEntity;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/DamageEntity;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/DamageEntity;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public LevelBasedValue minDamage() {
/* 15 */     return this.minDamage;
/*    */   }
/*    */   
/*    */   public LevelBasedValue maxDamage() {
/* 15 */     return this.maxDamage;
/*    */   }
/*    */   
/*    */   public Holder<DamageType> damageType() {
/* 15 */     return this.damageType;
/*    */   }
/*    */   
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("min_damage").forGetter(DamageEntity::minDamage), (App)LevelBasedValue.CODEC.fieldOf("max_damage").forGetter(DamageEntity::maxDamage), (App)DamageType.CODEC.fieldOf("damage_type").forGetter(DamageEntity::damageType)).apply((Applicative)i, DamageEntity::new));
/*    */   }
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 28 */     float damage = Mth.randomBetween(entity.getRandom(), this.minDamage.calculate(enchantmentLevel), this.maxDamage.calculate(enchantmentLevel));
/* 29 */     entity.hurtServer(serverLevel, new DamageSource(this.damageType, (Entity)item.owner()), damage);
/*    */   }
/*    */   
/*    */   public MapCodec<DamageEntity> codec() {
/* 34 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\DamageEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */