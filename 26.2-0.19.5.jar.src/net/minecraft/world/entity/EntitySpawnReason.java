/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ public enum EntitySpawnReason {
/*  4 */   NATURAL, CHUNK_GENERATION, SPAWNER, STRUCTURE, BREEDING, MOB_SUMMONED, JOCKEY, EVENT, CONVERSION, REINFORCEMENT, TRIGGERED, BUCKET, SPAWN_ITEM_USE, COMMAND, DISPENSER, PATROL, TRIAL_SPAWNER, LOAD, DIMENSION_TRAVEL;
/*    */   
/*    */   public static boolean isSpawner(EntitySpawnReason reason) {
/* 26 */     return (reason == SPAWNER || reason == TRIAL_SPAWNER);
/*    */   }
/*    */   
/*    */   public static boolean ignoresLightRequirements(EntitySpawnReason reason) {
/* 30 */     return (reason == TRIAL_SPAWNER);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntitySpawnReason.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */