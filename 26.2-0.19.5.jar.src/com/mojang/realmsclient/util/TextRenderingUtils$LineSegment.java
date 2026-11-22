/*     */ package com.mojang.realmsclient.util;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Objects;
/*     */ 
/*     */ public class LineSegment {
/*     */   private final String fullText;
/*     */   
/*     */   private final String linkTitle;
/*     */   
/*     */   private final String linkUrl;
/*     */   
/*     */   private LineSegment(String fullText) {
/*  58 */     this.fullText = fullText;
/*  59 */     this.linkTitle = null;
/*  60 */     this.linkUrl = null;
/*     */   }
/*     */   
/*     */   private LineSegment(String fullText, String linkTitle, String linkUrl) {
/*  64 */     this.fullText = fullText;
/*  65 */     this.linkTitle = linkTitle;
/*  66 */     this.linkUrl = linkUrl;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  71 */     if (this == o)
/*  72 */       return true; 
/*  74 */     if (o == null || getClass() != o.getClass())
/*  75 */       return false; 
/*  77 */     LineSegment segment = (LineSegment)o;
/*  78 */     return (Objects.equals(this.fullText, segment.fullText) && 
/*  79 */       Objects.equals(this.linkTitle, segment.linkTitle) && 
/*  80 */       Objects.equals(this.linkUrl, segment.linkUrl));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  85 */     return Objects.hash(new Object[] { this.fullText, this.linkTitle, this.linkUrl });
/*     */   }
/*     */   
/*     */   public String toString() {
/*  90 */     return "Segment{fullText='" + this.fullText + "', linkTitle='" + this.linkTitle + "', linkUrl='" + this.linkUrl + "'}";
/*     */   }
/*     */   
/*     */   public String renderedText() {
/*  98 */     return isLink() ? this.linkTitle : this.fullText;
/*     */   }
/*     */   
/*     */   public boolean isLink() {
/* 102 */     return (this.linkTitle != null);
/*     */   }
/*     */   
/*     */   public String getLinkUrl() {
/* 106 */     if (!isLink())
/* 107 */       throw new IllegalStateException("Not a link: " + String.valueOf(this)); 
/* 109 */     return this.linkUrl;
/*     */   }
/*     */   
/*     */   public static LineSegment link(String linkTitle, String linkUrl) {
/* 113 */     return new LineSegment(null, linkTitle, linkUrl);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static LineSegment text(String fullText) {
/* 118 */     return new LineSegment(fullText);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclien\\util\TextRenderingUtils$LineSegment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */