/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public interface NeutralMob {
/*     */   public static final String TAG_ANGER_END_TIME = "anger_end_time";
/*     */   
/*     */   public static final String TAG_ANGRY_AT = "angry_at";
/*     */   
/*     */   public static final long NO_ANGER_END_TIME = -1L;
/*     */   
/*     */   long getPersistentAngerEndTime();
/*     */   
/*     */   default void setTimeToRemainAngry(long remainingTime) {
/*  38 */     setPersistentAngerEndTime(level().getGameTime() + remainingTime);
/*     */   }
/*     */   
/*     */   void setPersistentAngerEndTime(long paramLong);
/*     */   
/*     */   EntityReference<LivingEntity> getPersistentAngerTarget();
/*     */   
/*     */   void setPersistentAngerTarget(EntityReference<LivingEntity> paramEntityReference);
/*     */   
/*     */   void startPersistentAngerTimer();
/*     */   
/*     */   Level level();
/*     */   
/*     */   default void addPersistentAngerSaveData(ValueOutput output) {
/*  56 */     output.putLong("anger_end_time", getPersistentAngerEndTime());
/*  57 */     output.storeNullable("angry_at", EntityReference.codec(), getPersistentAngerTarget());
/*     */   }
/*     */   
/*     */   default void readPersistentAngerSaveData(Level level, ValueInput input) {
/*  62 */     Optional<Long> endTime = input.getLong("anger_end_time");
/*  63 */     if (endTime.isPresent()) {
/*  64 */       setPersistentAngerEndTime(((Long)endTime.get()).longValue());
/*     */     } else {
/*  68 */       Optional<Integer> angerTime = input.getInt("AngerTime");
/*  69 */       if (angerTime.isPresent()) {
/*  70 */         setTimeToRemainAngry(((Integer)angerTime.get()).intValue());
/*     */       } else {
/*  72 */         setPersistentAngerEndTime(-1L);
/*     */       } 
/*     */     } 
/*  76 */     if (!(level instanceof ServerLevel))
/*     */       return; 
/*  80 */     setPersistentAngerTarget(EntityReference.read(input, "angry_at"));
/*  81 */     setTarget(EntityReference.getLivingEntity(getPersistentAngerTarget(), level));
/*     */   }
/*     */   
/*     */   default void updatePersistentAnger(ServerLevel level, boolean stayAngryIfTargetPresent) {
/*  86 */     LivingEntity previousTarget = getTargetUnchecked();
/*  88 */     EntityReference<LivingEntity> persistentAngerTarget = getPersistentAngerTarget();
/*  90 */     if (previousTarget != null && previousTarget.isDeadOrDying() && persistentAngerTarget != null && persistentAngerTarget.matches(previousTarget) && previousTarget instanceof Mob) {
/*  94 */       stopBeingAngry();
/*     */       return;
/*     */     } 
/*  98 */     LivingEntity target = getTarget();
/*  99 */     if (target != null) {
/* 100 */       boolean newTarget = (persistentAngerTarget == null || !persistentAngerTarget.matches(target));
/* 101 */       if (newTarget)
/* 103 */         setPersistentAngerTarget(EntityReference.of(target)); 
/* 105 */       if (newTarget || stayAngryIfTargetPresent)
/* 107 */         startPersistentAngerTimer(); 
/*     */     } 
/* 111 */     if (persistentAngerTarget != null && !isAngry() && (
/* 112 */       target == null || !isValidPlayerTarget(target) || !stayAngryIfTargetPresent))
/* 113 */       stopBeingAngry(); 
/* 117 */     LivingEntity persistentTarget = EntityReference.getLivingEntity(persistentAngerTarget, (Level)level);
/* 118 */     if (persistentTarget instanceof Player) {
/* 118 */       Player player = (Player)persistentTarget;
/* 118 */       if (player.isCreative() || player.isSpectator() || level.getDifficulty() == Difficulty.PEACEFUL)
/* 119 */         stopBeingAngry(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isValidPlayerTarget(LivingEntity target) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: instanceof net/minecraft/world/entity/player/Player
/*     */     //   4: ifeq -> 43
/*     */     //   7: aload_0
/*     */     //   8: checkcast net/minecraft/world/entity/player/Player
/*     */     //   11: astore_1
/*     */     //   12: aload_1
/*     */     //   13: invokevirtual isCreative : ()Z
/*     */     //   16: ifne -> 43
/*     */     //   19: aload_1
/*     */     //   20: invokevirtual isSpectator : ()Z
/*     */     //   23: ifne -> 43
/*     */     //   26: aload_1
/*     */     //   27: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   30: invokevirtual getDifficulty : ()Lnet/minecraft/world/Difficulty;
/*     */     //   33: getstatic net/minecraft/world/Difficulty.PEACEFUL : Lnet/minecraft/world/Difficulty;
/*     */     //   36: if_acmpeq -> 43
/*     */     //   39: iconst_1
/*     */     //   40: goto -> 44
/*     */     //   43: iconst_0
/*     */     //   44: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     //   #124	-> 7
/*     */     //   #125	-> 13
/*     */     //   #126	-> 20
/*     */     //   #127	-> 27
/*     */     //   #124	-> 44
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   12	31	1	player	Lnet/minecraft/world/entity/player/Player;
/*     */     //   0	45	0	target	Lnet/minecraft/world/entity/LivingEntity;
/*     */   }
/*     */   
/*     */   default boolean isAngryAt(LivingEntity entity, ServerLevel level) {
/* 132 */     if (!canAttack(entity))
/* 133 */       return false; 
/* 136 */     if (isValidPlayerTarget(entity) && isAngryAtAllPlayers(level))
/* 137 */       return true; 
/* 140 */     EntityReference<LivingEntity> persistentAngerTarget = getPersistentAngerTarget();
/* 141 */     return (persistentAngerTarget != null && persistentAngerTarget.matches(entity));
/*     */   }
/*     */   
/*     */   default boolean isAngryAtAllPlayers(ServerLevel level) {
/* 147 */     return (((Boolean)level.getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue() && isAngry() && getPersistentAngerTarget() == null);
/*     */   }
/*     */   
/*     */   default boolean isAngry() {
/* 151 */     long endTime = getPersistentAngerEndTime();
/* 152 */     if (endTime > 0L) {
/* 153 */       long remaining = endTime - level().getGameTime();
/* 154 */       return (remaining > 0L);
/*     */     } 
/* 156 */     return false;
/*     */   }
/*     */   
/*     */   default void playerDied(ServerLevel level, Player player) {
/* 160 */     if (!((Boolean)level.getGameRules().get(GameRules.FORGIVE_DEAD_PLAYERS)).booleanValue())
/*     */       return; 
/* 164 */     EntityReference<LivingEntity> persistentAngerTarget = getPersistentAngerTarget();
/* 165 */     if (persistentAngerTarget == null || !persistentAngerTarget.matches(player))
/*     */       return; 
/* 170 */     stopBeingAngry();
/*     */   }
/*     */   
/*     */   default void forgetCurrentTargetAndRefreshUniversalAnger() {
/* 175 */     stopBeingAngry();
/* 176 */     startPersistentAngerTimer();
/*     */   }
/*     */   
/*     */   default void stopBeingAngry() {
/* 180 */     setLastHurtByMob(null);
/* 181 */     setPersistentAngerTarget(null);
/* 182 */     setTarget(null);
/* 183 */     setPersistentAngerEndTime(-1L);
/*     */   }
/*     */   
/*     */   LivingEntity getLastHurtByMob();
/*     */   
/*     */   void setLastHurtByMob(LivingEntity paramLivingEntity);
/*     */   
/*     */   void setTarget(LivingEntity paramLivingEntity);
/*     */   
/*     */   boolean canAttack(LivingEntity paramLivingEntity);
/*     */   
/*     */   LivingEntity getTarget();
/*     */   
/*     */   LivingEntity getTargetUnchecked();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\NeutralMob.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */