/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface Coordinates {
/*    */   Vec3 getPosition(CommandSourceStack paramCommandSourceStack);
/*    */   
/*    */   Vec2 getRotation(CommandSourceStack paramCommandSourceStack);
/*    */   
/*    */   default BlockPos getBlockPos(CommandSourceStack sender) {
/* 14 */     return BlockPos.containing((Position)getPosition(sender));
/*    */   }
/*    */   
/*    */   boolean isXRelative();
/*    */   
/*    */   boolean isYRelative();
/*    */   
/*    */   boolean isZRelative();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\coordinates\Coordinates.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */