/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class PowerParticleOption implements ParticleOptions {
/*    */   private final ParticleType<PowerParticleOption> type;
/*    */   
/*    */   private final float power;
/*    */   
/*    */   public static MapCodec<PowerParticleOption> codec(ParticleType<PowerParticleOption> type) {
/* 11 */     return Codec.FLOAT.xmap(power -> new PowerParticleOption(type, power.floatValue()), o -> Float.valueOf(o.power)).optionalFieldOf("power", create(type, 1.0F));
/*    */   }
/*    */   
/*    */   public static StreamCodec<? super ByteBuf, PowerParticleOption> streamCodec(ParticleType<PowerParticleOption> type) {
/* 15 */     return ByteBufCodecs.FLOAT.map(color -> new PowerParticleOption(type, color.floatValue()), o -> Float.valueOf(o.power));
/*    */   }
/*    */   
/*    */   private PowerParticleOption(ParticleType<PowerParticleOption> type, float power) {
/* 22 */     this.type = type;
/* 23 */     this.power = power;
/*    */   }
/*    */   
/*    */   public ParticleType<PowerParticleOption> getType() {
/* 28 */     return this.type;
/*    */   }
/*    */   
/*    */   public float getPower() {
/* 32 */     return this.power;
/*    */   }
/*    */   
/*    */   public static PowerParticleOption create(ParticleType<PowerParticleOption> type, float power) {
/* 36 */     return new PowerParticleOption(type, power);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\PowerParticleOption.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */