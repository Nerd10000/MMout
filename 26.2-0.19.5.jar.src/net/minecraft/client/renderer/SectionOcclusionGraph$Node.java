/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ 
/*     */ @VisibleForDebug
/*     */ public class Node {
/*     */   @VisibleForDebug
/*     */   protected final SectionRenderDispatcher.RenderSection section;
/*     */   
/*     */   private byte sourceDirections;
/*     */   
/*     */   private byte directions;
/*     */   
/*     */   @VisibleForDebug
/*     */   public final int step;
/*     */   
/*     */   private Node(SectionRenderDispatcher.RenderSection section, Direction sourceDirection, int step) {
/* 460 */     this.section = section;
/* 461 */     if (sourceDirection != null)
/* 462 */       addSourceDirection(sourceDirection); 
/* 464 */     this.step = step;
/*     */   }
/*     */   
/*     */   private void setDirections(byte oldDirections, Direction direction) {
/* 468 */     this.directions = (byte)(this.directions | oldDirections | 1 << direction.ordinal());
/*     */   }
/*     */   
/*     */   private boolean hasDirection(Direction direction) {
/* 472 */     return ((this.directions & 1 << direction.ordinal()) > 0);
/*     */   }
/*     */   
/*     */   private void addSourceDirection(Direction direction) {
/* 476 */     this.sourceDirections = (byte)(this.sourceDirections | this.sourceDirections | 1 << direction.ordinal());
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public boolean hasSourceDirection(int directionOrdinal) {
/* 481 */     return ((this.sourceDirections & 1 << directionOrdinal) > 0);
/*     */   }
/*     */   
/*     */   private boolean hasSourceDirections() {
/* 485 */     return (this.sourceDirections != 0);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 490 */     return Long.hashCode(this.section.getSectionNode());
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/*     */     Node other;
/* 495 */     if (obj instanceof Node) {
/* 495 */       other = (Node)obj;
/*     */     } else {
/* 496 */       return false;
/*     */     } 
/* 499 */     return (this.section.getSectionNode() == other.section.getSectionNode());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\SectionOcclusionGraph$Node.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */