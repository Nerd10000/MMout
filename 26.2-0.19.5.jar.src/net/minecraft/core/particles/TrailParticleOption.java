/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class TrailParticleOption extends Record implements ParticleOptions {
/*    */   private final Vec3 target;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   private final int duration;
/*    */   
/*    */   public static final MapCodec<TrailParticleOption> CODEC;
/*    */   
/*    */   public TrailParticleOption(Vec3 target, int color, int duration) {
/* 11 */     this.target = target;
/* 11 */     this.color = color;
/* 11 */     this.duration = duration;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/particles/TrailParticleOption;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/TrailParticleOption;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/particles/TrailParticleOption;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/TrailParticleOption;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/particles/TrailParticleOption;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/particles/TrailParticleOption;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 target() {
/* 11 */     return this.target;
/*    */   }
/*    */   
/*    */   public int color() {
/* 11 */     return this.color;
/*    */   }
/*    */   
/*    */   public int duration() {
/* 11 */     return this.duration;
/*    */   }
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Vec3.CODEC.fieldOf("target").forGetter(TrailParticleOption::target), (App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(TrailParticleOption::color), (App)ExtraCodecs.POSITIVE_INT.fieldOf("duration").forGetter(TrailParticleOption::duration)).apply((Applicative)i, TrailParticleOption::new));
/*    */   }
/*    */   
/* 18 */   public static final StreamCodec<RegistryFriendlyByteBuf, TrailParticleOption> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, TrailParticleOption::target, ByteBufCodecs.INT, TrailParticleOption::color, ByteBufCodecs.VAR_INT, TrailParticleOption::duration, TrailParticleOption::new);
/*    */   
/*    */   public ParticleType<TrailParticleOption> getType() {
/* 27 */     return ParticleTypes.TRAIL;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\TrailParticleOption.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */