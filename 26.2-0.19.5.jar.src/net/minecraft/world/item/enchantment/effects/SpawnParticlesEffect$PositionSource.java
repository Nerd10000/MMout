/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public final class PositionSource extends Record {
/*    */   private final SpawnParticlesEffect.PositionSourceType type;
/*    */   
/*    */   private final float offset;
/*    */   
/*    */   private final float scale;
/*    */   
/*    */   public static final MapCodec<PositionSource> CODEC;
/*    */   
/*    */   public PositionSource(SpawnParticlesEffect.PositionSourceType type, float offset, float scale) {
/* 67 */     this.type = type;
/* 67 */     this.offset = offset;
/* 67 */     this.scale = scale;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #67	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #67	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #67	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public SpawnParticlesEffect.PositionSourceType type() {
/* 67 */     return this.type;
/*    */   }
/*    */   
/*    */   public float offset() {
/* 67 */     return this.offset;
/*    */   }
/*    */   
/*    */   public float scale() {
/* 67 */     return this.scale;
/*    */   }
/*    */   
/*    */   static {
/* 76 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SpawnParticlesEffect.PositionSourceType.CODEC.fieldOf("type").forGetter(PositionSource::type), (App)Codec.FLOAT.optionalFieldOf("offset", Float.valueOf(0.0F)).forGetter(PositionSource::offset), (App)ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("scale", Float.valueOf(1.0F)).forGetter(PositionSource::scale)).apply((Applicative)i, PositionSource::new)).validate(positioning -> 
/* 77 */         (positioning.type() == SpawnParticlesEffect.PositionSourceType.ENTITY_POSITION && positioning.scale() != 1.0F) ? DataResult.error(()) : DataResult.success(positioning));
/*    */   }
/*    */   
/*    */   public double getCoordinate(double position, double center, float boundingBoxSpan, RandomSource random) {
/* 84 */     return this.type.getCoordinate(position, center, boundingBoxSpan * this.scale, random) + this.offset;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\SpawnParticlesEffect$PositionSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */