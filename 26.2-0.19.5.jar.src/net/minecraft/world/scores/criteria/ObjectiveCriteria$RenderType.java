/*     */ package net.minecraft.world.scores.criteria;
/*     */ 
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum RenderType implements StringRepresentable {
/* 113 */   INTEGER("integer"),
/* 114 */   HEARTS("hearts");
/*     */   
/*     */   private final String id;
/*     */   
/*     */   public static final StringRepresentable.EnumCodec<RenderType> CODEC;
/*     */   
/*     */   RenderType(String id) {
/* 120 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getId() {
/* 124 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 129 */     return this.id;
/*     */   }
/*     */   
/*     */   static {
/* 132 */     CODEC = StringRepresentable.fromEnum(RenderType::values);
/*     */   }
/*     */   
/*     */   public static RenderType byId(String key) {
/* 135 */     return (RenderType)CODEC.byName(key, INTEGER);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\criteria\ObjectiveCriteria$RenderType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */