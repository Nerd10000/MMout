/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Region<T extends Stitcher.Entry> {
/*     */   private final int originX;
/*     */   
/*     */   private final int originY;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private List<Region<T>> subSlots;
/*     */   
/*     */   private Stitcher.Holder<T> holder;
/*     */   
/*     */   public Region(int originX, int originY, int width, int height) {
/* 147 */     this.originX = originX;
/* 148 */     this.originY = originY;
/* 149 */     this.width = width;
/* 150 */     this.height = height;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 154 */     return this.originX;
/*     */   }
/*     */   
/*     */   public int getY() {
/* 158 */     return this.originY;
/*     */   }
/*     */   
/*     */   public boolean add(Stitcher.Holder<T> holder) {
/* 163 */     if (this.holder != null)
/* 164 */       return false; 
/* 167 */     int textureWidth = holder.width;
/* 168 */     int textureHeight = holder.height;
/* 171 */     if (textureWidth > this.width || textureHeight > this.height)
/* 172 */       return false; 
/* 176 */     if (textureWidth == this.width && textureHeight == this.height) {
/* 178 */       this.holder = holder;
/* 179 */       return true;
/*     */     } 
/* 183 */     if (this.subSlots == null) {
/* 184 */       this.subSlots = new ArrayList<>(1);
/* 187 */       this.subSlots.add(new Region(this.originX, this.originY, textureWidth, textureHeight));
/* 189 */       int spareWidth = this.width - textureWidth;
/* 190 */       int spareHeight = this.height - textureHeight;
/* 192 */       if (spareHeight > 0 && spareWidth > 0) {
/* 207 */         int right = Math.max(this.height, spareWidth);
/* 208 */         int bottom = Math.max(this.width, spareHeight);
/* 209 */         if (right >= bottom) {
/* 210 */           this.subSlots.add(new Region(this.originX, this.originY + textureHeight, textureWidth, spareHeight));
/* 211 */           this.subSlots.add(new Region(this.originX + textureWidth, this.originY, spareWidth, this.height));
/*     */         } else {
/* 213 */           this.subSlots.add(new Region(this.originX + textureWidth, this.originY, spareWidth, textureHeight));
/* 214 */           this.subSlots.add(new Region(this.originX, this.originY + textureHeight, this.width, spareHeight));
/*     */         } 
/* 216 */       } else if (spareWidth == 0) {
/* 226 */         this.subSlots.add(new Region(this.originX, this.originY + textureHeight, textureWidth, spareHeight));
/* 227 */       } else if (spareHeight == 0) {
/* 238 */         this.subSlots.add(new Region(this.originX + textureWidth, this.originY, spareWidth, textureHeight));
/*     */       } 
/*     */     } 
/* 242 */     for (Region<T> subSlot : this.subSlots) {
/* 243 */       if (subSlot.add(holder))
/* 244 */         return true; 
/*     */     } 
/* 248 */     return false;
/*     */   }
/*     */   
/*     */   public void walk(Stitcher.SpriteLoader<T> output, int padding) {
/* 252 */     if (this.holder != null) {
/* 253 */       output.load(this.holder.entry, getX(), getY(), padding);
/* 254 */     } else if (this.subSlots != null) {
/* 255 */       for (Region<T> subSlot : this.subSlots)
/* 256 */         subSlot.walk(output, padding); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 263 */     return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + String.valueOf(this.holder) + ", subSlots=" + String.valueOf(this.subSlots) + "}";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\Stitcher$Region.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */