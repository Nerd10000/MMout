/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.util.concurrent.RateLimiter;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.Unit;
/*     */ import com.mojang.realmsclient.client.UploadStatus;
/*     */ import com.mojang.realmsclient.client.worldupload.RealmsUploadException;
/*     */ import com.mojang.realmsclient.client.worldupload.RealmsWorldUpload;
/*     */ import com.mojang.realmsclient.client.worldupload.RealmsWorldUploadStatusTracker;
/*     */ import com.mojang.realmsclient.dto.RealmsSetting;
/*     */ import com.mojang.realmsclient.dto.RealmsSlot;
/*     */ import com.mojang.realmsclient.dto.RealmsWorldOptions;
/*     */ import com.mojang.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import com.mojang.realmsclient.util.task.RealmCreationTask;
/*     */ import com.mojang.realmsclient.util.task.SwitchSlotTask;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import net.minecraft.client.GameNarrator;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.TitleScreen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ 
/*     */ public class RealmsUploadScreen extends RealmsScreen implements RealmsWorldUploadStatusTracker {
/*     */   private static final int BAR_WIDTH = 200;
/*     */   
/*     */   private static final int BAR_TOP = 80;
/*     */   
/*     */   private static final int BAR_BOTTOM = 95;
/*     */   
/*     */   private static final int BAR_BORDER = 1;
/*     */   
/*  45 */   private static final String[] DOTS = new String[] { "", ".", ". .", ". . ." };
/*     */   
/*  46 */   private static final Component VERIFYING_TEXT = (Component)Component.translatable("mco.upload.verifying");
/*     */   
/*     */   private final RealmsResetWorldScreen lastScreen;
/*     */   
/*     */   private final LevelSummary selectedLevel;
/*     */   
/*     */   private final RealmCreationTask realmCreationTask;
/*     */   
/*     */   private final long realmId;
/*     */   
/*     */   private final int slotId;
/*     */   
/*  54 */   private final AtomicReference<RealmsWorldUpload> currentUpload = new AtomicReference<>();
/*     */   
/*     */   private final UploadStatus uploadStatus;
/*     */   
/*     */   private final RateLimiter narrationRateLimiter;
/*     */   
/*     */   private volatile Component[] errorMessage;
/*     */   
/*  59 */   private volatile Component status = (Component)Component.translatable("mco.upload.preparing");
/*     */   
/*     */   private volatile String progress;
/*     */   
/*     */   private volatile boolean cancelled;
/*     */   
/*     */   private volatile boolean uploadFinished;
/*     */   
/*     */   private volatile boolean showDots = true;
/*     */   
/*     */   private volatile boolean uploadStarted;
/*     */   
/*     */   private Button backButton;
/*     */   
/*     */   private Button cancelButton;
/*     */   
/*     */   private int tickCount;
/*     */   
/*  72 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout((Screen)this);
/*     */   
/*     */   public RealmsUploadScreen(RealmCreationTask realmCreationTask, long realmId, int slotId, RealmsResetWorldScreen lastScreen, LevelSummary selectedLevel) {
/*  75 */     super(GameNarrator.NO_TITLE);
/*  76 */     this.realmCreationTask = realmCreationTask;
/*  77 */     this.realmId = realmId;
/*  78 */     this.slotId = slotId;
/*  79 */     this.lastScreen = lastScreen;
/*  80 */     this.selectedLevel = selectedLevel;
/*  81 */     this.uploadStatus = new UploadStatus();
/*  82 */     this.narrationRateLimiter = RateLimiter.create(0.10000000149011612D);
/*     */   }
/*     */   
/*     */   public void init() {
/*  87 */     this.backButton = (Button)this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onBack()).build());
/*  88 */     this.backButton.visible = false;
/*  89 */     this.cancelButton = (Button)this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> onCancel()).build());
/*  91 */     if (!this.uploadStarted)
/*  92 */       if (this.lastScreen.slot == -1) {
/*  93 */         this.uploadStarted = true;
/*  94 */         upload();
/*     */       } else {
/*  96 */         List<LongRunningTask> tasks = new ArrayList<>();
/*  97 */         if (this.realmCreationTask != null)
/*  98 */           tasks.add(this.realmCreationTask); 
/* 100 */         tasks.add(new SwitchSlotTask(this.realmId, this.lastScreen.slot, () -> {
/*     */                 if (!this.uploadStarted) {
/*     */                   this.uploadStarted = true;
/*     */                   this.minecraft.execute(());
/*     */                 } 
/*     */               }));
/* 109 */         this.minecraft.gui.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)this.lastScreen, tasks.<LongRunningTask>toArray(new LongRunningTask[0])));
/*     */       }  
/* 113 */     RealmsUploadScreen realmsUploadScreen = this;
/* 113 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/* 114 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 119 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   private void onBack() {
/* 123 */     this.minecraft.gui.setScreen((Screen)new RealmsConfigureWorldScreen(new RealmsMainScreen((Screen)new TitleScreen()), this.realmId));
/*     */   }
/*     */   
/*     */   private void onCancel() {
/* 127 */     this.cancelled = true;
/* 128 */     RealmsWorldUpload realmsWorldUpload = this.currentUpload.get();
/* 129 */     if (realmsWorldUpload != null) {
/* 130 */       realmsWorldUpload.cancel();
/*     */     } else {
/* 132 */       this.minecraft.gui.setScreen((Screen)this.lastScreen);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 138 */     if (event.isEscape()) {
/* 139 */       if (this.showDots) {
/* 140 */         onCancel();
/*     */       } else {
/* 142 */         onBack();
/*     */       } 
/* 144 */       return true;
/*     */     } 
/* 146 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int xm, int ym, float a) {
/* 151 */     super.extractRenderState(graphics, xm, ym, a);
/* 153 */     if (!this.uploadFinished && this.uploadStatus.uploadStarted() && this.uploadStatus.uploadCompleted() && this.cancelButton != null) {
/* 154 */       this.status = VERIFYING_TEXT;
/* 155 */       this.cancelButton.active = false;
/*     */     } 
/* 158 */     graphics.centeredText(this.font, this.status, this.width / 2, 50, -1);
/* 160 */     if (this.showDots)
/* 161 */       graphics.text(this.font, DOTS[this.tickCount / 10 % DOTS.length], this.width / 2 + this.font.width((FormattedText)this.status) / 2 + 5, 50, -1); 
/* 164 */     if (this.uploadStatus.uploadStarted() && !this.cancelled) {
/* 165 */       extractProgressBar(graphics);
/* 166 */       extractUploadSpeed(graphics);
/*     */     } 
/* 169 */     Component[] errorMessages = this.errorMessage;
/* 170 */     if (errorMessages != null)
/* 171 */       for (int i = 0; i < errorMessages.length; i++)
/* 172 */         graphics.centeredText(this.font, errorMessages[i], this.width / 2, 110 + 12 * i, -65536);  
/*     */   }
/*     */   
/*     */   private void extractProgressBar(GuiGraphicsExtractor graphics) {
/* 178 */     double percentage = this.uploadStatus.getPercentage();
/* 179 */     this.progress = String.format(Locale.ROOT, "%.1f", new Object[] { Double.valueOf(percentage * 100.0D) });
/* 181 */     int left = (this.width - 200) / 2;
/* 182 */     int right = left + (int)Math.round(200.0D * percentage);
/* 183 */     graphics.fill(left - 1, 79, right + 1, 96, -1);
/* 184 */     graphics.fill(left, 80, right, 95, -8355712);
/* 185 */     graphics.centeredText(this.font, (Component)Component.translatable("mco.upload.percent", new Object[] { this.progress }), this.width / 2, 84, -1);
/*     */   }
/*     */   
/*     */   private void extractUploadSpeed(GuiGraphicsExtractor graphics) {
/* 189 */     extractUploadSpeed0(graphics, this.uploadStatus.getBytesPerSecond());
/*     */   }
/*     */   
/*     */   private void extractUploadSpeed0(GuiGraphicsExtractor graphics, long bytesPerSecond) {
/* 193 */     String uploadProgress = this.progress;
/* 194 */     if (bytesPerSecond > 0L && uploadProgress != null) {
/* 195 */       int progressLength = this.font.width(uploadProgress);
/* 196 */       String stringPresentation = "(" + Unit.humanReadable(bytesPerSecond) + "/s)";
/* 197 */       graphics.text(this.font, stringPresentation, this.width / 2 + progressLength / 2 + 15, 84, -1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 203 */     super.tick();
/* 205 */     this.tickCount++;
/* 207 */     this.uploadStatus.refreshBytesPerSecond();
/* 209 */     if (this.narrationRateLimiter.tryAcquire(1)) {
/* 210 */       Component message = createProgressNarrationMessage();
/* 211 */       this.minecraft.getNarrator().saySystemNow(message);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Component createProgressNarrationMessage() {
/* 216 */     List<Component> elements = Lists.newArrayList();
/* 217 */     elements.add(this.status);
/* 218 */     if (this.progress != null)
/* 219 */       elements.add(Component.translatable("mco.upload.percent", new Object[] { this.progress })); 
/* 221 */     Component[] errorMessages = this.errorMessage;
/* 222 */     if (errorMessages != null)
/* 223 */       elements.addAll(Arrays.asList(errorMessages)); 
/* 225 */     return CommonComponents.joinLines(elements);
/*     */   }
/*     */   
/*     */   private void upload() {
/* 229 */     Path worldFolder = this.minecraft.gameDirectory.toPath().resolve("saves").resolve(this.selectedLevel.getLevelId());
/* 230 */     RealmsWorldOptions worldOptions = RealmsWorldOptions.createFromSettings(this.selectedLevel.getSettings(), this.selectedLevel.levelVersion().minecraftVersionName());
/* 231 */     RealmsSlot realmsSlot = new RealmsSlot(this.slotId, worldOptions, List.of(RealmsSetting.hardcoreSetting(this.selectedLevel.isHardcore())));
/* 232 */     RealmsWorldUpload newUpload = new RealmsWorldUpload(worldFolder, realmsSlot, this.minecraft.getUser(), this.realmId, this);
/* 234 */     if (!this.currentUpload.compareAndSet(null, newUpload))
/* 235 */       throw new IllegalStateException("Tried to start uploading but was already uploading"); 
/* 237 */     newUpload.packAndUpload().handleAsync((result, exception) -> {
/*     */           if (exception != null) {
/*     */             if (exception instanceof CompletionException) {
/*     */               CompletionException e = (CompletionException)exception;
/*     */               exception = e.getCause();
/*     */             } 
/*     */             if (exception instanceof RealmsUploadException) {
/*     */               RealmsUploadException e = (RealmsUploadException)exception;
/*     */               if (e.getStatusMessage() != null)
/*     */                 this.status = e.getStatusMessage(); 
/*     */               setErrorMessage(e.getErrorMessages());
/*     */             } else {
/*     */               this.status = (Component)Component.translatable("mco.upload.failed", new Object[] { exception.getMessage() });
/*     */             } 
/*     */           } else {
/*     */             this.status = (Component)Component.translatable("mco.upload.done");
/*     */             if (this.backButton != null)
/*     */               this.backButton.setMessage(CommonComponents.GUI_DONE); 
/*     */           } 
/*     */           this.uploadFinished = true;
/*     */           this.showDots = false;
/*     */           if (this.backButton != null)
/*     */             this.backButton.visible = true; 
/*     */           if (this.cancelButton != null)
/*     */             this.cancelButton.visible = false; 
/*     */           this.currentUpload.set(null);
/*     */           return null;
/* 237 */         }(Executor)this.minecraft);
/*     */   }
/*     */   
/*     */   private void setErrorMessage(Component... messages) {
/* 272 */     this.errorMessage = messages;
/*     */   }
/*     */   
/*     */   public UploadStatus getUploadStatus() {
/* 277 */     return this.uploadStatus;
/*     */   }
/*     */   
/*     */   public void setUploading() {
/* 282 */     this.status = (Component)Component.translatable("mco.upload.uploading", new Object[] { this.selectedLevel.getLevelName() });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsUploadScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */