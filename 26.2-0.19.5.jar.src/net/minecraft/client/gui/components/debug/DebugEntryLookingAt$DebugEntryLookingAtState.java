/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.TypedInstance;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.StateHolder;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public abstract class DebugEntryLookingAtState<OwnerType, StateType extends StateHolder<OwnerType, StateType> & TypedInstance<OwnerType>> extends DebugEntryLookingAt {
/*    */   private final String prefix;
/*    */   
/*    */   protected DebugEntryLookingAtState(String prefix) {
/* 60 */     this.prefix = prefix;
/*    */   }
/*    */   
/*    */   public void extractInfo(List<String> result, Level level, BlockPos pos) {
/* 67 */     StateType stateInstance = getInstance(level, pos);
/* 69 */     result.add(String.valueOf(ChatFormatting.UNDERLINE) + String.valueOf(ChatFormatting.UNDERLINE) + ": " + this.prefix + ", " + pos.getX() + ", " + pos.getY());
/* 70 */     result.add(((TypedInstance)stateInstance).typeHolder().getRegisteredName());
/* 72 */     addStateProperties(result, (StateHolder<?, ?>)stateInstance);
/*    */   }
/*    */   
/*    */   private static void addStateProperties(List<String> result, StateHolder<?, ?> stateHolder) {
/* 76 */     stateHolder.getValues().forEach(entry -> result.add(getPropertyValueString(entry)));
/*    */   }
/*    */   
/*    */   private static String getPropertyValueString(Property.Value<?> entry) {
/* 80 */     String valueString = entry.valueName();
/* 82 */     if (Boolean.TRUE.equals(entry.value())) {
/* 83 */       valueString = String.valueOf(ChatFormatting.GREEN) + String.valueOf(ChatFormatting.GREEN);
/* 84 */     } else if (Boolean.FALSE.equals(entry.value())) {
/* 85 */       valueString = String.valueOf(ChatFormatting.RED) + String.valueOf(ChatFormatting.RED);
/*    */     } 
/* 88 */     return entry.property().getName() + ": " + entry.property().getName();
/*    */   }
/*    */   
/*    */   protected abstract StateType getInstance(Level paramLevel, BlockPos paramBlockPos);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryLookingAt$DebugEntryLookingAtState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */