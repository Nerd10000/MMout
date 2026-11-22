/*     */ package net.minecraft.world.attribute.modifier;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.LerpFunction;
/*     */ 
/*     */ public interface AttributeModifier<Subject, Argument> {
/*  11 */   public static final Map<OperationId, AttributeModifier<Boolean, ?>> BOOLEAN_LIBRARY = Map.of(OperationId.AND, BooleanModifier.AND, OperationId.NAND, BooleanModifier.NAND, OperationId.OR, BooleanModifier.OR, OperationId.NOR, BooleanModifier.NOR, OperationId.XOR, BooleanModifier.XOR, OperationId.XNOR, BooleanModifier.XNOR);
/*     */   
/*  19 */   public static final Map<OperationId, AttributeModifier<Float, ?>> FLOAT_LIBRARY = Map.of(OperationId.ALPHA_BLEND, FloatModifier.ALPHA_BLEND, OperationId.ADD, FloatModifier.ADD, OperationId.SUBTRACT, FloatModifier.SUBTRACT, OperationId.MULTIPLY, FloatModifier.MULTIPLY, OperationId.MINIMUM, FloatModifier.MINIMUM, OperationId.MAXIMUM, FloatModifier.MAXIMUM);
/*     */   
/*  27 */   public static final Map<OperationId, AttributeModifier<Integer, ?>> RGB_COLOR_LIBRARY = Map.of(OperationId.ALPHA_BLEND, ColorModifier.ALPHA_BLEND, OperationId.ADD, ColorModifier.ADD, OperationId.SUBTRACT, ColorModifier.SUBTRACT, OperationId.MULTIPLY, ColorModifier.MULTIPLY_RGB, OperationId.BLEND_TO_GRAY, ColorModifier.BLEND_TO_GRAY);
/*     */   
/*  34 */   public static final Map<OperationId, AttributeModifier<Integer, ?>> ARGB_COLOR_LIBRARY = Map.of(OperationId.ALPHA_BLEND, ColorModifier.ALPHA_BLEND, OperationId.ADD, ColorModifier.ADD, OperationId.SUBTRACT, ColorModifier.SUBTRACT, OperationId.MULTIPLY, ColorModifier.MULTIPLY_ARGB, OperationId.BLEND_TO_GRAY, ColorModifier.BLEND_TO_GRAY);
/*     */   
/*  41 */   public static final Map<OperationId, AttributeModifier<Integer, ?>> INTEGER_LIBRARY = Map.of(OperationId.ADD, IntegerModifier.ADD, OperationId.SUBTRACT, IntegerModifier.SUBTRACT, OperationId.MULTIPLY, IntegerModifier.MULTIPLY, OperationId.MINIMUM, IntegerModifier.MINIMUM, OperationId.MAXIMUM, IntegerModifier.MAXIMUM);
/*     */   
/*     */   static <Value> AttributeModifier<Value, Value> override() {
/*  51 */     return (AttributeModifier)OverrideModifier.INSTANCE;
/*     */   }
/*     */   
/*     */   Subject apply(Subject paramSubject, Argument paramArgument);
/*     */   
/*     */   Codec<Argument> argumentCodec(EnvironmentAttribute<Subject> paramEnvironmentAttribute);
/*     */   
/*     */   LerpFunction<Argument> argumentKeyframeLerp(EnvironmentAttribute<Subject> paramEnvironmentAttribute);
/*     */   
/*     */   public static final class OverrideModifier<Value> extends Record implements AttributeModifier<Value, Value> {
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier<TValue;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier<TValue;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/world/attribute/modifier/AttributeModifier$OverrideModifier<TValue;>;
/*     */     }
/*     */     
/*  61 */     private static final OverrideModifier<?> INSTANCE = new OverrideModifier();
/*     */     
/*     */     public Value apply(Value subject, Value argument) {
/*  65 */       return argument;
/*     */     }
/*     */     
/*     */     public Codec<Value> argumentCodec(EnvironmentAttribute<Value> attribute) {
/*  70 */       return attribute.valueCodec();
/*     */     }
/*     */     
/*     */     public LerpFunction<Value> argumentKeyframeLerp(EnvironmentAttribute<Value> attribute) {
/*  75 */       return attribute.type().keyframeLerp();
/*     */     }
/*     */   }
/*     */   
/*     */   public enum OperationId implements StringRepresentable {
/*  80 */     OVERRIDE("override"),
/*  81 */     ALPHA_BLEND("alpha_blend"),
/*  82 */     ADD("add"),
/*  83 */     SUBTRACT("subtract"),
/*  84 */     MULTIPLY("multiply"),
/*  85 */     BLEND_TO_GRAY("blend_to_gray"),
/*  86 */     MINIMUM("minimum"),
/*  87 */     MAXIMUM("maximum"),
/*  88 */     AND("and"),
/*  89 */     NAND("nand"),
/*  90 */     OR("or"),
/*  91 */     NOR("nor"),
/*  92 */     XOR("xor"),
/*  93 */     XNOR("xnor");
/*     */     
/*  96 */     public static final Codec<OperationId> CODEC = (Codec<OperationId>)StringRepresentable.fromEnum(OperationId::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     OperationId(String name) {
/* 101 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 106 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\modifier\AttributeModifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */