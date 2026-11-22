/*     */ package net.minecraft.world.item.enchantment.effects;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.ConstantFloat;
/*     */ import net.minecraft.util.valueproviders.FloatProvider;
/*     */ import net.minecraft.util.valueproviders.FloatProviders;
/*     */ 
/*     */ public final class VelocitySource extends Record {
/*     */   private final float movementScale;
/*     */   
/*     */   private final FloatProvider base;
/*     */   
/*     */   public static final MapCodec<VelocitySource> CODEC;
/*     */   
/*     */   public VelocitySource(float movementScale, FloatProvider base) {
/*  96 */     this.movementScale = movementScale;
/*  96 */     this.base = base;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #96	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #96	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #96	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float movementScale() {
/*  96 */     return this.movementScale;
/*     */   }
/*     */   
/*     */   public FloatProvider base() {
/*  96 */     return this.base;
/*     */   }
/*     */   
/*     */   static {
/* 100 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.optionalFieldOf("movement_scale", Float.valueOf(0.0F)).forGetter(VelocitySource::movementScale), (App)FloatProviders.CODEC.optionalFieldOf("base", ConstantFloat.ZERO).forGetter(VelocitySource::base)).apply((Applicative)i, VelocitySource::new));
/*     */   }
/*     */   
/*     */   public double getVelocity(double movement, RandomSource random) {
/* 106 */     return movement * this.movementScale + this.base.sample(random);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\SpawnParticlesEffect$VelocitySource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */