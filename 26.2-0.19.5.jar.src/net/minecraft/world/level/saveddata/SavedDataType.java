/*    */ package net.minecraft.world.level.saveddata;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ 
/*    */ public final class SavedDataType<T extends SavedData> extends Record {
/*    */   private final Identifier id;
/*    */   
/*    */   private final Supplier<T> constructor;
/*    */   
/*    */   private final Codec<T> codec;
/*    */   
/*    */   private final DataFixTypes dataFixType;
/*    */   
/*    */   public SavedDataType(Identifier id, Supplier<T> constructor, Codec<T> codec, DataFixTypes dataFixType) {
/*  9 */     this.id = id;
/*  9 */     this.constructor = constructor;
/*  9 */     this.codec = codec;
/*  9 */     this.dataFixType = dataFixType;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/*  9 */     return this.id;
/*    */   }
/*    */   
/*    */   public Supplier<T> constructor() {
/*  9 */     return this.constructor;
/*    */   }
/*    */   
/*    */   public Codec<T> codec() {
/*  9 */     return this.codec;
/*    */   }
/*    */   
/*    */   public DataFixTypes dataFixType() {
/*  9 */     return this.dataFixType;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 17 */     if (obj instanceof SavedDataType) {
/* 17 */       SavedDataType<?> type = (SavedDataType)obj;
/* 17 */       if (this.id.equals(type.id));
/*    */     } 
/* 17 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 22 */     return this.id.hashCode();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 27 */     return "SavedDataType[" + String.valueOf(this.id) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\saveddata\SavedDataType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */