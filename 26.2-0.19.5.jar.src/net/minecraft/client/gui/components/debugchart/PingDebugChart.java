/*    */ package net.minecraft.client.gui.components.debugchart;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.util.debugchart.SampleStorage;
/*    */ 
/*    */ public class PingDebugChart extends AbstractDebugChart {
/*    */   private static final int CHART_TOP_VALUE = 500;
/*    */   
/*    */   public PingDebugChart(Font font, SampleStorage sampleStorage) {
/* 14 */     super(font, sampleStorage);
/*    */   }
/*    */   
/*    */   protected void extractAdditionalLinesAndLabels(GuiGraphicsExtractor graphics, int left, int width, int bottom) {
/* 19 */     extractStringWithShade(graphics, "500 ms", left + 1, bottom - 60 + 1);
/*    */   }
/*    */   
/*    */   protected String toDisplayString(double millis) {
/* 24 */     return String.format(Locale.ROOT, "%d ms", new Object[] { Integer.valueOf((int)Math.round(millis)) });
/*    */   }
/*    */   
/*    */   protected int getSampleHeight(double millis) {
/* 29 */     return (int)Math.round(millis * 60.0D / 500.0D);
/*    */   }
/*    */   
/*    */   protected int getSampleColor(long millis) {
/* 34 */     return getSampleColor(millis, 0.0D, -16711936, 250.0D, -256, 500.0D, -65536);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debugchart\PingDebugChart.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */