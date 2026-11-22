/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.ResolutionContext;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SetNameFunction extends LootItemConditionalFunction {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final MapCodec<SetNameFunction> MAP_CODEC;
/*     */   
/*     */   private final Optional<Component> name;
/*     */   
/*     */   private final Optional<LootContext.EntityTarget> resolutionContext;
/*     */   
/*     */   private final Target target;
/*     */   
/*     */   static {
/*  34 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(()), (App)LootContext.EntityTarget.CODEC.optionalFieldOf("entity").forGetter(()), (App)Target.CODEC.optionalFieldOf("target", Target.CUSTOM_NAME).forGetter(()))).apply((Applicative)i, SetNameFunction::new));
/*     */   }
/*     */   
/*     */   private SetNameFunction(List<LootItemCondition> predicates, Optional<Component> name, Optional<LootContext.EntityTarget> resolutionContext, Target target) {
/*  45 */     super(predicates);
/*  46 */     this.name = name;
/*  47 */     this.resolutionContext = resolutionContext;
/*  48 */     this.target = target;
/*     */   }
/*     */   
/*     */   public MapCodec<SetNameFunction> codec() {
/*  53 */     return MAP_CODEC;
/*     */   }
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  58 */     return (Set<ContextKey<?>>)DataFixUtils.orElse(this.resolutionContext.map(target -> Set.of(target.contextParam())), Set.of());
/*     */   }
/*     */   
/*     */   public static UnaryOperator<Component> createResolver(LootContext context, LootContext.EntityTarget entityTarget) {
/*  62 */     if (entityTarget != null) {
/*  63 */       Entity entity = (Entity)context.getOptionalParameter(entityTarget.contextParam());
/*  64 */       if (entity != null) {
/*  67 */         CommandSourceStack commandSourceStack = entity.createCommandSourceStackForNameResolution(context.getLevel()).withPermission((PermissionSet)LevelBasedPermissionSet.GAMEMASTER);
/*  68 */         ResolutionContext resolutionContext = ResolutionContext.create(commandSourceStack);
/*  69 */         return line -> {
/*     */             try {
/*     */               return (Component)ComponentUtils.resolve(resolutionContext, line);
/*  72 */             } catch (CommandSyntaxException e) {
/*     */               LOGGER.warn("Failed to resolve text component", (Throwable)e);
/*     */               return line;
/*     */             } 
/*     */           };
/*     */       } 
/*     */     } 
/*  79 */     return line -> line;
/*     */   }
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  84 */     this.name.ifPresent(name -> itemStack.set(this.target.component(), createResolver(context, this.resolutionContext.orElse(null)).apply(name)));
/*  85 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> setName(Component value, Target target) {
/*  89 */     return simpleBuilder(conditions -> new SetNameFunction(conditions, Optional.of(value), Optional.empty(), target));
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> setName(Component value, Target target, LootContext.EntityTarget resolutionContext) {
/*  93 */     return simpleBuilder(conditions -> new SetNameFunction(conditions, Optional.of(value), Optional.of(resolutionContext), target));
/*     */   }
/*     */   
/*     */   public enum Target implements StringRepresentable {
/*  97 */     CUSTOM_NAME("custom_name"),
/*  98 */     ITEM_NAME("item_name");
/*     */     
/* 101 */     public static final Codec<Target> CODEC = (Codec<Target>)StringRepresentable.fromEnum(Target::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Target(String name) {
/* 106 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 111 */       return this.name;
/*     */     }
/*     */     
/*     */     public DataComponentType<Component> component() {
/* 116 */       switch (ordinal()) {
/*     */         default:
/* 116 */           throw new MatchException(null, null);
/*     */         case 1:
/*     */         
/*     */         case 0:
/*     */           break;
/*     */       } 
/* 116 */       return 
/*     */         
/* 118 */         DataComponents.CUSTOM_NAME;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetNameFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */