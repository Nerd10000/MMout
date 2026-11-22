/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ 
/*     */ @VisibleForTesting
/*     */ enum HandRenderSelection {
/* 428 */   RENDER_BOTH_HANDS(true, true),
/* 429 */   RENDER_MAIN_HAND_ONLY(true, false),
/* 430 */   RENDER_OFF_HAND_ONLY(false, true);
/*     */   
/*     */   public final boolean renderMainHand;
/*     */   
/*     */   public final boolean renderOffHand;
/*     */   
/*     */   HandRenderSelection(boolean renderMainHand, boolean renderOffHand) {
/* 435 */     this.renderMainHand = renderMainHand;
/* 436 */     this.renderOffHand = renderOffHand;
/*     */   }
/*     */   
/*     */   public static HandRenderSelection onlyForHand(InteractionHand hand) {
/* 440 */     return (hand == InteractionHand.MAIN_HAND) ? RENDER_MAIN_HAND_ONLY : RENDER_OFF_HAND_ONLY;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\ItemInHandRenderer$HandRenderSelection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */