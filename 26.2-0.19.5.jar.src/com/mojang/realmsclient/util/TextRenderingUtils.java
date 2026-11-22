/*     */ package com.mojang.realmsclient.util;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ 
/*     */ public class TextRenderingUtils {
/*     */   public static class Line {
/*     */     public final List<TextRenderingUtils.LineSegment> segments;
/*     */     
/*     */     public Line(TextRenderingUtils.LineSegment... segments) {
/*  20 */       this(Arrays.asList(segments));
/*     */     }
/*     */     
/*     */     public Line(List<TextRenderingUtils.LineSegment> segments) {
/*  24 */       this.segments = segments;
/*     */     }
/*     */     
/*     */     public String toString() {
/*  29 */       return "Line{segments=" + String.valueOf(this.segments) + "}";
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/*  36 */       if (this == o)
/*  37 */         return true; 
/*  39 */       if (o == null || getClass() != o.getClass())
/*  40 */         return false; 
/*  42 */       Line line = (Line)o;
/*  43 */       return Objects.equals(this.segments, line.segments);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/*  48 */       return Objects.hash(new Object[] { this.segments });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LineSegment {
/*     */     private final String fullText;
/*     */     
/*     */     private final String linkTitle;
/*     */     
/*     */     private final String linkUrl;
/*     */     
/*     */     private LineSegment(String fullText) {
/*  58 */       this.fullText = fullText;
/*  59 */       this.linkTitle = null;
/*  60 */       this.linkUrl = null;
/*     */     }
/*     */     
/*     */     private LineSegment(String fullText, String linkTitle, String linkUrl) {
/*  64 */       this.fullText = fullText;
/*  65 */       this.linkTitle = linkTitle;
/*  66 */       this.linkUrl = linkUrl;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/*  71 */       if (this == o)
/*  72 */         return true; 
/*  74 */       if (o == null || getClass() != o.getClass())
/*  75 */         return false; 
/*  77 */       LineSegment segment = (LineSegment)o;
/*  78 */       return (Objects.equals(this.fullText, segment.fullText) && 
/*  79 */         Objects.equals(this.linkTitle, segment.linkTitle) && 
/*  80 */         Objects.equals(this.linkUrl, segment.linkUrl));
/*     */     }
/*     */     
/*     */     public int hashCode() {
/*  85 */       return Objects.hash(new Object[] { this.fullText, this.linkTitle, this.linkUrl });
/*     */     }
/*     */     
/*     */     public String toString() {
/*  90 */       return "Segment{fullText='" + this.fullText + "', linkTitle='" + this.linkTitle + "', linkUrl='" + this.linkUrl + "'}";
/*     */     }
/*     */     
/*     */     public String renderedText() {
/*  98 */       return isLink() ? this.linkTitle : this.fullText;
/*     */     }
/*     */     
/*     */     public boolean isLink() {
/* 102 */       return (this.linkTitle != null);
/*     */     }
/*     */     
/*     */     public String getLinkUrl() {
/* 106 */       if (!isLink())
/* 107 */         throw new IllegalStateException("Not a link: " + String.valueOf(this)); 
/* 109 */       return this.linkUrl;
/*     */     }
/*     */     
/*     */     public static LineSegment link(String linkTitle, String linkUrl) {
/* 113 */       return new LineSegment(null, linkTitle, linkUrl);
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     protected static LineSegment text(String fullText) {
/* 118 */       return new LineSegment(fullText);
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static List<String> lineBreak(String text) {
/* 124 */     return Arrays.asList(text.split("\\n"));
/*     */   }
/*     */   
/*     */   public static List<Line> decompose(String text, LineSegment... links) {
/* 130 */     return decompose(text, Arrays.asList(links));
/*     */   }
/*     */   
/*     */   private static List<Line> decompose(String text, List<LineSegment> links) {
/* 134 */     List<String> brokenLines = lineBreak(text);
/* 135 */     return insertLinks(brokenLines, links);
/*     */   }
/*     */   
/*     */   private static List<Line> insertLinks(List<String> lines, List<LineSegment> links) {
/* 139 */     int linkCount = 0;
/* 140 */     List<Line> processedLines = Lists.newArrayList();
/* 141 */     for (String line : lines) {
/* 142 */       List<LineSegment> segments = Lists.newArrayList();
/* 143 */       List<String> parts = split(line, "%link");
/* 144 */       for (String part : parts) {
/* 145 */         if ("%link".equals(part)) {
/* 146 */           segments.add(links.get(linkCount++));
/*     */           continue;
/*     */         } 
/* 148 */         segments.add(LineSegment.text(part));
/*     */       } 
/* 151 */       processedLines.add(new Line(segments));
/*     */     } 
/* 153 */     return processedLines;
/*     */   }
/*     */   
/*     */   public static List<String> split(String line, String delimiter) {
/* 157 */     if (delimiter.isEmpty())
/* 158 */       throw new IllegalArgumentException("Delimiter cannot be the empty string"); 
/* 160 */     List<String> parts = Lists.newArrayList();
/* 161 */     int searchStart = 0;
/*     */     int matchIndex;
/* 162 */     while ((matchIndex = line.indexOf(delimiter, searchStart)) != -1) {
/* 163 */       if (matchIndex > searchStart)
/* 164 */         parts.add(line.substring(searchStart, matchIndex)); 
/* 166 */       parts.add(delimiter);
/* 167 */       searchStart = matchIndex + delimiter.length();
/*     */     } 
/* 169 */     if (searchStart < line.length())
/* 170 */       parts.add(line.substring(searchStart)); 
/* 172 */     return parts;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclien\\util\TextRenderingUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */