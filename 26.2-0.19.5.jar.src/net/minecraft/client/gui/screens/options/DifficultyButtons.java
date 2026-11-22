/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.CycleButton;
/*    */ import net.minecraft.client.gui.components.LockIconButton;
/*    */ import net.minecraft.client.gui.layouts.EqualSpacingLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
/*    */ import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class DifficultyButtons extends Record {
/*    */   private final LayoutElement layout;
/*    */   
/*    */   private final CycleButton<Difficulty> difficultyButton;
/*    */   
/*    */   private final LockIconButton lockButton;
/*    */   
/*    */   private final Level level;
/*    */   
/*    */   public DifficultyButtons(LayoutElement layout, CycleButton<Difficulty> difficultyButton, LockIconButton lockButton, Level level) {
/* 17 */     this.layout = layout;
/* 17 */     this.difficultyButton = difficultyButton;
/* 17 */     this.lockButton = lockButton;
/* 17 */     this.level = level;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/options/DifficultyButtons;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/options/DifficultyButtons;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/options/DifficultyButtons;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/options/DifficultyButtons;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/options/DifficultyButtons;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/options/DifficultyButtons;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public LayoutElement layout() {
/* 17 */     return this.layout;
/*    */   }
/*    */   
/*    */   public CycleButton<Difficulty> difficultyButton() {
/* 17 */     return this.difficultyButton;
/*    */   }
/*    */   
/*    */   public LockIconButton lockButton() {
/* 17 */     return this.lockButton;
/*    */   }
/*    */   
/*    */   public Level level() {
/* 17 */     return this.level;
/*    */   }
/*    */   
/*    */   public static DifficultyButtons create(Minecraft minecraft, Level level, Screen screen) {
/* 22 */     CycleButton<Difficulty> difficultyButton = CycleButton.builder(Difficulty::getDisplayName, level.getDifficulty()).withValues((Object[])Difficulty.values()).create(0, 0, 150, 20, (Component)Component.translatable("options.difficulty"), (button, value) -> minecraft.getConnection().send((Packet)new ServerboundChangeDifficultyPacket(value)));
/* 25 */     LockIconButton lockButton = new LockIconButton(0, 0, button -> minecraft.gui.setScreen((Screen)new ConfirmScreen((), (Component)Component.translatable("difficulty.lock.title"), (Component)Component.translatable("difficulty.lock.question", new Object[] { level.getLevelData().getDifficulty().getDisplayName() }))));
/* 32 */     difficultyButton.setWidth(difficultyButton.getWidth() - lockButton.getWidth());
/* 34 */     lockButton.setLocked(isDifficultyLocked(level));
/* 35 */     lockButton.active = (!lockButton.isLocked() && playerHasPermissionToChangeDifficulty(minecraft));
/* 36 */     difficultyButton.active = (!lockButton.isLocked() && playerHasPermissionToChangeDifficulty(minecraft));
/* 38 */     EqualSpacingLayout linearLayout = new EqualSpacingLayout(150, 0, EqualSpacingLayout.Orientation.HORIZONTAL);
/* 39 */     linearLayout.addChild((LayoutElement)difficultyButton);
/* 40 */     linearLayout.addChild((LayoutElement)lockButton);
/* 42 */     return new DifficultyButtons((LayoutElement)linearLayout, difficultyButton, lockButton, level);
/*    */   }
/*    */   
/*    */   public void refresh(Minecraft minecraft) {
/* 46 */     this.difficultyButton.setValue(this.level.getDifficulty());
/* 47 */     this.lockButton.setLocked(isDifficultyLocked(this.level));
/* 48 */     this.lockButton.active = (!this.lockButton.isLocked() && playerHasPermissionToChangeDifficulty(minecraft));
/* 49 */     this.difficultyButton.active = (!this.lockButton.isLocked() && playerHasPermissionToChangeDifficulty(minecraft));
/*    */   }
/*    */   
/*    */   private static boolean isDifficultyLocked(Level level) {
/* 53 */     return (level.getLevelData().isDifficultyLocked() || level.getLevelData().isHardcore());
/*    */   }
/*    */   
/*    */   private static boolean playerHasPermissionToChangeDifficulty(Minecraft minecraft) {
/* 59 */     return minecraft.hasSingleplayerServer();
/*    */   }
/*    */   
/*    */   private static void onLockCallback(boolean result, Minecraft minecraft, Screen screen, CycleButton<Difficulty> difficultyButton, LockIconButton lockButton) {
/* 69 */     minecraft.gui.setScreen(screen);
/* 70 */     if (result) {
/* 71 */       minecraft.getConnection().send((Packet)new ServerboundLockDifficultyPacket(true));
/* 72 */       lockButton.setLocked(true);
/* 73 */       lockButton.active = false;
/* 74 */       difficultyButton.active = false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\DifficultyButtons.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */