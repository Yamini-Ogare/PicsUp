package yamini.picsup.Other;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZip {
    public static abstract class zipper{

        public static final byte[] gzipCompress(byte[] uncompressedData) {
            byte[] result = new byte[]{};
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompressedData.length);
                 GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
                gzipOS.write(uncompressedData);
                gzipOS.close();
                result = bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }


        public static final byte[] gzipUncompress(byte[] compressedData) {
            byte[] result = new byte[]{};
            try (ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 GZIPInputStream gzipIS = new GZIPInputStream(bis)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = gzipIS.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                result = bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

    }
}
