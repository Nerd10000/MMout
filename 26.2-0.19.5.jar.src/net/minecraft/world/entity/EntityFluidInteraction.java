/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EntityFluidInteraction {
/*  24 */   private final Map<TagKey<Fluid>, Tracker> trackerByFluid = (Map<TagKey<Fluid>, Tracker>)new Reference2ObjectArrayMap();
/*     */   
/*     */   public EntityFluidInteraction(Set<TagKey<Fluid>> fluids) {
/*  27 */     for (TagKey<Fluid> fluid : fluids)
/*  28 */       this.trackerByFluid.put(fluid, new Tracker()); 
/*     */   }
/*     */   
/*     */   public void update(Entity entity, boolean ignoreCurrent) {
/*  33 */     this.trackerByFluid.values().forEach(Tracker::reset);
/*  35 */     AABB box = entity.getFluidInteractionBox();
/*  36 */     if (box == null)
/*     */       return; 
/*  40 */     int x0 = Mth.floor(box.minX);
/*  41 */     int y0 = Mth.floor(box.minY);
/*  42 */     int z0 = Mth.floor(box.minZ);
/*  43 */     int x1 = Mth.ceil(box.maxX) - 1;
/*  44 */     int y1 = Mth.ceil(box.maxY) - 1;
/*  45 */     int z1 = Mth.ceil(box.maxZ) - 1;
/*  49 */     if (!hasFluidAndLoaded(entity.level(), x0 - 1, y0, z0 - 1, x1 + 1, y1, z1 + 1))
/*     */       return; 
/*  54 */     double entityY = (entity.getBoundingBox()).minY;
/*  55 */     int eyeBlockX = entity.getBlockX();
/*  56 */     double eyeY = entity.getEyeY();
/*  57 */     int eyeBlockZ = entity.getBlockZ();
/*  59 */     Fluid lastFluidType = null;
/*  60 */     Tracker tracker = null;
/*  62 */     Level level = entity.level();
/*  64 */     BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
/*  66 */     for (int x = x0; x <= x1; x++) {
/*  67 */       for (int y = y0; y <= y1; y++) {
/*  68 */         for (int z = z0; z <= z1; z++) {
/*  69 */           mutablePos.set(x, y, z);
/*  70 */           FluidState fluidState = level.getFluidState((BlockPos)mutablePos);
/*  71 */           if (!fluidState.isEmpty()) {
/*  75 */             double fluidBottom = mutablePos.getY();
/*  76 */             double fluidTop = fluidBottom + fluidState.getHeight((BlockGetter)level, (BlockPos)mutablePos);
/*  77 */             if (fluidTop >= box.minY) {
/*  81 */               Fluid fluidType = fluidState.getType();
/*  82 */               if (fluidType != lastFluidType) {
/*  83 */                 lastFluidType = fluidType;
/*  84 */                 tracker = getTrackerFor(fluidType);
/*     */               } 
/*  87 */               if (tracker != null) {
/*  91 */                 if (x == eyeBlockX && z == eyeBlockZ && eyeY >= fluidBottom && eyeY <= fluidTop)
/*  92 */                   tracker.eyesInside = true; 
/*  95 */                 tracker.height = Math.max(fluidTop - entityY, tracker.height);
/*  96 */                 if (!ignoreCurrent) {
/*  97 */                   Vec3 flow = fluidState.getFlow((BlockGetter)level, (BlockPos)mutablePos);
/*  99 */                   if (tracker.height < 0.4D)
/* 100 */                     flow = flow.scale(tracker.height); 
/* 102 */                   tracker.accumulateCurrent(flow);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean hasFluidAndLoaded(Level level, int x0, int y0, int z0, int x1, int y1, int z1) {
/* 110 */     int sectionX0 = SectionPos.blockToSectionCoord(x0);
/* 111 */     int sectionY0 = SectionPos.blockToSectionCoord(y0);
/* 112 */     int sectionZ0 = SectionPos.blockToSectionCoord(z0);
/* 113 */     int sectionX1 = SectionPos.blockToSectionCoord(x1);
/* 114 */     int sectionY1 = SectionPos.blockToSectionCoord(y1);
/* 115 */     int sectionZ1 = SectionPos.blockToSectionCoord(z1);
/* 116 */     boolean hasFluid = false;
/* 117 */     for (int chunkZ = sectionZ0; chunkZ <= sectionZ1; chunkZ++) {
/* 118 */       for (int chunkX = sectionX0; chunkX <= sectionX1; chunkX++) {
/* 119 */         ChunkAccess chunk = level.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
/* 120 */         if (chunk == null)
/* 121 */           return false; 
/* 123 */         LevelChunkSection[] sections = chunk.getSections();
/* 124 */         for (int sectionY = sectionY0; sectionY <= sectionY1; sectionY++) {
/* 125 */           int sectionIndex = chunk.getSectionIndexFromSectionY(sectionY);
/* 126 */           if (sectionIndex >= 0 && sectionIndex < sections.length)
/* 129 */             hasFluid |= sections[sectionIndex].hasFluid(); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 133 */     return hasFluid;
/*     */   }
/*     */   
/*     */   private Tracker getTrackerFor(Fluid fluid) {
/* 137 */     for (Map.Entry<TagKey<Fluid>, Tracker> entry : this.trackerByFluid.entrySet()) {
/* 138 */       TagKey<Fluid> tag = entry.getKey();
/* 139 */       if (fluid.is(tag))
/* 140 */         return entry.getValue(); 
/*     */     } 
/* 143 */     return null;
/*     */   }
/*     */   
/*     */   public void applyCurrentTo(TagKey<Fluid> fluid, Entity entity, double scale) {
/* 147 */     Tracker tracker = this.trackerByFluid.get(fluid);
/* 148 */     if (tracker != null)
/* 149 */       tracker.applyCurrentTo(entity, scale); 
/*     */   }
/*     */   
/*     */   public double getFluidHeight(TagKey<Fluid> fluid) {
/* 154 */     Tracker tracker = this.trackerByFluid.get(fluid);
/* 155 */     return (tracker != null) ? tracker.height : 0.0D;
/*     */   }
/*     */   
/*     */   public boolean isInFluid(TagKey<Fluid> fluid) {
/* 159 */     return (getFluidHeight(fluid) > 0.0D);
/*     */   }
/*     */   
/*     */   public boolean isEyeInFluid(TagKey<Fluid> fluid) {
/* 163 */     Tracker tracker = this.trackerByFluid.get(fluid);
/* 164 */     return (tracker != null && tracker.eyesInside);
/*     */   }
/*     */   
/*     */   private static class Tracker {
/*     */     private double height;
/*     */     
/*     */     private boolean eyesInside;
/*     */     
/* 170 */     private Vec3 accumulatedCurrent = Vec3.ZERO;
/*     */     
/*     */     private int currentCount;
/*     */     
/*     */     public void reset() {
/* 174 */       this.height = 0.0D;
/* 175 */       this.eyesInside = false;
/* 176 */       this.accumulatedCurrent = Vec3.ZERO;
/* 177 */       this.currentCount = 0;
/*     */     }
/*     */     
/*     */     public void accumulateCurrent(Vec3 flow) {
/* 181 */       this.accumulatedCurrent = this.accumulatedCurrent.add(flow);
/* 182 */       this.currentCount++;
/*     */     }
/*     */     
/*     */     public void applyCurrentTo(Entity entity, double scale) {
/* 186 */       if (this.currentCount == 0 || this.accumulatedCurrent.lengthSqr() < 9.999999747378752E-6D)
/*     */         return; 
/* 191 */       if (!(entity instanceof net.minecraft.world.entity.player.Player)) {
/* 192 */         impulse = this.accumulatedCurrent.normalize();
/*     */       } else {
/* 194 */         impulse = this.accumulatedCurrent.scale(1.0D / this.currentCount);
/*     */       } 
/* 197 */       Vec3 oldMovement = entity.getDeltaMovement();
/* 198 */       Vec3 impulse = impulse.scale(scale);
/* 201 */       double min = 0.003D;
/* 202 */       if (Math.abs(oldMovement.x) < 0.003D && Math.abs(oldMovement.z) < 0.003D && impulse.length() < 0.0045000000000000005D)
/* 203 */         impulse = impulse.normalize().scale(0.0045000000000000005D); 
/* 206 */       entity.addDeltaMovement(impulse);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntityFluidInteraction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */