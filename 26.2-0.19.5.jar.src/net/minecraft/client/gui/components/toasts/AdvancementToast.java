/*    */ package net.minecraft.client.gui.components.toasts;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.advancements.AdvancementType;
/*    */ import net.minecraft.advancements.DisplayInfo;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class AdvancementToast implements Toast {
/* 23 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/advancement");
/*    */   
/*    */   public static final int DISPLAY_TIME = 5000;
/*    */   
/*    */   private final AdvancementHolder advancement;
/*    */   
/* 26 */   private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;
/*    */   
/*    */   private final ItemStack iconItem;
/*    */   
/*    */   public AdvancementToast(AdvancementHolder advancement) {
/* 30 */     this.advancement = advancement;
/* 31 */     this.iconItem = advancement.value().display().map(d -> d.getIcon().create()).orElse(ItemStack.EMPTY);
/*    */   }
/*    */   
/*    */   public Toast.Visibility getWantedVisibility() {
/* 36 */     return this.wantedVisibility;
/*    */   }
/*    */   
/*    */   public void update(ToastManager manager, long fullyVisibleForMs) {
/* 41 */     DisplayInfo display = this.advancement.value().display().orElse(null);
/* 42 */     if (display == null) {
/* 43 */       this.wantedVisibility = Toast.Visibility.HIDE;
/*    */       return;
/*    */     } 
/* 47 */     this.wantedVisibility = (fullyVisibleForMs >= 5000.0D * manager.getNotificationDisplayTimeMultiplier()) ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
/*    */   }
/*    */   
/*    */   public SoundEvent getSoundEvent() {
/* 52 */     return isChallengeAdvancement() ? SoundEvents.UI_TOAST_CHALLENGE_COMPLETE : null;
/*    */   }
/*    */   
/*    */   private boolean isChallengeAdvancement() {
/* 56 */     Optional<DisplayInfo> displayInfo = this.advancement.value().display();
/* 57 */     return (displayInfo.isPresent() && ((DisplayInfo)displayInfo.get()).getType().equals(AdvancementType.CHALLENGE));
/*    */   }
/*    */   
/*    */   public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long fullyVisibleForMs) {
/* 62 */     DisplayInfo display = this.advancement.value().display().orElse(null);
/* 63 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, width(), height());
/* 65 */     if (display == null)
/*    */       return; 
/* 69 */     List<FormattedCharSequence> lines = font.split((FormattedText)display.getTitle(), 125);
/* 70 */     int titleColor = (display.getType() == AdvancementType.CHALLENGE) ? -30465 : -256;
/* 72 */     if (lines.size() == 1) {
/* 73 */       graphics.text(font, display.getType().getDisplayName(), 30, 7, titleColor, false);
/* 74 */       graphics.text(font, lines.get(0), 30, 18, -1, false);
/*    */     } else {
/* 76 */       int unlockTextTime = 1500;
/* 77 */       float unlockFadeTime = 300.0F;
/* 78 */       if (fullyVisibleForMs < 1500L) {
/* 79 */         int alpha = Mth.floor(Mth.clamp((float)(1500L - fullyVisibleForMs) / 300.0F, 0.0F, 1.0F) * 255.0F);
/* 80 */         graphics.text(font, display.getType().getDisplayName(), 30, 11, ARGB.color(alpha, titleColor), false);
/*    */       } else {
/* 82 */         int alpha = Mth.floor(Mth.clamp((float)(fullyVisibleForMs - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F);
/* 83 */         Objects.requireNonNull(font);
/* 83 */         int y = height() / 2 - lines.size() * 9 / 2;
/* 84 */         for (FormattedCharSequence line : lines) {
/* 85 */           graphics.text(font, line, 30, y, ARGB.white(alpha), false);
/* 86 */           Objects.requireNonNull(font);
/* 86 */           y += 9;
/*    */         } 
/*    */       } 
/*    */     } 
/* 91 */     graphics.fakeItem(this.iconItem, 8, 8);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\toasts\AdvancementToast.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */