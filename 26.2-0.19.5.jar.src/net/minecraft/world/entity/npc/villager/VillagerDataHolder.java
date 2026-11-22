/*    */ package net.minecraft.world.entity.npc.villager;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ 
/*    */ public interface VillagerDataHolder {
/*    */   VillagerData getVillagerData();
/*    */   
/*    */   void setVillagerData(VillagerData paramVillagerData);
/*    */   
/*    */   boolean getVillagerDataFinalized();
/*    */   
/*    */   void setVillagerDataFinalized(boolean paramBoolean);
/*    */   
/*    */   default void finalizeVillagerType(ServerLevelAccessor level, BlockPos pos) {
/* 19 */     if (!getVillagerDataFinalized()) {
/* 20 */       setVillagerData(getVillagerData().withType((HolderGetter.Provider)level.registryAccess(), VillagerType.byBiome(level.getBiome(pos))));
/* 21 */       setVillagerDataFinalized(true);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\npc\villager\VillagerDataHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */