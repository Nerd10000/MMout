/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ import com.mojang.math.Divisor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class EqualSpacingLayout extends AbstractLayout {
/*     */   private final Orientation orientation;
/*     */   
/*  22 */   private final List<ChildContainer> children = new ArrayList<>();
/*     */   
/*  24 */   private final LayoutSettings defaultChildLayoutSettings = LayoutSettings.defaults();
/*     */   
/*     */   public EqualSpacingLayout(int width, int height, Orientation orientation) {
/*  27 */     this(0, 0, width, height, orientation);
/*     */   }
/*     */   
/*     */   public EqualSpacingLayout(int x, int y, int width, int height, Orientation orientation) {
/*  31 */     super(x, y, width, height);
/*  32 */     this.orientation = orientation;
/*     */   }
/*     */   
/*     */   public void arrangeElements() {
/*  37 */     super.arrangeElements();
/*  39 */     if (this.children.isEmpty())
/*     */       return; 
/*  43 */     int totalChildPrimaryLength = 0;
/*  44 */     int maxChildSecondaryLength = this.orientation.getSecondaryLength(this);
/*  46 */     for (ChildContainer child : this.children) {
/*  47 */       totalChildPrimaryLength += this.orientation.getPrimaryLength(child);
/*  48 */       maxChildSecondaryLength = Math.max(maxChildSecondaryLength, this.orientation.getSecondaryLength(child));
/*     */     } 
/*  51 */     int remainingSpace = this.orientation.getPrimaryLength(this) - totalChildPrimaryLength;
/*  53 */     int position = this.orientation.getPrimaryPosition(this);
/*  55 */     Iterator<ChildContainer> childIterator = this.children.iterator();
/*  56 */     ChildContainer firstChild = childIterator.next();
/*  57 */     this.orientation.setPrimaryPosition(firstChild, position);
/*  58 */     position += this.orientation.getPrimaryLength(firstChild);
/*  59 */     if (this.children.size() >= 2) {
/*  60 */       Divisor divisor = new Divisor(remainingSpace, this.children.size() - 1);
/*  61 */       while (divisor.hasNext()) {
/*  62 */         position += divisor.nextInt();
/*  63 */         ChildContainer child = childIterator.next();
/*  64 */         this.orientation.setPrimaryPosition(child, position);
/*  65 */         position += this.orientation.getPrimaryLength(child);
/*     */       } 
/*     */     } 
/*  69 */     int thisSecondaryPosition = this.orientation.getSecondaryPosition(this);
/*  70 */     for (ChildContainer child : this.children)
/*  71 */       this.orientation.setSecondaryPosition(child, thisSecondaryPosition, maxChildSecondaryLength); 
/*  74 */     switch (this.orientation.ordinal()) {
/*     */       case 0:
/*  75 */         this.height = maxChildSecondaryLength;
/*     */         break;
/*     */       case 1:
/*  76 */         this.width = maxChildSecondaryLength;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void visitChildren(Consumer<LayoutElement> layoutElementVisitor) {
/*  82 */     this.children.forEach(wrapper -> layoutElementVisitor.accept(wrapper.child));
/*     */   }
/*     */   
/*     */   public void removeChildren() {
/*  87 */     this.children.clear();
/*     */   }
/*     */   
/*     */   public LayoutSettings newChildLayoutSettings() {
/*  91 */     return this.defaultChildLayoutSettings.copy();
/*     */   }
/*     */   
/*     */   public LayoutSettings defaultChildLayoutSetting() {
/*  95 */     return this.defaultChildLayoutSettings;
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child) {
/*  99 */     return addChild(child, newChildLayoutSettings());
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, LayoutSettings layoutSettings) {
/* 103 */     this.children.add(new ChildContainer((LayoutElement)child, layoutSettings));
/* 104 */     return child;
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/* 108 */     return addChild(child, (LayoutSettings)Util.make(newChildLayoutSettings(), layoutSettingsAdjustments));
/*     */   }
/*     */   
/*     */   public enum Orientation {
/* 112 */     HORIZONTAL, VERTICAL;
/*     */     
/*     */     private int getPrimaryLength(LayoutElement widget) {
/* 115 */       switch (ordinal()) {
/*     */         default:
/* 115 */           throw new MatchException(null, null);
/*     */         case 0:
/*     */         
/*     */         case 1:
/*     */           break;
/*     */       } 
/* 115 */       return 
/*     */         
/* 117 */         widget.getHeight();
/*     */     }
/*     */     
/*     */     private int getPrimaryLength(EqualSpacingLayout.ChildContainer childContainer) {
/* 122 */       switch (ordinal()) {
/*     */         default:
/* 122 */           throw new MatchException(null, null);
/*     */         case 0:
/*     */         
/*     */         case 1:
/*     */           break;
/*     */       } 
/* 122 */       return 
/*     */         
/* 124 */         childContainer.getHeight();
/*     */     }
/*     */     
/*     */     private int getSecondaryLength(LayoutElement widget) {
/* 129 */       switch (ordinal()) {
/*     */         default:
/* 129 */           throw new MatchException(null, null);
/*     */         case 0:
/*     */         
/*     */         case 1:
/*     */           break;
/*     */       } 
/* 129 */       return 
/*     */         
/* 131 */         widget.getWidth();
/*     */     }
/*     */     
/*     */     private int getSecondaryLength(EqualSpacingLayout.ChildContainer childContainer) {
/* 136 */       switch (ordinal()) {
/*     */         default:
/* 136 */           throw new MatchException(null, null);
/*     */         case 0:
/*     */         
/*     */         case 1:
/*     */           break;
/*     */       } 
/* 136 */       return 
/*     */         
/* 138 */         childContainer.getWidth();
/*     */     }
/*     */     
/*     */     private void setPrimaryPosition(EqualSpacingLayout.ChildContainer childContainer, int position) {
/* 143 */       switch (ordinal()) {
/*     */         case 0:
/* 144 */           childContainer.setX(position, childContainer.getWidth());
/*     */           break;
/*     */         case 1:
/* 145 */           childContainer.setY(position, childContainer.getHeight());
/*     */           break;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void setSecondaryPosition(EqualSpacingLayout.ChildContainer childContainer, int position, int availableSpace) {
/* 150 */       switch (ordinal()) {
/*     */         case 0:
/* 151 */           childContainer.setY(position, availableSpace);
/*     */           break;
/*     */         case 1:
/* 152 */           childContainer.setX(position, availableSpace);
/*     */           break;
/*     */       } 
/*     */     }
/*     */     
/*     */     private int getPrimaryPosition(LayoutElement widget) {
/* 157 */       switch (ordinal()) {
/*     */         default:
/* 157 */           throw new MatchException(null, null);
/*     */         case 0:
/*     */         
/*     */         case 1:
/*     */           break;
/*     */       } 
/* 157 */       return 
/*     */         
/* 159 */         widget.getY();
/*     */     }
/*     */     
/*     */     private int getSecondaryPosition(LayoutElement widget) {
/* 164 */       switch (ordinal()) {
/*     */         default:
/* 164 */           throw new MatchException(null, null);
/*     */         case 0:
/*     */         
/*     */         case 1:
/*     */           break;
/*     */       } 
/* 164 */       return 
/*     */         
/* 166 */         widget.getX();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ChildContainer extends AbstractLayout.AbstractChildWrapper {
/*     */     protected ChildContainer(LayoutElement child, LayoutSettings layoutSettings) {
/* 173 */       super(child, layoutSettings);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\layouts\EqualSpacingLayout.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */