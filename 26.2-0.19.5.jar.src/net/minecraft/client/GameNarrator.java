/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.MessageBox;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.text2speech.Narrator;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.components.toasts.ToastManager;
/*     */ import net.minecraft.client.main.SilentInitException;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GameNarrator {
/*  16 */   public static final Component NO_TITLE = CommonComponents.EMPTY;
/*     */   
/*  17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*  20 */   private final Narrator narrator = Narrator.getNarrator();
/*     */   
/*     */   public GameNarrator(Minecraft minecraft) {
/*  23 */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   public void sayChatQueued(Component message) {
/*  27 */     if (getStatus().shouldNarrateChat())
/*  28 */       narrateNotInterruptingMessage(message); 
/*     */   }
/*     */   
/*     */   public void saySystemChatQueued(Component message) {
/*  33 */     if (getStatus().shouldNarrateSystemOrChat())
/*  34 */       narrateNotInterruptingMessage(message); 
/*     */   }
/*     */   
/*     */   public void saySystemQueued(Component message) {
/*  39 */     if (getStatus().shouldNarrateSystem())
/*  40 */       narrateNotInterruptingMessage(message); 
/*     */   }
/*     */   
/*     */   private void narrateNotInterruptingMessage(Component message) {
/*  45 */     String messageString = message.getString();
/*  46 */     if (!messageString.isEmpty()) {
/*  47 */       logNarratedMessage(messageString);
/*  48 */       narrateMessage(messageString, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saySystemNow(Component message) {
/*  53 */     saySystemNow(message.getString());
/*     */   }
/*     */   
/*     */   public void saySystemNow(String message) {
/*  57 */     if (getStatus().shouldNarrateSystem() && !message.isEmpty()) {
/*  58 */       logNarratedMessage(message);
/*  59 */       if (this.narrator.active()) {
/*  60 */         this.narrator.clear();
/*  61 */         narrateMessage(message, true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void narrateMessage(String message, boolean interrupt) {
/*  67 */     this.narrator.say(message, interrupt, this.minecraft.options.getFinalSoundSourceVolume(SoundSource.VOICE));
/*     */   }
/*     */   
/*     */   private NarratorStatus getStatus() {
/*  71 */     return this.minecraft.options.narrator().get();
/*     */   }
/*     */   
/*     */   private void logNarratedMessage(String message) {
/*  75 */     if (SharedConstants.IS_RUNNING_IN_IDE)
/*  76 */       LOGGER.debug("Narrating: {}", message.replaceAll("\n", "\\\\n")); 
/*     */   }
/*     */   
/*     */   public void updateNarratorStatus(NarratorStatus status) {
/*  81 */     clear();
/*  83 */     narrateMessage(Component.translatable("options.narrator").append(" : ").append(status.getName()).getString(), true);
/*  85 */     ToastManager toastManager = (Minecraft.getInstance()).gui.toastManager();
/*  86 */     if (this.narrator.active()) {
/*  87 */       if (status == NarratorStatus.OFF) {
/*  88 */         SystemToast.addOrUpdate(toastManager, SystemToast.SystemToastId.NARRATOR_TOGGLE, (Component)Component.translatable("narrator.toast.disabled"), null);
/*     */       } else {
/*  90 */         SystemToast.addOrUpdate(toastManager, SystemToast.SystemToastId.NARRATOR_TOGGLE, (Component)Component.translatable("narrator.toast.enabled"), status.getName());
/*     */       } 
/*     */     } else {
/*  93 */       SystemToast.addOrUpdate(toastManager, SystemToast.SystemToastId.NARRATOR_TOGGLE, (Component)Component.translatable("narrator.toast.disabled"), (Component)Component.translatable("options.narrator.notavailable"));
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/*  98 */     return this.narrator.active();
/*     */   }
/*     */   
/*     */   public void clear() {
/* 102 */     if (getStatus() == NarratorStatus.OFF || !this.narrator.active())
/*     */       return; 
/* 105 */     this.narrator.clear();
/*     */   }
/*     */   
/*     */   public void destroy() {
/* 109 */     this.narrator.destroy();
/*     */   }
/*     */   
/*     */   public void checkStatus(boolean requiredActive) {
/* 113 */     if (requiredActive && !isActive() && 
/* 114 */       !MessageBox.errorWithContinue("Failed to initialize text-to-speech library. Do you want to continue?\nIf this problem persists, please report it at bugs.mojang.com"))
/* 118 */       throw new NarratorInitException("Narrator library is not active"); 
/*     */   }
/*     */   
/*     */   public static class NarratorInitException extends SilentInitException {
/*     */     public NarratorInitException(String message) {
/* 125 */       super(message);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\GameNarrator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */