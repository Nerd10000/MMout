/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ 
/*    */ public interface StructureSpawner {
/*    */   public static final StructureSpawner IN_PLACE;
/*    */   
/*    */   default void onBatchStart(ServerLevel level) {}
/*    */   
/*    */   static {
/* 46 */     IN_PLACE = (testInfo -> Optional.<GameTestInfo>ofNullable(testInfo.prepareTestStructure()).map(()));
/*    */   }
/*    */   
/*    */   public static final StructureSpawner NOT_SET = testInfo -> Optional.empty();
/*    */   
/*    */   Optional<GameTestInfo> spawnStructure(GameTestInfo paramGameTestInfo);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestRunner$StructureSpawner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */