/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.EntityCollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public enum Block implements ClipContext.ShapeGetter {
/* 57 */   COLLIDER(BlockBehaviour.BlockStateBase::getCollisionShape),
/* 58 */   OUTLINE(BlockBehaviour.BlockStateBase::getShape),
/* 59 */   VISUAL(BlockBehaviour.BlockStateBase::getVisualShape),
/* 59 */   FALLDAMAGE_RESETTING(BlockBehaviour.BlockStateBase::getVisualShape);
/*    */   
/*    */   private final ClipContext.ShapeGetter shapeGetter;
/*    */   
/*    */   static {
/* 60 */     FALLDAMAGE_RESETTING = new Block("FALLDAMAGE_RESETTING", 3, (state, level, pos, collisionContext) -> {
/*    */           if (state.is(BlockTags.FALL_DAMAGE_RESETTING))
/*    */             return Shapes.block(); 
/*    */           if (collisionContext instanceof EntityCollisionContext) {
/*    */             EntityCollisionContext entityCollisionContext = (EntityCollisionContext)collisionContext;
/*    */             if (entityCollisionContext.getEntity() != null && entityCollisionContext.getEntity().is(EntityTypes.PLAYER)) {
/*    */               if (state.is(Blocks.END_GATEWAY) || state.is(Blocks.END_PORTAL))
/*    */                 return Shapes.block(); 
/*    */               if (level instanceof ServerLevel) {
/*    */                 ServerLevel serverLevel = (ServerLevel)level;
/*    */                 if (state.is(Blocks.NETHER_PORTAL) && ((Integer)serverLevel.getGameRules().get(GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY)).intValue() == 0)
/*    */                   return Shapes.block(); 
/*    */               } 
/*    */             } 
/*    */           } 
/*    */           return Shapes.empty();
/*    */         });
/*    */   }
/*    */   
/*    */   Block(ClipContext.ShapeGetter getShape) {
/* 88 */     this.shapeGetter = getShape;
/*    */   }
/*    */   
/*    */   public VoxelShape get(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 93 */     return this.shapeGetter.get(state, level, pos, context);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\ClipContext$Block.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */