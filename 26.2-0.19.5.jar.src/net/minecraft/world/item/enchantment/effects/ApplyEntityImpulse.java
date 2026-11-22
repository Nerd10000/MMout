/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ApplyEntityImpulse extends Record implements EnchantmentEntityEffect {
/*    */   private final Vec3 direction;
/*    */   
/*    */   private final Vec3 coordinateScale;
/*    */   
/*    */   private final LevelBasedValue magnitude;
/*    */   
/*    */   public static final MapCodec<ApplyEntityImpulse> CODEC;
/*    */   
/*    */   private static final int POST_IMPULSE_CONTEXT_RESET_GRACE_TIME_TICKS = 10;
/*    */   
/*    */   public ApplyEntityImpulse(Vec3 direction, Vec3 coordinateScale, LevelBasedValue magnitude) {
/* 13 */     this.direction = direction;
/* 13 */     this.coordinateScale = coordinateScale;
/* 13 */     this.magnitude = magnitude;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ApplyEntityImpulse;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyEntityImpulse;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ApplyEntityImpulse;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyEntityImpulse;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ApplyEntityImpulse;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyEntityImpulse;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 direction() {
/* 13 */     return this.direction;
/*    */   }
/*    */   
/*    */   public Vec3 coordinateScale() {
/* 13 */     return this.coordinateScale;
/*    */   }
/*    */   
/*    */   public LevelBasedValue magnitude() {
/* 13 */     return this.magnitude;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Vec3.CODEC.fieldOf("direction").forGetter(ApplyEntityImpulse::direction), (App)Vec3.CODEC.fieldOf("coordinate_scale").forGetter(ApplyEntityImpulse::coordinateScale), (App)LevelBasedValue.CODEC.fieldOf("magnitude").forGetter(ApplyEntityImpulse::magnitude)).apply((Applicative)i, ApplyEntityImpulse::new));
/*    */   }
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 28 */     Vec3 look = entity.getLookAngle();
/* 29 */     Vec3 direction = look.addLocalCoordinates(this.direction).multiply(this.coordinateScale).scale(this.magnitude.calculate(enchantmentLevel));
/* 30 */     entity.addDeltaMovement(direction);
/* 31 */     entity.hurtMarked = true;
/* 32 */     entity.needsSync = true;
/* 33 */     if (entity instanceof LivingEntity) {
/* 33 */       LivingEntity livingEntity = (LivingEntity)entity;
/* 34 */       livingEntity.applyPostImpulseGraceTime(10);
/*    */     } 
/*    */   }
/*    */   
/*    */   public MapCodec<ApplyEntityImpulse> codec() {
/* 40 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\ApplyEntityImpulse.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */