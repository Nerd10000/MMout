/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ComposableEntryContainer {
/*    */   public static final ComposableEntryContainer ALWAYS_FALSE = (context, output) -> false;
/*    */   
/*    */   public static final ComposableEntryContainer ALWAYS_TRUE = (context, output) -> true;
/*    */   
/*    */   default ComposableEntryContainer and(ComposableEntryContainer other) {
/* 16 */     Objects.requireNonNull(other);
/* 17 */     return (context, output) -> (expand(context, output) && other.expand(context, output));
/*    */   }
/*    */   
/*    */   default ComposableEntryContainer or(ComposableEntryContainer other) {
/* 21 */     Objects.requireNonNull(other);
/* 22 */     return (context, output) -> (expand(context, output) || other.expand(context, output));
/*    */   }
/*    */   
/*    */   boolean expand(LootContext paramLootContext, Consumer<LootPoolEntry> paramConsumer);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\ComposableEntryContainer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */