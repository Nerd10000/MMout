/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.CompoundTagArgument;
/*    */ import net.minecraft.commands.arguments.ResourceArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*    */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntitySpawnRequest;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SummonCommand {
/* 36 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.summon.failed"));
/*    */   
/* 37 */   private static final SimpleCommandExceptionType ERROR_FAILED_PEACEFUL = new SimpleCommandExceptionType((Message)Component.translatable("commands.summon.failed.peaceful"));
/*    */   
/* 38 */   private static final SimpleCommandExceptionType ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType((Message)Component.translatable("commands.summon.failed.uuid"));
/*    */   
/* 39 */   private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType((Message)Component.translatable("commands.summon.invalidPosition"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/* 42 */     dispatcher.register(
/* 43 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("summon")
/* 44 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 45 */         .then((
/* 46 */           (RequiredArgumentBuilder)Commands.argument("entity", (ArgumentType)ResourceArgument.resource(context, Registries.ENTITY_TYPE))
/* 47 */           .suggests(SuggestionProviders.cast(SuggestionProviders.SUMMONABLE_ENTITIES))
/* 48 */           .executes(c -> spawnEntity((CommandSourceStack)c.getSource(), ResourceArgument.getSummonableEntityType(c, "entity"), ((CommandSourceStack)c.getSource()).getPosition(), new CompoundTag(), true)))
/* 49 */           .then((
/* 50 */             (RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)Vec3Argument.vec3())
/* 51 */             .executes(c -> spawnEntity((CommandSourceStack)c.getSource(), ResourceArgument.getSummonableEntityType(c, "entity"), Vec3Argument.getVec3(c, "pos"), new CompoundTag(), true)))
/* 52 */             .then(
/* 53 */               Commands.argument("nbt", (ArgumentType)CompoundTagArgument.compoundTag())
/* 54 */               .executes(c -> spawnEntity((CommandSourceStack)c.getSource(), ResourceArgument.getSummonableEntityType(c, "entity"), Vec3Argument.getVec3(c, "pos"), CompoundTagArgument.getCompoundTag(c, "nbt"), false))))));
/*    */   }
/*    */   
/*    */   public static Entity createEntity(CommandSourceStack source, Holder.Reference<EntityType<?>> type, Vec3 pos, CompoundTag nbt, boolean finalize) throws CommandSyntaxException {
/* 62 */     BlockPos blockPos = BlockPos.containing((Position)pos);
/* 63 */     if (!Level.isInSpawnableBounds(blockPos))
/* 64 */       throw INVALID_POSITION.create(); 
/* 67 */     if (source.getLevel().getDifficulty() == Difficulty.PEACEFUL && !((EntityType)type.value()).isAllowedInPeaceful())
/* 68 */       throw ERROR_FAILED_PEACEFUL.create(); 
/* 71 */     CompoundTag entityTag = nbt.copy();
/* 72 */     entityTag.putString("id", type.key().identifier().toString());
/* 74 */     ServerLevel level = source.getLevel();
/* 75 */     Entity entity = EntityType.loadEntityRecursive(entityTag, (Level)level, new EntitySpawnRequest(EntitySpawnReason.COMMAND, false), e -> {
/*    */           e.snapTo(pos.x, pos.y, pos.z, e.getYRot(), e.getXRot());
/*    */           return e;
/*    */         });
/* 79 */     if (entity == null)
/* 80 */       throw ERROR_FAILED.create(); 
/* 83 */     if (finalize && entity instanceof Mob) {
/* 83 */       Mob mob = (Mob)entity;
/* 84 */       mob.finalizeSpawn((ServerLevelAccessor)source.getLevel(), source.getLevel().getCurrentDifficultyAt(entity.blockPosition()), EntitySpawnReason.COMMAND, null);
/*    */     } 
/* 87 */     if (!level.tryAddFreshEntityWithPassengers(entity))
/* 88 */       throw ERROR_DUPLICATE_UUID.create(); 
/* 90 */     return entity;
/*    */   }
/*    */   
/*    */   private static int spawnEntity(CommandSourceStack source, Holder.Reference<EntityType<?>> type, Vec3 pos, CompoundTag nbt, boolean finalize) throws CommandSyntaxException {
/* 94 */     Entity entity = createEntity(source, type, pos, nbt, finalize);
/* 96 */     source.sendSuccess(() -> Component.translatable("commands.summon.success", new Object[] { entity.getDisplayName() }), true);
/* 97 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\SummonCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */