/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*     */ import net.minecraft.commands.arguments.SlotArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*     */ import net.minecraft.commands.arguments.item.ItemArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LootCommand {
/*     */   private static final DynamicCommandExceptionType ERROR_NO_HELD_ITEMS;
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_NO_ENTITY_LOOT_TABLE;
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_NO_BLOCK_LOOT_TABLE;
/*     */   
/*     */   static {
/*  61 */     ERROR_NO_HELD_ITEMS = new DynamicCommandExceptionType(entity -> Component.translatableEscape("commands.drop.no_held_items", new Object[] { entity }));
/*  62 */     ERROR_NO_ENTITY_LOOT_TABLE = new DynamicCommandExceptionType(entity -> Component.translatableEscape("commands.drop.no_loot_table.entity", new Object[] { entity }));
/*  63 */     ERROR_NO_BLOCK_LOOT_TABLE = new DynamicCommandExceptionType(block -> Component.translatableEscape("commands.drop.no_loot_table.block", new Object[] { block }));
/*     */   }
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  66 */     dispatcher.register(
/*  67 */         addTargets(
/*  68 */           (LiteralArgumentBuilder)Commands.literal("loot")
/*  69 */           .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)), (target, output) -> target.then(Commands.literal("fish").then(Commands.argument("loot_table", (ArgumentType)ResourceOrIdArgument.lootTable(context)).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos()).executes(())).then(Commands.argument("tool", (ArgumentType)ItemArgument.item(context)).executes(()))).then(Commands.literal("mainhand").executes(()))).then(Commands.literal("offhand").executes(()))))).then(Commands.literal("loot").then(Commands.argument("loot_table", (ArgumentType)ResourceOrIdArgument.lootTable(context)).executes(()))).then(Commands.literal("kill").then(Commands.argument("target", (ArgumentType)EntityArgument.entity()).executes(()))).then(Commands.literal("mine").then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos()).executes(())).then(Commands.argument("tool", (ArgumentType)ItemArgument.item(context)).executes(()))).then(Commands.literal("mainhand").executes(()))).then(Commands.literal("offhand").executes(()))))));
/*     */   }
/*     */   
/*     */   private static <T extends ArgumentBuilder<CommandSourceStack, T>> T addTargets(T root, TailProvider tail) {
/* 147 */     return (T)root
/* 148 */       .then((
/* 149 */         (LiteralArgumentBuilder)Commands.literal("replace")
/* 150 */         .then(Commands.literal("entity")
/* 151 */           .then(
/* 152 */             Commands.argument("entities", (ArgumentType)EntityArgument.entities())
/* 153 */             .then(tail
/* 154 */               .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("slot", (ArgumentType)SlotArgument.slot()), (c, drops, callback) -> entityReplace(EntityArgument.getEntities(c, "entities"), SlotArgument.getSlot(c, "slot"), drops.size(), drops, callback))
/*     */               
/* 157 */               .then(tail
/* 158 */                 .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(0)), (c, drops, callback) -> entityReplace(EntityArgument.getEntities(c, "entities"), SlotArgument.getSlot(c, "slot"), IntegerArgumentType.getInteger(c, "count"), drops, callback)))))))
/*     */         
/* 165 */         .then(Commands.literal("block")
/* 166 */           .then(
/* 167 */             Commands.argument("targetPos", (ArgumentType)BlockPosArgument.blockPos())
/* 168 */             .then(tail
/* 169 */               .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("slot", (ArgumentType)SlotArgument.slot()), (c, drops, callback) -> blockReplace((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "targetPos"), SlotArgument.getSlot(c, "slot"), drops.size(), drops, callback))
/*     */               
/* 172 */               .then(tail
/* 173 */                 .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(0)), (c, drops, callback) -> blockReplace((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "targetPos"), IntegerArgumentType.getInteger(c, "slot"), IntegerArgumentType.getInteger(c, "count"), drops, callback)))))))
/*     */       
/* 181 */       .then(
/* 182 */         Commands.literal("insert")
/* 183 */         .then(tail
/* 184 */           .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("targetPos", (ArgumentType)BlockPosArgument.blockPos()), (c, drops, callback) -> blockDistribute((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "targetPos"), drops, callback))))
/*     */       
/* 189 */       .then(
/* 190 */         Commands.literal("give")
/* 191 */         .then(tail
/* 192 */           .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("players", (ArgumentType)EntityArgument.players()), (c, drops, callback) -> playerGive(EntityArgument.getPlayers(c, "players"), drops, callback))))
/*     */       
/* 197 */       .then(
/* 198 */         Commands.literal("spawn")
/* 199 */         .then(tail
/* 200 */           .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("targetPos", (ArgumentType)Vec3Argument.vec3()), (c, drops, callback) -> dropInWorld((CommandSourceStack)c.getSource(), Vec3Argument.getVec3(c, "targetPos"), drops, callback))));
/*     */   }
/*     */   
/*     */   private static Container getContainer(CommandSourceStack source, BlockPos pos) throws CommandSyntaxException {
/*     */     Container container;
/* 208 */     BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);
/* 209 */     if (blockEntity instanceof Container) {
/* 209 */       container = (Container)blockEntity;
/*     */     } else {
/* 210 */       throw ItemCommands.ERROR_TARGET_NOT_A_CONTAINER.create(Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()));
/*     */     } 
/* 213 */     return container;
/*     */   }
/*     */   
/*     */   private static int blockDistribute(CommandSourceStack source, BlockPos pos, List<ItemStack> drops, Callback callback) throws CommandSyntaxException {
/* 217 */     Container container = getContainer(source, pos);
/* 219 */     List<ItemStack> usedItems = Lists.newArrayListWithCapacity(drops.size());
/* 220 */     for (ItemStack drop : drops) {
/* 221 */       if (distributeToContainer(container, drop.copy())) {
/* 222 */         container.setChanged();
/* 223 */         usedItems.add(drop);
/*     */       } 
/*     */     } 
/* 227 */     callback.accept(usedItems);
/* 228 */     return usedItems.size();
/*     */   }
/*     */   
/*     */   private static boolean distributeToContainer(Container container, ItemStack itemStack) {
/* 232 */     boolean changed = false;
/* 234 */     for (int slot = 0; slot < container.getContainerSize() && !itemStack.isEmpty(); slot++) {
/* 235 */       ItemStack current = container.getItem(slot);
/* 237 */       if (container.canPlaceItem(slot, itemStack)) {
/* 238 */         if (current.isEmpty()) {
/* 239 */           container.setItem(slot, itemStack);
/* 240 */           changed = true;
/*     */           break;
/*     */         } 
/* 242 */         if (canMergeItems(current, itemStack)) {
/* 243 */           int space = itemStack.getMaxStackSize() - current.getCount();
/* 244 */           int count = Math.min(itemStack.getCount(), space);
/* 246 */           itemStack.shrink(count);
/* 247 */           current.grow(count);
/* 248 */           changed = true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 253 */     return changed;
/*     */   }
/*     */   
/*     */   private static int blockReplace(CommandSourceStack source, BlockPos pos, int startSlot, int slotCount, List<ItemStack> drops, Callback callback) throws CommandSyntaxException {
/* 257 */     Container container = getContainer(source, pos);
/* 259 */     int maxSlot = container.getContainerSize();
/* 260 */     if (startSlot < 0 || startSlot >= maxSlot)
/* 261 */       throw ItemCommands.ERROR_TARGET_INAPPLICABLE_SLOT.create(Integer.valueOf(startSlot)); 
/* 264 */     List<ItemStack> usedItems = Lists.newArrayListWithCapacity(drops.size());
/* 266 */     for (int i = 0; i < slotCount; i++) {
/* 267 */       int slot = startSlot + i;
/* 268 */       ItemStack toAdd = (i < drops.size()) ? drops.get(i) : ItemStack.EMPTY;
/* 270 */       if (container.canPlaceItem(slot, toAdd)) {
/* 271 */         container.setItem(slot, toAdd);
/* 272 */         usedItems.add(toAdd);
/*     */       } 
/*     */     } 
/* 276 */     callback.accept(usedItems);
/* 277 */     return usedItems.size();
/*     */   }
/*     */   
/*     */   private static boolean canMergeItems(ItemStack a, ItemStack b) {
/* 281 */     return (a.getCount() <= a.getMaxStackSize() && ItemStack.isSameItemSameComponents(a, b));
/*     */   }
/*     */   
/*     */   private static int playerGive(Collection<ServerPlayer> players, List<ItemStack> drops, Callback callback) throws CommandSyntaxException {
/* 285 */     List<ItemStack> usedItems = Lists.newArrayListWithCapacity(drops.size());
/* 286 */     for (ItemStack drop : drops) {
/* 287 */       for (ServerPlayer player : players) {
/* 288 */         if (player.getInventory().add(drop.copy()))
/* 289 */           usedItems.add(drop); 
/*     */       } 
/*     */     } 
/* 294 */     callback.accept(usedItems);
/* 295 */     return usedItems.size();
/*     */   }
/*     */   
/*     */   private static void setSlots(Entity entity, List<ItemStack> itemsToSet, int startSlot, int count, List<ItemStack> usedItems) {
/* 299 */     for (int i = 0; i < count; i++) {
/* 300 */       ItemStack item = (i < itemsToSet.size()) ? itemsToSet.get(i) : ItemStack.EMPTY;
/* 301 */       SlotAccess slotAccess = entity.getSlot(startSlot + i);
/* 302 */       if (slotAccess != null && slotAccess.set(item.copy()))
/* 303 */         usedItems.add(item); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int entityReplace(Collection<? extends Entity> entities, int startSlot, int count, List<ItemStack> drops, Callback callback) throws CommandSyntaxException {
/* 309 */     List<ItemStack> usedItems = Lists.newArrayListWithCapacity(drops.size());
/* 311 */     for (Entity entity : entities) {
/* 312 */       if (entity instanceof ServerPlayer) {
/* 312 */         ServerPlayer player = (ServerPlayer)entity;
/* 313 */         setSlots(entity, drops, startSlot, count, usedItems);
/* 314 */         player.containerMenu.broadcastChanges();
/*     */         continue;
/*     */       } 
/* 316 */       setSlots(entity, drops, startSlot, count, usedItems);
/*     */     } 
/* 320 */     callback.accept(usedItems);
/* 321 */     return usedItems.size();
/*     */   }
/*     */   
/*     */   private static int dropInWorld(CommandSourceStack source, Vec3 pos, List<ItemStack> drops, Callback callback) throws CommandSyntaxException {
/* 325 */     ServerLevel level = source.getLevel();
/* 326 */     drops.forEach(drop -> {
/*     */           ItemEntity entity = new ItemEntity((Level)level, pos.x, pos.y, pos.z, drop.copy());
/*     */           entity.setDefaultPickUpDelay();
/*     */           level.addFreshEntity((Entity)entity);
/*     */         });
/* 332 */     callback.accept(drops);
/* 333 */     return drops.size();
/*     */   }
/*     */   
/*     */   private static void callback(CommandSourceStack source, List<ItemStack> drops) {
/* 337 */     if (drops.size() == 1) {
/* 338 */       ItemStack drop = drops.get(0);
/* 339 */       source.sendSuccess(() -> Component.translatable("commands.drop.success.single", new Object[] { Integer.valueOf(drop.getCount()), drop.getDisplayName() }), false);
/*     */     } else {
/* 341 */       source.sendSuccess(() -> Component.translatable("commands.drop.success.multiple", new Object[] { Integer.valueOf(drops.size()) }), false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void callback(CommandSourceStack source, List<ItemStack> drops, ResourceKey<LootTable> location) {
/* 346 */     if (drops.size() == 1) {
/* 347 */       ItemStack drop = drops.get(0);
/* 348 */       source.sendSuccess(() -> Component.translatable("commands.drop.success.single_with_table", new Object[] { Integer.valueOf(drop.getCount()), drop.getDisplayName(), Component.translationArg(location.identifier()) }), false);
/*     */     } else {
/* 350 */       source.sendSuccess(() -> Component.translatable("commands.drop.success.multiple_with_table", new Object[] { Integer.valueOf(drops.size()), Component.translationArg(location.identifier()) }), false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ItemStack getSourceHandItem(CommandSourceStack source, EquipmentSlot slot) throws CommandSyntaxException {
/* 355 */     Entity entity = source.getEntityOrException();
/* 356 */     if (entity instanceof LivingEntity) {
/* 356 */       LivingEntity livingEntity = (LivingEntity)entity;
/* 357 */       return livingEntity.getItemBySlot(slot);
/*     */     } 
/* 359 */     throw ERROR_NO_HELD_ITEMS.create(entity.getDisplayName());
/*     */   }
/*     */   
/*     */   private static int dropBlockLoot(CommandContext<CommandSourceStack> context, BlockPos pos, ItemInstance tool, DropConsumer output) throws CommandSyntaxException {
/* 364 */     CommandSourceStack source = (CommandSourceStack)context.getSource();
/* 365 */     ServerLevel level = source.getLevel();
/* 366 */     BlockState blockState = level.getBlockState(pos);
/* 367 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 369 */     Optional<ResourceKey<LootTable>> lootTable = blockState.getBlock().getLootTable();
/* 370 */     if (lootTable.isEmpty())
/* 371 */       throw ERROR_NO_BLOCK_LOOT_TABLE.create(blockState.getBlock().getName()); 
/* 379 */     LootParams.Builder lootParams = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)pos)).withParameter(LootContextParams.BLOCK_STATE, blockState).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity).withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity()).withParameter(LootContextParams.TOOL, tool);
/* 381 */     List<ItemStack> drops = blockState.getDrops(lootParams);
/* 382 */     return output.accept(context, drops, usedItems -> callback(source, usedItems, lootTable.get()));
/*     */   }
/*     */   
/*     */   private static int dropKillLoot(CommandContext<CommandSourceStack> context, Entity target, DropConsumer output) throws CommandSyntaxException {
/* 386 */     Optional<ResourceKey<LootTable>> lootTableId = target.getLootTable();
/* 387 */     if (lootTableId.isEmpty())
/* 388 */       throw ERROR_NO_ENTITY_LOOT_TABLE.create(target.getDisplayName()); 
/* 390 */     CommandSourceStack source = (CommandSourceStack)context.getSource();
/* 392 */     LootParams.Builder builder = new LootParams.Builder(source.getLevel());
/* 393 */     Entity killer = source.getEntity();
/* 394 */     if (killer instanceof Player) {
/* 394 */       Player player = (Player)killer;
/* 395 */       builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player);
/*     */     } 
/* 397 */     builder.withParameter(LootContextParams.DAMAGE_SOURCE, target.damageSources().magic());
/* 398 */     builder.withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, killer);
/* 399 */     builder.withOptionalParameter(LootContextParams.ATTACKING_ENTITY, killer);
/* 400 */     builder.withParameter(LootContextParams.THIS_ENTITY, target);
/* 401 */     builder.withParameter(LootContextParams.ORIGIN, source.getPosition());
/* 402 */     LootParams lootParams = builder.create(LootContextParamSets.ENTITY);
/* 404 */     LootTable lootTable = source.getServer().reloadableRegistries().getLootTable(lootTableId.get());
/* 405 */     ObjectArrayList objectArrayList = lootTable.getRandomItems(lootParams);
/* 406 */     return output.accept(context, (List<ItemStack>)objectArrayList, usedItems -> callback(source, usedItems, lootTableId.get()));
/*     */   }
/*     */   
/*     */   private static int dropChestLoot(CommandContext<CommandSourceStack> context, Holder<LootTable> lootTable, DropConsumer output) throws CommandSyntaxException {
/* 410 */     CommandSourceStack source = (CommandSourceStack)context.getSource();
/* 415 */     LootParams lootParams = (new LootParams.Builder(source.getLevel())).withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity()).withParameter(LootContextParams.ORIGIN, source.getPosition()).create(LootContextParamSets.CHEST);
/* 417 */     return drop(context, lootTable, lootParams, output);
/*     */   }
/*     */   
/*     */   private static int dropFishingLoot(CommandContext<CommandSourceStack> context, Holder<LootTable> lootTable, BlockPos pos, ItemInstance tool, DropConsumer output) throws CommandSyntaxException {
/* 421 */     CommandSourceStack source = (CommandSourceStack)context.getSource();
/* 427 */     LootParams lootParams = (new LootParams.Builder(source.getLevel())).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)pos)).withParameter(LootContextParams.TOOL, tool).withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity()).create(LootContextParamSets.FISHING);
/* 429 */     return drop(context, lootTable, lootParams, output);
/*     */   }
/*     */   
/*     */   private static int drop(CommandContext<CommandSourceStack> context, Holder<LootTable> lootTable, LootParams lootParams, DropConsumer output) throws CommandSyntaxException {
/* 433 */     CommandSourceStack source = (CommandSourceStack)context.getSource();
/* 434 */     ObjectArrayList objectArrayList = ((LootTable)lootTable.value()).getRandomItems(lootParams);
/* 435 */     return output.accept(context, (List<ItemStack>)objectArrayList, usedItems -> callback(source, usedItems));
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface TailProvider {
/*     */     ArgumentBuilder<CommandSourceStack, ?> construct(ArgumentBuilder<CommandSourceStack, ?> param1ArgumentBuilder, LootCommand.DropConsumer param1DropConsumer);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface DropConsumer {
/*     */     int accept(CommandContext<CommandSourceStack> param1CommandContext, List<ItemStack> param1List, LootCommand.Callback param1Callback) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface Callback {
/*     */     void accept(List<ItemStack> param1List) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\LootCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */