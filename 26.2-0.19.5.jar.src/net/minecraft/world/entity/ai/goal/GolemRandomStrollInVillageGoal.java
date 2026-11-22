/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.level.entity.EntityAccess;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class GolemRandomStrollInVillageGoal extends RandomStrollGoal {
/*     */   private static final int POI_SECTION_SCAN_RADIUS = 2;
/*     */   
/*     */   private static final int VILLAGER_SCAN_RADIUS = 32;
/*     */   
/*     */   private static final int RANDOM_POS_XY_DISTANCE = 10;
/*     */   
/*     */   private static final int RANDOM_POS_Y_DISTANCE = 7;
/*     */   
/*     */   public GolemRandomStrollInVillageGoal(PathfinderMob mob, double speedModifier) {
/*  26 */     super(mob, speedModifier, 240, false);
/*     */   }
/*     */   
/*     */   protected Vec3 getPosition() {
/*     */     Vec3 target;
/*  32 */     RandomSource random = this.mob.level().getRandom();
/*  33 */     if (random.nextFloat() < 0.3F)
/*  34 */       return getPositionTowardsAnywhere(); 
/*  37 */     if (random.nextFloat() < 0.7F) {
/*  38 */       target = getPositionTowardsVillagerWhoWantsGolem();
/*  39 */       if (target == null)
/*  40 */         target = getPositionTowardsPoi(); 
/*     */     } else {
/*  43 */       target = getPositionTowardsPoi();
/*  44 */       if (target == null)
/*  45 */         target = getPositionTowardsVillagerWhoWantsGolem(); 
/*     */     } 
/*  49 */     return (target == null) ? getPositionTowardsAnywhere() : target;
/*     */   }
/*     */   
/*     */   private Vec3 getPositionTowardsAnywhere() {
/*  53 */     return LandRandomPos.getPos(this.mob, 10, 7);
/*     */   }
/*     */   
/*     */   private Vec3 getPositionTowardsVillagerWhoWantsGolem() {
/*  57 */     ServerLevel level = (ServerLevel)this.mob.level();
/*  58 */     List<Villager> villagers = level.getEntities((EntityTypeTest)EntityTypes.VILLAGER, this.mob.getBoundingBox().inflate(32.0D), this::doesVillagerWantGolem);
/*  59 */     if (villagers.isEmpty())
/*  60 */       return null; 
/*  62 */     Villager villager = villagers.get(this.mob.level().getRandom().nextInt(villagers.size()));
/*  63 */     Vec3 targetPos = villager.position();
/*  64 */     return LandRandomPos.getPosTowards(this.mob, 10, 7, targetPos);
/*     */   }
/*     */   
/*     */   private Vec3 getPositionTowardsPoi() {
/*  68 */     SectionPos targetSection = getRandomVillageSection();
/*  69 */     if (targetSection == null)
/*  70 */       return null; 
/*  73 */     BlockPos targetPos = getRandomPoiWithinSection(targetSection);
/*  74 */     if (targetPos == null)
/*  76 */       return null; 
/*  79 */     return LandRandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf((Vec3i)targetPos));
/*     */   }
/*     */   
/*     */   private SectionPos getRandomVillageSection() {
/*  83 */     ServerLevel level = (ServerLevel)this.mob.level();
/*  87 */     List<SectionPos> villageSections = (List<SectionPos>)SectionPos.cube(SectionPos.of((EntityAccess)this.mob), 2).filter(sectionPos -> (level.sectionsToVillage(sectionPos) == 0)).collect(Collectors.toList());
/*  89 */     if (villageSections.isEmpty())
/*  90 */       return null; 
/*  92 */     return villageSections.get(level.getRandom().nextInt(villageSections.size()));
/*     */   }
/*     */   
/*     */   private BlockPos getRandomPoiWithinSection(SectionPos sectionPos) {
/*  96 */     ServerLevel level = (ServerLevel)this.mob.level();
/*  97 */     PoiManager poiManager = level.getPoiManager();
/* 100 */     List<BlockPos> pois = (List<BlockPos>)poiManager.getInRange(poiType -> true, sectionPos.center(), 8, PoiManager.Occupancy.IS_OCCUPIED).map(PoiRecord::getPos).collect(Collectors.toList());
/* 102 */     if (pois.isEmpty())
/* 103 */       return null; 
/* 105 */     return pois.get(level.getRandom().nextInt(pois.size()));
/*     */   }
/*     */   
/*     */   private boolean doesVillagerWantGolem(Villager villager) {
/* 109 */     return villager.wantsToSpawnGolem(this.mob.level().getGameTime());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\GolemRandomStrollInVillageGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */