/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum ChatVisiblity {
/* 10 */   FULL(0, "options.chat.visibility.full"),
/* 11 */   SYSTEM(1, "options.chat.visibility.system"),
/* 12 */   HIDDEN(2, "options.chat.visibility.hidden");
/*    */   
/*    */   private static final IntFunction<ChatVisiblity> BY_ID;
/*    */   
/*    */   public static final Codec<ChatVisiblity> LEGACY_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final Component caption;
/*    */   
/*    */   static {
/* 14 */     BY_ID = ByIdMap.continuous(v -> v.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/* 15 */     Objects.requireNonNull(BY_ID);
/* 15 */     LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, v -> Integer.valueOf(v.id));
/*    */   }
/*    */   
/*    */   ChatVisiblity(int id, String key) {
/* 21 */     this.id = id;
/* 22 */     this.caption = (Component)Component.translatable(key);
/*    */   }
/*    */   
/*    */   public Component caption() {
/* 26 */     return this.caption;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\player\ChatVisiblity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */