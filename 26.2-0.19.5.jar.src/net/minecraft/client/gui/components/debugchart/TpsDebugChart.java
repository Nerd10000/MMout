/*    */ package net.minecraft.client.gui.components.debugchart;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.util.TimeUtil;
/*    */ import net.minecraft.util.debugchart.SampleStorage;
/*    */ import net.minecraft.util.debugchart.TpsDebugDimensions;
/*    */ 
/*    */ public class TpsDebugChart extends AbstractDebugChart {
/*    */   private static final int TICK_METHOD_COLOR = -6745839;
/*    */   
/*    */   private static final int TASK_COLOR = -4548257;
/*    */   
/*    */   private static final int OTHER_COLOR = -10547572;
/*    */   
/*    */   private final Supplier<Float> msptSupplier;
/*    */   
/*    */   public TpsDebugChart(Font font, SampleStorage sampleStorage, Supplier<Float> msptSupplier) {
/* 21 */     super(font, sampleStorage);
/* 22 */     this.msptSupplier = msptSupplier;
/*    */   }
/*    */   
/*    */   protected void extractAdditionalLinesAndLabels(GuiGraphicsExtractor graphics, int left, int width, int bottom) {
/* 27 */     float tps = (float)TimeUtil.MILLISECONDS_PER_SECOND / ((Float)this.msptSupplier.get()).floatValue();
/* 28 */     extractStringWithShade(graphics, String.format(Locale.ROOT, "%.1f TPS", new Object[] { Float.valueOf(tps) }), left + 1, bottom - 60 + 1);
/*    */   }
/*    */   
/*    */   protected void extractAdditionalSampleBars(GuiGraphicsExtractor graphics, int bottom, int currentX, int sampleIndex) {
/* 33 */     long tickMethodTime = this.sampleStorage.get(sampleIndex, TpsDebugDimensions.TICK_SERVER_METHOD.ordinal());
/* 34 */     int tickMethodHeight = getSampleHeight(tickMethodTime);
/* 35 */     graphics.fill(currentX, bottom - tickMethodHeight, currentX + 1, bottom, -6745839);
/* 37 */     long tasksTime = this.sampleStorage.get(sampleIndex, TpsDebugDimensions.SCHEDULED_TASKS.ordinal());
/* 38 */     int tasksHeight = getSampleHeight(tasksTime);
/* 39 */     graphics.fill(currentX, bottom - tickMethodHeight - tasksHeight, currentX + 1, bottom - tickMethodHeight, -4548257);
/* 41 */     long otherTime = this.sampleStorage.get(sampleIndex) - this.sampleStorage.get(sampleIndex, TpsDebugDimensions.IDLE.ordinal()) - tickMethodTime - tasksTime;
/* 42 */     int otherHeight = getSampleHeight(otherTime);
/* 43 */     graphics.fill(currentX, bottom - otherHeight - tasksHeight - tickMethodHeight, currentX + 1, bottom - tasksHeight - tickMethodHeight, -10547572);
/*    */   }
/*    */   
/*    */   protected long getValueForAggregation(int sampleIndex) {
/* 48 */     return this.sampleStorage.get(sampleIndex) - this.sampleStorage.get(sampleIndex, TpsDebugDimensions.IDLE.ordinal());
/*    */   }
/*    */   
/*    */   protected String toDisplayString(double nanos) {
/* 53 */     return String.format(Locale.ROOT, "%d ms", new Object[] { Integer.valueOf((int)Math.round(toMilliseconds(nanos))) });
/*    */   }
/*    */   
/*    */   protected int getSampleHeight(double nanos) {
/* 58 */     return (int)Math.round(toMilliseconds(nanos) * 60.0D / ((Float)this.msptSupplier.get()).floatValue());
/*    */   }
/*    */   
/*    */   protected int getSampleColor(long nanos) {
/* 63 */     float mspt = ((Float)this.msptSupplier.get()).floatValue();
/* 64 */     return getSampleColor(toMilliseconds(nanos), mspt, -16711936, mspt * 1.125D, -256, mspt * 1.25D, -65536);
/*    */   }
/*    */   
/*    */   private static double toMilliseconds(double nanos) {
/* 68 */     return nanos / 1000000.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debugchart\TpsDebugChart.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */