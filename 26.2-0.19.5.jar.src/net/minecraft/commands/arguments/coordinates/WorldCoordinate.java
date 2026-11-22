/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public final class WorldCoordinate extends Record {
/*    */   private final boolean relative;
/*    */   
/*    */   private final double value;
/*    */   
/*    */   private static final char PREFIX_RELATIVE = '~';
/*    */   
/*    */   public WorldCoordinate(boolean relative, double value) {
/*  8 */     this.relative = relative;
/*  8 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/coordinates/WorldCoordinate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/coordinates/WorldCoordinate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/coordinates/WorldCoordinate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/coordinates/WorldCoordinate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/coordinates/WorldCoordinate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/coordinates/WorldCoordinate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean relative() {
/*  8 */     return this.relative;
/*    */   }
/*    */   
/*    */   public double value() {
/*  8 */     return this.value;
/*    */   }
/*    */   
/* 11 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_DOUBLE = new SimpleCommandExceptionType((Message)Component.translatable("argument.pos.missing.double"));
/*    */   
/* 12 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_INT = new SimpleCommandExceptionType((Message)Component.translatable("argument.pos.missing.int"));
/*    */   
/*    */   public double get(double original) {
/* 15 */     if (this.relative)
/* 16 */       return this.value + original; 
/* 18 */     return this.value;
/*    */   }
/*    */   
/*    */   public static WorldCoordinate parseDouble(StringReader reader, boolean center) throws CommandSyntaxException {
/* 23 */     if (reader.canRead() && reader.peek() == '^')
/* 24 */       throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(reader); 
/* 27 */     if (!reader.canRead())
/* 28 */       throw ERROR_EXPECTED_DOUBLE.createWithContext(reader); 
/* 31 */     boolean relative = isRelative(reader);
/* 32 */     int start = reader.getCursor();
/* 33 */     double value = (reader.canRead() && reader.peek() != ' ') ? reader.readDouble() : 0.0D;
/* 34 */     String number = reader.getString().substring(start, reader.getCursor());
/* 36 */     if (relative && number.isEmpty())
/* 37 */       return new WorldCoordinate(true, 0.0D); 
/* 40 */     if (!number.contains(".") && !relative && center)
/* 41 */       value += 0.5D; 
/* 44 */     return new WorldCoordinate(relative, value);
/*    */   }
/*    */   
/*    */   public static WorldCoordinate parseInt(StringReader reader) throws CommandSyntaxException {
/*    */     double value;
/* 48 */     if (reader.canRead() && reader.peek() == '^')
/* 49 */       throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(reader); 
/* 52 */     if (!reader.canRead())
/* 53 */       throw ERROR_EXPECTED_INT.createWithContext(reader); 
/* 56 */     boolean relative = isRelative(reader);
/* 58 */     if (reader.canRead() && reader.peek() != ' ') {
/* 59 */       value = relative ? reader.readDouble() : reader.readInt();
/*    */     } else {
/* 61 */       value = 0.0D;
/*    */     } 
/* 63 */     return new WorldCoordinate(relative, value);
/*    */   }
/*    */   
/*    */   public static boolean isRelative(StringReader reader) {
/*    */     boolean relative;
/* 68 */     if (reader.peek() == '~') {
/* 69 */       relative = true;
/* 70 */       reader.skip();
/*    */     } else {
/* 72 */       relative = false;
/*    */     } 
/* 74 */     return relative;
/*    */   }
/*    */   
/*    */   public boolean isRelative() {
/* 78 */     return this.relative;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\coordinates\WorldCoordinate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */