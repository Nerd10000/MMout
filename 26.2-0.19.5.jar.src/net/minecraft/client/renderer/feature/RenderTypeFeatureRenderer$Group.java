/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import com.mojang.blaze3d.vertex.VertexSorting;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.StagedVertexBuffer;
/*    */ import net.minecraft.client.renderer.rendertype.PreparedRenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ 
/*    */ class Group {
/*    */   private final StagedVertexBuffer stagedBuffer;
/*    */   
/*    */   private final boolean canReorder;
/*    */   
/* 59 */   private final List<StagedVertexBuffer.Draw> draws = new ArrayList<>();
/*    */   
/* 60 */   private final List<PreparedRenderType> drawRenderTypes = new ArrayList<>();
/*    */   
/*    */   private RenderType lastRenderType;
/*    */   
/*    */   private StagedVertexBuffer.Draw lastDraw;
/*    */   
/*    */   private Group(StagedVertexBuffer stagedBuffer, boolean canReorder) {
/* 66 */     this.stagedBuffer = stagedBuffer;
/* 67 */     this.canReorder = canReorder;
/*    */   }
/*    */   
/*    */   public VertexConsumer getVertexBuilder(RenderType renderType) {
/* 71 */     if (this.lastDraw == null || this.lastRenderType != renderType || !renderType.canConsolidateConsecutiveGeometry()) {
/* 72 */       this.lastDraw = getOrAddDraw(renderType);
/* 73 */       this.lastRenderType = renderType;
/*    */     } 
/* 75 */     return this.stagedBuffer.getVertexBuilder(this.lastDraw);
/*    */   }
/*    */   
/*    */   private StagedVertexBuffer.Draw getOrAddDraw(RenderType renderType) {
/* 80 */     PreparedRenderType preparedRenderType = renderType.prepare();
/* 81 */     int existingIndex = (this.canReorder && renderType.canConsolidateConsecutiveGeometry()) ? this.drawRenderTypes.indexOf(preparedRenderType) : -1;
/* 82 */     if (existingIndex != -1)
/* 83 */       return this.draws.get(existingIndex); 
/* 85 */     VertexSorting quadSorting = renderType.sortOnUpload() ? RenderSystem.getProjectionType().vertexSorting() : null;
/* 86 */     StagedVertexBuffer.Draw draw = this.stagedBuffer.appendDraw(renderType.format(), renderType.primitiveTopology(), quadSorting);
/* 87 */     this.draws.add(draw);
/* 88 */     this.drawRenderTypes.add(preparedRenderType);
/* 89 */     return draw;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\RenderTypeFeatureRenderer$Group.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */