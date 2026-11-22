/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import org.apache.commons.lang3.function.TriFunction;
/*     */ 
/*     */ public final class ColorCollection<T> extends Record {
/*     */   private final T white;
/*     */   
/*     */   private final T orange;
/*     */   
/*     */   private final T magenta;
/*     */   
/*     */   private final T lightBlue;
/*     */   
/*     */   private final T yellow;
/*     */   
/*     */   private final T lime;
/*     */   
/*     */   private final T pink;
/*     */   
/*     */   private final T gray;
/*     */   
/*     */   private final T lightGray;
/*     */   
/*     */   private final T cyan;
/*     */   
/*     */   private final T purple;
/*     */   
/*     */   private final T blue;
/*     */   
/*     */   private final T brown;
/*     */   
/*     */   private final T green;
/*     */   
/*     */   private final T red;
/*     */   
/*     */   private final T black;
/*     */   
/*     */   public ColorCollection(T white, T orange, T magenta, T lightBlue, T yellow, T lime, T pink, T gray, T lightGray, T cyan, T purple, T blue, T brown, T green, T red, T black) {
/*  15 */     this.white = white;
/*  15 */     this.orange = orange;
/*  15 */     this.magenta = magenta;
/*  15 */     this.lightBlue = lightBlue;
/*  15 */     this.yellow = yellow;
/*  15 */     this.lime = lime;
/*  15 */     this.pink = pink;
/*  15 */     this.gray = gray;
/*  15 */     this.lightGray = lightGray;
/*  15 */     this.cyan = cyan;
/*  15 */     this.purple = purple;
/*  15 */     this.blue = blue;
/*  15 */     this.brown = brown;
/*  15 */     this.green = green;
/*  15 */     this.red = red;
/*  15 */     this.black = black;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/ColorCollection;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/ColorCollection;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/ColorCollection<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/ColorCollection;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/ColorCollection;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/ColorCollection<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/ColorCollection;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/ColorCollection;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/ColorCollection<TT;>;
/*     */   }
/*     */   
/*     */   public T white() {
/*  15 */     return this.white;
/*     */   }
/*     */   
/*     */   public T orange() {
/*  15 */     return this.orange;
/*     */   }
/*     */   
/*     */   public T magenta() {
/*  15 */     return this.magenta;
/*     */   }
/*     */   
/*     */   public T lightBlue() {
/*  15 */     return this.lightBlue;
/*     */   }
/*     */   
/*     */   public T yellow() {
/*  15 */     return this.yellow;
/*     */   }
/*     */   
/*     */   public T lime() {
/*  15 */     return this.lime;
/*     */   }
/*     */   
/*     */   public T pink() {
/*  15 */     return this.pink;
/*     */   }
/*     */   
/*     */   public T gray() {
/*  15 */     return this.gray;
/*     */   }
/*     */   
/*     */   public T lightGray() {
/*  15 */     return this.lightGray;
/*     */   }
/*     */   
/*     */   public T cyan() {
/*  15 */     return this.cyan;
/*     */   }
/*     */   
/*     */   public T purple() {
/*  15 */     return this.purple;
/*     */   }
/*     */   
/*     */   public T blue() {
/*  15 */     return this.blue;
/*     */   }
/*     */   
/*     */   public T brown() {
/*  15 */     return this.brown;
/*     */   }
/*     */   
/*     */   public T green() {
/*  15 */     return this.green;
/*     */   }
/*     */   
/*     */   public T red() {
/*  15 */     return this.red;
/*     */   }
/*     */   
/*     */   public T black() {
/*  15 */     return this.black;
/*     */   }
/*     */   
/*     */   public static <T> ColorCollection<T> create(T value) {
/*  34 */     return new ColorCollection<>(value, value, value, value, value, value, value, value, value, value, value, value, value, value, value, value);
/*     */   }
/*     */   
/*  54 */   public static final ColorCollection<DyeColor> VALUES = new ColorCollection((T)DyeColor.WHITE, (T)DyeColor.ORANGE, (T)DyeColor.MAGENTA, (T)DyeColor.LIGHT_BLUE, (T)DyeColor.YELLOW, (T)DyeColor.LIME, (T)DyeColor.PINK, (T)DyeColor.GRAY, (T)DyeColor.LIGHT_GRAY, (T)DyeColor.CYAN, (T)DyeColor.PURPLE, (T)DyeColor.BLUE, (T)DyeColor.BROWN, (T)DyeColor.GREEN, (T)DyeColor.RED, (T)DyeColor.BLACK);
/*     */   
/*  73 */   public static final ColorCollection<String> NAMES = VALUES.map(DyeColor::getName);
/*     */   
/*     */   public static <B extends Block, Id> ColorCollection<Block> registerBlocks(ColorCollection<Id> ids, TriFunction<Id, Function<BlockBehaviour.Properties, Block>, BlockBehaviour.Properties, Block> register, BiFunction<DyeColor, BlockBehaviour.Properties, B> colorBlockFactory, Function<DyeColor, BlockBehaviour.Properties> propertiesSupplier) {
/*  81 */     return zipMap(VALUES, ids, (color, id) -> (Block)register.apply(id, (), propertiesSupplier.apply(color)));
/*     */   }
/*     */   
/*     */   public static <Id> ColorCollection<Item> registerBlockItems(ColorCollection<Id> ids, ColorCollection<Block> blocks, TriFunction<Id, Block, DyeColor, Item> itemFactory) {
/*  85 */     return zipMap(VALUES, ids, (color, id) -> (Item)itemFactory.apply(id, blocks.pick(color), color));
/*     */   }
/*     */   
/*     */   public static <Id> ColorCollection<Item> registerItems(ColorCollection<Id> ids, BiFunction<Id, DyeColor, Item> itemFactory) {
/*  89 */     return zipMap(VALUES, ids, (color, id) -> (Item)itemFactory.apply(id, color));
/*     */   }
/*     */   
/*     */   public static ColorCollection<String> prefixWithColor(ColorCollection<String> ids) {
/*  93 */     return zipMap(NAMES, ids, (color, id) -> color + "_" + color);
/*     */   }
/*     */   
/*     */   public List<T> asList() {
/*  97 */     ImmutableList.Builder<T> builder = ImmutableList.builderWithExpectedSize(16);
/*  98 */     Objects.requireNonNull(builder);
/*  98 */     forEach(builder::add);
/*  99 */     return (List<T>)builder.build();
/*     */   }
/*     */   
/*     */   public void forEach(Consumer<T> consumer) {
/* 103 */     consumer.accept(this.white);
/* 104 */     consumer.accept(this.orange);
/* 105 */     consumer.accept(this.magenta);
/* 106 */     consumer.accept(this.lightBlue);
/* 107 */     consumer.accept(this.yellow);
/* 108 */     consumer.accept(this.lime);
/* 109 */     consumer.accept(this.pink);
/* 110 */     consumer.accept(this.gray);
/* 111 */     consumer.accept(this.lightGray);
/* 112 */     consumer.accept(this.cyan);
/* 113 */     consumer.accept(this.purple);
/* 114 */     consumer.accept(this.blue);
/* 115 */     consumer.accept(this.brown);
/* 116 */     consumer.accept(this.green);
/* 117 */     consumer.accept(this.red);
/* 118 */     consumer.accept(this.black);
/*     */   }
/*     */   
/*     */   public T pick(DyeColor dyeColor) {
/* 122 */     switch (dyeColor) {
/*     */       default:
/* 122 */         throw new MatchException(null, null);
/*     */       case WHITE:
/*     */       
/*     */       case ORANGE:
/*     */       
/*     */       case MAGENTA:
/*     */       
/*     */       case LIGHT_BLUE:
/*     */       
/*     */       case YELLOW:
/*     */       
/*     */       case LIME:
/*     */       
/*     */       case PINK:
/*     */       
/*     */       case GRAY:
/*     */       
/*     */       case LIGHT_GRAY:
/*     */       
/*     */       case CYAN:
/*     */       
/*     */       case PURPLE:
/*     */       
/*     */       case BLUE:
/*     */       
/*     */       case BROWN:
/*     */       
/*     */       case GREEN:
/*     */       
/*     */       case RED:
/*     */       
/*     */       case BLACK:
/*     */         break;
/*     */     } 
/* 122 */     return 
/*     */       
/* 138 */       this.black;
/*     */   }
/*     */   
/*     */   public <U> ColorCollection<U> map(Function<T, U> mapper) {
/* 143 */     return new ColorCollection((T)mapper
/* 144 */         .apply(this.white), (T)mapper
/* 145 */         .apply(this.orange), (T)mapper
/* 146 */         .apply(this.magenta), (T)mapper
/* 147 */         .apply(this.lightBlue), (T)mapper
/* 148 */         .apply(this.yellow), (T)mapper
/* 149 */         .apply(this.lime), (T)mapper
/* 150 */         .apply(this.pink), (T)mapper
/* 151 */         .apply(this.gray), (T)mapper
/* 152 */         .apply(this.lightGray), (T)mapper
/* 153 */         .apply(this.cyan), (T)mapper
/* 154 */         .apply(this.purple), (T)mapper
/* 155 */         .apply(this.blue), (T)mapper
/* 156 */         .apply(this.brown), (T)mapper
/* 157 */         .apply(this.green), (T)mapper
/* 158 */         .apply(this.red), (T)mapper
/* 159 */         .apply(this.black));
/*     */   }
/*     */   
/*     */   public static <T, U> void zipApply(ColorCollection<T> first, ColorCollection<U> second, BiConsumer<T, U> consumer) {
/* 164 */     consumer.accept(first.white(), second.white());
/* 165 */     consumer.accept(first.orange(), second.orange());
/* 166 */     consumer.accept(first.magenta(), second.magenta());
/* 167 */     consumer.accept(first.lightBlue(), second.lightBlue());
/* 168 */     consumer.accept(first.yellow(), second.yellow());
/* 169 */     consumer.accept(first.lime(), second.lime());
/* 170 */     consumer.accept(first.pink(), second.pink());
/* 171 */     consumer.accept(first.gray(), second.gray());
/* 172 */     consumer.accept(first.lightGray(), second.lightGray());
/* 173 */     consumer.accept(first.cyan(), second.cyan());
/* 174 */     consumer.accept(first.purple(), second.purple());
/* 175 */     consumer.accept(first.blue(), second.blue());
/* 176 */     consumer.accept(first.brown(), second.brown());
/* 177 */     consumer.accept(first.green(), second.green());
/* 178 */     consumer.accept(first.red(), second.red());
/* 179 */     consumer.accept(first.black(), second.black());
/*     */   }
/*     */   
/*     */   public static <T, U, R> ColorCollection<R> zipMap(ColorCollection<T> first, ColorCollection<U> second, BiFunction<T, U, R> operation) {
/* 183 */     return new ColorCollection<>(operation
/* 184 */         .apply(first.white(), second.white()), operation
/* 185 */         .apply(first.orange(), second.orange()), operation
/* 186 */         .apply(first.magenta(), second.magenta()), operation
/* 187 */         .apply(first.lightBlue(), second.lightBlue()), operation
/* 188 */         .apply(first.yellow(), second.yellow()), operation
/* 189 */         .apply(first.lime(), second.lime()), operation
/* 190 */         .apply(first.pink(), second.pink()), operation
/* 191 */         .apply(first.gray(), second.gray()), operation
/* 192 */         .apply(first.lightGray(), second.lightGray()), operation
/* 193 */         .apply(first.cyan(), second.cyan()), operation
/* 194 */         .apply(first.purple(), second.purple()), operation
/* 195 */         .apply(first.blue(), second.blue()), operation
/* 196 */         .apply(first.brown(), second.brown()), operation
/* 197 */         .apply(first.green(), second.green()), operation
/* 198 */         .apply(first.red(), second.red()), operation
/* 199 */         .apply(first.black(), second.black()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ColorCollection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */