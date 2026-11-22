/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public interface Recipe<T extends RecipeInput> {
/* 21 */   public static final Codec<Recipe<?>> CODEC = BuiltInRegistries.RECIPE_SERIALIZER.byNameCodec().dispatch(Recipe::getSerializer, RecipeSerializer::codec);
/*    */   
/* 22 */   public static final Codec<ResourceKey<Recipe<?>>> KEY_CODEC = ResourceKey.codec(Registries.RECIPE);
/*    */   
/* 24 */   public static final StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.RECIPE_SERIALIZER).dispatch(Recipe::getSerializer, RecipeSerializer::streamCodec);
/*    */   
/*    */   boolean matches(T paramT, Level paramLevel);
/*    */   
/*    */   ItemStack assemble(T paramT);
/*    */   
/*    */   default boolean isSpecial() {
/* 35 */     return false;
/*    */   }
/*    */   
/*    */   boolean showNotification();
/*    */   
/*    */   String group();
/*    */   
/*    */   RecipeSerializer<? extends Recipe<T>> getSerializer();
/*    */   
/*    */   RecipeType<? extends Recipe<T>> getType();
/*    */   
/*    */   PlacementInfo placementInfo();
/*    */   
/*    */   default List<RecipeDisplay> display() {
/* 50 */     return List.of();
/*    */   }
/*    */   
/*    */   RecipeBookCategory recipeBookCategory();
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Constructor<CategoryType, SelfType extends BookInfo<CategoryType>> extends BiFunction<CategoryType, String, SelfType> {}
/*    */   
/*    */   public static final class CommonInfo extends Record {
/*    */     private final boolean showNotification;
/*    */     
/*    */     public static final MapCodec<CommonInfo> MAP_CODEC;
/*    */     
/*    */     public CommonInfo(boolean showNotification) {
/* 55 */       this.showNotification = showNotification;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/Recipe$CommonInfo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #55	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/Recipe$CommonInfo;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/Recipe$CommonInfo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #55	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/Recipe$CommonInfo;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/Recipe$CommonInfo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #55	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/Recipe$CommonInfo;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public boolean showNotification() {
/* 55 */       return this.showNotification;
/*    */     }
/*    */     
/*    */     static {
/* 58 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.optionalFieldOf("show_notification", Boolean.valueOf(true)).forGetter(CommonInfo::showNotification)).apply((Applicative)i, CommonInfo::new));
/*    */     }
/*    */     
/* 62 */     public static final StreamCodec<RegistryFriendlyByteBuf, CommonInfo> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, CommonInfo::showNotification, CommonInfo::new);
/*    */   }
/*    */   
/*    */   public static interface BookInfo<CategoryType> {
/*    */     static <CategoryType, SelfType extends BookInfo<CategoryType>> MapCodec<SelfType> mapCodec(Codec<CategoryType> categoryCodec, CategoryType defaultCategory, Constructor<CategoryType, SelfType> constructor) {
/* 74 */       return RecordCodecBuilder.mapCodec(i -> i.group((App)categoryCodec.optionalFieldOf("category", defaultCategory).forGetter(BookInfo::category), (App)Codec.STRING.optionalFieldOf("group", "").forGetter(BookInfo::group)).apply((Applicative)i, constructor));
/*    */     }
/*    */     
/*    */     static <CategoryType, SelfType extends BookInfo<CategoryType>> StreamCodec<RegistryFriendlyByteBuf, SelfType> streamCodec(StreamCodec<? super RegistryFriendlyByteBuf, CategoryType> categoryCodec, Constructor<CategoryType, SelfType> constructor) {
/* 81 */       return StreamCodec.composite(categoryCodec, BookInfo::category, ByteBufCodecs.STRING_UTF8, BookInfo::group, constructor);
/*    */     }
/*    */     
/*    */     CategoryType category();
/*    */     
/*    */     String group();
/*    */     
/*    */     @FunctionalInterface
/*    */     public static interface Constructor<CategoryType, SelfType extends BookInfo<CategoryType>> extends BiFunction<CategoryType, String, SelfType> {}
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\Recipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */