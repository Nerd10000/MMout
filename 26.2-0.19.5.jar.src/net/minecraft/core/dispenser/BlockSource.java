/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class BlockSource extends Record {
/*    */   private final ServerLevel level;
/*    */   
/*    */   private final BlockPos pos;
/*    */   
/*    */   private final BlockState state;
/*    */   
/*    */   private final DispenserBlockEntity blockEntity;
/*    */   
/*    */   public BlockSource(ServerLevel level, BlockPos pos, BlockState state, DispenserBlockEntity blockEntity) {
/*  9 */     this.level = level;
/*  9 */     this.pos = pos;
/*  9 */     this.state = state;
/*  9 */     this.blockEntity = blockEntity;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/dispenser/BlockSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/dispenser/BlockSource;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/dispenser/BlockSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/dispenser/BlockSource;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/dispenser/BlockSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/dispenser/BlockSource;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ServerLevel level() {
/*  9 */     return this.level;
/*    */   }
/*    */   
/*    */   public BlockPos pos() {
/*  9 */     return this.pos;
/*    */   }
/*    */   
/*    */   public BlockState state() {
/*  9 */     return this.state;
/*    */   }
/*    */   
/*    */   public DispenserBlockEntity blockEntity() {
/*  9 */     return this.blockEntity;
/*    */   }
/*    */   
/*    */   public Vec3 center() {
/* 11 */     return Vec3.atCenterOf((Vec3i)this.pos);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\BlockSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */