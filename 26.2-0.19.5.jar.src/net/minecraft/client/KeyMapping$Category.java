/*    */ package net.minecraft.client;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class Category extends Record {
/*    */   private final Identifier id;
/*    */   
/*    */   public Category(Identifier id) {
/* 25 */     this.id = id;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/KeyMapping$Category;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/KeyMapping$Category;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/KeyMapping$Category;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/KeyMapping$Category;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/KeyMapping$Category;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/KeyMapping$Category;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/* 25 */     return this.id;
/*    */   }
/*    */   
/* 26 */   private static final List<Category> SORT_ORDER = new ArrayList<>();
/*    */   
/* 27 */   public static final Category MOVEMENT = register("movement");
/*    */   
/* 28 */   public static final Category MISC = register("misc");
/*    */   
/* 29 */   public static final Category MULTIPLAYER = register("multiplayer");
/*    */   
/* 30 */   public static final Category GAMEPLAY = register("gameplay");
/*    */   
/* 31 */   public static final Category INVENTORY = register("inventory");
/*    */   
/* 32 */   public static final Category CREATIVE = register("creative");
/*    */   
/* 33 */   public static final Category SPECTATOR = register("spectator");
/*    */   
/* 34 */   public static final Category DEBUG = register("debug");
/*    */   
/*    */   private static Category register(String name) {
/* 37 */     return register(Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/*    */   public static Category register(Identifier id) {
/* 41 */     Category category = new Category(id);
/* 42 */     if (SORT_ORDER.contains(category))
/* 43 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "Category '%s' is already registered.", new Object[] { id })); 
/* 45 */     SORT_ORDER.add(category);
/* 46 */     return category;
/*    */   }
/*    */   
/*    */   public Component label() {
/* 50 */     return (Component)Component.translatable(this.id.toLanguageKey("key.category"));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\KeyMapping$Category.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */