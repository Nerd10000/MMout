/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function11;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ 
/*    */ public final class TestData<EnvironmentType> extends Record {
/*    */   private final EnvironmentType environment;
/*    */   
/*    */   private final Identifier structure;
/*    */   
/*    */   private final int maxTicks;
/*    */   
/*    */   private final int setupTicks;
/*    */   
/*    */   private final boolean required;
/*    */   
/*    */   private final Rotation rotation;
/*    */   
/*    */   private final boolean manualOnly;
/*    */   
/*    */   private final int maxAttempts;
/*    */   
/*    */   private final int requiredSuccesses;
/*    */   
/*    */   private final boolean skyAccess;
/*    */   
/*    */   private final int padding;
/*    */   
/*    */   public static final MapCodec<TestData<Holder<TestEnvironmentDefinition<?>>>> CODEC;
/*    */   
/*    */   public TestData(EnvironmentType environment, Identifier structure, int maxTicks, int setupTicks, boolean required, Rotation rotation, boolean manualOnly, int maxAttempts, int requiredSuccesses, boolean skyAccess, int padding) {
/* 13 */     this.environment = environment;
/* 13 */     this.structure = structure;
/* 13 */     this.maxTicks = maxTicks;
/* 13 */     this.setupTicks = setupTicks;
/* 13 */     this.required = required;
/* 13 */     this.rotation = rotation;
/* 13 */     this.manualOnly = manualOnly;
/* 13 */     this.maxAttempts = maxAttempts;
/* 13 */     this.requiredSuccesses = requiredSuccesses;
/* 13 */     this.skyAccess = skyAccess;
/* 13 */     this.padding = padding;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestData<TEnvironmentType;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestData<TEnvironmentType;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestData<TEnvironmentType;>;
/*    */   }
/*    */   
/*    */   public EnvironmentType environment() {
/* 13 */     return this.environment;
/*    */   }
/*    */   
/*    */   public Identifier structure() {
/* 13 */     return this.structure;
/*    */   }
/*    */   
/*    */   public int maxTicks() {
/* 13 */     return this.maxTicks;
/*    */   }
/*    */   
/*    */   public int setupTicks() {
/* 13 */     return this.setupTicks;
/*    */   }
/*    */   
/*    */   public boolean required() {
/* 13 */     return this.required;
/*    */   }
/*    */   
/*    */   public Rotation rotation() {
/* 13 */     return this.rotation;
/*    */   }
/*    */   
/*    */   public boolean manualOnly() {
/* 13 */     return this.manualOnly;
/*    */   }
/*    */   
/*    */   public int maxAttempts() {
/* 13 */     return this.maxAttempts;
/*    */   }
/*    */   
/*    */   public int requiredSuccesses() {
/* 13 */     return this.requiredSuccesses;
/*    */   }
/*    */   
/*    */   public boolean skyAccess() {
/* 13 */     return this.skyAccess;
/*    */   }
/*    */   
/*    */   public int padding() {
/* 13 */     return this.padding;
/*    */   }
/*    */   
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TestEnvironmentDefinition.CODEC.fieldOf("environment").forGetter(TestData::environment), (App)Identifier.CODEC.fieldOf("structure").forGetter(TestData::structure), (App)ExtraCodecs.POSITIVE_INT.fieldOf("max_ticks").forGetter(TestData::maxTicks), (App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("setup_ticks", Integer.valueOf(0)).forGetter(TestData::setupTicks), (App)Codec.BOOL.optionalFieldOf("required", Boolean.valueOf(true)).forGetter(TestData::required), (App)Rotation.CODEC.optionalFieldOf("rotation", Rotation.NONE).forGetter(TestData::rotation), (App)Codec.BOOL.optionalFieldOf("manual_only", Boolean.valueOf(false)).forGetter(TestData::manualOnly), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("max_attempts", Integer.valueOf(1)).forGetter(TestData::maxAttempts), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("required_successes", Integer.valueOf(1)).forGetter(TestData::requiredSuccesses), (App)Codec.BOOL.optionalFieldOf("sky_access", Boolean.valueOf(false)).forGetter(TestData::skyAccess), (App)ExtraCodecs.intRange(0, 128).optionalFieldOf("padding", Integer.valueOf(0)).forGetter(TestData::padding)).apply((Applicative)i, TestData::new));
/*    */   }
/*    */   
/*    */   public TestData(EnvironmentType environment, Identifier structure, int maxTicks, int setupTicks, boolean required, Rotation rotation) {
/* 41 */     this(environment, structure, maxTicks, setupTicks, required, rotation, false, 1, 1, false, 0);
/*    */   }
/*    */   
/*    */   public TestData(EnvironmentType environment, Identifier structure, int maxTicks, int setupTicks, boolean required) {
/* 45 */     this(environment, structure, maxTicks, setupTicks, required, Rotation.NONE);
/*    */   }
/*    */   
/*    */   public <T> TestData<T> map(Function<EnvironmentType, T> mapper) {
/* 49 */     return new TestData((EnvironmentType)mapper.apply(this.environment), this.structure, this.maxTicks, this.setupTicks, this.required, this.rotation, this.manualOnly, this.maxAttempts, this.requiredSuccesses, this.skyAccess, this.padding);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */