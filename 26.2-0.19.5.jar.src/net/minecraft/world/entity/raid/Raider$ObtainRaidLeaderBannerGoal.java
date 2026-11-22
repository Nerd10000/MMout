/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ public class ObtainRaidLeaderBannerGoal<T extends Raider> extends Goal {
/*     */   private final T mob;
/*     */   
/* 290 */   private Int2LongOpenHashMap unreachableBannerCache = new Int2LongOpenHashMap();
/*     */   
/*     */   private Path pathToBanner;
/*     */   
/*     */   private ItemEntity pursuedBannerItemEntity;
/*     */   
/*     */   public ObtainRaidLeaderBannerGoal(T mob) {
/* 295 */     this.mob = mob;
/* 296 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 301 */     if (cannotPickUpBanner())
/* 302 */       return false; 
/* 305 */     Int2LongOpenHashMap tempCache = new Int2LongOpenHashMap();
/* 306 */     double followRange = Raider.this.getAttributeValue(Attributes.FOLLOW_RANGE);
/* 307 */     List<ItemEntity> items = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(followRange, 8.0D, followRange), Raider.ALLOWED_ITEMS);
/* 309 */     for (ItemEntity banner : items) {
/* 310 */       long unreachableUntilTime = this.unreachableBannerCache.getOrDefault(banner.getId(), Long.MIN_VALUE);
/* 311 */       if (Raider.this.level().getGameTime() < unreachableUntilTime) {
/* 312 */         tempCache.put(banner.getId(), unreachableUntilTime);
/*     */         continue;
/*     */       } 
/* 316 */       Path path = this.mob.getNavigation().createPath((Entity)banner, 1);
/* 317 */       if (path != null && path.canReach()) {
/* 318 */         this.pathToBanner = path;
/* 319 */         this.pursuedBannerItemEntity = banner;
/* 320 */         return true;
/*     */       } 
/* 322 */       tempCache.put(banner.getId(), Raider.this.level().getGameTime() + 600L);
/*     */     } 
/* 326 */     this.unreachableBannerCache = tempCache;
/* 328 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 333 */     if (this.pursuedBannerItemEntity == null || this.pathToBanner == null)
/* 334 */       return false; 
/* 336 */     if (this.pursuedBannerItemEntity.isRemoved())
/* 337 */       return false; 
/* 339 */     if (this.pathToBanner.isDone())
/* 340 */       return false; 
/* 342 */     if (cannotPickUpBanner())
/* 343 */       return false; 
/* 345 */     return true;
/*     */   }
/*     */   
/*     */   private boolean cannotPickUpBanner() {
/* 349 */     if (!this.mob.hasActiveRaid())
/* 350 */       return true; 
/* 352 */     if (this.mob.getCurrentRaid().isOver())
/* 353 */       return true; 
/* 355 */     if (!this.mob.canBeLeader())
/* 356 */       return true; 
/* 358 */     if (ItemStack.matches(this.mob.getItemBySlot(EquipmentSlot.HEAD), Raid.getOminousBannerInstance((HolderGetter<BannerPattern>)this.mob.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN))))
/* 359 */       return true; 
/* 361 */     Raider leader = Raider.this.raid.getLeader(this.mob.getWave());
/* 362 */     if (leader != null && leader.isAlive())
/* 363 */       return true; 
/* 365 */     return false;
/*     */   }
/*     */   
/*     */   public void start() {
/* 370 */     this.mob.getNavigation().moveTo(this.pathToBanner, 1.149999976158142D);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 375 */     this.pathToBanner = null;
/* 376 */     this.pursuedBannerItemEntity = null;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 381 */     if (this.pursuedBannerItemEntity != null && this.pursuedBannerItemEntity.closerThan((Entity)this.mob, 1.414D))
/* 382 */       this.mob.pickUpItem(getServerLevel(Raider.this.level()), this.pursuedBannerItemEntity); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\raid\Raider$ObtainRaidLeaderBannerGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */