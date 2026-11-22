/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*     */ import net.minecraft.commands.arguments.SlotArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.item.ItemArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SlotProvider;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ 
/*     */ public class ItemCommands {
/*     */   static final Dynamic3CommandExceptionType ERROR_TARGET_NOT_A_CONTAINER;
/*     */   
/*     */   static final Dynamic3CommandExceptionType ERROR_SOURCE_NOT_A_CONTAINER;
/*     */   
/*     */   static final DynamicCommandExceptionType ERROR_TARGET_INAPPLICABLE_SLOT;
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_SOURCE_INAPPLICABLE_SLOT;
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_TARGET_NO_CHANGES;
/*     */   
/*     */   private static final Dynamic2CommandExceptionType ERROR_TARGET_NO_CHANGES_KNOWN_ITEM;
/*     */   
/*     */   static {
/*  56 */     ERROR_TARGET_NOT_A_CONTAINER = new Dynamic3CommandExceptionType((x, y, z) -> Component.translatableEscape("commands.item.target.not_a_container", new Object[] { x, y, z }));
/*  58 */     ERROR_SOURCE_NOT_A_CONTAINER = new Dynamic3CommandExceptionType((x, y, z) -> Component.translatableEscape("commands.item.source.not_a_container", new Object[] { x, y, z }));
/*  60 */     ERROR_TARGET_INAPPLICABLE_SLOT = new DynamicCommandExceptionType(slot -> Component.translatableEscape("commands.item.target.no_such_slot", new Object[] { slot }));
/*  61 */     ERROR_SOURCE_INAPPLICABLE_SLOT = new DynamicCommandExceptionType(slot -> Component.translatableEscape("commands.item.source.no_such_slot", new Object[] { slot }));
/*  63 */     ERROR_TARGET_NO_CHANGES = new DynamicCommandExceptionType(slot -> Component.translatableEscape("commands.item.target.no_changes", new Object[] { slot }));
/*  64 */     ERROR_TARGET_NO_CHANGES_KNOWN_ITEM = new Dynamic2CommandExceptionType((item, slot) -> Component.translatableEscape("commands.item.target.no_changed.known_item", new Object[] { item, slot }));
/*     */   }
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  67 */     dispatcher.register(
/*  68 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("item")
/*  69 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  70 */         .then((
/*  71 */           (LiteralArgumentBuilder)Commands.literal("replace")
/*  72 */           .then(
/*  73 */             Commands.literal("block")
/*  74 */             .then(
/*  75 */               Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/*  76 */               .then((
/*  77 */                 (RequiredArgumentBuilder)Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/*  78 */                 .then(
/*  79 */                   Commands.literal("with")
/*  80 */                   .then((
/*  81 */                     (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemArgument.item(context))
/*  82 */                     .executes(c -> setBlockItem((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"), ItemArgument.getItem(c, "item").createItemStack(1))))
/*  83 */                     .then(
/*  84 */                       Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(1, 99))
/*  85 */                       .executes(c -> setBlockItem((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"), ItemArgument.getItem(c, "item").createItemStack(IntegerArgumentType.getInteger(c, "count"))))))))
/*     */                 
/*  89 */                 .then((
/*  90 */                   (LiteralArgumentBuilder)Commands.literal("from")
/*  91 */                   .then(
/*  92 */                     Commands.literal("block")
/*  93 */                     .then(
/*  94 */                       Commands.argument("source", (ArgumentType)BlockPosArgument.blockPos())
/*  95 */                       .then((
/*  96 */                         (RequiredArgumentBuilder)Commands.argument("sourceSlot", (ArgumentType)SlotArgument.slot())
/*  97 */                         .executes(c -> blockToBlock((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"))))
/*  98 */                         .then(
/*  99 */                           Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(context))
/* 100 */                           .executes(c -> blockToBlock((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"), ResourceOrIdArgument.getLootModifier(c, "modifier"))))))))
/*     */                   
/* 105 */                   .then(
/* 106 */                     Commands.literal("entity")
/* 107 */                     .then(
/* 108 */                       Commands.argument("source", (ArgumentType)EntityArgument.entity())
/* 109 */                       .then((
/* 110 */                         (RequiredArgumentBuilder)Commands.argument("sourceSlot", (ArgumentType)SlotArgument.slot())
/* 111 */                         .executes(c -> entityToBlock((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"))))
/* 112 */                         .then(
/* 113 */                           Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(context))
/* 114 */                           .executes(c -> entityToBlock((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"), ResourceOrIdArgument.getLootModifier(c, "modifier"))))))))))))
/*     */           
/* 123 */           .then(
/* 124 */             Commands.literal("entity")
/* 125 */             .then(
/* 126 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 127 */               .then((
/* 128 */                 (RequiredArgumentBuilder)Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/* 129 */                 .then(
/* 130 */                   Commands.literal("with")
/* 131 */                   .then((
/* 132 */                     (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemArgument.item(context))
/* 133 */                     .executes(c -> setEntityItem((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"), ItemArgument.getItem(c, "item").createItemStack(1))))
/* 134 */                     .then(
/* 135 */                       Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(1, 99))
/* 136 */                       .executes(c -> setEntityItem((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"), ItemArgument.getItem(c, "item").createItemStack(IntegerArgumentType.getInteger(c, "count"))))))))
/*     */                 
/* 140 */                 .then((
/* 141 */                   (LiteralArgumentBuilder)Commands.literal("from")
/* 142 */                   .then(
/* 143 */                     Commands.literal("block")
/* 144 */                     .then(
/* 145 */                       Commands.argument("source", (ArgumentType)BlockPosArgument.blockPos())
/* 146 */                       .then((
/* 147 */                         (RequiredArgumentBuilder)Commands.argument("sourceSlot", (ArgumentType)SlotArgument.slot())
/* 148 */                         .executes(c -> blockToEntities((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"))))
/* 149 */                         .then(
/* 150 */                           Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(context))
/* 151 */                           .executes(c -> blockToEntities((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"), ResourceOrIdArgument.getLootModifier(c, "modifier"))))))))
/*     */                   
/* 156 */                   .then(
/* 157 */                     Commands.literal("entity")
/* 158 */                     .then(
/* 159 */                       Commands.argument("source", (ArgumentType)EntityArgument.entity())
/* 160 */                       .then((
/* 161 */                         (RequiredArgumentBuilder)Commands.argument("sourceSlot", (ArgumentType)SlotArgument.slot())
/* 162 */                         .executes(c -> entityToEntities((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"))))
/* 163 */                         .then(
/* 164 */                           Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(context))
/* 165 */                           .executes(c -> entityToEntities((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "source"), SlotArgument.getSlot(c, "sourceSlot"), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"), ResourceOrIdArgument.getLootModifier(c, "modifier")))))))))))))
/*     */         
/* 175 */         .then((
/* 176 */           (LiteralArgumentBuilder)Commands.literal("modify")
/* 177 */           .then(
/* 178 */             Commands.literal("block")
/* 179 */             .then(
/* 180 */               Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/* 181 */               .then(
/* 182 */                 Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/* 183 */                 .then(
/* 184 */                   Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(context))
/* 185 */                   .executes(c -> modifyBlockItem((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "pos"), SlotArgument.getSlot(c, "slot"), ResourceOrIdArgument.getLootModifier(c, "modifier"))))))))
/*     */           
/* 190 */           .then(
/* 191 */             Commands.literal("entity")
/* 192 */             .then(
/* 193 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 194 */               .then(
/* 195 */                 Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/* 196 */                 .then(
/* 197 */                   Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(context))
/* 198 */                   .executes(c -> modifyEntityItem((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), SlotArgument.getSlot(c, "slot"), ResourceOrIdArgument.getLootModifier(c, "modifier")))))))));
/*     */   }
/*     */   
/*     */   private static int modifyBlockItem(CommandSourceStack source, BlockPos pos, int slot, Holder<LootItemFunction> modifier) throws CommandSyntaxException {
/* 208 */     Container container = getContainer(source, pos, ERROR_TARGET_NOT_A_CONTAINER);
/* 209 */     if (slot < 0 || slot >= container.getContainerSize())
/* 210 */       throw ERROR_TARGET_INAPPLICABLE_SLOT.create(Integer.valueOf(slot)); 
/* 213 */     ItemStack itemStack = applyModifier(source, modifier, container.getItem(slot));
/* 214 */     container.setItem(slot, itemStack);
/* 215 */     source.sendSuccess(() -> Component.translatable("commands.item.block.set.success", new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()), itemStack.getDisplayName() }), true);
/* 216 */     return 1;
/*     */   }
/*     */   
/*     */   private static int modifyEntityItem(CommandSourceStack source, Collection<? extends Entity> entities, int slot, Holder<LootItemFunction> modifier) throws CommandSyntaxException {
/* 220 */     Map<Entity, ItemStack> changedEntities = Maps.newHashMapWithExpectedSize(entities.size());
/* 222 */     for (Entity entity : entities) {
/* 223 */       SlotAccess slotAccess = entity.getSlot(slot);
/* 224 */       if (slotAccess != null) {
/* 225 */         ItemStack itemStack = applyModifier(source, modifier, slotAccess.get().copy());
/* 226 */         if (slotAccess.set(itemStack)) {
/* 227 */           changedEntities.put(entity, itemStack);
/* 228 */           if (entity instanceof ServerPlayer) {
/* 228 */             ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 229 */             serverPlayer.containerMenu.broadcastChanges();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 235 */     if (changedEntities.isEmpty())
/* 236 */       throw ERROR_TARGET_NO_CHANGES.create(Integer.valueOf(slot)); 
/* 239 */     if (changedEntities.size() == 1) {
/* 240 */       Map.Entry<Entity, ItemStack> e = changedEntities.entrySet().iterator().next();
/* 241 */       source.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.single", new Object[] { ((Entity)e.getKey()).getDisplayName(), ((ItemStack)e.getValue()).getDisplayName() }), true);
/*     */     } else {
/* 243 */       source.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.multiple", new Object[] { Integer.valueOf(changedEntities.size()) }), true);
/*     */     } 
/* 246 */     return changedEntities.size();
/*     */   }
/*     */   
/*     */   private static int setBlockItem(CommandSourceStack source, BlockPos pos, int slot, ItemStack itemStack) throws CommandSyntaxException {
/* 250 */     Container container = getContainer(source, pos, ERROR_TARGET_NOT_A_CONTAINER);
/* 251 */     if (slot < 0 || slot >= container.getContainerSize())
/* 252 */       throw ERROR_TARGET_INAPPLICABLE_SLOT.create(Integer.valueOf(slot)); 
/* 255 */     container.setItem(slot, itemStack);
/* 256 */     source.sendSuccess(() -> Component.translatable("commands.item.block.set.success", new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()), itemStack.getDisplayName() }), true);
/* 257 */     return 1;
/*     */   }
/*     */   
/*     */   static Container getContainer(CommandSourceStack source, BlockPos pos, Dynamic3CommandExceptionType exceptionType) throws CommandSyntaxException {
/* 262 */     BlockEntity entity = source.getLevel().getBlockEntity(pos);
/* 263 */     if (entity instanceof Container) {
/* 263 */       Container container = (Container)entity;
/* 264 */       return container;
/*     */     } 
/* 266 */     throw exceptionType.create(Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()));
/*     */   }
/*     */   
/*     */   private static int setEntityItem(CommandSourceStack source, Collection<? extends Entity> entities, int slot, ItemStack itemStack) throws CommandSyntaxException {
/* 270 */     List<Entity> changedEntities = Lists.newArrayListWithCapacity(entities.size());
/* 272 */     for (Entity entity : entities) {
/* 273 */       SlotAccess slotAccess = entity.getSlot(slot);
/* 274 */       if (slotAccess != null && slotAccess.set(itemStack.copy())) {
/* 275 */         changedEntities.add(entity);
/* 276 */         if (entity instanceof ServerPlayer) {
/* 276 */           ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 277 */           serverPlayer.containerMenu.broadcastChanges();
/*     */         } 
/*     */       } 
/*     */     } 
/* 282 */     if (changedEntities.isEmpty())
/* 283 */       throw ERROR_TARGET_NO_CHANGES_KNOWN_ITEM.create(itemStack.getDisplayName(), Integer.valueOf(slot)); 
/* 286 */     if (changedEntities.size() == 1) {
/* 287 */       source.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.single", new Object[] { ((Entity)changedEntities.getFirst()).getDisplayName(), itemStack.getDisplayName() }), true);
/*     */     } else {
/* 289 */       source.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.multiple", new Object[] { Integer.valueOf(changedEntities.size()), itemStack.getDisplayName() }), true);
/*     */     } 
/* 292 */     return changedEntities.size();
/*     */   }
/*     */   
/*     */   private static int blockToEntities(CommandSourceStack source, BlockPos sourcePos, int sourceSlot, Collection<? extends Entity> targetEntities, int targetSlot) throws CommandSyntaxException {
/* 296 */     return setEntityItem(source, targetEntities, targetSlot, getBlockItem(source, sourcePos, sourceSlot));
/*     */   }
/*     */   
/*     */   private static int blockToEntities(CommandSourceStack source, BlockPos sourcePos, int sourceSlot, Collection<? extends Entity> targetEntities, int targetSlot, Holder<LootItemFunction> modifier) throws CommandSyntaxException {
/* 300 */     return setEntityItem(source, targetEntities, targetSlot, applyModifier(source, modifier, getBlockItem(source, sourcePos, sourceSlot)));
/*     */   }
/*     */   
/*     */   private static int blockToBlock(CommandSourceStack source, BlockPos sourcePos, int sourceSlot, BlockPos targetPos, int targetSlot) throws CommandSyntaxException {
/* 304 */     return setBlockItem(source, targetPos, targetSlot, getBlockItem(source, sourcePos, sourceSlot));
/*     */   }
/*     */   
/*     */   private static int blockToBlock(CommandSourceStack source, BlockPos sourcePos, int sourceSlot, BlockPos targetPos, int targetSlot, Holder<LootItemFunction> modifier) throws CommandSyntaxException {
/* 308 */     return setBlockItem(source, targetPos, targetSlot, applyModifier(source, modifier, getBlockItem(source, sourcePos, sourceSlot)));
/*     */   }
/*     */   
/*     */   private static int entityToBlock(CommandSourceStack source, Entity sourceEntity, int sourceSlot, BlockPos targetPos, int targetSlot) throws CommandSyntaxException {
/* 312 */     return setBlockItem(source, targetPos, targetSlot, getItemInSlot((SlotProvider)sourceEntity, sourceSlot));
/*     */   }
/*     */   
/*     */   private static int entityToBlock(CommandSourceStack source, Entity sourceEntity, int sourceSlot, BlockPos targetPos, int targetSlot, Holder<LootItemFunction> modifier) throws CommandSyntaxException {
/* 316 */     return setBlockItem(source, targetPos, targetSlot, applyModifier(source, modifier, getItemInSlot((SlotProvider)sourceEntity, sourceSlot)));
/*     */   }
/*     */   
/*     */   private static int entityToEntities(CommandSourceStack source, Entity sourceEntity, int sourceSlot, Collection<? extends Entity> targetEntities, int targetSlot) throws CommandSyntaxException {
/* 320 */     return setEntityItem(source, targetEntities, targetSlot, getItemInSlot((SlotProvider)sourceEntity, sourceSlot));
/*     */   }
/*     */   
/*     */   private static int entityToEntities(CommandSourceStack source, Entity sourceEntity, int sourceSlot, Collection<? extends Entity> targetEntities, int targetSlot, Holder<LootItemFunction> modifier) throws CommandSyntaxException {
/* 324 */     return setEntityItem(source, targetEntities, targetSlot, applyModifier(source, modifier, getItemInSlot((SlotProvider)sourceEntity, sourceSlot)));
/*     */   }
/*     */   
/*     */   private static ItemStack applyModifier(CommandSourceStack source, Holder<LootItemFunction> modifier, ItemStack item) {
/* 328 */     ServerLevel level = source.getLevel();
/* 333 */     LootParams lootParams = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, source.getPosition()).withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity()).create(LootContextParamSets.COMMAND);
/* 334 */     LootContext context = (new LootContext.Builder(lootParams)).create(Optional.empty());
/* 335 */     context.pushVisitedElement(LootContext.createVisitedEntry((LootItemFunction)modifier.value()));
/* 337 */     ItemStack newItem = (ItemStack)((LootItemFunction)modifier.value()).apply(item, context);
/* 338 */     newItem.limitSize(newItem.getMaxStackSize());
/* 339 */     return newItem;
/*     */   }
/*     */   
/*     */   private static ItemStack getItemInSlot(SlotProvider slotProvider, int slot) throws CommandSyntaxException {
/* 343 */     SlotAccess slotAccess = slotProvider.getSlot(slot);
/* 344 */     if (slotAccess == null)
/* 345 */       throw ERROR_SOURCE_INAPPLICABLE_SLOT.create(Integer.valueOf(slot)); 
/* 347 */     return slotAccess.get().copy();
/*     */   }
/*     */   
/*     */   private static ItemStack getBlockItem(CommandSourceStack source, BlockPos pos, int slot) throws CommandSyntaxException {
/* 351 */     Container container = getContainer(source, pos, ERROR_SOURCE_NOT_A_CONTAINER);
/* 352 */     return getItemInSlot((SlotProvider)container, slot);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\ItemCommands.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */