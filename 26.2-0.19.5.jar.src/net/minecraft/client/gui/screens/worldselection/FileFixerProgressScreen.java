/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*     */ 
/*     */ public class FileFixerProgressScreen extends Screen {
/*     */   private static final int PROGRESS_BAR_WIDTH = 200;
/*     */   
/*     */   private static final int PROGRESS_BAR_HEIGHT = 2;
/*     */   
/*     */   private static final int LINE_SPACING = 3;
/*     */   
/*     */   private static final int SECTION_SPACING = 30;
/*     */   
/*  17 */   private static final Component SCANNING = (Component)Component.translatable("upgradeWorld.info.scanning");
/*     */   
/*     */   private final UpgradeProgress upgradeProgress;
/*     */   
/*     */   private Button cancelButton;
/*     */   
/*     */   public FileFixerProgressScreen(UpgradeProgress upgradeProgress) {
/*  23 */     super((Component)Component.translatable("upgradeWorld.title"));
/*  24 */     this.upgradeProgress = upgradeProgress;
/*     */   }
/*     */   
/*     */   protected void init() {
/*  29 */     super.init();
/*  30 */     this
/*     */       
/*  33 */       .cancelButton = Button.builder(CommonComponents.GUI_CANCEL, button -> {
/*     */           this.upgradeProgress.setCanceled();
/*     */           button.active = false;
/*  33 */         }).bounds((this.width - 200) / 2, this.height / 2 + 100, 200, 20).build();
/*  34 */     addRenderableWidget((GuiEventListener)this.cancelButton);
/*     */   }
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/*  39 */     return false;
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
/*  44 */     super.extractRenderState(graphics, mouseX, mouseY, partialTick);
/*  46 */     int xCenter = this.width / 2;
/*  47 */     int yCenter = this.height / 2;
/*  48 */     int textTop = yCenter - 50;
/*  50 */     extractTitle(graphics, xCenter, textTop);
/*  52 */     int totalFiles = this.upgradeProgress.getTotalFileFixStats().totalOperations();
/*  53 */     if (totalFiles > 0) {
/*  54 */       extractProgress(graphics, xCenter, textTop);
/*     */     } else {
/*  56 */       extractScanning(graphics, xCenter, textTop);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractTitle(GuiGraphicsExtractor graphics, int xCenter, int yTop) {
/*  61 */     graphics.centeredText(this.font, this.title, xCenter, yTop, -1);
/*     */   }
/*     */   
/*     */   private void extractProgress(GuiGraphicsExtractor graphics, int xCenter, int textTop) {
/*  65 */     UpgradeProgress.FileFixStats typeFileStats = this.upgradeProgress.getTypeFileFixStats();
/*  66 */     UpgradeProgress.FileFixStats totalFileStats = this.upgradeProgress.getTotalFileFixStats();
/*  67 */     UpgradeProgress.FileFixStats runningFileFixerStats = this.upgradeProgress.getRunningFileFixerStats();
/*  69 */     Objects.requireNonNull(this.font);
/*  69 */     int y = textTop + 9 + 3;
/*  70 */     extractProgressBar(graphics, xCenter, y, runningFileFixerStats.getProgress());
/*  72 */     y += 7;
/*  73 */     extractFileStats(graphics, xCenter, y, totalFileStats.finishedOperations(), totalFileStats.totalOperations());
/*  75 */     Objects.requireNonNull(this.font);
/*  75 */     y += 9 * 2 + 6;
/*  76 */     extractFileFixerCount(graphics, xCenter, y, runningFileFixerStats.finishedOperations(), runningFileFixerStats.totalOperations());
/*  78 */     Objects.requireNonNull(this.font);
/*  78 */     y += 9 + 30 - 5;
/*  79 */     extractTypeText(graphics, xCenter, y);
/*  81 */     Objects.requireNonNull(this.font);
/*  81 */     y += 9 + 3;
/*  82 */     extractProgressBar(graphics, xCenter, y, typeFileStats.getProgress());
/*  84 */     y += 7;
/*  85 */     extractTypeProgress(graphics, xCenter, y, typeFileStats.getProgress());
/*     */   }
/*     */   
/*     */   private void extractProgressBar(GuiGraphicsExtractor graphics, int xCenter, int y, float progress) {
/*  89 */     int barLeft = xCenter - 100;
/*  90 */     int barRight = barLeft + 200;
/*  91 */     int barBottom = y + 2;
/*  93 */     graphics.fill(barLeft, y, barRight, barBottom, -16777216);
/*  94 */     graphics.fill(barLeft, y, barLeft + Math.round(progress * 200.0F), barBottom, -16711936);
/*     */   }
/*     */   
/*     */   private void extractTypeText(GuiGraphicsExtractor graphics, int xCenter, int y) {
/*  98 */     UpgradeProgress.Type upgradeProgressType = this.upgradeProgress.getType();
/*  99 */     if (upgradeProgressType != null)
/* 100 */       graphics.centeredText(this.font, upgradeProgressType.label(), xCenter, y, -6250336); 
/*     */   }
/*     */   
/*     */   private void extractTypeProgress(GuiGraphicsExtractor graphics, int xCenter, int y, float progress) {
/* 105 */     MutableComponent mutableComponent = Component.translatable("upgradeWorld.progress.percentage", new Object[] { Integer.valueOf(Mth.floor(progress * 100.0F)) });
/* 106 */     graphics.centeredText(this.font, (Component)mutableComponent, xCenter, y, -6250336);
/*     */   }
/*     */   
/*     */   private void extractFileStats(GuiGraphicsExtractor graphics, int xCenter, int yStart, int converted, int total) {
/* 110 */     Objects.requireNonNull(this.font);
/* 110 */     int lineHeight = 9 + 3;
/* 112 */     graphics.centeredText(this.font, 
/* 113 */         (Component)Component.translatable("upgradeWorld.info.converted", new Object[] { Integer.valueOf(converted) }), xCenter, yStart, -6250336);
/* 116 */     graphics.centeredText(this.font, 
/* 117 */         (Component)Component.translatable("upgradeWorld.info.total", new Object[] { Integer.valueOf(total) }), xCenter, yStart + lineHeight, -6250336);
/*     */   }
/*     */   
/*     */   private void extractScanning(GuiGraphicsExtractor graphics, int xCenter, int textTop) {
/* 122 */     Objects.requireNonNull(this.font);
/* 122 */     graphics.centeredText(this.font, SCANNING, xCenter, textTop + 9 + 3, -6250336);
/*     */   }
/*     */   
/*     */   private void extractFileFixerCount(GuiGraphicsExtractor graphics, int xCenter, int y, int current, int total) {
/* 126 */     MutableComponent mutableComponent = Component.translatable("upgradeWorld.info.file_fix_stage", new Object[] { Integer.valueOf(current), Integer.valueOf(total) });
/* 127 */     graphics.centeredText(this.font, (Component)mutableComponent, xCenter, y, -6250336);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\FileFixerProgressScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */