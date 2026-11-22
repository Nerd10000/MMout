/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.time.Instant;
/*     */ import java.time.ZoneId;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.SelectableEntry;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.screens.AlertScreen;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.FaviconTexture;
/*     */ import net.minecraft.client.gui.screens.GenericMessageScreen;
/*     */ import net.minecraft.client.gui.screens.NoticeWithLinkScreen;
/*     */ import net.minecraft.client.gui.screens.ProgressScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ import net.minecraft.world.level.validation.ContentValidationException;
/*     */ import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
/*     */ 
/*     */ public final class WorldListEntry extends WorldSelectionList.Entry implements SelectableEntry {
/*     */   private static final int ICON_SIZE = 32;
/*     */   
/*     */   private final WorldSelectionList list;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final Screen screen;
/*     */   
/*     */   private final LevelSummary summary;
/*     */   
/*     */   private final FaviconTexture icon;
/*     */   
/*     */   private final StringWidget worldNameText;
/*     */   
/*     */   private final StringWidget idAndLastPlayedText;
/*     */   
/*     */   private final StringWidget infoText;
/*     */   
/*     */   private Path iconFile;
/*     */   
/*     */   public WorldListEntry(WorldSelectionList list, LevelSummary summary) {
/* 326 */     this.list = list;
/* 327 */     this.minecraft = WorldSelectionList.access$000(list);
/* 328 */     this.screen = list.getScreen();
/* 329 */     this.summary = summary;
/* 330 */     this.icon = FaviconTexture.forWorld(this.minecraft.getTextureManager(), summary.getLevelId());
/* 331 */     this.iconFile = summary.getIcon();
/* 333 */     int maxTextWidth = list.getRowWidth() - getTextX() - 2;
/* 334 */     MutableComponent mutableComponent1 = Component.literal(summary.getLevelName());
/* 335 */     this.worldNameText = new StringWidget((Component)mutableComponent1, this.minecraft.font);
/* 336 */     this.worldNameText.setMaxWidth(maxTextWidth);
/* 337 */     if (this.minecraft.font.width((FormattedText)mutableComponent1) > maxTextWidth)
/* 338 */       this.worldNameText.setTooltip(Tooltip.create((Component)mutableComponent1)); 
/* 341 */     String levelIdAndDate = summary.getLevelId();
/* 342 */     long lastPlayed = summary.getLastPlayed();
/* 343 */     if (lastPlayed != -1L) {
/* 344 */       ZonedDateTime lastPlayedTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(lastPlayed), ZoneId.systemDefault());
/* 345 */       levelIdAndDate = levelIdAndDate + " (" + levelIdAndDate + ")";
/*     */     } 
/* 347 */     MutableComponent mutableComponent2 = Component.literal(levelIdAndDate).withColor(-8355712);
/* 348 */     this.idAndLastPlayedText = new StringWidget((Component)mutableComponent2, this.minecraft.font);
/* 349 */     this.idAndLastPlayedText.setMaxWidth(maxTextWidth);
/* 350 */     if (this.minecraft.font.width(levelIdAndDate) > maxTextWidth)
/* 351 */       this.idAndLastPlayedText.setTooltip(Tooltip.create((Component)mutableComponent2)); 
/* 354 */     Component info = ComponentUtils.mergeStyles(summary.getInfo(), Style.EMPTY.withColor(-8355712));
/* 355 */     this.infoText = new StringWidget(info, this.minecraft.font);
/* 356 */     this.infoText.setMaxWidth(maxTextWidth);
/* 357 */     if (this.minecraft.font.width((FormattedText)info) > maxTextWidth)
/* 358 */       this.infoText.setTooltip(Tooltip.create(info)); 
/* 361 */     validateIconFile();
/* 362 */     loadIcon();
/*     */   }
/*     */   
/*     */   private void validateIconFile() {
/* 366 */     if (this.iconFile == null)
/*     */       return; 
/*     */     try {
/* 370 */       BasicFileAttributes attributes = Files.readAttributes(this.iconFile, BasicFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/* 371 */       if (attributes.isSymbolicLink()) {
/* 372 */         List<ForbiddenSymlinkInfo> issues = this.minecraft.directoryValidator().validateSymlink(this.iconFile);
/* 373 */         if (!issues.isEmpty()) {
/* 374 */           WorldSelectionList.LOGGER.warn("{}", ContentValidationException.getMessage(this.iconFile, issues));
/* 375 */           this.iconFile = null;
/*     */         } else {
/* 377 */           attributes = Files.readAttributes(this.iconFile, BasicFileAttributes.class, new LinkOption[0]);
/*     */         } 
/*     */       } 
/* 380 */       if (!attributes.isRegularFile())
/* 381 */         this.iconFile = null; 
/* 383 */     } catch (NoSuchFileException e) {
/* 384 */       this.iconFile = null;
/* 385 */     } catch (IOException e) {
/* 386 */       WorldSelectionList.LOGGER.error("could not validate symlink", e);
/* 387 */       this.iconFile = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 393 */     MutableComponent mutableComponent = Component.translatable("narrator.select.world_info", new Object[] { this.summary
/* 394 */           .getLevelName(), 
/* 395 */           Component.translationArg(new Date(this.summary.getLastPlayed())), this.summary
/* 396 */           .getInfo() });
/* 398 */     if (this.summary.isLocked())
/* 399 */       mutableComponent = CommonComponents.joinForNarration(new Component[] { (Component)mutableComponent, WorldSelectionList.WORLD_LOCKED_TOOLTIP }); 
/* 401 */     if (this.summary.isExperimental())
/* 402 */       mutableComponent = CommonComponents.joinForNarration(new Component[] { (Component)mutableComponent, WorldSelectionList.WORLD_EXPERIMENTAL }); 
/* 404 */     return (Component)Component.translatable("narrator.select", new Object[] { mutableComponent });
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 409 */     int textX = getTextX();
/* 410 */     this.worldNameText.setPosition(textX, getContentY() + 1);
/* 411 */     this.worldNameText.extractRenderState(graphics, mouseX, mouseY, a);
/* 413 */     Objects.requireNonNull(this.minecraft.font);
/* 413 */     this.idAndLastPlayedText.setPosition(textX, getContentY() + 9 + 3);
/* 414 */     this.idAndLastPlayedText.extractRenderState(graphics, mouseX, mouseY, a);
/* 416 */     Objects.requireNonNull(this.minecraft.font);
/* 416 */     Objects.requireNonNull(this.minecraft.font);
/* 416 */     this.infoText.setPosition(textX, getContentY() + 9 + 9 + 3);
/* 417 */     this.infoText.extractRenderState(graphics, mouseX, mouseY, a);
/* 419 */     graphics.blit(RenderPipelines.GUI_TEXTURED, this.icon.textureLocation(), getContentX(), getContentY(), 0.0F, 0.0F, 32, 32, 32, 32);
/* 421 */     if (this.list.entryType == WorldSelectionList.EntryType.SINGLEPLAYER && hovered) {
/* 422 */       graphics.fill(getContentX(), getContentY(), getContentX() + 32, getContentY() + 32, -1601138544);
/* 423 */       int relX = mouseX - getContentX();
/* 424 */       int relY = mouseY - getContentY();
/* 426 */       boolean isOverIcon = mouseOverIcon(relX, relY, 32);
/* 427 */       Identifier joinSprite = isOverIcon ? WorldSelectionList.JOIN_HIGHLIGHTED_SPRITE : WorldSelectionList.JOIN_SPRITE;
/* 428 */       Identifier warningSprite = isOverIcon ? WorldSelectionList.WARNING_HIGHLIGHTED_SPRITE : WorldSelectionList.WARNING_SPRITE;
/* 429 */       Identifier errorSprite = isOverIcon ? WorldSelectionList.ERROR_HIGHLIGHTED_SPRITE : WorldSelectionList.ERROR_SPRITE;
/* 430 */       Identifier joinWithErrorSprite = isOverIcon ? WorldSelectionList.MARKED_JOIN_HIGHLIGHTED_SPRITE : WorldSelectionList.MARKED_JOIN_SPRITE;
/* 431 */       if (this.summary instanceof LevelSummary.SymlinkLevelSummary || this.summary instanceof LevelSummary.CorruptedLevelSummary) {
/* 432 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, getContentX(), getContentY(), 32, 32);
/* 433 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, joinWithErrorSprite, getContentX(), getContentY(), 32, 32);
/*     */         return;
/*     */       } 
/* 437 */       if (this.summary.isLocked()) {
/* 438 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, getContentX(), getContentY(), 32, 32);
/* 439 */         if (isOverIcon)
/* 440 */           graphics.setTooltipForNextFrame(this.minecraft.font.split((FormattedText)WorldSelectionList.WORLD_LOCKED_TOOLTIP, 175), mouseX, mouseY); 
/* 442 */       } else if (this.summary.requiresManualConversion()) {
/* 443 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, getContentX(), getContentY(), 32, 32);
/* 444 */         if (isOverIcon)
/* 445 */           graphics.setTooltipForNextFrame(this.minecraft.font.split((FormattedText)WorldSelectionList.WORLD_REQUIRES_CONVERSION, 175), mouseX, mouseY); 
/* 447 */       } else if (!this.summary.isCompatible()) {
/* 448 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, getContentX(), getContentY(), 32, 32);
/* 449 */         if (isOverIcon)
/* 450 */           graphics.setTooltipForNextFrame(this.minecraft.font.split((FormattedText)WorldSelectionList.INCOMPATIBLE_VERSION_TOOLTIP, 175), mouseX, mouseY); 
/* 452 */       } else if (this.summary.shouldBackup()) {
/* 453 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, joinWithErrorSprite, getContentX(), getContentY(), 32, 32);
/* 454 */         if (this.summary.isDowngrade()) {
/* 455 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, getContentX(), getContentY(), 32, 32);
/* 456 */           if (isOverIcon)
/* 457 */             graphics.setTooltipForNextFrame((List)ImmutableList.of(WorldSelectionList.FROM_NEWER_TOOLTIP_1.getVisualOrderText(), WorldSelectionList.FROM_NEWER_TOOLTIP_2.getVisualOrderText()), mouseX, mouseY); 
/* 459 */         } else if (!SharedConstants.getCurrentVersion().stable()) {
/* 460 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, warningSprite, getContentX(), getContentY(), 32, 32);
/* 461 */           if (isOverIcon)
/* 462 */             graphics.setTooltipForNextFrame((List)ImmutableList.of(WorldSelectionList.SNAPSHOT_TOOLTIP_1.getVisualOrderText(), WorldSelectionList.SNAPSHOT_TOOLTIP_2.getVisualOrderText()), mouseX, mouseY); 
/*     */         } 
/* 465 */         if (isOverIcon)
/* 466 */           WorldSelectionList.access$100(WorldSelectionList.this, graphics); 
/*     */       } else {
/* 469 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, joinSprite, getContentX(), getContentY(), 32, 32);
/* 470 */         if (isOverIcon)
/* 471 */           WorldSelectionList.access$200(WorldSelectionList.this, graphics); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getTextX() {
/* 478 */     return getContentX() + 32 + 3;
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 483 */     if (canInteract()) {
/* 484 */       int relX = (int)event.x() - getContentX();
/* 485 */       int relY = (int)event.y() - getContentY();
/* 487 */       if (doubleClick || (mouseOverIcon(relX, relY, 32) && this.list.entryType == WorldSelectionList.EntryType.SINGLEPLAYER)) {
/* 488 */         this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/* 489 */         Consumer<WorldListEntry> onEntryInteract = this.list.onEntryInteract;
/* 490 */         if (onEntryInteract != null) {
/* 491 */           onEntryInteract.accept(this);
/* 492 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 496 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 501 */     if (event.isSelection() && canInteract()) {
/* 502 */       this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/* 503 */       Consumer<WorldListEntry> onEntryInteract = this.list.onEntryInteract;
/* 504 */       if (onEntryInteract != null) {
/* 505 */         onEntryInteract.accept(this);
/* 506 */         return true;
/*     */       } 
/*     */     } 
/* 509 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public boolean canInteract() {
/* 513 */     return (this.summary.primaryActionActive() || this.list.entryType == WorldSelectionList.EntryType.UPLOAD_WORLD);
/*     */   }
/*     */   
/*     */   public void joinWorld() {
/* 517 */     if (!this.summary.primaryActionActive())
/*     */       return; 
/* 521 */     if (this.summary instanceof LevelSummary.SymlinkLevelSummary) {
/* 522 */       this.minecraft.gui.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.gui.setScreen(this.screen)));
/*     */       return;
/*     */     } 
/* 526 */     Objects.requireNonNull(this.list);
/* 526 */     this.minecraft.createWorldOpenFlows().openWorld(this.summary.getLevelId(), this.list::returnToScreen);
/*     */   }
/*     */   
/*     */   public void deleteWorld() {
/* 530 */     this.minecraft.gui.setScreen((Screen)new ConfirmScreen(result -> {
/*     */             if (result) {
/*     */               this.minecraft.gui.setScreen((Screen)new ProgressScreen(true));
/*     */               doDeleteWorld();
/*     */             } 
/*     */             this.list.returnToScreen();
/* 538 */           }(Component)Component.translatable("selectWorld.deleteQuestion"), 
/* 539 */           (Component)Component.translatable("selectWorld.deleteWarning", new Object[] { this.summary.getLevelName() }), (Component)Component.translatable("selectWorld.deleteButton"), CommonComponents.GUI_CANCEL));
/*     */   }
/*     */   
/*     */   public void doDeleteWorld() {
/* 546 */     LevelStorageSource levelSource = this.minecraft.getLevelSource();
/* 547 */     String levelId = this.summary.getLevelId();
/*     */     try {
/* 549 */       LevelStorageSource.LevelStorageAccess access = levelSource.createAccess(levelId);
/*     */       try {
/* 550 */         access.deleteLevel();
/* 551 */         if (access != null)
/* 551 */           access.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (access != null)
/*     */           try {
/*     */             access.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 551 */     } catch (IOException e) {
/* 552 */       SystemToast.onWorldDeleteFailure(this.minecraft, levelId);
/* 553 */       WorldSelectionList.LOGGER.error("Failed to delete world {}", levelId, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void editWorld() {
/*     */     LevelStorageSource.LevelStorageAccess access;
/*     */     EditWorldScreen editScreen;
/* 558 */     queueLoadScreen();
/* 559 */     String levelId = this.summary.getLevelId();
/*     */     try {
/* 562 */       access = this.minecraft.getLevelSource().validateAndCreateAccess(levelId);
/* 563 */     } catch (IOException e) {
/* 564 */       SystemToast.onWorldAccessFailure(this.minecraft, levelId);
/* 565 */       WorldSelectionList.LOGGER.error("Failed to access level {}", levelId, e);
/* 566 */       this.list.reloadWorldList();
/*     */       return;
/* 568 */     } catch (ContentValidationException e) {
/* 569 */       WorldSelectionList.LOGGER.warn("{}", e.getMessage());
/* 570 */       this.minecraft.gui.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.gui.setScreen(this.screen)));
/*     */       return;
/*     */     } 
/*     */     try {
/* 575 */       editScreen = EditWorldScreen.create(this.minecraft, access, result -> {
/*     */             access.safeClose();
/*     */             this.list.returnToScreen();
/*     */           });
/* 581 */     } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException e) {
/* 582 */       access.safeClose();
/* 583 */       SystemToast.onWorldAccessFailure(this.minecraft, levelId);
/* 584 */       WorldSelectionList.LOGGER.error("Failed to load world data {}", levelId, e);
/* 585 */       this.list.reloadWorldList();
/*     */       return;
/*     */     } 
/* 588 */     this.minecraft.gui.setScreen(editScreen);
/*     */   }
/*     */   
/*     */   public void recreateWorld() {
/* 592 */     queueLoadScreen();
/*     */     try {
/* 594 */       LevelStorageSource.LevelStorageAccess access = this.minecraft.getLevelSource().validateAndCreateAccess(this.summary.getLevelId());
/*     */       try {
/* 596 */         Pair<LevelSettings, WorldCreationContext> recreatedSettings = this.minecraft.createWorldOpenFlows().recreateWorldData(access);
/* 597 */         LevelSettings levelSettings = (LevelSettings)recreatedSettings.getFirst();
/* 598 */         WorldCreationContext creationContext = (WorldCreationContext)recreatedSettings.getSecond();
/* 599 */         Path dataPackDir = CreateWorldScreen.createTempDataPackDirFromExistingWorld(access.getLevelPath(LevelResource.DATAPACK_DIR), this.minecraft);
/* 601 */         creationContext.validate();
/* 602 */         if (creationContext.options().isOldCustomizedWorld()) {
/* 603 */           this.minecraft.gui.setScreen((Screen)new ConfirmScreen(result -> {
/*     */                   Objects.requireNonNull(this.list);
/*     */                   this.minecraft.gui.setScreen(result ? CreateWorldScreen.createFromExisting(this.minecraft, this.list::returnToScreen, levelSettings, creationContext, dataPackDir) : this.screen);
/* 605 */                 }(Component)Component.translatable("selectWorld.recreate.customized.title"), 
/* 606 */                 (Component)Component.translatable("selectWorld.recreate.customized.text"), CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL));
/*     */         } else {
/* 611 */           Objects.requireNonNull(this.list);
/* 611 */           this.minecraft.gui.setScreen(CreateWorldScreen.createFromExisting(this.minecraft, this.list::returnToScreen, levelSettings, creationContext, dataPackDir));
/*     */         } 
/* 613 */         if (access != null)
/* 613 */           access.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (access != null)
/*     */           try {
/*     */             access.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 613 */     } catch (ContentValidationException e) {
/* 614 */       WorldSelectionList.LOGGER.warn("{}", e.getMessage());
/* 615 */       this.minecraft.gui.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.gui.setScreen(this.screen)));
/* 616 */     } catch (Exception e) {
/* 617 */       WorldSelectionList.LOGGER.error("Unable to recreate world", e);
/* 618 */       this.minecraft.gui.setScreen((Screen)new AlertScreen(() -> this.minecraft.gui.setScreen(this.screen), 
/*     */             
/* 620 */             (Component)Component.translatable("selectWorld.recreate.error.title"), 
/* 621 */             (Component)Component.translatable("selectWorld.recreate.error.text")));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void queueLoadScreen() {
/* 627 */     this.minecraft.setScreenAndShow((Screen)new GenericMessageScreen((Component)Component.translatable("selectWorld.data_read")));
/*     */   }
/*     */   
/*     */   private void loadIcon() {
/* 631 */     boolean shouldHaveIcon = (this.iconFile != null && Files.isRegularFile(this.iconFile, new LinkOption[0]));
/* 632 */     if (shouldHaveIcon) {
/*     */       try {
/* 633 */         InputStream stream = Files.newInputStream(this.iconFile, new java.nio.file.OpenOption[0]);
/*     */         try {
/* 634 */           this.icon.upload(NativeImage.read(stream));
/* 635 */           if (stream != null)
/* 635 */             stream.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (stream != null)
/*     */             try {
/*     */               stream.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 635 */       } catch (Throwable t) {
/* 636 */         WorldSelectionList.LOGGER.error("Invalid icon for world {}", this.summary.getLevelId(), t);
/* 637 */         this.iconFile = null;
/*     */       } 
/*     */     } else {
/* 640 */       this.icon.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 646 */     if (!this.icon.isClosed())
/* 647 */       this.icon.close(); 
/*     */   }
/*     */   
/*     */   public String getLevelName() {
/* 652 */     return this.summary.getLevelName();
/*     */   }
/*     */   
/*     */   public LevelSummary getLevelSummary() {
/* 657 */     return this.summary;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldSelectionList$WorldListEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */