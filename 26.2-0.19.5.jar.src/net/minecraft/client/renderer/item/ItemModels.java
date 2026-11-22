/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class ItemModels {
/*  9 */   private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends ItemModel.Unbaked>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*    */   
/*    */   public static final Codec<ItemModel.Unbaked> CODEC;
/*    */   
/*    */   static {
/* 11 */     CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatch(ItemModel.Unbaked::type, c -> c);
/*    */   }
/*    */   
/*    */   public static void bootstrap() {
/* 14 */     ID_MAPPER.put(Identifier.withDefaultNamespace("empty"), EmptyModel.Unbaked.MAP_CODEC);
/* 15 */     ID_MAPPER.put(Identifier.withDefaultNamespace("model"), CuboidItemModelWrapper.Unbaked.MAP_CODEC);
/* 16 */     ID_MAPPER.put(Identifier.withDefaultNamespace("range_dispatch"), RangeSelectItemModel.Unbaked.MAP_CODEC);
/* 17 */     ID_MAPPER.put(Identifier.withDefaultNamespace("special"), SpecialModelWrapper.Unbaked.MAP_CODEC);
/* 18 */     ID_MAPPER.put(Identifier.withDefaultNamespace("composite"), CompositeModel.Unbaked.MAP_CODEC);
/* 19 */     ID_MAPPER.put(Identifier.withDefaultNamespace("bundle/selected_item"), BundleSelectedItemSpecialRenderer.Unbaked.MAP_CODEC);
/* 20 */     ID_MAPPER.put(Identifier.withDefaultNamespace("select"), SelectItemModel.Unbaked.MAP_CODEC);
/* 21 */     ID_MAPPER.put(Identifier.withDefaultNamespace("condition"), ConditionalItemModel.Unbaked.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\ItemModels.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */