/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ 
/*    */ public interface Getter<T, R> extends LootContextArg<R> {
/*    */   R get(T paramT);
/*    */   
/*    */   ContextKey<? extends T> contextParam();
/*    */   
/*    */   default R get(LootContext context) {
/* 43 */     T value = context.getOptionalParameter((ContextKey)contextParam());
/* 44 */     return (value != null) ? get(value) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootContextArg$Getter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */