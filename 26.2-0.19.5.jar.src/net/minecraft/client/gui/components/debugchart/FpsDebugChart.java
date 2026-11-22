/*    */ package net.minecraft.client.gui.components.debugchart;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.util.debugchart.SampleStorage;
/*    */ 
/*    */ public class FpsDebugChart extends AbstractDebugChart {
/*    */   private static final int CHART_TOP_FPS = 30;
/*    */   
/*    */   private static final double CHART_TOP_VALUE = 33.333333333333336D;
/*    */   
/*    */   public FpsDebugChart(Font font, SampleStorage sampleStorage) {
/* 17 */     super(font, sampleStorage);
/*    */   }
/*    */   
/*    */   protected void extractAdditionalLinesAndLabels(GuiGraphicsExtractor graphics, int left, int width, int bottom) {
/* 22 */     extractStringWithShade(graphics, "30 FPS", left + 1, bottom - 60 + 1);
/* 23 */     extractStringWithShade(graphics, "60 FPS", left + 1, bottom - 30 + 1);
/* 24 */     graphics.horizontalLine(left, left + width - 1, bottom - 30, -1);
/* 26 */     int framerateLimit = ((Integer)(Minecraft.getInstance()).options.framerateLimit().get()).intValue();
/* 27 */     if (framerateLimit > 0 && framerateLimit <= 250)
/* 28 */       graphics.horizontalLine(left, left + width - 1, bottom - getSampleHeight(1.0E9D / framerateLimit) - 1, -16711681); 
/*    */   }
/*    */   
/*    */   protected String toDisplayString(double nanos) {
/* 34 */     return String.format(Locale.ROOT, "%d ms", new Object[] { Integer.valueOf((int)Math.round(toMilliseconds(nanos))) });
/*    */   }
/*    */   
/*    */   protected int getSampleHeight(double nanos) {
/* 39 */     return (int)Math.round(toMilliseconds(nanos) * 60.0D / 33.333333333333336D);
/*    */   }
/*    */   
/*    */   protected int getSampleColor(long nanos) {
/* 44 */     return getSampleColor(toMilliseconds(nanos), 0.0D, -16711936, 28.0D, -256, 56.0D, -65536);
/*    */   }
/*    */   
/*    */   private static double toMilliseconds(double nanos) {
/* 48 */     return nanos / 1000000.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debugchart\FpsDebugChart.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */