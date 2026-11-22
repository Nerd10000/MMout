/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ 
/*    */ public class QuadInstance {
/*  9 */   private int color0 = -1;
/*    */   
/* 10 */   private int color1 = -1;
/*    */   
/* 11 */   private int color2 = -1;
/*    */   
/* 12 */   private int color3 = -1;
/*    */   
/* 13 */   private int lightCoords0 = 15728880;
/*    */   
/* 14 */   private int lightCoords1 = 15728880;
/*    */   
/* 15 */   private int lightCoords2 = 15728880;
/*    */   
/* 16 */   private int lightCoords3 = 15728880;
/*    */   
/* 17 */   private int overlayCoords = OverlayTexture.NO_OVERLAY;
/*    */   
/*    */   public int getColor(int vertex) {
/* 20 */     switch (vertex) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */     } 
/* 25 */     throw new IndexOutOfBoundsException();
/*    */   }
/*    */   
/*    */   public int getLightCoords(int vertex) {
/* 30 */     switch (vertex) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */     } 
/* 35 */     throw new IndexOutOfBoundsException();
/*    */   }
/*    */   
/*    */   public int getLightCoordsWithEmission(int vertex, int lightEmission) {
/* 40 */     return LightCoordsUtil.lightCoordsWithEmission(getLightCoords(vertex), lightEmission);
/*    */   }
/*    */   
/*    */   public int overlayCoords() {
/* 44 */     return this.overlayCoords;
/*    */   }
/*    */   
/*    */   public void setColor(int vertex, int color) {
/* 48 */     switch (vertex) {
/*    */       case 0:
/* 49 */         this.color0 = color;
/*    */         return;
/*    */       case 1:
/* 50 */         this.color1 = color;
/*    */         return;
/*    */       case 2:
/* 51 */         this.color2 = color;
/*    */         return;
/*    */       case 3:
/* 52 */         this.color3 = color;
/*    */         return;
/*    */     } 
/* 53 */     throw new IndexOutOfBoundsException();
/*    */   }
/*    */   
/*    */   public void setLightCoords(int vertex, int lightCoords) {
/* 58 */     switch (vertex) {
/*    */       case 0:
/* 59 */         this.lightCoords0 = lightCoords;
/*    */         return;
/*    */       case 1:
/* 60 */         this.lightCoords1 = lightCoords;
/*    */         return;
/*    */       case 2:
/* 61 */         this.lightCoords2 = lightCoords;
/*    */         return;
/*    */       case 3:
/* 62 */         this.lightCoords3 = lightCoords;
/*    */         return;
/*    */     } 
/* 63 */     throw new IndexOutOfBoundsException();
/*    */   }
/*    */   
/*    */   public void setColor(int color) {
/* 68 */     this.color0 = color;
/* 69 */     this.color1 = color;
/* 70 */     this.color2 = color;
/* 71 */     this.color3 = color;
/*    */   }
/*    */   
/*    */   public void setLightCoords(int lightCoords) {
/* 75 */     this.lightCoords0 = lightCoords;
/* 76 */     this.lightCoords1 = lightCoords;
/* 77 */     this.lightCoords2 = lightCoords;
/* 78 */     this.lightCoords3 = lightCoords;
/*    */   }
/*    */   
/*    */   public void setOverlayCoords(int overlayCoords) {
/* 82 */     this.overlayCoords = overlayCoords;
/*    */   }
/*    */   
/*    */   public void multiplyColor(int color) {
/* 86 */     this.color0 = ARGB.multiply(this.color0, color);
/* 87 */     this.color1 = ARGB.multiply(this.color1, color);
/* 88 */     this.color2 = ARGB.multiply(this.color2, color);
/* 89 */     this.color3 = ARGB.multiply(this.color3, color);
/*    */   }
/*    */   
/*    */   public void scaleColor(float scale) {
/* 93 */     this.color0 = ARGB.scaleRGB(this.color0, scale);
/* 94 */     this.color1 = ARGB.scaleRGB(this.color1, scale);
/* 95 */     this.color2 = ARGB.scaleRGB(this.color2, scale);
/* 96 */     this.color3 = ARGB.scaleRGB(this.color3, scale);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\QuadInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */