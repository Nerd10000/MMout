/*     */ package net.minecraft.client.renderer.state.gui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.render.TextureSetup;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
/*     */ import org.joml.Matrix3x2f;
/*     */ import org.joml.Matrix3x2fc;
/*     */ import org.joml.Vector4f;
/*     */ 
/*     */ public class GuiRenderState {
/*     */   private static final int DEBUG_RECTANGLE_COLOR = 2000962815;
/*     */   
/*  25 */   private final List<Node> strata = new ArrayList<>();
/*     */   
/*  26 */   private int firstStratumAfterBlur = Integer.MAX_VALUE;
/*     */   
/*     */   private Node current;
/*     */   
/*  29 */   private final Set<Object> itemModelIdentities = new HashSet();
/*     */   
/*     */   private ScreenRectangle lastElementBounds;
/*     */   
/*     */   public PanoramaRenderState panoramaRenderState;
/*     */   
/*  34 */   public Vector4f clearColorOverride = new Vector4f(0.0F);
/*     */   
/*     */   public boolean isHudHidden;
/*     */   
/*     */   public GuiRenderState() {
/*  38 */     nextStratum();
/*     */   }
/*     */   
/*     */   public void nextStratum() {
/*  42 */     this.current = new Node(null);
/*  43 */     this.strata.add(this.current);
/*     */   }
/*     */   
/*     */   public void blurBeforeThisStratum() {
/*  47 */     if (this.firstStratumAfterBlur != Integer.MAX_VALUE)
/*  48 */       throw new IllegalStateException("Can only blur once per frame"); 
/*  50 */     this.firstStratumAfterBlur = this.strata.size() - 1;
/*     */   }
/*     */   
/*     */   public void up() {
/*  54 */     if (this.current.up == null)
/*  55 */       this.current.up = new Node(this.current); 
/*  57 */     this.current = this.current.up;
/*     */   }
/*     */   
/*     */   public void addItem(GuiItemRenderState itemState) {
/*  61 */     if (!findAppropriateNode(itemState))
/*     */       return; 
/*  64 */     this.itemModelIdentities.add(itemState.itemStackRenderState().getModelIdentity());
/*  65 */     this.current.addItem(itemState);
/*  66 */     addDebugRectangleIfEnabled(itemState.bounds());
/*     */   }
/*     */   
/*     */   public void addText(GuiTextRenderState textState) {
/*  70 */     if (!findAppropriateNode(textState))
/*     */       return; 
/*  73 */     this.current.addText(textState);
/*  74 */     addDebugRectangleIfEnabled(textState.bounds());
/*     */   }
/*     */   
/*     */   public void addPicturesInPictureState(PictureInPictureRenderState picturesInPictureState) {
/*  78 */     if (!findAppropriateNode((ScreenArea)picturesInPictureState))
/*     */       return; 
/*  81 */     this.current.addPicturesInPictureState(picturesInPictureState);
/*  82 */     addDebugRectangleIfEnabled(picturesInPictureState.bounds());
/*     */   }
/*     */   
/*     */   public void addGuiElement(GuiElementRenderState blitState) {
/*  86 */     if (!findAppropriateNode(blitState))
/*     */       return; 
/*  89 */     this.current.addGuiElement(blitState);
/*  90 */     addDebugRectangleIfEnabled(blitState.bounds());
/*     */   }
/*     */   
/*     */   private void addDebugRectangleIfEnabled(ScreenRectangle bounds) {
/*  94 */     if (!SharedConstants.DEBUG_RENDER_UI_LAYERING_RECTANGLES || bounds == null)
/*     */       return; 
/*  97 */     up();
/*  98 */     this.current.addGuiElement(new ColoredRectangleRenderState(RenderPipelines.GUI, TextureSetup.noTexture(), (Matrix3x2fc)new Matrix3x2f(), 0, 0, 10000, 10000, 2000962815, 2000962815, bounds));
/*     */   }
/*     */   
/*     */   private boolean findAppropriateNode(ScreenArea screenArea) {
/* 102 */     ScreenRectangle bounds = screenArea.bounds();
/* 103 */     if (bounds == null)
/* 104 */       return false; 
/* 106 */     if (this.lastElementBounds != null && this.lastElementBounds.encompasses(bounds)) {
/* 107 */       up();
/*     */     } else {
/* 109 */       navigateToAboveHighestElementWithIntersectingBounds(bounds);
/*     */     } 
/* 111 */     this.lastElementBounds = bounds;
/* 112 */     return true;
/*     */   }
/*     */   
/*     */   private void navigateToAboveHighestElementWithIntersectingBounds(ScreenRectangle bounds) {
/* 116 */     Node node = this.strata.getLast();
/* 117 */     while (node.up != null)
/* 118 */       node = node.up; 
/* 120 */     boolean found = false;
/* 121 */     while (!found) {
/* 125 */       found = (hasIntersection(bounds, (List)node.elementStates) || hasIntersection(bounds, (List)node.itemStates) || hasIntersection(bounds, (List)node.textStates) || hasIntersection(bounds, (List)node.picturesInPictureStates));
/* 126 */       if (node.parent == null)
/*     */         break; 
/* 129 */       if (!found)
/* 130 */         node = node.parent; 
/*     */     } 
/* 133 */     this.current = node;
/* 134 */     if (found)
/* 135 */       up(); 
/*     */   }
/*     */   
/*     */   private boolean hasIntersection(ScreenRectangle bounds, List<? extends ScreenArea> states) {
/* 140 */     if (states != null)
/* 141 */       for (ScreenArea area : states) {
/* 142 */         ScreenRectangle existingBounds = area.bounds();
/* 143 */         if (existingBounds != null && existingBounds.intersects(bounds))
/* 144 */           return true; 
/*     */       }  
/* 148 */     return false;
/*     */   }
/*     */   
/*     */   public void addBlitToCurrentLayer(BlitRenderState blitState) {
/* 152 */     this.current.addGuiElement(blitState);
/*     */   }
/*     */   
/*     */   public void addGlyphToCurrentLayer(GuiElementRenderState glyphState) {
/* 156 */     this.current.addGlyph(glyphState);
/*     */   }
/*     */   
/*     */   public Set<Object> getItemModelIdentities() {
/* 160 */     return this.itemModelIdentities;
/*     */   }
/*     */   
/*     */   public void forEachElement(Consumer<GuiElementRenderState> consumer, TraverseRange range) {
/* 164 */     traverse(node -> {
/*     */           if (node.elementStates == null && node.glyphStates == null)
/*     */             return; 
/*     */           if (node.elementStates != null)
/*     */             for (GuiElementRenderState elementState : node.elementStates)
/*     */               consumer.accept(elementState);  
/*     */           if (node.glyphStates != null)
/*     */             for (GuiElementRenderState glyphState : node.glyphStates)
/*     */               consumer.accept(glyphState);  
/* 164 */         }range);
/*     */   }
/*     */   
/*     */   public void forEachItem(Consumer<GuiItemRenderState> consumer) {
/* 182 */     Node currentBackup = this.current;
/* 183 */     traverse(node -> {
/*     */           if (node.itemStates != null) {
/*     */             this.current = node;
/*     */             for (GuiItemRenderState itemState : node.itemStates)
/*     */               consumer.accept(itemState); 
/*     */           } 
/* 183 */         }TraverseRange.ALL);
/* 191 */     this.current = currentBackup;
/*     */   }
/*     */   
/*     */   public void forEachText(Consumer<GuiTextRenderState> consumer) {
/* 195 */     Node currentBackup = this.current;
/* 196 */     traverse(node -> {
/*     */           if (node.textStates != null)
/*     */             for (GuiTextRenderState textState : node.textStates) {
/*     */               this.current = node;
/*     */               consumer.accept(textState);
/*     */             }  
/* 196 */         }TraverseRange.ALL);
/* 204 */     this.current = currentBackup;
/*     */   }
/*     */   
/*     */   public void forEachPictureInPicture(Consumer<PictureInPictureRenderState> consumer) {
/* 208 */     Node currentBackup = this.current;
/* 209 */     traverse(node -> {
/*     */           if (node.picturesInPictureStates != null) {
/*     */             this.current = node;
/*     */             for (PictureInPictureRenderState pictureInPictureState : node.picturesInPictureStates)
/*     */               consumer.accept(pictureInPictureState); 
/*     */           } 
/* 209 */         }TraverseRange.ALL);
/* 217 */     this.current = currentBackup;
/*     */   }
/*     */   
/*     */   public void sortElements(Comparator<GuiElementRenderState> comparator) {
/* 221 */     traverse(node -> {
/*     */           if (node.elementStates != null) {
/*     */             if (SharedConstants.DEBUG_SHUFFLE_UI_RENDERING_ORDER)
/*     */               Collections.shuffle(node.elementStates); 
/*     */             node.elementStates.sort(comparator);
/*     */           } 
/* 221 */         }TraverseRange.ALL);
/*     */   }
/*     */   
/*     */   private void traverse(Consumer<Node> consumer, TraverseRange range) {
/* 232 */     int startIndex = 0;
/* 233 */     int endIndex = this.strata.size();
/* 235 */     if (range == TraverseRange.BEFORE_BLUR) {
/* 236 */       endIndex = Math.min(this.firstStratumAfterBlur, this.strata.size());
/* 237 */     } else if (range == TraverseRange.AFTER_BLUR) {
/* 238 */       startIndex = this.firstStratumAfterBlur;
/*     */     } 
/* 241 */     for (int i = startIndex; i < endIndex; i++) {
/* 242 */       Node stratum = this.strata.get(i);
/* 243 */       traverse(stratum, consumer);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void traverse(Node node, Consumer<Node> consumer) {
/* 248 */     consumer.accept(node);
/* 249 */     if (node.up != null)
/* 250 */       traverse(node.up, consumer); 
/*     */   }
/*     */   
/*     */   private static class Node {
/*     */     public final Node parent;
/*     */     
/*     */     public Node up;
/*     */     
/*     */     public List<GuiElementRenderState> elementStates;
/*     */     
/*     */     public List<GuiElementRenderState> glyphStates;
/*     */     
/*     */     public List<GuiItemRenderState> itemStates;
/*     */     
/*     */     public List<GuiTextRenderState> textStates;
/*     */     
/*     */     public List<PictureInPictureRenderState> picturesInPictureStates;
/*     */     
/*     */     private Node(Node parent) {
/* 265 */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void addItem(GuiItemRenderState itemState) {
/* 269 */       if (this.itemStates == null)
/* 270 */         this.itemStates = new ArrayList<>(); 
/* 272 */       this.itemStates.add(itemState);
/*     */     }
/*     */     
/*     */     public void addText(GuiTextRenderState textState) {
/* 276 */       if (this.textStates == null)
/* 277 */         this.textStates = new ArrayList<>(); 
/* 279 */       this.textStates.add(textState);
/*     */     }
/*     */     
/*     */     public void addPicturesInPictureState(PictureInPictureRenderState picturesInPictureState) {
/* 283 */       if (this.picturesInPictureStates == null)
/* 284 */         this.picturesInPictureStates = new ArrayList<>(); 
/* 286 */       this.picturesInPictureStates.add(picturesInPictureState);
/*     */     }
/*     */     
/*     */     public void addGuiElement(GuiElementRenderState blitState) {
/* 290 */       if (this.elementStates == null)
/* 291 */         this.elementStates = new ArrayList<>(); 
/* 293 */       this.elementStates.add(blitState);
/*     */     }
/*     */     
/*     */     public void addGlyph(GuiElementRenderState glyphState) {
/* 297 */       if (this.glyphStates == null)
/* 298 */         this.glyphStates = new ArrayList<>(); 
/* 300 */       this.glyphStates.add(glyphState);
/*     */     }
/*     */   }
/*     */   
/*     */   public void reset() {
/* 305 */     this.itemModelIdentities.clear();
/* 306 */     this.strata.clear();
/* 307 */     this.firstStratumAfterBlur = Integer.MAX_VALUE;
/* 308 */     nextStratum();
/* 309 */     this.panoramaRenderState = null;
/* 310 */     this.clearColorOverride.set(0.0F);
/*     */   }
/*     */   
/*     */   public enum TraverseRange {
/* 314 */     ALL, BEFORE_BLUR, AFTER_BLUR;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\gui\GuiRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */