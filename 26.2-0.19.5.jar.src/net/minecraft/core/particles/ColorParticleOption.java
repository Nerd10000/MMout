/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class ColorParticleOption implements ParticleOptions {
/*    */   private final ParticleType<ColorParticleOption> type;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   public static MapCodec<ColorParticleOption> codec(ParticleType<ColorParticleOption> type) {
/* 13 */     return ExtraCodecs.ARGB_COLOR_CODEC.xmap(color -> new ColorParticleOption(type, color.intValue()), o -> Integer.valueOf(o.color)).fieldOf("color");
/*    */   }
/*    */   
/*    */   public static StreamCodec<? super ByteBuf, ColorParticleOption> streamCodec(ParticleType<ColorParticleOption> type) {
/* 17 */     return ByteBufCodecs.INT.map(color -> new ColorParticleOption(type, color.intValue()), o -> Integer.valueOf(o.color));
/*    */   }
/*    */   
/*    */   private ColorParticleOption(ParticleType<ColorParticleOption> type, int color) {
/* 24 */     this.type = type;
/* 25 */     this.color = color;
/*    */   }
/*    */   
/*    */   public ParticleType<ColorParticleOption> getType() {
/* 30 */     return this.type;
/*    */   }
/*    */   
/*    */   public float getRed() {
/* 34 */     return ARGB.red(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public float getGreen() {
/* 38 */     return ARGB.green(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public float getBlue() {
/* 42 */     return ARGB.blue(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public float getAlpha() {
/* 46 */     return ARGB.alpha(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public static ColorParticleOption create(ParticleType<ColorParticleOption> type, int color) {
/* 50 */     return new ColorParticleOption(type, color);
/*    */   }
/*    */   
/*    */   public static ColorParticleOption create(ParticleType<ColorParticleOption> type, float red, float green, float blue) {
/* 54 */     return create(type, ARGB.colorFromFloat(1.0F, red, green, blue));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\ColorParticleOption.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */