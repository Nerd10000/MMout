/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Type implements StringRepresentable {
/* 53 */   WARM("warm"),
/* 54 */   COLD("cold");
/*    */   
/*    */   public static final Codec<Type> CODEC;
/*    */   
/*    */   @Deprecated
/*    */   public static final Codec<Type> LEGACY_CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 57 */     CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/* 60 */     LEGACY_CODEC = ExtraCodecs.legacyEnum(Type::valueOf);
/*    */   }
/*    */   
/*    */   Type(String name) {
/* 65 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 69 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 74 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanRuinStructure$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */