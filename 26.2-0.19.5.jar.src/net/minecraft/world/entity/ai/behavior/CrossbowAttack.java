/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.monster.CrossbowAttackMob;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.ChargedProjectiles;
/*     */ 
/*     */ public class CrossbowAttack<E extends Mob & CrossbowAttackMob, T extends LivingEntity> extends Behavior<E> {
/*     */   private static final int TIMEOUT = 1200;
/*     */   
/*     */   private int attackDelay;
/*     */   
/*     */   private enum CrossbowState {
/*  24 */     UNCHARGED, CHARGING, CHARGED, READY_TO_ATTACK;
/*     */   }
/*     */   
/*  31 */   private CrossbowState crossbowState = CrossbowState.UNCHARGED;
/*     */   
/*     */   public CrossbowAttack() {
/*  34 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, E body) {
/*  42 */     LivingEntity attackTarget = getAttackTarget((LivingEntity)body);
/*  43 */     return (body.isHolding(Items.CROSSBOW) && BehaviorUtils.canSee((LivingEntity)body, attackTarget) && BehaviorUtils.isWithinAttackRange((Mob)body, attackTarget, 0));
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, E body, long timestamp) {
/*  48 */     return (body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && checkExtraStartConditions(level, body));
/*     */   }
/*     */   
/*     */   protected void tick(ServerLevel level, E body, long timestamp) {
/*  54 */     LivingEntity target = getAttackTarget((LivingEntity)body);
/*  55 */     lookAtTarget((Mob)body, target);
/*  56 */     crossbowAttack(body, target);
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, E body, long timestamp) {
/*  61 */     if (body.isUsingItem())
/*  62 */       body.stopUsingItem(); 
/*  64 */     if (body.isHolding(Items.CROSSBOW)) {
/*  65 */       ((CrossbowAttackMob)body).setChargingCrossbow(false);
/*  66 */       body.getUseItem().set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void crossbowAttack(E body, LivingEntity target) {
/*  72 */     if (this.crossbowState == CrossbowState.UNCHARGED) {
/*  73 */       body.startUsingItem(ProjectileUtil.getWeaponHoldingHand((LivingEntity)body, Items.CROSSBOW));
/*  74 */       this.crossbowState = CrossbowState.CHARGING;
/*  75 */       ((CrossbowAttackMob)body).setChargingCrossbow(true);
/*  76 */     } else if (this.crossbowState == CrossbowState.CHARGING) {
/*  77 */       if (!body.isUsingItem())
/*  78 */         this.crossbowState = CrossbowState.UNCHARGED; 
/*  80 */       int pullTime = body.getTicksUsingItem();
/*  81 */       ItemStack useItem = body.getUseItem();
/*  82 */       if (pullTime >= CrossbowItem.getChargeDuration(useItem, (LivingEntity)body)) {
/*  83 */         body.releaseUsingItem();
/*  84 */         this.crossbowState = CrossbowState.CHARGED;
/*  85 */         this.attackDelay = 20 + body.getRandom().nextInt(20);
/*  86 */         ((CrossbowAttackMob)body).setChargingCrossbow(false);
/*     */       } 
/*  88 */     } else if (this.crossbowState == CrossbowState.CHARGED) {
/*  89 */       this.attackDelay--;
/*  90 */       if (this.attackDelay == 0)
/*  91 */         this.crossbowState = CrossbowState.READY_TO_ATTACK; 
/*  93 */     } else if (this.crossbowState == CrossbowState.READY_TO_ATTACK) {
/*  94 */       ((RangedAttackMob)body).performRangedAttack(target, 1.0F);
/*  95 */       this.crossbowState = CrossbowState.UNCHARGED;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void lookAtTarget(Mob body, LivingEntity target) {
/* 100 */     body.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)target, true));
/*     */   }
/*     */   
/*     */   private static LivingEntity getAttackTarget(LivingEntity body) {
/* 104 */     return body.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\CrossbowAttack.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */