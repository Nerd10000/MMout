/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ClipBlockStateContext {
/*    */   private final Vec3 from;
/*    */   
/*    */   private final Vec3 to;
/*    */   
/*    */   private final Predicate<BlockState> block;
/*    */   
/*    */   public ClipBlockStateContext(Vec3 from, Vec3 to, Predicate<BlockState> block) {
/* 14 */     this.from = from;
/* 15 */     this.to = to;
/* 16 */     this.block = block;
/*    */   }
/*    */   
/*    */   public Vec3 getTo() {
/* 20 */     return this.to;
/*    */   }
/*    */   
/*    */   public Vec3 getFrom() {
/* 24 */     return this.from;
/*    */   }
/*    */   
/*    */   public Predicate<BlockState> isTargetBlock() {
/* 28 */     return this.block;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\ClipBlockStateContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */