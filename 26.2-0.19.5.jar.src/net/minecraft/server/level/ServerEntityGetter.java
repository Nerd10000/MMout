/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.EntityGetter;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public interface ServerEntityGetter extends EntityGetter {
/*     */   default Player getNearestPlayer(TargetingConditions targetConditions, LivingEntity source) {
/*  20 */     return getNearestEntity(players(), targetConditions, source, source.getX(), source.getY(), source.getZ());
/*     */   }
/*     */   
/*     */   default Player getNearestPlayer(TargetingConditions targetConditions, LivingEntity source, double x, double y, double z) {
/*  24 */     return getNearestEntity(players(), targetConditions, source, x, y, z);
/*     */   }
/*     */   
/*     */   default Player getNearestPlayer(TargetingConditions targetConditions, double x, double y, double z) {
/*  28 */     return getNearestEntity(players(), targetConditions, (LivingEntity)null, x, y, z);
/*     */   }
/*     */   
/*     */   default <T extends LivingEntity> T getNearestEntity(Class<? extends T> type, TargetingConditions targetConditions, LivingEntity source, double x, double y, double z, AABB bb) {
/*  32 */     return getNearestEntity(getEntitiesOfClass(type, bb, entity -> true), targetConditions, source, x, y, z);
/*     */   }
/*     */   
/*     */   default LivingEntity getNearestEntity(TagKey<EntityType<?>> tag, TargetingConditions targetConditions, LivingEntity source, double x, double y, double z, AABB bb) {
/*  36 */     double bestDistance = Double.MAX_VALUE;
/*  37 */     LivingEntity nearestEntity = null;
/*  38 */     for (LivingEntity entity : getEntitiesOfClass(LivingEntity.class, bb, e -> e.is(tag))) {
/*  39 */       if (!targetConditions.test(getLevel(), source, entity))
/*     */         continue; 
/*  43 */       double distance = entity.distanceToSqr(x, y, z);
/*  44 */       if (distance < bestDistance) {
/*  45 */         bestDistance = distance;
/*  46 */         nearestEntity = entity;
/*     */       } 
/*     */     } 
/*  49 */     return nearestEntity;
/*     */   }
/*     */   
/*     */   default <T extends Entity> T getNearestEntity(List<? extends T> entities, double x, double y, double z) {
/*     */     Entity entity;
/*  53 */     double best = -1.0D;
/*  54 */     T result = null;
/*  55 */     for (Entity entity1 : entities) {
/*  56 */       double dist = entity1.distanceToSqr(x, y, z);
/*  57 */       if (best == -1.0D || dist < best) {
/*  58 */         best = dist;
/*  59 */         entity = entity1;
/*     */       } 
/*     */     } 
/*  62 */     return (T)entity;
/*     */   }
/*     */   
/*     */   default <T extends LivingEntity> T getNearestEntity(List<? extends T> entities, TargetingConditions targetConditions, LivingEntity source, double x, double y, double z) {
/*     */     LivingEntity livingEntity;
/*  66 */     double best = -1.0D;
/*  67 */     T result = null;
/*  68 */     for (LivingEntity livingEntity1 : entities) {
/*  69 */       if (!targetConditions.test(getLevel(), source, livingEntity1))
/*     */         continue; 
/*  73 */       double dist = livingEntity1.distanceToSqr(x, y, z);
/*  74 */       if (best == -1.0D || dist < best) {
/*  75 */         best = dist;
/*  76 */         livingEntity = livingEntity1;
/*     */       } 
/*     */     } 
/*  80 */     return (T)livingEntity;
/*     */   }
/*     */   
/*     */   default List<Player> getNearbyPlayers(TargetingConditions targetConditions, LivingEntity source, AABB bb) {
/*  84 */     List<Player> foundPlayers = new ArrayList<>();
/*  85 */     for (Player player : players()) {
/*  86 */       if (bb.contains(player.getX(), player.getY(), player.getZ()) && targetConditions.test(getLevel(), source, (LivingEntity)player))
/*  87 */         foundPlayers.add(player); 
/*     */     } 
/*  91 */     return foundPlayers;
/*     */   }
/*     */   
/*     */   default <T extends LivingEntity> List<T> getNearbyEntities(Class<T> type, TargetingConditions targetConditions, LivingEntity source, AABB bb) {
/*  95 */     List<T> nearby = getEntitiesOfClass(type, bb, entity -> true);
/*  96 */     List<T> entities = new ArrayList<>();
/*  98 */     for (LivingEntity livingEntity : nearby) {
/*  99 */       if (targetConditions.test(getLevel(), source, livingEntity))
/* 100 */         entities.add((T)livingEntity); 
/*     */     } 
/* 104 */     return entities;
/*     */   }
/*     */   
/*     */   ServerLevel getLevel();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerEntityGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */