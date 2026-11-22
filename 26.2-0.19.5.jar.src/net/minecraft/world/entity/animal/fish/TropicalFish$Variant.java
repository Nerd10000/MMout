/*    */ package net.minecraft.world.entity.animal.fish;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public final class Variant extends Record {
/*    */   private final TropicalFish.Pattern pattern;
/*    */   
/*    */   private final DyeColor baseColor;
/*    */   
/*    */   private final DyeColor patternColor;
/*    */   
/*    */   public Variant(TropicalFish.Pattern pattern, DyeColor baseColor, DyeColor patternColor) {
/* 70 */     this.pattern = pattern;
/* 70 */     this.baseColor = baseColor;
/* 70 */     this.patternColor = patternColor;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #70	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/fish/TropicalFish$Variant;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public TropicalFish.Pattern pattern() {
/* 70 */     return this.pattern;
/*    */   }
/*    */   
/*    */   public DyeColor baseColor() {
/* 70 */     return this.baseColor;
/*    */   }
/*    */   
/*    */   public DyeColor patternColor() {
/* 70 */     return this.patternColor;
/*    */   }
/*    */   
/* 71 */   public static final Codec<Variant> CODEC = Codec.INT.xmap(Variant::new, Variant::getPackedId);
/*    */   
/*    */   public Variant(int packedId) {
/* 74 */     this(TropicalFish.getPattern(packedId), TropicalFish.getBaseColor(packedId), TropicalFish.getPatternColor(packedId));
/*    */   }
/*    */   
/*    */   public int getPackedId() {
/* 78 */     return TropicalFish.packVariant(this.pattern, this.baseColor, this.patternColor);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fish\TropicalFish$Variant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */