/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.animal.llama.BabyLlamaModel;
/*    */ import net.minecraft.client.model.animal.llama.LlamaModel;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LlamaRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.equine.Llama;
/*    */ 
/*    */ public class LlamaRenderer extends AgeableMobRenderer<Llama, LlamaRenderState, LlamaModel> {
/* 16 */   private static final Map<Llama.Variant, Identifier> TEXTURES = Maps.newEnumMap(Map.of(Llama.Variant.CREAMY, 
/* 17 */         Identifier.withDefaultNamespace("textures/entity/llama/llama_creamy.png"), Llama.Variant.WHITE, 
/* 18 */         Identifier.withDefaultNamespace("textures/entity/llama/llama_white.png"), Llama.Variant.BROWN, 
/* 19 */         Identifier.withDefaultNamespace("textures/entity/llama/llama_brown.png"), Llama.Variant.GRAY, 
/* 20 */         Identifier.withDefaultNamespace("textures/entity/llama/llama_gray.png")));
/*    */   
/* 23 */   private static final Map<Llama.Variant, Identifier> BABY_TEXTURES = Maps.newEnumMap(Map.of(Llama.Variant.CREAMY, 
/* 24 */         Identifier.withDefaultNamespace("textures/entity/llama/llama_creamy_baby.png"), Llama.Variant.WHITE, 
/* 25 */         Identifier.withDefaultNamespace("textures/entity/llama/llama_white_baby.png"), Llama.Variant.BROWN, 
/* 26 */         Identifier.withDefaultNamespace("textures/entity/llama/llama_brown_baby.png"), Llama.Variant.GRAY, 
/* 27 */         Identifier.withDefaultNamespace("textures/entity/llama/llama_gray_baby.png")));
/*    */   
/*    */   public LlamaRenderer(EntityRendererProvider.Context context, ModelLayerLocation model, ModelLayerLocation babyModel) {
/* 31 */     super(context, new LlamaModel(context.bakeLayer(model)), new BabyLlamaModel(context.bakeLayer(babyModel)), 0.7F);
/* 33 */     addLayer((RenderLayer<LlamaRenderState, LlamaModel>)new LlamaDecorLayer(this, context.getModelSet(), context.getEquipmentRenderer()));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(LlamaRenderState state) {
/* 38 */     Map<Llama.Variant, Identifier> textures = state.isBaby ? BABY_TEXTURES : TEXTURES;
/* 39 */     return textures.get(state.variant);
/*    */   }
/*    */   
/*    */   public LlamaRenderState createRenderState() {
/* 44 */     return new LlamaRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Llama entity, LlamaRenderState state, float partialTicks) {
/* 49 */     super.extractRenderState(entity, state, partialTicks);
/* 50 */     state.variant = entity.getVariant();
/* 51 */     state.hasChest = (!entity.isBaby() && entity.hasChest());
/* 52 */     state.bodyItem = entity.getBodyArmorItem();
/* 53 */     state.isTraderLlama = entity.isTraderLlama();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\LlamaRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */