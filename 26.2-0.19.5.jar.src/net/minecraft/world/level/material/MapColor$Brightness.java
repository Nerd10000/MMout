/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ 
/*     */ public enum Brightness {
/* 115 */   LOW(0, 180),
/* 116 */   NORMAL(1, 220),
/* 117 */   HIGH(2, 255),
/* 118 */   LOWEST(3, 135);
/*     */   
/*     */   private static final Brightness[] VALUES;
/*     */   
/*     */   public final int id;
/*     */   
/*     */   public final int modifier;
/*     */   
/*     */   static {
/* 121 */     VALUES = new Brightness[] { LOW, NORMAL, HIGH, LOWEST };
/*     */   }
/*     */   
/*     */   Brightness(int id, int modifier) {
/* 127 */     this.id = id;
/* 128 */     this.modifier = modifier;
/*     */   }
/*     */   
/*     */   public static Brightness byId(int id) {
/* 132 */     Preconditions.checkPositionIndex(id, VALUES.length, "brightness id");
/* 133 */     return byIdUnsafe(id);
/*     */   }
/*     */   
/*     */   private static Brightness byIdUnsafe(int id) {
/* 137 */     return VALUES[id];
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\material\MapColor$Brightness.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */