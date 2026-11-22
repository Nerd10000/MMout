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
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class DustColorTransitionOptions extends ScalableParticleOptionsBase {
/*    */   public static final int SCULK_PARTICLE_COLOR = 3790560;
/*    */   
/* 14 */   public static final DustColorTransitionOptions SCULK_TO_REDSTONE = new DustColorTransitionOptions(3790560, 16711680, 1.0F);
/*    */   
/*    */   public static final MapCodec<DustColorTransitionOptions> CODEC;
/*    */   
/*    */   public static final StreamCodec<RegistryFriendlyByteBuf, DustColorTransitionOptions> STREAM_CODEC;
/*    */   
/*    */   private final int fromColor;
/*    */   
/*    */   private final int toColor;
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("from_color").forGetter(()), (App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("to_color").forGetter(()), (App)SCALE.fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale)).apply((Applicative)i, DustColorTransitionOptions::new));
/* 22 */     STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, o -> Integer.valueOf(o.fromColor), ByteBufCodecs.INT, o -> Integer.valueOf(o.toColor), ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale, DustColorTransitionOptions::new);
/*    */   }
/*    */   
/*    */   public DustColorTransitionOptions(int fromColor, int toColor, float scale) {
/* 33 */     super(scale);
/* 34 */     this.fromColor = fromColor;
/* 35 */     this.toColor = toColor;
/*    */   }
/*    */   
/*    */   public Vector3f getFromColor() {
/* 39 */     return ARGB.vector3fFromRGB24(this.fromColor);
/*    */   }
/*    */   
/*    */   public Vector3f getToColor() {
/* 43 */     return ARGB.vector3fFromRGB24(this.toColor);
/*    */   }
/*    */   
/*    */   public ParticleType<DustColorTransitionOptions> getType() {
/* 48 */     return ParticleTypes.DUST_COLOR_TRANSITION;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\DustColorTransitionOptions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */