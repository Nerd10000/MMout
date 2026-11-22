/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import com.mojang.blaze3d.vertex.VertexSorting;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.renderer.StagedVertexBuffer;
/*    */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*    */ import net.minecraft.client.renderer.rendertype.PreparedRenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ 
/*    */ public abstract class RenderTypeFeatureRenderer<Submit extends SubmitNode> implements FeatureRenderer<Submit> {
/*    */   private Group currentGroup;
/*    */   
/* 18 */   private final List<Group> groups = new ArrayList<>();
/*    */   
/*    */   protected abstract void buildGroup(FeatureFrameContext paramFeatureFrameContext, List<Submit> paramList);
/*    */   
/*    */   protected final VertexConsumer getVertexBuilder(RenderType renderType) {
/* 23 */     return currentGroup().getVertexBuilder(renderType);
/*    */   }
/*    */   
/*    */   private Group currentGroup() {
/* 27 */     return Objects.<Group>requireNonNull(this.currentGroup, "Not preparing group");
/*    */   }
/*    */   
/*    */   public final void prepareGroup(FeatureFrameContext context, List<Submit> submits, boolean strictlyOrdered) {
/* 32 */     this.currentGroup = new Group(context.stagedVertexBuffer(), !strictlyOrdered);
/* 33 */     buildGroup(context, submits);
/* 34 */     this.groups.add(this.currentGroup);
/* 35 */     this.currentGroup = null;
/*    */   }
/*    */   
/*    */   public void executeGroup(FeatureFrameContext context, int groupIndex, List<Submit> submits, boolean strictlyOrdered) {
/* 40 */     Group group = this.groups.get(groupIndex);
/* 41 */     for (int i = 0; i < group.draws.size(); i++) {
/* 42 */       PreparedRenderType renderType = group.drawRenderTypes.get(i);
/* 43 */       StagedVertexBuffer.ExecuteInfo info = context.stagedVertexBuffer().getExecuteInfo(group.draws.get(i));
/* 44 */       if (info != null)
/* 45 */         renderType.drawFromBuffer(info); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void finishExecute(FeatureFrameContext context) {
/* 52 */     this.groups.clear();
/*    */   }
/*    */   
/*    */   private static class Group {
/*    */     private final StagedVertexBuffer stagedBuffer;
/*    */     
/*    */     private final boolean canReorder;
/*    */     
/* 59 */     private final List<StagedVertexBuffer.Draw> draws = new ArrayList<>();
/*    */     
/* 60 */     private final List<PreparedRenderType> drawRenderTypes = new ArrayList<>();
/*    */     
/*    */     private RenderType lastRenderType;
/*    */     
/*    */     private StagedVertexBuffer.Draw lastDraw;
/*    */     
/*    */     private Group(StagedVertexBuffer stagedBuffer, boolean canReorder) {
/* 66 */       this.stagedBuffer = stagedBuffer;
/* 67 */       this.canReorder = canReorder;
/*    */     }
/*    */     
/*    */     public VertexConsumer getVertexBuilder(RenderType renderType) {
/* 71 */       if (this.lastDraw == null || this.lastRenderType != renderType || !renderType.canConsolidateConsecutiveGeometry()) {
/* 72 */         this.lastDraw = getOrAddDraw(renderType);
/* 73 */         this.lastRenderType = renderType;
/*    */       } 
/* 75 */       return this.stagedBuffer.getVertexBuilder(this.lastDraw);
/*    */     }
/*    */     
/*    */     private StagedVertexBuffer.Draw getOrAddDraw(RenderType renderType) {
/* 80 */       PreparedRenderType preparedRenderType = renderType.prepare();
/* 81 */       int existingIndex = (this.canReorder && renderType.canConsolidateConsecutiveGeometry()) ? this.drawRenderTypes.indexOf(preparedRenderType) : -1;
/* 82 */       if (existingIndex != -1)
/* 83 */         return this.draws.get(existingIndex); 
/* 85 */       VertexSorting quadSorting = renderType.sortOnUpload() ? RenderSystem.getProjectionType().vertexSorting() : null;
/* 86 */       StagedVertexBuffer.Draw draw = this.stagedBuffer.appendDraw(renderType.format(), renderType.primitiveTopology(), quadSorting);
/* 87 */       this.draws.add(draw);
/* 88 */       this.drawRenderTypes.add(preparedRenderType);
/* 89 */       return draw;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\RenderTypeFeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */