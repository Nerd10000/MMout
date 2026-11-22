/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.EntityCollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ClipContext {
/*     */   private final Vec3 from;
/*     */   
/*     */   private final Vec3 to;
/*     */   
/*     */   private final Block block;
/*     */   
/*     */   private final Fluid fluid;
/*     */   
/*     */   private final CollisionContext collisionContext;
/*     */   
/*     */   public ClipContext(Vec3 from, Vec3 to, Block block, Fluid fluid, Entity entity) {
/*  29 */     this(from, to, block, fluid, CollisionContext.of(entity));
/*     */   }
/*     */   
/*     */   public ClipContext(Vec3 from, Vec3 to, Block block, Fluid fluid, CollisionContext collisionContext) {
/*  33 */     this.from = from;
/*  34 */     this.to = to;
/*  35 */     this.block = block;
/*  36 */     this.fluid = fluid;
/*  37 */     this.collisionContext = collisionContext;
/*     */   }
/*     */   
/*     */   public Vec3 getTo() {
/*  41 */     return this.to;
/*     */   }
/*     */   
/*     */   public Vec3 getFrom() {
/*  45 */     return this.from;
/*     */   }
/*     */   
/*     */   public VoxelShape getBlockShape(BlockState blockState, BlockGetter level, BlockPos pos) {
/*  49 */     return this.block.get(blockState, level, pos, this.collisionContext);
/*     */   }
/*     */   
/*     */   public VoxelShape getFluidShape(FluidState fluidState, BlockGetter level, BlockPos pos) {
/*  53 */     return this.fluid.canPick(fluidState) ? fluidState.getShape(level, pos) : Shapes.empty();
/*     */   }
/*     */   
/*     */   public enum Block implements ShapeGetter {
/*  57 */     COLLIDER((String)BlockBehaviour.BlockStateBase::getCollisionShape),
/*  58 */     OUTLINE((String)BlockBehaviour.BlockStateBase::getShape),
/*  59 */     VISUAL((String)BlockBehaviour.BlockStateBase::getVisualShape),
/*  59 */     FALLDAMAGE_RESETTING((String)BlockBehaviour.BlockStateBase::getVisualShape);
/*     */     
/*     */     private final ClipContext.ShapeGetter shapeGetter;
/*     */     
/*     */     static {
/*  60 */       FALLDAMAGE_RESETTING = new Block("FALLDAMAGE_RESETTING", 3, (state, level, pos, collisionContext) -> {
/*     */             if (state.is(BlockTags.FALL_DAMAGE_RESETTING))
/*     */               return Shapes.block(); 
/*     */             if (collisionContext instanceof EntityCollisionContext) {
/*     */               EntityCollisionContext entityCollisionContext = (EntityCollisionContext)collisionContext;
/*     */               if (entityCollisionContext.getEntity() != null && entityCollisionContext.getEntity().is(EntityTypes.PLAYER)) {
/*     */                 if (state.is(Blocks.END_GATEWAY) || state.is(Blocks.END_PORTAL))
/*     */                   return Shapes.block(); 
/*     */                 if (level instanceof ServerLevel) {
/*     */                   ServerLevel serverLevel = (ServerLevel)level;
/*     */                   if (state.is(Blocks.NETHER_PORTAL) && ((Integer)serverLevel.getGameRules().get(GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY)).intValue() == 0)
/*     */                     return Shapes.block(); 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */             return Shapes.empty();
/*     */           });
/*     */     }
/*     */     
/*     */     Block(ClipContext.ShapeGetter getShape) {
/*  88 */       this.shapeGetter = getShape;
/*     */     }
/*     */     
/*     */     public VoxelShape get(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  93 */       return this.shapeGetter.get(state, level, pos, context);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Fluid {
/* 102 */     NONE((String)(state -> false)),
/* 103 */     SOURCE_ONLY((String)FluidState::isSource),
/* 103 */     ANY((String)FluidState::isSource),
/* 103 */     WATER((String)FluidState::isSource);
/*     */     
/*     */     private final Predicate<FluidState> canPick;
/*     */     
/*     */     static {
/* 104 */       ANY = new Fluid("ANY", 2, state -> !state.isEmpty());
/* 105 */       WATER = new Fluid("WATER", 3, fluidState -> fluidState.is(FluidTags.WATER));
/*     */     }
/*     */     
/*     */     Fluid(Predicate<FluidState> canPick) {
/* 111 */       this.canPick = canPick;
/*     */     }
/*     */     
/*     */     public boolean canPick(FluidState fluidState) {
/* 115 */       return this.canPick.test(fluidState);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface ShapeGetter {
/*     */     VoxelShape get(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos, CollisionContext param1CollisionContext);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\ClipContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */