/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Type implements StringRepresentable {
/* 42 */   REPLACE_ALL("replace_all", (MapCodec)ListOperation.ReplaceAll.MAP_CODEC),
/* 43 */   REPLACE_SECTION("replace_section", (MapCodec)ListOperation.ReplaceSection.MAP_CODEC),
/* 44 */   INSERT("insert", (MapCodec)ListOperation.Insert.MAP_CODEC),
/* 45 */   APPEND("append", (MapCodec)ListOperation.Append.MAP_CODEC);
/*    */   
/*    */   public static final Codec<Type> CODEC;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   private final MapCodec<? extends ListOperation> mapCodec;
/*    */   
/*    */   static {
/* 47 */     CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*    */   }
/*    */   
/*    */   Type(String id, MapCodec<? extends ListOperation> mapCodec) {
/* 53 */     this.id = id;
/* 54 */     this.mapCodec = mapCodec;
/*    */   }
/*    */   
/*    */   public MapCodec<? extends ListOperation> mapCodec() {
/* 58 */     return this.mapCodec;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 63 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ListOperation$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */