/*     */ package net.minecraft.world.attribute.modifier;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum OperationId implements StringRepresentable {
/*  80 */   OVERRIDE("override"),
/*  81 */   ALPHA_BLEND("alpha_blend"),
/*  82 */   ADD("add"),
/*  83 */   SUBTRACT("subtract"),
/*  84 */   MULTIPLY("multiply"),
/*  85 */   BLEND_TO_GRAY("blend_to_gray"),
/*  86 */   MINIMUM("minimum"),
/*  87 */   MAXIMUM("maximum"),
/*  88 */   AND("and"),
/*  89 */   NAND("nand"),
/*  90 */   OR("or"),
/*  91 */   NOR("nor"),
/*  92 */   XOR("xor"),
/*  93 */   XNOR("xnor");
/*     */   
/*     */   public static final Codec<OperationId> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/*  96 */     CODEC = (Codec<OperationId>)StringRepresentable.fromEnum(OperationId::values);
/*     */   }
/*     */   
/*     */   OperationId(String name) {
/* 101 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 106 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\modifier\AttributeModifier$OperationId.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */