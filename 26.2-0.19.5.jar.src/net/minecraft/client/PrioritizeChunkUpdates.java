/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum PrioritizeChunkUpdates {
/* 10 */   NONE(0, "options.prioritizeChunkUpdates.none"),
/* 11 */   PLAYER_AFFECTED(1, "options.prioritizeChunkUpdates.byPlayer"),
/* 12 */   NEARBY(2, "options.prioritizeChunkUpdates.nearby");
/*    */   
/*    */   private static final IntFunction<PrioritizeChunkUpdates> BY_ID;
/*    */   
/*    */   public static final Codec<PrioritizeChunkUpdates> LEGACY_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final Component caption;
/*    */   
/*    */   static {
/* 15 */     BY_ID = ByIdMap.continuous(p -> p.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/* 16 */     Objects.requireNonNull(BY_ID);
/* 16 */     LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, p -> Integer.valueOf(p.id));
/*    */   }
/*    */   
/*    */   PrioritizeChunkUpdates(int id, String key) {
/* 22 */     this.id = id;
/* 23 */     this.caption = (Component)Component.translatable(key);
/*    */   }
/*    */   
/*    */   public Component caption() {
/* 27 */     return this.caption;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\PrioritizeChunkUpdates.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */