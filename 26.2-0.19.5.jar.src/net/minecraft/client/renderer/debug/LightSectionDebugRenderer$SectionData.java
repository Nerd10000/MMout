/*     */ package net.minecraft.client.renderer.debug;
/*     */ 
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.lighting.LayerLightSectionStorage;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
/*     */ import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
/*     */ 
/*     */ final class SectionData {
/*     */   private final DiscreteVoxelShape lightAndBlocksShape;
/*     */   
/*     */   private final DiscreteVoxelShape lightShape;
/*     */   
/*     */   private final SectionPos minPos;
/*     */   
/*     */   private SectionData(LevelLightEngine engine, SectionPos centerPos, int radius, LightLayer lightLayer) {
/* 104 */     int size = radius * 2 + 1;
/* 106 */     this.lightAndBlocksShape = (DiscreteVoxelShape)new BitSetDiscreteVoxelShape(size, size, size);
/* 107 */     this.lightShape = (DiscreteVoxelShape)new BitSetDiscreteVoxelShape(size, size, size);
/* 109 */     for (int z = 0; z < size; z++) {
/* 110 */       for (int y = 0; y < size; y++) {
/* 111 */         for (int x = 0; x < size; x++) {
/* 112 */           SectionPos pos = SectionPos.of(centerPos.x() + x - radius, centerPos.y() + y - radius, centerPos.z() + z - radius);
/* 113 */           LayerLightSectionStorage.SectionType type = engine.getDebugSectionType(lightLayer, pos);
/* 114 */           if (type == LayerLightSectionStorage.SectionType.LIGHT_AND_DATA) {
/* 115 */             this.lightAndBlocksShape.fill(x, y, z);
/* 116 */             this.lightShape.fill(x, y, z);
/* 117 */           } else if (type == LayerLightSectionStorage.SectionType.LIGHT_ONLY) {
/* 118 */             this.lightShape.fill(x, y, z);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 124 */     this.minPos = SectionPos.of(centerPos.x() - radius, centerPos.y() - radius, centerPos.z() - radius);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\LightSectionDebugRenderer$SectionData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */