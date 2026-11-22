/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum ParticleStatus {
/* 10 */   ALL(0, "options.particles.all"),
/* 11 */   DECREASED(1, "options.particles.decreased"),
/* 12 */   MINIMAL(2, "options.particles.minimal");
/*    */   
/*    */   private static final IntFunction<ParticleStatus> BY_ID;
/*    */   
/*    */   public static final Codec<ParticleStatus> LEGACY_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final Component caption;
/*    */   
/*    */   static {
/* 15 */     BY_ID = ByIdMap.continuous(s -> s.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/* 16 */     Objects.requireNonNull(BY_ID);
/* 16 */     LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, s -> Integer.valueOf(s.id));
/*    */   }
/*    */   
/*    */   ParticleStatus(int id, String key) {
/* 22 */     this.id = id;
/* 23 */     this.caption = (Component)Component.translatable(key);
/*    */   }
/*    */   
/*    */   public Component caption() {
/* 27 */     return this.caption;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ParticleStatus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */