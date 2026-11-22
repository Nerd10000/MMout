/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.BrushableBlock;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BrushableBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class BrushItem extends Item {
/*     */   public static final int ANIMATION_DURATION = 10;
/*     */   
/*     */   private static final int USE_DURATION = 200;
/*     */   
/*     */   public BrushItem(Item.Properties properties) {
/*  37 */     super(properties);
/*     */   }
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/*  42 */     Player player = context.getPlayer();
/*  43 */     if (player != null && calculateHitResult(player).getType() == HitResult.Type.BLOCK)
/*  44 */       player.startUsingItem(context.getHand()); 
/*  47 */     return (InteractionResult)InteractionResult.CONSUME;
/*     */   }
/*     */   
/*     */   public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
/*  52 */     return ItemUseAnimation.BRUSH;
/*     */   }
/*     */   
/*     */   public int getUseDuration(ItemStack itemStack, LivingEntity user) {
/*  57 */     return 200;
/*     */   }
/*     */   
/*     */   public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
/*     */     Player player;
/*  62 */     if (ticksRemaining >= 0 && livingEntity instanceof Player) {
/*  62 */       player = (Player)livingEntity;
/*     */     } else {
/*  63 */       livingEntity.releaseUsingItem();
/*     */       return;
/*     */     } 
/*  67 */     HitResult hitResult = calculateHitResult(player);
/*  68 */     if (hitResult instanceof BlockHitResult) {
/*  68 */       BlockHitResult blockHitResult = (BlockHitResult)hitResult;
/*  68 */       if (hitResult.getType() == HitResult.Type.BLOCK) {
/*  73 */         int timeElapsed = getUseDuration(itemStack, livingEntity) - ticksRemaining + 1;
/*  74 */         boolean isLastTickBeforeBackswing = (timeElapsed % 10 == 5);
/*  76 */         if (isLastTickBeforeBackswing) {
/*     */           SoundEvent brushSound;
/*  77 */           BlockPos pos = blockHitResult.getBlockPos();
/*  78 */           BlockState state = level.getBlockState(pos);
/*  82 */           HumanoidArm brushingArm = (livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND) ? player.getMainArm() : player.getMainArm().getOpposite();
/*  84 */           if (state.shouldSpawnTerrainParticles() && state.getRenderShape() != RenderShape.INVISIBLE)
/*  85 */             spawnDustParticles(level, blockHitResult, state, livingEntity.getViewVector(0.0F), brushingArm); 
/*  89 */           Block block = state.getBlock();
/*  89 */           if (block instanceof BrushableBlock) {
/*  89 */             BrushableBlock brushableBlock = (BrushableBlock)block;
/*  90 */             brushSound = brushableBlock.getBrushSound();
/*     */           } else {
/*  92 */             brushSound = SoundEvents.BRUSH_GENERIC;
/*     */           } 
/*  95 */           level.playSound((Entity)player, pos, brushSound, SoundSource.BLOCKS);
/*  97 */           if (level instanceof ServerLevel) {
/*  97 */             ServerLevel serverLevel = (ServerLevel)level;
/*  98 */             BlockEntity blockEntity = level.getBlockEntity(pos);
/*  98 */             if (blockEntity instanceof BrushableBlockEntity) {
/*  98 */               BrushableBlockEntity brushableBlockEntity = (BrushableBlockEntity)blockEntity;
/*  99 */               boolean brushingUpdatedState = brushableBlockEntity.brush(level.getGameTime(), serverLevel, (LivingEntity)player, blockHitResult.getDirection(), itemStack);
/* 101 */               if (brushingUpdatedState) {
/* 104 */                 EquipmentSlot equippedHand = itemStack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
/* 105 */                 itemStack.hurtAndBreak(1, (LivingEntity)player, equippedHand);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     livingEntity.releaseUsingItem();
/*     */   }
/*     */   
/*     */   private HitResult calculateHitResult(Player player) {
/* 112 */     return ProjectileUtil.getHitResultOnViewVector((Entity)player, EntitySelector.CAN_BE_PICKED, player.blockInteractionRange());
/*     */   }
/*     */   
/*     */   private void spawnDustParticles(Level level, BlockHitResult hitResult, BlockState state, Vec3 viewVector, HumanoidArm brushingArm) {
/* 116 */     double deltaScale = 3.0D;
/* 117 */     int flip = (brushingArm == HumanoidArm.RIGHT) ? 1 : -1;
/* 118 */     int particles = level.getRandom().nextInt(7, 12);
/* 119 */     BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, state);
/* 121 */     Direction hitDirection = hitResult.getDirection();
/* 122 */     DustParticlesDelta dustParticlesDelta = DustParticlesDelta.fromDirection(viewVector, hitDirection);
/* 123 */     Vec3 hitLocation = hitResult.getLocation();
/* 125 */     for (int i = 0; i < particles; i++)
/* 126 */       level.addParticle((ParticleOptions)particle, hitLocation.x - (
/*     */           
/* 128 */           (hitDirection == Direction.WEST) ? 1.0E-6F : 0.0F), hitLocation.y, hitLocation.z - (
/*     */           
/* 130 */           (hitDirection == Direction.NORTH) ? 1.0E-6F : 0.0F), dustParticlesDelta
/* 131 */           .xd() * flip * 3.0D * level.getRandom().nextDouble(), 0.0D, dustParticlesDelta
/*     */           
/* 133 */           .zd() * flip * 3.0D * level.getRandom().nextDouble()); 
/*     */   }
/*     */   
/*     */   private static final class DustParticlesDelta extends Record {
/*     */     private final double xd;
/*     */     
/*     */     private final double yd;
/*     */     
/*     */     private final double zd;
/*     */     
/*     */     private static final double ALONG_SIDE_DELTA = 1.0D;
/*     */     
/*     */     private static final double OUT_FROM_SIDE_DELTA = 0.1D;
/*     */     
/*     */     private DustParticlesDelta(double xd, double yd, double zd) {
/* 138 */       this.xd = xd;
/* 138 */       this.yd = yd;
/* 138 */       this.zd = zd;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public double xd() {
/* 138 */       return this.xd;
/*     */     }
/*     */     
/*     */     public double yd() {
/* 138 */       return this.yd;
/*     */     }
/*     */     
/*     */     public double zd() {
/* 138 */       return this.zd;
/*     */     }
/*     */     
/*     */     public static DustParticlesDelta fromDirection(Vec3 viewVector, Direction hitDirection) {
/*     */       // Byte code:
/*     */       //   0: dconst_0
/*     */       //   1: dstore_2
/*     */       //   2: getstatic net/minecraft/world/item/BrushItem$1.$SwitchMap$net$minecraft$core$Direction : [I
/*     */       //   5: aload_1
/*     */       //   6: invokevirtual ordinal : ()I
/*     */       //   9: iaload
/*     */       //   10: tableswitch default -> 48, 1 -> 58, 2 -> 58, 3 -> 78, 4 -> 93, 5 -> 110, 6 -> 127
/*     */       //   48: new java/lang/MatchException
/*     */       //   51: dup
/*     */       //   52: aconst_null
/*     */       //   53: aconst_null
/*     */       //   54: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   57: athrow
/*     */       //   58: new net/minecraft/world/item/BrushItem$DustParticlesDelta
/*     */       //   61: dup
/*     */       //   62: aload_0
/*     */       //   63: invokevirtual z : ()D
/*     */       //   66: dconst_0
/*     */       //   67: aload_0
/*     */       //   68: invokevirtual x : ()D
/*     */       //   71: dneg
/*     */       //   72: invokespecial <init> : (DDD)V
/*     */       //   75: goto -> 139
/*     */       //   78: new net/minecraft/world/item/BrushItem$DustParticlesDelta
/*     */       //   81: dup
/*     */       //   82: dconst_1
/*     */       //   83: dconst_0
/*     */       //   84: ldc2_w -0.1
/*     */       //   87: invokespecial <init> : (DDD)V
/*     */       //   90: goto -> 139
/*     */       //   93: new net/minecraft/world/item/BrushItem$DustParticlesDelta
/*     */       //   96: dup
/*     */       //   97: ldc2_w -1.0
/*     */       //   100: dconst_0
/*     */       //   101: ldc2_w 0.1
/*     */       //   104: invokespecial <init> : (DDD)V
/*     */       //   107: goto -> 139
/*     */       //   110: new net/minecraft/world/item/BrushItem$DustParticlesDelta
/*     */       //   113: dup
/*     */       //   114: ldc2_w -0.1
/*     */       //   117: dconst_0
/*     */       //   118: ldc2_w -1.0
/*     */       //   121: invokespecial <init> : (DDD)V
/*     */       //   124: goto -> 139
/*     */       //   127: new net/minecraft/world/item/BrushItem$DustParticlesDelta
/*     */       //   130: dup
/*     */       //   131: ldc2_w 0.1
/*     */       //   134: dconst_0
/*     */       //   135: dconst_1
/*     */       //   136: invokespecial <init> : (DDD)V
/*     */       //   139: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #144	-> 0
/*     */       //   #145	-> 2
/*     */       //   #146	-> 58
/*     */       //   #147	-> 78
/*     */       //   #148	-> 93
/*     */       //   #149	-> 110
/*     */       //   #150	-> 127
/*     */       //   #145	-> 139
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	140	0	viewVector	Lnet/minecraft/world/phys/Vec3;
/*     */       //   0	140	1	hitDirection	Lnet/minecraft/core/Direction;
/*     */       //   2	138	2	yd	D
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\BrushItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */