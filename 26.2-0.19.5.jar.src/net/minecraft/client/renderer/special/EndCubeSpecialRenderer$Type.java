/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Type implements StringRepresentable {
/* 53 */   PORTAL("portal"),
/* 54 */   GATEWAY("gateway");
/*    */   
/*    */   public static final Codec<Type> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 57 */     CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*    */   }
/*    */   
/*    */   Type(String name) {
/* 62 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 67 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\EndCubeSpecialRenderer$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */