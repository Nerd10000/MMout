/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class SculkChargeParticleOptions extends Record implements ParticleOptions {
/*    */   private final float roll;
/*    */   
/*    */   public static final MapCodec<SculkChargeParticleOptions> CODEC;
/*    */   
/*    */   public static final StreamCodec<RegistryFriendlyByteBuf, SculkChargeParticleOptions> STREAM_CODEC;
/*    */   
/*    */   public SculkChargeParticleOptions(float roll) {
/* 10 */     this.roll = roll;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/particles/SculkChargeParticleOptions;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/SculkChargeParticleOptions;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/particles/SculkChargeParticleOptions;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/SculkChargeParticleOptions;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/particles/SculkChargeParticleOptions;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/particles/SculkChargeParticleOptions;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float roll() {
/* 10 */     return this.roll;
/*    */   }
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("roll").forGetter(())).apply((Applicative)i, SculkChargeParticleOptions::new));
/* 15 */     STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, o -> Float.valueOf(o.roll), SculkChargeParticleOptions::new);
/*    */   }
/*    */   
/*    */   public ParticleType<SculkChargeParticleOptions> getType() {
/* 22 */     return ParticleTypes.SCULK_CHARGE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\SculkChargeParticleOptions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */