/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.Bucketable;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.item.component.CustomData;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class MobBucketItem extends BucketItem {
/*    */   private final EntityType<? extends Mob> type;
/*    */   
/*    */   private final SoundEvent emptySound;
/*    */   
/*    */   public MobBucketItem(EntityType<? extends Mob> type, Fluid content, SoundEvent emptySound, Item.Properties properties) {
/* 28 */     super(content, properties);
/* 29 */     this.type = type;
/* 30 */     this.emptySound = emptySound;
/*    */   }
/*    */   
/*    */   public void checkExtraContent(LivingEntity user, Level level, ItemStack itemStack, BlockPos pos) {
/* 35 */     if (level instanceof ServerLevel) {
/* 35 */       ServerLevel serverLevel = (ServerLevel)level;
/* 36 */       spawn(serverLevel, itemStack, pos);
/* 37 */       level.gameEvent((Entity)user, (Holder)GameEvent.ENTITY_PLACE, pos);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void playEmptySound(LivingEntity user, LevelAccessor level, BlockPos pos) {
/* 43 */     level.playSound((Entity)user, pos, this.emptySound, SoundSource.NEUTRAL, 1.0F, 1.0F);
/*    */   }
/*    */   
/*    */   private void spawn(ServerLevel level, ItemStack itemStack, BlockPos spawnPos) {
/* 47 */     Mob mob = (Mob)this.type.create(level, EntityType.createDefaultStackConfig((Level)level, itemStack, null), spawnPos, EntitySpawnReason.BUCKET, true, false);
/* 49 */     if (mob instanceof Bucketable) {
/* 49 */       Bucketable bucketable = (Bucketable)mob;
/* 50 */       CustomData entityData = (CustomData)itemStack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
/* 51 */       bucketable.loadFromBucketTag(entityData.copyTag());
/* 52 */       bucketable.setFromBucket(true);
/*    */     } 
/* 55 */     if (mob != null) {
/* 56 */       level.addFreshEntityWithPassengers((Entity)mob);
/* 57 */       mob.playAmbientSound();
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean emptyContents(LivingEntity user, Level level, BlockPos pos, BlockHitResult hitResult) {
/* 63 */     if (this.content == Fluids.EMPTY) {
/* 67 */       playEmptySound(user, (LevelAccessor)level, pos);
/* 69 */       return true;
/*    */     } 
/* 71 */     return super.emptyContents(user, level, pos, hitResult);
/*    */   }
/*    */   
/*    */   public ClipContext.Fluid getFluidContext() {
/* 76 */     return ClipContext.Fluid.NONE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\MobBucketItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */