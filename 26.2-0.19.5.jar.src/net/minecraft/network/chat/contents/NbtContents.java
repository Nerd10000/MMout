/*     */ package net.minecraft.network.chat.contents;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.TextComponentTagVisitor;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentContents;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.ResolutionContext;
/*     */ import net.minecraft.network.chat.contents.data.DataSource;
/*     */ import net.minecraft.network.chat.contents.data.DataSources;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.util.CompilableString;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class NbtContents extends Record implements ComponentContents {
/*     */   private final CompilableString<NbtPathArgument.NbtPath> nbtPath;
/*     */   
/*     */   private final boolean interpreting;
/*     */   
/*     */   private final boolean plain;
/*     */   
/*     */   private final Optional<Component> separator;
/*     */   
/*     */   private final DataSource dataSource;
/*     */   
/*     */   public NbtContents(CompilableString<NbtPathArgument.NbtPath> nbtPath, boolean interpreting, boolean plain, Optional<Component> separator, DataSource dataSource) {
/*  31 */     this.nbtPath = nbtPath;
/*  31 */     this.interpreting = interpreting;
/*  31 */     this.plain = plain;
/*  31 */     this.separator = separator;
/*  31 */     this.dataSource = dataSource;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/contents/NbtContents;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #31	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/NbtContents;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/NbtContents;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #31	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/NbtContents;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/NbtContents;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #31	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/NbtContents;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public CompilableString<NbtPathArgument.NbtPath> nbtPath() {
/*  31 */     return this.nbtPath;
/*     */   }
/*     */   
/*     */   public boolean interpreting() {
/*  31 */     return this.interpreting;
/*     */   }
/*     */   
/*     */   public boolean plain() {
/*  31 */     return this.plain;
/*     */   }
/*     */   
/*     */   public Optional<Component> separator() {
/*  31 */     return this.separator;
/*     */   }
/*     */   
/*     */   public DataSource dataSource() {
/*  31 */     return this.dataSource;
/*     */   }
/*     */   
/*  38 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  40 */   public static final Codec<CompilableString<NbtPathArgument.NbtPath>> NBT_PATH_CODEC = CompilableString.codec((Function)new CompilableString.CommandParserHelper<NbtPathArgument.NbtPath>() {
/*     */         protected NbtPathArgument.NbtPath parse(StringReader reader) throws CommandSyntaxException {
/*  43 */           return (new NbtPathArgument()).parse(reader);
/*     */         }
/*     */         
/*     */         protected String errorMessage(String original, CommandSyntaxException exception) {
/*  48 */           return "Invalid NBT path: " + original + ": " + exception.getMessage();
/*     */         }
/*     */       });
/*     */   
/*     */   public static final MapCodec<NbtContents> MAP_CODEC;
/*     */   
/*     */   static {
/*  58 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)NBT_PATH_CODEC.fieldOf("nbt").forGetter(NbtContents::nbtPath), (App)Codec.BOOL.lenientOptionalFieldOf("interpret", Boolean.valueOf(false)).forGetter(NbtContents::interpreting), (App)Codec.BOOL.lenientOptionalFieldOf("plain", Boolean.valueOf(false)).forGetter(NbtContents::plain), (App)ComponentSerialization.CODEC.lenientOptionalFieldOf("separator").forGetter(NbtContents::separator), (App)DataSources.CODEC.forGetter(NbtContents::dataSource)).apply((Applicative)i, NbtContents::new)).validate(v -> 
/*  59 */         (v.interpreting && v.plain) ? DataResult.error(()) : DataResult.success(v));
/*     */   }
/*     */   
/*     */   public MutableComponent resolve(ResolutionContext context, int recursionDepth) throws CommandSyntaxException {
/*  67 */     CommandSourceStack source = context.source();
/*  68 */     if (source == null)
/*  69 */       return Component.empty(); 
/*  73 */     Stream<Tag> elements = this.dataSource.getData(source).flatMap(t -> {
/*     */           try {
/*     */             return ((NbtPathArgument.NbtPath)this.nbtPath.compiled()).get((Tag)t).stream();
/*  76 */           } catch (CommandSyntaxException ignored) {
/*     */             return Stream.empty();
/*     */           } 
/*     */         });
/*  81 */     Component resolvedSeparator = (Component)DataFixUtils.orElse(ComponentUtils.resolve(context, this.separator, recursionDepth), ComponentUtils.DEFAULT_NO_STYLE_SEPARATOR);
/*  82 */     if (this.interpreting) {
/*  83 */       RegistryOps<Tag> registryOps = source.registryAccess().createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/*  84 */       return elements.<MutableComponent>flatMap(tag -> {
/*     */             try {
/*     */               Component component = (Component)ComponentSerialization.CODEC.parse((DynamicOps)registryOps, tag).getOrThrow();
/*     */               return Stream.of(ComponentUtils.resolve(context, component, recursionDepth));
/*  88 */             } catch (Exception e) {
/*     */               LOGGER.warn("Failed to parse component: {}", tag, e);
/*     */               return Stream.of(new MutableComponent[0]);
/*     */             } 
/*  93 */           }).reduce((left, right) -> left.append(resolvedSeparator).append((Component)right))
/*  94 */         .orElseGet(Component::empty);
/*     */     } 
/*  96 */     return elements.<MutableComponent>map(tag -> {
/*     */           TextComponentTagVisitor visitor = new TextComponentTagVisitor("", this.plain ? TextComponentTagVisitor.PlainStyling.INSTANCE : TextComponentTagVisitor.RichStyling.INSTANCE);
/*     */           return (MutableComponent)visitor.visit(tag);
/*  99 */         }).reduce((left, right) -> left.append(resolvedSeparator).append((Component)right)).orElseGet(Component::empty);
/*     */   }
/*     */   
/*     */   public MapCodec<NbtContents> codec() {
/* 105 */     return MAP_CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\NbtContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */