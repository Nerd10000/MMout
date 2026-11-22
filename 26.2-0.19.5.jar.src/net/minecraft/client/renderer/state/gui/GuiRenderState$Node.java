/*     */ package net.minecraft.client.renderer.state.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
/*     */ 
/*     */ class Node {
/*     */   public final Node parent;
/*     */   
/*     */   public Node up;
/*     */   
/*     */   public List<GuiElementRenderState> elementStates;
/*     */   
/*     */   public List<GuiElementRenderState> glyphStates;
/*     */   
/*     */   public List<GuiItemRenderState> itemStates;
/*     */   
/*     */   public List<GuiTextRenderState> textStates;
/*     */   
/*     */   public List<PictureInPictureRenderState> picturesInPictureStates;
/*     */   
/*     */   private Node(Node parent) {
/* 265 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public void addItem(GuiItemRenderState itemState) {
/* 269 */     if (this.itemStates == null)
/* 270 */       this.itemStates = new ArrayList<>(); 
/* 272 */     this.itemStates.add(itemState);
/*     */   }
/*     */   
/*     */   public void addText(GuiTextRenderState textState) {
/* 276 */     if (this.textStates == null)
/* 277 */       this.textStates = new ArrayList<>(); 
/* 279 */     this.textStates.add(textState);
/*     */   }
/*     */   
/*     */   public void addPicturesInPictureState(PictureInPictureRenderState picturesInPictureState) {
/* 283 */     if (this.picturesInPictureStates == null)
/* 284 */       this.picturesInPictureStates = new ArrayList<>(); 
/* 286 */     this.picturesInPictureStates.add(picturesInPictureState);
/*     */   }
/*     */   
/*     */   public void addGuiElement(GuiElementRenderState blitState) {
/* 290 */     if (this.elementStates == null)
/* 291 */       this.elementStates = new ArrayList<>(); 
/* 293 */     this.elementStates.add(blitState);
/*     */   }
/*     */   
/*     */   public void addGlyph(GuiElementRenderState glyphState) {
/* 297 */     if (this.glyphStates == null)
/* 298 */       this.glyphStates = new ArrayList<>(); 
/* 300 */     this.glyphStates.add(glyphState);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\gui\GuiRenderState$Node.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */