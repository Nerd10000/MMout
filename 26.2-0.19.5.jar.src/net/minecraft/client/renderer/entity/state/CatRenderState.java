/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class CatRenderState extends FelineRenderState {
/*  8 */   private static final Identifier DEFAULT_TEXTURE = Identifier.withDefaultNamespace("textures/entity/cat/cat_tabby.png");
/*    */   
/* 10 */   public Identifier texture = DEFAULT_TEXTURE;
/*    */   
/*    */   public boolean isLyingOnTopOfSleepingPlayer;
/*    */   
/*    */   public DyeColor collarColor;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\CatRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */