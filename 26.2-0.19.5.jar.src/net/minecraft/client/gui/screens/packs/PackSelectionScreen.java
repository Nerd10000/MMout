/*     */ package net.minecraft.client.gui.screens.packs;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardWatchEventKinds;
/*     */ import java.nio.file.WatchEvent;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.nio.file.WatchService;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.AlertScreen;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.NoticeWithLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.repository.PackDetector;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.resources.IoSupplier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PackSelectionScreen extends Screen {
/*  59 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  61 */   private static final Component AVAILABLE_TITLE = (Component)Component.translatable("pack.available.title");
/*     */   
/*  62 */   private static final Component SELECTED_TITLE = (Component)Component.translatable("pack.selected.title");
/*     */   
/*  63 */   private static final Component OPEN_PACK_FOLDER_TITLE = (Component)Component.translatable("pack.openFolder");
/*     */   
/*  64 */   private static final Component SEARCH = (Component)Component.translatable("gui.packSelection.search").withStyle(EditBox.SEARCH_HINT_STYLE);
/*     */   
/*     */   private static final int LIST_WIDTH = 200;
/*     */   
/*     */   private static final int HEADER_ELEMENT_SPACING = 4;
/*     */   
/*     */   private static final int SEARCH_BOX_HEIGHT = 15;
/*     */   
/*  69 */   private static final Component DRAG_AND_DROP = (Component)Component.translatable("pack.dropInfo").withStyle(ChatFormatting.GRAY);
/*     */   
/*  70 */   private static final Component DIRECTORY_BUTTON_TOOLTIP = (Component)Component.translatable("pack.folderInfo");
/*     */   
/*     */   private static final int RELOAD_COOLDOWN = 20;
/*     */   
/*  73 */   private static final Identifier DEFAULT_ICON = Identifier.withDefaultNamespace("textures/misc/unknown_pack.png");
/*     */   
/*  75 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private final PackSelectionModel model;
/*     */   
/*     */   private Watcher watcher;
/*     */   
/*     */   private long ticksToReload;
/*     */   
/*     */   private TransferableSelectionList availablePackList;
/*     */   
/*     */   private TransferableSelectionList selectedPackList;
/*     */   
/*     */   private EditBox search;
/*     */   
/*     */   private final Path packDir;
/*     */   
/*     */   private Button doneButton;
/*     */   
/*  87 */   private final Map<String, Identifier> packIcons = Maps.newHashMap();
/*     */   
/*     */   public PackSelectionScreen(PackRepository repository, Consumer<PackRepository> output, Path packDir, Component title) {
/*  90 */     super(title);
/*  91 */     this.model = new PackSelectionModel(this::populateLists, this::getPackIcon, repository, output);
/*  92 */     this.packDir = packDir;
/*  93 */     this.watcher = Watcher.create(packDir);
/*     */   }
/*     */   
/*     */   public void onClose() {
/*  98 */     this.model.commit();
/*  99 */     closeWatcher();
/*     */   }
/*     */   
/*     */   private void closeWatcher() {
/* 103 */     if (this.watcher != null)
/*     */       try {
/* 105 */         this.watcher.close();
/* 106 */         this.watcher = null;
/* 107 */       } catch (Exception exception) {} 
/*     */   }
/*     */   
/*     */   protected void init() {
/* 114 */     Objects.requireNonNull(this.font);
/* 114 */     Objects.requireNonNull(this.font);
/* 114 */     this.layout.setHeaderHeight(4 + 9 + 4 + 9 + 4 + 15 + 4);
/* 115 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(4));
/* 116 */     header.defaultCellSetting().alignHorizontallyCenter();
/* 117 */     header.addChild((LayoutElement)new StringWidget(getTitle(), this.font));
/* 118 */     header.addChild((LayoutElement)new StringWidget(DRAG_AND_DROP, this.font));
/* 119 */     this.search = (EditBox)header.addChild((LayoutElement)new EditBox(this.font, 0, 0, 200, 15, (Component)Component.empty()));
/* 120 */     this.search.setHint(SEARCH);
/* 121 */     this.search.setResponder(this::updateFilteredEntries);
/* 122 */     this.availablePackList = (TransferableSelectionList)this.layout.addToContents((LayoutElement)new TransferableSelectionList(this.minecraft, this, 200, this.height - 66, AVAILABLE_TITLE));
/* 123 */     this.selectedPackList = (TransferableSelectionList)this.layout.addToContents((LayoutElement)new TransferableSelectionList(this.minecraft, this, 200, this.height - 66, SELECTED_TITLE));
/* 125 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 126 */     footer.addChild((LayoutElement)Button.builder(OPEN_PACK_FOLDER_TITLE, button -> Util.getPlatform().openPath(this.packDir))
/* 127 */         .tooltip(Tooltip.create(DIRECTORY_BUTTON_TOOLTIP))
/* 128 */         .build());
/* 130 */     this.doneButton = (Button)footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).build());
/* 132 */     PackSelectionScreen packSelectionScreen = this;
/* 132 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/* 133 */     repositionElements();
/* 134 */     reload();
/*     */   }
/*     */   
/*     */   protected void setInitialFocus() {
/* 139 */     if (this.search != null) {
/* 140 */       setInitialFocus((GuiEventListener)this.search);
/*     */     } else {
/* 142 */       super.setInitialFocus();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateFilteredEntries(String value) {
/* 147 */     updateFilteredEntries(value, (PackSelectionModel.EntryBase)null);
/*     */   }
/*     */   
/*     */   private void updateFilteredEntries(String value, PackSelectionModel.EntryBase transferredEntry) {
/* 151 */     filterEntries(value, this.model.getSelected(), this.selectedPackList, transferredEntry);
/* 152 */     filterEntries(value, this.model.getUnselected(), this.availablePackList, transferredEntry);
/*     */   }
/*     */   
/*     */   private void filterEntries(String value, Stream<PackSelectionModel.Entry> oldEntries, TransferableSelectionList listToUpdate, PackSelectionModel.EntryBase transferredEntry) {
/* 156 */     if (listToUpdate == null)
/*     */       return; 
/* 159 */     String lowerCaseValue = value.toLowerCase(Locale.ROOT);
/* 160 */     Stream<PackSelectionModel.Entry> filteredEntries = oldEntries.filter(packEntry -> (value.isBlank() || packEntry.getId().toLowerCase(Locale.ROOT).contains(lowerCaseValue) || packEntry.getTitle().getString().toLowerCase(Locale.ROOT).contains(lowerCaseValue) || packEntry.getDescription().getString().toLowerCase(Locale.ROOT).contains(lowerCaseValue)));
/* 164 */     listToUpdate.updateList(filteredEntries, transferredEntry);
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 169 */     this.layout.arrangeElements();
/* 170 */     if (this.availablePackList != null)
/* 171 */       this.availablePackList.updateSizeAndPosition(200, this.layout.getContentHeight(), this.width / 2 - 15 - 200, this.layout.getHeaderHeight()); 
/* 173 */     if (this.selectedPackList != null)
/* 174 */       this.selectedPackList.updateSizeAndPosition(200, this.layout.getContentHeight(), this.width / 2 + 15, this.layout.getHeaderHeight()); 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 180 */     if (this.watcher != null)
/*     */       try {
/* 182 */         if (this.watcher.pollForChanges())
/* 184 */           this.ticksToReload = 20L; 
/* 186 */       } catch (IOException e) {
/* 187 */         LOGGER.warn("Failed to poll for directory {} changes, stopping", this.packDir);
/* 188 */         closeWatcher();
/*     */       }  
/* 192 */     if (this.ticksToReload > 0L && 
/* 193 */       --this.ticksToReload == 0L)
/* 194 */       reload(); 
/*     */   }
/*     */   
/*     */   private void populateLists(PackSelectionModel.EntryBase transferredEntry) {
/* 200 */     if (this.selectedPackList != null)
/* 201 */       this.selectedPackList.updateList(this.model.getSelected(), transferredEntry); 
/* 203 */     if (this.availablePackList != null)
/* 204 */       this.availablePackList.updateList(this.model.getUnselected(), transferredEntry); 
/* 206 */     if (this.search != null)
/* 207 */       updateFilteredEntries(this.search.getValue(), transferredEntry); 
/* 209 */     if (this.doneButton != null)
/* 210 */       this.doneButton.active = !this.selectedPackList.children().isEmpty(); 
/*     */   }
/*     */   
/*     */   private void reload() {
/* 215 */     this.model.findNewPacks();
/* 216 */     populateLists((PackSelectionModel.EntryBase)null);
/* 217 */     this.ticksToReload = 0L;
/* 218 */     this.packIcons.clear();
/*     */   }
/*     */   
/*     */   protected static void copyPacks(Minecraft minecraft, List<Path> files, Path targetDir) {
/* 222 */     MutableBoolean showErrorToast = new MutableBoolean();
/* 223 */     files.forEach(pack -> {
/*     */           try {
/*     */             Stream<Path> contents = Files.walk(pack, new java.nio.file.FileVisitOption[0]);
/*     */             try {
/*     */               contents.forEach(());
/*     */               if (contents != null)
/*     */                 contents.close(); 
/* 224 */             } catch (Throwable t$) {
/*     */               if (contents != null)
/*     */                 try {
/*     */                   contents.close();
/* 224 */                 } catch (Throwable x2) {
/*     */                   t$.addSuppressed(x2);
/*     */                 }  
/*     */               throw t$;
/*     */             } 
/* 233 */           } catch (IOException e) {
/*     */             LOGGER.warn("Failed to copy datapack file from {} to {}", pack, targetDir);
/*     */             showErrorToast.setTrue();
/*     */           } 
/*     */         });
/* 238 */     if (showErrorToast.isTrue())
/* 239 */       SystemToast.onPackCopyFailure(minecraft, targetDir.toString()); 
/*     */   }
/*     */   
/*     */   public void onFilesDrop(List<Path> files) {
/* 245 */     String names = extractPackNames(files).collect(Collectors.joining(", "));
/* 246 */     this.minecraft.gui.setScreen((Screen)new ConfirmScreen(result -> {
/*     */             if (result) {
/*     */               List<Path> packCandidates = new ArrayList<>(files.size());
/*     */               Set<Path> leftoverPacks = new HashSet<>(files);
/*     */               PackDetector<Path> packDetector = new PackDetector<Path>(this, this.minecraft.directoryValidator()) {
/*     */                   protected Path createZipPack(Path content) {
/* 253 */                     return content;
/*     */                   }
/*     */                   
/*     */                   protected Path createDirectoryPack(Path content) {
/* 258 */                     return content;
/*     */                   }
/*     */                 };
/*     */               List<ForbiddenSymlinkInfo> issues = new ArrayList<>();
/*     */               for (Path path : files) {
/*     */                 try {
/*     */                   Path candidate = (Path)packDetector.detectPackResources(path, issues);
/*     */                   if (candidate == null) {
/*     */                     LOGGER.warn("Path {} does not seem like pack", path);
/*     */                     continue;
/*     */                   } 
/*     */                   packCandidates.add(candidate);
/*     */                   leftoverPacks.remove(candidate);
/* 272 */                 } catch (IOException e) {
/*     */                   LOGGER.warn("Failed to check {} for packs", path, e);
/*     */                 } 
/*     */               } 
/*     */               if (!issues.isEmpty()) {
/*     */                 this.minecraft.gui.setScreen(NoticeWithLinkScreen.createPackSymlinkWarningScreen(()));
/*     */                 return;
/*     */               } 
/*     */               if (!packCandidates.isEmpty()) {
/*     */                 copyPacks(this.minecraft, packCandidates, this.packDir);
/*     */                 reload();
/*     */               } 
/*     */               if (!leftoverPacks.isEmpty()) {
/*     */                 String leftoverNames = extractPackNames(leftoverPacks).collect(Collectors.joining(", "));
/*     */                 this.minecraft.gui.setScreen((Screen)new AlertScreen((), (Component)Component.translatable("pack.dropRejected.title"), (Component)Component.translatable("pack.dropRejected.message", new Object[] { leftoverNames })));
/*     */                 return;
/*     */               } 
/*     */             } 
/*     */             this.minecraft.gui.setScreen(this);
/* 299 */           }(Component)Component.translatable("pack.dropConfirm"), 
/* 300 */           (Component)Component.literal(names)));
/*     */   }
/*     */   
/*     */   private static Stream<String> extractPackNames(Collection<Path> files) {
/* 304 */     return files.stream().map(Path::getFileName).map(Path::toString);
/*     */   }
/*     */   
/*     */   private Identifier loadPackIcon(TextureManager textureManager, Pack pack) {
/*     */     try {
/* 308 */       PackResources packResources = pack.open();
/*     */       try {
/* 309 */         IoSupplier<InputStream> resource = packResources.getRootResource(new String[] { "pack.png" });
/* 310 */         if (resource == null) {
/* 311 */           Identifier identifier = DEFAULT_ICON;
/* 321 */           if (packResources != null)
/* 321 */             packResources.close(); 
/*     */           return identifier;
/*     */         } 
/*     */         String id = pack.getId();
/*     */         Identifier location = Identifier.withDefaultNamespace("pack/" + Util.sanitizeName(id, Identifier::validPathChar) + "/" + String.valueOf(Hashing.sha1().hashUnencodedChars(id)) + "/icon");
/*     */         InputStream stream = (InputStream)resource.get();
/*     */         try {
/*     */           NativeImage iconImage = NativeImage.read(stream);
/*     */           Objects.requireNonNull(location);
/*     */           textureManager.register(location, (AbstractTexture)new DynamicTexture(location::toString, iconImage));
/*     */           Identifier identifier = location;
/*     */           if (stream != null)
/*     */             stream.close(); 
/* 321 */           if (packResources != null)
/* 321 */             packResources.close(); 
/*     */           return identifier;
/*     */         } catch (Throwable throwable) {
/*     */           if (stream != null)
/*     */             try {
/*     */               stream.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/*     */       } catch (Throwable throwable) {
/*     */         if (packResources != null)
/*     */           try {
/*     */             packResources.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 321 */     } catch (Exception e) {
/* 322 */       LOGGER.warn("Failed to load icon from pack {}", pack.getId(), e);
/* 324 */       return DEFAULT_ICON;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Identifier getPackIcon(Pack pack) {
/* 328 */     return this.packIcons.computeIfAbsent(pack.getId(), s -> loadPackIcon(this.minecraft.getTextureManager(), pack));
/*     */   }
/*     */   
/*     */   private static class Watcher implements AutoCloseable {
/*     */     private final WatchService watcher;
/*     */     
/*     */     private final Path packPath;
/*     */     
/*     */     public Watcher(Path packPath) throws IOException {
/* 336 */       this.packPath = packPath;
/* 337 */       this.watcher = packPath.getFileSystem().newWatchService();
/*     */       try {
/* 340 */         watchDir(packPath);
/* 343 */         DirectoryStream<Path> paths = Files.newDirectoryStream(packPath);
/*     */         try {
/* 344 */           for (Path path : paths) {
/* 345 */             if (Files.isDirectory(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS }))
/* 346 */               watchDir(path); 
/*     */           } 
/* 349 */           if (paths != null)
/* 349 */             paths.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (paths != null)
/*     */             try {
/*     */               paths.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 350 */       } catch (Exception e) {
/* 351 */         this.watcher.close();
/* 352 */         throw e;
/*     */       } 
/*     */     }
/*     */     
/*     */     public static Watcher create(Path packDir) {
/*     */       try {
/* 358 */         return new Watcher(packDir);
/* 359 */       } catch (IOException e) {
/* 360 */         PackSelectionScreen.LOGGER.warn("Failed to initialize pack directory {} monitoring", packDir, e);
/* 361 */         return null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void watchDir(Path packPath) throws IOException {
/* 366 */       packPath.register(this.watcher, (WatchEvent.Kind<?>[])new WatchEvent.Kind[] { StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY });
/*     */     }
/*     */     
/*     */     public boolean pollForChanges() throws IOException {
/* 370 */       boolean hasChanges = false;
/*     */       WatchKey key;
/* 374 */       while ((key = this.watcher.poll()) != null) {
/* 375 */         List<WatchEvent<?>> watchEvents = key.pollEvents();
/* 376 */         for (WatchEvent<?> watchEvent : watchEvents) {
/* 377 */           hasChanges = true;
/* 379 */           Path newPath = this.packPath.resolve((Path)watchEvent.context());
/* 380 */           if (key.watchable() == this.packPath && watchEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE && Files.isDirectory(newPath, new LinkOption[] { LinkOption.NOFOLLOW_LINKS }))
/* 381 */             watchDir(newPath); 
/*     */         } 
/* 385 */         key.reset();
/*     */       } 
/* 388 */       return hasChanges;
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 393 */       this.watcher.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\packs\PackSelectionScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */