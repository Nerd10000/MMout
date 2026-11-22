/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Type implements StringRepresentable {
/* 38 */   DEFAULT("default", CardinalLighting.DEFAULT),
/* 39 */   NETHER("nether", CardinalLighting.NETHER);
/*    */   
/*    */   public static final Codec<Type> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final CardinalLighting lighting;
/*    */   
/*    */   static {
/* 42 */     CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*    */   }
/*    */   
/*    */   Type(String name, CardinalLighting lighting) {
/* 48 */     this.name = name;
/* 49 */     this.lighting = lighting;
/*    */   }
/*    */   
/*    */   public CardinalLighting get() {
/* 53 */     return this.lighting;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 58 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\CardinalLighting$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */