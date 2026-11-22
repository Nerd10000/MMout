/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class EntryBase implements LootPoolEntry {
/*    */   public int getWeight(float luck) {
/* 62 */     return Math.max(Mth.floor(LootPoolSingletonContainer.this.weight + LootPoolSingletonContainer.this.quality * luck), 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolSingletonContainer$EntryBase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */