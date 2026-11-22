/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public interface BookInfo<CategoryType> {
/*    */   static <CategoryType, SelfType extends BookInfo<CategoryType>> MapCodec<SelfType> mapCodec(Codec<CategoryType> categoryCodec, CategoryType defaultCategory, Constructor<CategoryType, SelfType> constructor) {
/* 74 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)categoryCodec.optionalFieldOf("category", defaultCategory).forGetter(BookInfo::category), (App)Codec.STRING.optionalFieldOf("group", "").forGetter(BookInfo::group)).apply((Applicative)i, constructor));
/*    */   }
/*    */   
/*    */   static <CategoryType, SelfType extends BookInfo<CategoryType>> StreamCodec<RegistryFriendlyByteBuf, SelfType> streamCodec(StreamCodec<? super RegistryFriendlyByteBuf, CategoryType> categoryCodec, Constructor<CategoryType, SelfType> constructor) {
/* 81 */     return StreamCodec.composite(categoryCodec, BookInfo::category, ByteBufCodecs.STRING_UTF8, BookInfo::group, constructor);
/*    */   }
/*    */   
/*    */   CategoryType category();
/*    */   
/*    */   String group();
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Constructor<CategoryType, SelfType extends BookInfo<CategoryType>> extends BiFunction<CategoryType, String, SelfType> {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\Recipe$BookInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */