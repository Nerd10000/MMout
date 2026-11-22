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
/*    */ public class ShriekParticleOption implements ParticleOptions {
/*    */   public static final MapCodec<ShriekParticleOption> CODEC;
/*    */   
/*    */   public static final StreamCodec<RegistryFriendlyByteBuf, ShriekParticleOption> STREAM_CODEC;
/*    */   
/*    */   private final int delay;
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("delay").forGetter(())).apply((Applicative)i, ShriekParticleOption::new));
/* 15 */     STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, o -> Integer.valueOf(o.delay), ShriekParticleOption::new);
/*    */   }
/*    */   
/*    */   public ShriekParticleOption(int delay) {
/* 23 */     this.delay = delay;
/*    */   }
/*    */   
/*    */   public ParticleType<ShriekParticleOption> getType() {
/* 28 */     return ParticleTypes.SHRIEK;
/*    */   }
/*    */   
/*    */   public int getDelay() {
/* 32 */     return this.delay;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\ShriekParticleOption.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */