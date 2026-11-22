/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class SpecialModelRenderers {
/*  9 */   private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends SpecialModelRenderer.Unbaked<?>>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*    */   
/*    */   public static final Codec<SpecialModelRenderer.Unbaked<?>> CODEC;
/*    */   
/*    */   static {
/* 11 */     CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatch(SpecialModelRenderer.Unbaked::type, c -> c);
/*    */   }
/*    */   
/*    */   public static void bootstrap() {
/* 14 */     ID_MAPPER.put(Identifier.withDefaultNamespace("bell"), BellSpecialRenderer.Unbaked.MAP_CODEC);
/* 15 */     ID_MAPPER.put(Identifier.withDefaultNamespace("banner"), BannerSpecialRenderer.Unbaked.MAP_CODEC);
/* 16 */     ID_MAPPER.put(Identifier.withDefaultNamespace("book"), BookSpecialRenderer.Unbaked.MAP_CODEC);
/* 17 */     ID_MAPPER.put(Identifier.withDefaultNamespace("conduit"), ConduitSpecialRenderer.Unbaked.MAP_CODEC);
/* 18 */     ID_MAPPER.put(Identifier.withDefaultNamespace("chest"), ChestSpecialRenderer.Unbaked.MAP_CODEC);
/* 19 */     ID_MAPPER.put(Identifier.withDefaultNamespace("copper_golem_statue"), CopperGolemStatueSpecialRenderer.Unbaked.MAP_CODEC);
/* 20 */     ID_MAPPER.put(Identifier.withDefaultNamespace("head"), SkullSpecialRenderer.Unbaked.MAP_CODEC);
/* 21 */     ID_MAPPER.put(Identifier.withDefaultNamespace("player_head"), PlayerHeadSpecialRenderer.Unbaked.MAP_CODEC);
/* 22 */     ID_MAPPER.put(Identifier.withDefaultNamespace("shulker_box"), ShulkerBoxSpecialRenderer.Unbaked.MAP_CODEC);
/* 23 */     ID_MAPPER.put(Identifier.withDefaultNamespace("shield"), ShieldSpecialRenderer.Unbaked.MAP_CODEC);
/* 24 */     ID_MAPPER.put(Identifier.withDefaultNamespace("trident"), TridentSpecialRenderer.Unbaked.MAP_CODEC);
/* 25 */     ID_MAPPER.put(Identifier.withDefaultNamespace("decorated_pot"), DecoratedPotSpecialRenderer.Unbaked.MAP_CODEC);
/* 26 */     ID_MAPPER.put(Identifier.withDefaultNamespace("end_cube"), EndCubeSpecialRenderer.Unbaked.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\SpecialModelRenderers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */