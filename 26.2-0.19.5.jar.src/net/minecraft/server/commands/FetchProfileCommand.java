/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.UuidArgument;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.contents.objects.ObjectInfo;
/*     */ import net.minecraft.network.chat.contents.objects.PlayerSprite;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Avatar;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ 
/*     */ public class FetchProfileCommand {
/*     */   private static final DynamicCommandExceptionType NO_PROFILE;
/*     */   
/*     */   static {
/*  39 */     NO_PROFILE = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.fetchprofile.no_profile", new Object[] { id }));
/*     */   }
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  42 */     dispatcher.register(
/*  43 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("fetchprofile")
/*  44 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  45 */         .then(
/*  46 */           Commands.literal("name")
/*  47 */           .then(
/*  48 */             Commands.argument("name", (ArgumentType)StringArgumentType.greedyString())
/*  49 */             .executes(c -> resolveName((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "name"))))))
/*     */         
/*  52 */         .then(
/*  53 */           Commands.literal("id")
/*  54 */           .then(
/*  55 */             Commands.argument("id", (ArgumentType)UuidArgument.uuid())
/*  56 */             .executes(c -> resolveId((CommandSourceStack)c.getSource(), UuidArgument.getUuid(c, "id"))))))
/*     */         
/*  59 */         .then(
/*  60 */           Commands.literal("entity")
/*  61 */           .then(
/*  62 */             Commands.argument("entity", (ArgumentType)EntityArgument.entity())
/*  63 */             .executes(c -> printForEntity((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "entity"))))));
/*     */   }
/*     */   
/*     */   private static void reportResolvedProfile(CommandSourceStack sender, GameProfile gameProfile, String messageId, Component argument) {
/*  70 */     reportResolvedProfile(sender, messageId, argument, ResolvableProfile.createResolved(gameProfile));
/*     */   }
/*     */   
/*     */   private static void reportResolvedProfile(CommandSourceStack sender, String messageId, Component argument, ResolvableProfile profile) {
/*  74 */     ResolvableProfile.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, profile)
/*  75 */       .ifSuccess(encodedProfile -> {
/*     */           String encodedProfileAsString = encodedProfile.toString();
/*     */           MutableComponent headComponent = Component.object((ObjectInfo)new PlayerSprite(profile, true));
/*     */           ComponentSerialization.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, headComponent).ifSuccess(()).ifError(());
/*  99 */         }).ifError(error -> sender.sendFailure((Component)Component.translatable("commands.fetchprofile.failed_to_serialize", new Object[] { error.message() })));
/*     */   }
/*     */   
/*     */   private static int resolveName(CommandSourceStack source, String name) {
/* 105 */     MinecraftServer server = source.getServer();
/* 106 */     ProfileResolver resolver = server.services().profileResolver();
/* 108 */     Util.nonCriticalIoPool().execute(() -> {
/*     */           MutableComponent mutableComponent = Component.literal(name);
/*     */           Optional<GameProfile> result = resolver.fetchByName(name);
/*     */           server.execute(());
/*     */         });
/* 118 */     return 1;
/*     */   }
/*     */   
/*     */   private static int resolveId(CommandSourceStack source, UUID id) {
/* 122 */     MinecraftServer server = source.getServer();
/* 123 */     ProfileResolver resolver = server.services().profileResolver();
/* 125 */     Util.nonCriticalIoPool().execute(() -> {
/*     */           Component idComponent = Component.translationArg(id);
/*     */           Optional<GameProfile> result = resolver.fetchById(id);
/*     */           server.execute(());
/*     */         });
/* 135 */     return 1;
/*     */   }
/*     */   
/*     */   private static int printForEntity(CommandSourceStack source, Entity entity) throws CommandSyntaxException {
/*     */     Avatar avatar;
/* 139 */     if (entity instanceof Avatar) {
/* 139 */       avatar = (Avatar)entity;
/*     */     } else {
/* 140 */       throw NO_PROFILE.create(entity.getDisplayName());
/*     */     } 
/* 142 */     printForAvatar(source, avatar);
/* 143 */     return 1;
/*     */   }
/*     */   
/*     */   public static void printForAvatar(CommandSourceStack source, Avatar avatar) {
/* 147 */     reportResolvedProfile(source, "commands.fetchprofile.entity.success", avatar.getDisplayName(), avatar.getProfile());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\FetchProfileCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */