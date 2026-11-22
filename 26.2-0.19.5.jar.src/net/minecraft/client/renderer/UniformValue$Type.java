/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Type implements StringRepresentable {
/* 25 */   INT("int", (Codec)UniformValue.IntUniform.CODEC),
/* 26 */   IVEC3("ivec3", (Codec)UniformValue.IVec3Uniform.CODEC),
/* 27 */   FLOAT("float", (Codec)UniformValue.FloatUniform.CODEC),
/* 28 */   VEC2("vec2", (Codec)UniformValue.Vec2Uniform.CODEC),
/* 29 */   VEC3("vec3", (Codec)UniformValue.Vec3Uniform.CODEC),
/* 30 */   VEC4("vec4", (Codec)UniformValue.Vec4Uniform.CODEC),
/* 31 */   MATRIX4X4("matrix4x4", (Codec)UniformValue.Matrix4x4Uniform.CODEC);
/*    */   
/*    */   public static final Codec<Type> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final MapCodec<? extends UniformValue> valueCodec;
/*    */   
/*    */   static {
/* 34 */     CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*    */   }
/*    */   
/*    */   Type(String name, Codec<? extends UniformValue> valueCodec) {
/* 40 */     this.name = name;
/* 41 */     this.valueCodec = valueCodec.fieldOf("value");
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 46 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\UniformValue$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */