/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class GeyserBaseParticleOptions extends Record implements ParticleOptions {
/*    */   private final ParticleType<GeyserBaseParticleOptions> type;
/*    */   
/*    */   private final int waterBlocks;
/*    */   
/*    */   private final float burstImpulseBase;
/*    */   
/*    */   public GeyserBaseParticleOptions(ParticleType<GeyserBaseParticleOptions> type, int waterBlocks, float burstImpulseBase) {
/* 11 */     this.type = type;
/* 11 */     this.waterBlocks = waterBlocks;
/* 11 */     this.burstImpulseBase = burstImpulseBase;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/particles/GeyserBaseParticleOptions;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/GeyserBaseParticleOptions;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/particles/GeyserBaseParticleOptions;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/GeyserBaseParticleOptions;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/particles/GeyserBaseParticleOptions;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/particles/GeyserBaseParticleOptions;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ParticleType<GeyserBaseParticleOptions> type() {
/* 11 */     return this.type;
/*    */   }
/*    */   
/*    */   public int waterBlocks() {
/* 11 */     return this.waterBlocks;
/*    */   }
/*    */   
/*    */   public float burstImpulseBase() {
/* 11 */     return this.burstImpulseBase;
/*    */   }
/*    */   
/*    */   public static MapCodec<GeyserBaseParticleOptions> codec(ParticleType<GeyserBaseParticleOptions> type) {
/* 14 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.POSITIVE_INT.fieldOf("water_blocks").forGetter(()), (App)Codec.FLOAT.fieldOf("burst_impulse_base").forGetter(())).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   public static StreamCodec<? super ByteBuf, GeyserBaseParticleOptions> streamCodec(ParticleType<GeyserBaseParticleOptions> type) {
/* 21 */     return StreamCodec.composite(ByteBufCodecs.INT, o -> Integer.valueOf(o.waterBlocks), ByteBufCodecs.FLOAT, o -> Float.valueOf(o.burstImpulseBase), (waterBlocks, burstImpulseBase) -> new GeyserBaseParticleOptions(type, waterBlocks.intValue(), burstImpulseBase.floatValue()));
/*    */   }
/*    */   
/*    */   public ParticleType<GeyserBaseParticleOptions> getType() {
/* 30 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\GeyserBaseParticleOptions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */