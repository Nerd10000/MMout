/*     */ package net.minecraft.client.gui.components.debugchart;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.util.profiling.ProfileResults;
/*     */ import net.minecraft.util.profiling.ResultField;
/*     */ 
/*     */ public class ProfilerPieChart {
/*     */   public static final int RADIUS = 105;
/*     */   
/*     */   public static final int PIE_CHART_THICKNESS = 10;
/*     */   
/*  21 */   private static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("##0.00", DecimalFormatSymbols.getInstance(Locale.ROOT));
/*     */   
/*     */   private static final int MARGIN = 5;
/*     */   
/*     */   private static final int WIDTH = 260;
/*     */   
/*     */   private static final int SUBSEQUENT_LINES_INDENT = 10;
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   private ProfileResults profilerPieChartResults;
/*     */   
/*  28 */   private String profilerTreePath = "root";
/*     */   
/*  29 */   private int bottomOffset = 0;
/*     */   
/*     */   public ProfilerPieChart(Font font) {
/*  32 */     this.font = font;
/*     */   }
/*     */   
/*     */   public void setPieChartResults(ProfileResults results) {
/*  36 */     this.profilerPieChartResults = results;
/*     */   }
/*     */   
/*     */   public void setBottomOffset(int bottomOffset) {
/*  40 */     this.bottomOffset = bottomOffset;
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics) {
/*  44 */     if (this.profilerPieChartResults == null)
/*     */       return; 
/*  48 */     List<ResultField> list = this.profilerPieChartResults.getTimes(this.profilerTreePath);
/*  49 */     ResultField currentNode = list.removeFirst();
/*  51 */     int chartCenterX = graphics.guiWidth() - 130 - 10;
/*  52 */     int left = chartCenterX - 130;
/*  53 */     int right = chartCenterX + 130;
/*  55 */     Objects.requireNonNull(this.font);
/*  55 */     int textUnderChartHeight = list.size() * 9;
/*  56 */     int bottom = graphics.guiHeight() - this.bottomOffset - 5;
/*  57 */     int textStartY = bottom - textUnderChartHeight;
/*  58 */     int chartHalfSizeY = 62;
/*  59 */     int chartCenterY = textStartY - 62 - 5;
/*  61 */     String globalPercentage = PERCENTAGE_FORMAT.format(currentNode.globalPercentage) + "%";
/*  62 */     int globalPercentageWidth = this.font.width(globalPercentage);
/*  63 */     int zeroPrefixWidth = this.font.width("[0] ");
/*  64 */     int topTextMaxWidth = right - globalPercentageWidth - 5 - left - zeroPrefixWidth;
/*  66 */     String currentNodeName = ProfileResults.demanglePath(currentNode.name);
/*  67 */     List<String> currentNodeNameLines = splitNodeName(currentNodeName, topTextMaxWidth, topTextMaxWidth - 10);
/*  69 */     Objects.requireNonNull(this.font);
/*  69 */     int currentNodeNameTop = chartCenterY - 62 - (currentNodeNameLines.size() - 1) * 9;
/*  71 */     graphics.fill(left - 5, currentNodeNameTop - 5, right + 5, bottom + 5, -1873784752);
/*  73 */     graphics.profilerChart(list, left, chartCenterY - 62 + 10, right, chartCenterY + 62);
/*  75 */     String firstLineText = "";
/*  76 */     if (!"unspecified".equals(currentNodeName) && !"root".equals(currentNodeName))
/*  77 */       firstLineText = firstLineText + "[0] "; 
/*  79 */     firstLineText = firstLineText + firstLineText;
/*  81 */     int col = -1;
/*  82 */     graphics.text(this.font, firstLineText, left, currentNodeNameTop, -1);
/*     */     int i;
/*  84 */     for (i = 1; i < currentNodeNameLines.size(); i++) {
/*  85 */       Objects.requireNonNull(this.font);
/*  85 */       graphics.text(this.font, currentNodeNameLines.get(i), left + 10 + zeroPrefixWidth, currentNodeNameTop + i * 9, -1);
/*     */     } 
/*  88 */     graphics.text(this.font, globalPercentage, right - globalPercentageWidth, currentNodeNameTop, -1);
/*  90 */     for (i = 0; i < list.size(); i++) {
/*  91 */       ResultField result = list.get(i);
/*  92 */       StringBuilder string = new StringBuilder();
/*  93 */       if ("unspecified".equals(result.name)) {
/*  94 */         string.append("[?] ");
/*     */       } else {
/*  96 */         string.append("[").append(i + 1).append("] ");
/*     */       } 
/*  99 */       String msg = string.append(result.name).toString();
/* 100 */       Objects.requireNonNull(this.font);
/* 100 */       int textY = textStartY + i * 9;
/* 101 */       graphics.text(this.font, msg, left, textY, result.getColor());
/* 102 */       msg = PERCENTAGE_FORMAT.format(result.percentage) + "%";
/* 103 */       graphics.text(this.font, msg, right - 50 - this.font.width(msg), textY, result.getColor());
/* 104 */       msg = PERCENTAGE_FORMAT.format(result.globalPercentage) + "%";
/* 105 */       graphics.text(this.font, msg, right - this.font.width(msg), textY, result.getColor());
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<String> splitNodeName(String nodeName, int firstLineMaxWidth, int maxWidth) {
/* 110 */     String[] nodeNameSplit = nodeName.split("\\.");
/* 112 */     List<String> lines = new ArrayList<>();
/* 114 */     String currentLine = "";
/* 115 */     int nameIndex = 0;
/* 116 */     while (nameIndex < nodeNameSplit.length) {
/* 117 */       String currentName = nodeNameSplit[nameIndex];
/* 118 */       String currentNameWithPeriod = ((nameIndex != 0) ? "." : "") + ((nameIndex != 0) ? "." : "");
/* 119 */       String newLine = currentLine + currentLine;
/* 120 */       int newWidth = this.font.width(newLine);
/* 121 */       if (newWidth > (!lines.isEmpty() ? maxWidth : firstLineMaxWidth)) {
/* 122 */         if (currentLine.isEmpty()) {
/* 123 */           lines.add(currentNameWithPeriod);
/* 124 */           nameIndex++;
/*     */           continue;
/*     */         } 
/* 127 */         lines.add(currentLine);
/* 128 */         currentLine = "";
/*     */         continue;
/*     */       } 
/* 131 */       currentLine = newLine;
/* 132 */       nameIndex++;
/*     */     } 
/* 134 */     if (!currentLine.isEmpty())
/* 135 */       lines.add(currentLine); 
/* 137 */     return lines;
/*     */   }
/*     */   
/*     */   public void profilerPieChartKeyPress(int key) {
/* 141 */     if (this.profilerPieChartResults == null)
/*     */       return; 
/* 144 */     List<ResultField> list = this.profilerPieChartResults.getTimes(this.profilerTreePath);
/* 145 */     if (list.isEmpty())
/*     */       return; 
/* 149 */     ResultField node = list.remove(0);
/* 150 */     if (key == 0) {
/* 151 */       if (!node.name.isEmpty()) {
/* 152 */         int pos = this.profilerTreePath.lastIndexOf('\036');
/* 153 */         if (pos >= 0)
/* 154 */           this.profilerTreePath = this.profilerTreePath.substring(0, pos); 
/*     */       } 
/*     */     } else {
/* 158 */       key--;
/* 159 */       if (key < list.size() && !"unspecified".equals(((ResultField)list.get(key)).name)) {
/* 160 */         if (!this.profilerTreePath.isEmpty())
/* 161 */           this.profilerTreePath += "\036"; 
/* 163 */         this.profilerTreePath += this.profilerTreePath;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debugchart\ProfilerPieChart.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */