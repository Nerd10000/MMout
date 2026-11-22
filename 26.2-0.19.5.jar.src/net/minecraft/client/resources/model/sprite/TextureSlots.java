/*     */ package net.minecraft.client.resources.model.sprite;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.resources.model.ModelDebugName;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TextureSlots {
/*  25 */   public static final TextureSlots EMPTY = new TextureSlots(Map.of());
/*     */   
/*     */   private static final char REFERENCE_CHAR = '#';
/*     */   
/*     */   private final Map<String, Material> resolvedValues;
/*     */   
/*     */   private TextureSlots(Map<String, Material> resolvedValues) {
/*  31 */     this.resolvedValues = resolvedValues;
/*     */   }
/*     */   
/*     */   public Material getMaterial(String reference) {
/*  36 */     if (isTextureReference(reference))
/*  37 */       reference = reference.substring(1); 
/*  40 */     return this.resolvedValues.get(reference);
/*     */   }
/*     */   
/*     */   private static boolean isTextureReference(String texture) {
/*  44 */     return (texture.charAt(0) == '#');
/*     */   }
/*     */   
/*     */   public static Data parseTextureMap(JsonObject texturesObject) {
/*  48 */     Data.Builder builder = new Data.Builder();
/*  49 */     for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)texturesObject.entrySet())
/*  50 */       parseEntry(entry.getKey(), entry.getValue(), builder); 
/*  52 */     return builder.build();
/*     */   }
/*     */   
/*     */   private static void parseEntry(String slot, JsonElement value, Data.Builder output) {
/*  56 */     if (GsonHelper.isStringValue(value) && isTextureReference(value.getAsString())) {
/*  57 */       output.addReference(slot, value.getAsString().substring(1));
/*     */     } else {
/*  59 */       output.addTexture(slot, (Material)Material.CODEC.parse((DynamicOps)JsonOps.INSTANCE, value).getOrThrow(com.google.gson.JsonParseException::new));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class Value extends Record implements SlotContents {
/*     */     private final Material material;
/*     */     
/*     */     private Value(Material material) {
/*  66 */       this.material = material;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$Value;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Value;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$Value;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Value;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$Value;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Value;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Material material() {
/*  66 */       return this.material;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Reference extends Record implements SlotContents {
/*     */     private final String target;
/*     */     
/*     */     private Reference(String target) {
/*  69 */       this.target = target;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$Reference;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Reference;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$Reference;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Reference;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$Reference;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Reference;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String target() {
/*  69 */       return this.target;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Data extends Record {
/*     */     private final Map<String, TextureSlots.SlotContents> values;
/*     */     
/*     */     public Data(Map<String, TextureSlots.SlotContents> values) {
/*  72 */       this.values = values;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #72	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Data;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #72	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Data;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #72	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Data;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Map<String, TextureSlots.SlotContents> values() {
/*  72 */       return this.values;
/*     */     }
/*     */     
/*  73 */     public static final Data EMPTY = new Data(Map.of());
/*     */     
/*     */     public static class Builder {
/*  76 */       private final Map<String, TextureSlots.SlotContents> textureMap = new HashMap<>();
/*     */       
/*     */       public Builder addReference(String slot, String reference) {
/*  79 */         this.textureMap.put(slot, new TextureSlots.Reference(reference));
/*  80 */         return this;
/*     */       }
/*     */       
/*     */       public Builder addTexture(String slot, Material material) {
/*  84 */         this.textureMap.put(slot, new TextureSlots.Value(material));
/*  85 */         return this;
/*     */       }
/*     */       
/*     */       public TextureSlots.Data build() {
/*  89 */         if (this.textureMap.isEmpty())
/*  90 */           return TextureSlots.Data.EMPTY; 
/*  93 */         return new TextureSlots.Data(Map.copyOf(this.textureMap));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final Map<String, TextureSlots.SlotContents> textureMap;
/*     */     
/*     */     public Builder() {
/*     */       this.textureMap = new HashMap<>();
/*     */     }
/*     */     
/*     */     public Builder addReference(String slot, String reference) {
/*     */       this.textureMap.put(slot, new TextureSlots.Reference(reference));
/*     */       return this;
/*     */     }
/*     */     
/*     */     public Builder addTexture(String slot, Material material) {
/*     */       this.textureMap.put(slot, new TextureSlots.Value(material));
/*     */       return this;
/*     */     }
/*     */     
/*     */     public TextureSlots.Data build() {
/*     */       if (this.textureMap.isEmpty())
/*     */         return TextureSlots.Data.EMPTY; 
/*  93 */       return new TextureSlots.Data(Map.copyOf(this.textureMap));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Resolver {
/*     */     public Resolver() {
/* 102 */       this.entries = new ArrayList<>();
/*     */     }
/*     */     
/*     */     private static final Logger LOGGER = LogUtils.getLogger();
/*     */     
/*     */     private final List<TextureSlots.Data> entries;
/*     */     
/*     */     public Resolver addLast(TextureSlots.Data data) {
/* 105 */       this.entries.addLast(data);
/* 106 */       return this;
/*     */     }
/*     */     
/*     */     public Resolver addFirst(TextureSlots.Data data) {
/* 110 */       this.entries.addFirst(data);
/* 111 */       return this;
/*     */     }
/*     */     
/*     */     public TextureSlots resolve(ModelDebugName debugNameProvider) {
/* 115 */       if (this.entries.isEmpty())
/* 116 */         return TextureSlots.EMPTY; 
/* 119 */       Object2ObjectArrayMap object2ObjectArrayMap1 = new Object2ObjectArrayMap();
/* 120 */       Object2ObjectArrayMap object2ObjectArrayMap2 = new Object2ObjectArrayMap();
/* 122 */       for (TextureSlots.Data data : Lists.reverse(this.entries)) {
/* 123 */         data.values.forEach((slot, contents) -> {
/*     */               // Byte code:
/*     */               //   0: aload_3
/*     */               //   1: dup
/*     */               //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */               //   5: pop
/*     */               //   6: astore #4
/*     */               //   8: iconst_0
/*     */               //   9: istore #5
/*     */               //   11: aload #4
/*     */               //   13: iload #5
/*     */               //   15: <illegal opcode> typeSwitch : (Lnet/minecraft/client/resources/model/sprite/TextureSlots$SlotContents;I)I
/*     */               //   20: lookupswitch default -> 48, 0 -> 58, 1 -> 89
/*     */               //   48: new java/lang/MatchException
/*     */               //   51: dup
/*     */               //   52: aconst_null
/*     */               //   53: aconst_null
/*     */               //   54: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */               //   57: athrow
/*     */               //   58: aload #4
/*     */               //   60: checkcast net/minecraft/client/resources/model/sprite/TextureSlots$Value
/*     */               //   63: astore #6
/*     */               //   65: aload_0
/*     */               //   66: aload_2
/*     */               //   67: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */               //   72: pop
/*     */               //   73: aload_1
/*     */               //   74: aload_2
/*     */               //   75: aload #6
/*     */               //   77: invokevirtual material : ()Lnet/minecraft/client/resources/model/sprite/Material;
/*     */               //   80: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */               //   85: pop
/*     */               //   86: goto -> 114
/*     */               //   89: aload #4
/*     */               //   91: checkcast net/minecraft/client/resources/model/sprite/TextureSlots$Reference
/*     */               //   94: astore #7
/*     */               //   96: aload_1
/*     */               //   97: aload_2
/*     */               //   98: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */               //   103: pop
/*     */               //   104: aload_0
/*     */               //   105: aload_2
/*     */               //   106: aload #7
/*     */               //   108: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */               //   113: pop
/*     */               //   114: return
/*     */               // Line number table:
/*     */               //   Java source line number -> byte code offset
/*     */               //   #124	-> 0
/*     */               //   #125	-> 58
/*     */               //   #126	-> 65
/*     */               //   #127	-> 73
/*     */               //   #128	-> 86
/*     */               //   #129	-> 89
/*     */               //   #130	-> 96
/*     */               //   #131	-> 104
/*     */               //   #134	-> 114
/*     */               // Local variable table:
/*     */               //   start	length	slot	name	descriptor
/*     */               //   65	24	6	value	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Value;
/*     */               //   96	18	7	reference	Lnet/minecraft/client/resources/model/sprite/TextureSlots$Reference;
/*     */               //   8	106	4	selector0$temp	Lnet/minecraft/client/resources/model/sprite/TextureSlots$SlotContents;
/*     */               //   11	103	5	index$1	I
/*     */               //   0	115	0	unresolved	Lit/unimi/dsi/fastutil/objects/Object2ObjectMap;
/*     */               //   0	115	1	resolved	Lit/unimi/dsi/fastutil/objects/Object2ObjectMap;
/*     */               //   0	115	2	slot	Ljava/lang/String;
/*     */               //   0	115	3	contents	Lnet/minecraft/client/resources/model/sprite/TextureSlots$SlotContents;
/*     */             });
/*     */       } 
/* 137 */       if (object2ObjectArrayMap2.isEmpty())
/* 138 */         return new TextureSlots((Map<String, Material>)object2ObjectArrayMap1); 
/* 142 */       boolean hasChanges = true;
/* 143 */       while (hasChanges) {
/* 144 */         hasChanges = false;
/* 145 */         ObjectIterator<Object2ObjectMap.Entry<String, TextureSlots.Reference>> iterator = Object2ObjectMaps.fastIterator((Object2ObjectMap)object2ObjectArrayMap2);
/* 146 */         while (iterator.hasNext()) {
/* 147 */           Object2ObjectMap.Entry<String, TextureSlots.Reference> entry = (Object2ObjectMap.Entry<String, TextureSlots.Reference>)iterator.next();
/* 148 */           Material maybeResolved = (Material)object2ObjectArrayMap1.get(((TextureSlots.Reference)entry.getValue()).target);
/* 149 */           if (maybeResolved != null) {
/* 150 */             object2ObjectArrayMap1.put(entry.getKey(), maybeResolved);
/* 151 */             iterator.remove();
/* 152 */             hasChanges = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 157 */       if (!object2ObjectArrayMap2.isEmpty())
/* 160 */         LOGGER.warn("Unresolved texture references in {}:\n{}", debugNameProvider.debugName(), object2ObjectArrayMap2.entrySet().stream().map(e -> "\t#" + (String)e.getKey() + "-> #" + ((TextureSlots.Reference)e.getValue()).target + "\n").collect(Collectors.joining())); 
/* 163 */       return new TextureSlots((Map<String, Material>)object2ObjectArrayMap1);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface SlotContents {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\sprite\TextureSlots.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */