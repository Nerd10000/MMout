/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ 
/*     */ public abstract class GameTestInstance {
/*     */   public static final Codec<GameTestInstance> DIRECT_CODEC;
/*     */   
/*     */   private final TestData<Holder<TestEnvironmentDefinition<?>>> info;
/*     */   
/*     */   public static MapCodec<? extends GameTestInstance> bootstrap(Registry<MapCodec<? extends GameTestInstance>> registry) {
/*  20 */     register(registry, "block_based", (MapCodec)BlockBasedTestInstance.CODEC);
/*  21 */     return register(registry, "function", (MapCodec)FunctionGameTestInstance.CODEC);
/*     */   }
/*     */   
/*     */   private static MapCodec<? extends GameTestInstance> register(Registry<MapCodec<? extends GameTestInstance>> registry, String name, MapCodec<? extends GameTestInstance> codec) {
/*  25 */     return (MapCodec<? extends GameTestInstance>)Registry.register(registry, ResourceKey.create(Registries.TEST_INSTANCE_TYPE, Identifier.withDefaultNamespace(name)), codec);
/*     */   }
/*     */   
/*     */   static {
/*  28 */     DIRECT_CODEC = BuiltInRegistries.TEST_INSTANCE_TYPE.byNameCodec().dispatch(GameTestInstance::codec, i -> i);
/*     */   }
/*     */   
/*     */   protected GameTestInstance(TestData<Holder<TestEnvironmentDefinition<?>>> info) {
/*  33 */     this.info = info;
/*     */   }
/*     */   
/*     */   public Holder<TestEnvironmentDefinition<?>> batch() {
/*  42 */     return this.info.environment();
/*     */   }
/*     */   
/*     */   public Identifier structure() {
/*  46 */     return this.info.structure();
/*     */   }
/*     */   
/*     */   public int maxTicks() {
/*  50 */     return this.info.maxTicks();
/*     */   }
/*     */   
/*     */   public int setupTicks() {
/*  54 */     return this.info.setupTicks();
/*     */   }
/*     */   
/*     */   public boolean required() {
/*  58 */     return this.info.required();
/*     */   }
/*     */   
/*     */   public boolean manualOnly() {
/*  62 */     return this.info.manualOnly();
/*     */   }
/*     */   
/*     */   public int maxAttempts() {
/*  66 */     return this.info.maxAttempts();
/*     */   }
/*     */   
/*     */   public int requiredSuccesses() {
/*  70 */     return this.info.requiredSuccesses();
/*     */   }
/*     */   
/*     */   public boolean skyAccess() {
/*  74 */     return this.info.skyAccess();
/*     */   }
/*     */   
/*     */   public Rotation rotation() {
/*  78 */     return this.info.rotation();
/*     */   }
/*     */   
/*     */   public int padding() {
/*  82 */     return this.info.padding();
/*     */   }
/*     */   
/*     */   protected TestData<Holder<TestEnvironmentDefinition<?>>> info() {
/*  86 */     return this.info;
/*     */   }
/*     */   
/*     */   public Component describe() {
/*  92 */     return (Component)describeType().append(describeInfo());
/*     */   }
/*     */   
/*     */   protected MutableComponent describeType() {
/*  96 */     return descriptionRow("test_instance.description.type", typeDescription());
/*     */   }
/*     */   
/*     */   protected Component describeInfo() {
/* 100 */     return (Component)descriptionRow("test_instance.description.structure", this.info.structure().toString())
/* 101 */       .append((Component)descriptionRow("test_instance.description.batch", ((Holder)this.info.environment()).getRegisteredName()));
/*     */   }
/*     */   
/*     */   protected MutableComponent descriptionRow(String translationKey, String value) {
/* 105 */     return descriptionRow(translationKey, Component.literal(value));
/*     */   }
/*     */   
/*     */   protected MutableComponent descriptionRow(String translationKey, MutableComponent value) {
/* 109 */     return Component.translatable(translationKey, new Object[] { value.withStyle(ChatFormatting.BLUE) }).append((Component)Component.literal("\n"));
/*     */   }
/*     */   
/*     */   public abstract void run(GameTestHelper paramGameTestHelper);
/*     */   
/*     */   public abstract MapCodec<? extends GameTestInstance> codec();
/*     */   
/*     */   protected abstract MutableComponent typeDescription();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */