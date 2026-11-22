/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.animal.rabbit.AdultRabbitModel;
/*    */ import net.minecraft.client.model.animal.rabbit.BabyRabbitModel;
/*    */ import net.minecraft.client.model.animal.rabbit.RabbitModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.RabbitRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.rabbit.Rabbit;
/*    */ 
/*    */ public class RabbitRenderer extends AgeableMobRenderer<Rabbit, RabbitRenderState, RabbitModel> {
/* 15 */   private static final Identifier TOAST = Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_toast.png");
/*    */   
/* 16 */   private static final Identifier TOAST_BABY = Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_toast_baby.png");
/*    */   
/* 18 */   private static final Map<Rabbit.Variant, Identifier> RABBIT_LOCATIONS = Maps.newEnumMap(Map.of(Rabbit.Variant.BROWN, 
/* 19 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_brown.png"), Rabbit.Variant.WHITE_SPLOTCHED, 
/* 20 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_white_splotched.png"), Rabbit.Variant.EVIL, 
/* 21 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_caerbannog.png"), Rabbit.Variant.WHITE, 
/* 22 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_white.png"), Rabbit.Variant.GOLD, 
/* 23 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_gold.png"), Rabbit.Variant.BLACK, 
/* 24 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_black.png"), Rabbit.Variant.SALT, 
/* 25 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_salt.png")));
/*    */   
/* 28 */   private static final Map<Rabbit.Variant, Identifier> BABY_RABBIT_LOCATIONS = Maps.newEnumMap(Map.of(Rabbit.Variant.BROWN, 
/* 29 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_brown_baby.png"), Rabbit.Variant.WHITE_SPLOTCHED, 
/* 30 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_white_splotched_baby.png"), Rabbit.Variant.EVIL, 
/* 31 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_caerbannog_baby.png"), Rabbit.Variant.WHITE, 
/* 32 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_white_baby.png"), Rabbit.Variant.GOLD, 
/* 33 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_gold_baby.png"), Rabbit.Variant.BLACK, 
/* 34 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_black_baby.png"), Rabbit.Variant.SALT, 
/* 35 */         Identifier.withDefaultNamespace("textures/entity/rabbit/rabbit_salt_baby.png")));
/*    */   
/*    */   public RabbitRenderer(EntityRendererProvider.Context context) {
/* 39 */     super(context, new AdultRabbitModel(context.bakeLayer(ModelLayers.RABBIT)), new BabyRabbitModel(context.bakeLayer(ModelLayers.RABBIT_BABY)), 0.3F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(RabbitRenderState state) {
/* 44 */     if (state.isToast)
/* 45 */       return state.isBaby ? TOAST_BABY : TOAST; 
/* 48 */     Map<Rabbit.Variant, Identifier> locations = state.isBaby ? BABY_RABBIT_LOCATIONS : RABBIT_LOCATIONS;
/* 49 */     return locations.get(state.variant);
/*    */   }
/*    */   
/*    */   public RabbitRenderState createRenderState() {
/* 54 */     return new RabbitRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Rabbit entity, RabbitRenderState state, float partialTicks) {
/* 59 */     super.extractRenderState(entity, state, partialTicks);
/* 60 */     state.jumpCompletion = entity.getJumpCompletion(partialTicks);
/* 61 */     state.isToast = checkMagicName((Entity)entity, "Toast");
/* 62 */     state.variant = entity.getVariant();
/* 63 */     state.hopAnimationState.copyFrom(entity.hopAnimationState);
/* 64 */     state.idleHeadTiltAnimationState.copyFrom(entity.idleHeadTiltAnimationState);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\RabbitRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */