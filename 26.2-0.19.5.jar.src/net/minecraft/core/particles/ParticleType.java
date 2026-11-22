/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public abstract class ParticleType<T extends ParticleOptions> {
/*    */   private final boolean overrideLimiter;
/*    */   
/*    */   protected ParticleType(boolean overrideLimiter) {
/* 11 */     this.overrideLimiter = overrideLimiter;
/*    */   }
/*    */   
/*    */   public boolean getOverrideLimiter() {
/* 15 */     return this.overrideLimiter;
/*    */   }
/*    */   
/*    */   public abstract MapCodec<T> codec();
/*    */   
/*    */   public abstract StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\ParticleType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */