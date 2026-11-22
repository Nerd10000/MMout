/*     */ package net.minecraft.util.filefix.virtualfilesystem;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.AccessMode;
/*     */ import java.nio.file.AtomicMoveNotSupportedException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.DummyFileAttributes;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSDirectoryNotEmptyException;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSFileAlreadyExistsException;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSFileSystemException;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSNoSuchFileException;
/*     */ 
/*     */ public class CopyOnWriteFSProvider extends FileSystemProvider {
/*     */   public static final String SCHEME = "x-mc-copy-on-write";
/*     */   
/*  42 */   private static final BasicFileAttributeView DUMMY_DIRECTORY_VIEW = new BasicFileAttributeView() {
/*     */       public String name() {
/*  45 */         return "basic";
/*     */       }
/*     */       
/*     */       public BasicFileAttributes readAttributes() {
/*  50 */         return DummyFileAttributes.DIRECTORY;
/*     */       }
/*     */       
/*     */       public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {}
/*     */     };
/*     */   
/*     */   private final CopyOnWriteFileSystem fs;
/*     */   
/*     */   public CopyOnWriteFSProvider(CopyOnWriteFileSystem fileSystem) {
/*  61 */     this.fs = fileSystem;
/*     */   }
/*     */   
/*     */   public String getScheme() {
/*  66 */     return "x-mc-copy-on-write";
/*     */   }
/*     */   
/*     */   public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
/*  71 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public FileSystem getFileSystem(URI uri) {
/*  76 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Path getPath(URI uri) {
/*  81 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
/*  86 */     return newChannel(path, options, attrs, Files::newByteChannel);
/*     */   }
/*     */   
/*     */   public FileChannel newFileChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
/*  91 */     return newChannel(path, options, attrs, FileChannel::open);
/*     */   }
/*     */   
/*     */   private synchronized <C> C newChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>[] attrs, ChannelFactory<C> channelFactory) throws IOException {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokestatic asCow : (Ljava/nio/file/Path;)Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;
/*     */     //   4: astore #5
/*     */     //   6: aload_2
/*     */     //   7: getstatic java/nio/file/StandardOpenOption.DELETE_ON_CLOSE : Ljava/nio/file/StandardOpenOption;
/*     */     //   10: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   15: ifeq -> 28
/*     */     //   18: new java/lang/UnsupportedOperationException
/*     */     //   21: dup
/*     */     //   22: ldc 'DELETE_ON_CLOSE is not supported by CowFS'
/*     */     //   24: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   27: athrow
/*     */     //   28: aload_0
/*     */     //   29: getfield fs : Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem;
/*     */     //   32: invokevirtual fileTree : ()Lnet/minecraft/util/filefix/virtualfilesystem/DirectoryNode;
/*     */     //   35: aload #5
/*     */     //   37: invokevirtual byPathOrNull : (Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;)Lnet/minecraft/util/filefix/virtualfilesystem/Node;
/*     */     //   40: astore #6
/*     */     //   42: iconst_0
/*     */     //   43: istore #7
/*     */     //   45: aload #6
/*     */     //   47: iload #7
/*     */     //   49: <illegal opcode> typeSwitch : (Lnet/minecraft/util/filefix/virtualfilesystem/Node;I)I
/*     */     //   54: tableswitch default -> 80, -1 -> 144, 0 -> 90, 1 -> 126
/*     */     //   80: new java/lang/MatchException
/*     */     //   83: dup
/*     */     //   84: aconst_null
/*     */     //   85: aconst_null
/*     */     //   86: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   89: athrow
/*     */     //   90: aload #6
/*     */     //   92: checkcast net/minecraft/util/filefix/virtualfilesystem/FileNode
/*     */     //   95: astore #8
/*     */     //   97: aload_2
/*     */     //   98: invokestatic wantsWrite : (Ljava/util/Set;)Z
/*     */     //   101: ifeq -> 109
/*     */     //   104: aload #8
/*     */     //   106: invokevirtual ensureCopy : ()V
/*     */     //   109: aload #4
/*     */     //   111: aload #8
/*     */     //   113: invokevirtual storagePath : ()Ljava/nio/file/Path;
/*     */     //   116: aload_2
/*     */     //   117: aload_3
/*     */     //   118: invokeinterface newChannel : (Ljava/nio/file/Path;Ljava/util/Set;[Ljava/nio/file/attribute/FileAttribute;)Ljava/lang/Object;
/*     */     //   123: goto -> 252
/*     */     //   126: new net/minecraft/util/filefix/virtualfilesystem/exception/CowFSFileSystemException
/*     */     //   129: dup
/*     */     //   130: aload #5
/*     */     //   132: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   135: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   140: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   143: athrow
/*     */     //   144: aload_2
/*     */     //   145: getstatic java/nio/file/StandardOpenOption.CREATE : Ljava/nio/file/StandardOpenOption;
/*     */     //   148: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   153: ifne -> 168
/*     */     //   156: aload_2
/*     */     //   157: getstatic java/nio/file/StandardOpenOption.CREATE_NEW : Ljava/nio/file/StandardOpenOption;
/*     */     //   160: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   165: ifeq -> 239
/*     */     //   168: aload_0
/*     */     //   169: getfield fs : Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem;
/*     */     //   172: invokevirtual fileTree : ()Lnet/minecraft/util/filefix/virtualfilesystem/DirectoryNode;
/*     */     //   175: aload #5
/*     */     //   177: invokevirtual getParent : ()Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;
/*     */     //   180: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   183: checkcast net/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath
/*     */     //   186: invokevirtual directoryByPath : (Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;)Lnet/minecraft/util/filefix/virtualfilesystem/DirectoryNode;
/*     */     //   189: astore #9
/*     */     //   191: aload_0
/*     */     //   192: getfield fs : Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem;
/*     */     //   195: invokevirtual createTemporaryFilePath : ()Ljava/nio/file/Path;
/*     */     //   198: astore #10
/*     */     //   200: aload #4
/*     */     //   202: aload #10
/*     */     //   204: aload_2
/*     */     //   205: aload_3
/*     */     //   206: invokeinterface newChannel : (Ljava/nio/file/Path;Ljava/util/Set;[Ljava/nio/file/attribute/FileAttribute;)Ljava/lang/Object;
/*     */     //   211: astore #11
/*     */     //   213: new net/minecraft/util/filefix/virtualfilesystem/FileNode
/*     */     //   216: dup
/*     */     //   217: aload #5
/*     */     //   219: aload #10
/*     */     //   221: iconst_1
/*     */     //   222: invokespecial <init> : (Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;Ljava/nio/file/Path;Z)V
/*     */     //   225: astore #12
/*     */     //   227: aload #9
/*     */     //   229: aload #12
/*     */     //   231: invokevirtual addChild : (Lnet/minecraft/util/filefix/virtualfilesystem/Node;)V
/*     */     //   234: aload #11
/*     */     //   236: goto -> 252
/*     */     //   239: new net/minecraft/util/filefix/virtualfilesystem/exception/CowFSNoSuchFileException
/*     */     //   242: dup
/*     */     //   243: aload #5
/*     */     //   245: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   248: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   251: athrow
/*     */     //   252: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #95	-> 0
/*     */     //   #96	-> 6
/*     */     //   #97	-> 18
/*     */     //   #99	-> 28
/*     */     //   #100	-> 90
/*     */     //   #101	-> 97
/*     */     //   #104	-> 104
/*     */     //   #106	-> 109
/*     */     //   #108	-> 126
/*     */     //   #110	-> 144
/*     */     //   #111	-> 168
/*     */     //   #112	-> 191
/*     */     //   #113	-> 200
/*     */     //   #114	-> 213
/*     */     //   #115	-> 227
/*     */     //   #116	-> 234
/*     */     //   #118	-> 239
/*     */     //   #99	-> 252
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   97	29	8	fileNode	Lnet/minecraft/util/filefix/virtualfilesystem/FileNode;
/*     */     //   191	48	9	directoryNode	Lnet/minecraft/util/filefix/virtualfilesystem/DirectoryNode;
/*     */     //   200	39	10	tempFile	Ljava/nio/file/Path;
/*     */     //   213	26	11	result	Ljava/lang/Object;
/*     */     //   227	12	12	child	Lnet/minecraft/util/filefix/virtualfilesystem/FileNode;
/*     */     //   0	253	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSProvider;
/*     */     //   0	253	1	path	Ljava/nio/file/Path;
/*     */     //   0	253	2	options	Ljava/util/Set;
/*     */     //   0	253	3	attrs	[Ljava/nio/file/attribute/FileAttribute;
/*     */     //   0	253	4	channelFactory	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSProvider$ChannelFactory;
/*     */     //   6	247	5	cowPath	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   213	26	11	result	TC;
/*     */     //   0	253	2	options	Ljava/util/Set<+Ljava/nio/file/OpenOption;>;
/*     */     //   0	253	3	attrs	[Ljava/nio/file/attribute/FileAttribute<*>;
/*     */     //   0	253	4	channelFactory	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSProvider$ChannelFactory<TC;>;
/*     */   }
/*     */   
/*     */   private static boolean wantsWrite(Set<? extends OpenOption> options) {
/* 125 */     return (options.contains(StandardOpenOption.WRITE) || (
/* 126 */       !options.contains(StandardOpenOption.READ) && options.contains(StandardOpenOption.APPEND)));
/*     */   }
/*     */   
/*     */   public synchronized DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
/* 131 */     CopyOnWriteFSPath cowPath = CopyOnWriteFSPath.asCow(dir);
/* 132 */     DirectoryNode directoryNode = this.fs.fileTree().directoryByPath(cowPath);
/* 133 */     final List<Path> result = new ArrayList<>();
/* 134 */     for (Node childNode : directoryNode.children()) {
/* 135 */       Path path = childNode.path();
/* 136 */       if (filter.accept(path))
/* 137 */         result.add(path); 
/*     */     } 
/* 141 */     return new DirectoryStream<Path>(this) {
/*     */         public void close() {}
/*     */         
/*     */         public Iterator<Path> iterator() {
/* 148 */           return result.iterator();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public synchronized void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
/* 155 */     CopyOnWriteFSPath cowPath = CopyOnWriteFSPath.asCow(dir);
/* 156 */     CopyOnWriteFSPath parent = cowPath.getParent();
/* 157 */     if (parent == null)
/* 158 */       throw new CowFSFileAlreadyExistsException(cowPath.toString()); 
/* 160 */     DirectoryNode parentFolder = this.fs.fileTree().directoryByPath(parent);
/* 161 */     String folderName = ((CopyOnWriteFSPath)Objects.<CopyOnWriteFSPath>requireNonNull(cowPath.getFileName())).toString();
/* 162 */     if (parentFolder.getChild(folderName) != null)
/* 163 */       throw new CowFSFileAlreadyExistsException(cowPath.toString()); 
/* 165 */     parentFolder.addChild(new DirectoryNode(cowPath));
/*     */   }
/*     */   
/*     */   public synchronized void delete(Path path) throws IOException {
/* 170 */     CopyOnWriteFSPath cowPath = CopyOnWriteFSPath.asCow(path);
/* 171 */     Node node = this.fs.fileTree().byPath(cowPath);
/* 172 */     if (node.parent == null)
/* 173 */       throw new CowFSFileSystemException("Can't remove root"); 
/* 175 */     String name = Objects.<String>requireNonNull(node.name());
/* 176 */     if (node instanceof DirectoryNode) {
/* 176 */       DirectoryNode directoryNode = (DirectoryNode)node;
/* 177 */       if (!directoryNode.children().isEmpty())
/* 178 */         throw new CowFSDirectoryNotEmptyException(cowPath.toString()); 
/* 180 */     } else if (node instanceof FileNode) {
/* 180 */       FileNode fileNode = (FileNode)node;
/* 181 */       fileNode.deleteCopy();
/*     */     } 
/* 183 */     node.parent.removeChild(name);
/*     */   }
/*     */   
/*     */   public void copy(Path source, Path target, CopyOption... options) throws IOException {
/* 188 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public synchronized void move(Path source, Path target, CopyOption... options) throws IOException {
/* 193 */     CopyOnWriteFSPath sourceCow = CopyOnWriteFSPath.asCow(source);
/* 194 */     CopyOnWriteFSPath targetCow = CopyOnWriteFSPath.asCow(target);
/* 195 */     if (sourceCow.isRoot())
/* 196 */       throw new CowFSFileSystemException(String.valueOf(sourceCow) + ": can't move root directory"); 
/* 198 */     boolean replaceExisting = false;
/* 199 */     for (CopyOption option : options) {
/* 200 */       if (option.equals(StandardCopyOption.ATOMIC_MOVE))
/* 201 */         throw new AtomicMoveNotSupportedException(sourceCow.toString(), targetCow.toString(), "CowFs does not support atomic move"); 
/* 202 */       if (option.equals(StandardCopyOption.REPLACE_EXISTING))
/* 203 */         replaceExisting = true; 
/*     */     } 
/* 206 */     Node sourceNode = this.fs.fileTree().byPathOrNull(sourceCow);
/* 207 */     if (sourceNode == null)
/* 208 */       throw new CowFSNoSuchFileException(sourceCow.toString()); 
/* 210 */     CopyOnWriteFSPath parent = targetCow.toAbsolutePath().getParent();
/* 211 */     if (parent == null)
/* 213 */       throw new CowFSFileAlreadyExistsException(targetCow.toString()); 
/* 215 */     Node targetParentNode = this.fs.fileTree().byPathOrNull(parent);
/* 216 */     if (targetParentNode instanceof DirectoryNode) {
/* 216 */       DirectoryNode folderTarget = (DirectoryNode)targetParentNode;
/* 217 */       String newName = ((CopyOnWriteFSPath)Objects.<CopyOnWriteFSPath>requireNonNull(targetCow.getFileName())).toString();
/* 218 */       Node oldChild = folderTarget.getChild(newName);
/* 219 */       if (oldChild != null) {
/* 220 */         if (oldChild.equals(sourceNode))
/*     */           return; 
/* 224 */         if (replaceExisting) {
/* 225 */           folderTarget.removeChild(newName);
/*     */         } else {
/* 227 */           throw new CowFSFileAlreadyExistsException(targetCow.toString());
/*     */         } 
/*     */       } 
/* 230 */       ((DirectoryNode)Objects.<DirectoryNode>requireNonNull(sourceNode.parent)).removeChild(Objects.<String>requireNonNull(sourceNode.name()));
/* 231 */       sourceNode.setPath(targetCow);
/* 232 */       folderTarget.addChild(sourceNode);
/*     */       return;
/*     */     } 
/* 235 */     throw new CowFSNoSuchFileException(targetCow.toString());
/*     */   }
/*     */   
/*     */   public boolean isSameFile(Path path, Path path2) {
/* 240 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isHidden(Path path) {
/* 245 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public FileStore getFileStore(Path path) {
/* 250 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public synchronized void checkAccess(Path path, AccessMode... modes) throws IOException {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokestatic asCow : (Ljava/nio/file/Path;)Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;
/*     */     //   4: astore_3
/*     */     //   5: aload_0
/*     */     //   6: getfield fs : Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem;
/*     */     //   9: invokevirtual fileTree : ()Lnet/minecraft/util/filefix/virtualfilesystem/DirectoryNode;
/*     */     //   12: aload_3
/*     */     //   13: invokevirtual byPath : (Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;)Lnet/minecraft/util/filefix/virtualfilesystem/Node;
/*     */     //   16: astore #4
/*     */     //   18: aload #4
/*     */     //   20: dup
/*     */     //   21: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   24: pop
/*     */     //   25: astore #6
/*     */     //   27: iconst_0
/*     */     //   28: istore #7
/*     */     //   30: aload #6
/*     */     //   32: iload #7
/*     */     //   34: <illegal opcode> typeSwitch : (Lnet/minecraft/util/filefix/virtualfilesystem/Node;I)I
/*     */     //   39: lookupswitch default -> 64, 0 -> 74, 1 -> 84
/*     */     //   64: new java/lang/MatchException
/*     */     //   67: dup
/*     */     //   68: aconst_null
/*     */     //   69: aconst_null
/*     */     //   70: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   73: athrow
/*     */     //   74: aload_0
/*     */     //   75: getfield fs : Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem;
/*     */     //   78: invokevirtual tmpDirectory : ()Ljava/nio/file/Path;
/*     */     //   81: goto -> 96
/*     */     //   84: aload #6
/*     */     //   86: checkcast net/minecraft/util/filefix/virtualfilesystem/FileNode
/*     */     //   89: astore #8
/*     */     //   91: aload #8
/*     */     //   93: invokevirtual storagePath : ()Ljava/nio/file/Path;
/*     */     //   96: astore #5
/*     */     //   98: aload #5
/*     */     //   100: invokeinterface getFileSystem : ()Ljava/nio/file/FileSystem;
/*     */     //   105: invokevirtual provider : ()Ljava/nio/file/spi/FileSystemProvider;
/*     */     //   108: aload #5
/*     */     //   110: aload_2
/*     */     //   111: invokevirtual checkAccess : (Ljava/nio/file/Path;[Ljava/nio/file/AccessMode;)V
/*     */     //   114: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #255	-> 0
/*     */     //   #256	-> 5
/*     */     //   #257	-> 18
/*     */     //   #259	-> 74
/*     */     //   #260	-> 84
/*     */     //   #261	-> 96
/*     */     //   #262	-> 98
/*     */     //   #263	-> 114
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   91	5	8	file	Lnet/minecraft/util/filefix/virtualfilesystem/FileNode;
/*     */     //   0	115	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSProvider;
/*     */     //   0	115	1	path	Ljava/nio/file/Path;
/*     */     //   0	115	2	modes	[Ljava/nio/file/AccessMode;
/*     */     //   5	110	3	cowPath	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;
/*     */     //   18	97	4	node	Lnet/minecraft/util/filefix/virtualfilesystem/Node;
/*     */     //   98	17	5	checkPath	Ljava/nio/file/Path;
/*     */   }
/*     */   
/*     */   public synchronized <V extends java.nio.file.attribute.FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokestatic asCow : (Ljava/nio/file/Path;)Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;
/*     */     //   4: astore #4
/*     */     //   6: aload_0
/*     */     //   7: getfield fs : Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem;
/*     */     //   10: invokevirtual fileTree : ()Lnet/minecraft/util/filefix/virtualfilesystem/DirectoryNode;
/*     */     //   13: aload #4
/*     */     //   15: invokevirtual byPathOrNull : (Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;)Lnet/minecraft/util/filefix/virtualfilesystem/Node;
/*     */     //   18: astore #5
/*     */     //   20: aload #5
/*     */     //   22: astore #6
/*     */     //   24: iconst_0
/*     */     //   25: istore #7
/*     */     //   27: aload #6
/*     */     //   29: iload #7
/*     */     //   31: <illegal opcode> typeSwitch : (Lnet/minecraft/util/filefix/virtualfilesystem/Node;I)I
/*     */     //   36: tableswitch default -> 64, -1 -> 111, 0 -> 74, 1 -> 91
/*     */     //   64: new java/lang/MatchException
/*     */     //   67: dup
/*     */     //   68: aconst_null
/*     */     //   69: aconst_null
/*     */     //   70: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   73: athrow
/*     */     //   74: aload_2
/*     */     //   75: ldc_w java/nio/file/attribute/BasicFileAttributeView
/*     */     //   78: if_acmpne -> 87
/*     */     //   81: getstatic net/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSProvider.DUMMY_DIRECTORY_VIEW : Ljava/nio/file/attribute/BasicFileAttributeView;
/*     */     //   84: goto -> 132
/*     */     //   87: aconst_null
/*     */     //   88: goto -> 132
/*     */     //   91: aload #6
/*     */     //   93: checkcast net/minecraft/util/filefix/virtualfilesystem/FileNode
/*     */     //   96: astore #8
/*     */     //   98: aload #8
/*     */     //   100: invokevirtual storagePath : ()Ljava/nio/file/Path;
/*     */     //   103: aload_2
/*     */     //   104: aload_3
/*     */     //   105: invokestatic getFileAttributeView : (Ljava/nio/file/Path;Ljava/lang/Class;[Ljava/nio/file/LinkOption;)Ljava/nio/file/attribute/FileAttributeView;
/*     */     //   108: goto -> 132
/*     */     //   111: aload_2
/*     */     //   112: ldc_w java/nio/file/attribute/BasicFileAttributeView
/*     */     //   115: if_acmpne -> 131
/*     */     //   118: new net/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSProvider$3
/*     */     //   121: dup
/*     */     //   122: aload_0
/*     */     //   123: aload #4
/*     */     //   125: invokespecial <init> : (Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSProvider;Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;)V
/*     */     //   128: goto -> 132
/*     */     //   131: aconst_null
/*     */     //   132: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #268	-> 0
/*     */     //   #269	-> 6
/*     */     //   #270	-> 20
/*     */     //   #272	-> 74
/*     */     //   #273	-> 81
/*     */     //   #275	-> 87
/*     */     //   #277	-> 91
/*     */     //   #279	-> 111
/*     */     //   #280	-> 118
/*     */     //   #297	-> 131
/*     */     //   #270	-> 132
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   98	13	8	file	Lnet/minecraft/util/filefix/virtualfilesystem/FileNode;
/*     */     //   0	133	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSProvider;
/*     */     //   0	133	1	path	Ljava/nio/file/Path;
/*     */     //   0	133	2	type	Ljava/lang/Class;
/*     */     //   0	133	3	options	[Ljava/nio/file/LinkOption;
/*     */     //   6	127	4	cowPath	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;
/*     */     //   20	113	5	node	Lnet/minecraft/util/filefix/virtualfilesystem/Node;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	133	2	type	Ljava/lang/Class<TV;>;
/*     */   }
/*     */   
/*     */   public synchronized <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokestatic asCow : (Ljava/nio/file/Path;)Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;
/*     */     //   4: astore #4
/*     */     //   6: aload_0
/*     */     //   7: getfield fs : Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFileSystem;
/*     */     //   10: invokevirtual fileTree : ()Lnet/minecraft/util/filefix/virtualfilesystem/DirectoryNode;
/*     */     //   13: aload #4
/*     */     //   15: invokevirtual byPath : (Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;)Lnet/minecraft/util/filefix/virtualfilesystem/Node;
/*     */     //   18: astore #5
/*     */     //   20: aload #5
/*     */     //   22: dup
/*     */     //   23: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   26: pop
/*     */     //   27: astore #6
/*     */     //   29: iconst_0
/*     */     //   30: istore #7
/*     */     //   32: aload #6
/*     */     //   34: iload #7
/*     */     //   36: <illegal opcode> typeSwitch : (Lnet/minecraft/util/filefix/virtualfilesystem/Node;I)I
/*     */     //   41: lookupswitch default -> 68, 0 -> 78, 1 -> 84
/*     */     //   68: new java/lang/MatchException
/*     */     //   71: dup
/*     */     //   72: aconst_null
/*     */     //   73: aconst_null
/*     */     //   74: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   77: athrow
/*     */     //   78: getstatic net/minecraft/util/DummyFileAttributes.DIRECTORY : Ljava/nio/file/attribute/BasicFileAttributes;
/*     */     //   81: goto -> 101
/*     */     //   84: aload #6
/*     */     //   86: checkcast net/minecraft/util/filefix/virtualfilesystem/FileNode
/*     */     //   89: astore #8
/*     */     //   91: aload #8
/*     */     //   93: invokevirtual storagePath : ()Ljava/nio/file/Path;
/*     */     //   96: aload_2
/*     */     //   97: aload_3
/*     */     //   98: invokestatic readAttributes : (Ljava/nio/file/Path;Ljava/lang/Class;[Ljava/nio/file/LinkOption;)Ljava/nio/file/attribute/BasicFileAttributes;
/*     */     //   101: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #305	-> 0
/*     */     //   #306	-> 6
/*     */     //   #307	-> 20
/*     */     //   #308	-> 78
/*     */     //   #309	-> 84
/*     */     //   #307	-> 101
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   91	10	8	file	Lnet/minecraft/util/filefix/virtualfilesystem/FileNode;
/*     */     //   0	102	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSProvider;
/*     */     //   0	102	1	path	Ljava/nio/file/Path;
/*     */     //   0	102	2	type	Ljava/lang/Class;
/*     */     //   0	102	3	options	[Ljava/nio/file/LinkOption;
/*     */     //   6	96	4	cowPath	Lnet/minecraft/util/filefix/virtualfilesystem/CopyOnWriteFSPath;
/*     */     //   20	82	5	node	Lnet/minecraft/util/filefix/virtualfilesystem/Node;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	102	2	type	Ljava/lang/Class<TA;>;
/*     */   }
/*     */   
/*     */   public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) {
/* 315 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void setAttribute(Path path, String attribute, Object value, LinkOption... options) {
/* 320 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public synchronized CopyOnWriteFSPath getRealPath(CopyOnWriteFSPath path) throws CowFSNoSuchFileException {
/* 324 */     return (this.fs.fileTree().byPath(path.toAbsolutePath())).path;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface ChannelFactory<C> {
/*     */     C newChannel(Path param1Path, Set<? extends OpenOption> param1Set, FileAttribute<?>... param1VarArgs) throws IOException;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\virtualfilesystem\CopyOnWriteFSProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */