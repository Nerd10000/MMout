/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.TypedInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryLookingAtEntityTags implements DebugScreenEntry {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 15 */     Minecraft minecraft = Minecraft.getInstance();
/* 16 */     Entity entity = minecraft.crosshairPickEntity;
/* 17 */     List<String> result = new ArrayList<>();
/* 18 */     if (entity != null)
/* 19 */       DebugEntryLookingAt.addTagEntries(result, (TypedInstance<?>)entity); 
/* 23 */     displayer.addToGroup(DebugEntryLookingAtEntity.GROUP, result);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryLookingAtEntityTags.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */