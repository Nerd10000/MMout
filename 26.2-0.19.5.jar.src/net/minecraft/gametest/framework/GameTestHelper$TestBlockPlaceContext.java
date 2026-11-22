/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ class TestBlockPlaceContext extends BlockPlaceContext {
/*     */   private final Direction placeDirection;
/*     */   
/*     */   public TestBlockPlaceContext(GameTestHelper paramGameTestHelper, Level level, InteractionHand hand, ItemStack itemStackInHand, BlockHitResult hitResult, Direction placeDirection) {
/* 440 */     super(level, null, hand, itemStackInHand, hitResult);
/* 441 */     this.placeDirection = placeDirection;
/*     */   }
/*     */   
/*     */   public Direction getNearestLookingDirection() {
/* 446 */     return this.placeDirection;
/*     */   }
/*     */   
/*     */   public Direction[] getNearestLookingDirections() {
/* 451 */     Direction[] directions = Direction.values();
/* 452 */     Arrays.sort(directions, Comparator.comparingDouble(d -> d.getUnitVec3().distanceTo(this.placeDirection.getUnitVec3())));
/* 453 */     return directions;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestHelper$TestBlockPlaceContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */