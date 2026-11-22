/*     */ package net.minecraft.world.entity.ai.village;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.clock.ClockTimeMarkers;
/*     */ import net.minecraft.world.clock.WorldClock;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.CustomSpawner;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VillageSiege implements CustomSpawner {
/*  26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private boolean hasSetupSiege;
/*     */   
/*  29 */   private State siegeState = State.SIEGE_DONE;
/*     */   
/*     */   private int zombiesToSpawn;
/*     */   
/*     */   private int nextSpawnTime;
/*     */   
/*     */   private int spawnX;
/*     */   
/*     */   private int spawnY;
/*     */   
/*     */   private int spawnZ;
/*     */   
/*     */   private enum State {
/*  37 */     SIEGE_CAN_ACTIVATE, SIEGE_TONIGHT, SIEGE_DONE;
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel level, boolean spawnEnemies) {
/*  45 */     if (level.isBrightOutside() || !spawnEnemies) {
/*  46 */       this.siegeState = State.SIEGE_DONE;
/*  47 */       this.hasSetupSiege = false;
/*     */       return;
/*     */     } 
/*  51 */     Optional<Holder<WorldClock>> defaultClock = level.dimensionType().defaultClock();
/*  52 */     if (defaultClock.isPresent() && level.clockManager().isAtTimeMarker(defaultClock.get(), ClockTimeMarkers.ROLL_VILLAGE_SIEGE))
/*  53 */       this.siegeState = (level.getRandom().nextInt(10) == 0) ? State.SIEGE_TONIGHT : State.SIEGE_DONE; 
/*  56 */     if (this.siegeState == State.SIEGE_DONE)
/*     */       return; 
/*  60 */     if (!this.hasSetupSiege)
/*  61 */       if (tryToSetupSiege(level)) {
/*  62 */         this.hasSetupSiege = true;
/*     */       } else {
/*     */         return;
/*     */       }  
/*  69 */     if (this.nextSpawnTime > 0) {
/*  70 */       this.nextSpawnTime--;
/*     */       return;
/*     */     } 
/*  74 */     this.nextSpawnTime = 2;
/*  75 */     if (this.zombiesToSpawn > 0) {
/*  76 */       trySpawn(level);
/*  77 */       this.zombiesToSpawn--;
/*     */     } else {
/*  79 */       this.siegeState = State.SIEGE_DONE;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean tryToSetupSiege(ServerLevel level) {
/*  84 */     RandomSource random = level.getRandom();
/*  85 */     for (Player player : level.players()) {
/*  86 */       if (!player.isSpectator()) {
/*  87 */         BlockPos center = player.blockPosition();
/*  88 */         if (!level.isVillage(center) || level.getBiome(center).is(BiomeTags.WITHOUT_ZOMBIE_SIEGES))
/*     */           continue; 
/*  93 */         for (int i = 0; i < 10; i++) {
/*  94 */           float angle = random.nextFloat() * 6.2831855F;
/*  95 */           this.spawnX = center.getX() + Mth.floor(Mth.cos(angle) * 32.0F);
/*  96 */           this.spawnY = center.getY();
/*  97 */           this.spawnZ = center.getZ() + Mth.floor(Mth.sin(angle) * 32.0F);
/*  99 */           if (findRandomSpawnPos(level, new BlockPos(this.spawnX, this.spawnY, this.spawnZ)) != null) {
/* 100 */             this.nextSpawnTime = 0;
/* 101 */             this.zombiesToSpawn = 20;
/*     */             break;
/*     */           } 
/*     */         } 
/* 105 */         return true;
/*     */       } 
/*     */     } 
/* 108 */     return false;
/*     */   }
/*     */   
/*     */   private void trySpawn(ServerLevel level) {
/*     */     Zombie zombie;
/* 112 */     Vec3 spawnPos = findRandomSpawnPos(level, new BlockPos(this.spawnX, this.spawnY, this.spawnZ));
/* 113 */     if (spawnPos == null)
/*     */       return; 
/*     */     try {
/* 118 */       zombie = new Zombie((Level)level);
/* 119 */       zombie.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(zombie.blockPosition()), EntitySpawnReason.EVENT, null);
/* 120 */     } catch (Exception e) {
/* 121 */       LOGGER.warn("Failed to create zombie for village siege at {}", spawnPos, e);
/*     */       return;
/*     */     } 
/* 124 */     zombie.snapTo(spawnPos.x, spawnPos.y, spawnPos.z, level.getRandom().nextFloat() * 360.0F, 0.0F);
/* 125 */     level.addFreshEntityWithPassengers((Entity)zombie);
/*     */   }
/*     */   
/*     */   private Vec3 findRandomSpawnPos(ServerLevel level, BlockPos pos) {
/* 130 */     RandomSource random = level.getRandom();
/* 131 */     for (int i = 0; i < 10; i++) {
/* 132 */       int x = pos.getX() + random.nextInt(16) - 8;
/* 133 */       int z = pos.getZ() + random.nextInt(16) - 8;
/* 134 */       int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
/* 135 */       BlockPos offset = new BlockPos(x, y, z);
/* 137 */       if (level.isVillage(offset))
/* 140 */         if (Monster.checkMonsterSpawnRules(EntityTypes.ZOMBIE, (ServerLevelAccessor)level, EntitySpawnReason.EVENT, offset, random))
/* 141 */           return Vec3.atBottomCenterOf((Vec3i)offset);  
/*     */     } 
/* 144 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\village\VillageSiege.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */