/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.CustomData;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public interface Bucketable {
/*     */   @Deprecated
/*     */   static void saveDefaultDataToBucketTag(Mob entity, ItemStack bucket) {
/*  37 */     bucket.copyFrom(DataComponents.CUSTOM_NAME, entity);
/*  38 */     CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucket, tag -> {
/*     */           if (entity.isNoAi())
/*     */             tag.putBoolean("NoAI", entity.isNoAi()); 
/*     */           if (entity.isSilent())
/*     */             tag.putBoolean("Silent", entity.isSilent()); 
/*     */           if (entity.isNoGravity())
/*     */             tag.putBoolean("NoGravity", entity.isNoGravity()); 
/*     */           if (entity.hasGlowingTag())
/*     */             tag.putBoolean("Glowing", entity.hasGlowingTag()); 
/*     */           if (entity.isInvulnerable())
/*     */             tag.putBoolean("Invulnerable", entity.isInvulnerable()); 
/*     */           if (entity.isPersistenceRequired())
/*     */             tag.putBoolean("PersistenceRequired", entity.isPersistenceRequired()); 
/*     */           tag.putFloat("Health", entity.getHealth());
/*     */         });
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   static void loadDefaultDataFromBucketTag(Mob entity, CompoundTag tag) {
/*  65 */     Objects.requireNonNull(entity);
/*  65 */     tag.getBoolean("NoAI").ifPresent(entity::setNoAi);
/*  66 */     Objects.requireNonNull(entity);
/*  66 */     tag.getBoolean("Silent").ifPresent(entity::setSilent);
/*  67 */     Objects.requireNonNull(entity);
/*  67 */     tag.getBoolean("NoGravity").ifPresent(entity::setNoGravity);
/*  68 */     Objects.requireNonNull(entity);
/*  68 */     tag.getBoolean("Glowing").ifPresent(entity::setGlowingTag);
/*  69 */     Objects.requireNonNull(entity);
/*  69 */     tag.getBoolean("Invulnerable").ifPresent(entity::setInvulnerable);
/*  70 */     tag.getBoolean("PersistenceRequired").ifPresent(required -> {
/*     */           if (required.booleanValue())
/*     */             entity.setPersistenceRequired(); 
/*     */         });
/*  71 */     Objects.requireNonNull(entity);
/*  71 */     tag.getFloat("Health").ifPresent(entity::setHealth);
/*     */   }
/*     */   
/*     */   static <T extends LivingEntity & Bucketable> Optional<InteractionResult> bucketMobPickup(Player player, InteractionHand hand, T pickupEntity) {
/*  75 */     ItemStack itemStack = player.getItemInHand(hand);
/*  77 */     if (((Bucketable)pickupEntity).canBePickedUpWithBucket(itemStack) && pickupEntity.isAlive()) {
/*  78 */       pickupEntity.playSound(((Bucketable)pickupEntity).getPickupSound(), 1.0F, 1.0F);
/*  80 */       ItemStack bucket = ((Bucketable)pickupEntity).getBucketItemStack();
/*  81 */       ((Bucketable)pickupEntity).saveToBucketTag(bucket);
/*  83 */       ItemStack result = ItemUtils.createFilledResult(itemStack, player, bucket, false);
/*  84 */       player.setItemInHand(hand, result);
/*  86 */       Level level = pickupEntity.level();
/*  88 */       if (!level.isClientSide() && player instanceof ServerPlayer) {
/*  88 */         ServerPlayer serverPlayer = (ServerPlayer)player;
/*  89 */         CriteriaTriggers.FILLED_BUCKET.trigger(serverPlayer, bucket);
/*     */       } 
/*  91 */       if (pickupEntity instanceof Leashable) {
/*  91 */         Leashable leashable = (Leashable)pickupEntity;
/*  92 */         leashable.dropLeash();
/*     */       } 
/*  94 */       pickupEntity.discard();
/*  96 */       return (Optional)Optional.of(InteractionResult.SUCCESS);
/*     */     } 
/*  98 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   default boolean canBePickedUpWithBucket(ItemStack itemStack) {
/* 102 */     return (itemStack.getItem() == Items.WATER_BUCKET);
/*     */   }
/*     */   
/*     */   boolean fromBucket();
/*     */   
/*     */   void setFromBucket(boolean paramBoolean);
/*     */   
/*     */   void saveToBucketTag(ItemStack paramItemStack);
/*     */   
/*     */   void loadFromBucketTag(CompoundTag paramCompoundTag);
/*     */   
/*     */   ItemStack getBucketItemStack();
/*     */   
/*     */   SoundEvent getPickupSound();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Bucketable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */