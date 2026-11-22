/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ 
/*    */ public interface SimpleGetter<T> extends LootContextArg<T> {
/*    */   ContextKey<? extends T> contextParam();
/*    */   
/*    */   default T get(LootContext context) {
/* 54 */     return context.getOptionalParameter((ContextKey)contextParam());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootContextArg$SimpleGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */