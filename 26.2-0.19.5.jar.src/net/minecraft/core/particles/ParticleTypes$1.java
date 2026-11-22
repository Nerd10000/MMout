/*     */ package net.minecraft.core.particles;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ class null extends ParticleType<T> {
/*     */   null(boolean overrideLimiter) {
/* 155 */     super(overrideLimiter);
/*     */   }
/*     */   
/*     */   public MapCodec<T> codec() {
/* 158 */     return codec.apply(this);
/*     */   }
/*     */   
/*     */   public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
/* 163 */     return streamCodec.apply(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\ParticleTypes$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */