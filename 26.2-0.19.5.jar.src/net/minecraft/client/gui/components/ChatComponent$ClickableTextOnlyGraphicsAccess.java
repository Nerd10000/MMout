/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.TextAlignment;
/*     */ import net.minecraft.client.multiplayer.chat.GuiMessageTag;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import org.joml.Matrix3x2f;
/*     */ import org.joml.Matrix3x2fc;
/*     */ 
/*     */ class ClickableTextOnlyGraphicsAccess implements ChatComponent.ChatGraphicsAccess {
/*     */   private final ActiveTextCollector output;
/*     */   
/*     */   public ClickableTextOnlyGraphicsAccess(ActiveTextCollector output) {
/* 742 */     this.output = output;
/*     */   }
/*     */   
/*     */   public void updatePose(Consumer<Matrix3x2f> updater) {
/* 747 */     ActiveTextCollector.Parameters defaultParameters = this.output.defaultParameters();
/* 748 */     Matrix3x2f newPose = new Matrix3x2f(defaultParameters.pose());
/* 749 */     updater.accept(newPose);
/* 750 */     this.output.defaultParameters(defaultParameters.withPose((Matrix3x2fc)newPose));
/*     */   }
/*     */   
/*     */   public void fill(int x0, int y0, int x1, int y1, int color) {}
/*     */   
/*     */   public boolean handleMessage(int textTop, float opacity, FormattedCharSequence message) {
/* 760 */     this.output.accept(TextAlignment.LEFT, 0, textTop, message);
/* 761 */     return false;
/*     */   }
/*     */   
/*     */   public void handleTag(int x0, int y0, int x1, int y1, float opacity, GuiMessageTag tag) {}
/*     */   
/*     */   public void handleTagIcon(int left, int bottom, boolean forceVisible, GuiMessageTag tag, GuiMessageTag.Icon icon) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ChatComponent$ClickableTextOnlyGraphicsAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */