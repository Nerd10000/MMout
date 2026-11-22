/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.TextAlignment;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.Checkbox;
/*    */ import net.minecraft.client.gui.components.MultiLineLabel;
/*    */ import net.minecraft.client.input.KeyEvent;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class BackupConfirmScreen extends Screen {
/* 15 */   private static final Component SKIP_AND_JOIN = (Component)Component.translatable("selectWorld.backupJoinSkipButton");
/*    */   
/* 16 */   public static final Component BACKUP_AND_JOIN = (Component)Component.translatable("selectWorld.backupJoinConfirmButton");
/*    */   
/*    */   private final Runnable onCancel;
/*    */   
/*    */   protected final Listener onProceed;
/*    */   
/*    */   private final Component description;
/*    */   
/*    */   private final boolean promptForCacheErase;
/*    */   
/* 22 */   private MultiLineLabel message = MultiLineLabel.EMPTY;
/*    */   
/*    */   private final Component confirmation;
/*    */   
/*    */   protected int id;
/*    */   
/*    */   private Checkbox eraseCache;
/*    */   
/*    */   public BackupConfirmScreen(Runnable onCancel, Listener onProceed, Component title, Component description, boolean promptForCacheErase) {
/* 28 */     this(onCancel, onProceed, title, description, BACKUP_AND_JOIN, promptForCacheErase);
/*    */   }
/*    */   
/*    */   public BackupConfirmScreen(Runnable onCancel, Listener onProceed, Component title, Component description, Component confirmation, boolean promptForCacheErase) {
/* 32 */     super(title);
/* 33 */     this.onCancel = onCancel;
/* 34 */     this.onProceed = onProceed;
/* 35 */     this.description = description;
/* 36 */     this.promptForCacheErase = promptForCacheErase;
/* 37 */     this.confirmation = confirmation;
/*    */   }
/*    */   
/*    */   protected void init() {
/* 42 */     super.init();
/* 44 */     this.message = MultiLineLabel.create(this.font, this.description, this.width - 50);
/* 46 */     Objects.requireNonNull(this.font);
/* 46 */     int textSize = (this.message.getLineCount() + 1) * 9;
/* 47 */     this.eraseCache = Checkbox.builder((Component)Component.translatable("selectWorld.backupEraseCache").withColor(-2039584), this.font).pos(this.width / 2 - 155 + 80, 76 + textSize).build();
/* 48 */     if (this.promptForCacheErase)
/* 49 */       addRenderableWidget(this.eraseCache); 
/* 51 */     addRenderableWidget(Button.builder(this.confirmation, button -> this.onProceed.proceed(true, this.eraseCache.selected())).bounds(this.width / 2 - 155, 100 + textSize, 150, 20).build());
/* 52 */     Button skipAndJoinButton = Button.builder(SKIP_AND_JOIN, button -> this.onProceed.proceed(false, this.eraseCache.selected())).bounds(this.width / 2 - 155 + 160, 100 + textSize, 150, 20).build();
/* 53 */     addRenderableWidget(skipAndJoinButton);
/* 54 */     addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> this.onCancel.run()).bounds(this.width / 2 - 155 + 80, 124 + textSize, 150, 20).build());
/*    */   }
/*    */   
/*    */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 59 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 60 */     ActiveTextCollector textRenderer = graphics.textRenderer();
/* 61 */     graphics.centeredText(this.font, this.title, this.width / 2, 50, -1);
/* 62 */     Objects.requireNonNull(this.font);
/* 62 */     this.message.visitLines(TextAlignment.CENTER, this.width / 2, 70, 9, textRenderer);
/*    */   }
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 67 */     return false;
/*    */   }
/*    */   
/*    */   public boolean keyPressed(KeyEvent event) {
/* 72 */     if (event.isEscape()) {
/* 73 */       this.onCancel.run();
/* 74 */       return true;
/*    */     } 
/* 76 */     return super.keyPressed(event);
/*    */   }
/*    */   
/*    */   public static interface Listener {
/*    */     void proceed(boolean param1Boolean1, boolean param1Boolean2);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\BackupConfirmScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */