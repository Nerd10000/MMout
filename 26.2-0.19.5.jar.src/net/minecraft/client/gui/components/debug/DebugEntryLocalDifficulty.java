/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryLocalDifficulty implements DebugScreenEntry {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/*    */     ServerLevel serverLevel;
/* 17 */     Minecraft minecraft = Minecraft.getInstance();
/* 18 */     Entity entity = minecraft.getCameraEntity();
/* 20 */     if (entity != null && serverChunk != null && serverOrClientLevel instanceof ServerLevel) {
/* 20 */       serverLevel = (ServerLevel)serverOrClientLevel;
/*    */     } else {
/*    */       return;
/*    */     } 
/* 24 */     BlockPos feetPos = entity.blockPosition();
/* 26 */     if (serverLevel.isInsideBuildHeight(feetPos.getY())) {
/* 27 */       float moonBrightness = serverLevel.getMoonBrightness(feetPos);
/* 28 */       long localTime = serverChunk.getInhabitedTime();
/* 29 */       DifficultyInstance localDifficulty = new DifficultyInstance(serverLevel.getDifficulty(), serverLevel.getOverworldClockTime(), localTime, moonBrightness);
/* 30 */       displayer.addLine(String.format(Locale.ROOT, "Local Difficulty: %.2f // %.2f", new Object[] { Float.valueOf(localDifficulty.getEffectiveDifficulty()), Float.valueOf(localDifficulty.getSpecialMultiplier()) }));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryLocalDifficulty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */