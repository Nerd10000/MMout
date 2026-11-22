/*     */ package net.minecraft.network.chat.contents;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentContents;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.ResolutionContext;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public class TranslatableContents implements ComponentContents {
/*  31 */   public static final Object[] NO_ARGS = new Object[0];
/*     */   
/*  33 */   private static final Codec<Object> PRIMITIVE_ARG_CODEC = ExtraCodecs.JAVA.validate(TranslatableContents::filterAllowedArguments);
/*     */   
/*     */   private static final Codec<Object> ARG_CODEC;
/*     */   
/*     */   public static final MapCodec<TranslatableContents> MAP_CODEC;
/*     */   
/*     */   private static DataResult<Object> filterAllowedArguments(Object result) {
/*  36 */     if (!isAllowedPrimitiveArgument(result))
/*  37 */       return DataResult.error(() -> "This value needs to be parsed as component"); 
/*  39 */     return DataResult.success(result);
/*     */   }
/*     */   
/*     */   public static boolean isAllowedPrimitiveArgument(Object object) {
/*  43 */     return (object instanceof Number || object instanceof Boolean || object instanceof String);
/*     */   }
/*     */   
/*     */   static {
/*  47 */     ARG_CODEC = Codec.either(PRIMITIVE_ARG_CODEC, ComponentSerialization.CODEC).xmap(e -> e.map((), ()), o -> {
/*     */           Component c = (Component)o;
/*     */           return (o instanceof Component) ? Either.right(c) : Either.left(o);
/*     */         });
/*  54 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("translate").forGetter(()), (App)Codec.STRING.lenientOptionalFieldOf("fallback").forGetter(()), (App)ARG_CODEC.listOf().optionalFieldOf("with").forGetter(())).apply((Applicative)i, TranslatableContents::create));
/*     */   }
/*     */   
/*     */   private static Optional<List<Object>> adjustArgs(Object[] args) {
/*  61 */     return (args.length == 0) ? Optional.<List<Object>>empty() : Optional.<List<Object>>of(Arrays.asList(args));
/*     */   }
/*     */   
/*     */   private static Object[] adjustArgs(Optional<List<Object>> args) {
/*  65 */     return args.<Object[]>map(a -> a.isEmpty() ? NO_ARGS : a.toArray()).orElse(NO_ARGS);
/*     */   }
/*     */   
/*     */   private static TranslatableContents create(String key, Optional<String> fallback, Optional<List<Object>> args) {
/*  69 */     return new TranslatableContents(key, fallback.orElse(null), adjustArgs(args));
/*     */   }
/*     */   
/*  72 */   private static final FormattedText TEXT_PERCENT = FormattedText.of("%");
/*     */   
/*  73 */   private static final FormattedText TEXT_NULL = FormattedText.of("null");
/*     */   
/*     */   private final String key;
/*     */   
/*     */   private final String fallback;
/*     */   
/*     */   private final Object[] args;
/*     */   
/*     */   private Language decomposedWith;
/*     */   
/*  80 */   private List<FormattedText> decomposedParts = (List<FormattedText>)ImmutableList.of();
/*     */   
/*  82 */   private static final Pattern FORMAT_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
/*     */   
/*     */   public TranslatableContents(String key, String fallback, Object[] args) {
/*  85 */     this.key = key;
/*  86 */     this.fallback = fallback;
/*  87 */     this.args = args;
/*     */   }
/*     */   
/*     */   public MapCodec<TranslatableContents> codec() {
/*  92 */     return MAP_CODEC;
/*     */   }
/*     */   
/*     */   private void decompose() {
/*  96 */     Language currentLanguage = Language.getInstance();
/*  97 */     if (currentLanguage == this.decomposedWith)
/*     */       return; 
/* 100 */     this.decomposedWith = currentLanguage;
/* 103 */     String format = (this.fallback != null) ? currentLanguage.getOrDefault(this.key, this.fallback) : currentLanguage.getOrDefault(this.key);
/*     */     try {
/* 105 */       ImmutableList.Builder<FormattedText> parts = ImmutableList.builder();
/* 106 */       Objects.requireNonNull(parts);
/* 106 */       decomposeTemplate(format, parts::add);
/* 107 */       this.decomposedParts = (List<FormattedText>)parts.build();
/* 108 */     } catch (TranslatableFormatException e) {
/* 109 */       this.decomposedParts = (List<FormattedText>)ImmutableList.of(FormattedText.of(format));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void decomposeTemplate(String template, Consumer<FormattedText> decomposedParts) {
/* 114 */     Matcher matcher = FORMAT_PATTERN.matcher(template);
/*     */     try {
/* 117 */       int replacementIndex = 0;
/* 118 */       int current = 0;
/* 120 */       while (matcher.find(current)) {
/* 121 */         int start = matcher.start();
/* 122 */         int end = matcher.end();
/* 124 */         if (start > current) {
/* 125 */           String prefix = template.substring(current, start);
/* 126 */           if (prefix.indexOf('%') != -1)
/* 127 */             throw new IllegalArgumentException(); 
/* 129 */           decomposedParts.accept(FormattedText.of(prefix));
/*     */         } 
/* 132 */         String formatType = matcher.group(2);
/* 133 */         String formatString = template.substring(start, end);
/* 136 */         if ("%".equals(formatType) && "%%".equals(formatString)) {
/* 137 */           decomposedParts.accept(TEXT_PERCENT);
/* 138 */         } else if ("s".equals(formatType)) {
/* 139 */           String possiblePositionIndex = matcher.group(1);
/* 140 */           int index = (possiblePositionIndex != null) ? (Integer.parseInt(possiblePositionIndex) - 1) : replacementIndex++;
/* 141 */           decomposedParts.accept(getArgument(index));
/*     */         } else {
/* 143 */           throw new TranslatableFormatException(this, "Unsupported format: '" + formatString + "'");
/*     */         } 
/* 146 */         current = end;
/*     */       } 
/* 149 */       if (current < template.length()) {
/* 150 */         String tail = template.substring(current);
/* 151 */         if (tail.indexOf('%') != -1)
/* 152 */           throw new IllegalArgumentException(); 
/* 154 */         decomposedParts.accept(FormattedText.of(tail));
/*     */       } 
/* 156 */     } catch (IllegalArgumentException e) {
/* 157 */       throw new TranslatableFormatException(this, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private FormattedText getArgument(int index) {
/* 162 */     if (index < 0 || index >= this.args.length)
/* 163 */       throw new TranslatableFormatException(this, index); 
/* 166 */     Object arg = this.args[index];
/* 168 */     if (arg instanceof Component) {
/* 168 */       Component componentArg = (Component)arg;
/* 169 */       return (FormattedText)componentArg;
/*     */     } 
/* 171 */     return (arg == null) ? TEXT_NULL : FormattedText.of(arg.toString());
/*     */   }
/*     */   
/*     */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) {
/* 177 */     decompose();
/* 179 */     for (FormattedText part : this.decomposedParts) {
/* 180 */       Optional<T> result = part.visit(output, currentStyle);
/* 181 */       if (result.isPresent())
/* 182 */         return result; 
/*     */     } 
/* 186 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 191 */     decompose();
/* 193 */     for (FormattedText part : this.decomposedParts) {
/* 194 */       Optional<T> result = part.visit(output);
/* 195 */       if (result.isPresent())
/* 196 */         return result; 
/*     */     } 
/* 200 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public MutableComponent resolve(ResolutionContext context, int recursionDepth) throws CommandSyntaxException {
/* 205 */     Object[] argsCopy = new Object[this.args.length];
/* 207 */     for (int i = 0; i < argsCopy.length; i++) {
/* 208 */       Object param = this.args[i];
/* 209 */       if (param instanceof Component) {
/* 209 */         Component component = (Component)param;
/* 210 */         argsCopy[i] = ComponentUtils.resolve(context, component, recursionDepth);
/*     */       } else {
/* 212 */         argsCopy[i] = param;
/*     */       } 
/*     */     } 
/* 215 */     return MutableComponent.create(new TranslatableContents(this.key, this.fallback, argsCopy));
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: if_acmpne -> 7
/*     */     //   5: iconst_1
/*     */     //   6: ireturn
/*     */     //   7: aload_1
/*     */     //   8: instanceof net/minecraft/network/chat/contents/TranslatableContents
/*     */     //   11: ifeq -> 65
/*     */     //   14: aload_1
/*     */     //   15: checkcast net/minecraft/network/chat/contents/TranslatableContents
/*     */     //   18: astore_2
/*     */     //   19: aload_0
/*     */     //   20: getfield key : Ljava/lang/String;
/*     */     //   23: aload_2
/*     */     //   24: getfield key : Ljava/lang/String;
/*     */     //   27: invokestatic equals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*     */     //   30: ifeq -> 65
/*     */     //   33: aload_0
/*     */     //   34: getfield fallback : Ljava/lang/String;
/*     */     //   37: aload_2
/*     */     //   38: getfield fallback : Ljava/lang/String;
/*     */     //   41: invokestatic equals : (Ljava/lang/Object;Ljava/lang/Object;)Z
/*     */     //   44: ifeq -> 65
/*     */     //   47: aload_0
/*     */     //   48: getfield args : [Ljava/lang/Object;
/*     */     //   51: aload_2
/*     */     //   52: getfield args : [Ljava/lang/Object;
/*     */     //   55: invokestatic equals : ([Ljava/lang/Object;[Ljava/lang/Object;)Z
/*     */     //   58: ifeq -> 65
/*     */     //   61: iconst_1
/*     */     //   62: goto -> 66
/*     */     //   65: iconst_0
/*     */     //   66: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #220	-> 0
/*     */     //   #221	-> 5
/*     */     //   #227	-> 7
/*     */     //   #224	-> 14
/*     */     //   #225	-> 27
/*     */     //   #226	-> 41
/*     */     //   #227	-> 55
/*     */     //   #224	-> 66
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   19	46	2	that	Lnet/minecraft/network/chat/contents/TranslatableContents;
/*     */     //   0	67	0	this	Lnet/minecraft/network/chat/contents/TranslatableContents;
/*     */     //   0	67	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 232 */     int result = Objects.hashCode(this.key);
/* 233 */     result = 31 * result + Objects.hashCode(this.fallback);
/* 234 */     result = 31 * result + Arrays.hashCode(this.args);
/* 235 */     return result;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 240 */     return "translation{key='" + this.key + "'" + (
/*     */       
/* 242 */       (this.fallback != null) ? (", fallback='" + this.fallback + "'") : "") + ", args=" + 
/* 243 */       Arrays.toString(this.args) + "}";
/*     */   }
/*     */   
/*     */   public String getKey() {
/* 248 */     return this.key;
/*     */   }
/*     */   
/*     */   public String getFallback() {
/* 252 */     return this.fallback;
/*     */   }
/*     */   
/*     */   public Object[] getArgs() {
/* 256 */     return this.args;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\TranslatableContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */