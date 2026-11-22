/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ 
/*    */ class MobCounts {
/* 48 */   private final Object2IntMap<MobCategory> counts = (Object2IntMap<MobCategory>)new Object2IntOpenHashMap((MobCategory.values()).length);
/*    */   
/*    */   public void add(MobCategory category) {
/* 51 */     this.counts.computeInt(category, (k, count) -> Integer.valueOf((count == null) ? 1 : (count.intValue() + 1)));
/*    */   }
/*    */   
/*    */   public boolean canSpawn(MobCategory category) {
/* 55 */     return (this.counts.getOrDefault(category, 0) < category.getMaxInstancesPerChunk());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\LocalMobCapCalculator$MobCounts.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */