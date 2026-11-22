/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.block.BlockAndTintGetter;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.CardinalLighting;
/*    */ import net.minecraft.world.level.ColorResolver;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class RenderSectionRegion implements BlockAndTintGetter {
/*    */   public static final int RADIUS = 1;
/*    */   
/*    */   public static final int SIZE = 3;
/*    */   
/*    */   private final int minSectionX;
/*    */   
/*    */   private final int minSectionY;
/*    */   
/*    */   private final int minSectionZ;
/*    */   
/*    */   private final SectionCopy[] sections;
/*    */   
/*    */   private final ClientLevel level;
/*    */   
/*    */   private final CardinalLighting cardinalLighting;
/*    */   
/*    */   private final LevelLightEngine lightEngine;
/*    */   
/*    */   public RenderSectionRegion(ClientLevel level, int minSectionX, int minSectionY, int minSectionZ, SectionCopy[] sections) {
/* 29 */     this.level = level;
/* 30 */     this.minSectionX = minSectionX;
/* 31 */     this.minSectionY = minSectionY;
/* 32 */     this.minSectionZ = minSectionZ;
/* 33 */     this.sections = sections;
/* 34 */     this.cardinalLighting = level.cardinalLighting();
/* 35 */     this.lightEngine = level.getLightEngine();
/*    */   }
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 40 */     return getSection(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getY()), SectionPos.blockToSectionCoord(pos.getZ())).getBlockState(pos);
/*    */   }
/*    */   
/*    */   public FluidState getFluidState(BlockPos pos) {
/* 45 */     return getSection(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getY()), SectionPos.blockToSectionCoord(pos.getZ())).getBlockState(pos).getFluidState();
/*    */   }
/*    */   
/*    */   public CardinalLighting cardinalLighting() {
/* 50 */     return this.cardinalLighting;
/*    */   }
/*    */   
/*    */   public LevelLightEngine getLightEngine() {
/* 55 */     return this.lightEngine;
/*    */   }
/*    */   
/*    */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 60 */     return getSection(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getY()), SectionPos.blockToSectionCoord(pos.getZ())).getBlockEntity(pos);
/*    */   }
/*    */   
/*    */   private SectionCopy getSection(int sectionX, int sectionY, int sectionZ) {
/* 64 */     return this.sections[index(this.minSectionX, this.minSectionY, this.minSectionZ, sectionX, sectionY, sectionZ)];
/*    */   }
/*    */   
/*    */   public int getBlockTint(BlockPos pos, ColorResolver resolver) {
/* 69 */     return this.level.getBlockTint(pos, resolver);
/*    */   }
/*    */   
/*    */   public int getMinY() {
/* 74 */     return this.level.getMinY();
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 79 */     return this.level.getHeight();
/*    */   }
/*    */   
/*    */   public static int index(int minSectionX, int minSectionY, int minSectionZ, int sectionX, int sectionY, int sectionZ) {
/* 83 */     return sectionX - minSectionX + (sectionY - minSectionY) * 3 + (sectionZ - minSectionZ) * 3 * 3;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\RenderSectionRegion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */