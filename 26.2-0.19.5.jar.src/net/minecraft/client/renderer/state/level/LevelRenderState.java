/*    */ package net.minecraft.client.renderer.state.level;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ 
/*    */ public class LevelRenderState {
/* 11 */   public CameraRenderState cameraRenderState = new CameraRenderState();
/*    */   
/* 12 */   public final List<SectionUpdateRenderState> sectionUpdateRenderStates = new ArrayList<>();
/*    */   
/* 13 */   public final List<EntityRenderState> entityRenderStates = new ArrayList<>();
/*    */   
/* 14 */   public final List<BlockEntityRenderState> blockEntityRenderStates = new ArrayList<>();
/*    */   
/*    */   public BlockOutlineRenderState blockOutlineRenderState;
/*    */   
/* 16 */   public final List<BlockBreakingRenderState> blockBreakingRenderStates = new ArrayList<>();
/*    */   
/* 17 */   public final WeatherRenderState weatherRenderState = new WeatherRenderState();
/*    */   
/* 18 */   public final WorldBorderRenderState worldBorderRenderState = new WorldBorderRenderState();
/*    */   
/* 19 */   public final SkyRenderState skyRenderState = new SkyRenderState();
/*    */   
/* 20 */   public final ParticlesRenderState particlesRenderState = new ParticlesRenderState();
/*    */   
/*    */   public long gameTime;
/*    */   
/*    */   public int lastEntityRenderStateCount;
/*    */   
/*    */   public int cloudColor;
/*    */   
/*    */   public float cloudHeight;
/*    */   
/*    */   public boolean render3dCrosshair;
/*    */   
/*    */   public Runnable playerCompiledSectionCallback;
/*    */   
/* 27 */   public ChunkLoadingRenderState chunkLoadingRenderState = new ChunkLoadingRenderState();
/*    */   
/*    */   public boolean shouldResetChunkLayerSampler;
/*    */   
/*    */   public boolean shouldShowEntityOutlines;
/*    */   
/*    */   public boolean shouldResetSkyRenderer;
/*    */   
/*    */   public void reset() {
/* 33 */     this.sectionUpdateRenderStates.clear();
/* 34 */     this.entityRenderStates.clear();
/* 35 */     this.blockEntityRenderStates.clear();
/* 36 */     this.blockBreakingRenderStates.clear();
/* 37 */     this.blockOutlineRenderState = null;
/* 38 */     this.weatherRenderState.reset();
/* 39 */     this.worldBorderRenderState.reset();
/* 40 */     this.skyRenderState.reset();
/* 41 */     this.particlesRenderState.reset();
/* 42 */     this.chunkLoadingRenderState.reset();
/* 43 */     this.gameTime = 0L;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\level\LevelRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */