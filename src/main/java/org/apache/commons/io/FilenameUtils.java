package org.apache.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class FilenameUtils {
   private static final int NOT_FOUND = -1;
   public static final char EXTENSION_SEPARATOR = '.';
   public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
   private static final char UNIX_SEPARATOR = '/';
   private static final char WINDOWS_SEPARATOR = '\\';
   private static final char SYSTEM_SEPARATOR = File.separatorChar;
   private static final char OTHER_SEPARATOR;

   static boolean isSystemWindows() {
      return SYSTEM_SEPARATOR == '\\';
   }

   private static boolean isSeparator(char ch) {
      return ch == '/' || ch == '\\';
   }

   public static String normalize(String filename) {
      return doNormalize(filename, SYSTEM_SEPARATOR, true);
   }

   public static String normalize(String filename, boolean unixSeparator) {
      char separator = (char)(unixSeparator ? 47 : 92);
      return doNormalize(filename, separator, true);
   }

   public static String normalizeNoEndSeparator(String filename) {
      return doNormalize(filename, SYSTEM_SEPARATOR, false);
   }

   public static String normalizeNoEndSeparator(String filename, boolean unixSeparator) {
      char separator = (char)(unixSeparator ? 47 : 92);
      return doNormalize(filename, separator, false);
   }

   private static String doNormalize(String filename, char separator, boolean keepSeparator) {
      if (filename == null) {
         return null;
      } else {
         failIfNullBytePresent(filename);
         int size = filename.length();
         if (size == 0) {
            return filename;
         } else {
            int prefix = getPrefixLength(filename);
            if (prefix < 0) {
               return null;
            } else {
               char[] array = new char[size + 2];
               filename.getChars(0, filename.length(), array, 0);
               char otherSeparator = separator == SYSTEM_SEPARATOR ? OTHER_SEPARATOR : SYSTEM_SEPARATOR;

               for (int i = 0; i < array.length; i++) {
                  if (array[i] == otherSeparator) {
                     array[i] = separator;
                  }
               }

               boolean lastIsDirectory = true;
               if (array[size - 1] != separator) {
                  array[size++] = separator;
                  lastIsDirectory = false;
               }

               for (int ix = prefix + 1; ix < size; ix++) {
                  if (array[ix] == separator && array[ix - 1] == separator) {
                     System.arraycopy(array, ix, array, ix - 1, size - ix);
                     size--;
                     ix--;
                  }
               }

               for (int ixx = prefix + 1; ixx < size; ixx++) {
                  if (array[ixx] == separator && array[ixx - 1] == '.' && (ixx == prefix + 1 || array[ixx - 2] == separator)) {
                     if (ixx == size - 1) {
                        lastIsDirectory = true;
                     }

                     System.arraycopy(array, ixx + 1, array, ixx - 1, size - ixx);
                     size -= 2;
                     ixx--;
                  }
               }

               label109:
               for (int ixxx = prefix + 2; ixxx < size; ixxx++) {
                  if (array[ixxx] == separator && array[ixxx - 1] == '.' && array[ixxx - 2] == '.' && (ixxx == prefix + 2 || array[ixxx - 3] == separator)) {
                     if (ixxx == prefix + 2) {
                        return null;
                     }

                     if (ixxx == size - 1) {
                        lastIsDirectory = true;
                     }

                     for (int j = ixxx - 4; j >= prefix; j--) {
                        if (array[j] == separator) {
                           System.arraycopy(array, ixxx + 1, array, j + 1, size - ixxx);
                           size -= ixxx - j;
                           ixxx = j + 1;
                           continue label109;
                        }
                     }

                     System.arraycopy(array, ixxx + 1, array, prefix, size - ixxx);
                     size -= ixxx + 1 - prefix;
                     ixxx = prefix + 1;
                  }
               }

               if (size <= 0) {
                  return "";
               } else if (size <= prefix) {
                  return new String(array, 0, size);
               } else {
                  return lastIsDirectory && keepSeparator ? new String(array, 0, size) : new String(array, 0, size - 1);
               }
            }
         }
      }
   }

   public static String concat(String basePath, String fullFilenameToAdd) {
      int prefix = getPrefixLength(fullFilenameToAdd);
      if (prefix < 0) {
         return null;
      } else if (prefix > 0) {
         return normalize(fullFilenameToAdd);
      } else if (basePath == null) {
         return null;
      } else {
         int len = basePath.length();
         if (len == 0) {
            return normalize(fullFilenameToAdd);
         } else {
            char ch = basePath.charAt(len - 1);
            return isSeparator(ch) ? normalize(basePath + fullFilenameToAdd) : normalize(basePath + '/' + fullFilenameToAdd);
         }
      }
   }

   public static boolean directoryContains(String canonicalParent, String canonicalChild) throws IOException {
      if (canonicalParent == null) {
         throw new IllegalArgumentException("Directory must not be null");
      } else if (canonicalChild == null) {
         return false;
      } else {
         return IOCase.SYSTEM.checkEquals(canonicalParent, canonicalChild) ? false : IOCase.SYSTEM.checkStartsWith(canonicalChild, canonicalParent);
      }
   }

   public static String separatorsToUnix(String path) {
      return path != null && path.indexOf(92) != -1 ? path.replace('\\', '/') : path;
   }

   public static String separatorsToWindows(String path) {
      return path != null && path.indexOf(47) != -1 ? path.replace('/', '\\') : path;
   }

   public static String separatorsToSystem(String path) {
      if (path == null) {
         return null;
      } else {
         return isSystemWindows() ? separatorsToWindows(path) : separatorsToUnix(path);
      }
   }

   public static int getPrefixLength(String filename) {
      if (filename == null) {
         return -1;
      } else {
         int len = filename.length();
         if (len == 0) {
            return 0;
         } else {
            char ch0 = filename.charAt(0);
            if (ch0 == ':') {
               return -1;
            } else if (len == 1) {
               if (ch0 == '~') {
                  return 2;
               } else {
                  return isSeparator(ch0) ? 1 : 0;
               }
            } else if (ch0 == '~') {
               int posUnix = filename.indexOf(47, 1);
               int posWin = filename.indexOf(92, 1);
               if (posUnix == -1 && posWin == -1) {
                  return len + 1;
               } else {
                  posUnix = posUnix == -1 ? posWin : posUnix;
                  posWin = posWin == -1 ? posUnix : posWin;
                  return Math.min(posUnix, posWin) + 1;
               }
            } else {
               char ch1 = filename.charAt(1);
               if (ch1 == ':') {
                  ch0 = Character.toUpperCase(ch0);
                  if (ch0 < 'A' || ch0 > 'Z') {
                     return -1;
                  } else {
                     return len != 2 && isSeparator(filename.charAt(2)) ? 3 : 2;
                  }
               } else if (isSeparator(ch0) && isSeparator(ch1)) {
                  int posUnix = filename.indexOf(47, 2);
                  int posWin = filename.indexOf(92, 2);
                  if ((posUnix != -1 || posWin != -1) && posUnix != 2 && posWin != 2) {
                     posUnix = posUnix == -1 ? posWin : posUnix;
                     posWin = posWin == -1 ? posUnix : posWin;
                     return Math.min(posUnix, posWin) + 1;
                  } else {
                     return -1;
                  }
               } else {
                  return isSeparator(ch0) ? 1 : 0;
               }
            }
         }
      }
   }

   public static int indexOfLastSeparator(String filename) {
      if (filename == null) {
         return -1;
      } else {
         int lastUnixPos = filename.lastIndexOf(47);
         int lastWindowsPos = filename.lastIndexOf(92);
         return Math.max(lastUnixPos, lastWindowsPos);
      }
   }

   public static int indexOfExtension(String filename) {
      if (filename == null) {
         return -1;
      } else {
         int extensionPos = filename.lastIndexOf(46);
         int lastSeparator = indexOfLastSeparator(filename);
         return lastSeparator > extensionPos ? -1 : extensionPos;
      }
   }

   public static String getPrefix(String filename) {
      if (filename == null) {
         return null;
      } else {
         int len = getPrefixLength(filename);
         if (len < 0) {
            return null;
         } else if (len > filename.length()) {
            failIfNullBytePresent(filename + '/');
            return filename + '/';
         } else {
            String path = filename.substring(0, len);
            failIfNullBytePresent(path);
            return path;
         }
      }
   }

   public static String getPath(String filename) {
      return doGetPath(filename, 1);
   }

   public static String getPathNoEndSeparator(String filename) {
      return doGetPath(filename, 0);
   }

   private static String doGetPath(String filename, int separatorAdd) {
      if (filename == null) {
         return null;
      } else {
         int prefix = getPrefixLength(filename);
         if (prefix < 0) {
            return null;
         } else {
            int index = indexOfLastSeparator(filename);
            int endIndex = index + separatorAdd;
            if (prefix < filename.length() && index >= 0 && prefix < endIndex) {
               String path = filename.substring(prefix, endIndex);
               failIfNullBytePresent(path);
               return path;
            } else {
               return "";
            }
         }
      }
   }

   public static String getFullPath(String filename) {
      return doGetFullPath(filename, true);
   }

   public static String getFullPathNoEndSeparator(String filename) {
      return doGetFullPath(filename, false);
   }

   private static String doGetFullPath(String filename, boolean includeSeparator) {
      if (filename == null) {
         return null;
      } else {
         int prefix = getPrefixLength(filename);
         if (prefix < 0) {
            return null;
         } else if (prefix >= filename.length()) {
            return includeSeparator ? getPrefix(filename) : filename;
         } else {
            int index = indexOfLastSeparator(filename);
            if (index < 0) {
               return filename.substring(0, prefix);
            } else {
               int end = index + (includeSeparator ? 1 : 0);
               if (end == 0) {
                  end++;
               }

               return filename.substring(0, end);
            }
         }
      }
   }

   public static String getName(String filename) {
      if (filename == null) {
         return null;
      } else {
         failIfNullBytePresent(filename);
         int index = indexOfLastSeparator(filename);
         return filename.substring(index + 1);
      }
   }

   private static void failIfNullBytePresent(String path) {
      int len = path.length();

      for (int i = 0; i < len; i++) {
         if (path.charAt(i) == 0) {
            throw new IllegalArgumentException(
               "Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it"
            );
         }
      }
   }

   public static String getBaseName(String filename) {
      return removeExtension(getName(filename));
   }

   public static String getExtension(String filename) {
      if (filename == null) {
         return null;
      } else {
         int index = indexOfExtension(filename);
         return index == -1 ? "" : filename.substring(index + 1);
      }
   }

   public static String removeExtension(String filename) {
      if (filename == null) {
         return null;
      } else {
         failIfNullBytePresent(filename);
         int index = indexOfExtension(filename);
         return index == -1 ? filename : filename.substring(0, index);
      }
   }

   public static boolean equals(String filename1, String filename2) {
      return equals(filename1, filename2, false, IOCase.SENSITIVE);
   }

   public static boolean equalsOnSystem(String filename1, String filename2) {
      return equals(filename1, filename2, false, IOCase.SYSTEM);
   }

   public static boolean equalsNormalized(String filename1, String filename2) {
      return equals(filename1, filename2, true, IOCase.SENSITIVE);
   }

   public static boolean equalsNormalizedOnSystem(String filename1, String filename2) {
      return equals(filename1, filename2, true, IOCase.SYSTEM);
   }

   public static boolean equals(String filename1, String filename2, boolean normalized, IOCase caseSensitivity) {
      if (filename1 != null && filename2 != null) {
         if (normalized) {
            filename1 = normalize(filename1);
            filename2 = normalize(filename2);
            if (filename1 == null || filename2 == null) {
               throw new NullPointerException("Error normalizing one or both of the file names");
            }
         }

         if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
         }

         return caseSensitivity.checkEquals(filename1, filename2);
      } else {
         return filename1 == null && filename2 == null;
      }
   }

   public static boolean isExtension(String filename, String extension) {
      if (filename == null) {
         return false;
      } else {
         failIfNullBytePresent(filename);
         if (extension != null && !extension.isEmpty()) {
            String fileExt = getExtension(filename);
            return fileExt.equals(extension);
         } else {
            return indexOfExtension(filename) == -1;
         }
      }
   }

   public static boolean isExtension(String filename, String[] extensions) {
      if (filename == null) {
         return false;
      } else {
         failIfNullBytePresent(filename);
         if (extensions != null && extensions.length != 0) {
            String fileExt = getExtension(filename);

            for (String extension : extensions) {
               if (fileExt.equals(extension)) {
                  return true;
               }
            }

            return false;
         } else {
            return indexOfExtension(filename) == -1;
         }
      }
   }

   public static boolean isExtension(String filename, Collection<String> extensions) {
      if (filename == null) {
         return false;
      } else {
         failIfNullBytePresent(filename);
         if (extensions != null && !extensions.isEmpty()) {
            String fileExt = getExtension(filename);

            for (String extension : extensions) {
               if (fileExt.equals(extension)) {
                  return true;
               }
            }

            return false;
         } else {
            return indexOfExtension(filename) == -1;
         }
      }
   }

   public static boolean wildcardMatch(String filename, String wildcardMatcher) {
      return wildcardMatch(filename, wildcardMatcher, IOCase.SENSITIVE);
   }

   public static boolean wildcardMatchOnSystem(String filename, String wildcardMatcher) {
      return wildcardMatch(filename, wildcardMatcher, IOCase.SYSTEM);
   }

   public static boolean wildcardMatch(String filename, String wildcardMatcher, IOCase caseSensitivity) {
      if (filename == null && wildcardMatcher == null) {
         return true;
      } else if (filename != null && wildcardMatcher != null) {
         if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
         }

         String[] wcs = splitOnTokens(wildcardMatcher);
         boolean anyChars = false;
         int textIdx = 0;
         int wcsIdx = 0;
         Stack<int[]> backtrack = new Stack<>();

         do {
            if (backtrack.size() > 0) {
               int[] array = backtrack.pop();
               wcsIdx = array[0];
               textIdx = array[1];
               anyChars = true;
            }

            for (; wcsIdx < wcs.length; wcsIdx++) {
               if (wcs[wcsIdx].equals("?")) {
                  if (++textIdx > filename.length()) {
                     break;
                  }

                  anyChars = false;
               } else if (wcs[wcsIdx].equals("*")) {
                  anyChars = true;
                  if (wcsIdx == wcs.length - 1) {
                     textIdx = filename.length();
                  }
               } else {
                  if (anyChars) {
                     textIdx = caseSensitivity.checkIndexOf(filename, textIdx, wcs[wcsIdx]);
                     if (textIdx == -1) {
                        break;
                     }

                     int repeat = caseSensitivity.checkIndexOf(filename, textIdx + 1, wcs[wcsIdx]);
                     if (repeat >= 0) {
                        backtrack.push(new int[]{wcsIdx, repeat});
                     }
                  } else if (!caseSensitivity.checkRegionMatches(filename, textIdx, wcs[wcsIdx])) {
                     break;
                  }

                  textIdx += wcs[wcsIdx].length();
                  anyChars = false;
               }
            }

            if (wcsIdx == wcs.length && textIdx == filename.length()) {
               return true;
            }
         } while (backtrack.size() > 0);

         return false;
      } else {
         return false;
      }
   }

   static String[] splitOnTokens(String text) {
      if (text.indexOf(63) == -1 && text.indexOf(42) == -1) {
         return new String[]{text};
      } else {
         char[] array = text.toCharArray();
         ArrayList<String> list = new ArrayList<>();
         StringBuilder buffer = new StringBuilder();
         char prevChar = 0;

         for (char ch : array) {
            if (ch != '?' && ch != '*') {
               buffer.append(ch);
            } else {
               if (buffer.length() != 0) {
                  list.add(buffer.toString());
                  buffer.setLength(0);
               }

               if (ch == '?') {
                  list.add("?");
               } else if (prevChar != '*') {
                  list.add("*");
               }
            }

            prevChar = ch;
         }

         if (buffer.length() != 0) {
            list.add(buffer.toString());
         }

         return list.toArray(new String[list.size()]);
      }
   }

   static {
      if (isSystemWindows()) {
         OTHER_SEPARATOR = '/';
      } else {
         OTHER_SEPARATOR = '\\';
      }
   }
}