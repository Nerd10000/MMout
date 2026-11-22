/*    */ package com.mojang.realmsclient.util;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class Line {
/*    */   public final List<TextRenderingUtils.LineSegment> segments;
/*    */   
/*    */   public Line(TextRenderingUtils.LineSegment... segments) {
/* 20 */     this(Arrays.asList(segments));
/*    */   }
/*    */   
/*    */   public Line(List<TextRenderingUtils.LineSegment> segments) {
/* 24 */     this.segments = segments;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 29 */     return "Line{segments=" + String.valueOf(this.segments) + "}";
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 36 */     if (this == o)
/* 37 */       return true; 
/* 39 */     if (o == null || getClass() != o.getClass())
/* 40 */       return false; 
/* 42 */     Line line = (Line)o;
/* 43 */     return Objects.equals(this.segments, line.segments);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 48 */     return Objects.hash(new Object[] { this.segments });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclien\\util\TextRenderingUtils$Line.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */