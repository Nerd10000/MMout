/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ public class IntRange implements LootContextUser {
/*     */   private static final Codec<IntRange> RECORD_CODEC;
/*     */   
/*     */   public static final Codec<IntRange> CODEC;
/*     */   
/*     */   private final NumberProvider min;
/*     */   
/*     */   private final NumberProvider max;
/*     */   
/*     */   private final IntLimiter limiter;
/*     */   
/*     */   private final IntChecker predicate;
/*     */   
/*     */   static {
/*  18 */     RECORD_CODEC = RecordCodecBuilder.create(i -> i.group((App)NumberProviders.CODEC.optionalFieldOf("min").forGetter(()), (App)NumberProviders.CODEC.optionalFieldOf("max").forGetter(())).apply((Applicative)i, IntRange::new));
/*  23 */     CODEC = Codec.either((Codec)Codec.INT, RECORD_CODEC).xmap(e -> (IntRange)e.map(IntRange::exact, Function.identity()), range -> {
/*     */           OptionalInt exact = range.unpackExact();
/*     */           return exact.isPresent() ? Either.left(Integer.valueOf(exact.getAsInt())) : Either.right(range);
/*     */         });
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext context) {
/*  53 */     super.validate(context);
/*  55 */     if (this.min != null)
/*  56 */       Validatable.validate(context, "min", (Validatable)this.min); 
/*  58 */     if (this.max != null)
/*  59 */       Validatable.validate(context, "max", (Validatable)this.max); 
/*     */   }
/*     */   
/*     */   private IntRange(Optional<NumberProvider> min, Optional<NumberProvider> max) {
/*  64 */     this(min.orElse(null), max.orElse(null));
/*     */   }
/*     */   
/*     */   private IntRange(NumberProvider min, NumberProvider max) {
/*  68 */     this.min = min;
/*  69 */     this.max = max;
/*  71 */     if (min == null) {
/*  72 */       if (max == null) {
/*  73 */         this.limiter = ((context, value) -> value);
/*  74 */         this.predicate = ((context, value) -> true);
/*     */       } else {
/*  76 */         this.limiter = ((context, value) -> Math.min(max.getInt(context), value));
/*  77 */         this.predicate = ((context, value) -> (value <= max.getInt(context)));
/*     */       } 
/*  80 */     } else if (max == null) {
/*  81 */       this.limiter = ((context, value) -> Math.max(min.getInt(context), value));
/*  82 */       this.predicate = ((context, value) -> (value >= min.getInt(context)));
/*     */     } else {
/*  84 */       this.limiter = ((context, value) -> Mth.clamp(value, min.getInt(context), max.getInt(context)));
/*  85 */       this.predicate = ((context, value) -> (value >= min.getInt(context) && value <= max.getInt(context)));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static IntRange exact(int value) {
/*  91 */     ConstantValue c = ConstantValue.exactly(value);
/*  92 */     return new IntRange((Optional)Optional.of(c), (Optional)Optional.of(c));
/*     */   }
/*     */   
/*     */   public static IntRange range(int min, int max) {
/*  96 */     return new IntRange((Optional)Optional.of(ConstantValue.exactly(min)), (Optional)Optional.of(ConstantValue.exactly(max)));
/*     */   }
/*     */   
/*     */   public static IntRange lowerBound(int value) {
/* 100 */     return new IntRange((Optional)Optional.of(ConstantValue.exactly(value)), Optional.empty());
/*     */   }
/*     */   
/*     */   public static IntRange upperBound(int value) {
/* 104 */     return new IntRange(Optional.empty(), (Optional)Optional.of(ConstantValue.exactly(value)));
/*     */   }
/*     */   
/*     */   public int clamp(LootContext context, int value) {
/* 108 */     return this.limiter.apply(context, value);
/*     */   }
/*     */   
/*     */   public boolean test(LootContext context, int value) {
/* 112 */     return this.predicate.test(context, value);
/*     */   }
/*     */   
/*     */   private OptionalInt unpackExact() {
/* 116 */     if (Objects.equals(this.min, this.max)) {
/* 116 */       NumberProvider numberProvider = this.min;
/* 116 */       if (numberProvider instanceof ConstantValue) {
/* 116 */         ConstantValue constant = (ConstantValue)numberProvider;
/* 117 */         if (Math.floor(constant.value()) == constant.value())
/* 118 */           return OptionalInt.of((int)constant.value()); 
/*     */       } 
/*     */     } 
/* 121 */     return OptionalInt.empty();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface IntLimiter {
/*     */     int apply(LootContext param1LootContext, int param1Int);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface IntChecker {
/*     */     boolean test(LootContext param1LootContext, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\IntRange.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */