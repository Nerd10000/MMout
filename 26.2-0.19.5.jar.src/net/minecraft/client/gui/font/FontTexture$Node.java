/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import com.mojang.blaze3d.font.GlyphBitmap;
/*     */ 
/*     */ class Node {
/*     */   private final int x;
/*     */   
/*     */   private final int y;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private Node left;
/*     */   
/*     */   private Node right;
/*     */   
/*     */   private boolean occupied;
/*     */   
/*     */   private Node(int x, int y, int width, int height) {
/*  92 */     this.x = x;
/*  93 */     this.y = y;
/*  94 */     this.width = width;
/*  95 */     this.height = height;
/*     */   }
/*     */   
/*     */   public Node insert(GlyphBitmap glyph) {
/*  99 */     if (this.left != null && this.right != null) {
/* 100 */       Node newNode = this.left.insert(glyph);
/* 101 */       if (newNode == null)
/* 102 */         newNode = this.right.insert(glyph); 
/* 104 */       return newNode;
/*     */     } 
/* 107 */     if (this.occupied)
/* 108 */       return null; 
/* 110 */     int glyphWidth = glyph.getPixelWidth();
/* 111 */     int glyphHeight = glyph.getPixelHeight();
/* 113 */     if (glyphWidth > this.width || glyphHeight > this.height)
/* 114 */       return null; 
/* 116 */     if (glyphWidth == this.width && glyphHeight == this.height) {
/* 117 */       this.occupied = true;
/* 118 */       return this;
/*     */     } 
/* 121 */     int deltaWidth = this.width - glyphWidth;
/* 122 */     int deltaHeight = this.height - glyphHeight;
/* 124 */     if (deltaWidth > deltaHeight) {
/* 125 */       this.left = new Node(this.x, this.y, glyphWidth, this.height);
/* 126 */       this.right = new Node(this.x + glyphWidth + 1, this.y, this.width - glyphWidth - 1, this.height);
/*     */     } else {
/* 128 */       this.left = new Node(this.x, this.y, this.width, glyphHeight);
/* 129 */       this.right = new Node(this.x, this.y + glyphHeight + 1, this.width, this.height - glyphHeight - 1);
/*     */     } 
/* 131 */     return this.left.insert(glyph);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\FontTexture$Node.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */