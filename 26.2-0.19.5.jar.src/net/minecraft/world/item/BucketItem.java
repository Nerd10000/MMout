/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.BucketPickup;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ 
/*     */ public class BucketItem extends Item implements DispensibleContainerItem {
/*     */   protected final Fluid content;
/*     */   
/*     */   public BucketItem(Fluid content, Item.Properties properties) {
/*  38 */     super(properties);
/*  39 */     this.content = content;
/*     */   }
/*     */   
/*     */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/*  44 */     ItemStack itemStack = player.getItemInHand(hand);
/*  45 */     BlockHitResult hitResult = getPlayerPOVHitResult(level, player, getFluidContext());
/*  46 */     if (hitResult.getType() == HitResult.Type.MISS)
/*  47 */       return (InteractionResult)InteractionResult.PASS; 
/*  50 */     if (hitResult.getType() == HitResult.Type.BLOCK) {
/*  51 */       BlockPos pos = hitResult.getBlockPos();
/*  52 */       Direction direction = hitResult.getDirection();
/*  53 */       BlockPos directionOffsetPos = pos.relative(direction);
/*  55 */       if (!level.mayInteract((Entity)player, pos) || !player.mayUseItemAt(directionOffsetPos, direction, itemStack))
/*  56 */         return (InteractionResult)InteractionResult.FAIL; 
/*  59 */       BlockState clicked = level.getBlockState(pos);
/*  60 */       BlockPos placePos = (clicked.getBlock() instanceof net.minecraft.world.level.block.LiquidBlockContainer && this.content == Fluids.WATER) ? pos : directionOffsetPos;
/*  62 */       if (emptyContents((LivingEntity)player, level, placePos, hitResult)) {
/*  63 */         checkExtraContent((LivingEntity)player, level, itemStack, placePos);
/*  64 */         if (player instanceof ServerPlayer) {
/*  64 */           ServerPlayer serverPlayer = (ServerPlayer)player;
/*  64 */           if (this.content != Fluids.EMPTY)
/*  65 */             CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, placePos, itemStack); 
/*     */         } 
/*  67 */         player.awardStat(Stats.ITEM_USED.get(this));
/*  68 */         ItemStack emptyResult = ItemUtils.createFilledResult(itemStack, player, getEmptySuccessItem(itemStack, player));
/*  69 */         return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(emptyResult);
/*     */       } 
/*  72 */       if (this.content == Fluids.EMPTY) {
/*  73 */         BlockState blockState = level.getBlockState(pos);
/*  75 */         Block block = blockState.getBlock();
/*  75 */         if (block instanceof BucketPickup) {
/*  75 */           BucketPickup bucketPickupBlock = (BucketPickup)block;
/*  76 */           ItemStack taken = bucketPickupBlock.pickupBlock((LivingEntity)player, (LevelAccessor)level, pos, blockState);
/*  77 */           if (!taken.isEmpty()) {
/*  78 */             player.awardStat(Stats.ITEM_USED.get(this));
/*  79 */             bucketPickupBlock.getPickupSound().ifPresent(soundEvent -> player.playSound(soundEvent, 1.0F, 1.0F));
/*  80 */             level.gameEvent((Entity)player, (Holder)GameEvent.FLUID_PICKUP, pos);
/*  81 */             ItemStack result = ItemUtils.createFilledResult(itemStack, player, taken);
/*  82 */             if (!level.isClientSide())
/*  83 */               CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, taken); 
/*  85 */             return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(result);
/*     */           } 
/*     */         } 
/*     */       } 
/*  89 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     } 
/*  92 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public ClipContext.Fluid getFluidContext() {
/*  96 */     return (this.content == Fluids.EMPTY) ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE;
/*     */   }
/*     */   
/*     */   public static ItemStack getEmptySuccessItem(ItemStack itemStack, Player player) {
/* 100 */     if (!player.hasInfiniteMaterials())
/* 101 */       return new ItemStack(Items.BUCKET); 
/* 103 */     return itemStack;
/*     */   }
/*     */   
/*     */   public void checkExtraContent(LivingEntity user, Level level, ItemStack itemStack, BlockPos pos) {}
/*     */   
/*     */   public boolean emptyContents(LivingEntity user, Level level, BlockPos pos, BlockHitResult hitResult) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield content : Lnet/minecraft/world/level/material/Fluid;
/*     */     //   4: astore #6
/*     */     //   6: aload #6
/*     */     //   8: instanceof net/minecraft/world/level/material/FlowingFluid
/*     */     //   11: ifeq -> 24
/*     */     //   14: aload #6
/*     */     //   16: checkcast net/minecraft/world/level/material/FlowingFluid
/*     */     //   19: astore #5
/*     */     //   21: goto -> 26
/*     */     //   24: iconst_0
/*     */     //   25: ireturn
/*     */     //   26: aload_2
/*     */     //   27: aload_3
/*     */     //   28: invokevirtual getBlockState : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   31: astore #6
/*     */     //   33: aload #6
/*     */     //   35: invokevirtual getBlock : ()Lnet/minecraft/world/level/block/Block;
/*     */     //   38: astore #7
/*     */     //   40: aload #6
/*     */     //   42: aload_0
/*     */     //   43: getfield content : Lnet/minecraft/world/level/material/Fluid;
/*     */     //   46: invokevirtual canBeReplaced : (Lnet/minecraft/world/level/material/Fluid;)Z
/*     */     //   49: istore #8
/*     */     //   51: aload_1
/*     */     //   52: ifnull -> 66
/*     */     //   55: aload_1
/*     */     //   56: invokevirtual isShiftKeyDown : ()Z
/*     */     //   59: ifeq -> 66
/*     */     //   62: iconst_1
/*     */     //   63: goto -> 67
/*     */     //   66: iconst_0
/*     */     //   67: istore #9
/*     */     //   69: iload #8
/*     */     //   71: ifne -> 108
/*     */     //   74: aload #7
/*     */     //   76: instanceof net/minecraft/world/level/block/LiquidBlockContainer
/*     */     //   79: ifeq -> 112
/*     */     //   82: aload #7
/*     */     //   84: checkcast net/minecraft/world/level/block/LiquidBlockContainer
/*     */     //   87: astore #11
/*     */     //   89: aload #11
/*     */     //   91: aload_1
/*     */     //   92: aload_2
/*     */     //   93: aload_3
/*     */     //   94: aload #6
/*     */     //   96: aload_0
/*     */     //   97: getfield content : Lnet/minecraft/world/level/material/Fluid;
/*     */     //   100: invokeinterface canPlaceLiquid : (Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/Fluid;)Z
/*     */     //   105: ifeq -> 112
/*     */     //   108: iconst_1
/*     */     //   109: goto -> 113
/*     */     //   112: iconst_0
/*     */     //   113: istore #10
/*     */     //   115: aload #6
/*     */     //   117: invokevirtual isAir : ()Z
/*     */     //   120: ifne -> 138
/*     */     //   123: iload #10
/*     */     //   125: ifeq -> 142
/*     */     //   128: iload #9
/*     */     //   130: ifeq -> 138
/*     */     //   133: aload #4
/*     */     //   135: ifnonnull -> 142
/*     */     //   138: iconst_1
/*     */     //   139: goto -> 143
/*     */     //   142: iconst_0
/*     */     //   143: istore #11
/*     */     //   145: iload #11
/*     */     //   147: ifne -> 184
/*     */     //   150: aload #4
/*     */     //   152: ifnull -> 182
/*     */     //   155: aload_0
/*     */     //   156: aload_1
/*     */     //   157: aload_2
/*     */     //   158: aload #4
/*     */     //   160: invokevirtual getBlockPos : ()Lnet/minecraft/core/BlockPos;
/*     */     //   163: aload #4
/*     */     //   165: invokevirtual getDirection : ()Lnet/minecraft/core/Direction;
/*     */     //   168: invokevirtual relative : (Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;
/*     */     //   171: aconst_null
/*     */     //   172: invokevirtual emptyContents : (Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/BlockHitResult;)Z
/*     */     //   175: ifeq -> 182
/*     */     //   178: iconst_1
/*     */     //   179: goto -> 183
/*     */     //   182: iconst_0
/*     */     //   183: ireturn
/*     */     //   184: aload_2
/*     */     //   185: invokevirtual environmentAttributes : ()Lnet/minecraft/world/attribute/EnvironmentAttributeSystem;
/*     */     //   188: getstatic net/minecraft/world/attribute/EnvironmentAttributes.WATER_EVAPORATES : Lnet/minecraft/world/attribute/EnvironmentAttribute;
/*     */     //   191: aload_3
/*     */     //   192: invokevirtual getValue : (Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/core/BlockPos;)Ljava/lang/Object;
/*     */     //   195: checkcast java/lang/Boolean
/*     */     //   198: invokevirtual booleanValue : ()Z
/*     */     //   201: ifeq -> 343
/*     */     //   204: aload_0
/*     */     //   205: getfield content : Lnet/minecraft/world/level/material/Fluid;
/*     */     //   208: getstatic net/minecraft/tags/FluidTags.WATER : Lnet/minecraft/tags/TagKey;
/*     */     //   211: invokevirtual is : (Lnet/minecraft/tags/TagKey;)Z
/*     */     //   214: ifeq -> 343
/*     */     //   217: aload_3
/*     */     //   218: invokevirtual getX : ()I
/*     */     //   221: istore #12
/*     */     //   223: aload_3
/*     */     //   224: invokevirtual getY : ()I
/*     */     //   227: istore #13
/*     */     //   229: aload_3
/*     */     //   230: invokevirtual getZ : ()I
/*     */     //   233: istore #14
/*     */     //   235: aload_2
/*     */     //   236: invokevirtual getRandom : ()Lnet/minecraft/util/RandomSource;
/*     */     //   239: astore #15
/*     */     //   241: aload_2
/*     */     //   242: aload_1
/*     */     //   243: aload_3
/*     */     //   244: getstatic net/minecraft/sounds/SoundEvents.FIRE_EXTINGUISH : Lnet/minecraft/sounds/SoundEvent;
/*     */     //   247: getstatic net/minecraft/sounds/SoundSource.BLOCKS : Lnet/minecraft/sounds/SoundSource;
/*     */     //   250: ldc_w 0.5
/*     */     //   253: ldc_w 2.6
/*     */     //   256: aload #15
/*     */     //   258: invokeinterface nextFloat : ()F
/*     */     //   263: aload #15
/*     */     //   265: invokeinterface nextFloat : ()F
/*     */     //   270: fsub
/*     */     //   271: ldc_w 0.8
/*     */     //   274: fmul
/*     */     //   275: fadd
/*     */     //   276: invokevirtual playSound : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V
/*     */     //   279: iconst_0
/*     */     //   280: istore #16
/*     */     //   282: iload #16
/*     */     //   284: bipush #8
/*     */     //   286: if_icmpge -> 341
/*     */     //   289: aload_2
/*     */     //   290: getstatic net/minecraft/core/particles/ParticleTypes.LARGE_SMOKE : Lnet/minecraft/core/particles/SimpleParticleType;
/*     */     //   293: iload #12
/*     */     //   295: i2f
/*     */     //   296: aload #15
/*     */     //   298: invokeinterface nextFloat : ()F
/*     */     //   303: fadd
/*     */     //   304: f2d
/*     */     //   305: iload #13
/*     */     //   307: i2f
/*     */     //   308: aload #15
/*     */     //   310: invokeinterface nextFloat : ()F
/*     */     //   315: fadd
/*     */     //   316: f2d
/*     */     //   317: iload #14
/*     */     //   319: i2f
/*     */     //   320: aload #15
/*     */     //   322: invokeinterface nextFloat : ()F
/*     */     //   327: fadd
/*     */     //   328: f2d
/*     */     //   329: dconst_0
/*     */     //   330: dconst_0
/*     */     //   331: dconst_0
/*     */     //   332: invokevirtual addParticle : (Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V
/*     */     //   335: iinc #16, 1
/*     */     //   338: goto -> 282
/*     */     //   341: iconst_1
/*     */     //   342: ireturn
/*     */     //   343: aload #7
/*     */     //   345: instanceof net/minecraft/world/level/block/LiquidBlockContainer
/*     */     //   348: ifeq -> 395
/*     */     //   351: aload #7
/*     */     //   353: checkcast net/minecraft/world/level/block/LiquidBlockContainer
/*     */     //   356: astore #12
/*     */     //   358: aload_0
/*     */     //   359: getfield content : Lnet/minecraft/world/level/material/Fluid;
/*     */     //   362: getstatic net/minecraft/world/level/material/Fluids.WATER : Lnet/minecraft/world/level/material/FlowingFluid;
/*     */     //   365: if_acmpne -> 395
/*     */     //   368: aload #12
/*     */     //   370: aload_2
/*     */     //   371: aload_3
/*     */     //   372: aload #6
/*     */     //   374: aload #5
/*     */     //   376: iconst_0
/*     */     //   377: invokevirtual getSource : (Z)Lnet/minecraft/world/level/material/FluidState;
/*     */     //   380: invokeinterface placeLiquid : (Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)Z
/*     */     //   385: pop
/*     */     //   386: aload_0
/*     */     //   387: aload_1
/*     */     //   388: aload_2
/*     */     //   389: aload_3
/*     */     //   390: invokevirtual playEmptySound : (Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)V
/*     */     //   393: iconst_1
/*     */     //   394: ireturn
/*     */     //   395: aload_2
/*     */     //   396: invokevirtual isClientSide : ()Z
/*     */     //   399: ifne -> 422
/*     */     //   402: iload #8
/*     */     //   404: ifeq -> 422
/*     */     //   407: aload #6
/*     */     //   409: invokevirtual liquid : ()Z
/*     */     //   412: ifne -> 422
/*     */     //   415: aload_2
/*     */     //   416: aload_3
/*     */     //   417: iconst_1
/*     */     //   418: invokevirtual destroyBlock : (Lnet/minecraft/core/BlockPos;Z)Z
/*     */     //   421: pop
/*     */     //   422: aload_2
/*     */     //   423: aload_3
/*     */     //   424: aload_0
/*     */     //   425: getfield content : Lnet/minecraft/world/level/material/Fluid;
/*     */     //   428: invokevirtual defaultFluidState : ()Lnet/minecraft/world/level/material/FluidState;
/*     */     //   431: invokevirtual createLegacyBlock : ()Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   434: bipush #11
/*     */     //   436: invokevirtual setBlock : (Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z
/*     */     //   439: ifne -> 453
/*     */     //   442: aload #6
/*     */     //   444: invokevirtual getFluidState : ()Lnet/minecraft/world/level/material/FluidState;
/*     */     //   447: invokevirtual isSource : ()Z
/*     */     //   450: ifeq -> 462
/*     */     //   453: aload_0
/*     */     //   454: aload_1
/*     */     //   455: aload_2
/*     */     //   456: aload_3
/*     */     //   457: invokevirtual playEmptySound : (Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)V
/*     */     //   460: iconst_1
/*     */     //   461: ireturn
/*     */     //   462: iconst_0
/*     */     //   463: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     //   #113	-> 24
/*     */     //   #116	-> 26
/*     */     //   #117	-> 33
/*     */     //   #118	-> 40
/*     */     //   #120	-> 51
/*     */     //   #121	-> 69
/*     */     //   #122	-> 115
/*     */     //   #124	-> 145
/*     */     //   #127	-> 150
/*     */     //   #130	-> 184
/*     */     //   #131	-> 217
/*     */     //   #132	-> 223
/*     */     //   #133	-> 229
/*     */     //   #135	-> 235
/*     */     //   #136	-> 241
/*     */     //   #138	-> 279
/*     */     //   #139	-> 289
/*     */     //   #138	-> 335
/*     */     //   #141	-> 341
/*     */     //   #144	-> 343
/*     */     //   #145	-> 368
/*     */     //   #146	-> 386
/*     */     //   #147	-> 393
/*     */     //   #151	-> 395
/*     */     //   #152	-> 415
/*     */     //   #156	-> 422
/*     */     //   #157	-> 453
/*     */     //   #158	-> 460
/*     */     //   #161	-> 462
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   21	3	5	flowingFluid	Lnet/minecraft/world/level/material/FlowingFluid;
/*     */     //   89	19	11	container	Lnet/minecraft/world/level/block/LiquidBlockContainer;
/*     */     //   282	59	16	i	I
/*     */     //   223	120	12	x	I
/*     */     //   229	114	13	y	I
/*     */     //   235	108	14	z	I
/*     */     //   241	102	15	random	Lnet/minecraft/util/RandomSource;
/*     */     //   358	37	12	container	Lnet/minecraft/world/level/block/LiquidBlockContainer;
/*     */     //   0	464	0	this	Lnet/minecraft/world/item/BucketItem;
/*     */     //   0	464	1	user	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   0	464	2	level	Lnet/minecraft/world/level/Level;
/*     */     //   0	464	3	pos	Lnet/minecraft/core/BlockPos;
/*     */     //   0	464	4	hitResult	Lnet/minecraft/world/phys/BlockHitResult;
/*     */     //   26	438	5	flowingFluid	Lnet/minecraft/world/level/material/FlowingFluid;
/*     */     //   33	431	6	blockState	Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   40	424	7	block	Lnet/minecraft/world/level/block/Block;
/*     */     //   51	413	8	mayReplace	Z
/*     */     //   69	395	9	shiftKeyDown	Z
/*     */     //   115	349	10	placeLiquid	Z
/*     */     //   145	319	11	canPlaceFluidInsideBlock	Z
/*     */   }
/*     */   
/*     */   protected void playEmptySound(LivingEntity user, LevelAccessor level, BlockPos pos) {
/* 165 */     SoundEvent soundEvent = this.content.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
/* 166 */     level.playSound((Entity)user, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 167 */     level.gameEvent((Entity)user, (Holder)GameEvent.FLUID_PLACE, pos);
/*     */   }
/*     */   
/*     */   public Fluid getContent() {
/* 171 */     return this.content;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\BucketItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */