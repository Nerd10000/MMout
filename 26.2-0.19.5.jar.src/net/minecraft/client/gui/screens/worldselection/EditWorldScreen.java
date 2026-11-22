/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.components.toasts.Toast;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.client.gui.screens.BackupConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.GenericWaitingScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EditWorldScreen extends Screen {
/*  42 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  43 */   private static final Component NAME_LABEL = (Component)Component.translatable("selectWorld.enterName").withStyle(ChatFormatting.GRAY);
/*     */   
/*  44 */   private static final Component RESET_ICON_BUTTON = (Component)Component.translatable("selectWorld.edit.resetIcon");
/*     */   
/*  45 */   private static final Component FOLDER_BUTTON = (Component)Component.translatable("selectWorld.edit.openFolder");
/*     */   
/*  46 */   private static final Component BACKUP_BUTTON = (Component)Component.translatable("selectWorld.edit.backup");
/*     */   
/*  47 */   private static final Component BACKUP_FOLDER_BUTTON = (Component)Component.translatable("selectWorld.edit.backupFolder");
/*     */   
/*  48 */   private static final Component OPTIMIZE_BUTTON = (Component)Component.translatable("selectWorld.edit.optimize");
/*     */   
/*  49 */   private static final Component OPTIMIZE_TITLE = (Component)Component.translatable("optimizeWorld.confirm.title");
/*     */   
/*  50 */   private static final Component OPTIMIZE_DESCRIPTION = (Component)Component.translatable("optimizeWorld.confirm.description");
/*     */   
/*  51 */   private static final Component OPTIMIZE_CONFIRMATION = (Component)Component.translatable("optimizeWorld.confirm.proceed");
/*     */   
/*  52 */   private static final Component SAVE_BUTTON = (Component)Component.translatable("selectWorld.edit.save");
/*     */   
/*     */   private static final int DEFAULT_WIDTH = 200;
/*     */   
/*     */   private static final int VERTICAL_SPACING = 4;
/*     */   
/*     */   private static final int HALF_WIDTH = 98;
/*     */   
/*  58 */   private final LinearLayout layout = LinearLayout.vertical().spacing(5);
/*     */   
/*     */   private final BooleanConsumer callback;
/*     */   
/*     */   private final LevelStorageSource.LevelStorageAccess levelAccess;
/*     */   
/*     */   private final EditBox nameEdit;
/*     */   
/*     */   public static EditWorldScreen create(Minecraft minecraft, LevelStorageSource.LevelStorageAccess levelAccess, BooleanConsumer callback) throws IOException {
/*  65 */     LevelSummary summary = levelAccess.fixAndGetSummary();
/*  66 */     return new EditWorldScreen(minecraft, levelAccess, summary.getLevelName(), callback);
/*     */   }
/*     */   
/*     */   private EditWorldScreen(Minecraft minecraft, LevelStorageSource.LevelStorageAccess levelAccess, String name, BooleanConsumer callback) {
/*  70 */     super((Component)Component.translatable("selectWorld.edit.title"));
/*  71 */     this.callback = callback;
/*  72 */     this.levelAccess = levelAccess;
/*  74 */     Font font = minecraft.font;
/*  75 */     this.layout.addChild((LayoutElement)new SpacerElement(200, 20));
/*  76 */     this.layout.addChild((LayoutElement)new StringWidget(NAME_LABEL, font));
/*  77 */     this.nameEdit = (EditBox)this.layout.addChild((LayoutElement)new EditBox(font, 200, 20, NAME_LABEL));
/*  78 */     this.nameEdit.setValue(name);
/*  81 */     LinearLayout bottomButtonRow = LinearLayout.horizontal().spacing(4);
/*  82 */     Button renameButton = (Button)bottomButtonRow.addChild(
/*  83 */         (LayoutElement)Button.builder(SAVE_BUTTON, button -> onRename(this.nameEdit.getValue()))
/*  84 */         .width(98)
/*  85 */         .build());
/*  86 */     bottomButtonRow.addChild(
/*  87 */         (LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> onClose())
/*  88 */         .width(98)
/*  89 */         .build());
/*  92 */     this.nameEdit.setResponder(newName -> renameButton.active = !StringUtil.isBlank(newName));
/*  93 */     ((Button)this.layout.addChild(
/*  94 */         (LayoutElement)Button.builder(RESET_ICON_BUTTON, button -> {
/*     */             levelAccess.getIconFile().ifPresent(());
/*     */             button.active = false;
/*  98 */           }).width(200)
/*  99 */         .build()))
/* 100 */       .active = levelAccess.getIconFile().filter(x$0 -> Files.isRegularFile(x$0, new java.nio.file.LinkOption[0])).isPresent();
/* 102 */     this.layout.addChild(
/* 103 */         (LayoutElement)Button.builder(FOLDER_BUTTON, button -> Util.getPlatform().openPath(levelAccess.getLevelPath(LevelResource.ROOT)))
/*     */         
/* 106 */         .width(200)
/* 107 */         .build());
/* 109 */     this.layout.addChild(
/* 110 */         (LayoutElement)Button.builder(BACKUP_BUTTON, button -> makeBackupAndShowToast(levelAccess).thenAcceptAsync((), (Executor)minecraft))
/*     */         
/* 116 */         .width(200)
/* 117 */         .build());
/* 119 */     this.layout.addChild(
/* 120 */         (LayoutElement)Button.builder(BACKUP_FOLDER_BUTTON, button -> {
/*     */             LevelStorageSource levelSource = minecraft.getLevelSource();
/*     */             Path path = levelSource.getBackupPath();
/*     */             try {
/*     */               FileUtil.createDirectoriesSafe(path);
/* 125 */             } catch (IOException e) {
/*     */               throw new RuntimeException(e);
/*     */             } 
/*     */             Util.getPlatform().openPath(path);
/* 130 */           }).width(200)
/* 131 */         .build());
/* 133 */     this.layout.addChild(
/* 134 */         (LayoutElement)Button.builder(OPTIMIZE_BUTTON, button -> minecraft.gui.setScreen((Screen)new BackupConfirmScreen((), (), OPTIMIZE_TITLE, OPTIMIZE_DESCRIPTION, OPTIMIZE_CONFIRMATION, true)))
/*     */         
/* 141 */         .width(200)
/* 142 */         .build());
/* 145 */     this.layout.addChild((LayoutElement)new SpacerElement(200, 20));
/* 146 */     this.layout.addChild((LayoutElement)bottomButtonRow);
/* 148 */     EditWorldScreen editWorldScreen = this;
/* 148 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/*     */   }
/*     */   
/*     */   protected void setInitialFocus() {
/* 153 */     setInitialFocus((GuiEventListener)this.nameEdit);
/*     */   }
/*     */   
/*     */   protected void init() {
/* 158 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 163 */     this.layout.arrangeElements();
/* 164 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 169 */     if (this.nameEdit.isFocused() && event.isConfirmation()) {
/* 170 */       onRename(this.nameEdit.getValue());
/* 171 */       onClose();
/* 172 */       return true;
/*     */     } 
/* 174 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 179 */     this.callback.accept(false);
/*     */   }
/*     */   
/*     */   private void onRename(String newName) {
/*     */     try {
/* 184 */       this.levelAccess.renameLevel(newName);
/* 185 */     } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException e) {
/* 186 */       LOGGER.error("Failed to access world '{}'", this.levelAccess.getLevelId(), e);
/* 187 */       SystemToast.onWorldAccessFailure(this.minecraft, this.levelAccess.getLevelId());
/*     */     } 
/* 189 */     this.callback.accept(true);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<Boolean> conditionallyMakeBackupAndShowToast(boolean createBackup, LevelStorageSource.LevelStorageAccess access) {
/* 193 */     if (createBackup)
/* 194 */       return makeBackupAndShowToast(access); 
/* 196 */     return CompletableFuture.completedFuture(Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public static CompletableFuture<Boolean> makeBackupAndShowToast(LevelStorageSource.LevelStorageAccess access) {
/* 200 */     Minecraft minecraft = Minecraft.getInstance();
/* 201 */     minecraft.setScreenAndShow(
/* 202 */         (Screen)GenericWaitingScreen.createWaitingWithoutButton(
/* 203 */           (Component)Component.translatable("selectWorld.waitingForBackup.title"), 
/* 204 */           (Component)Component.translatable("selectWorld.waitingForBackup.message").withStyle(ChatFormatting.GRAY)));
/* 207 */     return CompletableFuture.supplyAsync(() -> {
/*     */           try {
/*     */             return Long.valueOf(access.makeWorldBackup());
/* 210 */           } catch (IOException e) {
/*     */             throw new RuntimeException(e);
/*     */           } 
/* 213 */         }(Executor)Util.backgroundExecutor())
/* 214 */       .thenApplyAsync(size -> {
/*     */           MutableComponent mutableComponent1 = Component.translatable("selectWorld.edit.backupCreated", new Object[] { access.getLevelId() });
/*     */           MutableComponent mutableComponent2 = Component.translatable("selectWorld.edit.backupSize", new Object[] { Integer.valueOf(Mth.ceil(size.longValue() / 1048576.0D)) });
/*     */           minecraft.gui.toastManager().addToast((Toast)new SystemToast(SystemToast.SystemToastId.WORLD_BACKUP, (Component)mutableComponent1, (Component)mutableComponent2));
/*     */           return Boolean.valueOf(true);
/* 220 */         }(Executor)minecraft).exceptionallyAsync(exception -> {
/*     */           MutableComponent mutableComponent1 = Component.translatable("selectWorld.edit.backupFailed");
/*     */           MutableComponent mutableComponent2 = Component.literal(exception.getMessage());
/*     */           minecraft.gui.toastManager().addToast((Toast)new SystemToast(SystemToast.SystemToastId.WORLD_BACKUP, (Component)mutableComponent1, (Component)mutableComponent2));
/*     */           return Boolean.valueOf(false);
/*     */         }(Executor)minecraft);
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 230 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 231 */     graphics.centeredText(this.font, this.title, this.width / 2, 15, -1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\EditWorldScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */