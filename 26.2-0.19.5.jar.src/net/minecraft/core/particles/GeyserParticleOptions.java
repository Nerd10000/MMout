/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class GeyserParticleOptions extends Record implements ParticleOptions {
/*    */   private final ParticleType<GeyserParticleOptions> type;
/*    */   
/*    */   private final int waterBlocks;
/*    */   
/*    */   public GeyserParticleOptions(ParticleType<GeyserParticleOptions> type, int waterBlocks) {
/* 10 */     this.type = type;
/* 10 */     this.waterBlocks = waterBlocks;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/particles/GeyserParticleOptions;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/GeyserParticleOptions;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/particles/GeyserParticleOptions;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/GeyserParticleOptions;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/particles/GeyserParticleOptions;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/particles/GeyserParticleOptions;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ParticleType<GeyserParticleOptions> type() {
/* 10 */     return this.type;
/*    */   }
/*    */   
/*    */   public int waterBlocks() {
/* 10 */     return this.waterBlocks;
/*    */   }
/*    */   
/*    */   public static MapCodec<GeyserParticleOptions> codec(ParticleType<GeyserParticleOptions> type) {
/* 12 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.POSITIVE_INT.fieldOf("water_blocks").forGetter(())).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   public static StreamCodec<? super ByteBuf, GeyserParticleOptions> streamCodec(ParticleType<GeyserParticleOptions> type) {
/* 17 */     return StreamCodec.composite(ByteBufCodecs.INT, o -> Integer.valueOf(o.waterBlocks), waterBlocks -> new GeyserParticleOptions(type, waterBlocks.intValue()));
/*    */   }
/*    */   
/*    */   public ParticleType<GeyserParticleOptions> getType() {
/* 25 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\GeyserParticleOptions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */