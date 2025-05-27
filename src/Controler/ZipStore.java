package Controler;

import java.io.*;
import java.util.zip.*;

public class ZipStore {

    public static void serializeToZip(String zipFilePath, String entryName, Serializable object) throws IOException {
        byte[] data = serialize(object);
        storeInZip(zipFilePath, entryName, data);
    }

    public static Object deserializeFromZip(String zipFilePath, String entryName) throws IOException, ClassNotFoundException {
        byte[] data = loadFromZip(zipFilePath, entryName);
        return deserialize(data);
    }

    private static byte[] serialize(Serializable obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }

    private static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        }
    }

    private static void storeInZip(String zipFilePath, String entryName, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            ZipEntry entry = new ZipEntry(entryName);
            zos.putNextEntry(entry);
            zos.write(data);
            zos.closeEntry();
        }
    }

    private static byte[] loadFromZip(String zipFilePath, String entryName) throws IOException {
        try (FileInputStream fis = new FileInputStream(zipFilePath);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals(entryName)) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        baos.write(buffer, 0, len);
                    }
                    return baos.toByteArray();
                }
            }
        }
        throw new FileNotFoundException("Entry " + entryName + " not found in ZIP.");
    }
}
