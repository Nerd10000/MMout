/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class RecipeHolder<T extends Recipe<?>> extends Record {
/*    */   private final ResourceKey<Recipe<?>> id;
/*    */   
/*    */   private final T value;
/*    */   
/*    */   public RecipeHolder(ResourceKey<Recipe<?>> id, T value) {
/*  9 */     this.id = id;
/*  9 */     this.value = value;
/*    */   }
/*    */   
/*    */   public ResourceKey<Recipe<?>> id() {
/*  9 */     return this.id;
/*    */   }
/*    */   
/*    */   public T value() {
/*  9 */     return this.value;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<RegistryFriendlyByteBuf, RecipeHolder<?>> STREAM_CODEC = StreamCodec.composite(
/* 11 */       ResourceKey.streamCodec(Registries.RECIPE), RecipeHolder::id, Recipe.STREAM_CODEC, RecipeHolder::value, RecipeHolder::new);
/*    */   
/*    */   public boolean equals(Object obj) {
/* 18 */     if (this == obj)
/* 19 */       return true; 
/* 21 */     if (obj instanceof RecipeHolder) {
/* 21 */       RecipeHolder<?> holder = (RecipeHolder)obj;
/* 21 */       if (this.id == holder.id);
/*    */     } 
/* 21 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 26 */     return this.id.hashCode();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 31 */     return this.id.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\RecipeHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */