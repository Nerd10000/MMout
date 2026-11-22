/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ExplosionParticleInfo extends Record {
/*    */   private final ParticleOptions particle;
/*    */   
/*    */   private final float scaling;
/*    */   
/*    */   private final float speed;
/*    */   
/*    */   public static final MapCodec<ExplosionParticleInfo> CODEC;
/*    */   
/*    */   public ExplosionParticleInfo(ParticleOptions particle, float scaling, float speed) {
/* 10 */     this.particle = particle;
/* 10 */     this.scaling = scaling;
/* 10 */     this.speed = speed;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/particles/ExplosionParticleInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/ExplosionParticleInfo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/particles/ExplosionParticleInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/ExplosionParticleInfo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/particles/ExplosionParticleInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/particles/ExplosionParticleInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ParticleOptions particle() {
/* 10 */     return this.particle;
/*    */   }
/*    */   
/*    */   public float scaling() {
/* 10 */     return this.scaling;
/*    */   }
/*    */   
/*    */   public float speed() {
/* 10 */     return this.speed;
/*    */   }
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ParticleTypes.CODEC.fieldOf("particle").forGetter(ExplosionParticleInfo::particle), (App)Codec.FLOAT.optionalFieldOf("scaling", Float.valueOf(1.0F)).forGetter(ExplosionParticleInfo::scaling), (App)Codec.FLOAT.optionalFieldOf("speed", Float.valueOf(1.0F)).forGetter(ExplosionParticleInfo::speed)).apply((Applicative)i, ExplosionParticleInfo::new));
/*    */   }
/*    */   
/* 17 */   public static final StreamCodec<RegistryFriendlyByteBuf, ExplosionParticleInfo> STREAM_CODEC = StreamCodec.composite(ParticleTypes.STREAM_CODEC, ExplosionParticleInfo::particle, ByteBufCodecs.FLOAT, ExplosionParticleInfo::scaling, ByteBufCodecs.FLOAT, ExplosionParticleInfo::speed, ExplosionParticleInfo::new);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\ExplosionParticleInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */