/*    */ package net.minecraft.client.gui.screens.debug;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ enum GameModeIcon {
/* 32 */   CREATIVE((Component)Component.translatable("gameMode.creative"), GameType.CREATIVE, new ItemStack((ItemLike)Blocks.GRASS_BLOCK)),
/* 33 */   SURVIVAL((Component)Component.translatable("gameMode.survival"), GameType.SURVIVAL, new ItemStack((ItemLike)Items.IRON_SWORD)),
/* 34 */   ADVENTURE((Component)Component.translatable("gameMode.adventure"), GameType.ADVENTURE, new ItemStack((ItemLike)Items.MAP)),
/* 35 */   SPECTATOR((Component)Component.translatable("gameMode.spectator"), GameType.SPECTATOR, new ItemStack((ItemLike)Items.ENDER_EYE));
/*    */   
/*    */   private static final GameModeIcon[] VALUES;
/*    */   
/*    */   private static final int ICON_AREA = 16;
/*    */   
/*    */   private static final int ICON_TOP_LEFT = 5;
/*    */   
/*    */   private final Component name;
/*    */   
/*    */   private final GameType mode;
/*    */   
/*    */   private final ItemStack renderStack;
/*    */   
/*    */   static {
/* 37 */     VALUES = values();
/*    */   }
/*    */   
/*    */   GameModeIcon(Component name, GameType mode, ItemStack renderStack) {
/* 46 */     this.name = name;
/* 47 */     this.mode = mode;
/* 48 */     this.renderStack = renderStack;
/*    */   }
/*    */   
/*    */   private void extractIcon(GuiGraphicsExtractor graphics, int x, int y) {
/* 52 */     graphics.item(this.renderStack, x, y);
/*    */   }
/*    */   
/*    */   private GameModeIcon getNext() {
/* 56 */     switch (ordinal()) {
/*    */       default:
/* 56 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */         break;
/*    */     } 
/* 56 */     return 
/*    */       
/* 60 */       CREATIVE;
/*    */   }
/*    */   
/*    */   private static GameModeIcon getFromGameType(GameType gameType) {
/* 65 */     switch (GameModeSwitcherScreen.null.$SwitchMap$net$minecraft$world$level$GameType[gameType.ordinal()]) {
/*    */       default:
/* 65 */         throw new MatchException(null, null);
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */       case 4:
/*    */         break;
/*    */     } 
/* 65 */     return 
/*    */       
/* 69 */       ADVENTURE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\debug\GameModeSwitcherScreen$GameModeIcon.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */