/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.Spawner;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SpawnEggItem extends Item {
/*     */   public SpawnEggItem(Item.Properties properties) {
/*  40 */     super(properties);
/*     */   }
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/*     */     ServerLevel serverLevel;
/*     */     BlockPos spawnPos;
/*  45 */     Level level = context.getLevel();
/*  46 */     ItemStack itemStack = context.getItemInHand();
/*  47 */     EntityType<?> type = getType(itemStack);
/*  48 */     if (type == null || !type.canSpawn(level))
/*  49 */       return (InteractionResult)InteractionResult.FAIL; 
/*  52 */     if (level instanceof ServerLevel) {
/*  52 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/*  53 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*  56 */     BlockPos pos = context.getClickedPos();
/*  57 */     Direction clickedFace = context.getClickedFace();
/*  59 */     BlockState blockState = level.getBlockState(pos);
/*  60 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  60 */     if (blockEntity instanceof Spawner) {
/*  60 */       Spawner spawnerHolder = (Spawner)blockEntity;
/*  61 */       if (!serverLevel.isSpawnerBlockEnabled()) {
/*  62 */         Player player = context.getPlayer();
/*  62 */         if (player instanceof ServerPlayer) {
/*  62 */           ServerPlayer serverPlayer = (ServerPlayer)player;
/*  63 */           serverPlayer.sendSystemMessage((Component)Component.translatable("advMode.notEnabled.spawner"));
/*     */         } 
/*  65 */         return (InteractionResult)InteractionResult.FAIL;
/*     */       } 
/*  68 */       spawnerHolder.setEntityId(type, level.getRandom());
/*  69 */       level.sendBlockUpdated(pos, blockState, blockState, 3);
/*  70 */       level.gameEvent((Entity)context.getPlayer(), (Holder)GameEvent.BLOCK_CHANGE, pos);
/*  71 */       itemStack.shrink(1);
/*  72 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*  76 */     if (blockState.getCollisionShape((BlockGetter)level, pos).isEmpty()) {
/*  77 */       spawnPos = pos;
/*     */     } else {
/*  79 */       spawnPos = pos.relative(clickedFace);
/*     */     } 
/*  82 */     return spawnMob(type, (LivingEntity)context.getPlayer(), itemStack, serverLevel, spawnPos, true, (!Objects.equals(pos, spawnPos) && clickedFace == Direction.UP));
/*     */   }
/*     */   
/*     */   private static InteractionResult spawnMob(EntityType<?> type, LivingEntity user, ItemStack itemStack, ServerLevel level, BlockPos spawnPos, boolean tryMoveDown, boolean movedUp) {
/*  86 */     if (type.spawn(level, itemStack, user, spawnPos, EntitySpawnReason.SPAWN_ITEM_USE, tryMoveDown, movedUp) != null) {
/*  87 */       itemStack.consume(1, user);
/*  88 */       level.gameEvent((Entity)user, (Holder)GameEvent.ENTITY_PLACE, spawnPos);
/*  89 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*  92 */     return (InteractionResult)InteractionResult.FAIL;
/*     */   }
/*     */   
/*     */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/*     */     ServerLevel serverLevel;
/*  98 */     ItemStack itemStack = player.getItemInHand(hand);
/* 100 */     BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
/* 101 */     if (hitResult.getType() != HitResult.Type.BLOCK)
/* 102 */       return (InteractionResult)InteractionResult.PASS; 
/* 105 */     EntityType<?> type = getType(itemStack);
/* 106 */     if (type == null || !type.canSpawn(level))
/* 107 */       return (InteractionResult)InteractionResult.FAIL; 
/* 110 */     if (level instanceof ServerLevel) {
/* 110 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/* 111 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 114 */     BlockPos pos = hitResult.getBlockPos();
/* 115 */     if (!(level.getBlockState(pos).getBlock() instanceof net.minecraft.world.level.block.LiquidBlock))
/* 116 */       return (InteractionResult)InteractionResult.PASS; 
/* 118 */     if (!level.mayInteract((Entity)player, pos) || !player.mayUseItemAt(pos, hitResult.getDirection(), itemStack))
/* 119 */       return (InteractionResult)InteractionResult.FAIL; 
/* 121 */     InteractionResult result = spawnMob(type, (LivingEntity)player, itemStack, serverLevel, pos, false, false);
/* 122 */     if (result == InteractionResult.SUCCESS)
/* 123 */       player.awardStat(Stats.ITEM_USED.get(this)); 
/* 125 */     return result;
/*     */   }
/*     */   
/*     */   public static boolean spawnsEntity(ItemStack itemStack, EntityType<?> type) {
/* 129 */     return Objects.equals(getType(itemStack), type);
/*     */   }
/*     */   
/*     */   public static Optional<Holder<Item>> byId(EntityType<?> type) {
/* 134 */     return BuiltInRegistries.ITEM.componentLookup()
/* 135 */       .findMatching(DataComponents.ENTITY_DATA, c -> (c.type() == type))
/* 136 */       .findAny();
/*     */   }
/*     */   
/*     */   public static EntityType<?> getType(ItemStack itemStack) {
/* 140 */     TypedEntityData<EntityType<?>> entityData = (TypedEntityData<EntityType<?>>)itemStack.get(DataComponents.ENTITY_DATA);
/* 141 */     if (entityData != null)
/* 142 */       return (EntityType)entityData.type(); 
/* 144 */     return null;
/*     */   }
/*     */   
/*     */   public static Optional<Mob> spawnOffspringFromSpawnEgg(Player player, Mob parent, EntityType<? extends Mob> type, ServerLevel level, Vec3 pos, ItemStack spawnEggStack) {
/*     */     Mob offspring;
/* 148 */     if (!spawnsEntity(spawnEggStack, type))
/* 149 */       return Optional.empty(); 
/* 153 */     if (parent instanceof AgeableMob) {
/* 153 */       AgeableMob ageableMob = (AgeableMob)parent;
/* 154 */       AgeableMob ageableMob1 = ageableMob.getBreedOffspring(level, ageableMob);
/*     */     } else {
/* 156 */       offspring = (Mob)type.create((Level)level, EntitySpawnReason.SPAWN_ITEM_USE);
/*     */     } 
/* 158 */     if (offspring == null)
/* 159 */       return Optional.empty(); 
/* 162 */     offspring.setBaby(true);
/* 163 */     if (!offspring.isBaby())
/* 164 */       return Optional.empty(); 
/* 167 */     offspring.snapTo(pos.x(), pos.y(), pos.z(), 0.0F, 0.0F);
/* 168 */     offspring.applyComponentsFromItemStack(spawnEggStack);
/* 170 */     level.addFreshEntityWithPassengers((Entity)offspring);
/* 171 */     spawnEggStack.consume(1, (LivingEntity)player);
/* 172 */     return Optional.of(offspring);
/*     */   }
/*     */   
/*     */   public boolean shouldPrintOpWarning(ItemStack stack, Player player) {
/* 177 */     if (player != null && player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
/* 178 */       TypedEntityData<EntityType<?>> entityData = (TypedEntityData<EntityType<?>>)stack.get(DataComponents.ENTITY_DATA);
/* 179 */       if (entityData != null)
/* 180 */         return ((EntityType)entityData.type()).onlyOpCanSetNbt(); 
/*     */     } 
/* 184 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\SpawnEggItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */