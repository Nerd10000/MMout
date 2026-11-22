/*     */ package net.minecraft.client.resources.model.cuboid;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ enum SideDirection {
/* 204 */   UP(Direction.UP),
/* 205 */   DOWN(Direction.DOWN),
/* 206 */   LEFT(Direction.EAST),
/* 207 */   RIGHT(Direction.WEST);
/*     */   
/*     */   private final Direction direction;
/*     */   
/*     */   SideDirection(Direction direction) {
/* 212 */     this.direction = direction;
/*     */   }
/*     */   
/*     */   public Direction getDirection() {
/* 216 */     return this.direction;
/*     */   }
/*     */   
/*     */   private boolean isHorizontal() {
/* 220 */     return (this == DOWN || this == UP);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\ItemModelGenerator$SideDirection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */