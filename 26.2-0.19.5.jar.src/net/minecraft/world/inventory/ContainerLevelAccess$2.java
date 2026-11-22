/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ class null implements ContainerLevelAccess {
/*    */   public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> action) {
/* 22 */     return Optional.of(action.apply(level, pos));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\ContainerLevelAccess$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */