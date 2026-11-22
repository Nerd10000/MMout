/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ public class CommandStorage {
/*    */   private static final String COMMAND_STORAGE = "command_storage";
/*    */   
/*    */   private static class Container extends SavedData {
/*    */     public static final Codec<Container> CODEC;
/*    */     
/*    */     private final Map<String, CompoundTag> storage;
/*    */     
/*    */     static {
/* 19 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.unboundedMap(ExtraCodecs.RESOURCE_PATH_CODEC, CompoundTag.CODEC).fieldOf("contents").forGetter(())).apply((Applicative)i, Container::new));
/*    */     }
/*    */     
/*    */     private Container(Map<String, CompoundTag> storage) {
/* 26 */       this.storage = new HashMap<>(storage);
/*    */     }
/*    */     
/*    */     private Container() {
/* 30 */       this(new HashMap<>());
/*    */     }
/*    */     
/*    */     public static SavedDataType<Container> type(String namespace) {
/* 34 */       return new SavedDataType(Identifier.fromNamespaceAndPath(namespace, "command_storage"), Container::new, CODEC, DataFixTypes.SAVED_DATA_COMMAND_STORAGE);
/*    */     }
/*    */     
/*    */     public CompoundTag get(String id) {
/* 38 */       CompoundTag result = this.storage.get(id);
/* 39 */       return (result != null) ? result : new CompoundTag();
/*    */     }
/*    */     
/*    */     public void put(String id, CompoundTag contents) {
/* 43 */       if (contents.isEmpty()) {
/* 44 */         this.storage.remove(id);
/*    */       } else {
/* 46 */         this.storage.put(id, contents);
/*    */       } 
/* 48 */       setDirty();
/*    */     }
/*    */     
/*    */     public Stream<Identifier> getKeys(String namespace) {
/* 52 */       return this.storage.keySet().stream().map(p -> Identifier.fromNamespaceAndPath(namespace, p));
/*    */     }
/*    */   }
/*    */   
/* 57 */   private final Map<String, Container> namespaces = new HashMap<>();
/*    */   
/*    */   private final SavedDataStorage savedDataStorage;
/*    */   
/*    */   public CommandStorage(SavedDataStorage savedDataStorage) {
/* 61 */     this.savedDataStorage = savedDataStorage;
/*    */   }
/*    */   
/*    */   public CompoundTag get(Identifier id) {
/* 65 */     Container container = getContainer(id.getNamespace());
/* 66 */     if (container != null)
/* 67 */       return container.get(id.getPath()); 
/* 69 */     return new CompoundTag();
/*    */   }
/*    */   
/*    */   private Container getContainer(String namespace) {
/* 73 */     Container container = this.namespaces.get(namespace);
/* 74 */     if (container != null)
/* 75 */       return container; 
/* 77 */     Container newContainer = this.savedDataStorage.<Container>get(Container.type(namespace));
/* 78 */     if (newContainer != null)
/* 79 */       this.namespaces.put(namespace, newContainer); 
/* 81 */     return newContainer;
/*    */   }
/*    */   
/*    */   private Container getOrCreateContainer(String namespace) {
/* 85 */     Container container = this.namespaces.get(namespace);
/* 86 */     if (container != null)
/* 87 */       return container; 
/* 89 */     Container newContainer = this.savedDataStorage.<Container>computeIfAbsent(Container.type(namespace));
/* 90 */     this.namespaces.put(namespace, newContainer);
/* 91 */     return newContainer;
/*    */   }
/*    */   
/*    */   public void set(Identifier id, CompoundTag contents) {
/* 95 */     getOrCreateContainer(id.getNamespace()).put(id.getPath(), contents);
/*    */   }
/*    */   
/*    */   public Stream<Identifier> keys() {
/* 99 */     return this.namespaces.entrySet().stream().flatMap(e -> ((Container)e.getValue()).getKeys((String)e.getKey()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\CommandStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */