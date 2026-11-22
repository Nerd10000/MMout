/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GameTestEntityBuilder<E extends Entity> {
/*    */   protected final GameTestHelper testHelper;
/*    */   
/*    */   private final EntityType<E> entityType;
/*    */   
/*    */   private final Vec3 position;
/*    */   
/*    */   private EntitySpawnReason spawnReason;
/*    */   
/*    */   private Rotation rotation;
/*    */   
/*    */   private boolean requirePersistence = true;
/*    */   
/*    */   public GameTestEntityBuilder(GameTestHelper testHelper, EntityType<E> entityType, Vec3 position) {
/* 25 */     this.testHelper = testHelper;
/* 26 */     this.entityType = entityType;
/* 27 */     this.position = position;
/*    */   }
/*    */   
/*    */   public GameTestEntityBuilder<E> spawnReason(EntitySpawnReason spawnReason) {
/* 31 */     this.spawnReason = spawnReason;
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public GameTestEntityBuilder<E> rotation(Rotation rotation) {
/* 36 */     this.rotation = (rotation == null) ? null : this.testHelper.getTestRotation().getRotated(rotation);
/* 37 */     return this;
/*    */   }
/*    */   
/*    */   public GameTestEntityBuilder<E> requirePersistence(boolean requirePersistence) {
/* 41 */     this.requirePersistence = requirePersistence;
/* 42 */     return this;
/*    */   }
/*    */   
/*    */   public E spawn() {
/* 46 */     ServerLevel level = this.testHelper.getLevel();
/* 47 */     Entity entity = this.entityType.create((Level)level, EntitySpawnReason.STRUCTURE);
/* 48 */     if (entity == null)
/* 49 */       throw this.testHelper.assertionException(BlockPos.containing(this.position), "test.error.spawn_failure", new Object[] { this.entityType.builtInRegistryHolder().getRegisteredName() }); 
/* 51 */     if (this.requirePersistence && entity instanceof Mob) {
/* 51 */       Mob mob = (Mob)entity;
/* 52 */       mob.setPersistenceRequired();
/*    */     } 
/* 54 */     Vec3 absoluteVec = this.testHelper.absoluteVec(this.position);
/* 55 */     float yRot = entity.rotate((this.rotation == null) ? this.testHelper.getTestRotation() : this.rotation);
/* 57 */     entity.snapTo(absoluteVec.x, absoluteVec.y, absoluteVec.z, yRot, entity.getXRot());
/* 58 */     entity.setYBodyRot(yRot);
/* 59 */     entity.setYHeadRot(yRot);
/* 60 */     if (this.spawnReason != null && entity instanceof Mob) {
/* 60 */       Mob mob = (Mob)entity;
/* 61 */       mob.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(mob.blockPosition()), this.spawnReason, null);
/*    */     } 
/* 63 */     level.addFreshEntityWithPassengers(entity);
/* 64 */     return (E)entity;
/*    */   }
/*    */   
/*    */   public List<E> spawn(int amount) {
/* 68 */     List<E> entities = new ArrayList<>();
/* 69 */     for (int i = 0; i < amount; i++)
/* 70 */       entities.add(spawn()); 
/* 72 */     return entities;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestEntityBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */