/*     */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Relative;
/*     */ import net.minecraft.world.entity.monster.Endermite;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ThrownEnderpearl extends ThrowableItemProjectile {
/*  36 */   private long ticketTimer = 0L;
/*     */   
/*     */   public ThrownEnderpearl(EntityType<? extends ThrownEnderpearl> type, Level level) {
/*  39 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public ThrownEnderpearl(Level level, LivingEntity mob, ItemStack itemStack) {
/*  43 */     super(EntityTypes.ENDER_PEARL, mob, level, itemStack);
/*     */   }
/*     */   
/*     */   protected Item getDefaultItem() {
/*  48 */     return Items.ENDER_PEARL;
/*     */   }
/*     */   
/*     */   protected void setOwner(EntityReference<Entity> owner) {
/*  53 */     deregisterFromCurrentOwner();
/*  54 */     super.setOwner(owner);
/*  55 */     registerToCurrentOwner();
/*     */   }
/*     */   
/*     */   private void deregisterFromCurrentOwner() {
/*  59 */     Entity entity = getOwner();
/*  59 */     if (entity instanceof ServerPlayer) {
/*  59 */       ServerPlayer serverPlayer = (ServerPlayer)entity;
/*  60 */       serverPlayer.deregisterEnderPearl(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void registerToCurrentOwner() {
/*  65 */     Entity entity = getOwner();
/*  65 */     if (entity instanceof ServerPlayer) {
/*  65 */       ServerPlayer serverPlayer = (ServerPlayer)entity;
/*  66 */       serverPlayer.registerEnderPearl(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Entity getOwner() {
/*     */     ServerLevel serverLevel;
/*  72 */     if (this.owner != null) {
/*  72 */       Level level = level();
/*  72 */       if (level instanceof ServerLevel) {
/*  72 */         serverLevel = (ServerLevel)level;
/*     */       } else {
/*  73 */         return super.getOwner();
/*     */       } 
/*     */     } else {
/*  73 */       return super.getOwner();
/*     */     } 
/*  75 */     return (Entity)this.owner.getEntity((Level)serverLevel, Entity.class);
/*     */   }
/*     */   
/*     */   private static Entity findOwnerIncludingDeadPlayer(ServerLevel serverLevel, UUID uuid) {
/*  79 */     Entity owner = serverLevel.getEntityInAnyDimension(uuid);
/*  80 */     if (owner != null)
/*  81 */       return owner; 
/*  85 */     return (Entity)serverLevel.getServer().getPlayerList().getPlayer(uuid);
/*     */   }
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/*  90 */     super.onHitEntity(hitResult);
/*  91 */     hitResult.getEntity().hurt(damageSources().thrown((Entity)this, getOwner()), 0.0F);
/*     */   }
/*     */   
/*     */   protected void onHit(HitResult hitResult) {
/*  96 */     super.onHit(hitResult);
/*  98 */     for (int i = 0; i < 32; i++)
/*  99 */       level().addParticle((ParticleOptions)ParticleTypes.PORTAL, getX(), getY() + this.random.nextDouble() * 2.0D, getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian()); 
/* 102 */     Level level = level();
/* 102 */     if (level instanceof ServerLevel) {
/* 102 */       ServerLevel serverLevel = (ServerLevel)level;
/* 102 */       if (!isRemoved()) {
/* 106 */         Entity owner = getOwner();
/* 107 */         if (owner == null || !isAllowedToTeleportOwner(owner, (Level)serverLevel)) {
/* 108 */           discard();
/*     */           return;
/*     */         } 
/* 114 */         Vec3 teleportPos = oldPosition();
/* 116 */         if (owner instanceof ServerPlayer) {
/* 116 */           ServerPlayer player = (ServerPlayer)owner;
/* 117 */           if (player.connection.isAcceptingMessages()) {
/* 118 */             if (this.random.nextFloat() < 0.05F && serverLevel.isSpawningMonsters() && serverLevel.getLevelData().getDifficulty() != Difficulty.PEACEFUL) {
/* 119 */               Endermite endermite = (Endermite)EntityTypes.ENDERMITE.create((Level)serverLevel, EntitySpawnReason.TRIGGERED);
/* 120 */               if (endermite != null) {
/* 121 */                 endermite.snapTo(owner.getX(), owner.getY(), owner.getZ(), owner.getYRot(), owner.getXRot());
/* 122 */                 serverLevel.addFreshEntity((Entity)endermite);
/*     */               } 
/*     */             } 
/* 126 */             if (isOnPortalCooldown())
/* 129 */               owner.setPortalCooldown(); 
/* 132 */             ServerPlayer newOwner = player.teleport(new TeleportTransition(serverLevel, teleportPos, Vec3.ZERO, 0.0F, 0.0F, Relative.union(new Set[] { Relative.ROTATION, Relative.DELTA }, ), TeleportTransition.DO_NOTHING));
/* 133 */             if (newOwner != null) {
/* 134 */               newOwner.resetFallDistance();
/* 135 */               newOwner.resetCurrentImpulseContext();
/* 136 */               newOwner.hurtServer(player.level(), damageSources().enderPearl(), 5.0F);
/*     */             } 
/* 139 */             playSound((Level)serverLevel, teleportPos);
/*     */           } 
/*     */         } else {
/* 142 */           Entity newOwner = owner.teleport(new TeleportTransition(serverLevel, teleportPos, owner.getDeltaMovement(), owner.getYRot(), owner.getXRot(), TeleportTransition.DO_NOTHING));
/* 143 */           if (newOwner != null)
/* 144 */             newOwner.resetFallDistance(); 
/* 146 */           if (newOwner instanceof LivingEntity) {
/* 146 */             LivingEntity livingEntity = (LivingEntity)newOwner;
/* 147 */             livingEntity.resetCurrentImpulseContext();
/*     */           } 
/* 150 */           playSound((Level)serverLevel, teleportPos);
/*     */         } 
/* 153 */         discard();
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isAllowedToTeleportOwner(Entity owner, Level newLevel) {
/* 157 */     if (owner.level().dimension() == newLevel.dimension()) {
/* 158 */       if (owner instanceof LivingEntity) {
/* 158 */         LivingEntity livingOwner = (LivingEntity)owner;
/* 159 */         return (livingOwner.isAlive() && !livingOwner.isSleeping());
/*     */       } 
/* 161 */       return owner.isAlive();
/*     */     } 
/* 163 */     return owner.canUsePortal(true);
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   4: astore_2
/*     */     //   5: aload_2
/*     */     //   6: instanceof net/minecraft/server/level/ServerLevel
/*     */     //   9: ifeq -> 20
/*     */     //   12: aload_2
/*     */     //   13: checkcast net/minecraft/server/level/ServerLevel
/*     */     //   16: astore_1
/*     */     //   17: goto -> 25
/*     */     //   20: aload_0
/*     */     //   21: invokespecial tick : ()V
/*     */     //   24: return
/*     */     //   25: aload_0
/*     */     //   26: invokevirtual position : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   29: invokevirtual x : ()D
/*     */     //   32: invokestatic blockToSectionCoord : (D)I
/*     */     //   35: istore_2
/*     */     //   36: aload_0
/*     */     //   37: invokevirtual position : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   40: invokevirtual z : ()D
/*     */     //   43: invokestatic blockToSectionCoord : (D)I
/*     */     //   46: istore_3
/*     */     //   47: aload_0
/*     */     //   48: getfield owner : Lnet/minecraft/world/entity/EntityReference;
/*     */     //   51: ifnull -> 68
/*     */     //   54: aload_1
/*     */     //   55: aload_0
/*     */     //   56: getfield owner : Lnet/minecraft/world/entity/EntityReference;
/*     */     //   59: invokevirtual getUUID : ()Ljava/util/UUID;
/*     */     //   62: invokestatic findOwnerIncludingDeadPlayer : (Lnet/minecraft/server/level/ServerLevel;Ljava/util/UUID;)Lnet/minecraft/world/entity/Entity;
/*     */     //   65: goto -> 69
/*     */     //   68: aconst_null
/*     */     //   69: astore #4
/*     */     //   71: aload #4
/*     */     //   73: instanceof net/minecraft/server/level/ServerPlayer
/*     */     //   76: ifeq -> 132
/*     */     //   79: aload #4
/*     */     //   81: checkcast net/minecraft/server/level/ServerPlayer
/*     */     //   84: astore #5
/*     */     //   86: aload #4
/*     */     //   88: invokevirtual isAlive : ()Z
/*     */     //   91: ifne -> 132
/*     */     //   94: aload #5
/*     */     //   96: getfield wonGame : Z
/*     */     //   99: ifne -> 132
/*     */     //   102: aload #5
/*     */     //   104: invokevirtual level : ()Lnet/minecraft/server/level/ServerLevel;
/*     */     //   107: invokevirtual getGameRules : ()Lnet/minecraft/world/level/gamerules/GameRules;
/*     */     //   110: getstatic net/minecraft/world/level/gamerules/GameRules.ENDER_PEARLS_VANISH_ON_DEATH : Lnet/minecraft/world/level/gamerules/GameRule;
/*     */     //   113: invokevirtual get : (Lnet/minecraft/world/level/gamerules/GameRule;)Ljava/lang/Object;
/*     */     //   116: checkcast java/lang/Boolean
/*     */     //   119: invokevirtual booleanValue : ()Z
/*     */     //   122: ifeq -> 132
/*     */     //   125: aload_0
/*     */     //   126: invokevirtual discard : ()V
/*     */     //   129: goto -> 136
/*     */     //   132: aload_0
/*     */     //   133: invokespecial tick : ()V
/*     */     //   136: aload_0
/*     */     //   137: invokevirtual isAlive : ()Z
/*     */     //   140: ifne -> 144
/*     */     //   143: return
/*     */     //   144: aload_0
/*     */     //   145: invokevirtual position : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   148: invokestatic containing : (Lnet/minecraft/core/Position;)Lnet/minecraft/core/BlockPos;
/*     */     //   151: astore #5
/*     */     //   153: aload_0
/*     */     //   154: dup
/*     */     //   155: getfield ticketTimer : J
/*     */     //   158: lconst_1
/*     */     //   159: lsub
/*     */     //   160: dup2_x1
/*     */     //   161: putfield ticketTimer : J
/*     */     //   164: lconst_0
/*     */     //   165: lcmp
/*     */     //   166: ifle -> 193
/*     */     //   169: iload_2
/*     */     //   170: aload #5
/*     */     //   172: invokevirtual getX : ()I
/*     */     //   175: invokestatic blockToSectionCoord : (I)I
/*     */     //   178: if_icmpne -> 193
/*     */     //   181: iload_3
/*     */     //   182: aload #5
/*     */     //   184: invokevirtual getZ : ()I
/*     */     //   187: invokestatic blockToSectionCoord : (I)I
/*     */     //   190: if_icmpeq -> 218
/*     */     //   193: aload #4
/*     */     //   195: instanceof net/minecraft/server/level/ServerPlayer
/*     */     //   198: ifeq -> 218
/*     */     //   201: aload #4
/*     */     //   203: checkcast net/minecraft/server/level/ServerPlayer
/*     */     //   206: astore #6
/*     */     //   208: aload_0
/*     */     //   209: aload #6
/*     */     //   211: aload_0
/*     */     //   212: invokevirtual registerAndUpdateEnderPearlTicket : (Lnet/minecraft/world/entity/projectile/throwableitemprojectile/ThrownEnderpearl;)J
/*     */     //   215: putfield ticketTimer : J
/*     */     //   218: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #168	-> 0
/*     */     //   #169	-> 20
/*     */     //   #170	-> 24
/*     */     //   #173	-> 25
/*     */     //   #174	-> 36
/*     */     //   #177	-> 47
/*     */     //   #179	-> 71
/*     */     //   #180	-> 125
/*     */     //   #182	-> 132
/*     */     //   #185	-> 136
/*     */     //   #186	-> 143
/*     */     //   #189	-> 144
/*     */     //   #190	-> 153
/*     */     //   #191	-> 184
/*     */     //   #192	-> 193
/*     */     //   #193	-> 208
/*     */     //   #196	-> 218
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   17	3	1	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   86	46	5	serverPlayer	Lnet/minecraft/server/level/ServerPlayer;
/*     */     //   208	10	6	serverPlayer	Lnet/minecraft/server/level/ServerPlayer;
/*     */     //   0	219	0	this	Lnet/minecraft/world/entity/projectile/throwableitemprojectile/ThrownEnderpearl;
/*     */     //   25	194	1	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   36	183	2	previousChunkX	I
/*     */     //   47	172	3	previousChunkZ	I
/*     */     //   71	148	4	owner	Lnet/minecraft/world/entity/Entity;
/*     */     //   153	66	5	currentPos	Lnet/minecraft/core/BlockPos;
/*     */   }
/*     */   
/*     */   private void playSound(Level level, Vec3 position) {
/* 199 */     level.playSound(null, position.x, position.y, position.z, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
/*     */   }
/*     */   
/*     */   public Entity teleport(TeleportTransition transition) {
/* 204 */     Entity newEntity = super.teleport(transition);
/* 205 */     if (newEntity != null)
/* 209 */       newEntity.placePortalTicket(BlockPos.containing((Position)newEntity.position())); 
/* 211 */     return newEntity;
/*     */   }
/*     */   
/*     */   public boolean canTeleport(Level from, Level to) {
/* 216 */     if (from.dimension() == Level.END && to.dimension() == Level.OVERWORLD) {
/* 216 */       Entity entity = getOwner();
/* 216 */       if (entity instanceof ServerPlayer) {
/* 216 */         ServerPlayer player = (ServerPlayer)entity;
/* 217 */         return (super.canTeleport(from, to) && player.seenCredits);
/*     */       } 
/*     */     } 
/* 219 */     return super.canTeleport(from, to);
/*     */   }
/*     */   
/*     */   protected void onInsideBlock(BlockState state) {
/* 224 */     super.onInsideBlock(state);
/* 225 */     if (state.is(Blocks.END_GATEWAY)) {
/* 225 */       Entity entity = getOwner();
/* 225 */       if (entity instanceof ServerPlayer) {
/* 225 */         ServerPlayer player = (ServerPlayer)entity;
/* 226 */         player.onInsideBlock(state);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onRemoval(Entity.RemovalReason reason) {
/* 232 */     if (reason != Entity.RemovalReason.UNLOADED_WITH_PLAYER)
/* 233 */       deregisterFromCurrentOwner(); 
/* 235 */     super.onRemoval(reason);
/*     */   }
/*     */   
/*     */   public void onAboveBubbleColumn(boolean dragDown, BlockPos pos) {
/* 240 */     Entity.handleOnAboveBubbleColumn((Entity)this, dragDown, pos);
/*     */   }
/*     */   
/*     */   public void onInsideBubbleColumn(boolean dragDown) {
/* 245 */     Entity.handleOnInsideBubbleColumn((Entity)this, dragDown);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownEnderpearl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */