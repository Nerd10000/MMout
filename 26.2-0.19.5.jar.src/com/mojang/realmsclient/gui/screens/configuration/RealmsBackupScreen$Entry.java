/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import com.mojang.realmsclient.dto.Backup;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsPopups;
/*     */ import com.mojang.realmsclient.util.RealmsUtil;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import com.mojang.realmsclient.util.task.RestoreTask;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ class Entry extends ContainerObjectSelectionList.Entry<RealmsBackupScreen.Entry> {
/*     */   private static final int Y_PADDING = 2;
/*     */   
/*     */   private final Backup backup;
/*     */   
/*     */   private Button restoreButton;
/*     */   
/*     */   private Button changesButton;
/*     */   
/* 172 */   private final List<AbstractWidget> children = new ArrayList<>();
/*     */   
/*     */   public Entry(Backup backup) {
/* 175 */     this.backup = backup;
/* 177 */     populateChangeList(backup);
/* 179 */     if (!backup.changeList.isEmpty()) {
/* 180 */       this
/*     */         
/* 183 */         .changesButton = Button.builder(RealmsBackupScreen.HAS_CHANGES_TOOLTIP, paramButton -> (RealmsBackupScreen.access$200(RealmsBackupScreen.this)).gui.setScreen((Screen)new RealmsBackupInfoScreen((Screen)RealmsBackupScreen.this, this.backup))).width(8 + RealmsBackupScreen.access$100(paramRealmsBackupScreen).width((FormattedText)RealmsBackupScreen.HAS_CHANGES_TOOLTIP)).createNarration(this::narrationForBackupEntry).build();
/* 184 */       this.children.add(this.changesButton);
/*     */     } 
/* 187 */     if (!paramRealmsBackupScreen.serverData.expired) {
/* 188 */       this
/*     */         
/* 191 */         .restoreButton = Button.builder(RealmsBackupScreen.RESTORE_TOOLTIP, button -> restoreClicked()).width(8 + RealmsBackupScreen.access$300(paramRealmsBackupScreen).width((FormattedText)RealmsBackupScreen.HAS_CHANGES_TOOLTIP)).createNarration(this::narrationForBackupEntry).build();
/* 192 */       this.children.add(this.restoreButton);
/*     */     } 
/*     */   }
/*     */   
/*     */   private MutableComponent narrationForBackupEntry(Supplier<MutableComponent> defaultNarrationSupplier) {
/* 197 */     return CommonComponents.joinForNarration(new Component[] { (Component)Component.translatable("mco.backup.narration", new Object[] { RealmsBackupScreen.SHORT_DATE_FORMAT.format(this.backup.lastModifiedDate()) }), (Component)defaultNarrationSupplier
/* 199 */           .get() });
/*     */   }
/*     */   
/*     */   private void populateChangeList(Backup backup) {
/* 203 */     int index = RealmsBackupScreen.this.backups.indexOf(backup);
/* 204 */     if (index == RealmsBackupScreen.this.backups.size() - 1)
/*     */       return; 
/* 207 */     Backup olderBackup = RealmsBackupScreen.this.backups.get(index + 1);
/* 208 */     for (String key : backup.metadata.keySet()) {
/* 209 */       if (!key.contains("uploaded") && olderBackup.metadata.containsKey(key)) {
/* 210 */         if (!((String)backup.metadata.get(key)).equals(olderBackup.metadata.get(key)))
/* 211 */           addToChangeList(key); 
/*     */         continue;
/*     */       } 
/* 214 */       addToChangeList(key);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addToChangeList(String key) {
/* 220 */     if (key.contains("uploaded")) {
/* 221 */       String uploadedTime = RealmsBackupScreen.SHORT_DATE_FORMAT.format(this.backup.lastModifiedDate());
/* 222 */       this.backup.changeList.put(key, uploadedTime);
/* 223 */       this.backup.uploadedVersion = true;
/*     */     } else {
/* 225 */       this.backup.changeList.put(key, (String)this.backup.metadata.get(key));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void restoreClicked() {
/* 230 */     Component age = RealmsUtil.convertToAgePresentationFromInstant(this.backup.lastModified);
/* 231 */     String lastModifiedDate = RealmsBackupScreen.SHORT_DATE_FORMAT.format(this.backup.lastModifiedDate());
/* 232 */     MutableComponent mutableComponent = Component.translatable("mco.configure.world.restore.question.line1", new Object[] { lastModifiedDate, age });
/* 233 */     (RealmsBackupScreen.access$500(RealmsBackupScreen.this)).gui.setScreen((Screen)RealmsPopups.warningPopupScreen((Screen)RealmsBackupScreen.this, (Component)mutableComponent, paramPopupScreen -> {
/*     */             RealmsConfigureWorldScreen newScreen = RealmsBackupScreen.this.lastScreen.getNewScreen();
/*     */             (RealmsBackupScreen.access$400(RealmsBackupScreen.this)).gui.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)newScreen, new LongRunningTask[] { (LongRunningTask)new RestoreTask(this.backup, this.this$0.serverData.id, newScreen) }));
/*     */           }));
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 243 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 248 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 253 */     int middle = getContentYMiddle();
/* 254 */     Objects.requireNonNull(RealmsBackupScreen.access$600(RealmsBackupScreen.this));
/* 254 */     int firstLineYPos = middle - 9 - 2;
/* 255 */     int secondLineYPos = middle + 2;
/* 256 */     int color = this.backup.uploadedVersion ? -8388737 : -1;
/* 257 */     graphics.text(RealmsBackupScreen.access$700(RealmsBackupScreen.this), (Component)Component.translatable("mco.backup.entry", new Object[] { RealmsUtil.convertToAgePresentationFromInstant(this.backup.lastModified) }), getContentX(), firstLineYPos, color);
/* 258 */     graphics.text(RealmsBackupScreen.access$800(RealmsBackupScreen.this), RealmsBackupScreen.SHORT_DATE_FORMAT.format(this.backup.lastModifiedDate()), getContentX(), secondLineYPos, -11776948);
/* 259 */     int iconXOffet = 0;
/* 260 */     int iconYPos = getContentYMiddle() - 10;
/* 261 */     if (this.restoreButton != null) {
/* 262 */       iconXOffet += this.restoreButton.getWidth() + 8;
/* 263 */       this.restoreButton.setX(getContentRight() - iconXOffet);
/* 264 */       this.restoreButton.setY(iconYPos);
/* 265 */       this.restoreButton.extractRenderState(graphics, mouseX, mouseY, a);
/*     */     } 
/* 267 */     if (this.changesButton != null) {
/* 268 */       iconXOffet += this.changesButton.getWidth() + 8;
/* 269 */       this.changesButton.setX(getContentRight() - iconXOffet);
/* 270 */       this.changesButton.setY(iconYPos);
/* 271 */       this.changesButton.extractRenderState(graphics, mouseX, mouseY, a);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsBackupScreen$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */