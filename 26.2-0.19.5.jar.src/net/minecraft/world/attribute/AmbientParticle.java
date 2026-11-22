/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public final class AmbientParticle extends Record {
/*    */   private final ParticleOptions particle;
/*    */   
/*    */   private final float probability;
/*    */   
/*    */   public static final Codec<AmbientParticle> CODEC;
/*    */   
/*    */   public AmbientParticle(ParticleOptions particle, float probability) {
/* 11 */     this.particle = particle;
/* 11 */     this.probability = probability;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/AmbientParticle;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientParticle;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AmbientParticle;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientParticle;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AmbientParticle;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/AmbientParticle;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ParticleOptions particle() {
/* 11 */     return this.particle;
/*    */   }
/*    */   
/*    */   public float probability() {
/* 11 */     return this.probability;
/*    */   }
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ParticleTypes.CODEC.fieldOf("particle").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(())).apply((Applicative)i, AmbientParticle::new));
/*    */   }
/*    */   
/*    */   public boolean canSpawn(RandomSource random) {
/* 18 */     return (random.nextFloat() <= this.probability);
/*    */   }
/*    */   
/*    */   public static List<AmbientParticle> of(ParticleOptions particle, float probability) {
/* 22 */     return List.of(new AmbientParticle(particle, probability));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\AmbientParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */