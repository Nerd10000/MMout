/*     */ package net.minecraft.client.gui.screens.reporting;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.PlayerFaceExtractor;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ 
/*     */ public class MessageHeadingEntry extends ChatSelectionScreen.ChatSelectionList.Entry {
/*     */   private static final int FACE_SIZE = 12;
/*     */   
/*     */   private static final int PADDING = 4;
/*     */   
/*     */   private final Component heading;
/*     */   
/*     */   private final Supplier<PlayerSkin> skin;
/*     */   
/*     */   private final boolean canReport;
/*     */   
/*     */   public MessageHeadingEntry(GameProfile profile, Component heading, boolean canReport) {
/* 397 */     this.heading = heading;
/* 398 */     this.canReport = canReport;
/* 399 */     this.skin = ChatSelectionScreen.ChatSelectionList.access$800(ChatSelectionScreen.ChatSelectionList.this).getSkinManager().createLookup(profile, true);
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 404 */     int faceX = getContentX() - 12 + 4;
/* 405 */     int faceY = getContentY() + (getContentHeight() - 12) / 2;
/* 406 */     PlayerFaceExtractor.extractRenderState(graphics, this.skin.get(), faceX, faceY, 12);
/* 408 */     Objects.requireNonNull(ChatSelectionScreen.access$900(this.this$1.this$0));
/* 408 */     int textY = getContentY() + 1 + (getContentHeight() - 9) / 2;
/* 409 */     graphics.text(ChatSelectionScreen.access$1000(this.this$1.this$0), this.heading, faceX + 12 + 4, textY, this.canReport ? -1 : -1593835521);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\reporting\ChatSelectionScreen$ChatSelectionList$MessageHeadingEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */