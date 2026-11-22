/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryLookingAtEntity implements DebugScreenEntry {
/* 16 */   public static final Identifier GROUP = Identifier.withDefaultNamespace("looking_at_entity");
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 20 */     Minecraft minecraft = Minecraft.getInstance();
/* 21 */     Entity entity = minecraft.crosshairPickEntity;
/* 22 */     List<String> result = new ArrayList<>();
/* 23 */     if (entity != null) {
/* 24 */       result.add(String.valueOf(ChatFormatting.UNDERLINE) + "Targeted Entity");
/* 25 */       result.add(entity.typeHolder().getRegisteredName());
/*    */     } 
/* 29 */     displayer.addToGroup(GROUP, result);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryLookingAtEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */