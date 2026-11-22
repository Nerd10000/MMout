/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public interface WorldGenLevel extends ServerLevelAccessor {
/*    */   long getSeed();
/*    */   
/*    */   default boolean ensureCanWrite(BlockPos pos) {
/* 12 */     return true;
/*    */   }
/*    */   
/*    */   default void setCurrentlyGenerating(Supplier<String> currentlyGenerating) {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\WorldGenLevel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */