/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import net.minecraft.util.context.ContextKeySet;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ContextGetter<T> {
/*    */   ContextKeySet context(T paramT);
/*    */   
/*    */   static <T> ContextGetter<T> constant(ContextKeySet v) {
/* 45 */     return value -> v;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootDataType$ContextGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */