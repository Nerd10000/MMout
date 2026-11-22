/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.Relative;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EndGatewayBlock extends BaseEntityBlock implements Portal {
/*  30 */   public static final MapCodec<EndGatewayBlock> CODEC = simpleCodec(EndGatewayBlock::new);
/*     */   
/*     */   public MapCodec<EndGatewayBlock> codec() {
/*  34 */     return CODEC;
/*     */   }
/*     */   
/*     */   protected EndGatewayBlock(BlockBehaviour.Properties properties) {
/*  38 */     super(properties);
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  43 */     return (BlockEntity)new TheEndGatewayBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/*  48 */     return createTickerHelper(type, BlockEntityTypes.END_GATEWAY, level.isClientSide() ? TheEndGatewayBlockEntity::beamAnimationTick : TheEndGatewayBlockEntity::portalTick);
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*     */     TheEndGatewayBlockEntity theEndGatewayBlockEntity;
/*  53 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  54 */     if (blockEntity instanceof TheEndGatewayBlockEntity) {
/*  54 */       theEndGatewayBlockEntity = (TheEndGatewayBlockEntity)blockEntity;
/*     */     } else {
/*     */       return;
/*     */     } 
/*  57 */     int particleCount = theEndGatewayBlockEntity.getParticleAmount();
/*  58 */     for (int i = 0; i < particleCount; i++) {
/*  59 */       double x = pos.getX() + random.nextDouble();
/*  60 */       double y = pos.getY() + random.nextDouble();
/*  61 */       double z = pos.getZ() + random.nextDouble();
/*  62 */       double xa = (random.nextDouble() - 0.5D) * 0.5D;
/*  63 */       double ya = (random.nextDouble() - 0.5D) * 0.5D;
/*  64 */       double za = (random.nextDouble() - 0.5D) * 0.5D;
/*  66 */       int flip = random.nextInt(2) * 2 - 1;
/*  67 */       if (random.nextBoolean()) {
/*  68 */         z = pos.getZ() + 0.5D + 0.25D * flip;
/*  69 */         za = (random.nextFloat() * 2.0F * flip);
/*     */       } else {
/*  71 */         x = pos.getX() + 0.5D + 0.25D * flip;
/*  72 */         xa = (random.nextFloat() * 2.0F * flip);
/*     */       } 
/*  75 */       level.addParticle((ParticleOptions)ParticleTypes.PORTAL, x, y, z, xa, ya, za);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/*  81 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   protected boolean canBeReplaced(BlockState state, Fluid fluid) {
/*  86 */     return false;
/*     */   }
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  91 */     if (entity.canUsePortal(false)) {
/*  92 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/*  93 */       if (!level.isClientSide() && blockEntity instanceof TheEndGatewayBlockEntity) {
/*  93 */         TheEndGatewayBlockEntity endGatewayBlockEntity = (TheEndGatewayBlockEntity)blockEntity;
/*  94 */         if (!endGatewayBlockEntity.isCoolingDown()) {
/*  95 */           entity.setAsInsidePortal(this, pos);
/*  96 */           TheEndGatewayBlockEntity.triggerCooldown(level, pos, state, endGatewayBlockEntity);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
/*     */     TheEndGatewayBlockEntity endGatewayBlockEntity;
/* 104 */     BlockEntity blockEntity = currentLevel.getBlockEntity(portalEntryPos);
/* 105 */     if (blockEntity instanceof TheEndGatewayBlockEntity) {
/* 105 */       endGatewayBlockEntity = (TheEndGatewayBlockEntity)blockEntity;
/*     */     } else {
/* 106 */       return null;
/*     */     } 
/* 109 */     Vec3 teleportPosition = endGatewayBlockEntity.getPortalPosition(currentLevel, portalEntryPos);
/* 111 */     if (teleportPosition == null)
/* 112 */       return null; 
/* 115 */     if (entity instanceof net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl)
/* 118 */       return new TeleportTransition(currentLevel, teleportPosition, Vec3.ZERO, 0.0F, 0.0F, 
/*     */           
/* 124 */           Set.of(), TeleportTransition.PLACE_PORTAL_TICKET); 
/* 128 */     return new TeleportTransition(currentLevel, teleportPosition, Vec3.ZERO, 0.0F, 0.0F, 
/*     */         
/* 134 */         Relative.union(new Set[] { Relative.DELTA, Relative.ROTATION }, ), TeleportTransition.PLACE_PORTAL_TICKET);
/*     */   }
/*     */   
/*     */   protected RenderShape getRenderShape(BlockState state) {
/* 142 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\EndGatewayBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */