/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class DustParticleOptions extends ScalableParticleOptionsBase {
/*    */   public static final int REDSTONE_PARTICLE_COLOR = 16711680;
/*    */   
/* 14 */   public static final DustParticleOptions REDSTONE = new DustParticleOptions(16711680, 1.0F);
/*    */   
/*    */   public static final MapCodec<DustParticleOptions> CODEC;
/*    */   
/*    */   public static final StreamCodec<RegistryFriendlyByteBuf, DustParticleOptions> STREAM_CODEC;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(()), (App)SCALE.fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale)).apply((Applicative)i, DustParticleOptions::new));
/* 21 */     STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, o -> Integer.valueOf(o.color), ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale, DustParticleOptions::new);
/*    */   }
/*    */   
/*    */   public DustParticleOptions(int color, float scale) {
/* 30 */     super(scale);
/* 31 */     this.color = color;
/*    */   }
/*    */   
/*    */   public ParticleType<DustParticleOptions> getType() {
/* 36 */     return ParticleTypes.DUST;
/*    */   }
/*    */   
/*    */   public Vector3f getColor() {
/* 40 */     return ARGB.vector3fFromRGB24(this.color);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\DustParticleOptions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */