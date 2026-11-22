/*     */ package net.minecraft.client.resources.model.sprite;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.resources.model.ModelDebugName;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Resolver {
/*  99 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/* 102 */   private final List<TextureSlots.Data> entries = new ArrayList<>();
/*     */   
/*     */   public Resolver addLast(TextureSlots.Data data) {
/* 105 */     this.entries.addLast(data);
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   public Resolver addFirst(TextureSlots.Data data) {
/* 110 */     this.entries.addFirst(data);
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public TextureSlots resolve(ModelDebugName debugNameProvider) {
/* 115 */     if (this.entries.isEmpty())
/* 116 */       return TextureSlots.EMPTY; 
/* 119 */     Object2ObjectArrayMap object2ObjectArrayMap1 = new Object2ObjectArrayMap();
/* 120 */     Object2ObjectArrayMap object2ObjectArrayMap2 = new Object2ObjectArrayMap();
/* 122 */     for (TextureSlots.Data data : Lists.reverse(this.entries)) {
/* 123 */       data.values.forEach((slot, contents) -> {
/*     */             // Byte code:
/*     */             //   0: aload_3
/*     */             //   1: dup
/*     */             //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */             //   5: pop
/*     */             //   6: astore #4
/*     */             //   8: iconst_0
/*     */             //   9: istore #5
/*     */             //   11: aload #4
/*     */             //   13: iload #5
/*     */             //   15: <illegal opcode> typeSwitch : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$SlotContents;I)I
/*     */             //   20: lookupswitch default -> 48, 0 -> 58, 1 -> 89
/*     */             //   48: new java/lang/MatchException
/*     */             //   51: dup
/*     */             //   52: aconst_null
/*     */             //   53: aconst_null
/*     */             //   54: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */             //   57: athrow
/*     */             //   58: aload #4
/*     */             //   60: checkcast net/minecraft/client/resources/model/sprite/TextureSlots$Value
/*     */             //   63: astore #6
/*     */             //   65: aload_0
/*     */             //   66: aload_2
/*     */             //   67: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */             //   72: pop
/*     */             //   73: aload_1
/*     */             //   74: aload_2
/*     */             //   75: aload #6
/*     */             //   77: invokevirtual material : ()Lnet/minecraft/client/resources/model/sprite/Material;
/*     */             //   80: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */             //   85: pop
/*     */             //   86: goto -> 114
/*     */             //   89: aload #4
/*     */             //   91: checkcast net/minecraft/client/resources/model/sprite/TextureSlots$Reference
/*     */             //   94: astore #7
/*     */             //   96: aload_1
/*     */             //   97: aload_2
/*     */             //   98: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */             //   103: pop
/*     */             //   104: aload_0
/*     */             //   105: aload_2
/*     */             //   106: aload #7
/*     */             //   108: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */             //   113: pop
/*     */             //   114: return
/*     */             // Line number table:
/*     */             //   Java source line number -> byte code offset
/*     */             //   #124	-> 0
/*     */             //   #125	-> 58
/*     */             //   #126	-> 65
/*     */             //   #127	-> 73
/*     */             //   #128	-> 86
/*     */             //   #129	-> 89
/*     */             //   #130	-> 96
/*     */             //   #131	-> 104
/*     */             //   #134	-> 114
/*     */             // Local variable table:
/*     */             //   start	length	slot	name	descriptor
/*     */             //   65	24	6	value	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Value;
/*     */             //   96	18	7	reference	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Reference;
/*     */             //   8	106	4	selector0$temp	Lnet/minecraft/client/resources/model/sprite/TextureSlots$SlotContents;
/*     */             //   11	103	5	index$1	I
/*     */             //   0	115	0	unresolved	Lit/unimi/dsi/fastutil/objects/Object2ObjectMap;
/*     */             //   0	115	1	resolved	Lit/unimi/dsi/fastutil/objects/Object2ObjectMap;
/*     */             //   0	115	2	slot	Ljava/lang/String;
/*     */             //   0	115	3	contents	Lnet/minecraft/client/resources/model/sprite/TextureSlots$SlotContents;
/*     */           });
/*     */     } 
/* 137 */     if (object2ObjectArrayMap2.isEmpty())
/* 138 */       return new TextureSlots((Map<String, Material>)object2ObjectArrayMap1); 
/* 142 */     boolean hasChanges = true;
/* 143 */     while (hasChanges) {
/* 144 */       hasChanges = false;
/* 145 */       ObjectIterator<Object2ObjectMap.Entry<String, TextureSlots.Reference>> iterator = Object2ObjectMaps.fastIterator((Object2ObjectMap)object2ObjectArrayMap2);
/* 146 */       while (iterator.hasNext()) {
/* 147 */         Object2ObjectMap.Entry<String, TextureSlots.Reference> entry = (Object2ObjectMap.Entry<String, TextureSlots.Reference>)iterator.next();
/* 148 */         Material maybeResolved = (Material)object2ObjectArrayMap1.get(((TextureSlots.Reference)entry.getValue()).target);
/* 149 */         if (maybeResolved != null) {
/* 150 */           object2ObjectArrayMap1.put(entry.getKey(), maybeResolved);
/* 151 */           iterator.remove();
/* 152 */           hasChanges = true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 157 */     if (!object2ObjectArrayMap2.isEmpty())
/* 160 */       LOGGER.warn("Unresolved texture references in {}:\n{}", debugNameProvider.debugName(), object2ObjectArrayMap2.entrySet().stream().map(e -> "\t#" + (String)e.getKey() + "-> #" + ((TextureSlots.Reference)e.getValue()).target + "\n").collect(Collectors.joining())); 
/* 163 */     return new TextureSlots((Map<String, Material>)object2ObjectArrayMap1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\sprite\TextureSlots$Resolver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */