/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.TypedInstance;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class DebugEntryLookingAtTags<T extends TypedInstance<?>> extends DebugEntryLookingAt {
/*    */   protected abstract T getInstance(Level paramLevel, BlockPos paramBlockPos);
/*    */   
/*    */   public void extractInfo(List<String> result, Level level, BlockPos pos) {
/* 97 */     T instance = getInstance(level, pos);
/* 98 */     addTagEntries(result, (TypedInstance<?>)instance);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryLookingAt$DebugEntryLookingAtTags.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */