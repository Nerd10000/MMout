/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.narration.NarratedElementType;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.gui.narration.NarrationSupplier;
/*    */ import net.minecraft.locale.Language;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*    */ 
/*    */ public class Tooltip implements NarrationSupplier {
/*    */   private static final int MAX_WIDTH = 170;
/*    */   
/*    */   private final Component message;
/*    */   
/*    */   private List<FormattedCharSequence> cachedTooltip;
/*    */   
/*    */   private Language splitWithLanguage;
/*    */   
/*    */   private final Identifier style;
/*    */   
/*    */   private final Component narration;
/*    */   
/*    */   private final Optional<TooltipComponent> component;
/*    */   
/*    */   private Tooltip(Component message, Component narration, Optional<TooltipComponent> component, Identifier style) {
/* 27 */     this.message = message;
/* 28 */     this.narration = narration;
/* 29 */     this.component = component;
/* 30 */     this.style = style;
/*    */   }
/*    */   
/*    */   public static Tooltip create(Component message) {
/* 34 */     return new Tooltip(message, message, Optional.empty(), null);
/*    */   }
/*    */   
/*    */   public static Tooltip create(Component message, Component narration) {
/* 38 */     return new Tooltip(message, narration, Optional.empty(), null);
/*    */   }
/*    */   
/*    */   public static Tooltip create(Component message, Optional<TooltipComponent> component, Identifier style) {
/* 42 */     return new Tooltip(message, message, component, style);
/*    */   }
/*    */   
/*    */   public Optional<TooltipComponent> component() {
/* 46 */     return this.component;
/*    */   }
/*    */   
/*    */   public Identifier style() {
/* 50 */     return this.style;
/*    */   }
/*    */   
/*    */   public void updateNarration(NarrationElementOutput output) {
/* 55 */     if (this.narration != null)
/* 56 */       output.add(NarratedElementType.HINT, this.narration); 
/*    */   }
/*    */   
/*    */   public List<FormattedCharSequence> toCharSequence(Minecraft minecraft) {
/* 61 */     Language currentLanguage = Language.getInstance();
/* 62 */     if (this.cachedTooltip == null || currentLanguage != this.splitWithLanguage) {
/* 63 */       this.cachedTooltip = splitTooltip(minecraft, this.message);
/* 64 */       this.splitWithLanguage = currentLanguage;
/*    */     } 
/* 66 */     return this.cachedTooltip;
/*    */   }
/*    */   
/*    */   public static List<FormattedCharSequence> splitTooltip(Minecraft minecraft, Component message) {
/* 70 */     return minecraft.font.split((FormattedText)message, 170);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\Tooltip.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */